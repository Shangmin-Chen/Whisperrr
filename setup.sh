#!/bin/bash

# Whisperrr Docker Compose Setup Script
# This script helps you get started with Whisperrr using Docker Compose

set -e

echo "🚀 Whisperrr Docker Compose Setup"
echo "================================="

# Colors
GREEN='\033[0;32m'
BLUE='\033[0;34m'
YELLOW='\033[1;33m'
NC='\033[0m'

print_status() {
    echo -e "${BLUE}[INFO]${NC} $1"
}

print_success() {
    echo -e "${GREEN}[SUCCESS]${NC} $1"
}

print_warning() {
    echo -e "${YELLOW}[WARNING]${NC} $1"
}

# Check if we're in the right directory
if [ ! -f "README.md" ] || [ ! -d "backend" ] || [ ! -d "python-service" ] || [ ! -d "frontend" ]; then
    echo "❌ Please run this script from the Whisperrr root directory"
    exit 1
fi

# Check if Docker is installed
if ! command -v docker &> /dev/null; then
    echo "❌ Docker is not installed. Please install Docker first."
    echo "   Visit: https://docs.docker.com/get-docker/"
    exit 1
fi

# Check if Docker Compose v2 is available
if ! docker compose version &> /dev/null; then
    echo "❌ Docker Compose v2 is not available."
    echo "   Please ensure you have Docker Desktop or Docker Compose v2 installed."
    exit 1
fi

print_success "Docker and Docker Compose are available"

echo ""
print_status "Starting Whisperrr services with Docker Compose..."

# Start services
docker compose up -d --build

echo ""
print_success "🎉 Whisperrr is starting up!"
echo ""
echo "Services will be available at:"
echo "  • Frontend: http://localhost:3000"
echo "  • Backend API: http://localhost:8080"
echo "  • Python Service: http://localhost:8000"
echo ""
echo "Useful commands:"
echo "  • View logs: docker compose logs -f"
echo "  • Stop services: docker compose down"
echo "  • Restart services: docker compose restart"
echo "  • Check status: docker compose ps"
echo ""
