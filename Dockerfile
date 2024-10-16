# Этап 1: Сборка приложения
FROM gradle:8.5-jdk21 AS build

WORKDIR /app

# Копируем файлы сборки
COPY build.gradle.kts settings.gradle.kts ./
COPY config ./config

# Загружаем зависимости
RUN gradle dependencies --no-daemon

# Копируем исходный код
COPY src ./src

# Собираем приложение, пропуская тесты и проверку Checkstyle
RUN gradle build -x test -x checkstyleMain -x checkstyleTest --no-daemon

# Этап 2: Создание финального образа
FROM eclipse-temurin:21-jre-alpine

WORKDIR /app

# Копируем собранный jar из первого этапа
COPY --from=build /app/build/libs/*.jar app.jar

# Открываем порт, который будет использовать приложение
EXPOSE 8080

# Указываем точку входа для запуска приложения
CMD ["java", "-XX:+UnlockExperimentalVMOptions", "-XX:+UseContainerSupport", "-jar", "app.jar"]