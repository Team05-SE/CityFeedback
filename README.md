Moderne Softwareentwicklung



vorgelegt von: 

1. Matthias Lindner (matthias.lindner@th-brandenburg.de)
2. Fenna Haan  (fenna.haan@stud.hs-emden-leer.de)
3. Janne Surborg (ja.surborg@ostfalia.de)
4. Constantin Moye (wgtz1919@bht-berlin.de)
5. Cornelia Demes (cornelia.demes@stud.hs-emden-leer.de)


vorgelegt am: 13.10.2025


1. Was ist ein Git und warum sollte es verwendet werden?

2. Grundlegende Git-Befehle

3. Nützliche Git-Tools und Plattformen (z.B. GitHub)

4. Branches und ihre Nutzung, Umgang mit Merge-Konflikten
   - Definition:
     Ein Branch ist ein separater Entwicklungszweig, dieser ermöglicht paralleles Arbeiten ohne den main-Branch zu verändern
   - Vorteil:
     Dadurch können mehrere Personen gleichzeitig an verschiedenen Features oder Bugfixes arbeiten, ohne sich gegenseitig zu stören, bevor diese dann in den Hauptzweig integriert werden
   - Wichtige Befehle:
     -   git branch 
         (Zeigt an, in welchem Branch man sich derzeit befindet)
     -   git branch name-des-branches 
         (Erstellt einen neuen Branch)
     -   git checkout name-des-branches 
         (Wechseln in einen anderen Branch)
     -   git branch -d name-des-branches
         (Einen Branch löschen)


6. Git mit IntelliJ/PyCharm benutzen: Local Repository und Remote Repository
