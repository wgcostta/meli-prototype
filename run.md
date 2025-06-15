# Mercado Clone - Execution Guide

This guide explains how to run the complete application using Docker or individually.

## 📋 Prerequisites

- Docker and Docker Compose installed
- Node.js 18+ (for individual frontend execution)
- Java 17+ and Maven (for individual backend execution)

## 🚀 Complete Execution with Docker Compose

### Run the entire application
```bash
# Start all services
docker-compose up

# View real-time logs
docker-compose logs -f

# View logs for a specific service
docker-compose logs -f backend
docker-compose logs -f frontend
```

### Rebuild after changes
```bash
# Stop and rebuild
docker-compose down
docker-compose up --build -d
```

### Stop application
```bash
# Stop services
docker-compose down

# Stop and remove volumes
docker-compose down -v
```

## 🔧 Individual Execution

### Backend (Spring Boot)
```bash
cd mercadoclone-backend

# With Maven
mvn spring-boot:run

# Or with Maven Wrapper
./mvnw spring-boot:run

# Or via individual Docker
docker build -t backend .
docker run -p 3001:3001 backend
```

### Frontend (Next.js)
```bash
cd mercadoclone-frontend

# Install dependencies
npm install

# Run in development mode
npm run dev

# Or via individual Docker
docker build -t frontend .
docker run -p 3000:3000 frontend
```

## 🧪 Running Tests

### Backend Tests
```bash
cd mercadoclone-backend

# Run all tests
mvn test

# Or with Maven Wrapper
./mvnw test

# Run specific tests
mvn test -Dtest=TestClassName

# Run with coverage report
mvn test jacoco:report
```

### Tests via Docker
```bash
# Run tests inside container
docker-compose exec backend mvn test

# Or build a specific image for tests
docker build -f Dockerfile.test -t backend-test .
docker run --rm backend-test
```

## 🌐 Access Application

After successful execution:

- **Frontend**: http://localhost:3000
- **Backend API**: http://localhost:3001
- **Health Check**: http://localhost:3001/actuator/health (if configured)

## 🔍 Useful Debug Commands

### Copy the .env.local file to the mercadoclone-frontend folder
```shell
cp .env.local ~/workspace/meli-prototype/mercadoclone-frontend/
```

### View running containers
```bash
docker ps
```

### Access container shell
```bash
# Backend
docker-compose exec backend sh

# Frontend  
docker-compose exec frontend sh
```

### Clean Docker (if needed)
```bash
# Remove stopped containers
docker container prune

# Remove unused images
docker image prune

# Complete cleanup (be careful!)
docker system prune -a
```

### Check error logs
```bash
# Detailed logs
docker-compose logs --tail=50 backend
docker-compose logs --tail=50 frontend

# Follow logs in real-time
docker-compose logs -f --tail=10
```

## ⚡ Quick Commands

```bash
# Complete setup
git clone <repo>
cd <project>
docker-compose up --build -d

# Check status
docker-compose ps

# View logs
docker-compose logs -f

# Stop everything
docker-compose down
```

## 🐛 Troubleshooting

### Frontend can't connect to Backend
- Check if both containers are on the same network
- Verify `REACT_APP_API_URL=http://backend:3001` variable

### Backend build error
- Check if `pom.xml` is correct
- Clear cache: `docker system prune`

### Frontend dependency errors
- Check if `package.json` has all dependencies
- Clear `node_modules`: `docker-compose down -v`

### Ports already in use
```bash
# Check processes using ports
lsof -i :3000
lsof -i :3001

# Stop processes if needed
kill -9 <PID>
```

## 📝 Additional Notes

### Environment Variables
- Backend runs on port 3001
- Frontend runs on port 3000
- Development mode enabled by default

### File Structure
```
project/
├── docker-compose.yml
├── mercadoclone-backend/
│   ├── Dockerfile
│   ├── pom.xml
│   └── src/
└── mercadoclone-frontend/
    ├── Dockerfile
    ├── package.json
    └── src/
```

### Production Deployment
For production, consider:
- Using multi-stage builds for smaller images
- Setting appropriate environment variables
- Configuring proper logging and monitoring
- Using Docker secrets for sensitive data