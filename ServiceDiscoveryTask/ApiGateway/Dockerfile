# syntax=docker/dockerfile:1

FROM eclipse-temurin:17-jdk-alpine

WORKDIR /app

COPY ApiGateway/build/libs/ApiGateway.jar ./ApiGateway.jar

EXPOSE 8080

CMD ["java", "-jar", "/app/ApiGateway.jar"]


