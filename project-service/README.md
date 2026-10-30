# Project Service

Standalone Spring Boot 3 microservice for managing enterprise projects. Built
with Java 21, Spring Data JPA, Bean Validation, Lombok, and an in-memory H2
database.

## Package structure

```text
com.enterprise.project
├── configuration
├── controller
├── dto
├── entity
├── exception
├── repository
├── service
│   └── impl
└── ProjectServiceApplication.java
```

## Run

Install Java 21 and Maven, then run from this directory:

```powershell
mvn spring-boot:run
```

The service listens on port `8082` by default. Override it with
`$env:SERVER_PORT = '8090'` before starting. The in-memory database resets when
the application stops. It registers as `project-service` with Eureka at
`http://localhost:8761/eureka/`; start the Discovery Server first. Override the
registry URL with `$env:EUREKA_SERVER_URL`. Run the integration tests with
`mvn test`.

## API

| Method | Path | Result |
|---|---|---|
| POST | `/api/projects` | Creates a project (`201 Created`, with `Location`) |
| GET | `/api/projects` | Lists all projects |
| GET | `/api/projects/{id}` | Returns a project |
| PUT | `/api/projects/{id}` | Replaces a project |
| DELETE | `/api/projects/{id}` | Deletes a project (`204 No Content`) |

`projectCode` is unique (case-insensitive). `clientId` and `projectManagerId`
are positive IDs owned by other services; this service stores them without
cross-service database relationships. `endDate` must be on or after
`startDate`, and budget cannot be negative. Invalid input returns `400`, missing
projects return `404`, and duplicate project codes return `409`.

Swagger UI is available at `http://localhost:8082/swagger-ui.html`; the OpenAPI
document is at `http://localhost:8082/api-docs`.
