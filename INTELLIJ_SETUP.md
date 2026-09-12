# Millionaire Quiz: IntelliJ IDEA setup

## Как открыть

1. Открой IntelliJ IDEA.
2. Выбери `Open`.
3. Открой папку `millionaire-quiz`, именно папку с файлом `pom.xml`.
4. Если IDEA спросит, импортировать ли Maven-проект, согласись.
5. Проверь, что Project SDK выставлен на Java 21 или новее.

## Как запустить

Перед запуском приложения должна быть поднята база данных:

```bash
cd database
/Applications/Docker.app/Contents/Resources/bin/docker compose up -d
```

В верхнем меню конфигураций выбери `Millionaire Quiz` и нажми Run.

Если конфигурация не появилась автоматически:

1. Открой вкладку Maven справа.
2. Запусти цель `clean javafx:run`.

## Как проверить тесты

В терминале из корня проекта:

```bash
./mvnw clean test
```

## Где данные

Основные данные теперь лежат в реляционной базе:

- PostgreSQL: `localhost:5432`, база `millionaire_quiz`
- MySQL: `localhost:3306`, база `millionaire_quiz`
- пользователь: `millionaire`
- пароль: `millionaire`

Старые файлы `data/questions.txt` и `data/scores.csv` оставлены только как исходный материал из старой версии проекта.
