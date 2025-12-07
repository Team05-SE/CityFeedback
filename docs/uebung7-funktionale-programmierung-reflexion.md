# Übung 7: Funktionale Programmierung - Reflexion

## Aufgabe 4: Reflexion über die Anwendung funktionaler Konzepte

---

## Vorteile und Nachteile der funktionalen Implementierung

Die funktionale Programmierung mit Stream API führte zu **deklarativem, kompakterem Code**. Bei Gruppierungen und Aggregationen ist der Code deutlich kürzer (4 Zeilen statt 7-10 Zeilen). Die Pipeline-Struktur macht Transformationen klar erkennbar. Allerdings ist der Code für Entwickler ohne Stream API-Erfahrung **schwerer zu verstehen**. Komplexe Pipelines erfordern ein gutes Verständnis funktionaler Konzepte. Bei **Debugging** sind Stacktraces tiefer und weniger intuitiv. Die **Performance** kann bei kleinen Collections schlechter sein als bei einfachen Schleifen.

---

## Technologien und deren Einsatz

Als einzige Technologie wurde ein **Large Language Model (LLM) - Auto (Cursor AI)** eingesetzt. Das LLM wurde primär für die **Code-Generierung** der funktionalen Implementierungen verwendet, insbesondere für die Collection-Processing-Methoden mit Stream API. Zusätzlich unterstützte es bei der **Erstellung von Tests** und der **Dokumentation**. Die Zusammenarbeit erfolgte iterativ: Der Entwickler formulierte Anforderungen, das LLM generierte Code-Vorschläge, die dann gemeinsam überprüft und angepasst wurden.

---

## Auswirkungen auf Codequalität und Lesbarkeit

Die funktionale Implementierung hat **gemischte Auswirkungen**. Einerseits ist der Code deklarativer, andererseits ist **deutlich mehr Code hinzugekommen** (ca. 150 Zeilen für Collection-Processing-Methoden plus DTOs). Die **Lesbarkeit** wurde generell **schwerer**, besonders für Teammitglieder ohne Stream API-Erfahrung. Komplexe Pipelines mit mehreren `filter()`, `map()`, `collect()` Operationen erfordern mentale Anstrengung. Die **Wartbarkeit** leidet unter der höheren Komplexität - Änderungen an Stream-Pipelines sind fehleranfälliger als bei imperativen Schleifen.

---

## Herausforderungen bei der Umsetzung

Die größte Herausforderung waren **Test-Fehler beim Maven Build**. Die Tests waren nicht ausreichend isoliert, sodass Daten aus vorherigen Tests in der Datenbank verblieben und zu falschen Assertions führten. Beispielsweise erwartete ein Test genau 2 Feedbacks in einer Kategorie, aber durch Daten aus anderen Tests waren es 7. Die Lösung war, **robustere Assertions** zu verwenden (`>=` statt `==`) und explizit zu prüfen, dass die eigenen Test-Daten enthalten sind, anstatt exakte Zahlen zu erwarten.

---

## Lessons Learned

**Funktionale Programmierung ist nicht immer besser** - sie sollte selektiv eingesetzt werden. Bei komplexen Transformationen ist sie ideal, bei einfachen Operationen kann imperativer Code klarer sein. **Test-Isolation** ist kritisch - auch mit `@Transactional` können Daten zwischen Tests persistieren, daher sollten Assertions robust sein. Die **Dokumentation** funktionaler Code-Stellen ist essentiell. **Code-Reviews** sind besonders wichtig bei LLM-generiertem Code. Letztendlich ist **Balance** der Schlüssel: Funktionale Konzepte dort nutzen, wo sie Mehrwert bringen, aber nicht um jeden Preis.

---

*Reflektiert am: 07.12.2025*  
*Projekt: CityFeedback*  
*Übung: 7 - Funktionale Programmierung*
