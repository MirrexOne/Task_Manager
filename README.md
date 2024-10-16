<!--- ### Hexlet tests and linter status:
[![Actions Status](https://github.com/MirrexOne/java-project-99/actions/workflows/hexlet-check.yml/badge.svg)](https://github.com/MirrexOne/java-project-99/actions)
-->
### Maintainability:
[![Project build check](https://github.com/MirrexOne/java-project-99/actions/workflows/main.yml/badge.svg)](https://github.com/MirrexOne/java-project-99/actions)
[![Maintainability](https://api.codeclimate.com/v1/badges/456719fe54f7c2ebe27f/maintainability)](https://codeclimate.com/github/MirrexOne/java-project-99/maintainability)
[![Test Coverage](https://api.codeclimate.com/v1/badges/456719fe54f7c2ebe27f/test_coverage)](https://codeclimate.com/github/MirrexOne/java-project-99/test_coverage)

# Менеджер задач #
[Deployed application](https://task-tracker-1qam.onrender.com)

## Описание ##

Менеджер задач - представляет собой приложение для создания, отслеживания и манипуляции задачами - назначения на них разных исполнителей, изменения статуса задач и добавления к задачам разных множественных лейблов(аналог категорий).  
Приложение можно использовать, как локально, так и в production-среде. 

### Реализовано ###

Создание нескольких сущностей и полного цикла CRUD-операций для них.  
Реализована связь между полями таблиц по внешнему ключу, как связями OneToMany, так и ManyToMany.  
Возможность и необходимость первичный аутентификации(большинство путей не будут доступны без неё).  
Начальная инцииализация пользователя, нескольких статусов и лейблов для оперативной работы.  
Добавлена фильтрация по параметрам - имя задачи, исполнитель, статусы и лейблы задачи.  

### Необходимо для локального подключения: ###
- [Git installed](https://git-scm.com/book/en/v2/Getting-Started-Installing-Git)  
- [Java](https://www.oracle.com/java/technologies/downloads)  
- [Gradle](https://gradle.org/install)

### Подключи и используй локально ###
##### В терминале: #####
```
git clone git@github.com:MirrexOne/Task_Manager.git
cd TaskManager
make run
```
##### В браузере: #####
```
localhost:8080
```
##### Авторизация: #####
```
Username: mirrex@dev.io
Password: qwerty
```

### Использующиеся технологии ###
- Фреймворк: **Spring Boot**
- Аутентификация: **Spring Security**
- Автоматический маппинг: **Mapstruct**
- Шаблон проектирования: **DTO**
- Внешнее отслеживание ошибок: **Sentry**
- Документация по API-приложения: **Springdoc Openapi**, **Swagger**
- Тесты: **JUnit 5**, **Mockwebserver**, **Datafaker**
- Отчет о тестах: **Jacoco**
- Линтер: **Checkstyle**
- Базы данных: **H2** (внутренняя), **PostgreSQL** (в production)
- Развертывание в production: **Docker**
> Примечание: в данный момент из-за того, что приложение задеплоено на бесплатный тариф на хостинге, оно очень медленно открывается в production среде, **локальное использование предпочтительнее** 

![image](https://github.com/DEGTEVUWU/java-project-99/assets/148809450/4b1660f7-a80a-44f8-bb3d-739047af1336)
![image](https://github.com/DEGTEVUWU/java-project-99/assets/148809450/2774f884-8ca9-41e9-b7cf-c35185435801)
![image](https://github.com/DEGTEVUWU/java-project-99/assets/148809450/0fee1ca4-bec2-482e-88c1-6e589f92b358)
![image](https://github.com/DEGTEVUWU/java-project-99/assets/148809450/7d1a10c6-da81-4a95-b7f5-a283bd8db0b8)
