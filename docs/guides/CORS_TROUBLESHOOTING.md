# CORS Troubleshooting Guide

This guide helps you diagnose and fix Cross-Origin Resource Sharing (CORS) issues in Whisperrr.

## Table of Contents

- [Common CORS Issues](#common-cors-issues)
- [Cloudflare Tunnel Configuration](#cloudflare-tunnel-configuration)
- [Diagnosis Steps](#diagnosis-steps)
- [Solutions](#solutions)
- [Verification](#verification)

---

## Common CORS Issues

### Issue: CORS Timeout or Blocked Requests

**Symptoms:**
- Browser console shows: `Access to fetch at '...' from origin '...' has been blocked by CORS policy`
- Network tab shows OPTIONS preflight requests failing
- Direct `curl` requests work, but browser requests fail
- Error: `CORS timeout` or `CORS policy: No 'Access-Control-Allow-Origin' header`

**Common Causes:**
1. **Port mismatch** - URLs include ports when using Cloudflare Tunnel (ports should be omitted)
2. **Origin mismatch** - Backend CORS config doesn't match frontend origin exactly
3. **Missing environment variables** - CORS configuration not loaded from `.env` files
4. **Service not restarted** - Changes to `.env` files require service restart

---

## Cloudflare Tunnel Configuration

### Understanding Cloudflare Tunnel URLs

When using Cloudflare Tunnel or similar proxies (Tailscale, ngrok, etc.), the proxy handles SSL termination and routing. **Ports are NOT included in the public URL** because:

- HTTPS defaults to port 443
- The proxy routes traffic internally to your services
- Public URLs don't expose internal ports

**Correct URLs (Cloudflare Tunnel):**
```
Frontend:  https://whisperrr.shangmin.me          (no port)
Backend:   https://api-whisperrr.shangmin.me       (no port)
Python:    https://python-whisperrr.shangmin.me   (no port)
```

**Incorrect URLs (with ports):**
```
Frontend:  https://whisperrr.shangmin.me:3737     ❌ WRONG
Backend:   https://api-whisperrr.shangmin.me:7331 ❌ WRONG
Python:    https://python-whisperrr.shangmin.me:5001 ❌ WRONG
```

### Why Ports Cause CORS Failures

1. **Browser sends origin without port:**
   - Browser: `Origin: https://whisperrr.shangmin.me` (no port)

2. **Backend expects origin with port:**
   - Backend CORS config: `https://whisperrr.shangmin.me:3737` (with port)

3. **Mismatch = CORS rejection:**
   - Browser origin ≠ Backend allowed origin
   - Result: CORS policy blocks the request

---

## Diagnosis Steps

### Step 1: Check Environment Files

Verify `.env` files exist and have correct values:

```bash
# Check frontend .env
cat frontend/.env | grep REACT_APP_API_URL

# Check backend .env
cat backend/.env | grep CORS_ALLOWED_ORIGINS

# Check python-service .env
cat python-service/.env | grep CORS_ORIGINS
```

**Expected values (Cloudflare Tunnel):**
```bash
# frontend/.env
REACT_APP_API_URL=https://api-whisperrr.shangmin.me/api

# backend/.env
CORS_ALLOWED_ORIGINS=https://whisperrr.shangmin.me

# python-service/.env
CORS_ORIGINS=https://whisperrr.shangmin.me,https://api-whisperrr.shangmin.me
```

**⚠️ If you see ports (`:3737`, `:7331`, `:5001`), that's the problem!**

### Step 2: Check Browser Console

Open browser DevTools → Console and look for:

```
[API Config]   REACT_APP_API_URL: https://api-whisperrr.shangmin.me/api
```

If you see a port in the URL, the frontend is using the wrong configuration.

### Step 3: Check Network Tab

1. Open DevTools → Network tab
2. Make a request from the frontend
3. Look for the OPTIONS preflight request
4. Check response headers:

**✅ Correct CORS headers:**
```
Access-Control-Allow-Origin: https://whisperrr.shangmin.me
Access-Control-Allow-Credentials: true
Access-Control-Allow-Methods: GET,POST,PUT,DELETE,OPTIONS
```

**❌ Missing or incorrect headers:**
- No `Access-Control-Allow-Origin` header
- Origin doesn't match exactly (e.g., includes port when it shouldn't)

### Step 4: Test with curl

Test if backend accepts the origin:

```bash
# Test OPTIONS preflight
curl -X OPTIONS \
  -H "Origin: https://whisperrr.shangmin.me" \
  -H "Access-Control-Request-Method: POST" \
  -v \
  https://api-whisperrr.shangmin.me/api/audio/health

# Check response headers for Access-Control-Allow-Origin
```

---

## Solutions

### Solution 1: Fix Environment Files (Recommended)

Use the setup script with Cloudflare Tunnel option:

```bash
./setup-env.sh
# When prompted:
# 1. "Set up for remote deployment? (y/N)" → Answer "y"
# 2. "Are you using Cloudflare Tunnel or similar proxy? (y/N)" → Answer "y"
# 3. Enter your domains (without ports)
```

The script will automatically:
- Omit ports from URLs
- Configure CORS correctly
- Generate proper `.env` files

### Solution 2: Manual Fix

If you prefer to fix manually, edit `.env` files:

**`frontend/.env`:**
```properties
# Remove port from API URL
REACT_APP_API_URL=https://api-whisperrr.shangmin.me/api
```

**`backend/.env`:**
```properties
# Remove port from CORS origin
CORS_ALLOWED_ORIGINS=https://whisperrr.shangmin.me
```

**`python-service/.env`:**
```properties
# Remove ports from CORS origins
CORS_ORIGINS=https://whisperrr.shangmin.me,https://api-whisperrr.shangmin.me
```

### Solution 3: Use Wildcard (Development Only)

For development/testing, you can use a wildcard (⚠️ **NOT recommended for production**):

**`backend/.env`:**
```properties
CORS_ALLOWED_ORIGINS=*
```

**⚠️ Security Warning:** Wildcard allows ALL origins. Only use for development.

### Solution 4: Restart Services

After fixing `.env` files, **restart all services**:

```bash
# If using systemd:
sudo systemctl restart whisperrr-backend
sudo systemctl restart whisperrr-frontend
sudo systemctl restart whisperrr-python

# If using Docker:
docker compose restart backend frontend python-service

# If running manually:
# Stop and restart each service
```

**Important:** Services only read `.env` files at startup. Changes require a restart.

---

## Verification

### 1. Check Frontend Console

After restarting, check browser console:

```
[API Config] ============================================
[API Config] Environment Variable Debug Info:
[API Config]   REACT_APP_API_URL: https://api-whisperrr.shangmin.me/api
[API Config]   ✓ Using API URL from environment variable
[API Config]   Resolved API URL: https://api-whisperrr.shangmin.me/api
[API Config] ============================================
```

✅ **Success:** No ports in the URL

### 2. Check Network Tab

1. Make a request from the frontend
2. Check OPTIONS preflight response:
   - Status: `200 OK`
   - Headers include: `Access-Control-Allow-Origin: https://whisperrr.shangmin.me`

✅ **Success:** CORS headers present and correct

### 3. Test API Call

Try uploading a file or making an API call. It should work without CORS errors.

---

## Quick Reference

### Correct Configuration (Cloudflare Tunnel)

| Service | Environment Variable | Value |
|---------|---------------------|-------|
| Frontend | `REACT_APP_API_URL` | `https://api-whisperrr.shangmin.me/api` |
| Backend | `CORS_ALLOWED_ORIGINS` | `https://whisperrr.shangmin.me` |
| Python | `CORS_ORIGINS` | `https://whisperrr.shangmin.me,https://api-whisperrr.shangmin.me` |

### Common Mistakes

❌ **Including ports in URLs:**
```properties
REACT_APP_API_URL=https://api-whisperrr.shangmin.me:7331/api  # WRONG
CORS_ALLOWED_ORIGINS=https://whisperrr.shangmin.me:3737      # WRONG
```

✅ **Omitting ports (correct):**
```properties
REACT_APP_API_URL=https://api-whisperrr.shangmin.me/api      # CORRECT
CORS_ALLOWED_ORIGINS=https://whisperrr.shangmin.me            # CORRECT
```

---

## Additional Resources

- [Configuration Guide](./CONFIGURATION.md) - General configuration documentation
- [Quick Start Guide](../getting-started/QUICK_START.md) - Setup instructions
- [Spring Boot CORS Documentation](https://spring.io/guides/gs/rest-service-cors/) - Official Spring Boot CORS guide

---

## Still Having Issues?

1. **Double-check `.env` files** - Ensure no ports in URLs
2. **Restart services** - Changes require restart
3. **Check browser console** - Look for specific CORS error messages
4. **Test with curl** - Verify backend CORS configuration
5. **Check Cloudflare Tunnel config** - Ensure routing is correct

If problems persist, check:
- Backend logs for CORS-related errors
- Browser Network tab for failed requests
- Service startup logs for environment variable loading

