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

export function LoginForm({
  className,
  ...props
}: React.ComponentProps<"form">) {

  const [email, setEmail] = useState("")
  const [password, setPassword] = useState("")
  const [loading, setLoading] = useState(false)
  const [error, setError] = useState("")

  async function handleLogin(e: React.FormEvent) {
    e.preventDefault()
    setError("")
    setLoading(true)

    const payload = { email, password }

    try {
      const res = await fetch("http://localhost:8080/user/login", {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify(payload),
      })

      if (!res.ok) {
        setError("Email oder Passwort falsch.")
        setLoading(false)
        return
      }

      const user = await res.json()

      // User speichern (z.B. Session)
      localStorage.setItem("user", JSON.stringify(user))

      // Weiterleiten zum Dashboard
      window.location.href = "/dashboard"

    } catch (err) {
      setError("Backend nicht erreichbar.")
    }

    setLoading(false)
  }

  return (
    <form onSubmit={handleLogin} className={cn("flex flex-col gap-6", className)} {...props}>
      <FieldGroup>
        <div className="flex flex-col items-center gap-1 text-center">
          <h1 className="text-2xl font-bold">Login</h1>
          <p className="text-muted-foreground text-sm">
            Melden Sie sich mit Email & Passwort an.
          </p>
        </div>

        {error && <p className="text-red-600 text-center">{error}</p>}

        <Field>
          <FieldLabel htmlFor="email">Email</FieldLabel>
          <Input
            id="email"
            type="email"
            placeholder="beispiel@domain.de"
            value={email}
            onChange={(e) => setEmail(e.target.value)}
            required
          />
        </Field>

        <Field>
          <FieldLabel htmlFor="password">Passwort</FieldLabel>
          <Input
            id="password"
            type="password"
            placeholder="••••••••"
            value={password}
            onChange={(e) => setPassword(e.target.value)}
            required
          />
        </Field>

        <Field>
          <Button type="submit" disabled={loading}>
            {loading ? "Bitte warten…" : "Login"}
          </Button>
        </Field>

        <FieldSeparator>Oder mit BundID anmelden</FieldSeparator>

        <Field>
          <Button variant="outline" type="button">
            Mit BundID anmelden
          </Button>

          <FieldDescription className="text-center">
            Noch kein Account?{" "}
            <a href="/signup" className="underline">Hier registrieren</a>
          </FieldDescription>
        </Field>
      </FieldGroup>
    </form>
  )
}
