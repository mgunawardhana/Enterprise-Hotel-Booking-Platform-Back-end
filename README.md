# 🏗️ Enterprise Spring Boot Backend
### Production-Ready Hexagonal Architecture | Hotel Booking Platform

<div align="center">

![Java](https://img.shields.io/badge/Java-17-orange?style=for-the-badge&logo=java)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.2.1-brightgreen?style=for-the-badge&logo=spring)
![PostgreSQL](https://img.shields.io/badge/PostgreSQL-15-blue?style=for-the-badge&logo=postgresql)
![Architecture](https://img.shields.io/badge/Architecture-Hexagonal-purple?style=for-the-badge)
![License](https://img.shields.io/badge/License-MIT-yellow?style=for-the-badge)

**A production-grade Spring Boot backend implementing true Hexagonal Architecture (Ports & Adapters)**  
with **JWT/OAuth2 authentication**, **room management**, **image storage**, and **enterprise-level design patterns**.

[Features](#-key-features) • [Architecture](#-hexagonal-architecture-overview) • [Getting Started](#-getting-started) • [API Documentation](#-api-documentation) • [Testing](#-testing)

</div>

---

## 📋 Table of Contents

- [Overview](#-overview)
- [Key Features](#-key-features)
- [Hexagonal Architecture Overview](#-hexagonal-architecture-overview)
- [Project Structure](#-project-structure)
- [Technology Stack](#-technology-stack)
- [Getting Started](#-getting-started)
- [API Documentation](#-api-documentation)
- [Database Schema](#-database-schema)
- [Testing](#-testing)
- [Security](#-security)
- [Contributing](#-contributing)

---

## 🎯 Overview

This project is a **production-ready Spring Boot backend** for a hotel booking platform, built using **Hexagonal Architecture (Ports & Adapters pattern)**. The architecture ensures **clean separation of concerns**, **domain-centric design**, and **framework independence**, making the system highly **maintainable**, **testable**, and **scalable**.

### What is Hexagonal Architecture?

Hexagonal Architecture (also known as **Ports & Adapters**) isolates the **core business logic** from external concerns:

- 🌐 **Web frameworks** (REST controllers)
- 🗄️ **Databases** (JPA, PostgreSQL)
- 🔐 **Security** (JWT, OAuth2)
- 📦 **External services**

The system communicates with the outside world **only through well-defined ports**, while adapters implement those ports.

> **Golden Rule:**  
> The domain layer must **never** depend on frameworks, databases, or infrastructure.

---

## ✨ Key Features

### 🏨 Room Management System
- ✅ Full CRUD operations for hotel rooms
- ✅ Image storage in PostgreSQL (BYTEA)
- ✅ Fast paginated listings with sorting
- ✅ Soft delete support
- ✅ Advanced filtering and search

### 🔐 Authentication & Authorization
- ✅ JWT-based authentication
- ✅ Google OAuth2 login
- ✅ Role-based access control (RBAC)
- ✅ Refresh token mechanism
- ✅ Stateless security

### 🏗️ Enterprise Architecture
- ✅ Hexagonal Architecture (Ports & Adapters)
- ✅ Domain-Driven Design (DDD) principles
- ✅ CQRS-ready structure
- ✅ Event-driven capabilities
- ✅ Microservices-ready

### 🚀 Performance & Scalability
- ✅ Database indexing for fast queries
- ✅ Lazy loading optimization
- ✅ Batch fetching
- ✅ Connection pooling (HikariCP)
- ✅ Pagination support

### 📊 Observability & Monitoring
- ✅ Comprehensive logging (SLF4J)
- ✅ Request tracing with trace IDs
- ✅ Audit logging with AOP
- ✅ OpenAPI/Swagger documentation
- ✅ Health checks

---

## 🏛️ Hexagonal Architecture Overview

```
┌─────────────────────────────────────────────────────────────────┐
│                    DRIVING ADAPTERS (Input)                      │
│  ┌──────────────┐  ┌──────────────┐  ┌──────────────┐          │
│  │     REST     │  │    OAuth2    │  │   Scheduled  │          │
│  │ Controllers  │  │   Handlers   │  │    Tasks     │          │
│  └──────┬───────┘  └──────┬───────┘  └──────┬───────┘          │
└─────────┼──────────────────┼──────────────────┼─────────────────┘
          │                  │                  │
          ▼                  ▼                  ▼
┌─────────────────────────────────────────────────────────────────┐
│                     APPLICATION LAYER                            │
│  ┌────────────────────────────────────────────────────────┐     │
│  │  Services (Use Case Implementations)                   │     │
│  │  • AuthenticationService                               │     │
│  │  • UserManagementService                               │     │
│  │  • RoomManagementService                               │     │
│  └────────────────────────────────────────────────────────┘     │
└─────────────────────────┬───────────────────────────────────────┘
                          │ implements
                          ▼
┌─────────────────────────────────────────────────────────────────┐
│                       DOMAIN LAYER (CORE)                        │
│  ┌──────────────────────────────────────────────────────────┐   │
│  │  Domain Models (Pure Java - NO Framework Dependencies)  │   │
│  │  • User, Room, RoomImage, Role, AuditLog                │   │
│  └──────────────────────────────────────────────────────────┘   │
│  ┌──────────────────────────────────────────────────────────┐   │
│  │  Value Objects                                           │   │
│  │  • Email, BedType, RoomStatus                            │   │
│  └──────────────────────────────────────────────────────────┘   │
│  ┌──────────────────────────────────────────────────────────┐   │
│  │  Ports (Interfaces)                                      │   │
│  │  IN:  AuthenticationUseCase, RoomManagementUseCase       │   │
│  │  OUT: UserRepositoryPort, RoomRepositoryPort             │   │
│  └──────────────────────────────────────────────────────────┘   │
└─────────────────────────┬───────────────────────────────────────┘
                          │ implemented by
                          ▼
┌─────────────────────────────────────────────────────────────────┐
│                    INFRASTRUCTURE LAYER                          │
│  ┌──────────────┐  ┌──────────────┐  ┌──────────────┐          │
│  │  PostgreSQL  │  │     JWT      │  │    Image     │          │
│  │   Adapters   │  │   Security   │  │   Storage    │          │
│  └──────────────┘  └──────────────┘  └──────────────┘          │
└─────────────────────────────────────────────────────────────────┘
                    DRIVEN ADAPTERS (Output)
```

### Dependency Flow

```
Adapter Layer  ──depends on──▶  Application Layer
                                      │
                                      ▼
Application Layer  ──depends on──▶  Domain Layer
                                      ▲
                                      │
Infrastructure Layer  ──implements──┘
```

**Key Principle:** Dependencies point **inward** toward the domain. The domain never depends on outer layers.

---

## 📁 Project Structure

```
com.example.app
│
├── 📂 domain                          # ⭐ CORE - Pure Business Logic
│   ├── model                          # Domain entities (User, Room, RoomImage)
│   ├── valueobject                    # Value objects (Email, BedType, RoomStatus)
│   ├── event                          # Domain events (UserCreatedEvent)
│   └── port
│       ├── in                         # Inbound ports (Use cases)
│       │   ├── AuthenticationUseCase
│       │   ├── UserManagementUseCase
│       │   └── RoomManagementUseCase
│       └── out                        # Outbound ports (Repository interfaces)
│           ├── UserRepositoryPort
│           ├── RoomRepositoryPort
│           ├── RoomImageRepositoryPort
│           ├── TokenRepositoryPort
│           └── AuditLogRepositoryPort
│
├── 📂 application                     # Use Case Implementations
│   ├── service                        # Application services
│   │   ├── AuthenticationService
│   │   ├── UserManagementService
│   │   └── RoomManagementService
│   └── mapper                         # MapStruct mappers
│       ├── UserMapper
│       ├── RoomMapper
│       └── RoomImageMapper
│
├── 📂 adapter                         # 🔌 DRIVING ADAPTERS
│   ├── web
│   │   ├── controller                 # REST endpoints
│   │   │   ├── AuthenticationController
│   │   │   ├── UserController
│   │   │   ├── RoomController
│   │   │   └── RoomImageController
│   │   ├── request                    # Request DTOs
│   │   │   ├── LoginRequest
│   │   │   ├── RegisterRequest
│   │   │   ├── CreateRoomRequest
│   │   │   └── UpdateRoomRequest
│   │   └── response                   # Response DTOs
│   │       ├── AuthenticationResponse
│   │       ├── UserResponse
│   │       ├── RoomResponse
│   │       ├── RoomDetailResponse
│   │       └── RoomImageResponse
│   └── exception
│       └── GlobalExceptionHandler     # Centralized error handling
│
├── 📂 infrastructure                  # 🔧 DRIVEN ADAPTERS
│   ├── persistence
│   │   ├── entity                     # JPA entities
│   │   │   ├── BaseEntity
│   │   │   ├── UserEntity
│   │   │   ├── RoomEntity
│   │   │   ├── RoomImageEntity
│   │   │   ├── RoleEntity
│   │   │   └── RefreshTokenEntity
│   │   ├── repository                 # Spring Data JPA repositories
│   │   │   ├── UserJpaRepository
│   │   │   ├── RoomJpaRepository
│   │   │   ├── RoomImageJpaRepository
│   │   │   └── RefreshTokenJpaRepository
│   │   └── adapter                    # Repository adapters
│   │       ├── UserRepositoryAdapter
│   │       ├── RoomRepositoryAdapter
│   │       └── RoomImageRepositoryAdapter
│   ├── security
│   │   ├── config                     # Security configuration
│   │   ├── jwt                        # JWT utilities
│   │   └── oauth                      # OAuth2 handlers
│   ├── storage
│   │   └── ImageStorageService        # Image processing
│   ├── audit
│   │   ├── AuditAspect                # AOP auditing
│   │   └── AuditService
│   └── config
│       ├── OpenApiConfig
│       ├── AsyncConfig
│       └── DataInitializer
│
├── 📂 common                          # Shared Utilities
│   ├── constants                      # Application constants
│   ├── exception                      # Custom exceptions
│   │   ├── ResourceNotFoundException
│   │   ├── RoomNotFoundException
│   │   ├── ImageProcessingException
│   │   └── DuplicateResourceException
│   ├── response                       # Standard response wrappers
│   │   ├── CommonResponse
│   │   └── PageResponse
│   └── util
│       └── TraceIdGenerator
│
└── 📄 Application.java                # Spring Boot entry point
```

---

## 🛠️ Technology Stack

### Core Technologies
| Technology | Version | Purpose |
|-----------|---------|---------|
| **Java** | 17 | Programming language |
| **Spring Boot** | 3.2.1 | Application framework |
| **Spring Data JPA** | 3.2.1 | Data persistence |
| **PostgreSQL** | 15+ | Relational database |
| **Maven** | 3.9+ | Build tool |

### Security & Authentication
| Technology | Purpose |
|-----------|---------|
| **Spring Security** | Security framework |
| **JWT (JJWT)** | Token-based authentication |
| **OAuth2** | Google social login |

### Development & Quality
| Technology | Purpose |
|-----------|---------|
| **Lombok** | Boilerplate reduction |
| **MapStruct** | Object mapping |
| **SLF4J + Logback** | Logging |
| **Hibernate Validator** | Input validation |
| **SpringDoc OpenAPI** | API documentation |

### Database & Performance
| Technology | Purpose |
|-----------|---------|
| **HikariCP** | Connection pooling |
| **Hibernate** | ORM framework |
| **Flyway** (optional) | Database migrations |

---

## 🚀 Getting Started

### Prerequisites

- **Java 17+** installed
- **PostgreSQL 15+** running
- **Maven 3.9+** installed
- **Git** for version control

### 1. Clone the Repository

```bash
git clone https://github.com/yourusername/enterprise-spring-backend.git
cd enterprise-spring-backend
```

### 2. Configure PostgreSQL

Create a PostgreSQL database:

```sql
CREATE DATABASE enterprise_app;
CREATE USER app_user WITH PASSWORD 'app_password';
GRANT ALL PRIVILEGES ON DATABASE enterprise_app TO app_user;
```

### 3. Configure Application Properties

Update `src/main/resources/application-dev.yml`:

```yaml
spring:
  datasource:
    url: jdbc:postgresql://localhost:5432/enterprise_app
    username: app_user
    password: app_password

jwt:
  secret: YOUR_JWT_SECRET_KEY_HERE
  access-token-expiration: 900000      # 15 minutes
  refresh-token-expiration: 604800000  # 7 days
```

### 4. Build the Project

```bash
mvn clean install
```

### 5. Run the Application

```bash
mvn spring-boot:run
```

The application will start on `http://localhost:8080`

### 6. Access Swagger UI

Open your browser and navigate to:

```
http://localhost:8080/swagger-ui.html
```

---

## 📚 API Documentation

### Authentication Endpoints

| Method | Endpoint | Description | Auth Required |
|--------|----------|-------------|---------------|
| `POST` | `/api/v1/auth/register` | Register new user | ❌ |
| `POST` | `/api/v1/auth/login` | Login with credentials | ❌ |
| `POST` | `/api/v1/auth/refresh` | Refresh access token | ❌ |
| `POST` | `/api/v1/auth/google` | Google OAuth2 login | ❌ |

### User Management Endpoints

| Method | Endpoint | Description | Auth Required |
|--------|----------|-------------|---------------|
| `GET` | `/api/v1/users/me` | Get current user profile | ✅ |
| `PUT` | `/api/v1/users/me` | Update current user | ✅ |
| `GET` | `/api/v1/users` | List all users (Admin) | ✅ Admin |

### Room Management Endpoints

#### Public Endpoints (No Authentication)

| Method | Endpoint | Description |
|--------|----------|-------------|
| `GET` | `/api/v1/rooms` | List rooms (paginated) |
| `GET` | `/api/v1/rooms/{id}` | Get room details |
| `GET` | `/api/v1/rooms/{roomId}/images` | Get room images |
| `GET` | `/api/v1/rooms/{roomId}/images/{imageId}` | Get image binary |

#### Admin Endpoints (Authentication Required)

| Method | Endpoint | Description |
|--------|----------|-------------|
| `POST` | `/api/v1/rooms` | Create new room |
| `PUT` | `/api/v1/rooms/{id}` | Update room |
| `DELETE` | `/api/v1/rooms/{id}` | Delete room (soft) |
| `POST` | `/api/v1/rooms/{roomId}/images` | Upload room images |

### Example: Create Room

```bash
curl -X POST http://localhost:8080/api/v1/rooms \
  -H "Content-Type: application/json" \
  -d '{
    "title": "Deluxe Ocean View Suite",
    "description": "Luxurious suite with panoramic ocean views",
    "pricePerNight": 299.99,
    "rating": 4.8,
    "maxGuests": 4,
    "bedType": "KING",
    "roomSize": 45.5,
    "tags": ["Ocean View", "Butler Service"],
    "amenities": ["WiFi", "AC", "Mini Bar", "Jacuzzi"],
    "badges": ["Best Seller", "Most Luxurious"],
    "status": "AVAILABLE"
  }'
```

### Example: List Rooms with Pagination

```bash
curl "http://localhost:8080/api/v1/rooms?page=0&size=10&sortBy=price&sortDirection=DESC"
```

**Response:**

```json
{
  "success": true,
  "message": "Rooms fetched successfully",
  "statusCode": 200,
  "data": {
    "content": [
      {
        "id": "uuid-here",
        "title": "Deluxe Ocean View Suite",
        "pricePerNight": 299.99,
        "rating": 4.8,
        "status": "AVAILABLE"
      }
    ],
    "page": 0,
    "size": 10,
    "totalElements": 50,
    "totalPages": 5,
    "first": true,
    "last": false
  },
  "timestamp": "2026-01-27T20:15:00",
  "traceId": "abc-123-def"
}
```

---

## 🗄️ Database Schema

### Core Tables

#### `users`
- User authentication and profile information
- Relationships: Many-to-Many with `roles`

#### `rooms`
- Hotel room information
- Indexed on: `price_per_night`, `rating`, `status`, `is_deleted`
- Soft delete support

#### `room_images`
- Image storage using PostgreSQL BYTEA
- Lazy loading for performance
- Indexed on: `room_id`, `is_main`

#### `roles`
- User roles (ADMIN, USER)

#### `refresh_tokens`
- JWT refresh token storage

#### `audit_logs`
- System audit trail

### Entity Relationships

```
users ──< user_roles >── roles
  │
  └──< rooms ──< room_images
           │
           ├──< room_tags
           ├──< room_amenities
           └──< room_badges
```

---

## 🧪 Testing

### Run All Tests

```bash
mvn test
```

### Run Integration Tests

```bash
mvn verify
```

### Test Coverage

```bash
mvn jacoco:report
```

View coverage report at: `target/site/jacoco/index.html`

---

## 🔐 Security

### JWT Authentication Flow

1. User logs in with credentials
2. Server validates and returns JWT access token + refresh token
3. Client includes access token in `Authorization: Bearer <token>` header
4. Access token expires after 15 minutes
5. Client uses refresh token to get new access token

### OAuth2 Google Login

1. User clicks "Login with Google"
2. Redirected to Google OAuth consent screen
3. Google returns authorization code
4. Backend exchanges code for user info
5. User created/updated in database
6. JWT tokens returned to client

### Role-Based Access Control

```java
@PreAuthorize("hasRole('ADMIN')")
public ResponseEntity<?> adminOnlyEndpoint() { ... }
```

---

## 🎯 Best Practices Implemented

### ✅ SOLID Principles
- **Single Responsibility**: Each class has one reason to change
- **Open/Closed**: Open for extension, closed for modification
- **Liskov Substitution**: Interfaces properly abstracted
- **Interface Segregation**: Focused, minimal interfaces
- **Dependency Inversion**: Depend on abstractions, not concretions

### ✅ Clean Code
- Meaningful names
- Small, focused methods
- No code duplication
- Comprehensive logging
- Proper error handling

### ✅ Performance Optimization
- Database indexing
- Lazy loading
- Connection pooling
- Batch processing
- Pagination

### ✅ Security Best Practices
- Password hashing (BCrypt)
- JWT token expiration
- CORS configuration
- SQL injection prevention (JPA)
- Input validation

---

## 📊 Monitoring & Observability

### Health Check

```bash
curl http://localhost:8080/actuator/health
```

### Application Metrics

```bash
curl http://localhost:8080/actuator/metrics
```

### Logging

All requests include trace IDs for distributed tracing:

```
[abc-123-def] GET /api/v1/rooms - page: 0, size: 10
[abc-123-def] Successfully fetched 10 rooms
```

---

## 🐳 Docker Support

### Build Docker Image

```bash
docker build -t enterprise-spring-backend .
```

### Run with Docker Compose

```bash
docker-compose up -d
```

This starts:
- PostgreSQL database
- Spring Boot application
- pgAdmin (optional)

---

## 🤝 Contributing

Contributions are welcome! Please follow these steps:

1. Fork the repository
2. Create a feature branch (`git checkout -b feature/amazing-feature`)
3. Commit your changes (`git commit -m 'Add amazing feature'`)
4. Push to the branch (`git push origin feature/amazing-feature`)
5. Open a Pull Request

### Code Style

- Follow Java naming conventions
- Use Lombok annotations
- Write comprehensive JavaDoc
- Include unit tests
- Follow hexagonal architecture principles

---

## 📝 License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

---

## 👨‍💻 Author

**Mr. Gunawardhana**  
BSc (Hons) Software Engineering

- GitHub: [@mgunawardhana](https://github.com/mgunawardhana)
- LinkedIn: [mGunawardhana](https://www.linkedin.com/in/maneesha-gunawardhana/)
- Email: maneesha.gunawardhana.contact@gmail.com

---

## 🙏 Acknowledgments

- Spring Boot team for the excellent framework
- Hexagonal Architecture pioneers (Alistair Cockburn)
- Domain-Driven Design community
- Open source contributors

---

## 📖 Further Reading

- [Hexagonal Architecture](https://alistair.cockburn.us/hexagonal-architecture/)
- [Domain-Driven Design](https://martinfowler.com/bliki/DomainDrivenDesign.html)
- [Spring Boot Documentation](https://spring.io/projects/spring-boot)
- [Clean Architecture](https://blog.cleancoder.com/uncle-bob/2012/08/13/the-clean-architecture.html)

---

<div align="center">

**⭐ If you find this project useful, please consider giving it a star! ⭐**

Made with ❤️ using Hexagonal Architecture

</div>
