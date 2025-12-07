import { useState } from "react"
import { useNavigate } from "react-router-dom"
import { ArrowLeft, Send, Loader2 } from "lucide-react"

import { Card, CardContent, CardDescription, CardHeader, CardTitle } from "@/components/ui/card"
import { Button } from "@/components/ui/button"
import { Input } from "@/components/ui/input"
import { Textarea } from "@/components/ui/textarea"
import { Label } from "@/components/ui/label"
import {
  Select,
  SelectContent,
  SelectItem,
  SelectTrigger,
  SelectValue,
} from "@/components/ui/select"
import {
  createFeedback,
  getStoredUser,
  categoryLabels,
  type Category,
} from "@/lib/api"

const categories: Category[] = [
  "VERKEHR",
  "UMWELT",
  "BELEUCHTUNG",
  "VANDALISMUS",
  "VERWALTUNG",
]

export default function CreateFeedbackPage() {
  const navigate = useNavigate()
  const user = getStoredUser()

  const [title, setTitle] = useState("")
  const [content, setContent] = useState("")
  const [category, setCategory] = useState<Category | "">("")
  const [loading, setLoading] = useState(false)
  const [error, setError] = useState<string | null>(null)

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault()
    setError(null)

    if (!user?.id) {
      setError("Benutzer nicht angemeldet")
      return
    }

    if (!title.trim()) {
      setError("Bitte geben Sie einen Titel ein")
      return
    }

    if (!category) {
      setError("Bitte wählen Sie eine Kategorie")
      return
    }

    if (!content.trim()) {
      setError("Bitte beschreiben Sie Ihr Anliegen")
      return
    }

    setLoading(true)

    try {
      await createFeedback({
        userId: user.id,
        title: title.trim(),
        category: category as Category,
        content: content.trim(),
      })
      navigate("/dashboard")
    } catch (err) {
      setError(err instanceof Error ? err.message : "Feedback konnte nicht erstellt werden")
    } finally {
      setLoading(false)
    }
  }

  return (
    <div className="mx-auto max-w-2xl space-y-6">
      {/* Header */}
      <div className="flex items-center gap-4">
        <Button
          variant="ghost"
          size="icon"
          onClick={() => navigate("/dashboard")}
        >
          <ArrowLeft className="size-5" />
        </Button>
        <div>
          <h1 className="text-2xl font-bold tracking-tight">Neues Feedback</h1>
          <p className="text-muted-foreground">
            Teilen Sie uns Ihr Anliegen mit
          </p>
        </div>
      </div>

      {/* Form */}
      <Card>
        <CardHeader>
          <CardTitle>Feedback erstellen</CardTitle>
          <CardDescription>
            Beschreiben Sie Ihr Anliegen so genau wie möglich, damit wir es schnell
            bearbeiten können.
          </CardDescription>
        </CardHeader>
        <CardContent>
          <form onSubmit={handleSubmit} className="space-y-6">
            {error && (
              <div className="rounded-lg bg-destructive/10 p-3 text-sm text-destructive">
                {error}
              </div>
            )}

            <div className="space-y-2">
              <Label htmlFor="title">Titel *</Label>
              <Input
                id="title"
                placeholder="z.B. Schlagloch in der Hauptstraße"
                value={title}
                onChange={(e) => setTitle(e.target.value)}
                maxLength={200}
                disabled={loading}
              />
              <p className="text-xs text-muted-foreground">
                {title.length}/200 Zeichen
              </p>
            </div>

            <div className="space-y-2">
              <Label htmlFor="category">Kategorie *</Label>
              <Select
                value={category}
                onValueChange={(val) => setCategory(val as Category)}
                disabled={loading}
              >
                <SelectTrigger id="category">
                  <SelectValue placeholder="Kategorie auswählen..." />
                </SelectTrigger>
                <SelectContent>
                  {categories.map((cat) => (
                    <SelectItem key={cat} value={cat}>
                      {categoryLabels[cat]}
                    </SelectItem>
                  ))}
                </SelectContent>
              </Select>
            </div>

            <div className="space-y-2">
              <Label htmlFor="content">Beschreibung *</Label>
              <Textarea
                id="content"
                placeholder="Beschreiben Sie Ihr Anliegen detailliert..."
                value={content}
                onChange={(e) => setContent(e.target.value)}
                rows={6}
                maxLength={5000}
                disabled={loading}
              />
              <p className="text-xs text-muted-foreground">
                {content.length}/5000 Zeichen
              </p>
            </div>

            <div className="flex gap-3 pt-4">
              <Button
                type="button"
                variant="outline"
                onClick={() => navigate("/dashboard")}
                disabled={loading}
              >
                Abbrechen
              </Button>
              <Button type="submit" disabled={loading}>
                {loading ? (
                  <>
                    <Loader2 className="mr-2 size-4 animate-spin" />
                    Wird gesendet...
                  </>
                ) : (
                  <>
                    <Send className="mr-2 size-4" />
                    Feedback absenden
                  </>
                )}
              </Button>
            </div>
          </form>
        </CardContent>
      </Card>

      {/* Info Box */}
      <Card className="border-primary/20 bg-primary/5">
        <CardContent className="pt-6">
          <h3 className="font-semibold mb-2">Hinweis</h3>
          <p className="text-sm text-muted-foreground">
            Ihr Feedback wird nach dem Absenden von unseren Mitarbeitern geprüft und bearbeitet.
            Sie können den Status Ihres Feedbacks jederzeit im Dashboard einsehen.
          </p>
        </CardContent>
      </Card>
    </div>
  )
}

