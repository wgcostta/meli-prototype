# MercadoLibre Challenge - Product Detail Page

A full-stack application that replicates MercadoLibre's product detail page, built with Java Spring Boot backend and React Next.js frontend.

## 🌐 Live Demo

- **Frontend Application**: [https://meli-prototype-git-main-wagner-oliveira-da-costas-projects.vercel.app](https://meli-prototype-git-main-wagner-oliveira-da-costas-projects.vercel.app)
- **Backend API**: [https://meli-backend-33b27898349d.herokuapp.com](https://meli-backend-33b27898349d.herokuapp.com)
- **API Documentation**: [https://meli-backend-33b27898349d.herokuapp.com/swagger-ui/index.html#](https://meli-backend-33b27898349d.herokuapp.com/swagger-ui/index.html#)

## 🏗️ Project Structure

```
mercadolibre-challenge/
├── backend/                    # Spring Boot API
│   ├── src/
│   │   ├── main/java/
│   │   └── test/java/
│   ├── pom.xml
│   └── README.md
├── frontend/                   # Next.js React App
│   ├── src/
│   │   ├── components/
│   │   ├── pages/
│   │   └── styles/
│   ├── package.json
│   └── README.md
├── docs/                       # Documentation
│   ├── technical-documentation.md
│   └── api-documentation.md
└── README.md
```

## 🚀 Quick Start

### Prerequisites

- **Java 11+** (for backend)
- **Node.js 16+** (for frontend)
- **Maven 3.6+** (for backend)
- **npm or yarn** (for frontend)

### Clone the Repository

```bash
git clone https://github.com/your-username/mercadolibre-challenge.git
cd mercadolibre-challenge
```

## 🔧 Backend Setup (Spring Boot)

### 1. Navigate to Backend Directory

```bash
cd backend
```

### 2. Install Dependencies and Run

```bash
# Using Maven
mvn clean install
mvn spring-boot:run

# Or using Maven Wrapper (if available)
./mvnw clean install
./mvnw spring-boot:run
```

### 3. Verify Backend is Running

The backend will start on `http://localhost:3001`

**Test the API:**
```bash
curl http://localhost:3001/api/v1/products/1
```

**Health Check:**
```bash
curl http://localhost:3001/actuator/health
```

### Backend Configuration

The application uses the following default configuration:

- **Port**: 3001
- **Data Storage**: Local JSON files in `src/main/resources/data/`
- **CORS**: Configured to allow frontend on port 3000
- **Logging**: INFO level, file logging enabled

## ⚛️ Frontend Setup (React/Next.js)

### 1. Navigate to Frontend Directory

```bash
cd mercadoclone-frontend
```

### 2. Install Dependencies

```bash
npm install
# or
yarn install
```

### 3. Configure Environment Variables

Create a `.env.local` file in the frontend directory:

```bash
# API Configuration
NEXT_PUBLIC_API_BASE_URL=http://localhost:3001/api
NEXT_PUBLIC_APP_NAME=MercadoLibre Challenge

# Optional: Analytics or other services
# NEXT_PUBLIC_GA_ID=your-google-analytics-id
```

### 4. Run Development Server

```bash
npm run dev
# or
yarn dev
```

### 5. Access the Application

Open [http://localhost:3000](http://localhost:3000) in your browser.

### Frontend Scripts

```bash
npm run dev          # Start development server
npm run build        # Build for production
npm run start        # Start production server
npm run test         # Run tests
npm run test:watch   # Run tests in watch mode
npm run lint         # Run ESLint
```

## 🧪 Testing

### Backend Tests

```bash
cd mercadoclone-backend

# Run all tests
mvn test

# Run tests with coverage report
mvn test jacoco:report

# View coverage report
open target/site/jacoco/index.html
```

**Coverage Requirements**: Minimum 80% code coverage (enforced by JaCoCo)

### Frontend Tests

```bash
cd frontend

# Run all tests
npm test

# Run tests in watch mode
npm run test:watch

# Run tests with coverage
npm run test:coverage
```

## 📊 Code Coverage Reports

After running tests with coverage:

- **Backend**: `mercadoclone-backend/target/site/jacoco/index.html`
- **Frontend**: `mercadoclone-frontend/coverage/lcov-report/index.html`

## 🛠️ API Endpoints

### Products

| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/api/v1/products` | List all products |
| GET | `/api/v1/products/{id}` | Get product by ID |

### Example API Response

```json
{
  "id": "1",
  "title": "Samsung Galaxy A55 5G Dual SIM 256 GB 8 GB RAM",
  "price": {
    "amount": 439,
    "currency": "USD"
  },
  "images": [
    "https://example.com/image1.jpg",
    "https://example.com/image2.jpg"
  ],
  "description": "Product description...",
  "seller": {
    "name": "Official Samsung Store",
    "reputation": "gold",
    "rating": 4.8
  },
  "features": [
    {
      "name": "Screen Size",
      "value": "6.6 inches"
    }
  ]
}
```

## 🚢 Deployment

### Backend Deployment (Heroku)

```bash
cd backend

# Login to Heroku
heroku login

# Create Heroku app
heroku create your-app-name-backend

# Set Java version
echo "java.runtime.version=11" > system.properties

# Deploy
git add .
git commit -m "Deploy to Heroku"
git push heroku main
```

### Frontend Deployment (Vercel)

```bash
cd frontend

# Install Vercel CLI
npm i -g vercel

# Deploy
vercel

# Set environment variables in Vercel dashboard
# NEXT_PUBLIC_API_BASE_URL=https://your-backend-url.herokuapp.com/api
```

## 🔍 Key Features Implemented

### Frontend
- ✅ Responsive product detail page
- ✅ Image gallery with navigation
- ✅ Product information display
- ✅ Price and payment methods
- ✅ Seller information
- ✅ Product characteristics
- ✅ Loading states and error handling

### Backend
- ✅ RESTful API endpoints
- ✅ JSON file-based persistence
- ✅ Error handling and validation
- ✅ CORS configuration
- ✅ Unit and integration tests
- ✅ 80%+ code coverage
- ✅ API documentation

## 🛡️ Technical Requirements Met

- ✅ **Frontend**: Responsive design mimicking MercadoLibre
- ✅ **Backend**: RESTful API with product endpoints
- ✅ **No Database**: Local JSON/CSV file persistence
- ✅ **Error Handling**: Comprehensive error management
- ✅ **Testing**: 80%+ code coverage
- ✅ **Documentation**: Complete technical documentation

## 📁 Data Structure

Sample product data is stored in `backend/src/main/resources/data/products.json`:

```json
[
  {
    "id": "1",
    "title": "Samsung Galaxy A55 5G...",
    "price": {...},
    "images": [...],
    "description": "...",
    "seller": {...},
    "features": [...]
  }
]
```

## 🐛 Troubleshooting

### Common Issues

**Backend not starting:**
- Verify Java 11+ is installed: `java -version`
- Check port 3001 is available: `lsof -i :3001`

**Frontend build errors:**
- Clear npm cache: `npm cache clean --force`
- Delete node_modules and reinstall: `rm -rf node_modules && npm install`

**CORS errors:**
- Ensure backend CORS is configured for frontend URL
- Check environment variables are set correctly

**API connection issues:**
- Verify backend is running on correct port
- Check `NEXT_PUBLIC_API_BASE_URL` in frontend `.env.local`

## 📝 Development Notes

- **No Lombok**: Backend intentionally avoids Lombok for explicit code
- **Mobile-first**: Frontend designed with mobile-first approach
- **Test-driven**: Both applications maintain high test coverage
- **Production-ready**: Configured for easy deployment to cloud platforms

## 📞 Support

For questions or issues regarding this implementation, please refer to the technical documentation in the `docs/` folder or create an issue in the repository.

---

**Author**: Wagner Oliveira da Costa
**Email**: wg.o.costa@gmail.com  
**Date**: June 2025  
**Challenge**: MercadoLibre Technical Assessment