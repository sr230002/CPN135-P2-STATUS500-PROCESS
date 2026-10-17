# API process (STATUS500 - Parcial #2) -- contenedor para Azure.
# Build: docker build -t status500-process-api:latest .
# Run:   docker run -d --name status500-process -p 7777:7777 status500-process-api:latest
# Logs:  docker logs -f status500-process

# ---- stage 1: build ----
FROM maven:3.9-eclipse-temurin-21 AS build
WORKDIR /app
COPY pom.xml ./
COPY src ./src
RUN mvn -B -q -DskipTests package

# ---- stage 2: runtime ----
FROM eclipse-temurin:21-jre
WORKDIR /app
COPY --from=build /app/target/process.jar /app.jar
EXPOSE 7777
ENV SERVER_PORT=7777
ENV SPRING_OUTPUT_ANSI_ENABLED=never
ENTRYPOINT ["java", "-jar", "/app.jar"]
