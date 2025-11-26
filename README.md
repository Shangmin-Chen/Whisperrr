# Whisperrr - AI-Powered Audio Transcription Platform

A production-ready, full-stack audio transcription platform that leverages OpenAI's Whisper library to provide high-quality speech-to-text conversion. Built with modern technologies including Spring Boot, FastAPI, and React TypeScript, Whisperrr offers a scalable, maintainable solution for audio transcription needs.

## 🎯 Project Vision

Whisperrr transforms audio content into accurate, searchable text using state-of-the-art AI technology. Whether you're transcribing interviews, meetings, podcasts, or any audio content, Whisperrr provides professional-grade transcription with an intuitive user interface and robust backend infrastructure.

## 🏗️ System Architecture

Whisperrr follows a modern microservices architecture with clear separation of concerns:

```
┌─────────────────────────────────────────────────────────────────────┐
│                        Whisperrr Platform                          │
├─────────────────────────────────────────────────────────────────────┤
│                                                                     │
│  ┌─────────────────┐    ┌─────────────────┐    ┌─────────────────┐ │
│  │                 │    │                 │    │                 │ │
│  │  React Frontend │◄──►│ Spring Boot API │◄──►│ Python Service  │ │
│  │   (Port 3000)   │    │   (Port 8080)   │    │   (Port 8000)   │ │
│  │                 │    │                 │    │                 │ │
│  │  • File Upload  │    │ • REST API      │    │ • Whisper AI    │ │
│  │  • Status UI    │    │ • Job Management│    │ • Model Mgmt    │ │
│  │  • Results View │    │ • Database      │    │ • Audio Process │ │
│  └─────────────────┘    └─────────────────┘    └─────────────────┘ │
│           │                       │                       │        │
│           │                       │                       │        │
│           ▼                       ▼                       ▼        │
│  ┌─────────────────┐    ┌─────────────────┐    ┌─────────────────┐ │
│  │   Web Browser   │    │   PostgreSQL    │    │ Whisper Models  │ │
│  │    (Client)     │    │   Database      │    │  (tiny → large) │ │
│  └─────────────────┘    └─────────────────┘    └─────────────────┘ │
│                                                                     │
└─────────────────────────────────────────────────────────────────────┘
```

### Service Responsibilities

- **React Frontend**: Modern, responsive UI with real-time status updates
- **Spring Boot API**: Business logic, job orchestration, and data persistence  
- **Python Service**: AI-powered transcription using OpenAI Whisper models
- **PostgreSQL**: Reliable data storage with full ACID compliance

## ✨ Key Features

### 🎤 Audio Transcription
- **High Accuracy**: Powered by OpenAI's Whisper AI models (tiny to large)
- **Multi-Language**: Support for 99+ languages with automatic detection
- **Multiple Formats**: MP3, WAV, M4A, FLAC, OGG, WMA (up to 25MB)
- **Real-Time Processing**: Live status updates with progress tracking
- **Quality Options**: Choose model size based on accuracy vs speed needs

### 🖥️ User Experience
- **Drag & Drop Upload**: Intuitive file upload with visual feedback
- **Responsive Design**: Works seamlessly on desktop and mobile devices
- **Dark Mode Support**: Modern UI with theme switching
- **Real-Time Status**: Live progress monitoring with automatic polling
- **Rich Results**: Detailed transcription with timing and confidence scores

### 🔧 Technical Excellence
- **Microservices Architecture**: Scalable, maintainable service separation
- **Production Ready**: Comprehensive error handling and monitoring
- **Database Persistence**: Full audit trail and job history
- **RESTful APIs**: Clean, documented endpoints with OpenAPI specs
- **Docker Support**: Containerized deployment with docker-compose

### 🚀 Performance & Scalability
- **Asynchronous Processing**: Non-blocking transcription pipeline
- **Model Caching**: Efficient memory management and model reuse
- **Connection Pooling**: Optimized database connections
- **Concurrent Processing**: Multiple transcription jobs simultaneously
- **Health Monitoring**: Built-in health checks and metrics

## 📁 Project Structure

```
Whisperrr/
├── 📄 README.md                    # This comprehensive guide
├── 📄 OVERVIEW.md                  # Detailed technical overview
├── 📄 LICENSE                      # MIT License
├── 🐳 docker-compose.yml           # Multi-service orchestration
│
├── 🖥️ frontend/                    # React TypeScript Frontend
│   ├── src/
│   │   ├── components/            # Reusable UI components
│   │   ├── hooks/                 # Custom React hooks
│   │   ├── pages/                 # Route-level components
│   │   ├── services/              # API communication layer
│   │   ├── types/                 # TypeScript definitions
│   │   └── utils/                 # Helper functions
│   ├── package.json               # Node.js dependencies
│   └── 🐳 Dockerfile              # Container configuration
│
├── ⚙️ backend/                     # Spring Boot API Server
│   ├── src/main/java/com/shangmin/whisperrr/
│   │   ├── config/                # Configuration classes
│   │   ├── controller/            # REST API endpoints
│   │   ├── dto/                   # Data Transfer Objects
│   │   ├── entity/                # JPA database entities
│   │   ├── enums/                 # Enumeration types
│   │   ├── exception/             # Error handling
│   │   ├── repository/            # Data access layer
│   │   └── service/               # Business logic
│   ├── src/main/resources/
│   │   ├── application.properties # Configuration
│   │   └── db/migration/          # Database migrations
│   ├── pom.xml                    # Maven dependencies
│   └── 🐳 Dockerfile              # Container configuration
│
├── 🐍 python-service/             # FastAPI Transcription Service
│   ├── app/
│   │   ├── main.py                # FastAPI application
│   │   ├── config.py              # Configuration management
│   │   ├── models.py              # Pydantic data models
│   │   ├── whisper_service.py     # Core transcription logic
│   │   ├── exceptions.py          # Custom exceptions
│   │   └── utils.py               # Utility functions
│   ├── requirements.txt           # Python dependencies
│   ├── venv/                      # Virtual environment
│   └── 🐳 Dockerfile              # Container configuration
│
└── 🗄️ Database/                   # PostgreSQL Schema
    ├── Users                      # User accounts
    ├── AudioFiles                 # File metadata
    ├── Jobs                       # Transcription jobs
    └── Transcriptions             # Results storage
```

## 🚀 Quick Start

### 📋 Prerequisites

Ensure you have the following installed on your system:

- **Java 21+** - Latest LTS version for Spring Boot backend
- **Python 3.11+** - Required for OpenAI Whisper compatibility  
- **Node.js 18+** - For React frontend development
- **PostgreSQL 12+** - Database for persistent storage
- **Docker** (Optional) - For containerized deployment

### ⚡ Option 1: Docker Compose (Recommended)

The fastest way to get Whisperrr running:

```bash
# Clone the repository
git clone <repository-url>
cd Whisperrr

# Start all services with Docker
docker-compose up --build

# Access the application
# Frontend: http://localhost:3000
# Backend API: http://localhost:8080
# Python Service: http://localhost:8000
# Database: localhost:5432
```

### 🔧 Option 2: Manual Setup

For development or customization:

#### 1️⃣ Database Setup

```bash
# Install PostgreSQL (if not already installed)
# macOS: brew install postgresql
# Ubuntu: sudo apt install postgresql postgresql-contrib
# Windows: Download from postgresql.org

# Start PostgreSQL service
# macOS: brew services start postgresql
# Ubuntu: sudo systemctl start postgresql
# Windows: Use Services app

# Create database and user
createdb transcription_db
psql -c "CREATE USER transcription_user WITH PASSWORD 'transcription_pass';"
psql -c "GRANT ALL PRIVILEGES ON DATABASE transcription_db TO transcription_user;"
```

#### 2️⃣ Backend Setup (Spring Boot)

```bash
cd backend

# Configure database connection (edit application.properties if needed)
# spring.datasource.url=jdbc:postgresql://localhost:5432/transcription_db
# spring.datasource.username=transcription_user
# spring.datasource.password=transcription_pass

# Build and run the application
./mvnw spring-boot:run

# Verify it's running
curl http://localhost:8080/actuator/health
```

✅ Backend API available at `http://localhost:8080`

#### 3️⃣ Python Service Setup

```bash
cd python-service

# Create and activate virtual environment
python -m venv venv
source venv/bin/activate  # Windows: venv\Scripts\activate

# Install dependencies (this may take a few minutes for Whisper)
pip install -r requirements.txt

# Configure environment (optional)
cp env.example .env
# Edit .env file as needed

# Start the service
python -m app.main

# Verify it's running
curl http://localhost:8000/health
```

✅ Python service available at `http://localhost:8000`

#### 4️⃣ Frontend Setup

```bash
cd frontend

# Install Node.js dependencies
npm install

# Configure environment (optional)
cp env.example .env
# Edit .env file as needed

# Start development server
npm start

# Browser should automatically open to http://localhost:3000
```

✅ Frontend application available at `http://localhost:3000`

## ⚙️ Configuration

### 🔧 Environment Variables

Each service can be configured via environment variables for different deployment scenarios:

#### Backend Configuration (`backend/src/main/resources/application.properties`)

```properties
# Database Configuration
spring.datasource.url=jdbc:postgresql://localhost:5432/transcription_db
spring.datasource.username=transcription_user
spring.datasource.password=transcription_pass

# CORS Configuration (for frontend communication)
cors.allowed-origins=http://localhost:3000,http://localhost:3001
cors.allowed-methods=GET,POST,PUT,DELETE,OPTIONS
cors.allowed-headers=*
cors.allow-credentials=true

# Python Service Integration
whisperrr.service.url=http://localhost:8000
whisperrr.service.timeout=300000

# File Upload Limits
spring.servlet.multipart.max-file-size=25MB
spring.servlet.multipart.max-request-size=25MB

# Logging Configuration
logging.level.com.shangmin.whisperrr=INFO
logging.level.org.springframework.web=INFO
```

#### Python Service Configuration (`python-service/.env`)

```bash
# Whisper Model Configuration
MODEL_SIZE=base                    # tiny, base, small, medium, large
MAX_FILE_SIZE_MB=25               # Maximum upload size
UPLOAD_DIR=/tmp/whisperrr_uploads # Temporary file storage

# API Configuration
LOG_LEVEL=INFO                    # DEBUG, INFO, WARNING, ERROR
MAX_CONCURRENT_TRANSCRIPTIONS=3   # Parallel processing limit
REQUEST_TIMEOUT_SECONDS=300       # Request timeout

# CORS Configuration
CORS_ORIGINS=http://localhost:8080,http://localhost:3000

# Performance Settings
CLEANUP_TEMP_FILES=true          # Automatic cleanup
ENABLE_METRICS=true              # Performance monitoring
```

#### Frontend Configuration (`frontend/.env`)

```bash
# API Configuration
REACT_APP_API_URL=http://localhost:8080/api  # Backend API URL
REACT_APP_POLLING_INTERVAL=2000              # Status polling interval (ms)

# File Upload Configuration  
REACT_APP_MAX_FILE_SIZE=25                   # Max file size (MB)

# Feature Flags
REACT_APP_ENABLE_DEBUG=false                 # Debug mode
REACT_APP_ENABLE_ANALYTICS=false             # Analytics tracking
```

### 🐳 Docker Configuration

For containerized deployment, override settings via docker-compose.yml:

```yaml
services:
  backend:
    environment:
      - SPRING_DATASOURCE_URL=jdbc:postgresql://postgres:5432/transcription_db
      - WHISPERRR_SERVICE_URL=http://python-service:8000
      
  python-service:
    environment:
      - MODEL_SIZE=large
      - MAX_CONCURRENT_TRANSCRIPTIONS=5
      
  frontend:
    environment:
      - REACT_APP_API_URL=http://localhost:8080/api
```

## 🌐 API Documentation

### 📋 Backend API Endpoints (Port 8080)

| Method | Endpoint | Description | Request | Response |
|--------|----------|-------------|---------|----------|
| `POST` | `/api/audio/upload` | Upload audio file for transcription | `multipart/form-data` | `AudioUploadResponse` |
| `GET` | `/api/audio/status/{jobId}` | Get real-time transcription status | Job ID | `TranscriptionStatusResponse` |
| `GET` | `/api/audio/result/{jobId}` | Retrieve completed transcription | Job ID | `TranscriptionResultResponse` |
| `GET` | `/api/audio/health` | Service health check | None | Health status |

#### Example API Usage

```bash
# Upload audio file
curl -X POST http://localhost:8080/api/audio/upload \
  -F "audioFile=@recording.mp3"

# Check status
curl http://localhost:8080/api/audio/status/550e8400-e29b-41d4-a716-446655440000

# Get results
curl http://localhost:8080/api/audio/result/550e8400-e29b-41d4-a716-446655440000
```

### 🐍 Python Service Endpoints (Port 8000)

| Method | Endpoint | Description | Parameters | Response |
|--------|----------|-------------|------------|----------|
| `POST` | `/transcribe` | Direct audio transcription | `file`, `model_size`, `language` | `TranscriptionResponse` |
| `GET` | `/health` | Service health and model status | None | `HealthResponse` |
| `GET` | `/model/info` | Current model information | None | `ModelInfoResponse` |
| `POST` | `/model/load/{model_size}` | Load specific Whisper model | Model size | Load confirmation |

#### Interactive API Documentation

- **Swagger UI**: http://localhost:8000/docs
- **ReDoc**: http://localhost:8000/redoc

### 📊 Response Examples

#### Transcription Status Response
```json
{
  "jobId": "550e8400-e29b-41d4-a716-446655440000",
  "status": "PROCESSING",
  "updatedAt": "2024-01-15T10:30:45.123Z",
  "message": "Transcription is in progress",
  "progress": 65
}
```

#### Transcription Result Response
```json
{
  "jobId": "550e8400-e29b-41d4-a716-446655440000",
  "text": "Hello, this is a sample transcription...",
  "language": "en",
  "confidence": 0.95,
  "duration": 120.5,
  "segments": [
    {
      "start": 0.0,
      "end": 2.5,
      "text": "Hello, this is a sample",
      "confidence": 0.98
    }
  ],
  "completedAt": "2024-01-15T10:32:15.456Z",
  "processingTime": 8.3,
  "modelUsed": "base"
}
```

## 🎯 How to Use Whisperrr

### 📝 Step-by-Step Transcription Process

1. **🚀 Start the Application**
   - Ensure all services are running (see Quick Start above)
   - Open your browser to `http://localhost:3000`

2. **📁 Upload Your Audio File**
   - Drag and drop an audio file onto the upload area, or
   - Click to browse and select a file from your computer
   - Supported formats: MP3, WAV, M4A, FLAC, OGG, WMA (max 25MB)

3. **⏳ Monitor Progress**
   - Watch real-time status updates as your file is processed
   - See progress indicators and estimated completion time
   - Status updates automatically every 2 seconds

4. **📊 View Results**
   - Access detailed transcription results when processing completes
   - View confidence scores and timing information
   - Export results in multiple formats (TXT, JSON)

### 🎛️ Whisper Model Options

Choose the right model for your needs:

| Model | Size | Speed | Accuracy | Best For |
|-------|------|-------|----------|----------|
| `tiny` | 39 MB | ~32x realtime | Basic | Quick drafts, testing |
| `base` | 74 MB | ~16x realtime | Good | General use (default) |
| `small` | 244 MB | ~6x realtime | Better | Balanced quality/speed |
| `medium` | 769 MB | ~2x realtime | High | Important content |
| `large` | 1550 MB | ~1x realtime | Highest | Maximum accuracy |

### 🌍 Language Support

Whisperrr automatically detects the language of your audio, or you can specify:

- **99+ Languages Supported**: English, Spanish, French, German, Chinese, Japanese, and many more
- **Automatic Detection**: No need to specify language in most cases
- **Language Hints**: Improve accuracy by specifying the expected language
- **Multilingual Content**: Handles mixed-language audio effectively

### 💡 Tips for Best Results

- **Audio Quality**: Higher quality audio produces better transcriptions
- **Clear Speech**: Minimize background noise and ensure clear pronunciation
- **File Format**: WAV and FLAC provide the best quality, MP3 is most convenient
- **Length**: Longer files may take more time but are handled efficiently
- **Model Selection**: Use larger models for important or difficult audio

## 🚀 Deployment Options

### 🔧 Development Environment

Perfect for local development with hot reload:

```bash
# Each service runs independently with auto-reload
cd backend && ./mvnw spring-boot:run     # Port 8080
cd python-service && python -m app.main  # Port 8000  
cd frontend && npm start                  # Port 3000
```

### 🐳 Docker Deployment (Recommended)

Consistent environment across all platforms:

```bash
# Production deployment
docker-compose up -d

# Development with live reload
docker-compose -f docker-compose.dev.yml up

# Scale services as needed
docker-compose up --scale python-service=3
```

### ☁️ Cloud Deployment Options

#### Option 1: Microservices (Recommended for Scale)

```
┌─────────────────────────────────────────────────────────────┐
│                    Cloud Architecture                       │
├─────────────────────────────────────────────────────────────┤
│                                                             │
│  Frontend (CDN)     Backend (Container)    Python (Serverless) │
│  ┌─────────────┐    ┌─────────────────┐    ┌─────────────┐ │
│  │ Vercel      │    │ AWS ECS         │    │ AWS Lambda  │ │
│  │ Netlify     │    │ Google Cloud    │    │ Google      │ │
│  │ Cloudflare  │    │ Run             │    │ Cloud Fn    │ │
│  └─────────────┘    └─────────────────┘    └─────────────┘ │
│                                                             │
│                    ┌─────────────────┐                     │
│                    │ Managed DB      │                     │
│                    │ AWS RDS         │                     │
│                    │ Google Cloud    │                     │
│                    │ SQL             │                     │
│                    └─────────────────┘                     │
└─────────────────────────────────────────────────────────────┘
```

#### Option 2: Container Orchestration

```bash
# Kubernetes deployment
kubectl apply -f k8s/

# Docker Swarm deployment  
docker stack deploy -c docker-stack.yml whisperrr
```

#### Option 3: Platform-as-a-Service

```bash
# Heroku deployment
git push heroku main

# Railway deployment
railway up

# Render deployment
render deploy
```

### 🔒 Production Considerations

#### Security
- Enable HTTPS/TLS for all services
- Configure proper CORS origins
- Use environment variables for secrets
- Implement rate limiting
- Enable audit logging

#### Performance
- Use CDN for frontend assets
- Configure database connection pooling
- Implement caching strategies
- Monitor resource usage
- Set up auto-scaling

#### Monitoring
- Health check endpoints: `/actuator/health`, `/health`
- Application metrics and logging
- Database performance monitoring
- Error tracking and alerting
- Uptime monitoring

### 📊 Environment-Specific Configuration

#### Production Environment Variables

```bash
# Backend
SPRING_PROFILES_ACTIVE=production
SPRING_DATASOURCE_URL=jdbc:postgresql://prod-db:5432/whisperrr
CORS_ALLOWED_ORIGINS=https://yourdomain.com

# Python Service  
MODEL_SIZE=large
MAX_CONCURRENT_TRANSCRIPTIONS=10
LOG_LEVEL=INFO

# Frontend
REACT_APP_API_URL=https://api.yourdomain.com
REACT_APP_ENVIRONMENT=production
```

## 🧪 Testing & Quality Assurance

### 🔍 Running Tests

#### Backend Tests (Spring Boot)
```bash
cd backend

# Run all tests
./mvnw test

# Run with coverage
./mvnw test jacoco:report

# Run specific test class
./mvnw test -Dtest=AudioControllerTest

# Integration tests
./mvnw test -Dtest=*IntegrationTest
```

#### Frontend Tests (React)
```bash
cd frontend

# Run all tests
npm test

# Run tests with coverage
npm run test:coverage

# Run tests in CI mode
npm run test:ci

# Run specific test file
npm test -- FileUpload.test.tsx
```

#### Python Service Tests (FastAPI)
```bash
cd python-service

# Activate virtual environment
source venv/bin/activate

# Run all tests
python -m pytest

# Run with coverage
python -m pytest --cov=app

# Run specific test file
python -m pytest tests/test_whisper_service.py

# Run with verbose output
python -m pytest -v
```

### 🔧 Quality Checks

#### Code Formatting & Linting
```bash
# Backend (Java)
./mvnw spotless:check    # Check formatting
./mvnw spotless:apply    # Apply formatting

# Frontend (TypeScript)
npm run lint             # ESLint check
npm run lint:fix         # Auto-fix issues
npm run format           # Prettier formatting

# Python Service
black app/               # Format code
flake8 app/             # Linting
mypy app/               # Type checking
```

#### Security Scanning
```bash
# Backend dependencies
./mvnw dependency-check:check

# Frontend dependencies  
npm audit
npm audit fix

# Python dependencies
safety check
bandit -r app/
```

## 📊 Monitoring & Observability

### 🏥 Health Checks

Monitor service availability and status:

```bash
# Backend API Health
curl http://localhost:8080/actuator/health
# Response: {"status":"UP","components":{"db":{"status":"UP"}}}

# Python Service Health  
curl http://localhost:8000/health
# Response: {"status":"healthy","model_loaded":true,"uptime":3600.5}

# Frontend Health
# Accessible via browser at http://localhost:3000
# Check browser console for any JavaScript errors
```

### 📈 Performance Metrics

#### Backend Metrics (Spring Boot Actuator)
```bash
# Application metrics
curl http://localhost:8080/actuator/metrics

# Specific metrics
curl http://localhost:8080/actuator/metrics/jvm.memory.used
curl http://localhost:8080/actuator/metrics/http.server.requests
curl http://localhost:8080/actuator/metrics/jdbc.connections.active

# Database health
curl http://localhost:8080/actuator/health/db
```

#### Python Service Metrics
```bash
# Model information
curl http://localhost:8000/model/info

# Memory usage and performance
curl http://localhost:8000/health
```

### 🔍 Logging & Debugging

#### Log Levels and Configuration
```bash
# Backend - Enable debug logging
export LOGGING_LEVEL_COM_SHANGMIN_WHISPERRR=DEBUG

# Python Service - Enable debug logging  
export LOG_LEVEL=DEBUG

# Frontend - Enable development tools
export REACT_APP_ENABLE_DEBUG=true
```

#### Log Locations
- **Backend**: Console output or configured log file
- **Python Service**: Structured JSON logs to console
- **Frontend**: Browser developer tools console
- **Database**: PostgreSQL log directory

### 🚨 Alerting & Monitoring Setup

#### Recommended Monitoring Stack
```yaml
# docker-compose.monitoring.yml
services:
  prometheus:
    image: prom/prometheus
    ports: ["9090:9090"]
    
  grafana:
    image: grafana/grafana
    ports: ["3001:3000"]
    
  loki:
    image: grafana/loki
    ports: ["3100:3100"]
```

#### Key Metrics to Monitor
- **Response Times**: API endpoint latency
- **Error Rates**: 4xx/5xx HTTP responses  
- **Throughput**: Requests per second
- **Resource Usage**: CPU, memory, disk
- **Queue Depth**: Pending transcription jobs
- **Model Performance**: Transcription accuracy and speed

## 👨‍💻 Development Guide

### 🎯 Getting Started as a Developer

1. **📖 Read the Documentation**
   - Start with this README for overview
   - Read [OVERVIEW.md](OVERVIEW.md) for detailed technical architecture
   - Review inline code documentation for implementation details

2. **🏗️ Understand the Architecture**
   - Study the microservices communication flow
   - Learn the database schema and relationships
   - Understand the job lifecycle and state management

3. **🔧 Set Up Development Environment**
   - Follow the Quick Start guide above
   - Configure your IDE with appropriate plugins
   - Set up debugging and testing tools

### 🚀 Adding New Features

#### Backend Development (Spring Boot)
```bash
# 1. Add new endpoint in AudioController.java
@PostMapping("/new-feature")
public ResponseEntity<FeatureResponse> newFeature(@RequestBody FeatureRequest request) {
    // Implementation
}

# 2. Add business logic in AudioService
public interface AudioService {
    FeatureResponse processNewFeature(FeatureRequest request);
}

# 3. Create DTOs for request/response
public class FeatureRequest { /* fields */ }
public class FeatureResponse { /* fields */ }

# 4. Add database entities if needed
@Entity
public class NewEntity extends BaseEntity { /* fields */ }

# 5. Create repository interface
public interface NewEntityRepository extends JpaRepository<NewEntity, Long> {}
```

#### Python Service Development (FastAPI)
```python
# 1. Add new endpoint in main.py
@app.post("/new-feature", response_model=FeatureResponse)
async def new_feature(request: FeatureRequest):
    # Implementation
    
# 2. Add Pydantic models in models.py
class FeatureRequest(BaseModel):
    # fields with validation
    
class FeatureResponse(BaseModel):
    # response fields

# 3. Add business logic in appropriate service class
class NewFeatureService:
    async def process_feature(self, request: FeatureRequest) -> FeatureResponse:
        # Implementation
```

#### Frontend Development (React)
```typescript
// 1. Add API service method in services/transcription.ts
export const TranscriptionService = {
  newFeature: async (request: FeatureRequest): Promise<FeatureResponse> => {
    const response = await apiClient.post('/new-feature', request);
    return response.data;
  }
};

// 2. Create custom hook for state management
export const useNewFeature = () => {
  const mutation = useMutation({
    mutationFn: TranscriptionService.newFeature,
    // configuration
  });
  
  return { /* hook interface */ };
};

// 3. Create React component
export const NewFeatureComponent: React.FC = () => {
  const { newFeature, isLoading, error } = useNewFeature();
  
  return (
    // JSX implementation
  );
};
```

### 📋 Code Style Guidelines

#### Java (Spring Boot)
- **Conventions**: Follow Spring Boot and Java naming conventions
- **Documentation**: Comprehensive Javadoc for all public methods
- **Testing**: Unit tests with JUnit 5 and Mockito
- **Formatting**: Use consistent indentation and spacing
- **Architecture**: Maintain layered architecture (Controller → Service → Repository)

#### Python (FastAPI)
- **Style**: Follow PEP 8 with Black auto-formatting
- **Type Hints**: Use type annotations for all function parameters and returns
- **Documentation**: Comprehensive docstrings following Google style
- **Testing**: pytest with async test support
- **Validation**: Pydantic models for all request/response data

#### TypeScript/React
- **Style**: Follow React and TypeScript best practices
- **Components**: Functional components with hooks
- **Types**: Strict TypeScript with proper interface definitions
- **Testing**: Jest and React Testing Library
- **Hooks**: Custom hooks for reusable stateful logic

### 🔄 Development Workflow

#### Git Workflow
```bash
# 1. Create feature branch
git checkout -b feature/new-awesome-feature

# 2. Make changes with descriptive commits
git commit -m "feat: add new transcription model support"

# 3. Push and create pull request
git push origin feature/new-awesome-feature

# 4. Code review and merge
```

#### Testing Strategy
```bash
# Run tests before committing
cd backend && ./mvnw test
cd frontend && npm test  
cd python-service && python -m pytest

# Check code quality
npm run lint
./mvnw spotless:check
black --check app/
```

### 🐛 Debugging Tips

#### Backend Debugging
- Enable debug logging: `logging.level.com.shangmin.whisperrr=DEBUG`
- Use Spring Boot DevTools for hot reload
- Monitor database queries with `spring.jpa.show-sql=true`
- Use IDE debugger with breakpoints

#### Python Service Debugging  
- Enable debug logging: `LOG_LEVEL=DEBUG`
- Use `pdb` for interactive debugging
- Monitor memory usage for model loading
- Profile performance with `cProfile`

#### Frontend Debugging
- Use React Developer Tools browser extension
- Enable React Query DevTools for state inspection
- Monitor network requests in browser DevTools
- Use TypeScript strict mode for compile-time error catching

## 🤝 Contributing

We welcome contributions to make Whisperrr even better! Here's how you can help:

### 🚀 Getting Started

1. **Fork the Repository**
   ```bash
   git clone https://github.com/yourusername/Whisperrr.git
   cd Whisperrr
   ```

2. **Set Up Development Environment**
   - Follow the development setup guide above
   - Ensure all tests pass before making changes
   - Familiarize yourself with the codebase structure

3. **Find Something to Work On**
   - Check the [Issues](../../issues) for open tasks
   - Look for "good first issue" labels for beginners
   - Propose new features via issue discussion

### 📝 Contribution Process

1. **Create a Feature Branch**
   ```bash
   git checkout -b feature/your-feature-name
   ```

2. **Make Your Changes**
   - Write clean, well-documented code
   - Follow existing code style and conventions
   - Add comprehensive tests for new functionality
   - Update documentation as needed

3. **Test Your Changes**
   ```bash
   # Run all tests
   cd backend && ./mvnw test
   cd frontend && npm test
   cd python-service && python -m pytest
   
   # Check code quality
   npm run lint
   ./mvnw spotless:check
   black --check app/
   ```

4. **Commit Your Changes**
   ```bash
   git add .
   git commit -m "feat: add awesome new feature"
   
   # Follow conventional commit format:
   # feat: new feature
   # fix: bug fix
   # docs: documentation changes
   # style: formatting changes
   # refactor: code refactoring
   # test: adding tests
   # chore: maintenance tasks
   ```

5. **Submit a Pull Request**
   - Push your branch to your fork
   - Create a pull request with a clear description
   - Link any related issues
   - Wait for code review and address feedback

### 🎯 Contribution Guidelines

#### Code Quality Standards
- **Documentation**: All public methods must have comprehensive documentation
- **Testing**: New features require corresponding tests
- **Performance**: Consider performance impact of changes
- **Security**: Follow security best practices
- **Accessibility**: Ensure UI changes are accessible

#### Areas for Contribution
- 🐛 **Bug Fixes**: Help identify and fix issues
- ✨ **New Features**: Add new transcription capabilities
- 📚 **Documentation**: Improve guides and API docs
- 🧪 **Testing**: Increase test coverage
- 🎨 **UI/UX**: Enhance user interface and experience
- 🚀 **Performance**: Optimize speed and resource usage
- 🔒 **Security**: Strengthen security measures

### 💡 Ideas for Contributions

- **Multi-language UI**: Internationalization support
- **Batch Processing**: Upload and process multiple files
- **Export Formats**: Additional export options (SRT, VTT, etc.)
- **Audio Enhancement**: Noise reduction preprocessing
- **Real-time Transcription**: Live audio streaming support
- **User Management**: Authentication and user accounts
- **API Rate Limiting**: Prevent abuse and ensure fair usage
- **Monitoring Dashboard**: Enhanced observability tools

### 🆘 Getting Help

- **Documentation**: Check [OVERVIEW.md](OVERVIEW.md) for technical details
- **Issues**: Search existing issues or create a new one
- **Discussions**: Use GitHub Discussions for questions
- **Code Review**: Don't hesitate to ask for feedback

### 🏆 Recognition

Contributors will be recognized in:
- README contributors section
- Release notes for significant contributions
- Special thanks for major features or fixes

Thank you for helping make Whisperrr better! 🎉

## 📄 License

This project is licensed under the MIT License - see the LICENSE file for details.

## 🆘 Troubleshooting & Support

### 🔧 Common Issues & Solutions

#### Installation & Setup Issues

**🐍 Whisper Installation Problems**
```bash
# Issue: Python 3.13 compatibility problems
# Solution: Use Python 3.11 or 3.12
pyenv install 3.11.7
pyenv local 3.11.7
python -m venv venv
source venv/bin/activate
pip install -r requirements.txt

# Alternative: Use conda
conda create -n whisperrr python=3.11
conda activate whisperrr
conda install -c conda-forge openai-whisper

# Recommended: Use Docker (avoids all compatibility issues)
docker-compose up --build
```

**🗄️ Database Connection Issues**
```bash
# Check if PostgreSQL is running
brew services list | grep postgresql  # macOS
sudo systemctl status postgresql      # Linux
services.msc                         # Windows

# Verify database exists
psql -l | grep transcription_db

# Create database if missing
createdb transcription_db

# Test connection
psql -h localhost -p 5432 -U transcription_user -d transcription_db
```

**🌐 CORS Configuration Issues**
```bash
# Check backend CORS settings
# File: backend/src/main/resources/application.properties
cors.allowed-origins=http://localhost:3000,http://localhost:3001

# Verify frontend URL matches allowed origins
# Check browser developer tools for specific CORS errors
```

#### Runtime Issues

**⏳ Jobs Stuck in PROCESSING Status**
```bash
# Check Python service health
curl http://localhost:8000/health

# Verify model is loaded
curl http://localhost:8000/model/info

# Check system resources
top -p $(pgrep -f "python.*app.main")

# Restart Python service if needed
docker-compose restart python-service
```

**📁 File Upload Failures**
```bash
# Check file size (max 25MB by default)
ls -lh your-audio-file.mp3

# Verify file format is supported
file your-audio-file.mp3

# Check backend logs for specific error
docker-compose logs backend

# Test with smaller file or different format
```

**🚫 HTTP 413 (Payload Too Large) Errors**
```bash
# Increase backend file size limit
# File: application.properties
spring.servlet.multipart.max-file-size=50MB
spring.servlet.multipart.max-request-size=50MB

# Increase Python service limit
# File: .env
MAX_FILE_SIZE_MB=50

# If using nginx, increase client_max_body_size
client_max_body_size 50M;
```

#### Performance Issues

**🐌 Slow Transcription Processing**
```bash
# Use smaller/faster model
export MODEL_SIZE=base  # instead of large

# Check available memory
free -h

# Reduce concurrent transcriptions
export MAX_CONCURRENT_TRANSCRIPTIONS=1

# Monitor resource usage
docker stats
```

**💾 High Memory Usage**
```bash
# Monitor memory usage
docker stats whisperrr-python

# Check for memory leaks
curl http://localhost:8000/model/info

# Restart services if memory usage is excessive
docker-compose restart python-service

# Consider using smaller model
export MODEL_SIZE=tiny
```

### 🔍 Debugging Tools

#### Enable Debug Logging
```bash
# Backend (Spring Boot)
export LOGGING_LEVEL_COM_SHANGMIN_WHISPERRR=DEBUG
export SPRING_JPA_SHOW_SQL=true

# Python Service
export LOG_LEVEL=DEBUG

# Frontend
export REACT_APP_ENABLE_DEBUG=true
```

#### Health Check Commands
```bash
# Check all services
curl http://localhost:3000                    # Frontend
curl http://localhost:8080/actuator/health    # Backend
curl http://localhost:8000/health             # Python Service

# Database connectivity
pg_isready -h localhost -p 5432

# Docker container status
docker-compose ps
```

#### Log Analysis
```bash
# View service logs
docker-compose logs -f backend
docker-compose logs -f python-service
docker-compose logs -f frontend

# Follow specific service logs
docker logs -f whisperrr-backend

# Search logs for errors
docker-compose logs backend | grep ERROR
```

### 📞 Getting Help

#### Self-Service Resources
1. **📖 Documentation**
   - [OVERVIEW.md](OVERVIEW.md) - Comprehensive technical guide
   - Inline code documentation in all services
   - API documentation at http://localhost:8000/docs

2. **🔍 Search Existing Issues**
   - Check [GitHub Issues](../../issues) for similar problems
   - Search closed issues for solutions
   - Review troubleshooting discussions

#### Community Support
1. **🐛 Report Bugs**
   - Create detailed issue with reproduction steps
   - Include system information and logs
   - Provide minimal example if possible

2. **💬 Ask Questions**
   - Use [GitHub Discussions](../../discussions) for general questions
   - Include relevant configuration and error messages
   - Specify your environment (OS, versions, etc.)

3. **📧 Contact Information**
   - For security issues: Create private issue or email maintainers
   - For urgent production issues: Include "URGENT" in issue title

#### When Reporting Issues
Please include:
- **Environment**: OS, Python/Java/Node versions
- **Configuration**: Relevant config files (sanitized)
- **Steps to Reproduce**: Clear, minimal reproduction steps
- **Expected vs Actual**: What should happen vs what actually happens
- **Logs**: Relevant error messages and stack traces
- **Screenshots**: For UI-related issues

### 🚀 Performance Optimization Tips

#### System Requirements
- **Minimum**: 4GB RAM, 2 CPU cores
- **Recommended**: 8GB RAM, 4 CPU cores
- **Optimal**: 16GB RAM, 8 CPU cores (for large models)

#### Optimization Strategies
- Use appropriate Whisper model size for your accuracy needs
- Configure concurrent transcription limits based on available resources
- Monitor and tune JVM heap size for backend service
- Use SSD storage for better I/O performance
- Consider GPU acceleration for Python service (if available)

## 📚 Documentation & Resources

### 📖 Core Documentation
- **[OVERVIEW.md](OVERVIEW.md)** - Comprehensive technical architecture guide
- **[LICENSE](LICENSE)** - MIT License terms and conditions
- **API Documentation** - Interactive docs at http://localhost:8000/docs

### 🔧 Technical Resources
- **[Spring Boot Documentation](https://spring.io/projects/spring-boot)** - Backend framework
- **[FastAPI Documentation](https://fastapi.tiangolo.com/)** - Python service framework  
- **[React Documentation](https://react.dev/)** - Frontend framework
- **[OpenAI Whisper](https://github.com/openai/whisper)** - AI transcription models
- **[PostgreSQL Documentation](https://www.postgresql.org/docs/)** - Database system

### 🌟 Related Projects
- **[Whisper.cpp](https://github.com/ggerganov/whisper.cpp)** - C++ implementation of Whisper
- **[Faster Whisper](https://github.com/guillaumekln/faster-whisper)** - Optimized Whisper implementation
- **[Whisper JAX](https://github.com/sanchit-gandhi/whisper-jax)** - JAX implementation for TPUs

---

## 🎉 Acknowledgments

### 🙏 Special Thanks
- **OpenAI** for the incredible Whisper models
- **Spring Boot Team** for the excellent Java framework
- **FastAPI Team** for the modern Python web framework
- **React Team** for the powerful frontend library
- **PostgreSQL Community** for the robust database system

### 🏆 Contributors
- **shangmin** - Project creator and maintainer
- *Your name could be here!* - See [Contributing](#-contributing) section

### 📄 License
This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

### 🌟 Star History
If you find Whisperrr useful, please consider giving it a star on GitHub! ⭐

---

<div align="center">

**Built with ❤️ for the open source community**

[⬆️ Back to Top](#whisperrr---ai-powered-audio-transcription-platform)

</div>