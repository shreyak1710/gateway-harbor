
# Gateway Service

## 1. Project Overview

**Project Name:** Gateway Harbor - Gateway Service  
**Description:** Spring Cloud Gateway service that routes and manages requests to microservices in the Gateway Harbor architecture. Provides rate limiting, circuit breaking, security, and fallback capabilities.

## 2. Prerequisites

- **Java Version:** Java 21
- **Spring Boot Version:** 3.0.4
- **Database:** Redis (for rate limiting)
- **Build Tool:** Gradle 8.4
- **Other Tools:** 
  - Spring Cloud Gateway
  - Spring Security
  - Resilience4j (Circuit Breaker)
  - JWT Authentication

## 3. Project Setup

### Clone the Repository:
```bash
git clone <repository-url>
cd gateway-harbor
```

### Build the Project:
```bash
./gradlew :gateway-service:clean build
```

### Run the Application:
```bash
./gradlew :gateway-service:bootRun
```

## 4. Configuration

### Environment Configurations:
The application is configured via `application.yml` file. Key configurations include:

- Server port: 8080
- Application name: gateway-service
- Route configurations for downstream services
- Rate limiting settings
- Circuit breaker configurations

### Redis Connection Settings:
```yaml
spring:
  redis:
    host: localhost
    port: 6379
```

## 5. Directory Structure

```
gateway-service/
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── com/
│   │   │       └── gatewayharbor/
│   │   │           └── gateway/
│   │   │               ├── config/           # Configuration classes
│   │   │               │   ├── SecurityConfig.java
│   │   │               │   └── RateLimiterConfig.java
│   │   │               ├── controller/       # Controller classes
│   │   │               │   └── FallbackController.java
│   │   │               └── GatewayServiceApplication.java
│   │   └── resources/
│   │       └── application.yml               # Application configuration
│   └── test/                                 # Test classes
└── build.gradle                              # Gradle build file
```

## 6. API Documentation

Swagger documentation is not directly applicable to API Gateway services as they primarily route to other services. However, you can document the available routes and their purposes.

## 7. API Endpoints

The Gateway Service routes to the following services:

### Authentication Service Routes
- `POST /auth/api/auth/register` - Register new users
- `POST /auth/api/auth/login` - Authenticate users

### Customer Service Routes
- `GET /api/customers` - List all customers (Role: USER)
- `GET /api/customers/{id}` - Get customer by ID (Role: ADMIN)
- `POST /api/customers` - Create new customer (Role: ADMIN)
- `PUT /api/customers/{id}` - Update customer (Role: ADMIN)
- `DELETE /api/customers/{id}` - Delete customer (Role: ADMIN)

### Fallback Routes
- `GET /fallback/auth` - Fallback for authentication service
- `GET /fallback/customers` - Fallback for customer service

## 8. Error Handling

The Gateway Service implements centralized error handling through:

1. **Circuit Breakers**: When downstream services fail, the circuit breaker trips and redirects to fallback endpoints.
2. **Fallback Controllers**: Provides meaningful error responses when services are unavailable.

### Common Error Responses:

- **503 Service Unavailable**: When a service is down or not responding
- **429 Too Many Requests**: When rate limits are exceeded
- **401 Unauthorized**: When JWT authentication fails
- **403 Forbidden**: When authorization fails (incorrect roles)

## 9. Testing

### Run Tests:
```bash
./gradlew :gateway-service:test
```

### Test Categories:
- Unit Tests: Test individual components
- Integration Tests: Test routing and filter behavior

## 10. Deployment Instructions

### Local Deployment
1. Ensure Redis is running locally
2. Run with development profile:
   ```
   ./gradlew :gateway-service:bootRun --args='--spring.profiles.active=dev'
   ```

### QA Environment
1. Build the application:
   ```
   ./gradlew :gateway-service:build
   ```
2. Deploy the JAR file to the QA server
3. Run with QA profile:
   ```
   java -jar gateway-service.jar --spring.profiles.active=qa
   ```

### Production Environment
1. Build the application:
   ```
   ./gradlew :gateway-service:build
   ```
2. Deploy the JAR file to production servers
3. Run with production profile and appropriate memory settings:
   ```
   java -Xms1G -Xmx2G -jar gateway-service.jar --spring.profiles.active=prod
   ```

### Docker Setup

1. Create a Dockerfile in the gateway-service directory:
   ```dockerfile
   FROM openjdk:21-slim
   VOLUME /tmp
   ARG JAR_FILE=build/libs/*.jar
   COPY ${JAR_FILE} app.jar
   ENTRYPOINT ["java", "-Djava.security.egd=file:/dev/./urandom", "-jar", "/app.jar"]
   ```

2. Build the Docker image:
   ```bash
   docker build -t gateway-harbor/gateway-service .
   ```

3. Run the container:
   ```bash
   docker run -d -p 8080:8080 --name gateway-service \
     --network harbor-network \
     -e "SPRING_REDIS_HOST=redis" \
     gateway-harbor/gateway-service
   ```

4. Docker Compose (optional):
   Create a docker-compose.yml file for orchestrating all services:
   ```yaml
   version: '3'
   
   services:
     redis:
       image: redis:latest
       ports:
         - "6379:6379"
       networks:
         - harbor-network
       
     gateway-service:
       build: ./gateway-service
       ports:
         - "8080:8080"
       depends_on:
         - redis
       environment:
         - SPRING_REDIS_HOST=redis
       networks:
         - harbor-network
   
   networks:
     harbor-network:
       driver: bridge
   ```

   Run with:
   ```bash
   docker-compose up -d
   ```
