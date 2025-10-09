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
   
   - Zweck eines Branches:
      - Ein Branch ist ein separater Entwicklungszweig, der paralleles Arbeiten ermöglicht, ohne den main-Branch zu verändern
      - Dadurch können mehrere Personen gleichzeitig an verschiedenen Features oder Bugfixes arbeiten, ohne sich gegenseitig zu stören, bevor diese dann in den main-Branch integriert werden
   - Wichtige Befehle für Branches:
      - git branch 
        (Zeigt an, in welchem Branch man sich derzeit befindet)
      - git branch name-des-branches 
        (Erstellt einen neuen Branch)
      - git checkout name-des-branches 
        (Wechseln in einen anderen Branch)
      - git branch -d name-des-branches
        (Einen Branch löschen)
         
   - Zurückführen des Branches in den main-Branch (PullRequest):
      - Mit einem Pull Request können Änderungen aus einem Branch in den main-Branch übernommen werden
      - Ein Pull Request dient zur Überprüfung und Freigabe des Codes durch andere Teammitglieder
      - Der Pull Request kann in der Weboberfläche (z. B. GitHub) erstellt werden: Dort kann eine Beschreibung hinzugefügt und ein Reviewer ausgewählt werden
      - Nach Freigabe kann der Branch in den main-Branch gemergt werden
   - Wichtige Befehle:
      - git push origin name-des-branches
        (Branch auf Remote-Repository hochladen, um Pull Request zu erstellen)
        
   - Mit Merge-Konflikten umgehen:
      - Ein Merge-Konflikt entsteht, wenn Git Änderungen aus verschiedenen Branches nicht automatisch zusammenführen kann, z.B. wenn dieselbe Datei in den Branches unterschiedlich geändert wurde
      - Git zeigt die betroffenen Dateien und markiert Konfliktstellen, diese können manuell z.B direkt in der IDE gelöst werden
      - Nach der Anpassung wird die Datei mit git add markiert und der Merge mit git commit abgeschlossen
   - Wichtige Befehle:
      - git merge name-des-branches
        (Führt die Änderungen aus dem angegebenen Branch in den aktuellen Branch ein)
      - git status
        (Zeigt, welche Dateien Konflikte enthalten oder gelöst wurden)

   ![Branches in GitHub](https://axolo.s3.eu-west-3.amazonaws.com/communication/blog/ultimate-pull-request/Branches+in+GitHub.png)



6. Git mit IntelliJ/PyCharm benutzen: Local Repository und Remote Repository
