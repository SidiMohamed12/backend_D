# syntax=docker/dockerfile:1.7

FROM gradle:8.10.2-jdk17 AS build
WORKDIR /app

# Copier uniquement les fichiers de config en premier pour cacher les dépendances
COPY build.gradle settings.gradle ./
RUN gradle dependencies --no-daemon || true

# Copier les sources et builder le JAR
COPY src ./src
RUN gradle clean bootJar --no-daemon

FROM eclipse-temurin:17-jre-alpine
WORKDIR /app

COPY --from=build /app/build/libs/*.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "/app/app.jar"]
