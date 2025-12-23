import { useEffect, useState } from "react"
import {
  Users,
  Plus,
  RefreshCw,
  Edit,
  Key,
  AlertCircle,
  Trash2,
  Database,
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
  Dialog,
  DialogContent,
  DialogDescription,
  DialogFooter,
  DialogHeader,
  DialogTitle,
  DialogTrigger,
} from "@/components/ui/dialog"
import {
  Select,
  SelectContent,
  SelectItem,
  SelectTrigger,
  SelectValue,
} from "@/components/ui/select"
import { Input } from "@/components/ui/input"
import { Label } from "@/components/ui/label"
import {
  getAllUsers,
  createUserByAdmin,
  changeUserRole,
  changeUserPassword,
  deleteUser,
  deleteAllDemoData,
  roleLabels,
  getUserEmail,
  getUserId,
  getStoredUser,
  type User,
} from "@/lib/api"

export default function AdminUsersPage() {
  const [users, setUsers] = useState<User[]>([])
  const [loading, setLoading] = useState(true)
  const [error, setError] = useState<string | null>(null)
  const [createDialogOpen, setCreateDialogOpen] = useState(false)
  const [editRoleDialogOpen, setEditRoleDialogOpen] = useState<string | null>(null)
  const [changePasswordDialogOpen, setChangePasswordDialogOpen] = useState<string | null>(null)
  const [deleteDialogOpen, setDeleteDialogOpen] = useState<string | null>(null)
  const [deleteDemoDataDialogOpen, setDeleteDemoDataDialogOpen] = useState(false)

  // Form states
  const [newUserEmail, setNewUserEmail] = useState("")
  const [newUserPassword, setNewUserPassword] = useState("")
  const [newUserRole, setNewUserRole] = useState("CITIZEN")
  const [selectedUserRole, setSelectedUserRole] = useState("CITIZEN")
  const [newPassword, setNewPassword] = useState("")

  const currentUser = getStoredUser()

  // Prüfen ob User Admin ist
  useEffect(() => {
    if (!currentUser || currentUser.role !== "ADMIN") {
      window.location.href = "/dashboard"
    }
  }, [currentUser])

  const loadUsers = async () => {
    setLoading(true)
    setError(null)
    try {
      const data = await getAllUsers()
      setUsers(data)
    } catch (err) {
      setError("Benutzer konnten nicht geladen werden. Ist das Backend erreichbar?")
    } finally {
      setLoading(false)
    }
  }

  useEffect(() => {
    loadUsers()
  }, [])

  const handleCreateUser = async () => {
    if (!newUserEmail || !newUserPassword) {
      setError("Bitte füllen Sie alle Felder aus")
      return
    }

    try {
      await createUserByAdmin({
        email: newUserEmail,
        password: newUserPassword,
        role: newUserRole,
      })
      setNewUserEmail("")
      setNewUserPassword("")
      setNewUserRole("CITIZEN")
      setCreateDialogOpen(false)
      await loadUsers()
    } catch (err: any) {
      setError(err.message || "Benutzer konnte nicht erstellt werden")
    }
  }

  const handleChangeRole = async (userId: string) => {
    try {
      await changeUserRole(userId, selectedUserRole)
      setEditRoleDialogOpen(null)
      await loadUsers()
    } catch (err: any) {
      setError(err.message || "Rolle konnte nicht geändert werden")
    }
  }

  const handleChangePassword = async (userId: string) => {
    if (!newPassword) {
      setError("Bitte geben Sie ein neues Passwort ein")
      return
    }

    try {
      await changeUserPassword(userId, newPassword)
      setNewPassword("")
      setChangePasswordDialogOpen(null)
      await loadUsers()
    } catch (err: any) {
      setError(err.message || "Passwort konnte nicht geändert werden")
    }
  }

  const handleDeleteUser = async (userId: string) => {
    try {
      await deleteUser(userId)
      setDeleteDialogOpen(null)
      await loadUsers()
    } catch (err: any) {
      setError(err.message || "Benutzer konnte nicht gelöscht werden")
    }
  }

  const handleDeleteDemoData = async () => {
    try {
      const result = await deleteAllDemoData()
      setDeleteDemoDataDialogOpen(false)
      setError(null)
      await loadUsers()
      alert(`Demo-Daten erfolgreich gelöscht! ${result.deletedUsers} Demo-User wurden entfernt.`)
    } catch (err: any) {
      setError(err.message || "Demo-Daten konnten nicht gelöscht werden")
    }
  }

  const getUserRoleColor = (role: string) => {
    switch (role) {
      case "ADMIN":
        return "destructive"
      case "STAFF":
        return "secondary"
      default:
        return "outline"
    }
  }

  return (
    <div className="space-y-8">
      {/* Header */}
      <div className="flex flex-col gap-4 sm:flex-row sm:items-center sm:justify-between">
        <div>
          <h1 className="text-3xl font-bold tracking-tight">Benutzerverwaltung</h1>
          <p className="text-muted-foreground">
            Verwalten Sie alle Benutzer des Systems
          </p>
        </div>
        <div className="flex gap-2">
          <Button variant="outline" size="sm" onClick={loadUsers} disabled={loading}>
            <RefreshCw className={`mr-2 size-4 ${loading ? "animate-spin" : ""}`} />
            Aktualisieren
          </Button>
          <Dialog open={deleteDemoDataDialogOpen} onOpenChange={setDeleteDemoDataDialogOpen}>
            <DialogTrigger asChild>
              <Button variant="outline" size="sm" className="text-destructive hover:text-destructive">
                <Database className="mr-2 size-4" />
                Demo-Daten löschen
              </Button>
            </DialogTrigger>
            <DialogContent>
              <DialogHeader>
                <DialogTitle>Demo-Daten löschen</DialogTitle>
                <DialogDescription>
                  Möchten Sie wirklich alle Demo-Daten löschen?
                  <br />
                  <br />
                  <span className="text-destructive font-medium">
                    Diese Aktion kann nicht rückgängig gemacht werden. Alle Demo-User (erkennbar an "demo." oder "@example.com") und deren Feedbacks werden gelöscht.
                  </span>
                </DialogDescription>
              </DialogHeader>
              <DialogFooter>
                <Button variant="outline" onClick={() => setDeleteDemoDataDialogOpen(false)}>
                  Abbrechen
                </Button>
                <Button variant="destructive" onClick={handleDeleteDemoData}>
                  Alle Demo-Daten löschen
                </Button>
              </DialogFooter>
            </DialogContent>
          </Dialog>
          <Dialog open={createDialogOpen} onOpenChange={setCreateDialogOpen}>
            <DialogTrigger asChild>
              <Button size="sm">
                <Plus className="mr-2 size-4" />
                Neuer Benutzer
              </Button>
            </DialogTrigger>
            <DialogContent>
              <DialogHeader>
                <DialogTitle>Neuen Benutzer erstellen</DialogTitle>
                <DialogDescription>
                  Erstellen Sie einen neuen Benutzer mit E-Mail, Passwort und Rolle.
                </DialogDescription>
              </DialogHeader>
              <div className="grid gap-4 py-4">
                <div className="grid gap-2">
                  <Label htmlFor="email">E-Mail</Label>
                  <Input
                    id="email"
                    type="email"
                    placeholder="benutzer@example.com"
                    value={newUserEmail}
                    onChange={(e) => setNewUserEmail(e.target.value)}
                  />
                </div>
                <div className="grid gap-2">
                  <Label htmlFor="password">Passwort</Label>
                  <Input
                    id="password"
                    type="password"
                    placeholder="••••••••"
                    value={newUserPassword}
                    onChange={(e) => setNewUserPassword(e.target.value)}
                  />
                </div>
                <div className="grid gap-2">
                  <Label htmlFor="role">Rolle</Label>
                  <Select value={newUserRole} onValueChange={setNewUserRole}>
                    <SelectTrigger id="role">
                      <SelectValue />
                    </SelectTrigger>
                    <SelectContent>
                      <SelectItem value="CITIZEN">Bürger</SelectItem>
                      <SelectItem value="STAFF">Mitarbeiter</SelectItem>
                      <SelectItem value="ADMIN">Administrator</SelectItem>
                    </SelectContent>
                  </Select>
                </div>
              </div>
              <DialogFooter>
                <Button variant="outline" onClick={() => setCreateDialogOpen(false)}>
                  Abbrechen
                </Button>
                <Button onClick={handleCreateUser}>Erstellen</Button>
              </DialogFooter>
            </DialogContent>
          </Dialog>
        </div>
      </div>

      {/* Error Message */}
      {error && (
        <Card className="border-destructive">
          <CardContent className="pt-6">
            <div className="flex items-center gap-2 text-destructive">
              <AlertCircle className="size-4" />
              <p>{error}</p>
            </div>
          </CardContent>
        </Card>
      )}

      {/* Users Table */}
      <Card>
        <CardHeader>
          <CardTitle>Alle Benutzer</CardTitle>
          <CardDescription>
            Liste aller registrierten Benutzer mit ihren Rollen
          </CardDescription>
        </CardHeader>
        <CardContent>
          {loading ? (
            <div className="flex items-center justify-center py-12">
              <RefreshCw className="size-8 animate-spin text-muted-foreground" />
            </div>
          ) : users.length === 0 ? (
            <div className="flex flex-col items-center justify-center py-12 text-center">
              <Users className="size-12 text-muted-foreground mb-4" />
              <p className="text-lg font-medium">Noch keine Benutzer</p>
              <p className="text-muted-foreground mb-4">
                Erstellen Sie den ersten Benutzer, um loszulegen.
              </p>
            </div>
          ) : (
            <div className="rounded-md border">
              <Table>
                <TableHeader>
                  <TableRow>
                    <TableHead>E-Mail</TableHead>
                    <TableHead>Rolle</TableHead>
                    <TableHead className="text-right">Aktionen</TableHead>
                  </TableRow>
                </TableHeader>
                <TableBody>
                  {users.map((user) => {
                    const email = getUserEmail(user)
                    const userId = getUserId(user)
                    const currentUserId = currentUser ? getUserId(currentUser) : null
                    const isCurrentUser = userId === currentUserId
                    return (
                      <TableRow key={userId}>
                        <TableCell className="font-medium">{email}</TableCell>
                        <TableCell>
                          <Badge variant={getUserRoleColor(user.role) as any}>
                            {roleLabels[user.role] || user.role}
                          </Badge>
                        </TableCell>
                        <TableCell className="text-right">
                          <div className="flex justify-end gap-2">
                            <Dialog
                              open={editRoleDialogOpen === userId}
                              onOpenChange={(open) => {
                                setEditRoleDialogOpen(open ? userId : null)
                                if (open) {
                                  setSelectedUserRole(user.role)
                                }
                              }}
                            >
                              <DialogTrigger asChild>
                                <Button variant="outline" size="sm">
                                  <Edit className="mr-2 size-4" />
                                  Rolle ändern
                                </Button>
                              </DialogTrigger>
                              <DialogContent>
                                <DialogHeader>
                                  <DialogTitle>Rolle ändern</DialogTitle>
                                  <DialogDescription>
                                    Ändern Sie die Rolle für {email}
                                  </DialogDescription>
                                </DialogHeader>
                                <div className="grid gap-4 py-4">
                                  <div className="grid gap-2">
                                    <Label htmlFor="role-select">Rolle</Label>
                                    <Select
                                      value={selectedUserRole}
                                      onValueChange={setSelectedUserRole}
                                    >
                                      <SelectTrigger id="role-select">
                                        <SelectValue />
                                      </SelectTrigger>
                                      <SelectContent>
                                        <SelectItem value="CITIZEN">Bürger</SelectItem>
                                        <SelectItem value="STAFF">Mitarbeiter</SelectItem>
                                        <SelectItem value="ADMIN">Administrator</SelectItem>
                                      </SelectContent>
                                    </Select>
                                  </div>
                                </div>
                                <DialogFooter>
                                  <Button
                                    variant="outline"
                                    onClick={() => setEditRoleDialogOpen(null)}
                                  >
                                    Abbrechen
                                  </Button>
                                  <Button onClick={() => handleChangeRole(userId)}>
                                    Speichern
                                  </Button>
                                </DialogFooter>
                              </DialogContent>
                            </Dialog>
                            <Dialog
                              open={changePasswordDialogOpen === userId}
                              onOpenChange={(open) => {
                                setChangePasswordDialogOpen(open ? userId : null)
                                if (open) {
                                  setNewPassword("")
                                }
                              }}
                            >
                              <DialogTrigger asChild>
                                <Button variant="outline" size="sm">
                                  <Key className="mr-2 size-4" />
                                  Passwort ändern
                                </Button>
                              </DialogTrigger>
                              <DialogContent>
                                <DialogHeader>
                                  <DialogTitle>Passwort ändern</DialogTitle>
                                  <DialogDescription>
                                    Ändern Sie das Passwort für {email}
                                  </DialogDescription>
                                </DialogHeader>
                                <div className="grid gap-4 py-4">
                                  <div className="grid gap-2">
                                    <Label htmlFor="new-password">Neues Passwort</Label>
                                    <Input
                                      id="new-password"
                                      type="password"
                                      placeholder="••••••••"
                                      value={newPassword}
                                      onChange={(e) => setNewPassword(e.target.value)}
                                    />
                                  </div>
                                </div>
                                <DialogFooter>
                                  <Button
                                    variant="outline"
                                    onClick={() => setChangePasswordDialogOpen(null)}
                                  >
                                    Abbrechen
                                  </Button>
                                  <Button onClick={() => handleChangePassword(userId)}>
                                    Speichern
                                  </Button>
                                </DialogFooter>
                              </DialogContent>
                            </Dialog>
                            {!isCurrentUser && (
                              <Dialog
                                open={deleteDialogOpen === userId}
                                onOpenChange={(open) => {
                                  setDeleteDialogOpen(open ? userId : null)
                                }}
                              >
                                <DialogTrigger asChild>
                                  <Button variant="outline" size="sm" className="text-destructive hover:text-destructive">
                                    <Trash2 className="mr-2 size-4" />
                                    Löschen
                                  </Button>
                                </DialogTrigger>
                              <DialogContent>
                                <DialogHeader>
                                  <DialogTitle>Benutzer löschen</DialogTitle>
                                  <DialogDescription>
                                    Möchten Sie den Benutzer <strong>{email}</strong> wirklich löschen?
                                    <br />
                                    <br />
                                    <span className="text-destructive font-medium">
                                      Diese Aktion kann nicht rückgängig gemacht werden. Alle Feedbacks dieses Benutzers werden ebenfalls gelöscht.
                                    </span>
                                  </DialogDescription>
                                </DialogHeader>
                                <DialogFooter>
                                  <Button
                                    variant="outline"
                                    onClick={() => setDeleteDialogOpen(null)}
                                  >
                                    Abbrechen
                                  </Button>
                                  <Button
                                    variant="destructive"
                                    onClick={() => handleDeleteUser(userId)}
                                  >
                                    Löschen
                                  </Button>
                                </DialogFooter>
                              </DialogContent>
                            </Dialog>
                            )}
                          </div>
                        </TableCell>
                      </TableRow>
                    )
                  })}
                </TableBody>
              </Table>
            </div>
          )}
        </CardContent>
      </Card>
    </div>
  )
}

