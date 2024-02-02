# [Java_Spring_TelegramBot](https://t.me/SpringBoot2_Telegram_Bot)


 # О проекте
 
Данный проект представляет собой [бота](https://t.me/SpringBoot2_Telegram_Bot) для Telegram, разработанного на базе Spring Boot. Он реализует следующий функционал:

Регистрация пользователей и обновление времени последнего сообщения.
Регистрация всех входящих сообщений и отправленных ответов.
Ежедневное заполнение таблицы daily_domains данными из [backorder](https://backorder.ru/json/?order=desc&expired=1&by=hotness&page=1&items=50) (данные прошлого дня удаляются).

## Технологии

Для разработки проекта использовались следующие технологии:

Java 11
Maven
Spring Boot 2
PostgreSQL
Liquibase для миграций БД
Spring Boot Starter Logging
telegrambots-spring-boot-starter

## Установка и настройка проекта

1. Установите Java 11-14 на вашу систему, если еще не установлено.
2. Склонируйте репозиторий проекта с помощью команды:

`git clone https://github.com/your-username/your-repo.git`

3. Установите и настройте Maven, если еще не установлено.
4. Перейдите в каталог проекта:

`cd your-repo`

5. В корневом каталоге проекта выполните команду для сборки проекта:

`mvn clean install`

6. Настройте базу данных. Вы дожны использовать PostgreSQL.
* Создайте базу данных в **pgAdmin** и укажите её в файле "\SpringTelegramBot\src\main\resources\application.properties"
* Таблицы будут созданы автоматически
  
7. Настройте Telegram бота. В файле application.properties установите токен вашего Telegram бота в разделе telegram.bot.token.
8. Запустите проект с помощью команды:

`mvn spring-boot:run`

# Использование проекта

После запуска проекта, бот будет доступен в Telegram и выполнять указанный функционал:

* Регистрировать пользователей и обновлять время последнего сообщения, занося информацию в таблицу "users"
* Регистрировать все входящие сообщения и отправленные ответы, занося информацию в таблицу "messages"
* Ежедневно забирать данные с [backorder](https://backorder.ru/json/?order=desc&expired=1&by=hotness&page=1&items=50) и заносить информацию в таблицу "daily_domains", а данные прошлого дня удаляются. ***По умолчанию Json файл скачивается автоматически и заносится в базу данных каждый день в 14.55*** (время можно изменить)
* Ежедневно после сбора базы, отправлять всем зарегистрированным пользователям сообщение вида "YYY-MM-dd. Собрано N доменов". ***По умолчанию отправка сообщений происходит каждый день в 15.00*** (время можно изменить)

