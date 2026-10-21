# 🐳 Phase 11 & 12: Docker Containerization & Lifecycle Guide
## Mobile Recharge Portal (MRP)

---

### 1. Dockerfile Architecture
The project utilizes a production-grade multi-stage `Dockerfile`:
- **Stage 1 (Builder)**: Uses `eclipse-temurin:21-jdk-jammy`, compiles the source code, runs tests, and packages the JAR.
- **Stage 2 (Runtime)**: Uses minimal `eclipse-temurin:21-jre-jammy`, creates a dedicated non-root user `appuser` (UID/GID `devops`), and copies only the final `.jar` artifact to minimize image size and attack surface.
- **Health Check**: An integrated automated probe `curl -f http://localhost:8080/api/health` polls every 20 seconds.

---

### 2. Complete Docker Container Lifecycle Commands

#### A. Build and Tag the Image
```bash
docker build -t devops/mobile-recharge-portal:v1.0.0 -t devops/mobile-recharge-portal:latest .
```

#### B. Inspect Image Details & Size
```bash
docker images | grep mobile-recharge-portal
docker inspect devops/mobile-recharge-portal:v1.0.0
```

#### C. Run the Container with Port Mapping
Run container in detached mode mapping host port `8080` to container port `8080`:
```bash
docker run -d --name mobile-recharge-portal -p 8080:8080 devops/mobile-recharge-portal:v1.0.0
```

#### D. Inspect Container Logs & Status
```bash
# View live container logs
docker logs -f mobile-recharge-portal

# Inspect container runtime health status
docker ps -f name=mobile-recharge-portal
```

#### E. Verify Health via REST Endpoint
```bash
curl -i http://localhost:8080/api/health
```

#### F. Container Management Lifecycle (Stop, Restart, Remove)
```bash
# Stop the running container
docker stop mobile-recharge-portal

# Restart container
docker restart mobile-recharge-portal

# Stop and delete container
docker stop mobile-recharge-portal
docker rm mobile-recharge-portal

# Remove Docker image
docker rmi devops/mobile-recharge-portal:v1.0.0
```

---

### 3. Docker Compose Orchestration
You can also launch the complete stack with a single command using [`docker-compose.yml`](../docker-compose.yml):
```bash
# Start container in background
docker compose up -d

# Check service logs
docker compose logs -f

# Tear down service
docker compose down
```

---

### 4. Jenkins-Docker Continuous Deployment Pipeline (Phase 12)
In the project's [`Jenkinsfile`](../Jenkinsfile), the Docker CD stage is defined as:
```groovy
stage('Docker Image Build & Deploy') {
    steps {
        bat "docker build -t devops/mobile-recharge-portal:${BUILD_NUMBER} ."
        bat "docker stop mobile-recharge-portal || exit 0"
        bat "docker rm mobile-recharge-portal || exit 0"
        bat "docker run -d --name mobile-recharge-portal -p 8080:8080 devops/mobile-recharge-portal:${BUILD_NUMBER}"
    }
}
```
This guarantees an end-to-end commit-to-container pipeline where every passing build is automatically repackaged and launched as a freshly deployed container!
