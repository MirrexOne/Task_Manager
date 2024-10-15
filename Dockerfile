# Этап 1: Сборка приложения
FROM gradle:8.5-jdk21 AS build

# Устанавливаем рабочую директорию
WORKDIR /app

# Копируем только файлы, необходимые для разрешения зависимостей
COPY build.gradle.kts settings.gradle.kts ./

# Загружаем зависимости
RUN gradle dependencies --no-daemon

# Копируем исходный код
COPY src ./src

# Собираем приложение, пропуская тесты
RUN gradle build -x test --no-daemon

# Этап 2: Создание финального образа
FROM eclipse-temurin:21-jre-alpine

WORKDIR /app

# Копируем собранный jar из первого этапа
COPY --from=build /app/build/libs/*.jar app.jar

# Открываем порт, который будет использовать приложение
EXPOSE 8080

# Указываем точку входа для запуска приложения
CMD ["java", "-XX:+UnlockExperimentalVMOptions", "-XX:+UseContainerSupport", "-jar", "app.jar"]