# Используем базовый образ с Java 21
FROM eclipse-temurin:21-jdk

# Определяем версию Gradle
ARG GRADLE_VERSION=8.5

# Обновляем пакеты и устанавливаем необходимые зависимости
RUN apt-get update && apt-get install -yq make unzip

# Устанавливаем рабочую директорию в папку с проектом
WORKDIR /app

# Копируем все файлы проекта
COPY ./app .

# Выполняем сборку проекта через Gradle
RUN ./gradlew --no-daemon build

# Открываем порт 8080 для приложения
EXPOSE 8080

# Запуск приложения
CMD ["java", "-jar", "build/libs/app-0.0.1-SNAPSHOT.jar"]
