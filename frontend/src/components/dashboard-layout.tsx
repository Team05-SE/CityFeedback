import { useEffect, useState } from "react"
import { useNavigate, Outlet } from "react-router-dom"
import { SidebarProvider, SidebarInset, SidebarTrigger } from "@/components/ui/sidebar"
import { Separator } from "@/components/ui/separator"
import { AppSidebar } from "@/components/app-sidebar"
import { getStoredUser, type User } from "@/lib/api"

export function DashboardLayout() {
  const navigate = useNavigate()
  const [user, setUser] = useState<User | null>(null)
  const [checked, setChecked] = useState(false)

  useEffect(() => {
    const storedUser = getStoredUser()
    console.log("Stored user:", storedUser) // Debug
    if (!storedUser) {
      navigate("/login", { replace: true })
    } else {
      setUser(storedUser)
    }
    setChecked(true)
  }, [navigate])

  // Warte bis der Check abgeschlossen ist
  if (!checked) {
    return (
      <div className="flex min-h-screen items-center justify-center">
        <div className="animate-pulse text-muted-foreground">Laden...</div>
      </div>
    )
  }

  // User nicht eingeloggt - wird bereits zu /login navigiert
  if (!user) {
    return null
  }

  return (
    <SidebarProvider>
      <AppSidebar />
      <SidebarInset>
        <header className="flex h-16 shrink-0 items-center gap-2 border-b px-4">
          <SidebarTrigger className="-ml-1" />
          <Separator orientation="vertical" className="mr-2 h-4" />
          <div className="flex-1" />
        </header>
        <main className="flex-1 overflow-auto p-6">
          <Outlet />
        </main>
      </SidebarInset>
    </SidebarProvider>
  )
}

