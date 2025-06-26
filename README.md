# Book Management System

A simple full-stack application for managing books and library members. Built with Spring Boot (backend), React (frontend), and PostgreSQL.

---

##  Run the whole project with Docker Compose

1. **Requirements:**
   - [Docker](https://www.docker.com/) and [Docker Compose](https://docs.docker.com/compose/) installed

2. **Build and start all services:**
   ```sh
   docker-compose up --build
   ```

3. **Access the app:**
   - Frontend: [http://localhost:3000](http://localhost:3000)
   - Backend API: [http://localhost:8080](http://localhost:8080)
   - PostgreSQL: port 5432 (user: `postgres`, password: `root`, db: `book-management`)

4. **Stop all services:**
   ```sh
   docker-compose down
   ```

---

## Run backend and frontend separately (for development)

### 1. Backend (Spring Boot)

- **Requirements:** Java 17+, Maven, PostgreSQL running locally
- **Configure database:**
  - In `backend/src/main/resources/application.properties` set:
    ```properties
    spring.datasource.url=jdbc:postgresql://localhost:5432/db
    spring.datasource.username=user
    spring.datasource.password=pass
    spring.jpa.hibernate.ddl-auto=update
    spring.jpa.show-sql=true
    spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.PostgreSQLDialect
    ```
- **Start backend:**
  ```sh
  cd backend
  mvn spring-boot:run
  ```
- **API available at:** [http://localhost:8080](http://localhost:8080)

### 2. Frontend (React)

- **Requirements:** Node.js 18+ and npm
- **Start frontend:**
  ```sh
  cd frontend
  npm install
  npm start
  ```
- **App available at:** [http://localhost:3000](http://localhost:3000)

---

### 3. PostgreSQL 

- Install PostgreSQL 17+ from [official site](https://www.postgresql.org/download/)
- Create database:
  ```sql
  CREATE DATABASE book-management;

  CREATE TABLE IF NOT EXISTS books(
    id bigserial primary key,
    title varchar(255),
    author varchar(255),
    amount int DEFAULT 0,
    amount_of_borrowed_books int DEFAULT 0,
    UNIQUE(title, author)
  )

  CREATE TABLE IF NOT EXISTS users(
    id bigserial primary key,
    name varchar(255) UNIQUE,
    membership_date DATE,
    number_of_borrowed_books int DEFAULT 0
  )

  CREATE TABLE IF NOT EXISTS borrowing(
    id bigint primary key,
    user_id bigint,
    book_id bigint,
    FOREIGN KEY (user_id) REFERENCES users(id),
    FOREIGN KEY (book_id) REFERENCES books(id)
  )

  ```
- Make sure your backend `application.properties` points to this database (see above).

---

## Environment variables
- You can override database credentials and ports in `docker-compose.yaml` or `application.properties` as needed.

---

## Project structure
```
book-managment-system/
  backend/      # Spring Boot backend
  frontend/     # React frontend
  docker-compose.yaml
  README.md
```

---

## Notes
- The backend will auto-create tables in the database on first run.
- Default database credentials are for local development and Docker Compose.
- For production, change passwords and review security settings.

---
