# Bibliotekssystem API

## Inledning

Detta projekt är ett REST API för ett bibliotekssystem byggt med Spring Boot.

Systemet hanterar:

* Böcker
* Authors
* Loans

API:et är byggt med fokus på:

* DTO-användning
* Lagerstruktur (Controller → Service → Repository)
* Validation
* Security
* Pagination
* Caching
* Concurrency-hantering

---

# Funktioner

## Books API

### Endpoints

* `GET /api/v1/books`
* `GET /api/v1/books/{id}`
* `POST /api/v1/books`

### Funktionalitet

* Hämta alla böcker
* Hämta bok via ID
* Skapa nya böcker
* Pagination och sorting
* Caching på bokhämtning

---

## Authors API

### Endpoints

* `POST /api/v1/authors`
* `GET /api/v1/authors/{id}`
* `GET /api/v1/authors/{id}/books`

### Funktionalitet

* Skapa author
* Hämta author via ID
* Hämta alla böcker för en author

---

## Loans API

### Endpoints

* `GET /api/v1/loans`
* `POST /api/v1/loans`

### Funktionalitet

* Skapa lån
* Hämta lån
* Skydd mot dubbla lån

---

# Teknologier

Projektet använder:

* Java 17
* Spring Boot
* Spring Data JPA
* Spring Security
* H2 Database
* Swagger / OpenAPI
* Spring Cache
* Bucket4j (Rate Limiting)

---

# Security

Systemet använder Spring Security.

Alla requests kräver autentisering.

### Login

```text
Username: admin
Password: admin123
```

Swagger fungerar med authentication via "Authorize"-knappen.

---

# Validation

Validation används för att säkerställa korrekt input.

Exempel:

* Tom titel är inte tillåten
* Negativt årtal är inte tillåtet

Vid fel returneras:

```text
400 Bad Request
```

---

# Pagination och Sorting

Pagination används för att förbättra prestanda vid stora datamängder.

Exempel:

```text
GET "/api/v1/books?page=0&size=5&sortBy=id"
```

---

# Caching

Caching används för att förbättra prestandan.

Caching används på:

```text
GET /api/v1/books/{id}
```

Detta minskar antalet databas-anrop och förbättrar svarstiden.

---

# Rate Limiting

Systemet använder Bucket4j för att begränsa antalet requests från samma IP-adress.

Om för många requests skickas returneras:

```text
429 Too Many Requests
```

---

# Swagger

Swagger används för att testa API:et.

Swagger URL:

```text
http://localhost:8080/swagger-ui/index.html
```

---

# Tester

Projektet innehåller integrationstester för:

* Books API
* Loans API
* Validation
* Concurrency
* Pagination
* Security

---

# Reflektion om skalbarhet

## Inledning

Systemet fungerar bra vid låg belastning. När fler användare använder API:et samtidigt kan prestandan påverkas och systemet kan bli långsammare.

---

## Hög belastning och problem

Vid hög belastning, när många användare skickar requests samtidigt, kan det uppstå problem i systemet. Detta gäller särskilt när flera användare försöker låna samma bok samtidigt.

---

## Concurrency och lånefunktion

En viktig del i systemet är lånefunktionen.

Om flera användare försöker låna samma bok samtidigt kan det uppstå en så kallad race condition. Detta kan leda till att flera lån skapas för samma bok, vilket inte är korrekt.

För att lösa detta problem används:

* `@Transactional`
* Kontroll av aktiva lån
* Concurrency-testning

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

Caching används för att förbättra prestandan.

Det passar bra för endpoints som läses ofta, till exempel:

* Hämta böcker
* Hämta authors

Men caching bör inte användas för lånefunktioner, eftersom datan där måste vara uppdaterad i realtid.

---

# Performance Test

En enkel prestandatest genomfördes före och efter caching.

## Resultat

| Test | Response Time |
|---|---|
| Före caching | ~40 ms |
| Efter caching | ~8 ms |

Caching förbättrade svarstiden och minskade belastningen på databasen.

---

# Sammanfattning

Systemet fungerar bra för mindre användning.

Vid hög belastning behöver man ta hänsyn till:

* Databasens prestanda
* Concurrency-problem
* Caching
* Rate limiting
* Pagination

Genom att hantera dessa delar kan systemet bli mer skalbart och stabilt.

---

# Starta projektet

## Kör applikationen

Projektet kan köras direkt från IntelliJ genom att starta:

```text
BibliotekssystemApplication
http://localhost:8080/swagger-ui/index.html
---
# GitHub Repository

Projektet laddades upp till GitHub som en del av inlämningen.