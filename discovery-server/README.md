# Discovery Server

Netflix Eureka server for service registration and discovery, using Spring Boot
3, Spring Cloud Netflix, and Java 21.

## Run

```powershell
mvn spring-boot:run
```

The Eureka dashboard is available at `http://localhost:8761`. The Eureka
registry endpoint is `http://localhost:8761/eureka/`. Configure the server
port with `SERVER_PORT`, the advertised hostname with `EUREKA_HOSTNAME`, or the
registry URL with `EUREKA_SERVER_URL`.

The Employee, Client, Project, and Gateway projects are configured as Eureka
clients. Start this server before starting those services. In this repository,
authentication endpoints are part of `employee-service`; the gateway sends
both auth and employee routes to that registered service.
