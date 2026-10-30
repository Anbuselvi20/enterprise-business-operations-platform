# Gateway Service

Spring Cloud Gateway API gateway targeting Java 21. It routes the platform APIs,
logs request metadata without logging request bodies or authorization headers,
and validates JWTs before forwarding protected requests.

## Routes

| Public path | Upstream | Rewritten upstream path |
|---|---|---|
| `/auth/**` | `employee-service` | `/api/auth/**` |
| `/employees/**` | `employee-service` | `/api/employees/**` |
| `/clients/**` | `client-service` | `/api/clients/**` |
| `/projects/**` | `project-service` | `/api/projects/**` |

Auth registration and login are public. Other routes require a valid
`Authorization: Bearer <token>` header. Tokens must be signed with the same
Base64-encoded HMAC secret as the auth service and have issuer
`enterprise-employee-service`.

## Run

Set `JWT_SECRET` to the same secure Base64-encoded key configured for the auth
service (at least 32 decoded bytes), then run:

```powershell
mvn spring-boot:run
```

By default, the gateway listens on port `8085`; auth and employee routes use
Eureka service discovery. Start the Discovery Server and the services before
starting the gateway. Override the registry URL with `EUREKA_SERVER_URL`.
Override any upstream with `AUTH_SERVICE_URI`, `EMPLOYEE_SERVICE_URI`,
`CLIENT_SERVICE_URI`, or `PROJECT_SERVICE_URI`; override the gateway port with
`SERVER_PORT`. By default, auth and employee routes use the registered
`employee-service`, because auth endpoints are hosted by that app in this
workspace.

Run tests with `mvn test`.
