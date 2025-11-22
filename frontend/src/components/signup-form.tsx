import { useState } from "react"
import { cn } from "@/lib/utils"
import { Button } from "@/components/ui/button"
import {
  Field,
  FieldDescription,
  FieldGroup,
  FieldLabel,
  FieldSeparator,
} from "@/components/ui/field"
import { Input } from "@/components/ui/input"

export function SignupForm({
  className,
  ...props
}: React.ComponentProps<"form">) {

  const [name, setName] = useState("")
  const [email, setEmail] = useState("")
  const [password, setPassword] = useState("")
  const [confirmPassword, setConfirmPassword] = useState("")
  const [loading, setLoading] = useState(false)
  const [error, setError] = useState("")
  const [success, setSuccess] = useState("")

  // Passwort-Validierung: Mind. 8 Zeichen, Zahl & Buchstabe
  function validatePassword(password: string) {
    const hasMinLength = password.length >= 8
    const hasLetter = /[A-Za-z]/.test(password)
    const hasNumber = /[0-9]/.test(password)

    return hasMinLength && hasLetter && hasNumber
  }

  async function handleSignup(e: React.FormEvent) {
    e.preventDefault()
    setError("")
    setSuccess("")

    // Passwortvergleich
    if (password !== confirmPassword) {
      setError("Passwörter stimmen nicht überein.")
      return
    }

    // Passwortvalidierung
    if (!validatePassword(password)) {
      setError(
        "Ihr Passwort muss mindestens 8 Zeichen lang sein und mindestens eine Zahl und einen Buchstaben enthalten."
      )
      return
    }

    setLoading(true)

    const payload = {
      email: email,
      password: password,
      role: "CITIZEN",
    }

    try {
      const res = await fetch("http://localhost:8080/user", {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify(payload),
      })

      if (!res.ok) {
        setError("Registrierung fehlgeschlagen.")
        setLoading(false)
        return
      }

      setSuccess("Account erfolgreich erstellt! Sie werden weitergeleitet…")

      setTimeout(() => {
        window.location.href = "/login"
      }, 1500)

    } catch (e) {
      setError("Netzwerkfehler – Backend nicht erreichbar.")
    }

    setLoading(false)
  }

  return (
    <form
      onSubmit={handleSignup}
      className={cn("flex flex-col gap-6", className)}
      {...props}
    >
      <FieldGroup>
        <div className="flex flex-col items-center gap-1 text-center">
          <h1 className="text-2xl font-bold">Erstellen Sie Ihren Account</h1>
          <p className="text-muted-foreground text-sm text-balance">
            Bitte füllen Sie das nachfolgende Formular aus, um einen Account zu erstellen.
          </p>
        </div>

        {/* Fehler / Erfolgsmeldungen */}
        {error && <p className="text-red-600 text-center">{error}</p>}
        {success && <p className="text-green-600 text-center">{success}</p>}

        <Field>
          <FieldLabel htmlFor="name">Vor- und Nachname</FieldLabel>
          <Input
            id="name"
            type="text"
            placeholder="Jonas Dachdecker"
            value={name}
            onChange={(e) => setName(e.target.value)}
            required
          />
        </Field>

        <Field>
          <FieldLabel htmlFor="email">Email</FieldLabel>
          <Input
            id="email"
            type="email"
            placeholder="Jonas.Dachdecker@example.com"
            value={email}
            onChange={(e) => setEmail(e.target.value)}
            required
          />
          <FieldDescription>
            Wir verwenden ihre Email nur für die Anmeldung und wichtige Informationen.
          </FieldDescription>
        </Field>

        <Field>
          <FieldLabel htmlFor="password">Passwort</FieldLabel>
          <Input
            id="password"
            type="password"
            value={password}
            onChange={(e) => setPassword(e.target.value)}
            required
          />
          <FieldDescription>
            Das Passwort muss mindestens 8 Zeichen, eine Zahl und einen Buchstaben enthalten.
          </FieldDescription>
        </Field>

        <Field>
          <FieldLabel htmlFor="confirm-password">Passwort bestätigen</FieldLabel>
          <Input
            id="confirm-password"
            type="password"
            value={confirmPassword}
            onChange={(e) => setConfirmPassword(e.target.value)}
            required
          />
        </Field>

        <Field>
          <Button type="submit" disabled={loading}>
            {loading ? "Account wird erstellt…" : "Account erstellen"}
          </Button>
        </Field>

        <FieldSeparator>Oder mit BundID anmelden</FieldSeparator>

        <Field>
          <Button variant="outline" type="button">
            Mit BundID anmelden
          </Button>

          <FieldDescription className="px-6 text-center">
            Sie haben schon einen Account?{" "}
            <a href="/login" className="underline text-primary">
              Hier geht's zum Login!
            </a>
          </FieldDescription>
        </Field>
      </FieldGroup>
    </form>
  )
}
