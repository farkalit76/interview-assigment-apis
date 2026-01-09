# ---------- STAGE 1: Build ----------
FROM maven:3.9.9-eclipse-temurin-17 AS builder

WORKDIR /app

# Cache dependencies
COPY pom.xml .
RUN mvn dependency:go-offline -B

# Copy source
COPY src ./src

# Build fat jar
RUN mvn clean package -DskipTests

# Stage - 2:  run the application
FROM eclipse-temurin:17-jre-alpine

WORKDIR /app

COPY --from=builder /app/target/*.jar app.jar

# Expose app port
EXPOSE 8080

# JVM optimizations for containers
ENTRYPOINT ["java", "-XX:+UseContainerSupport", "-jar", "app.jar"]
