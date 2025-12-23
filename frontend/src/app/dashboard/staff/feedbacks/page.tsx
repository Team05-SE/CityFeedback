import { useEffect, useState } from "react"
import {
  MessageSquare,
  RefreshCw,
  CheckCircle2,
  AlertCircle,
  Clock,
  Eye,
  EyeOff,
  Trash2,
  MessageCircle,
  ChevronUp,
} from "lucide-react"

import { Card, CardContent, CardDescription, CardHeader, CardTitle } from "@/components/ui/card"
import { Button } from "@/components/ui/button"
import { Badge } from "@/components/ui/badge"
import {
  Table,
  TableBody,
  TableCell,
  TableHead,
  TableHeader,
  TableRow,
} from "@/components/ui/table"
import {
  Select,
  SelectContent,
  SelectItem,
  SelectTrigger,
  SelectValue,
} from "@/components/ui/select"
import {
  Dialog,
  DialogContent,
  DialogDescription,
  DialogFooter,
  DialogHeader,
  DialogTitle,
  DialogTrigger,
} from "@/components/ui/dialog"
import { Textarea } from "@/components/ui/textarea"
import { Label } from "@/components/ui/label"
import {
  getAllFeedbacks,
  updateFeedbackStatus,
  publishFeedback,
  unpublishFeedback,
  deleteFeedback,
  getCommentsByFeedbackId,
  addComment,
  categoryLabels,
  statusConfig,
  getStoredUser,
  type Feedback,
  type Status,
  type Comment,
} from "@/lib/api"

export default function StaffFeedbacksPage() {
  const [feedbacks, setFeedbacks] = useState<Feedback[]>([])
  const [loading, setLoading] = useState(true)
  const [error, setError] = useState<string | null>(null)
  const [selectedStatus, setSelectedStatus] = useState<Record<number, Status>>({})
  const [deleteDialogOpen, setDeleteDialogOpen] = useState<number | null>(null)
  const [expandedFeedback, setExpandedFeedback] = useState<number | null>(null)
  const [comments, setComments] = useState<Record<number, Comment[]>>({})
  const [commentTexts, setCommentTexts] = useState<Record<number, string>>({})
  const [statusChangeComment, setStatusChangeComment] = useState<Record<number, string>>({})
  const [statusChangeDialogOpen, setStatusChangeDialogOpen] = useState<number | null>(null)
  const [pendingStatusChange, setPendingStatusChange] = useState<Record<number, Status>>({})

  const currentUser = getStoredUser()

  // Prüfen ob User Mitarbeiter oder Admin ist
  useEffect(() => {
    if (!currentUser || (currentUser.role !== "STAFF" && currentUser.role !== "ADMIN")) {
      window.location.href = "/dashboard"
    }
  }, [currentUser])

  const loadFeedbacks = async () => {
    setLoading(true)
    setError(null)
    try {
      const data = await getAllFeedbacks()
      setFeedbacks(data)
      // Initialisiere Status-Auswahl
      const statusMap: Record<number, Status> = {}
      data.forEach((f) => {
        statusMap[f.id] = f.status as Status
      })
      setSelectedStatus(statusMap)
      
      // Kommentare für alle Feedbacks parallel laden
      const commentsPromises = data.map(async (feedback) => {
        try {
          const feedbackComments = await getCommentsByFeedbackId(feedback.id)
          return { feedbackId: feedback.id, comments: feedbackComments }
        } catch (err) {
          console.error(`Kommentare für Feedback ${feedback.id} konnten nicht geladen werden:`, err)
          return { feedbackId: feedback.id, comments: [] }
        }
      })
      
      const commentsResults = await Promise.all(commentsPromises)
      const commentsMap: Record<number, Comment[]> = {}
      commentsResults.forEach((result) => {
        commentsMap[result.feedbackId] = result.comments
      })
      setComments(commentsMap)
    } catch (err) {
      setError("Feedbacks konnten nicht geladen werden. Ist das Backend erreichbar?")
    } finally {
      setLoading(false)
    }
  }

  useEffect(() => {
    loadFeedbacks()
  }, [])

  const loadComments = async (feedbackId: number) => {
    try {
      const feedbackComments = await getCommentsByFeedbackId(feedbackId)
      setComments((prev) => ({ ...prev, [feedbackId]: feedbackComments }))
    } catch (err) {
      console.error("Kommentare konnten nicht geladen werden:", err)
    }
  }

  const handleExpandFeedback = (feedbackId: number) => {
    if (expandedFeedback === feedbackId) {
      setExpandedFeedback(null)
    } else {
      setExpandedFeedback(feedbackId)
      if (!comments[feedbackId]) {
        loadComments(feedbackId)
      }
    }
  }

  const handleAddComment = async (feedbackId: number) => {
    const commentText = commentTexts[feedbackId]?.trim()
    if (!commentText) {
      setError("Bitte geben Sie einen Kommentar ein")
      return
    }

    try {
      await addComment(feedbackId, commentText)
      setCommentTexts((prev) => ({ ...prev, [feedbackId]: "" }))
      await loadComments(feedbackId)
    } catch (err: any) {
      setError(err.message || "Kommentar konnte nicht hinzugefügt werden")
    }
  }

  const handleStatusChangeWithComment = async (feedbackId: number, newStatus: Status) => {
    const commentText = statusChangeComment[feedbackId]?.trim()
    
    try {
      await updateFeedbackStatus(feedbackId, newStatus)
      setSelectedStatus((prev) => ({ ...prev, [feedbackId]: newStatus }))
      
      // Wenn ein Kommentar vorhanden ist, hinzufügen
      if (commentText) {
        try {
          await addComment(feedbackId, commentText)
        } catch (commentErr) {
          console.error("Kommentar konnte nicht hinzugefügt werden:", commentErr)
        }
      }
      
      setStatusChangeDialogOpen(null)
      setStatusChangeComment((prev) => ({ ...prev, [feedbackId]: "" }))
      await loadFeedbacks()
      if (expandedFeedback === feedbackId) {
        await loadComments(feedbackId)
      }
    } catch (err: any) {
      setError(err.message || "Status konnte nicht geändert werden")
    }
  }

  const handleStatusChange = (feedbackId: number, newStatus: Status) => {
    setPendingStatusChange((prev) => ({ ...prev, [feedbackId]: newStatus }))
    setStatusChangeDialogOpen(feedbackId)
  }

  const handlePublish = async (feedbackId: number) => {
    try {
      await publishFeedback(feedbackId)
      await loadFeedbacks()
    } catch (err: any) {
      setError(err.message || "Feedback konnte nicht veröffentlicht werden")
    }
  }

  const handleUnpublish = async (feedbackId: number) => {
    try {
      await unpublishFeedback(feedbackId)
      await loadFeedbacks()
    } catch (err: any) {
      setError(err.message || "Feedback konnte nicht aus der Veröffentlichung genommen werden")
    }
  }

  const handleDeleteFeedback = async (feedbackId: number) => {
    try {
      await deleteFeedback(feedbackId)
      setDeleteDialogOpen(null)
      await loadFeedbacks()
    } catch (err: any) {
      setError(err.message || "Feedback konnte nicht gelöscht werden")
    }
  }

  const formatDate = (dateString: string) => {
    try {
      return new Date(dateString).toLocaleDateString("de-DE", {
        day: "2-digit",
        month: "2-digit",
        year: "numeric",
      })
    } catch {
      return dateString
    }
  }

  // Statistiken
  const openCount = feedbacks.filter((f) => f.status === "OPEN").length
  const inProgressCount = feedbacks.filter((f) => f.status === "INPROGRESS").length
  const doneCount = feedbacks.filter((f) => f.status === "DONE").length
  const publishedCount = feedbacks.filter((f) => f.published).length

  const stats = [
    {
      title: "Offen",
      value: openCount,
      icon: AlertCircle,
      description: "Freigegeben",
      color: "text-amber-500",
    },
    {
      title: "In Bearbeitung",
      value: inProgressCount,
      icon: Clock,
      description: "Aktuell bearbeitet",
      color: "text-blue-500",
    },
    {
      title: "Erledigt",
      value: doneCount,
      icon: CheckCircle2,
      description: "Abgeschlossen",
      color: "text-emerald-500",
    },
    {
      title: "Veröffentlicht",
      value: publishedCount,
      icon: Eye,
      description: "Öffentlich sichtbar",
      color: "text-green-500",
    },
  ]

  return (
    <div className="space-y-8">
      {/* Header */}
      <div className="flex flex-col gap-4 sm:flex-row sm:items-center sm:justify-between">
        <div>
          <h1 className="text-3xl font-bold tracking-tight">Feedback-Verwaltung</h1>
          <p className="text-muted-foreground">
            Verwalten Sie alle Feedbacks und deren Status
          </p>
        </div>
        <Button variant="outline" size="sm" onClick={loadFeedbacks} disabled={loading}>
          <RefreshCw className={`mr-2 size-4 ${loading ? "animate-spin" : ""}`} />
          Aktualisieren
        </Button>
      </div>

      {/* Error Message */}
      {error && (
        <Card className="border-destructive">
          <CardContent className="pt-6">
            <div className="flex items-center gap-2 text-destructive">
              <AlertCircle className="size-4" />
              <p>{error}</p>
            </div>
          </CardContent>
        </Card>
      )}

      {/* Stats Cards */}
      <div className="grid gap-4 sm:grid-cols-2 lg:grid-cols-5">
        {stats.map((stat) => (
          <Card key={stat.title}>
            <CardHeader className="flex flex-row items-center justify-between pb-2">
              <CardTitle className="text-sm font-medium text-muted-foreground">
                {stat.title}
              </CardTitle>
              <stat.icon className={`size-5 ${stat.color}`} />
            </CardHeader>
            <CardContent>
              <div className="text-3xl font-bold">{stat.value}</div>
              <p className="text-xs text-muted-foreground">{stat.description}</p>
            </CardContent>
          </Card>
        ))}
      </div>

      {/* Feedbacks Table */}
      <Card>
        <CardHeader>
          <CardTitle>Alle Feedbacks</CardTitle>
          <CardDescription>
            Verwalten Sie den Status und die Veröffentlichung von Feedbacks
          </CardDescription>
        </CardHeader>
        <CardContent>
          {loading ? (
            <div className="flex items-center justify-center py-12">
              <RefreshCw className="size-8 animate-spin text-muted-foreground" />
            </div>
          ) : feedbacks.length === 0 ? (
            <div className="flex flex-col items-center justify-center py-12 text-center">
              <MessageSquare className="size-12 text-muted-foreground mb-4" />
              <p className="text-lg font-medium">Noch keine Feedbacks</p>
            </div>
          ) : (
            <div className="rounded-md border">
              <Table>
                <TableHeader>
                  <TableRow>
                    <TableHead>#</TableHead>
                    <TableHead>Titel</TableHead>
                    <TableHead>Kategorie</TableHead>
                    <TableHead>Status</TableHead>
                    <TableHead>Veröffentlicht</TableHead>
                    <TableHead>Datum</TableHead>
                    <TableHead className="text-right">Aktionen</TableHead>
                  </TableRow>
                </TableHeader>
                <TableBody>
                  {feedbacks.map((feedback) => {
                    const currentStatus = selectedStatus[feedback.id] || (feedback.status as Status)
                    const isExpanded = expandedFeedback === feedback.id
                    const feedbackComments = comments[feedback.id] || []
                    const pendingNewStatus = pendingStatusChange[feedback.id]
                    return (
                      <>
                        <TableRow key={feedback.id}>
                          <TableCell className="font-medium">{feedback.id}</TableCell>
                          <TableCell>
                            <div className="max-w-xs truncate font-medium">
                              {feedback.title}
                            </div>
                            <div className="max-w-xs truncate text-xs text-muted-foreground">
                              {feedback.content}
                            </div>
                          </TableCell>
                          <TableCell>
                            <Badge variant="outline">
                              {categoryLabels[feedback.category] || feedback.category}
                            </Badge>
                          </TableCell>
                          <TableCell>
                            <Select
                              value={currentStatus}
                              onValueChange={(value) => handleStatusChange(feedback.id, value as Status)}
                            >
                              <SelectTrigger className="w-40">
                                <SelectValue />
                              </SelectTrigger>
                              <SelectContent>
                                <SelectItem value="OPEN">Offen</SelectItem>
                                <SelectItem value="INPROGRESS">In Bearbeitung</SelectItem>
                                <SelectItem value="DONE">Erledigt</SelectItem>
                                <SelectItem value="CLOSED">Geschlossen</SelectItem>
                              </SelectContent>
                            </Select>
                          </TableCell>
                          <TableCell>
                            {feedback.published ? (
                              <Badge variant="default" className="gap-1">
                                <Eye className="size-3" />
                                Ja
                              </Badge>
                            ) : (
                              <Badge variant="outline" className="gap-1">
                                <EyeOff className="size-3" />
                                Nein
                              </Badge>
                            )}
                          </TableCell>
                          <TableCell className="text-muted-foreground">
                            {formatDate(feedback.feedbackDate)}
                          </TableCell>
                          <TableCell className="text-right">
                            <div className="flex justify-end gap-2">
                              <Button
                                size="sm"
                                variant="outline"
                                onClick={() => handleExpandFeedback(feedback.id)}
                              >
                                {isExpanded ? (
                                  <>
                                    <ChevronUp className="mr-2 size-4" />
                                    Kommentare
                                  </>
                                ) : (
                                  <>
                                    <MessageCircle className="mr-2 size-4" />
                                    Kommentare ({feedbackComments.length})
                                  </>
                                )}
                              </Button>
                              {feedback.status !== "CLOSED" && !feedback.published && (
                                <Button
                                  size="sm"
                                  onClick={() => handlePublish(feedback.id)}
                                >
                                  Veröffentlichen
                                </Button>
                              )}
                              {feedback.published && (
                                <Button
                                  size="sm"
                                  variant="outline"
                                  onClick={() => handleUnpublish(feedback.id)}
                                >
                                  Nicht veröffentlichen
                                </Button>
                              )}
                              {currentUser && currentUser.role === "ADMIN" && (
                                <Dialog
                                  open={deleteDialogOpen === feedback.id}
                                  onOpenChange={(open) => {
                                    setDeleteDialogOpen(open ? feedback.id : null)
                                  }}
                                >
                                  <DialogTrigger asChild>
                                    <Button
                                      size="sm"
                                      variant="outline"
                                      className="text-destructive hover:text-destructive"
                                    >
                                      <Trash2 className="mr-2 size-4" />
                                      Löschen
                                    </Button>
                                  </DialogTrigger>
                                  <DialogContent>
                                    <DialogHeader>
                                      <DialogTitle>Feedback löschen</DialogTitle>
                                      <DialogDescription>
                                        Möchten Sie das Feedback <strong>"{feedback.title}"</strong> wirklich komplett löschen?
                                        <br />
                                        <br />
                                        <span className="text-destructive font-medium">
                                          Diese Aktion kann nicht rückgängig gemacht werden. Das Feedback wird dauerhaft aus der Datenbank entfernt.
                                        </span>
                                      </DialogDescription>
                                    </DialogHeader>
                                    <DialogFooter>
                                      <Button
                                        variant="outline"
                                        onClick={() => setDeleteDialogOpen(null)}
                                      >
                                        Abbrechen
                                      </Button>
                                      <Button
                                        variant="destructive"
                                        onClick={() => handleDeleteFeedback(feedback.id)}
                                      >
                                        Löschen
                                      </Button>
                                    </DialogFooter>
                                  </DialogContent>
                                </Dialog>
                              )}
                            </div>
                          </TableCell>
                        </TableRow>
                        {isExpanded && (
                          <TableRow>
                            <TableCell colSpan={7} className="bg-muted/50">
                              <div className="space-y-4 p-4">
                                <div className="flex items-center justify-between">
                                  <h4 className="font-semibold">Kommentare</h4>
                                  <Button
                                    size="sm"
                                    variant="outline"
                                    onClick={() => handleExpandFeedback(feedback.id)}
                                  >
                                    <ChevronUp className="size-4" />
                                  </Button>
                                </div>
                                
                                {/* Kommentare anzeigen */}
                                <div className="space-y-3">
                                  {feedbackComments.length === 0 ? (
                                    <p className="text-sm text-muted-foreground">Noch keine Kommentare</p>
                                  ) : (
                                    feedbackComments.map((comment) => (
                                      <Card key={comment.id} className="bg-background">
                                        <CardContent className="pt-4">
                                          <div className="flex items-start justify-between">
                                            <div className="flex-1">
                                              <p className="text-sm">{comment.content}</p>
                                              <p className="text-xs text-muted-foreground mt-1">
                                                {new Date(comment.createdAt).toLocaleString("de-DE")}
                                              </p>
                                            </div>
                                          </div>
                                        </CardContent>
                                      </Card>
                                    ))
                                  )}
                                </div>

                                {/* Neuen Kommentar hinzufügen */}
                                <div className="space-y-2">
                                  <Label htmlFor={`comment-${feedback.id}`}>Neuer Kommentar</Label>
                                  <Textarea
                                    id={`comment-${feedback.id}`}
                                    placeholder="Kommentar hinzufügen (z.B. Begründung für Status-Änderung)..."
                                    value={commentTexts[feedback.id] || ""}
                                    onChange={(e) =>
                                      setCommentTexts((prev) => ({
                                        ...prev,
                                        [feedback.id]: e.target.value,
                                      }))
                                    }
                                    rows={3}
                                  />
                                  <Button
                                    size="sm"
                                    onClick={() => handleAddComment(feedback.id)}
                                    disabled={!commentTexts[feedback.id]?.trim()}
                                  >
                                    Kommentar hinzufügen
                                  </Button>
                                </div>
                              </div>
                            </TableCell>
                          </TableRow>
                        )}
                        
                        {/* Dialog für Status-Änderung mit Kommentar */}
                        <Dialog
                          open={statusChangeDialogOpen === feedback.id}
                          onOpenChange={(open) => {
                            if (!open) {
                              setStatusChangeDialogOpen(null)
                              setPendingStatusChange((prev) => {
                                const newState = { ...prev }
                                delete newState[feedback.id]
                                return newState
                              })
                            }
                          }}
                        >
                          <DialogContent>
                            <DialogHeader>
                              <DialogTitle>Status ändern</DialogTitle>
                              <DialogDescription>
                                Status von "{statusConfig[currentStatus]?.label || currentStatus}" zu "{statusConfig[pendingNewStatus]?.label || pendingNewStatus}" ändern
                              </DialogDescription>
                            </DialogHeader>
                            <div className="grid gap-4 py-4">
                              <div className="grid gap-2">
                                <Label htmlFor={`status-comment-${feedback.id}`}>
                                  Kommentar (optional)
                                </Label>
                                <Textarea
                                  id={`status-comment-${feedback.id}`}
                                  placeholder="Begründung für die Status-Änderung..."
                                  value={statusChangeComment[feedback.id] || ""}
                                  onChange={(e) =>
                                    setStatusChangeComment((prev) => ({
                                      ...prev,
                                      [feedback.id]: e.target.value,
                                    }))
                                  }
                                  rows={3}
                                />
                              </div>
                            </div>
                            <DialogFooter>
                              <Button
                                variant="outline"
                                onClick={() => {
                                  setStatusChangeDialogOpen(null)
                                  setPendingStatusChange((prev) => {
                                    const newState = { ...prev }
                                    delete newState[feedback.id]
                                    return newState
                                  })
                                }}
                              >
                                Abbrechen
                              </Button>
                              <Button
                                onClick={() => pendingNewStatus && handleStatusChangeWithComment(feedback.id, pendingNewStatus)}
                              >
                                Status ändern
                              </Button>
                            </DialogFooter>
                          </DialogContent>
                        </Dialog>
                      </>
                    )
                  })}
                </TableBody>
              </Table>
            </div>
          )}
        </CardContent>
      </Card>
    </div>
  )
}

