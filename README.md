# BookVault - Library Management System (Spring Boot 3)

Backend API for BookVault with JWT security, role-based access, validation, transactional loan handling, async overdue events, caching, and automated tests.

## Tech Stack

- Java 17
- Spring Boot 3.x
- Spring Data JPA
- Spring Security + JWT
- H2 / PostgreSQL (configurable)
- Caffeine Cache
- Maven

## Features Implemented

### Part 1 - Core REST API

- Books:
  - `GET /api/books`
  - `POST /api/books`
  - `PUT /api/books/{id}`
  - `DELETE /api/books/{id}`
- Members:
  - `GET /api/members/search?q=...`
  - `GET /api/members/{id}/loans`
- Loans:
  - `POST /api/loans` (borrow)
  - `PUT /api/loans/{id}/return`
  - `GET /api/loans/overdue` (pageable + sortable)

All APIs return a consistent response envelope:

```json
{
  "data": {},
  "error": null,
  "timestamp": "2026-04-15T10:00:00"
}
```

Global exception handling is implemented using `@ControllerAdvice` with meaningful HTTP status codes.

### Part 2 - Validation & Data Integrity

- Bean validation on request DTOs (`@NotBlank`, `@Email`, `@Min`, `@Pattern`)
- ISBN format enforced: `978-XXXXXXXXXX`
- Business rule enforced in service layer: max 3 active loans per member
- Borrow/return methods are transactional (`@Transactional`)
- Rollback behavior validated through integration testing

### Part 3 - Security

- JWT-based authentication for secured endpoints
- `POST /api/auth/login` returns signed JWT
- Role-based access:
  - `LIBRARIAN`: full access
  - `MEMBER`: view books, view own loans, return own books
- Ownership checks are enforced in service layer for member loan access
- Seed users loaded via `data.sql` (no registration flow)

### Part 4 - Query & Search

- Dynamic book filtering using Spring Data Specification:
  - `GET /api/books?genre=&author=&available=true`
- Member search by name/email:
  - `GET /api/members/search?q=...`
- Overdue loans endpoint supports pagination + sorting

### Part 5 - Async & Events

- On overdue-loans fetch, overdue events are published
- Async listener (`@Async` + `@EventListener`) logs notification sent message

### Part 6 - Caching

- Book catalogue (`GET /api/books`) cached with Caffeine using `@Cacheable`
- Cache evicted on book create/update/delete using `@CacheEvict(allEntries = true)`

## Cache Eviction Strategy

Catalogue responses depend on book metadata and availability. Any book write operation can affect multiple read queries (genre, author, availability filters), so the safest strategy is full cache eviction on each write:

- `createBook` -> evict all catalogue entries
- `updateBook` -> evict all catalogue entries
- `deleteBook` -> evict all catalogue entries

This avoids stale catalogue results with minimal complexity.

## How to Run

### Prerequisites

- Java 17+
- Maven 3.9+

### Current default profile

- Default runtime DB is **H2 in-memory** from `application.properties`.
- This is the reviewer-friendly mode (no DB setup needed).

### Run commands (recommended)

Use Maven Wrapper to match assignment requirement:

- Windows (PowerShell):
  - `.\mvnw.cmd spring-boot:run`
- Linux/Mac:
  - `./mvnw spring-boot:run`

### Switch to PostgreSQL profile (your local setup)

```bash
.\mvnw.cmd spring-boot:run -Dspring-boot.run.profiles=postgres
```

Linux/Mac equivalent:

```bash
./mvnw spring-boot:run -Dspring-boot.run.profiles=postgres
```

### How to switch back

- PostgreSQL -> H2: remove the `-Dspring-boot.run.profiles=postgres` flag
- H2 -> PostgreSQL: add `-Dspring-boot.run.profiles=postgres`

### Database configuration notes

- Default profile uses in-memory H2 (no setup required).
- `postgres` profile uses PostgreSQL and is environment-configurable via:

- `DB_URL`
- `DB_USERNAME`
- `DB_PASSWORD`
- `DB_DRIVER`

Example PostgreSQL settings:

```bash
DB_URL=jdbc:postgresql://localhost:5432/bookvault
DB_USERNAME=postgres
DB_PASSWORD=root
DB_DRIVER=org.postgresql.Driver
```

## Seeded Users

Loaded from `src/main/resources/data.sql`:

- Librarian:
  - username: `librarian`
  - password: `librarian123`
  - role: `LIBRARIAN`
- Member:
  - username: `member1@example.com`
  - password: `member123`
  - role: `MEMBER`

> Note: current project uses `NoOpPasswordEncoder` for assignment simplicity (plain text passwords).

## Testing

Implemented tests include:

- `@WebMvcTest` for controller with mocked service (`LoanControllerWebMvcTest`)
- `@DataJpaTest` for Specification/repository filtering (`BookRepositoryDataJpaTest`)
- `@SpringBootTest` integration test covering full borrow -> return flow (`LoanFlowIntegrationTest`)
- Negative scenario: borrow when `availableCopies = 0` and assert error response

Run tests:

```bash
mvn test
```

## Design Decisions

- Kept layered architecture (`controller -> service -> repository`)
- Enforced core business rules in service layer
- Used Specifications for scalable dynamic filtering
- Added async event listener for non-blocking notification simulation
- Chose response envelope consistency for client-side predictability

## Trade-offs

- `NoOpPasswordEncoder` used for speed and assignment focus (not production-safe)
- Event notification currently logs only (no external notification provider)
- Cache eviction uses full-clear strategy over fine-grained key invalidation for correctness/simplicity

## Improvements With More Time

- Replace `NoOpPasswordEncoder` with BCrypt + password migration
- Add refresh tokens and token revocation/blacklist support
- Add Flyway migrations for versioned schema and seed evolution
- Add dedicated member CRUD endpoints if required as full member resource management
- Add audit logging and observability (metrics/tracing)
- Add stricter integration tests for role authorization matrices

