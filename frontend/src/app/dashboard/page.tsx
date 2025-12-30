import { useEffect, useState } from "react"
import { useNavigate } from "react-router-dom"
import {
  MessageSquare,
  Clock,
  CheckCircle2,
  AlertCircle,
  Plus,
  RefreshCw,
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
  getAllFeedbacks,
  categoryLabels,
  statusConfig,
  getStoredUser,
  getUserId,
  type Feedback,
  type Status,
} from "@/lib/api"

export default function DashboardPage() {
  const navigate = useNavigate()
  const [feedbacks, setFeedbacks] = useState<Feedback[]>([])
  const [loading, setLoading] = useState(true)
  const [error, setError] = useState<string | null>(null)

  const loadFeedbacks = async () => {
    setLoading(true)
    setError(null)
    try {
      const data = await getAllFeedbacks()
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

  // Statistics - nur eigene Feedbacks für Bürger
  const user = getStoredUser()
  const userId = user ? getUserId(user) : null
  const ownFeedbacks = userId ? feedbacks.filter((f) => f.userId === userId) : []
  const totalFeedbacks = ownFeedbacks.length
  const openCount = ownFeedbacks.filter((f) => f.status === "OPEN").length
  const inProgressCount = ownFeedbacks.filter((f) => f.status === "INPROGRESS").length
  const closedCount = ownFeedbacks.filter((f) => f.status === "CLOSED").length

  const stats = [
    {
      title: "Gesamt",
      value: totalFeedbacks,
      icon: MessageSquare,
      description: "Meine Feedbacks",
      color: "text-primary",
    },
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
      description: "Aktuell in Bearbeitung",
      color: "text-blue-500",
    },
    {
      title: "Geschlossen",
      value: closedCount,
      icon: CheckCircle2,
      description: "Abgeschlossene Fälle",
      color: "text-emerald-500",
    },
  ]

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

  return (
    <div className="space-y-8">
      {/* Header */}
      <div className="flex flex-col gap-4 sm:flex-row sm:items-center sm:justify-between">
        <div>
          <h1 className="text-3xl font-bold tracking-tight">Dashboard</h1>
          <p className="text-muted-foreground">
            Übersicht aller Bürger-Feedbacks
          </p>
        </div>
        <div className="flex gap-2">
          <Button variant="outline" size="sm" onClick={loadFeedbacks} disabled={loading}>
            <RefreshCw className={`mr-2 size-4 ${loading ? "animate-spin" : ""}`} />
            Aktualisieren
          </Button>
          <Button size="sm" onClick={() => navigate("/dashboard/create")}>
            <Plus className="mr-2 size-4" />
            Neues Feedback
          </Button>
        </div>
      </div>

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
          <CardTitle>Meine Feedbacks</CardTitle>
          <CardDescription>
            Liste Ihrer eingereichten Feedbacks mit Status und Kategorie
          </CardDescription>
        </CardHeader>
        <CardContent>
          {error ? (
            <div className="flex flex-col items-center justify-center py-12 text-center">
              <AlertCircle className="size-12 text-destructive mb-4" />
              <p className="text-muted-foreground">{error}</p>
              <Button variant="outline" className="mt-4" onClick={loadFeedbacks}>
                Erneut versuchen
              </Button>
            </div>
          ) : loading ? (
            <div className="flex items-center justify-center py-12">
              <RefreshCw className="size-8 animate-spin text-muted-foreground" />
            </div>
          ) : ownFeedbacks.length === 0 ? (
            <div className="flex flex-col items-center justify-center py-12 text-center">
              <MessageSquare className="size-12 text-muted-foreground mb-4" />
              <p className="text-lg font-medium">Noch keine Feedbacks</p>
              <p className="text-muted-foreground mb-4">
                Erstellen Sie Ihr erstes Feedback, um loszulegen.
              </p>
              <Button onClick={() => navigate("/dashboard/create")}>
                <Plus className="mr-2 size-4" />
                Erstes Feedback erstellen
              </Button>
            </div>
          ) : (
            <div className="rounded-md border">
              <Table>
                <TableHeader>
                  <TableRow>
                    <TableHead className="w-12">#</TableHead>
                    <TableHead>Titel</TableHead>
                    <TableHead>Kategorie</TableHead>
                    <TableHead>Status</TableHead>
                    <TableHead>Datum</TableHead>
                  </TableRow>
                </TableHeader>
                <TableBody>
                  {ownFeedbacks.map((feedback) => (
                    <TableRow key={feedback.id} className="cursor-pointer hover:bg-muted/50">
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
                        <Badge variant={statusConfig[feedback.status as Status]?.variant || "default"}>
                          {statusConfig[feedback.status as Status]?.label || feedback.status}
                        </Badge>
                      </TableCell>
                      <TableCell className="text-muted-foreground">
                        {formatDate(feedback.feedbackDate)}
                      </TableCell>
                    </TableRow>
                  ))}
                </TableBody>
              </Table>
            </div>
          )}
        </CardContent>
      </Card>
    </div>
  )
}

