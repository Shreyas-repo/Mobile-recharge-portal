# ==========================================
# Multi-Stage Dockerfile for Mobile Recharge Portal
# ==========================================

# Stage 1: Build stage
FROM eclipse-temurin:21-jdk-jammy AS builder
WORKDIR /build

# Copy Maven wrapper and dependencies
COPY pom.xml .
COPY src ./src

# Install Maven and build the application
RUN apt-get update && apt-get install -y maven && \
    mvn clean package -DskipTests

# Stage 2: Production runtime stage
FROM eclipse-temurin:21-jre-jammy
WORKDIR /app

# Create non-root system user for security
RUN groupadd -r devops && useradd -r -g devops appuser

# Copy jar from builder stage
COPY --from=builder /build/target/mobile-recharge-portal-1.0.0-SNAPSHOT.jar app.jar

# Ownership and permissions
RUN chown -R appuser:devops /app
USER appuser

# Expose standard application port
EXPOSE 8080

# Container health check
HEALTHCHECK --interval=20s --timeout=5s --start-period=30s --retries=3 \
  CMD curl -f http://localhost:8080/api/health || exit 1

ENTRYPOINT ["java", "-Djava.security.egd=file:/dev/./urandom", "-jar", "app.jar"]
