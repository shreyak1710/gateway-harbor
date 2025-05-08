
# Gateway Service - AI Customer Support Agents

## 1. Overview

The Gateway Service is the entry point for all client requests in the AI Customer Support Agents platform. It handles routing, security, rate limiting, and circuit breaking functionality.

## 2. Key Features

- API Key Based Authentication
- Request Rate Limiting
- Circuit Breaking for Service Failures
- Request/Response Filtering
- Centralized Error Handling
- Environment-specific Configurations

## 3. API Key Authentication Flow

1. Customer registers via Authentication Service
2. System sends verification email
3. Customer verifies email
4. API key is generated
5. All subsequent requests must include API key in header
6. Gateway validates API key before routing

## 4. Setup Instructions

### Prerequisites
- Java 21 JDK
- Redis for Rate Limiting
- Gradle 8.4

### Running the Service
```bash
./gradlew :gateway-service:bootRun
```

### Configuration Profiles
- Development: `--spring.profiles.active=dev`
- Production: `--spring.profiles.active=prod`

## 5. Endpoints

### Non-Protected Routes
- `POST /auth/api/auth/register` - Register new customer
- `POST /auth/api/auth/verify-email` - Verify email address
- `POST /auth/api/auth/login` - Login existing customer
- `POST /auth/api/auth/generate-api-key` - Generate API key after verification
- `GET /fallback/*` - Fallback endpoints for service failures

### Protected Routes (Require API Key)
- All routes to `/api/customers/*`

## 6. API Key Header Format

```
X-API-Key: {generated-api-key}
```

## 7. Docker Support

```bash
# Build Image
docker build -t gateway-service .

# Run Container
docker run -p 8080:8080 -e REDIS_HOST=redis gateway-service
```
