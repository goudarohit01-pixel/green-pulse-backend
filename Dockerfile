# Use Java 17
FROM eclipse-temurin:17-jdk-alpine

# Set working directory
WORKDIR /app

# Copy Maven files first (for caching)
COPY pom.xml .
COPY mvnw .
COPY .mvn .mvn

# Download dependencies
RUN ./mvnw dependency:go-offline -B

# Copy source code
COPY src ./src

# Build the application
RUN ./mvnw clean install -DskipTests

# Expose port
EXPOSE 8080

# Run the application
CMD ["java", "-jar", "target/green-pulse-backend-1.0.0.jar"]