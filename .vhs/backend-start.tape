Output .vhs/backend-start.gif

Set FontSize 14
Set Width 1000
Set Height 600
Set Theme "Catppuccin Mocha"
Set Padding 20
Set TypingSpeed 75ms

# Titel
Type echo '"City Feedback - Backend starten"'
Enter
Sleep 500ms
Enter

# Schritt 1: Ins Projektverzeichnis wechseln
Hide
Type "cd CityFeedback"
Enter
Show
Sleep 500ms

# Schritt 2: Backend bauen
Hide
Type "./mvnw clean install"
Enter
Show
Sleep 25s

# Schritt 3: Backend starten
Hide
Type "./mvnw spring-boot:run"
Show
Sleep 3s
Enter
Show
Sleep 10s
