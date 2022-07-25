# EGRP365Bot 

Бот для получения информации об объектах
недвижимого имущества и земельных участках c сайта  [ЕГРП365](https://egrp365.ru/map/).

- ЕГРП — единый государственный реестр прав.

### Стэк технологий 
- Java 11
- Selenium
- WebDriverManager
- Lombok
- Gradle
- JUnit
 

## Сборка приложения
```shell script
# Склонировать проект к себе
git clone https://github.com/devalurum/egrp365-bot.git

# загружает gradle wrapper
gradlew wrapper

# сборка проекта
gradlew clean build 

# запуск бота
gradlew run 

# Ввод кадастрового номера (пример)
> 38:36:000034:2954

# запуск бота с параметром кадастрового номера
gradlew run --args="38:36:000034:2954"
```

### Через jar-файл
```shell script
# Склонировать проект к себе
git clone https://github.com/devalurum/egrp365-bot.git

# загружает gradle wrapper
gradlew wrapper

# сборка проекта
gradlew clean build 

# запуск jar-файла
cd build/libs
java -jar egrp365-bot.jar
# или запуск с параметром кадастрового номера
java -jar egrp365-bot.jar 38:36:000034:2954

# Ввод кадастрового номера (пример)
> 38:36:000034:2954
```

### Todo:
- Определиться с полями сущности, т.к они зачастую меняются в зависимости от вида объекта.
- Продумать модель сущности.
- Реализовать для массива параметров.
- Разобраться как замокать WebDriver и остальные объекты (возникли проблемы) для модульного теста бота без браузера.
- Интегрировать в Spring для реализации API.