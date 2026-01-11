## 2. Aufgabe 2 - CI/CD-Pipeline

### 2.1. Definition
- Eine CI/CD-Pipeline ist ein automatisierter Prozess, der die Erstellung, das Testen und die Bereitstellung von Software optimiert. Ziel ist es, Änderungen schnell, zuverlässig und reproduzierbar in produktionsreife Versionen zu überführen

- CI (Continuous Integration) bedeutet, dass Entwickler ihre Codeänderungen regelmäßig in gemeinsame Branches einpflegen.
Jede Änderung wird dabei automatisch gebaut und getestet, um Integrationsprobleme früh zu erkennen und sicherzustellen, dass der Code stets in einem stabilen und lauffähigen Zustand bleibt

- CD (Continuous Delivery) baut auf CI auf und stellt sicher, dass die getesteten und integrierten Änderungen automatisch für den Rollout vorbereitet werden.
Dadurch ist die Software jederzeit auslieferbar, da alle notwendigen Konfigurationen für eine Bereitstellung in beliebige Umgebungen vorhanden sind

- die 5 zentralen Komponenten in GitHub Actions:
1. **Events** lösen einen Workflow aus.
2. **Jobs** sind eine Gruppe von Arbeitsschritten (Steps) und können parallel oder sequenziell ablaufen.
3. **Steps** werden innerhalb eines Jobs einzeln durchlaufen, laufen im selben Runner und können Daten teilen.
4. **Actions** sind vordefinierte Befehle oder Skripte, welche in den Steps verwendet werden, um einen Job auszuführen.
5. **Runners** sind die Ausführungsumgebungen, auf denen die Jobs laufen. Diese Runner können von GitHub oder selbst gehostet werden.

### 2.2. Vor- und Nachteile
- Vorteile:
  - Höhere Benutzer-Zufriedenheit durch weniger Bugs
  - Produkte können schneller auf den Markt gebracht werden
  - Entlastung der Entwickler
- Nachteile:
  - Technische Infrastruktur muss erst geschaffen werden  

    


### 2.3. Protokoll

Die verschiedenen Tools wurden ausgiebig betrachtet. Sie unterscheiden sich in der Art der Bereitstellung (Selfhosting vs. Hosting in der Cloud), dem Integrationsgrad mit bestimmten Plattformen (z.B. GitHub, GitLab, Azure, AWS), der Benutzerfreundlichkeit, der Erweiterbarkeit und anderen Features.
So gibt es für unterschiedliche Teamgrößen und Anforderungen passende Lösungen.

Wir haben uns für GitHub Actions entschieden, da wir mit unserem Repository bereits auf Github arbeiten.
GitHub Actions ist bereits in GitHub integriert, es ist keine externe Konfiguration notwendig.

Das Setup und die Bedienung sind einfach. GitHub Actions ist schnell und direkt im Repository per YAML-Dateien konfigurierbar. Es ist optimal für Teams, die keine eigene CI-CD-Infrastruktur selbst betreiben wollen oder können.

Wir haben zunächst folgende Pipelines gebaut:
   
1. **java-ci.yml**
    Java-ci wird ausgeführt, wenn Änderungen im Source-Ordner vorgenommen werden. 
    Diese Pipeline sorgt für Linting (Superlinter), eine automatisierte Codeprüfung, Tests und die Dokumentation bei jedem Commit und jedem Pull Request.
  
2. **readme-pdf.yml**
Diese Pipeline wird ausgeführt, wenn:
    - Änderungen an der Readme-Datei in den feature-Branches gemacht werden
    - Änderungen an der Readme-Datei in der Main-Branch gemacht werden
    - Ein Pull Request gemacht wird.
Dies sorgt dafür, dass eine README.md-Datei automatisch in eine PDF-Datei umgewandelt wird, 
so dass immer eine aktuelle PDF-Version der Readme-Datei im Repository zu finden ist.


### 2.4. Dokumentation der Tests
test
Dauer der Workflows teilweise zu lang.
Konflikte beim erzwingen von Checks, wenn vom Feature Branch in den Main gemerged werden soll.
SuperLinter teilweise zu aggressiv beim linten.
Zunächst haben wir eine einzelne Pipeline für den Superlinter eingerichtet. Dann haben wir uns jedoch dafür entschieden diesen mit in die java-ci Pipeline zu integrieren, da diese spezifisch auf Java zugeschnitten ist und der Linter dort benötigt wird.
Außerdem wollten wir testen, ob in einer Pipeline mehrere Jobs laufen und dadurch Abhängigkeiten geschaffen werden können. (Ein Job abhängig von einem anderen)


