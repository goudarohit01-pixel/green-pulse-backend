# ── Stage 1: Build ──────────────────────────────────────────
FROM maven:3.9.6-eclipse-temurin-17 AS build

WORKDIR /app

# Copy pom.xml and download dependencies first (cached layer)
COPY pom.xml .
RUN mvn dependency:resolve -B

# Copy source and build
COPY src ./src
RUN mvn clean package -DskipTests -q

# ── Stage 2: Run ────────────────────────────────────────────
FROM eclipse-temurin:17-jre-alpine

WORKDIR /app

# Copy jar
COPY --from=build /app/target/*.jar app.jar

EXPOSE 8080

# Optimized JVM flags for faster startup and less memory
ENTRYPOINT ["sh", "-c", "java \
  -XX:+UseSerialGC \
  -XX:MaxRAM=512m \
  -XX:TieredStopAtLevel=1 \
  -Djava.security.egd=file:/dev/./urandom \
  -jar app.jar \
  --server.port=${PORT:-8080}"]