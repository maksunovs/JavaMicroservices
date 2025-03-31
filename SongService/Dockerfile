FROM eclipse-temurin:17-jdk-alpine

WORKDIR /app

COPY build/libs/SongService.jar ./SongService.jar

EXPOSE 8180

CMD ["java", "-jar", "/app/SongService.jar"]
