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

export type Category = "VERKEHR" | "UMWELT" | "BELEUCHTUNG" | "VANDALISMUS" | "VERWALTUNG"
export type Status = "OPEN" | "IN_PROGRESS" | "CLOSED"

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
  IN_PROGRESS: { label: "In Bearbeitung", variant: "secondary" },
  CLOSED: { label: "Geschlossen", variant: "outline" },
}

