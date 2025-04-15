
# Gateway Harbor Microservices - Development Guide

## Prerequisites

- Java 21 JDK
- IntelliJ IDEA (Community/Ultimate)
- MongoDB 6.0.x
- Redis
- Gradle 8.4

## Development Setup

### Database Preparation

1. Start MongoDB:
   ```bash
   # macOS/Linux (Homebrew)
   brew services start mongodb-community@6.0

   # Windows
   net start mongodb
   ```

2. Create Databases:
   ```bash
   mongosh
   use auth-db
   use customer-db
   ```

### Running Services in IntelliJ

1. Open project in IntelliJ IDEA
2. Ensure Gradle sync is complete
3. Run services in order:
   - Authentication Service (Port 8081)
   - Customer Service (Port 8082)
   - Gateway Service (Port 8080)

## API Testing with Postman

### Registration
- Endpoint: `POST http://localhost:8080/auth/api/auth/register`
- Payload:
  ```json
  {
    "username": "testuser",
    "email": "test@example.com",
    "password": "securePassword123"
  }
  ```

### Login
- Endpoint: `POST http://localhost:8080/auth/api/auth/login`
- Payload:
  ```json
  {
    "username": "testuser",
    "password": "securePassword123"
  }
  ```

## Troubleshooting

- Verify all services are running
- Check MongoDB and Redis connectivity
- Ensure no port conflicts
- Review application logs for errors

## Recommended IntelliJ Setup

1. Install Lombok plugin
2. Enable annotation processing
3. Configure Gradle JVM settings
