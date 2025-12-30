Output .vhs/frontend-start.gif

Set FontSize 14
Set Width 1000
Set Height 250
Set Theme "Catppuccin Mocha"
Set Padding 20
Set TypingSpeed 75ms

# Titel
Type "echo 'City Feedback - Frontend starten'"
Enter
Sleep 500ms
Enter

# Schritt 1: In Frontend-Ordner wechseln
Hide
Type "cd frontend"
Enter
Show
Sleep 1s

# Schritt 2: Abhängigkeiten installieren
Hide
Type "bun install"
Enter
Show
Sleep 2s

# Schritt 3: Dev-Server starten
Hide
Type "bun dev"
Show
Sleep 2s
Enter
Show
Sleep 3s
