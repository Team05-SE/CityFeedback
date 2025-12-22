import { useEffect, useState } from "react"
import {
  MessageSquare,
  Filter,
  RefreshCw,
  Building2,
  MessageCircle,
} from "lucide-react"

import { Card, CardContent, CardDescription, CardHeader, CardTitle } from "@/components/ui/card"
import { Button } from "@/components/ui/button"
import { Badge } from "@/components/ui/badge"
import {
  Select,
  SelectContent,
  SelectItem,
  SelectTrigger,
  SelectValue,
} from "@/components/ui/select"
import {
  getPublishedFeedbacks,
  getCommentsByFeedbackId,
  categoryLabels,
  statusConfig,
  type Feedback,
  type Category,
  type Status,
  type Comment,
} from "@/lib/api"

export default function PublicFeedbacksPage() {
  const [feedbacks, setFeedbacks] = useState<Feedback[]>([])
  const [loading, setLoading] = useState(true)
  const [error, setError] = useState<string | null>(null)
  const [selectedCategory, setSelectedCategory] = useState<string>("ALL")
  const [comments, setComments] = useState<Record<number, Comment[]>>({})
  const [loadingComments, setLoadingComments] = useState<Record<number, boolean>>({})

  const loadFeedbacks = async () => {
    setLoading(true)
    setError(null)
    try {
      const data = await getPublishedFeedbacks()
      setFeedbacks(data)
    } catch (err) {
      setError("Feedbacks konnten nicht geladen werden. Ist das Backend erreichbar?")
    } finally {
      setLoading(false)
    }
  }

  useEffect(() => {
    loadFeedbacks()
  }, [])

  // Filter nach Kategorie
  const filteredFeedbacks = selectedCategory === "ALL"
    ? feedbacks
    : feedbacks.filter((f) => f.category === selectedCategory)

  // Gruppierung nach Status
  const groupedByStatus: Record<Status, Feedback[]> = {
    PENDING: [],
    OPEN: [],
    INPROGRESS: [],
    DONE: [],
    CLOSED: [],
  }

  filteredFeedbacks.forEach((feedback) => {
    const status = feedback.status as Status
    if (groupedByStatus[status]) {
      groupedByStatus[status].push(feedback)
    }
  })

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

  const formatDateTime = (dateString: string) => {
    try {
      return new Date(dateString).toLocaleString("de-DE", {
        day: "2-digit",
        month: "2-digit",
        year: "numeric",
        hour: "2-digit",
        minute: "2-digit",
      })
    } catch {
      return dateString
    }
  }

  const loadComments = async (feedbackId: number) => {
    // Wenn Kommentare bereits geladen sind, nicht erneut laden
    if (comments[feedbackId] !== undefined) {
      return
    }

    setLoadingComments((prev) => ({ ...prev, [feedbackId]: true }))
    try {
      const feedbackComments = await getCommentsByFeedbackId(feedbackId)
      setComments((prev) => ({ ...prev, [feedbackId]: feedbackComments }))
    } catch (err) {
      console.error("Kommentare konnten nicht geladen werden:", err)
      // Setze leere Liste bei Fehler
      setComments((prev) => ({ ...prev, [feedbackId]: [] }))
    } finally {
      setLoadingComments((prev) => ({ ...prev, [feedbackId]: false }))
    }
  }

  // Kommentare für alle Feedbacks beim Laden laden
  useEffect(() => {
    if (feedbacks.length > 0) {
      feedbacks.forEach((feedback) => {
        loadComments(feedback.id)
      })
    }
  }, [feedbacks])

  const statusOrder: Status[] = ["OPEN", "INPROGRESS", "DONE", "CLOSED"]

  return (
    <div className="min-h-screen bg-gradient-to-br from-background via-background to-muted">
      <div className="container mx-auto px-4 py-8 space-y-8">
        {/* Header */}
        <div className="text-center space-y-4">
          <div className="flex items-center justify-center gap-3">
            <div className="bg-primary text-primary-foreground flex size-12 items-center justify-center rounded-lg">
              <Building2 className="size-6" />
            </div>
            <h1 className="text-4xl font-bold tracking-tight">CityFeedback</h1>
          </div>
          <p className="text-muted-foreground text-lg">
            Öffentliche Übersicht aller Bürger-Feedbacks
          </p>
        </div>

        {/* Filter */}
        <Card>
          <CardHeader>
            <CardTitle className="flex items-center gap-2">
              <Filter className="size-5" />
              Filter
            </CardTitle>
          </CardHeader>
          <CardContent>
            <div className="flex gap-4 items-center">
              <label htmlFor="category-filter" className="text-sm font-medium">
                Kategorie:
              </label>
              <Select value={selectedCategory} onValueChange={setSelectedCategory}>
                <SelectTrigger id="category-filter" className="w-48">
                  <SelectValue />
                </SelectTrigger>
                <SelectContent>
                  <SelectItem value="ALL">Alle Kategorien</SelectItem>
                  <SelectItem value="VERKEHR">Verkehr</SelectItem>
                  <SelectItem value="UMWELT">Umwelt</SelectItem>
                  <SelectItem value="BELEUCHTUNG">Beleuchtung</SelectItem>
                  <SelectItem value="VANDALISMUS">Vandalismus</SelectItem>
                  <SelectItem value="VERWALTUNG">Verwaltung</SelectItem>
                </SelectContent>
              </Select>
              <Button variant="outline" size="sm" onClick={loadFeedbacks} disabled={loading}>
                <RefreshCw className={`mr-2 size-4 ${loading ? "animate-spin" : ""}`} />
                Aktualisieren
              </Button>
            </div>
          </CardContent>
        </Card>

        {/* Error Message */}
        {error && (
          <Card className="border-destructive">
            <CardContent className="pt-6">
              <div className="flex items-center gap-2 text-destructive">
                <MessageSquare className="size-4" />
                <p>{error}</p>
              </div>
            </CardContent>
          </Card>
        )}

        {/* Feedbacks nach Status gruppiert */}
        {loading ? (
          <div className="flex items-center justify-center py-12">
            <RefreshCw className="size-8 animate-spin text-muted-foreground" />
          </div>
        ) : filteredFeedbacks.length === 0 ? (
          <Card>
            <CardContent className="pt-6">
              <div className="flex flex-col items-center justify-center py-12 text-center">
                <MessageSquare className="size-12 text-muted-foreground mb-4" />
                <p className="text-lg font-medium">Keine Feedbacks gefunden</p>
                <p className="text-muted-foreground">
                  {selectedCategory !== "ALL"
                    ? "Keine Feedbacks in dieser Kategorie"
                    : "Noch keine veröffentlichten Feedbacks"}
                </p>
              </div>
            </CardContent>
          </Card>
        ) : (
          <div className="space-y-6">
            {statusOrder.map((status) => {
              const statusFeedbacks = groupedByStatus[status]
              if (statusFeedbacks.length === 0) return null

              const statusInfo = statusConfig[status]
              return (
                <Card key={status}>
                  <CardHeader>
                    <div className="flex items-center justify-between">
                      <div>
                        <CardTitle className="flex items-center gap-2">
                          {statusInfo.label}
                          <Badge variant={statusInfo.variant}>
                            {statusFeedbacks.length}
                          </Badge>
                        </CardTitle>
                        <CardDescription>
                          {status === "OPEN" && "Freigegebene Feedbacks, die bearbeitet werden"}
                          {status === "INPROGRESS" && "Feedbacks, die aktuell bearbeitet werden"}
                          {status === "DONE" && "Erledigte Feedbacks"}
                          {status === "CLOSED" && "Geschlossene Feedbacks"}
                        </CardDescription>
                      </div>
                    </div>
                  </CardHeader>
                  <CardContent>
                    <div className="space-y-4">
                      {statusFeedbacks.map((feedback) => {
                        const feedbackComments = comments[feedback.id] || []
                        const isLoadingComments = loadingComments[feedback.id] === true
                        return (
                          <Card key={feedback.id} className="border-l-4 border-l-primary">
                            <CardContent className="pt-6">
                              <div className="flex items-start justify-between">
                                <div className="flex-1">
                                  <div className="flex items-center gap-2 mb-2">
                                    <h3 className="text-lg font-semibold">{feedback.title}</h3>
                                    <Badge variant="outline">
                                      {categoryLabels[feedback.category] || feedback.category}
                                    </Badge>
                                  </div>
                                  <p className="text-muted-foreground mb-3">{feedback.content}</p>
                                  <p className="text-xs text-muted-foreground mb-4">
                                    Eingereicht am: {formatDate(feedback.feedbackDate)}
                                  </p>

                                  {/* Kommentare anzeigen */}
                                  {isLoadingComments ? (
                                    <div className="flex items-center gap-2 text-sm text-muted-foreground">
                                      <RefreshCw className="size-4 animate-spin" />
                                      Kommentare werden geladen...
                                    </div>
                                  ) : feedbackComments.length > 0 ? (
                                    <div className="mt-4 space-y-3 border-t pt-4">
                                      <div className="flex items-center gap-2 mb-2">
                                        <MessageCircle className="size-4 text-muted-foreground" />
                                        <h4 className="text-sm font-semibold">
                                          Kommentare ({feedbackComments.length})
                                        </h4>
                                      </div>
                                      {feedbackComments.map((comment) => (
                                        <div
                                          key={comment.id}
                                          className="bg-muted/50 rounded-lg p-3 space-y-1"
                                        >
                                          <p className="text-sm">{comment.content}</p>
                                          <p className="text-xs text-muted-foreground">
                                            {formatDateTime(comment.createdAt)}
                                          </p>
                                        </div>
                                      ))}
                                    </div>
                                  ) : null}
                                </div>
                              </div>
                            </CardContent>
                          </Card>
                        )
                      })}
                    </div>
                  </CardContent>
                </Card>
              )
            })}
          </div>
        )}

        {/* Login Link */}
        <Card>
          <CardContent className="pt-6">
            <div className="text-center space-y-2">
              <p className="text-sm text-muted-foreground">
                Möchten Sie ein Feedback einreichen?
              </p>
              <Button variant="outline" onClick={() => (window.location.href = "/login")}>
                Anmelden
              </Button>
            </div>
          </CardContent>
        </Card>
      </div>
    </div>
  )
}

