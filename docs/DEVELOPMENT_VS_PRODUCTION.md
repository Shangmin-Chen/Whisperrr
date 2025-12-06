# Development vs Production: API Communication Explained

## Overview

This document explains how your Whisperrr frontend communicates with the backend API in development vs production environments, and why different access methods (direct IP vs domain) behave differently.

---

## Development Mode (`npm start`)

### How It Works

**Command:** `npm start` → runs `react-scripts start`

**What you had:**
- `package.json` had `"proxy": "http://localhost:7331"`
- This creates a **server-side proxy** in the webpack dev server
- When your React code calls `/api/something`, the dev server intercepts it and proxies to `localhost:7331/api/something`

**How the proxy works:**
1. React dev server runs on port 3737
2. Dev server has **server-side proxy middleware** configured via `"proxy"` field
3. Browser makes request to `/api/audio/transcribe` (relative path)
4. Dev server intercepts the request (server-side)
5. Dev server makes request to `http://localhost:7331/api/audio/transcribe` (on your server)
6. Dev server returns response to browser

### Why It Worked via IP but Not Domain

**Accessing via `100.76.98.121:3737`:**
- Dev server proxy runs on your server → proxies to `localhost:7331` on your server ✅
- Works because proxy is server-side, regardless of how user accesses the frontend

**Accessing via `whisperrr.shangmin.me:3737`:**
- Dev server proxy still runs on your server → proxies to `localhost:7331` on your server ✅
- **Should work...** but it didn't because:

### The Real Problem: Hardcoded Absolute URLs

**What actually happened:**
- Your React code used **absolute URLs** like `http://localhost:7331/api` instead of relative paths like `/api/...`
- When you set `baseURL: 'http://localhost:7331/api'` in axios, it bypasses the proxy
- Browser tries to call `http://localhost:7331/api` directly → fails because `localhost` refers to user's machine, not your server

**Why relative paths would work:**
- If you used `baseURL: '/api'` or just relative paths
- Browser calls `/api/audio/transcribe` (relative to current origin)
- Dev server proxy intercepts it (server-side)
- Proxy forwards to `localhost:7331` on your server ✅
- Works from any access method (IP, domain, etc.)

**Current code issue:**
```typescript
// frontend/src/utils/constants.ts
const getApiUrl = (): string => {
  if (process.env.REACT_APP_API_URL) {
    return process.env.REACT_APP_API_URL;  // e.g., "http://localhost:7331/api"
  }
  return 'http://localhost:7331/api';  // Absolute URL - bypasses proxy!
};

// frontend/src/services/api.ts
const apiClient = axios.create({
  baseURL: API_CONFIG.BASE_URL,  // Absolute URL like "http://localhost:7331/api"
});
```

**What should happen with proxy:**
```typescript
// If using proxy, should use relative paths:
const apiClient = axios.create({
  baseURL: '/api',  // Relative path - uses proxy!
});
```

---

## Production Mode (`npx serve -s build -l 3737`)

### How It Works

**What changed:**
- No proxy middleware (just static file serving)
- Your React app now has **absolute URLs** to `http://100.76.98.121:7331/api` hardcoded in the build
- Browser makes direct requests to the backend IP
- Works from any access method because it's not relying on proxy or relative paths

**Build Process:**
1. Run `npm run build` with `REACT_APP_API_URL=http://100.76.98.121:7331/api`
2. Webpack reads env var and bakes it into the JavaScript bundle
3. All API calls use absolute URL: `http://100.76.98.121:7331/api`
4. Static files served with `npx serve -s build -l 3737` or nginx
5. Browser makes direct requests to backend IP ✅

**Starting Production Frontend:**
```bash
cd frontend
npm run build
npx serve -s build -l 3737
```

**Why it works:**
- Absolute URL points directly to your server's IP
- No proxy needed - browser calls backend directly
- Works from any access method (IP, domain) because URL is hardcoded to IP
- `-s` flag enables single-page app routing (handles client-side routing)
- `-l 3737` specifies the port to listen on (default frontend port)

---

## Mixed Content Issue

### The Problem

Your site loads via HTTPS (`https://whisperrr.shangmin.me`) but backend is HTTP (`http://100.76.98.121:7331`).

**What happens:**
- Browser blocks mixed content (HTTPS page calling HTTP API)
- Or browser shows security warnings
- API calls may fail or be blocked

### Solution: Cloudflare Tunnel Proxy

Add backend to Cloudflare tunnel config to proxy API requests through HTTPS:

```yaml
ingress:
  - hostname: whisperrr.shangmin.me
    path: ^/api(/.*)?$
    service: http://100.76.98.121:7331
  - hostname: whisperrr.shangmin.me
    service: http://100.76.98.121:3737
  - service: http_status:404
```

**Then update your frontend to use relative paths or HTTPS absolute URLs:**

**Option 1: Relative paths (recommended)**
```typescript
// frontend/src/utils/constants.ts
const getApiUrl = (): string => {
  // In production with Cloudflare tunnel, use relative path
  if (process.env.NODE_ENV === 'production') {
    return '/api';  // Relative path - uses Cloudflare tunnel proxy
  }
  // In development, use proxy or absolute URL
  if (process.env.REACT_APP_API_URL) {
    return process.env.REACT_APP_API_URL;
  }
  return 'http://localhost:7331/api';
};
```

**Option 2: HTTPS absolute URL**
```typescript
// frontend/src/utils/constants.ts
const getApiUrl = (): string => {
  if (process.env.REACT_APP_API_URL) {
    return process.env.REACT_APP_API_URL;
  }
  // In production, use HTTPS through Cloudflare tunnel
  if (process.env.NODE_ENV === 'production') {
    return 'https://whisperrr.shangmin.me/api';
  }
  return 'http://localhost:7331/api';
};
```

**Benefits:**
- All traffic goes through HTTPS (no mixed content)
- Works from any access method
- Cloudflare handles SSL termination
- Backend can still be HTTP internally

---

## Summary

| Aspect | Development (`npm start`) | Production (`npx serve -s build`) |
|--------|---------------------------|-----------------------------------|
| **Proxy** | ✅ Yes (via `package.json` `"proxy"` field) | ❌ No (static file serving) |
| **API URL Type** | Absolute URLs (bypasses proxy) | Absolute URLs (hardcoded in build) |
| **Why IP works** | Proxy runs server-side | Absolute URL points to IP |
| **Why domain fails** | Absolute URLs use `localhost` (user's machine) | Works if URL points to IP/domain |
| **Solution** | Use relative paths (`/api`) to use proxy | Use absolute URLs to IP or HTTPS domain |

**Key Takeaways:**

1. **Development proxy works** - but only if you use **relative paths** (`/api/...`), not absolute URLs (`http://localhost:7331/api`)
2. **Absolute URLs bypass the proxy** - browser makes direct requests, so `localhost` refers to user's machine
3. **Production uses absolute URLs** - hardcoded at build time, works from any access method
4. **Mixed content issue** - HTTPS frontend calling HTTP backend can be solved with Cloudflare tunnel proxy

**Recommended Fix:**

For development, either:
- Use relative paths (`baseURL: '/api'`) to leverage the proxy
- Or ensure `REACT_APP_API_URL` points to accessible IP/domain (not `localhost`)

For production:
- Build with `REACT_APP_API_URL` pointing to IP or HTTPS domain
- Or use Cloudflare tunnel to proxy API requests through HTTPS
