# Client Service

Standalone Spring Boot 3 microservice for managing client records. Built with
Java 21, Spring Data JPA, Bean Validation, Lombok, and an in-memory H2 database.

## Package structure

```text
com.enterprise.client
├── configuration
├── controller
├── dto
├── entity
├── exception
├── repository
├── service
│   └── impl
└── ClientServiceApplication.java
```

## Run

Install Java 21 and Maven, then run from this directory:

```powershell
mvn spring-boot:run
```

By default, the service listens on port `8081`. Override it with
`$env:SERVER_PORT = '8090'` before starting. The in-memory database resets when
the application stops. It registers as `client-service` with Eureka at
`http://localhost:8761/eureka/`; start the Discovery Server first. Override the
registry URL with `$env:EUREKA_SERVER_URL`.

Run integration tests with:

```powershell
mvn test
```

## API

| Method | Path | Result |
|---|---|---|
| POST | `/api/clients` | Creates a client (`201 Created`, with `Location`) |
| GET | `/api/clients` | Lists all clients |
| GET | `/api/clients/{id}` | Returns a client |
| PUT | `/api/clients/{id}` | Replaces a client |
| DELETE | `/api/clients/{id}` | Deletes a client (`204 No Content`) |

Email addresses are normalized to lowercase and must be unique. Invalid request
fields return `400`, missing clients return `404`, and duplicate emails return
`409`. Error bodies contain timestamp, HTTP status, message, path, and field
validation errors. The `status` field accepts `ACTIVE` or `INACTIVE`.

Swagger UI is available at `http://localhost:8081/swagger-ui.html`; the OpenAPI
document is at `http://localhost:8081/api-docs`.
