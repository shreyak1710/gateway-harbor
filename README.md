
# Gateway Harbor Microservices

This project demonstrates a microservices architecture with API Gateway implementing various patterns.

## Services

1. **Gateway Service**: Routes requests to appropriate microservices with added features:
   - Path rewriting
   - Rate limiting
   - Circuit breaker pattern using Resilience4j
   - JWT authentication and authorization
   - CORS configuration

2. **Authentication Service**: Handles user authentication and JWT token generation

3. **Customer Service**: Provides CRUD operations for customer data

## Technologies

- Java 21
- Spring Boot 3.0.4
- Gradle 8.4
- MongoDB 6.0.x
- Spring Cloud 2022.x
- Spring Cloud API Gateway

## Prerequisites

- JDK 21
- Gradle 8.4
- MongoDB 6.0.x
- Redis (for rate limiting)

## Setup & Running

### Building the project

```bash
./gradlew clean build
```

### Running the services

Start each service in separate terminal windows:

```bash
# Start Gateway Service
java -jar gateway-service/build/libs/gateway-service-0.0.1-SNAPSHOT.jar

# Start Authentication Service
java -jar authentication-service/build/libs/authentication-service-0.0.1-SNAPSHOT.jar

# Start Customer Service
java -jar customer-service/build/libs/customer-service-0.0.1-SNAPSHOT.jar
```

## API Endpoints

### Authentication Service (localhost:8080/auth/*)

- POST /api/auth/register - Register a new user
- POST /api/auth/login - Authenticate a user and get JWT token

### Customer Service (localhost:8080/api/customers/*)

- GET /api/customers - Get all customers (requires USER role)
- GET /api/customers/{id} - Get a customer by ID (requires USER role)
- POST /api/customers - Create a new customer (requires ADMIN role)
- PUT /api/customers/{id} - Update a customer (requires ADMIN role)
- DELETE /api/customers/{id} - Delete a customer (requires ADMIN role)

## Gateway Features

1. **Path Rewriting**: Routes are configured to rewrite paths before forwarding to microservices
2. **Rate Limiting**: Prevents abuse by limiting requests per client
3. **Circuit Breaker**: Prevents cascading failures using Resilience4j
4. **JWT Authentication**: Secures endpoints with JWT tokens
5. **CORS**: Configured to allow cross-origin requests

## Testing with Postman

1. Register a user:
   ```
   POST localhost:8080/auth/api/auth/register
   {
     "username": "admin",
     "email": "admin@example.com",
     "password": "password"
   }
   ```

2. Login to get a token:
   ```
   POST localhost:8080/auth/api/auth/login
   {
     "username": "admin",
     "password": "password"
   }
   ```

3. Use the token in other API calls:
   ```
   GET localhost:8080/api/customers
   Header: Authorization: Bearer {your-token}
   ```

## Architecture Diagram

```
┌─────────────┐      ┌─────────────────┐
│             │      │                 │
│   Client    │─────▶│  API Gateway    │
│             │      │                 │
└─────────────┘      └────────┬────────┘
                              │
                              │
           ┌─────────────────┴──────────────────┐
           │                                    │
           ▼                                    ▼
┌─────────────────────┐             ┌─────────────────────┐
│                     │             │                     │
│  Authentication     │             │  Customer           │
│  Service            │             │  Service            │
│                     │             │                     │
└─────────────────────┘             └─────────────────────┘
           │                                    │
           ▼                                    ▼
┌─────────────────────┐             ┌─────────────────────┐
│                     │             │                     │
│  MongoDB            │             │  MongoDB            │
│  (Auth DB)          │             │  (Customer DB)      │
│                     │             │                     │
└─────────────────────┘             └─────────────────────┘
```
