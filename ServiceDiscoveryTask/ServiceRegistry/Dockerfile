# syntax=docker/dockerfile:1

FROM eclipse-temurin:17-jdk-alpine

WORKDIR /app

COPY ServiceRegistry/build/libs/ServiceRegistry.jar ./ServiceRegistry.jar

EXPOSE 8083

CMD ["java", "-jar", "/app/ServiceRegistry.jar"]



