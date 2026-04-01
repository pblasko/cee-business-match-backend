# CEE Business Match Backend

A backend system for connecting exporters, investors, and legal firms in cross-border business opportunities.

This application models a real-world business workflow where companies can create opportunities, apply for collaboration, and establish structured partnerships.

---

## 🚀 Live Demo

Backend URL:
https://cee-business-match-backend-production.up.railway.app/

Swagger UI:
https://cee-business-match-backend-production.up.railway.app/swagger-ui.html

## 🚀 Features

### 🔐 Authentication & Security
- JWT-based authentication
- Stateless session management
- Role-based authorization
- Custom exception handling (401 / 403)

Supported roles:
- EXPORTER
- INVESTOR
- LAW_FIRM

---

### 📦 Offer Management
- Exporters can create business offers
- Offers include:
    - title
    - description
    - category
    - target role
    - country
    - legal support requirement
- Filtering and pagination supported

---

### 🤝 Contact Requests
- Investors and law firms can apply to offers
- Each request contains:
    - sender
    - role
    - message
    - status

Exporter can:
- ACCEPT
- REJECT requests

---

### 🧠 Matching Logic (Core Business Logic)

The system enforces real-world constraints:

- Only one accepted partner per role (e.g. 1 investor, 1 law firm)
- Automatic rejection of competing requests
- Offer status recalculated dynamically

Offer states:
- OPEN → no partners yet
- PARTIALLY_MATCHED → one partner selected
- MATCHED → all required partners selected

---

## 🧠 Business Workflow

1. Exporter creates an offer
2. Investor and/or law firm sends contact request
3. Exporter reviews incoming requests
4. Exporter accepts or rejects requests
5. System automatically:
    - enforces role constraints
    - updates offer status

---

## 🛠️ Tech Stack

Backend:
- Java 17
- Spring Boot
- Spring Security (JWT)
- Spring Data JPA
- Hibernate

Database:
- MySQL (Docker)
- H2 (for testing)

Tools:
- Maven
- Swagger (OpenAPI)
- Docker & Docker Compose

Testing:
- JUnit 5
- Mockito
- MockMvc

---

## 📂 Project Structure


src/main/java/com/cee/business_match_backend
├── auth
├── offer
├── contactrequest
├── config
├── common (exceptions, utilities)


---

## 📦 Running Locally (Docker)

### 1. Clone repository

```bash
git clone https://github.com/pblasko/cee-business-match-backend.git
cd cee-business-match-backend
2. Start application
docker compose up --build
3. Access services
Backend: http://localhost:8001
Swagger UI: http://localhost:8001/swagger-ui.html
🧪 Running Tests
./mvnw test

Test types:

Integration tests (controllers, authentication)
Unit tests (business logic)
In-memory database (H2)
🔐 Authentication

Use JWT token in requests:

Authorization: Bearer <token>

Obtain token via:

POST /api/auth/login
📚 API Documentation

Swagger UI:

http://localhost:8001/swagger-ui.html
🐳 Docker Setup

Multi-container architecture:

backend (Spring Boot app)
mysql (database)

Key features:

Docker networking
Environment-based configuration
Persistent data via volumes
⚠️ Notes
Swagger enabled by default (disable in production)
JWT secret is provided via environment variables
Database schema managed by Hibernate (DDL auto)
🔮 Future Improvements
Refresh token support
Email notifications
Advanced matching algorithm
Rate limiting
Cloud deployment (AWS / VPS)
Frontend integration (React / Next.js)
👤 Author

Peter Blasko

🎯 Project Goal

This project demonstrates:

real-world backend architecture
business-driven design
role-based workflows
complex state management
containerized deployment

Designed as a portfolio-ready backend application with a strong focus on clean architecture and real business logic.