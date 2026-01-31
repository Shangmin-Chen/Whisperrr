# Docker Deployment

> ⚠️ **Important Notice**: Docker is **not recommended** for running Whisperrr as it is **slower** and has **lower accuracy** compared to running the services natively. This documentation is provided for reference only if Docker deployment is absolutely necessary.

## Quick Start

```bash
docker compose up -d
```

Access services:
- Frontend: http://localhost:3737
- Backend API: http://localhost:7331
- Python Service: http://localhost:5001

## Critical Configuration

### Single Worker Requirement

The Python service **must** run with a single worker due to in-memory job state:

```yaml
# docker-compose.yml
services:
  python-service:
    environment:
      - UVICORN_WORKERS=1  # CRITICAL: Do not change without shared state backend
```

**Why**: In-memory job manager doesn't share state between workers. Multiple workers cause 404 errors during job polling.

**Future**: To use multiple workers, implement Redis or database for shared job state.

## Performance Considerations

Docker adds overhead, especially on macOS:
- 20-50% slower CPU performance
- 10-50x slower file I/O with volume mounts
- 2-5ms network latency between containers

**Recommendation**: Use native installation for development and production when possible.

## Health Checks

Services include health check dependencies to ensure proper startup order:

```yaml
depends_on:
  python-service:
    condition: service_healthy
```

## Troubleshooting

### 404 Errors During Polling
- Verify `UVICORN_WORKERS=1` in docker-compose.yml
- Check logs: `docker compose logs python-service`

### Connection Errors
- Ensure services start in order: python → backend → frontend
- Check health: `docker compose ps`

### Performance Issues
- Consider native installation instead
- Disable volume mounts for better I/O performance
- Allocate more resources to Docker Desktop

## See Also

- [Quick Start Guide](../getting-started/QUICK_START.md) - Native installation (recommended)
- [Configuration Guide](../guides/CONFIGURATION.md) - Environment configuration
