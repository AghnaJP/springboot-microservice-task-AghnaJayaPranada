# springboot-microservice-task-AghnaJayaPranada

A **Book Management** REST microservice built with **Spring Boot 3**, **Spring Data JPA**, and **PostgreSQL**, providing full CRUD operations on a `Book` resource.

## Tech Stack

- Java 17
- Spring Boot 3.3.2 (Web, Data JPA, Validation)
- PostgreSQL 16
- Flyway (database migrations)
- Maven (project/build)
- Lombok
- springdoc-openapi (Swagger UI)
- JUnit, Mockito
- Docker

## Project Structure

```
src/main/java/com/aghna/bookmanagement
├── BookManagementApplication.java   # entry point
├── controller/BookController.java   # REST endpoints
├── service/                         # business logic (interface + impl)
├── repository/BookRepository.java   # Spring Data JPA repository
├── model/Book.java                  # JPA entity
├── dto/                             # request/response payloads
├── mapper/BookMapper.java           # entity <-> DTO conversion
└── exception/                       # custom exceptions + @RestControllerAdvice

src/main/resources
├── application.yml                  # config
└── db/migration/V1__create_books_table.sql

src/test/java/...                    # unit tests
docs/er-diagram.{md,png}.            # ER diagram
postman/                             # Postman collection + environment
docker-compose.yml                   # local PostgreSQL for development
```

## How to Run the Project

### 1. Prerequisites

* JDK 17+
* Maven 3.9+ (or use the included Maven Wrapper: `./mvnw`)
* **Either:**

  * Docker + Docker Compose (recommended), or
  * PostgreSQL 14+ installed locally

### 2. Start PostgreSQL

#### Option A: Using Docker Compose (Recommended)

Run:

```bash
docker compose up -d
```

This starts a PostgreSQL 16 container with the following configuration:

* **Host:** `localhost`
* **Port:** `5432`
* **Database:** `bookdb`
* **Username:** `postgres`
* **Password:** `postgres`

##### Why no environment variables?

Since this project is a take-home assessment intended for local evaluation, the default database configuration is intentionally fixed so the application can be cloned and run with minimal setup.

But in a production environment, these values would be externalized using environment variables, Spring profiles, or a centralized configuration service.

#### Option B: Using an Existing PostgreSQL Installation

If you already have PostgreSQL installed, create a database with the following default configuration:

* **Database:** `bookdb`
* **Username:** `postgres`
* **Password:** `postgres`

If your local PostgreSQL uses different credentials, update the datasource configuration in `src/main/resources/application.yml`:

```yaml
spring:
  datasource:
    url: jdbc:postgresql://localhost:5432/bookdb
    username: your_username
    password: your_password
```

Ensure the PostgreSQL server is running before starting the application.

### 3. Build and Run

Build the project and start the application:

```bash
mvn clean install
mvn spring-boot:run
```

Alternatively, build the executable JAR and run it directly:

```bash
mvn clean package
java -jar target/bookmanagement-0.0.1-SNAPSHOT.jar
```

On startup, Flyway automatically applies the database migration (`V1__create_books_table.sql`), so no manual schema creation is required.

The API will be available at:

```
http://localhost:8080
```

### 4. Run Tests

Execute the unit test with:

```bash
mvn test
```

### 5. API Documentation

Once the application is running, Swagger UI is available at:

```
http://localhost:8080/swagger-ui.html
```

## API Endpoints

| Method | Endpoint          | Description                           |
|--------|-------------------|---------------------------------------|
| POST   | `/api/books`      | Add a new book                        |
| GET    | `/api/books`      | Get all books                         |
| GET    | `/api/books/{id}` | Get a book by ID                      |
| PUT    | `/api/books/{id}` | Update a book by ID (full update)     |
| PATCH  | `/api/books/{id}` | Partial update of a book (e.g. title) |
| DELETE | `/api/books/{id}` | Delete a book                         |


### Book Entity

```json
{
  "id": 1,
  "title": "Harry Potter",
  "author": "JK Rowling",
  "isbn": "1234567891",
  "publishedDate": "2004-07-08",
  "createdAt": "2026-07-08T10:00:00",
  "updatedAt": "2026-07-08T10:00:00"
}
```

`isbn` is unique — creating or updating a book with an ISBN that already belongs to another book returns `409 Conflict`.

### Error Response Format

All errors share a consistent shape:

```json
{
  "timestamp": "2026-07-08T10:00:00",
  "status": 404,
  "error": "Not Found",
  "message": "Book with id 999 not found",
  "path": "/api/books/999",
  "details": []
}
```

| Status | When                                              |
|--------|---------------------------------------------------|
| 400    | Validation failure or malformed JSON              |
| 404    | Book with the given id does not exist             |
| 409    | ISBN already belongs to another book              |
| 500    | Unexpected server error                           |