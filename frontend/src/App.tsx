import { Routes, Route } from 'react-router-dom'
import SignupPage from "@/app/signup/page"
import LoginPage from "@/app/login/page"
import { DashboardLayout } from "@/components/dashboard-layout"
import DashboardPage from "@/app/dashboard/page"
import CreateFeedbackPage from "@/app/dashboard/create/page"
import AdminUsersPage from "@/app/dashboard/admin/users/page"
import StaffFeedbacksPage from "@/app/dashboard/staff/feedbacks/page"
import PublicFeedbacksPage from "@/app/public/page"

function App() {
  return (
    <Routes>
      <Route path="/" element={<PublicFeedbacksPage />} />
      <Route path="/signup" element={<SignupPage />} />
      <Route path="/login" element={<LoginPage />} />
      <Route path="/public" element={<PublicFeedbacksPage />} />
      
      {/* Dashboard Routes with Layout */}
      <Route path="/dashboard" element={<DashboardLayout />}>
        <Route index element={<DashboardPage />} />
        <Route path="create" element={<CreateFeedbackPage />} />
        <Route path="staff/feedbacks" element={<StaffFeedbacksPage />} />
        <Route path="admin/users" element={<AdminUsersPage />} />
      </Route>
      
      {/* Legacy route redirect */}
      <Route path="/dashboard-welcome" element={<DashboardLayout />}>
        <Route index element={<DashboardPage />} />
      </Route>
    </Routes>
  )
}

export default App
