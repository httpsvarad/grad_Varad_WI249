# Spring Boot Multiple Database (H2 + PostgreSQL)

This project shows how to configure and use:

* Multiple DataSources
* Separate repository packages
* Database-specific configuration classes

The application saves student data into both **H2** and **PostgreSQL** databases.

---

## Project Structure

```
com.example.demo
│
├── config
│   ├── h2Config.java
│   └── postgreConfig.java
│
├── controllers
│   └── StdController.java
│
├── entities
│   └── Student.java
│
├── repos
│   ├── h2
│   └── psql
│
├── service
│   └── StudentService.java
│
└── SpringMultipleDbsApplication.java
```

---

## Features

* Save `Student` entity to H2 database
* Save `Student` entity to PostgreSQL database
* Separate repository layers for each database
* Service-layer transaction handling using @Transactional
* Clean layered architecture (Controller → Service → Repository)

---

## Transaction Management

The project uses @Transactional in the service layer to ensure:

* Both database operations run within a transactional boundary
* Data consistency between H2 and PostgreSQL
* Rollback in case of failure during the save operation

---

## Configuration

```properties
spring.application.name=spring_multiple_dbs

spring.jta.enabled=true
server.servlet.session.tracking-modes=cookie

#H2 Database
spring.datasource.primary.jdbc-url=jdbc:h2:mem:h2db
spring.datasource.primary.username=sa
spring.datasource.primary.password=
spring.datasource.primary.driver-class-name=org.h2.Driver
spring.h2.console.enabled=true

#PostgreSQL Database
spring.datasource.secondary.jdbc-url=jdbc:postgresql://localhost:5432/postgres_db
spring.datasource.secondary.username=
spring.datasource.secondary.password=
spring.datasource.secondary.driver-class-name=org.postgresql.Driver

```
