## Softwaredesign mit Eventsourcing - Modellierung und Testing von eventsourcing basierten Systemen

Video: https://youtu.be/2oPX5EF1jwE

### Build / Start

Initialer Build

```
mvn package
```

```
docker-compose up -d
mvn spring-boot:run
```

Zugriff über http://localhost:8080

### Setup

Slices sind im _root_ Package (wie im Generator angegeben) als Packages definiert.

### Todos nach der initialen Generierung

Ihre Code Richtlinien sind natürlich führend, daher ist es erwartungskonform dass Code
nicht sofort kompiliert (es sollten aber wirklich nur kleine Anpassungen notwendig sein).

### Start der Applikation

Zum Start des Services kann die Klasse _ApplicationStarter_ verwendet werden in _src/test/kotlin_.
Warum in _test_?

Diese Klasse startet die komplette Umgebung (inkl. Postgres und ggf. Kafka über TestContainers)

### Package Struktur

Events sind im Package "events"

Aggregates liegen im Package "domain"

Slices haben jeweils ein isoliertes Package <sliceName>

Package "common" enthält einige Interfaces für die generelle Struktur.

## UI

Damit die UI mitkompiliert wird _mvn clean package_ ausführen.
Zugriff über _localhost:8080_ im Browser

Nebulit GmbH 2024

https://www.nebulit.de

