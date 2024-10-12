FROM openjdk:21-jdk-slim-bullseye
ARG JAR_FILE=app/build/libs/*.jar
COPY ${JAR_FILE} app.jar
ENTRYPOINT ["java","-jar","/app.jar"]