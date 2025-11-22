export default function DashboardWelcomePage() {
  return (
    <div className="flex min-h-screen items-center justify-center bg-muted">
      <div className="bg-background shadow-lg rounded-xl p-10 max-w-lg text-center space-y-6">
        <h1 className="text-3xl font-bold">Herzlich Willkommen!</h1>
        <p className="text-muted-foreground text-lg">
          Ihre Dashboard-Implementierung erfolgt demnächst.
        </p>
        <p className="text-sm text-muted-foreground">
          Vielen Dank, dass Sie CityFeedback nutzen.
        </p>
      </div>
    </div>
  )
}
