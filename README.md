# EGRP365Bot 

Бот для получения информации об объектах
недвижимого имущества и земельных участках c сайта  [ЕГРП365](https://egrp365.org/map/).

- ЕГРП — единый государственный реестр прав.

### Стэк технологий 
- Java 11
- Selenium
- WebDriverManager
- Gradle
- Lombok
 

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

### Todo: 
- Собрать jar.
- Разобраться с кодировками.
- Дописать тесты. (JUnit, Mockito).
- Реализовать для массива параметров.
- Интегрировать в Spring для реализации API.