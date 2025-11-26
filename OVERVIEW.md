# Whisperrr Platform Overview

## Table of Contents
1. [System Architecture](#system-architecture)
2. [Component Deep Dive](#component-deep-dive)
3. [Data Flow & Communication](#data-flow--communication)
4. [Technology Stack](#technology-stack)
5. [Development Workflow](#development-workflow)
6. [Deployment Architecture](#deployment-architecture)
7. [Understanding the Codebase](#understanding-the-codebase)
8. [Key Concepts](#key-concepts)
9. [Troubleshooting Guide](#troubleshooting-guide)

---

## System Architecture

Whisperrr is a modern, microservices-based audio transcription platform that leverages OpenAI's Whisper library for high-quality speech-to-text conversion. The system is designed with scalability, maintainability, and performance in mind.

### High-Level Architecture

```
┌─────────────────────────────────────────────────────────────────┐
│                        Whisperrr Platform                      │
├─────────────────────────────────────────────────────────────────┤
│                                                                 │
│  ┌─────────────────┐    ┌─────────────────┐    ┌─────────────┐ │
│  │                 │    │                 │    │             │ │
│  │  React Frontend │◄──►│ Spring Boot API │◄──►│   Python    │ │
│  │   (Port 3000)   │    │   (Port 8080)   │    │  Service    │ │
│  │                 │    │                 │    │ (Port 8000) │ │
│  └─────────────────┘    └─────────────────┘    └─────────────┘ │
│           │                       │                      │     │
│           │                       │                      │     │
│           ▼                       ▼                      ▼     │
│  ┌─────────────────┐    ┌─────────────────┐    ┌─────────────┐ │
│  │   Web Browser   │    │   PostgreSQL    │    │   Whisper   │ │
│  │    (Client)     │    │   Database      │    │   Models    │ │
│  └─────────────────┘    └─────────────────┘    └─────────────┘ │
│                                                                 │
└─────────────────────────────────────────────────────────────────┘
```

### Service Responsibilities

#### 1. React Frontend (Client Layer)
- **Purpose**: User interface and experience
- **Technology**: React 18 + TypeScript + Tailwind CSS
- **Responsibilities**:
  - File upload with drag-and-drop interface
  - Real-time status monitoring with polling
  - Results display with formatting and export options
  - Responsive design for mobile and desktop
  - Error handling and user feedback

#### 2. Spring Boot API (Business Logic Layer)
- **Purpose**: Business logic orchestration and data management
- **Technology**: Spring Boot 3 + Java 21 + PostgreSQL
- **Responsibilities**:
  - RESTful API endpoints for client communication
  - File upload validation and processing
  - Job management and status tracking
  - Database operations (CRUD for all entities)
  - Integration with Python transcription service
  - CORS configuration for frontend communication

#### 3. Python Service (Processing Layer)
- **Purpose**: AI-powered audio transcription
- **Technology**: FastAPI + Python 3.11 + OpenAI Whisper
- **Responsibilities**:
  - Whisper model management and caching
  - Audio file preprocessing and validation
  - High-quality speech-to-text transcription
  - Multiple model size support (tiny to large)
  - 99+ language support with automatic detection
  - Performance monitoring and resource management

#### 4. PostgreSQL Database (Data Layer)
- **Purpose**: Persistent data storage
- **Technology**: PostgreSQL 16 with Flyway migrations
- **Responsibilities**:
  - User account management
  - Audio file metadata storage
  - Job tracking and status management
  - Transcription results and metadata
  - Audit logging and performance metrics

---

## Component Deep Dive

### Frontend Architecture (React)

```
src/
├── components/           # Reusable UI components
│   ├── common/          # Shared components (Button, Loading, ErrorBoundary)
│   ├── upload/          # File upload components
│   ├── status/          # Status display components
│   └── results/         # Results display components
├── hooks/               # Custom React hooks
│   ├── useFileUpload.ts    # File upload state management
│   ├── useTranscription.ts # Transcription workflow management
│   └── usePolling.ts       # Real-time status polling
├── pages/               # Route-level page components
│   ├── HomePage.tsx        # File upload and initiation
│   ├── StatusPage.tsx      # Progress monitoring
│   └── ResultsPage.tsx     # Results display and export
├── services/            # API communication layer
│   ├── api.ts             # HTTP client configuration
│   └── transcription.ts   # Transcription API calls
├── types/               # TypeScript type definitions
├── utils/               # Helper functions and constants
└── styles/              # Global styles and themes
```

#### Key Frontend Patterns

1. **Custom Hooks Pattern**: Encapsulates complex state logic
   - `useTranscription`: Manages entire transcription workflow
   - `useFileUpload`: Handles file selection and validation
   - `usePolling`: Implements smart polling with backoff

2. **Service Layer Pattern**: Separates API logic from components
   - Centralized HTTP client configuration
   - Type-safe API method definitions
   - Error handling and retry logic

3. **Component Composition**: Builds complex UIs from simple components
   - Atomic design principles
   - Reusable component library
   - Consistent styling with Tailwind CSS

### Backend Architecture (Spring Boot)

```
src/main/java/com/shangmin/whisperrr/
├── config/              # Configuration classes
│   ├── CorsConfig.java     # Cross-origin resource sharing
│   └── JpaConfig.java      # Database configuration
├── controller/          # REST API endpoints
│   └── AudioController.java # Audio transcription endpoints
├── dto/                 # Data Transfer Objects
│   ├── AudioUploadResponse.java
│   ├── TranscriptionStatusResponse.java
│   └── TranscriptionResultResponse.java
├── entity/              # JPA database entities
│   ├── User.java           # User accounts
│   ├── AudioFile.java      # Audio file metadata
│   ├── Job.java            # Transcription jobs
│   └── Transcription.java  # Transcription results
├── enums/               # Enumeration types
│   ├── JobStatus.java      # Job lifecycle states
│   ├── AudioFormat.java    # Supported audio formats
│   └── UserRole.java       # User permission levels
├── exception/           # Error handling
│   ├── GlobalExceptionHandler.java
│   └── Custom exception classes
├── repository/          # Data access layer
│   ├── UserRepository.java
│   ├── AudioFileRepository.java
│   ├── JobRepository.java
│   └── TranscriptionRepository.java
└── service/             # Business logic layer
    ├── AudioService.java      # Interface definition
    └── impl/
        └── AudioServiceImpl.java # Implementation
```

#### Key Backend Patterns

1. **Layered Architecture**: Clear separation of concerns
   - Controller → Service → Repository → Entity
   - Dependency injection with Spring IoC
   - Interface-based design for testability

2. **Domain-Driven Design**: Rich domain models
   - Entities with business logic methods
   - Value objects for complex types
   - Repository pattern for data access

3. **Exception Handling**: Centralized error management
   - Global exception handler for consistent responses
   - Custom exception hierarchy
   - Proper HTTP status code mapping

### Python Service Architecture (FastAPI)

```
app/
├── main.py              # FastAPI application and endpoints
├── config.py            # Configuration management
├── models.py            # Pydantic data models
├── whisper_service.py   # Core transcription service
├── exceptions.py        # Custom exception classes
└── utils.py             # Utility functions
```

#### Key Python Patterns

1. **Singleton Pattern**: Efficient resource management
   - Single WhisperService instance
   - Model caching and reuse
   - Thread-safe operations

2. **Async/Await Pattern**: Non-blocking operations
   - Asynchronous request handling
   - ThreadPoolExecutor for CPU-intensive tasks
   - Proper resource cleanup

3. **Dependency Injection**: Configuration management
   - Pydantic settings with environment variables
   - Flexible configuration without code changes
   - Type validation and conversion

---

## Data Flow & Communication

### Complete Transcription Workflow

```
1. File Upload Flow
   ┌─────────────┐    POST /api/audio/upload    ┌─────────────┐
   │   React     │──────────────────────────────►│ Spring Boot │
   │  Frontend   │◄──────────────────────────────│     API     │
   └─────────────┘    AudioUploadResponse       └─────────────┘
                           (jobId)                       │
                                                         │ Store job
                                                         ▼
                                                ┌─────────────┐
                                                │ PostgreSQL  │
                                                │  Database   │
                                                └─────────────┘

2. Status Polling Flow
   ┌─────────────┐   GET /api/audio/status/{id}  ┌─────────────┐
   │   React     │──────────────────────────────►│ Spring Boot │
   │  Frontend   │◄──────────────────────────────│     API     │
   └─────────────┘  TranscriptionStatusResponse  └─────────────┘
        │                                                │
        │ Poll every 2s                                  │ Query status
        │ until complete                                 ▼
        │                                       ┌─────────────┐
        └───────────────────────────────────────│ PostgreSQL  │
                                                │  Database   │
                                                └─────────────┘

3. Transcription Processing Flow
   ┌─────────────┐    HTTP POST /transcribe     ┌─────────────┐
   │ Spring Boot │──────────────────────────────►│   Python    │
   │     API     │◄──────────────────────────────│   Service   │
   └─────────────┘   TranscriptionResponse      └─────────────┘
        │                                                │
        │ Store results                                  │ Process with
        ▼                                                │ Whisper
   ┌─────────────┐                                      ▼
   │ PostgreSQL  │                              ┌─────────────┐
   │  Database   │                              │   Whisper   │
   └─────────────┘                              │   Models    │
                                                └─────────────┘

4. Results Retrieval Flow
   ┌─────────────┐   GET /api/audio/result/{id}  ┌─────────────┐
   │   React     │──────────────────────────────►│ Spring Boot │
   │  Frontend   │◄──────────────────────────────│     API     │
   └─────────────┘  TranscriptionResultResponse  └─────────────┘
                                                         │
                                                         │ Fetch results
                                                         ▼
                                                ┌─────────────┐
                                                │ PostgreSQL  │
                                                │  Database   │
                                                └─────────────┘
```

### Database Schema Relationships

```sql
-- Core entity relationships
Users (1) ──── (N) AudioFiles
AudioFiles (1) ──── (N) Jobs  
Jobs (1) ──── (1) Transcriptions

-- Detailed schema
CREATE TABLE users (
    id BIGSERIAL PRIMARY KEY,
    username VARCHAR(50) UNIQUE NOT NULL,
    email VARCHAR(255) UNIQUE NOT NULL,
    role VARCHAR(20) NOT NULL,
    created_at TIMESTAMP DEFAULT NOW(),
    updated_at TIMESTAMP DEFAULT NOW()
);

CREATE TABLE audio_files (
    id BIGSERIAL PRIMARY KEY,
    filename VARCHAR(255) NOT NULL,
    original_filename VARCHAR(255) NOT NULL,
    file_size BIGINT NOT NULL,
    duration DOUBLE PRECISION,
    format VARCHAR(10) NOT NULL,
    s3_key VARCHAR(500),
    checksum VARCHAR(32),
    uploaded_by BIGINT REFERENCES users(id),
    created_at TIMESTAMP DEFAULT NOW(),
    updated_at TIMESTAMP DEFAULT NOW()
);

CREATE TABLE jobs (
    id BIGSERIAL PRIMARY KEY,
    job_id UUID UNIQUE NOT NULL,
    status VARCHAR(20) NOT NULL,
    priority INTEGER DEFAULT 0,
    started_at TIMESTAMP,
    completed_at TIMESTAMP,
    error_message TEXT,
    processing_time_ms BIGINT,
    model_used VARCHAR(50),
    requested_by BIGINT REFERENCES users(id),
    audio_file_id BIGINT REFERENCES audio_files(id),
    created_at TIMESTAMP DEFAULT NOW(),
    updated_at TIMESTAMP DEFAULT NOW()
);

CREATE TABLE transcriptions (
    id BIGSERIAL PRIMARY KEY,
    text TEXT NOT NULL,
    language VARCHAR(10),
    confidence DECIMAL(3,2),
    duration DOUBLE PRECISION,
    segments_json TEXT,
    job_id BIGINT REFERENCES jobs(id) UNIQUE,
    created_at TIMESTAMP DEFAULT NOW(),
    updated_at TIMESTAMP DEFAULT NOW()
);
```

---

## Technology Stack

### Frontend Stack
- **React 18**: Modern React with hooks and concurrent features
- **TypeScript**: Type safety and enhanced developer experience
- **Tailwind CSS**: Utility-first CSS framework for rapid styling
- **React Router**: Client-side routing for SPA navigation
- **React Query**: Server state management with caching and synchronization
- **React Dropzone**: File upload with drag-and-drop functionality
- **Lucide React**: Modern icon library with consistent design
- **Axios**: HTTP client for API communication

### Backend Stack
- **Spring Boot 3**: Modern Java framework with auto-configuration
- **Java 21**: Latest LTS version with modern language features
- **Spring Data JPA**: Object-relational mapping and data access
- **PostgreSQL**: Robust relational database with JSON support
- **Flyway**: Database migration and version control
- **Spring Boot Actuator**: Production monitoring and health checks
- **Maven**: Dependency management and build automation

### Python Service Stack
- **FastAPI**: Modern, fast web framework for building APIs
- **Python 3.11**: Latest stable Python with performance improvements
- **OpenAI Whisper**: State-of-the-art speech recognition models
- **Pydantic**: Data validation using Python type annotations
- **Uvicorn**: Lightning-fast ASGI server implementation
- **PyTorch**: Deep learning framework (Whisper dependency)

### Infrastructure & DevOps
- **Docker**: Containerization for consistent deployments
- **Docker Compose**: Multi-container application orchestration
- **PostgreSQL 16**: Database with advanced features and performance
- **Nginx**: Reverse proxy and static file serving (production)

---

## Development Workflow

### Getting Started (Step-by-Step)

#### 1. Environment Setup
```bash
# Clone the repository
git clone <repository-url>
cd Whisperrr

# Verify prerequisites
java --version    # Should be 21+
node --version    # Should be 18+
python --version  # Should be 3.11+
docker --version  # For containerized setup
```

#### 2. Database Setup
```bash
# Install PostgreSQL (if not using Docker)
# macOS
brew install postgresql
brew services start postgresql

# Create database
createdb transcription_db

# Create user (optional)
psql -c "CREATE USER transcription_user WITH PASSWORD 'transcription_pass';"
psql -c "GRANT ALL PRIVILEGES ON DATABASE transcription_db TO transcription_user;"
```

#### 3. Backend Setup (Spring Boot)
```bash
cd backend

# Configure database connection
# Edit src/main/resources/application.properties
spring.datasource.url=jdbc:postgresql://localhost:5432/transcription_db
spring.datasource.username=transcription_user
spring.datasource.password=transcription_pass

# Build and run
./mvnw spring-boot:run

# Verify: http://localhost:8080/actuator/health
```

#### 4. Python Service Setup
```bash
cd python-service

# Create virtual environment
python -m venv venv
source venv/bin/activate  # Windows: venv\Scripts\activate

# Install dependencies
pip install -r requirements.txt

# Configure environment (copy env.example to .env)
cp env.example .env
# Edit .env as needed

# Run service
python -m app.main

# Verify: http://localhost:8000/health
```

#### 5. Frontend Setup
```bash
cd frontend

# Install dependencies
npm install

# Configure environment (copy env.example to .env)
cp env.example .env
# Edit .env as needed

# Start development server
npm start

# Verify: http://localhost:3000
```

### Development Best Practices

#### Code Organization
1. **Follow established patterns** in each service
2. **Use consistent naming conventions**
3. **Write comprehensive documentation** for all public methods
4. **Implement proper error handling** at all layers
5. **Add logging** for debugging and monitoring

#### Testing Strategy
```bash
# Backend tests
cd backend
./mvnw test

# Frontend tests
cd frontend
npm test

# Python service tests (when implemented)
cd python-service
python -m pytest
```

#### Git Workflow
1. **Feature branches** for new development
2. **Descriptive commit messages** with context
3. **Pull requests** for code review
4. **Automated testing** before merge

---

## Deployment Architecture

### Development Environment
```
┌─────────────────────────────────────────────────────────────────┐
│                     Development Setup                          │
├─────────────────────────────────────────────────────────────────┤
│                                                                 │
│  ┌─────────────────┐    ┌─────────────────┐    ┌─────────────┐ │
│  │ npm start       │    │ ./mvnw spring-  │    │ python -m   │ │
│  │ localhost:3000  │    │ boot:run        │    │ app.main    │ │
│  │                 │    │ localhost:8080  │    │ localhost:  │ │
│  │ Hot reload      │    │                 │    │ 8000        │ │
│  │ enabled         │    │ Auto restart    │    │             │ │
│  └─────────────────┘    └─────────────────┘    └─────────────┘ │
│                                                                 │
│                    ┌─────────────────┐                         │
│                    │ PostgreSQL      │                         │
│                    │ localhost:5432  │                         │
│                    │                 │                         │
│                    │ Local instance  │                         │
│                    └─────────────────┘                         │
└─────────────────────────────────────────────────────────────────┘
```

### Docker Compose Setup
```bash
# Start all services with Docker
docker-compose up --build

# Services will be available at:
# Frontend: http://localhost:3000
# Backend API: http://localhost:8080
# Python Service: http://localhost:8000
# PostgreSQL: localhost:5432
```

### Production Deployment Options

#### Option 1: Cloud Native (Recommended)
```
┌─────────────────────────────────────────────────────────────────┐
│                    Production Architecture                      │
├─────────────────────────────────────────────────────────────────┤
│                                                                 │
│  ┌─────────────────┐    ┌─────────────────┐    ┌─────────────┐ │
│  │ Frontend        │    │ Backend API     │    │ Python      │ │
│  │ (Vercel/        │    │ (AWS ECS/       │    │ Service     │ │
│  │  Netlify)       │    │  Google Cloud   │    │ (AWS Lambda/│ │
│  │                 │    │  Run)           │    │  Cloud Run) │ │
│  └─────────────────┘    └─────────────────┘    └─────────────┘ │
│                                                                 │
│                    ┌─────────────────┐                         │
│                    │ Managed         │                         │
│                    │ PostgreSQL      │                         │
│                    │ (AWS RDS/       │                         │
│                    │  Google Cloud   │                         │
│                    │  SQL)           │                         │
│                    └─────────────────┘                         │
└─────────────────────────────────────────────────────────────────┘
```

#### Option 2: Container Orchestration
```
┌─────────────────────────────────────────────────────────────────┐
│                 Kubernetes/Docker Swarm                        │
├─────────────────────────────────────────────────────────────────┤
│                                                                 │
│  ┌─────────────────┐    ┌─────────────────┐    ┌─────────────┐ │
│  │ Frontend Pod    │    │ Backend Pod     │    │ Python Pod  │ │
│  │ (Nginx +        │    │ (Spring Boot    │    │ (FastAPI +  │ │
│  │  React build)   │    │  JAR)           │    │  Whisper)   │ │
│  │                 │    │                 │    │             │ │
│  │ Replicas: 2-3   │    │ Replicas: 2-5   │    │ Replicas: 1-3│ │
│  └─────────────────┘    └─────────────────┘    └─────────────┘ │
│                                                                 │
│                    ┌─────────────────┐                         │
│                    │ PostgreSQL      │                         │
│                    │ StatefulSet     │                         │
│                    │                 │                         │
│                    │ Persistent      │                         │
│                    │ Volume          │                         │
│                    └─────────────────┘                         │
└─────────────────────────────────────────────────────────────────┘
```

---

## Understanding the Codebase

### Learning Path for New Developers

#### Phase 1: Foundation Understanding (Week 1)
1. **Start with the README.md** - Get familiar with setup and basic concepts
2. **Run the application locally** - Follow the development setup guide
3. **Explore the API endpoints** - Use the interactive docs at http://localhost:8000/docs
4. **Understand the data flow** - Trace a complete transcription workflow

#### Phase 2: Frontend Deep Dive (Week 2)
1. **Study the React components** - Start with `App.tsx` and follow the routing
2. **Understand state management** - Focus on custom hooks like `useTranscription`
3. **Explore the service layer** - See how API calls are structured
4. **Learn the styling approach** - Understand Tailwind CSS usage patterns

#### Phase 3: Backend Deep Dive (Week 3)
1. **Study the Spring Boot structure** - Understand the layered architecture
2. **Explore the entities and relationships** - See how data is modeled
3. **Understand the service layer** - Focus on business logic implementation
4. **Learn about error handling** - Study the exception handling patterns

#### Phase 4: Python Service Deep Dive (Week 4)
1. **Understand FastAPI structure** - See how endpoints are organized
2. **Study the WhisperService** - Learn about model management and transcription
3. **Explore configuration management** - Understand environment-based config
4. **Learn about async patterns** - See how concurrent processing works

### Key Files to Study First

#### Frontend Entry Points
1. `src/App.tsx` - Main application component and routing
2. `src/hooks/useTranscription.ts` - Core workflow management
3. `src/services/transcription.ts` - API communication layer
4. `src/components/upload/FileUpload.tsx` - File upload interface

#### Backend Entry Points
1. `WhisperrrApiApplication.java` - Application entry point
2. `AudioController.java` - REST API endpoints
3. `AudioServiceImpl.java` - Business logic implementation
4. `Job.java` - Core domain entity

#### Python Service Entry Points
1. `app/main.py` - FastAPI application and endpoints
2. `app/whisper_service.py` - Core transcription service
3. `app/config.py` - Configuration management
4. `app/models.py` - Data models and validation

### Common Development Tasks

#### Adding a New API Endpoint
1. **Define the endpoint** in `AudioController.java`
2. **Add business logic** in `AudioService` interface and implementation
3. **Create DTOs** for request/response if needed
4. **Add frontend service method** in `transcription.ts`
5. **Update React components** to use the new endpoint

#### Adding a New Database Entity
1. **Create JPA entity** in `entity/` package
2. **Create repository interface** in `repository/` package
3. **Add Flyway migration** in `src/main/resources/db/migration/`
4. **Update service layer** to use new entity
5. **Create DTOs** for API communication

#### Adding New Whisper Model Support
1. **Update configuration** in `config.py`
2. **Modify WhisperService** to handle new model
3. **Update validation** in Pydantic models
4. **Test model loading** and performance
5. **Update documentation** and API specs

---

## Key Concepts

### Job Lifecycle Management

The transcription workflow is built around a job-based system that tracks the complete lifecycle of each transcription request:

```
Job States:
PENDING → PROCESSING → COMPLETED
    ↓         ↓           ↑
    ↓    FAILED ←────────┘
    ↓         ↓
    └→ CANCELLED ←───────┘

State Transitions:
1. PENDING: Job created, waiting for processing
2. PROCESSING: Whisper model actively transcribing
3. COMPLETED: Transcription finished successfully
4. FAILED: Error occurred during processing
5. CANCELLED: Job cancelled by user or system
```

### Error Handling Strategy

The platform implements comprehensive error handling at every layer:

#### Frontend Error Handling
- **Network errors**: Automatic retry with exponential backoff
- **Validation errors**: Real-time feedback with specific messages
- **Service errors**: Graceful degradation with user-friendly messages
- **Unexpected errors**: Error boundaries prevent app crashes

#### Backend Error Handling
- **Global exception handler**: Consistent error response format
- **Custom exceptions**: Specific error types for different scenarios
- **Validation errors**: Bean validation with detailed messages
- **Database errors**: Transaction rollback and recovery

#### Python Service Error Handling
- **Custom exception hierarchy**: Specific error types for different failures
- **Resource cleanup**: Automatic cleanup on errors
- **Retry logic**: Automatic retry for transient failures
- **Graceful degradation**: Service continues operating despite errors

### Performance Optimization

#### Frontend Optimizations
- **React Query caching**: Reduces API calls and improves responsiveness
- **Component memoization**: Prevents unnecessary re-renders
- **Code splitting**: Lazy loading of routes and components
- **Asset optimization**: Minification and compression

#### Backend Optimizations
- **Connection pooling**: Efficient database connection management
- **JPA optimization**: Proper fetch strategies and query optimization
- **Caching**: Strategic caching of frequently accessed data
- **Async processing**: Non-blocking operations where appropriate

#### Python Service Optimizations
- **Model caching**: Avoid repeated model loading
- **ThreadPoolExecutor**: Concurrent processing of multiple requests
- **Memory management**: Efficient cleanup and garbage collection
- **Audio preprocessing**: Optimized audio format conversion

### Security Considerations

#### Data Protection
- **Input validation**: Comprehensive validation at all entry points
- **File type validation**: Strict checking of uploaded file types
- **Size limits**: Protection against large file attacks
- **SQL injection prevention**: Parameterized queries and JPA

#### CORS Configuration
- **Allowed origins**: Specific whitelist of trusted domains
- **Method restrictions**: Limited to necessary HTTP methods
- **Header controls**: Restricted set of allowed headers
- **Credential handling**: Configurable credential support

#### Error Information
- **Sanitized errors**: No sensitive information in error messages
- **Logging**: Comprehensive logging for security monitoring
- **Rate limiting**: Protection against abuse (can be added)

---

## Troubleshooting Guide

### Common Issues and Solutions

#### Development Setup Issues

**Issue**: Database connection failed
```
Solution:
1. Verify PostgreSQL is running: `brew services list | grep postgresql`
2. Check database exists: `psql -l | grep transcription_db`
3. Verify credentials in application.properties
4. Check firewall/network connectivity
```

**Issue**: Python service fails to start
```
Solution:
1. Verify Python version: `python --version` (should be 3.11+)
2. Check virtual environment: `which python` (should point to venv)
3. Install dependencies: `pip install -r requirements.txt`
4. Check for conflicting processes on port 8000
```

**Issue**: Frontend build fails
```
Solution:
1. Clear node_modules: `rm -rf node_modules package-lock.json`
2. Reinstall dependencies: `npm install`
3. Check Node.js version: `node --version` (should be 18+)
4. Verify environment variables in .env file
```

#### Runtime Issues

**Issue**: CORS errors in browser
```
Solution:
1. Check backend CORS configuration in CorsConfig.java
2. Verify frontend URL is in allowed origins
3. Ensure both services are running on correct ports
4. Check browser developer tools for specific CORS error
```

**Issue**: Transcription jobs stuck in PROCESSING
```
Solution:
1. Check Python service logs for errors
2. Verify Whisper model is loaded: GET /model/info
3. Check available memory and CPU resources
4. Restart Python service if needed
```

**Issue**: File upload fails with 413 error
```
Solution:
1. Check file size limit in backend configuration
2. Verify Python service MAX_FILE_SIZE_MB setting
3. Check web server (nginx) client_max_body_size
4. Ensure file is valid audio format
```

#### Performance Issues

**Issue**: Slow transcription processing
```
Solution:
1. Use smaller Whisper model (base instead of large)
2. Check available system memory
3. Reduce MAX_CONCURRENT_TRANSCRIPTIONS
4. Consider GPU acceleration if available
```

**Issue**: High memory usage
```
Solution:
1. Monitor model loading and cleanup
2. Check for memory leaks in long-running processes
3. Adjust JVM heap size for backend
4. Enable garbage collection logging
```

### Debugging Tips

#### Frontend Debugging
```javascript
// Enable React Query devtools
import { ReactQueryDevtools } from '@tanstack/react-query-devtools'

// Add to App component
<ReactQueryDevtools initialIsOpen={false} />

// Check network requests in browser dev tools
// Monitor React component re-renders
// Use React Developer Tools extension
```

#### Backend Debugging
```properties
# Enable debug logging
logging.level.com.shangmin.whisperrr=DEBUG
logging.level.org.springframework.web=DEBUG

# Enable SQL logging
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.format_sql=true

# Enable actuator endpoints
management.endpoints.web.exposure.include=health,info,metrics,loggers
```

#### Python Service Debugging
```python
# Enable debug logging
LOG_LEVEL=DEBUG

# Monitor memory usage
import psutil
print(f"Memory usage: {psutil.virtual_memory().percent}%")

# Profile performance
import cProfile
cProfile.run('your_function()')
```

### Monitoring and Observability

#### Health Check Endpoints
- Frontend: Application runs on http://localhost:3000
- Backend: http://localhost:8080/actuator/health
- Python Service: http://localhost:8000/health
- Database: `pg_isready -h localhost -p 5432`

#### Log Locations
- Backend: Console output or configured log file
- Python Service: Console output with structured logging
- Frontend: Browser developer tools console
- Database: PostgreSQL log directory

#### Metrics and Monitoring
- Backend: Spring Boot Actuator metrics at `/actuator/metrics`
- Python Service: Custom metrics in logs and responses
- Frontend: React Query devtools for cache and network state
- Database: PostgreSQL statistics and query performance

---

This overview provides a comprehensive understanding of the Whisperrr platform. For specific implementation details, refer to the inline documentation in each service's codebase. The platform is designed to be maintainable, scalable, and easy to understand for developers at all levels.
