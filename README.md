# FMS-Parsers

## Техническое задание
https://docs.google.com/document/d/1gwk3Ic6_WK7gwqLGy7QGbXRtWdqdipdcJ3wJNceZT-M/edit

## Список используемых технологий

### Backend
* OpenJDK 8
* Spring Boot
* Spring Mvc
* Spring Data
* Apache Commons
* PostgreSQL
* Liquibase
* JUnit
* MockMvc
* Testcontainers
* Swagger UI 
  
### Frontend
* HTML
* Vanila JS
* Bootstrap
* jQuery

### Containers
* Docker
* Docker Compose 

## Команды для деплоя, локального запуска и тестирования

### Docker Compose (from bash for Windows NT)
`docker compose up -f docker/docker-compose.yml -d`

### Maven
#### Запустить тесты
`mvn clean verify`

#### Быстрый запуск при насторенном подключении к БД
`mvn spring-boot:run`

## Ссылки

### Паенль управления
http://localhost:8080/api/index.html

### Swagger UI
http://localhost:8080/api/swagger-ui/index.html

## Дополнительные инструкции

### Ссылка на гайд по установке Docker и Docker Compose для популярных ОС
https://docs.docker.com/engine/install/

