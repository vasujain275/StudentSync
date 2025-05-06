# StudentSync

<div align="center">
  <img src="https://img.shields.io/badge/status-active-success.svg" alt="Status" />
  <img src="https://img.shields.io/badge/version-0.6.5-blue.svg" alt="Version" />
  <img src="https://img.shields.io/badge/license-MIT-green.svg" alt="License" />
  <img src="https://img.shields.io/badge/java-23-orange.svg" alt="Java 23" />
  <img src="https://img.shields.io/badge/spring_boot-3.4.1-green.svg" alt="Spring Boot 3.4.1" />
</div>

<br />

<div align="center">
  <p>A comprehensive full-stack College ERP system to streamline academic management for both students and faculty</p>
</div>

<hr />

## 📋 Table of Contents

- [About](#about)
- [System Architecture](#system-architecture)
- [Backend (API)](#backend-api)
  - [Technology Stack](#technology-stack)
  - [Key Features](#key-features)
  - [API Documentation](#api-documentation)
  - [Security Implementation](#security-implementation)
  - [Logging & Monitoring](#logging--monitoring)
  - [Testing](#testing)
  - [File Storage](#file-storage)
  - [Build and Deployment](#build-and-deployment)
- [Frontend (Web)](#frontend-web)
- [Getting Started](#getting-started)
  - [Prerequisites](#prerequisites)
  - [Installation & Setup](#installation--setup)
  - [Running Locally](#running-locally)
- [API Endpoints](#api-endpoints)
- [Contributing](#contributing)
- [License](#license)

## 📚 About

**StudentSync** is a comprehensive College ERP (Enterprise Resource Planning) system designed to modernize academic management. It provides a unified platform for students to track attendance and grades, while empowering teachers to manage their academic responsibilities efficiently.

The system features role-based access control with three primary user types:
- **Students**: View attendance, check grades, access course materials
- **Faculty/Admins**: Manage courses, record attendance, assign grades
- **Super Admins**: Oversee system configuration, manage departments and schools

<div align="center">
  <img src="https://img.shields.io/badge/Backend-Spring_Boot-green" alt="Spring Boot" />
  <img src="https://img.shields.io/badge/Frontend-React-blue" alt="React" />
  <img src="https://img.shields.io/badge/Database-PostgreSQL-blue" alt="PostgreSQL" />
  <img src="https://img.shields.io/badge/Security-JWT-yellow" alt="JWT" />
</div>

## 🏗️ System Architecture

StudentSync follows a modern microservices architecture with:

- **Frontend**: React-based SPA with Tailwind CSS and ShadCN UI components
- **Backend**: RESTful API developed with Spring Boot
- **Database**: PostgreSQL for robust data persistence
- **Authentication**: JWT-based secure authentication with HTTP-only cookies
- **Documentation**: OpenAPI/Swagger for comprehensive API documentation
- **Testing**: JUnit for unit and integration testing
- **Monitoring**: Spring Boot Actuators for health and metrics monitoring
- **Cloud Storage**: AWS S3 integration for document storage

<div align="center">
  <img src="https://img.shields.io/badge/Architecture-Microservices-blue" alt="Microservices" />
  <img src="https://img.shields.io/badge/API-RESTful-green" alt="RESTful API" />
  <img src="https://img.shields.io/badge/Documentation-OpenAPI-orange" alt="OpenAPI" />
</div>

## 🔧 Backend (API)

### Technology Stack

<div align="center">
  <img src="https://img.shields.io/badge/Java-23-orange?logo=openjdk" alt="Java 23" />
  <img src="https://img.shields.io/badge/Spring_Boot-3.4.1-green?logo=springboot" alt="Spring Boot" />
  <img src="https://img.shields.io/badge/PostgreSQL-14-blue?logo=postgresql" alt="PostgreSQL" />
  <img src="https://img.shields.io/badge/Spring_Security-6-green?logo=springsecurity" alt="Spring Security" />
  <img src="https://img.shields.io/badge/JWT-0.12.6-yellow?logo=jsonwebtokens" alt="JWT" />
  <img src="https://img.shields.io/badge/Maven-3.9-red?logo=apachemaven" alt="Maven" />
  <img src="https://img.shields.io/badge/SLF4J-2.0.16-blue?logo=slf4j" alt="SLF4J" />
  <img src="https://img.shields.io/badge/AWS_S3-blue?logo=amazons3" alt="AWS S3" />
  <img src="https://img.shields.io/badge/Docker-blue?logo=docker" alt="Docker" />
  <img src="https://img.shields.io/badge/OpenAPI-3-green?logo=openapiinitiative" alt="OpenAPI" />
  <img src="https://img.shields.io/badge/JUnit-5-green?logo=junit5" alt="JUnit 5" />
</div>

### Key Features

- **Comprehensive Domain Model**: Fully implemented entities for academic management:
  - Users (Students, Admins, Super Admins)
  - Schools and Departments
  - Courses and Batches
  - Semesters and Enrollments
  - Attendance Records
  - Grading System
  - Notice Board

- **RESTful API**: Well-designed controllers following REST principles:
  - Consistent response structure with `ApiResponse` wrapper
  - Proper HTTP status codes
  - Validation using Bean Validation API

- **Database**: Robust data persistence with:
  - JPA/Hibernate ORM
  - PostgreSQL for production
  - H2 in-memory database for testing
  - Proper entity relationships and constraints

### API Documentation

<div align="center">
  <img src="https://img.shields.io/badge/OpenAPI-3.0-green?logo=openapiinitiative" alt="OpenAPI 3.0" />
  <img src="https://img.shields.io/badge/Swagger_UI-orange?logo=swagger" alt="Swagger UI" />
</div>

The API is fully documented using **OpenAPI 3.0** specification (formerly Swagger):

- **Interactive Documentation**: Available at `/swagger-ui.html` when running the application
- **OpenAPI Specification**: Available as YAML at `/api-docs`
- **Bruno Collection**: Included in the repository for API testing and exploration
- **API Response Standards**: Consistent response format across all endpoints

```yaml
# Example OpenAPI specification
openapi: 3.0.1
info:
  title: StudentSync API
  description: RESTful API for StudentSync College ERP System
  version: 0.6.5
paths:
  /api/auth/login:
    post:
      tags:
        - Authentication
      summary: Authenticate a user
      # ...rest of specification
```

### Security Implementation

<div align="center">
  <img src="https://img.shields.io/badge/Spring_Security-6-green?logo=springsecurity" alt="Spring Security" />
  <img src="https://img.shields.io/badge/JWT-0.12.6-yellow?logo=jsonwebtokens" alt="JWT" />
  <img src="https://img.shields.io/badge/Role_Based_Access-blue" alt="RBAC" />
</div>

StudentSync implements robust security measures:

- **Spring Security**: Full configuration with `SecurityConfig.java`
- **JWT Authentication**: Secure token-based authentication
  - HTTP-only secure cookies for token storage
  - Token refresh mechanism
  - Token validation and verification
- **Role-Based Access Control**: Different permissions for:
  - Students
  - Faculty/Admins
  - Super Admins
- **CORS Configuration**: Customizable CORS settings via properties
- **Password Security**: Secure password storage with BCrypt encoding

### Logging & Monitoring

<div align="center">
  <img src="https://img.shields.io/badge/SLF4J-2.0.16-blue?logo=slf4j" alt="SLF4J" />
  <img src="https://img.shields.io/badge/Structured_Logging-blue" alt="Structured Logging" />
  <img src="https://img.shields.io/badge/Spring_Actuator-green?logo=springboot" alt="Spring Boot Actuator" />
  <img src="https://img.shields.io/badge/AOP_Logging-orange" alt="AOP Logging" />
</div>

Comprehensive logging and monitoring features:

- **Structured Logging**: Using SLF4J for consistent log format
- **AOP-based Logging**: Method-level logging for performance metrics
- **Spring Boot Actuators**: For health checks and application metrics
  - Health endpoint: `/actuator/health`
  - Metrics endpoint: `/actuator/metrics`
  - Info endpoint: `/actuator/info`
- **Exception Handling**: Global exception handler with consistent error responses

### Testing

<div align="center">
  <img src="https://img.shields.io/badge/JUnit-5-green?logo=junit5" alt="JUnit 5" />
  <img src="https://img.shields.io/badge/Spring_Test-green?logo=springboot" alt="Spring Test" />
  <img src="https://img.shields.io/badge/H2_Database-blue?logo=h2" alt="H2 Database" />
</div>

Comprehensive testing strategy:

- **Unit Testing**: With JUnit 5
- **Integration Testing**: For controllers and repositories
- **Mock MVC Testing**: For API endpoint testing
- **In-Memory Database**: H2 database for testing
- **Security Testing**: With Spring Security Test utilities

### File Storage

<div align="center">
  <img src="https://img.shields.io/badge/AWS_S3-blue?logo=amazons3" alt="AWS S3" />
</div>

- **AWS S3 Integration**: For document and file storage
- **File Upload Service**: Dedicated service for managing file operations

### Build and Deployment

<div align="center">
  <img src="https://img.shields.io/badge/Maven-3.9-red?logo=apachemaven" alt="Maven" />
  <img src="https://img.shields.io/badge/Docker-blue?logo=docker" alt="Docker" />
  <img src="https://img.shields.io/badge/GitHub_Actions-blue?logo=githubactions" alt="GitHub Actions" />
</div>

- **Maven Build**: Standard Maven project structure
- **Docker Support**: Containerization with Docker
  - Includes `Dockerfile` and `docker-compose.yml`
- **CI/CD Pipeline**: GitHub Actions workflow for automated builds
- **Database Initialization**: Database setup script included

## 🖥️ Frontend (Web)

> **Note:** The frontend is currently a work in progress.

<div align="center">
  <img src="https://img.shields.io/badge/React-18-blue?logo=react" alt="React" />
  <img src="https://img.shields.io/badge/TypeScript-5-blue?logo=typescript" alt="TypeScript" />
  <img src="https://img.shields.io/badge/Vite-5-purple?logo=vite" alt="Vite" />
  <img src="https://img.shields.io/badge/Tailwind_CSS-3-blue?logo=tailwindcss" alt="Tailwind CSS" />
  <img src="https://img.shields.io/badge/ShadcnUI-latest-gray" alt="ShadcnUI" />
</div>

The frontend is being developed with:

- **React**: Modern component-based UI library
- **TypeScript**: For type safety and better developer experience
- **Vite**: For fast development and optimized builds
- **Tailwind CSS**: Utility-first CSS framework
- **ShadcnUI**: Beautifully designed components built with Radix UI and Tailwind CSS

Key frontend features (planned/in-progress):
- Responsive design for all device sizes
- Theme switching (light/dark mode)
- Role-based dashboards
- Interactive data visualizations
- Forms with validation
- Real-time notifications

## 🚀 Getting Started

### Prerequisites

- Java 23 or higher
- Maven 3.9+
- PostgreSQL 14+
- Node.js 18+ (for frontend)
- pnpm (for frontend package management)

### Installation & Setup

1. **Clone the repository**
   ```bash
   git clone https://github.com/yourusername/studentsync.git
   cd studentsync
   ```

2. **Set up the database**
   ```bash
   cd api
   ./init-db.sh
   ```

3. **Configure environment variables**
   ```bash
   cp .env.sample .env
   # Edit .env with your configuration
   ```

4. **Build the backend**
   ```bash
   ./mvnw clean install
   ```

5. **Set up the frontend**
   ```bash
   cd ../web
   pnpm install
   ```

### Running Locally

1. **Start the backend server**
   ```bash
   cd api
   ./mvnw spring-boot:run
   ```

2. **Start the frontend development server**
   ```bash
   cd web
   pnpm dev
   ```

3. **Access the application**
   - Backend: http://localhost:8080
   - Frontend: http://localhost:5173
   - API Documentation: http://localhost:8080/swagger-ui.html

## 📝 API Endpoints

Here are the main API endpoints available:

| Endpoint | Method | Description | Roles |
|----------|--------|-------------|-------|
| `/api/auth/login` | POST | Authenticate user | All |
| `/api/auth/refresh` | POST | Refresh JWT token | All |
| `/api/auth/logout` | POST | Logout user | All |
| `/api/users` | GET | Get all users | Admin, SuperAdmin |
| `/api/users/{id}` | GET | Get user by ID | Admin, SuperAdmin, Self |
| `/api/schools` | GET | Get all schools | All |
| `/api/departments` | GET | Get all departments | All |
| `/api/courses` | GET | Get all courses | All |
| `/api/notices` | GET | Get all notices | All |
| `/api/attendance` | GET | Get attendance records | Admin, Student (self) |
| `/api/grades` | GET | Get grade records | Admin, Student (self) |

For a complete list of endpoints, please refer to the Swagger documentation available at `/swagger-ui.html` when running the application.

## 👥 Contributing

Contributions are welcome! Please feel free to submit a Pull Request.

1. Fork the repository
2. Create your feature branch (`git checkout -b feature/amazing-feature`)
3. Commit your changes (`git commit -m 'Add some amazing feature'`)
4. Push to the branch (`git push origin feature/amazing-feature`)
5. Open a Pull Request

## 📄 License

This project is licensed under the MIT License - see the LICENSE file for details.

---

<div align="center">
  <p>Developed with ❤️ by <a href="https://github.com/vasujain275">Vasu Jain</a></p>
</div>
