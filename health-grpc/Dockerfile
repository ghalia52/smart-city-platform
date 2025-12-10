# Build stage with protoc
FROM maven:3.9.6-eclipse-temurin-17 AS builder
WORKDIR /app

# Install protobuf compiler
RUN apt-get update && \
    apt-get install -y protobuf-compiler && \
    apt-get clean && \
    rm -rf /var/lib/apt/lists/*

# Copy project files
COPY pom.xml .
COPY src ./src

# Two-step build: download plugins, fix permissions, then build
RUN mvn dependency:resolve dependency:resolve-plugins && \
    mvn protobuf:compile protobuf:compile-custom -DskipTests 2>&1 | true && \
    find /app/target/protoc-plugins -type f -name "*.exe" -exec chmod +x {} \; && \
    mvn clean package -DskipTests

# Runtime stage
FROM eclipse-temurin:17-jre-alpine
WORKDIR /app

# Copy the built JAR (wildcard handles version changes)
COPY --from=builder /app/target/*.jar app.jar

# Install network tools for debugging
RUN apk add --no-cache curl

# Expose ports
EXPOSE 8083 9090

# Run the application
ENTRYPOINT ["java", "-jar", "app.jar"]