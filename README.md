# Violets Nails Telegram Bot

Telegram-бот для автоматизации записи клиентов в маникюрный салон.

Проект позволяет клиентам самостоятельно выбрать услугу, дату и время записи через Telegram, а также помогает автоматизировать процесс бронирования без участия администратора.

---

## Основной функционал

* Регистрация пользователей через Telegram
* Просмотр доступных услуг
* Выбор категории услуг
* Выбор даты и времени записи
* Создание записи на выбранную услугу
* Просмотр существующих записей
* Работа с ролями пользователей
* Интерактивные Telegram-клавиатуры

---

## Используемые технологии

* Java 21
* Spring Boot
* Spring Data JPA
* PostgreSQL
* Telegram Bot API
* Maven
* Lombok

---

## Архитектура

Проект построен по многослойной архитектуре.

```text
Telegram User
      │
      ▼
Telegram Bot
      │
      ▼
Command Dispatcher
      │
      ▼
Service
      │
      ▼
Repository
      │
      ▼
PostgreSQL
```

### Структура проекта

```text
src
├── bot
├── commands
├── controller
├── dto
├── entity
├── keyboards
├── repository
├── service
├── config
└── exception
```

---

## Запуск проекта

### Требования

* Java 21
* PostgreSQL
* Telegram Bot Token

### Клонирование репозитория

```bash
git clone https://github.com/Hollowflk/VioletsNeils.git

cd VioletsNeils
```

### Запуск

```bash
./mvnw spring-boot:run
```

Перед запуском необходимо указать:

* токен Telegram-бота;
* параметры подключения к PostgreSQL.

---

## Основные возможности

* Регистрация пользователей
* Выбор категории услуг
* Выбор услуги
* Выбор свободной даты
* Выбор времени
* Создание записи
* Просмотр записей
* Управление пользователями

---

## Что было реализовано

* Telegram-бот на Java
* Командная архитектура обработки пользовательских действий
* REST API на Spring Boot
* Работа с PostgreSQL через Spring Data JPA
* DTO для передачи данных
* Централизованная обработка исключений
* Интерактивные клавиатуры Telegram
* Разделение приложения на слои Controller / Service / Repository

---
