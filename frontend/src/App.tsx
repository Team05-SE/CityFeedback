import { Routes, Route, Link } from 'react-router-dom'
import SignupPage from "@/app/signup/page"
import LoginPage from "@/app/login/page"
import { DashboardLayout } from "@/components/dashboard-layout"
import DashboardPage from "@/app/dashboard/page"
import CreateFeedbackPage from "@/app/dashboard/create/page"

function HomePage() {
  return (
    <div className="flex min-h-screen items-center justify-center bg-gradient-to-br from-background via-background to-muted">
      <div className="text-center space-y-6 p-8">
        <div className="space-y-2">
          <h1 className="text-4xl font-bold tracking-tight">CityFeedback</h1>
          <p className="text-muted-foreground text-lg">
            Das Bürgerfeedback-Portal für Ihre Stadt
          </p>
        </div>
        <div className="flex gap-4 justify-center">
          <Link
            to="/signup"
            className="bg-primary text-primary-foreground px-6 py-3 rounded-lg font-medium hover:opacity-90 transition-opacity"
          >
            Registrieren
          </Link>
          <Link
            to="/login"
            className="border border-border px-6 py-3 rounded-lg font-medium hover:bg-accent transition-colors"
          >
            Anmelden
          </Link>
        </div>
      </div>
    </div>
  )
}

function App() {
  return (
    <Routes>
      <Route path="/" element={<HomePage />} />
      <Route path="/signup" element={<SignupPage />} />
      <Route path="/login" element={<LoginPage />} />
      
      {/* Dashboard Routes with Layout */}
      <Route path="/dashboard" element={<DashboardLayout />}>
        <Route index element={<DashboardPage />} />
        <Route path="create" element={<CreateFeedbackPage />} />
      </Route>
      
      {/* Legacy route redirect */}
      <Route path="/dashboard-welcome" element={<DashboardLayout />}>
        <Route index element={<DashboardPage />} />
      </Route>
    </Routes>
  )
}

export default App
