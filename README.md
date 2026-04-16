# Reflektion om skalbarhet

## Inledning

Systemet fungerar bra vid låg belastning. När fler användare använder API:et samtidigt kan prestandan påverkas och systemet kan bli långsammare.

---

## Hög belastning och problem

Vid hög belastning, när många användare skickar requests samtidigt, kan det uppstå problem i systemet. Detta gäller särskilt när flera användare försöker låna samma bok samtidigt.

---

## Concurrency och lånefunktion

En viktig del i systemet är lånefunktionen.

Om flera användare försöker låna samma bok samtidigt kan det uppstå en så kallad *race condition*. Detta kan leda till att flera lån skapas för samma bok, vilket inte är korrekt.

För att lösa detta problem är det viktigt att använda:

* Transaktioner (`@Transactional`)
* Låsning (locking)

Detta säkerställer att endast ett lån kan skapas åt gången för en bok.

---

## Flaskhals: Databasen

Den största flaskhalsen i systemet är databasen.

Alla requests behöver:

* läsa data (GET)
* skriva data (POST)

Vid många samtidiga requests kan databasen bli långsam och påverka hela systemets prestanda.

---

## Caching

Caching kan användas för att förbättra prestandan.

Det passar bra för endpoints som läses ofta, till exempel:

* Hämta alla böcker
* Hämta authors

Men caching bör inte användas för lånefunktioner, eftersom datan där måste vara uppdaterad i realtid.

---

## Sammanfattning

Systemet fungerar bra för mindre användning.

Vid hög belastning behöver man ta hänsyn till:

* Databasens prestanda
* Concurrency-problem
* Användning av caching

Genom att hantera dessa delar kan systemet bli mer skalbart och stabilt.
