# Environment Setup Guide

This guide explains how to configure environment variables for Whisperrr when services are running on different hosts or ports (not localhost).

## Overview

Whisperrr consists of three services that need to communicate with each other:

- **Frontend** (React) - Port 3737 (default)
- **Backend** (Spring Boot) - Port 7331 (default)
- **Python Service** (FastAPI) - Port 5001 (default)

When services are not running on localhost, you need to configure environment variables so they can find each other.

## Quick Start: Interactive Setup Script

The easiest way to configure environment variables is using the interactive setup script:

```bash
# Run the setup script
./setup-env.sh

# Follow the prompts to enter:
# - Host IP address (or 'localhost' for local development)
# - Frontend port (default: 3737)
# - Backend port (default: 7331)
# - Python service port (default: 5001)
```

The script will:
- Prompt you for configuration values
- Validate your inputs
- Export environment variables to your current shell session
- Optionally save variables to `.env-export.sh` for reuse

### Using the Generated Export File

If you saved variables to a file during setup:

```bash
# Source the export file to load variables
source .env-export.sh
```

## Environment Variables

### Required Variables

#### 1. `REACT_APP_API_URL`
**Purpose:** Tells the frontend where to find the backend API.

**Format:** `http://HOST:PORT/api`

**Examples:**
```bash
# Localhost (default)
export REACT_APP_API_URL=http://localhost:7331/api

# Remote IP
export REACT_APP_API_URL=http://192.168.1.100:7331/api

# Custom port
export REACT_APP_API_URL=http://localhost:8080/api
```

**Used by:** Frontend (`frontend/src/utils/constants.ts`)

---

#### 2. `WHISPERRR_SERVICE_URL`
**Purpose:** Tells the backend where to find the Python transcription service.

**Format:** `http://HOST:PORT`

**Examples:**
```bash
# Localhost (default)
export WHISPERRR_SERVICE_URL=http://localhost:5001

# Remote IP
export WHISPERRR_SERVICE_URL=http://192.168.1.100:5001

# Custom port
export WHISPERRR_SERVICE_URL=http://localhost:9000
```

**Used by:** Backend (`backend/src/main/resources/application.properties`)

---

#### 3. `CORS_ALLOWED_ORIGINS`
**Purpose:** Configures which frontend URLs the backend will accept requests from (CORS).

**Format:** Comma-separated list of URLs

**Examples:**
```bash
# Single origin
export CORS_ALLOWED_ORIGINS=http://localhost:3737

# Multiple origins
export CORS_ALLOWED_ORIGINS=http://localhost:3737,http://localhost:3738

# Remote IP
export CORS_ALLOWED_ORIGINS=http://192.168.1.100:3737
```

**Used by:** Backend (`backend/src/main/resources/application.properties`)

---

#### 4. `CORS_ORIGINS`
**Purpose:** Configures which origins the Python service will accept requests from (CORS).

**Format:** Comma-separated list of URLs

**Examples:**
```bash
# Frontend and backend origins
export CORS_ORIGINS=http://localhost:3737,http://localhost:7331

# Remote IPs
export CORS_ORIGINS=http://192.168.1.100:3737,http://192.168.1.100:7331
```

**Used by:** Python Service (`python-service/app/config.py`)

## Manual Configuration

If you prefer to set variables manually instead of using the script:

### Example: Localhost (Default)

```bash
export REACT_APP_API_URL=http://localhost:7331/api
export WHISPERRR_SERVICE_URL=http://localhost:5001
export CORS_ALLOWED_ORIGINS=http://localhost:3737,http://localhost:3738
export CORS_ORIGINS=http://localhost:3737,http://localhost:7331
```

### Example: Remote IP Address

```bash
# Replace 192.168.1.100 with your actual IP
export REACT_APP_API_URL=http://192.168.1.100:7331/api
export WHISPERRR_SERVICE_URL=http://192.168.1.100:5001
export CORS_ALLOWED_ORIGINS=http://192.168.1.100:3737
export CORS_ORIGINS=http://192.168.1.100:3737,http://192.168.1.100:7331
```

### Example: Custom Ports

```bash
# Frontend on 3000, Backend on 8080, Python on 9000
export REACT_APP_API_URL=http://localhost:8080/api
export WHISPERRR_SERVICE_URL=http://localhost:9000
export CORS_ALLOWED_ORIGINS=http://localhost:3000
export CORS_ORIGINS=http://localhost:3000,http://localhost:8080
```

## Deployment Scenarios

### Local Development (localhost)

No environment variables needed - defaults work out of the box:

- Frontend: `http://localhost:3737`
- Backend: `http://localhost:7331`
- Python: `http://localhost:5001`

### Remote Development (Same Network)

When services run on different machines on the same network:

```bash
# On the machine running the services
export REACT_APP_API_URL=http://192.168.1.100:7331/api
export WHISPERRR_SERVICE_URL=http://192.168.1.100:5001
export CORS_ALLOWED_ORIGINS=http://192.168.1.100:3737
export CORS_ORIGINS=http://192.168.1.100:3737,http://192.168.1.100:7331
```

### Docker Deployment

Docker Compose handles service discovery automatically using service names. For external access, you may need to configure:

```bash
# If accessing from outside Docker network
export REACT_APP_API_URL=http://your-server-ip:7331/api
export WHISPERRR_SERVICE_URL=http://your-server-ip:5001
export CORS_ALLOWED_ORIGINS=http://your-server-ip:3737
export CORS_ORIGINS=http://your-server-ip:3737,http://your-server-ip:7331
```

### Production Deployment

For production, set environment variables in your deployment platform:

**Example for systemd service:**
```ini
[Service]
Environment="REACT_APP_API_URL=https://api.example.com/api"
Environment="WHISPERRR_SERVICE_URL=https://transcribe.example.com"
Environment="CORS_ALLOWED_ORIGINS=https://app.example.com"
Environment="CORS_ORIGINS=https://app.example.com,https://api.example.com"
```

## Service Startup Order

After setting environment variables, start services in this order:

1. **Python Service** (takes longest to start - model loading)
   ```bash
   cd python-service
   uvicorn app.main:app --host 0.0.0.0 --port 5001
   ```

2. **Backend Service**
   ```bash
   cd backend
   ./mvnw spring-boot:run
   ```

3. **Frontend Service**
   ```bash
   cd frontend
   npm start
   ```

## Verification

After starting all services, verify they can communicate:

### Check Python Service
```bash
curl http://localhost:5001/health
# or with remote IP
curl http://192.168.1.100:5001/health
```

### Check Backend Service
```bash
curl http://localhost:7331/api/audio/health
# or with remote IP
curl http://192.168.1.100:7331/api/audio/health
```

### Check Frontend
Open in browser:
- `http://localhost:3737` (or your configured host/port)

## Troubleshooting

### CORS Errors

**Symptom:** Browser console shows CORS errors like:
```
Access to fetch at 'http://...' from origin 'http://...' has been blocked by CORS policy
```

**Solution:**
1. Verify `CORS_ALLOWED_ORIGINS` includes your frontend URL exactly (including port)
2. Verify `CORS_ORIGINS` includes both frontend and backend URLs
3. Check that URLs match exactly (no trailing slashes, correct protocol)

**Example:**
```bash
# Frontend is at http://192.168.1.100:3737
export CORS_ALLOWED_ORIGINS=http://192.168.1.100:3737  # Must match exactly
export CORS_ORIGINS=http://192.168.1.100:3737,http://192.168.1.100:7331
```

### Connection Refused

**Symptom:** Services can't connect to each other

**Solution:**
1. Verify services are running on the expected ports
2. Check firewall settings if using remote IPs
3. Ensure environment variables are set in the same shell session where services are started
4. For remote IPs, ensure services bind to `0.0.0.0` not `127.0.0.1`

**Example:**
```bash
# Python service must bind to 0.0.0.0 for remote access
uvicorn app.main:app --host 0.0.0.0 --port 5001
```

### Environment Variables Not Applied

**Symptom:** Services still use default localhost URLs

**Solution:**
1. Ensure variables are exported before starting services
2. For frontend, variables must be set before `npm start` (React reads env vars at build/start time)
3. Restart services after changing environment variables
4. Check that variables are exported in the same terminal session

**Verification:**
```bash
# Check if variables are set
echo $REACT_APP_API_URL
echo $WHISPERRR_SERVICE_URL
echo $CORS_ALLOWED_ORIGINS
echo $CORS_ORIGINS
```

### Frontend Can't Find Backend

**Symptom:** Frontend shows network errors when trying to connect to backend

**Solution:**
1. Verify `REACT_APP_API_URL` is set correctly
2. Restart frontend after setting `REACT_APP_API_URL`
3. Check browser console for exact error message
4. Verify backend is running and accessible

**Debug:**
```bash
# Test backend directly
curl http://your-backend-url/api/audio/health

# Check frontend config
# In browser console, check what API URL is being used
```

## Best Practices

1. **Use the setup script** for initial configuration - it validates inputs and prevents errors
2. **Save export file** during setup for easy reuse: `source .env-export.sh`
3. **Set variables before starting services** - especially for frontend (React reads env vars at start)
4. **Use consistent host/port values** across all variables
5. **Document your configuration** - note which IPs/ports you're using
6. **Test connectivity** after configuration changes

## Additional Resources

- [Configuration Guide](CONFIGURATION.md) - Detailed configuration options
- [Quick Start Guide](../getting-started/QUICK_START.md) - Getting started with Whisperrr
- [Prerequisites Guide](../getting-started/PREREQUISITES.md) - Required software and versions
