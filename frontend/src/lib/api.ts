// API Base URL
const API_BASE = "http://localhost:8080"

// Types
export interface User {
  id: string
  email: { value: string } | string
  role: string
}

// Helper um Email als String zu bekommen (Backend gibt Objekt zurück)
export function getUserEmail(user: User): string {
  if (typeof user.email === 'string') {
    return user.email
  }
  return user.email?.value || ''
}

// Helper um User-ID als String zu bekommen
export function getUserId(user: User): string {
  if (typeof user.id === 'string') {
    return user.id
  }
  // Fallback für UUID-Objekte
  return (user.id as any)?.value || String(user.id)
}

export type Category = "VERKEHR" | "UMWELT" | "BELEUCHTUNG" | "VANDALISMUS" | "VERWALTUNG"
export type Status = "OPEN" | "INPROGRESS" | "DONE" | "CLOSED"

export interface Feedback {
  id: number
  title: string
  category: Category
  feedbackDate: string
  content: string
  status: Status
  published: boolean
  userId: string
}

export interface CreateFeedbackDTO {
  userId: string
  title: string
  category: Category
  content: string
}

// Auth helpers
export function getStoredUser(): User | null {
  const stored = localStorage.getItem("user")
  if (!stored) return null
  try {
    return JSON.parse(stored)
  } catch {
    return null
  }
}

export function logout() {
  localStorage.removeItem("user")
  window.location.href = "/"
}

// Feedback API
export async function getAllFeedbacks(): Promise<Feedback[]> {
  const res = await fetch(`${API_BASE}/feedback`)
  if (!res.ok) {
    throw new Error("Feedbacks konnten nicht geladen werden")
  }
  return res.json()
}

export async function getFeedbackById(id: number): Promise<Feedback> {
  const res = await fetch(`${API_BASE}/feedback/${id}`)
  if (!res.ok) {
    throw new Error("Feedback nicht gefunden")
  }
  return res.json()
}

export async function createFeedback(dto: CreateFeedbackDTO): Promise<Feedback> {
  const res = await fetch(`${API_BASE}/feedback`, {
    method: "POST",
    headers: { "Content-Type": "application/json" },
    body: JSON.stringify(dto),
  })
  if (!res.ok) {
    const error = await res.text()
    throw new Error(error || "Feedback konnte nicht erstellt werden")
  }
  return res.json()
}

// Category labels for display
export const categoryLabels: Record<Category, string> = {
  VERKEHR: "Verkehr",
  UMWELT: "Umwelt",
  BELEUCHTUNG: "Beleuchtung",
  VANDALISMUS: "Vandalismus",
  VERWALTUNG: "Verwaltung",
}

// Status labels and colors
export const statusConfig: Record<Status, { label: string; variant: "default" | "secondary" | "destructive" | "outline" }> = {
  OPEN: { label: "Offen", variant: "default" },
  INPROGRESS: { label: "In Bearbeitung", variant: "secondary" },
  DONE: { label: "Erledigt", variant: "outline" },
  CLOSED: { label: "Geschlossen", variant: "outline" },
}

// User Role labels
export const roleLabels: Record<string, string> = {
  CITIZEN: "Bürger",
  STAFF: "Mitarbeiter",
  ADMIN: "Administrator",
}

// User API
export async function getAllUsers(): Promise<User[]> {
  const res = await fetch(`${API_BASE}/user`)
  if (!res.ok) {
    throw new Error("Benutzer konnten nicht geladen werden")
  }
  return res.json()
}

export async function getUserById(id: string): Promise<User> {
  const res = await fetch(`${API_BASE}/user/${id}`)
  if (!res.ok) {
    throw new Error("Benutzer nicht gefunden")
  }
  return res.json()
}

export async function createUserByAdmin(dto: { email: string; password: string; role: string }): Promise<User> {
  const user = getStoredUser()
  if (!user || user.role !== "ADMIN") {
    throw new Error("Nur Administratoren können Benutzer erstellen")
  }

  const res = await fetch(`${API_BASE}/user/admin/create`, {
    method: "POST",
    headers: {
      "Content-Type": "application/json",
      "X-Admin-Id": getUserId(user),
    },
    body: JSON.stringify(dto),
  })
  if (!res.ok) {
    const error = await res.text()
    throw new Error(error || "Benutzer konnte nicht erstellt werden")
  }
  return res.json()
}

export async function changeUserPassword(userId: string, newPassword: string): Promise<User> {
  const res = await fetch(`${API_BASE}/user/${userId}/password`, {
    method: "PUT",
    headers: { "Content-Type": "application/json" },
    body: JSON.stringify({ password: newPassword }),
  })
  if (!res.ok) {
    const error = await res.text()
    throw new Error(error || "Passwort konnte nicht geändert werden")
  }
  return res.json()
}

export async function changeUserRole(userId: string, newRole: string): Promise<User> {
  const user = getStoredUser()
  if (!user || user.role !== "ADMIN") {
    throw new Error("Nur Administratoren können Rollen ändern")
  }

  const res = await fetch(`${API_BASE}/user/${userId}/role`, {
    method: "PUT",
    headers: {
      "Content-Type": "application/json",
      "X-Admin-Id": getUserId(user),
    },
    body: JSON.stringify({ role: newRole }),
  })
  if (!res.ok) {
    const error = await res.text()
    throw new Error(error || "Rolle konnte nicht geändert werden")
  }
  return res.json()
}

// Feedback API für Mitarbeiter
export async function updateFeedbackStatus(feedbackId: number, newStatus: Status): Promise<Feedback> {
  const res = await fetch(`${API_BASE}/feedback/${feedbackId}/status`, {
    method: "PUT",
    headers: { "Content-Type": "application/json" },
    body: JSON.stringify({ status: newStatus }),
  })
  if (!res.ok) {
    const error = await res.text()
    throw new Error(error || "Status konnte nicht geändert werden")
  }
  return res.json()
}

export async function publishFeedback(feedbackId: number): Promise<Feedback> {
  const res = await fetch(`${API_BASE}/feedback/${feedbackId}/publish`, {
    method: "PUT",
    headers: { "Content-Type": "application/json" },
  })
  if (!res.ok) {
    const error = await res.text()
    throw new Error(error || "Feedback konnte nicht veröffentlicht werden")
  }
  return res.json()
}

export async function unpublishFeedback(feedbackId: number): Promise<Feedback> {
  const res = await fetch(`${API_BASE}/feedback/${feedbackId}/unpublish`, {
    method: "PUT",
    headers: { "Content-Type": "application/json" },
  })
  if (!res.ok) {
    const error = await res.text()
    throw new Error(error || "Feedback konnte nicht aus der Veröffentlichung genommen werden")
  }
  return res.json()
}

export async function deleteFeedback(feedbackId: number): Promise<void> {
  const user = getStoredUser()
  if (!user || user.role !== "ADMIN") {
    throw new Error("Nur Administratoren können Feedbacks löschen")
  }

  const res = await fetch(`${API_BASE}/feedback/${feedbackId}`, {
    method: "DELETE",
    headers: {
      "X-Admin-Id": getUserId(user),
    },
  })
  if (!res.ok) {
    const error = await res.text()
    throw new Error(error || "Feedback konnte nicht gelöscht werden")
  }
}

// Comment API
export interface Comment {
  id: number
  feedbackId: number
  authorId: string
  content: string
  createdAt: string
}

export interface CreateCommentDTO {
  authorId: string
  content: string
}

export async function getCommentsByFeedbackId(feedbackId: number): Promise<Comment[]> {
  const res = await fetch(`${API_BASE}/feedback/${feedbackId}/comments`)
  if (!res.ok) {
    throw new Error("Kommentare konnten nicht geladen werden")
  }
  return res.json()
}

export async function addComment(feedbackId: number, content: string): Promise<Comment> {
  const user = getStoredUser()
  if (!user || (user.role !== "STAFF" && user.role !== "ADMIN")) {
    throw new Error("Nur Mitarbeiter und Administratoren können Kommentare hinzufügen")
  }

  const res = await fetch(`${API_BASE}/feedback/${feedbackId}/comments`, {
    method: "POST",
    headers: { "Content-Type": "application/json" },
    body: JSON.stringify({
      authorId: getUserId(user),
      content: content,
    }),
  })
  if (!res.ok) {
    const error = await res.text()
    throw new Error(error || "Kommentar konnte nicht hinzugefügt werden")
  }
  return res.json()
}

// Öffentliche Feedback API (ohne Login)
export async function getPublishedFeedbacks(): Promise<Feedback[]> {
  const res = await fetch(`${API_BASE}/feedback/public`)
  if (!res.ok) {
    throw new Error("Feedbacks konnten nicht geladen werden")
  }
  return res.json()
}

export async function deleteUser(userId: string): Promise<void> {
  const user = getStoredUser()
  if (!user || user.role !== "ADMIN") {
    throw new Error("Nur Administratoren können Benutzer löschen")
  }

  const res = await fetch(`${API_BASE}/user/${userId}`, {
    method: "DELETE",
    headers: {
      "X-Admin-Id": getUserId(user),
    },
  })
  if (!res.ok) {
    const error = await res.text()
    throw new Error(error || "Benutzer konnte nicht gelöscht werden")
  }
}

// Demo-Daten API
export async function deleteAllDemoData(): Promise<{ deletedUsers: number; message: string }> {
  const user = getStoredUser()
  if (!user || user.role !== "ADMIN") {
    throw new Error("Nur Administratoren können Demo-Daten löschen")
  }

  const res = await fetch(`${API_BASE}/admin/demo-data`, {
    method: "DELETE",
    headers: {
      "X-Admin-Id": getUserId(user),
    },
  })
  if (!res.ok) {
    const error = await res.text()
    throw new Error(error || "Demo-Daten konnten nicht gelöscht werden")
  }
  return res.json()
}

