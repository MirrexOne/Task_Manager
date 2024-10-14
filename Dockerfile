FROM gradle:8.5.0-jdk21 AS build
WORKDIR /app
COPY /app .
RUN gradle clean build

FROM openjdk:21-jdk-slim-bullseye
COPY --from=build /app/build/libs/app-0.0.1-SNAPSHOT.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
