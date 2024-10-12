FROM gradle:8.5.0-jdk21
COPY /app .
RUN gradle bootJar
ARG JAR_FILE=app/build/libs/*.jar
COPY ${JAR_FILE} /app.jar
ENTRYPOINT ["java","-jar","/app.jar"]