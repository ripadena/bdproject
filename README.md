# Millionaire Quiz: проект по базам данных

JavaFX-приложение-викторина в стиле «Кто хочет стать миллионером?» с подключением к реляционной базе данных через JDBC.

Проект адаптирован для IntelliJ IDEA, содержит две реализации базы данных — PostgreSQL и MySQL — и может быть запущен локально через Docker Desktop.

## Что реализовано

- Графическое Java-приложение на JavaFX.
- Подключение к базе данных через JDBC без ORM-фреймворков.
- Реляционная база данных из 7 связанных таблиц.
- Две версии одной и той же логической модели: PostgreSQL и MySQL.
- SQL-скрипты для создания структуры базы данных.
- SQL-скрипты с тестовыми данными.
- Минимум 5 тестовых записей в каждой основной таблице.
- Настройки подключения вынесены в конфигурационные файлы.
- Проект можно открыть и запустить в IntelliJ IDEA как Maven-проект.
- Лидерборд считается из завершённых игр, а не хранится отдельной дублирующей таблицей.

## Предметная область

Игрок вводит имя и начинает викторину. Приложение загружает вопросы из базы данных, показывает варианты ответов, сохраняет ход игры и после завершения отображает таблицу лучших результатов.

В базе хранятся:

- игроки;
- категории вопросов;
- уровни сложности и суммы выигрыша;
- вопросы;
- варианты ответов;
- игровые сессии;
- ответы игрока в конкретной игре.

## Структура базы данных

Основные таблицы:

1. `players` — игроки.
2. `categories` — категории вопросов.
3. `difficulty_levels` — уровни сложности и суммы выигрыша.
4. `questions` — вопросы.
5. `answers` — варианты ответов.
6. `games` — игровые сессии.
7. `game_answers` — ответы игрока в рамках конкретной игры.

Дополнительно создано представление `leaderboard`, которое показывает таблицу лидеров на основе данных из `games` и `players`.

Файлы со структурой базы:

- PostgreSQL: `database/postgres/schema.sql`
- MySQL: `database/mysql/schema.sql`

Файлы с тестовыми данными:

- PostgreSQL: `database/postgres/data.sql`
- MySQL: `database/mysql/data.sql`

## Требования для запуска

На компьютере должны быть установлены:

- Java 21 или новее;
- IntelliJ IDEA;
- Docker Desktop;
- DBeaver, если нужно посмотреть базу данных через графический интерфейс.

Maven отдельно устанавливать не нужно: в проекте есть Maven Wrapper (`mvnw`).

## Быстрый запуск

1. Открой Docker Desktop и дождись, пока он полностью запустится.
2. Перейди в папку проекта:

```bash
cd millionaire-quiz
```

3. Запусти обе базы данных:

```bash
cd database
docker compose up -d
cd ..
```

После этого Docker поднимет два контейнера:

- `millionaire_postgres`
- `millionaire_mysql`

4. Запусти приложение с PostgreSQL:

```bash
./mvnw javafx:run
```

5. Если нужно запустить приложение с MySQL:

```bash
./mvnw javafx:run -Dmillionaire.db.config=config/db-mysql.properties
```

Если команда `docker` не находится на macOS, можно использовать полный путь:

```bash
/Applications/Docker.app/Contents/Resources/bin/docker compose up -d
```

## Запуск через IntelliJ IDEA

1. Открой IntelliJ IDEA.
2. Нажми `Open`.
3. Выбери папку проекта `millionaire-quiz`, где находится файл `pom.xml`.
4. Дождись, пока IntelliJ IDEA импортирует Maven-проект.
5. Убедись, что выбран SDK Java 21 или новее.
6. Перед запуском приложения подними базы данных через Docker:

```bash
cd database
docker compose up -d
```

7. В IntelliJ IDEA выбери конфигурацию `Millionaire Quiz` и нажми `Run`.

Если конфигурация не появилась автоматически, открой вкладку Maven и запусти цель:

```bash
clean javafx:run
```

## Подключение к базе данных в DBeaver

### PostgreSQL

- Host: `localhost`
- Port: `5432`
- Database: `millionaire_quiz`
- User: `millionaire`
- Password: `millionaire`

### MySQL

- Host: `localhost`
- Port: `3306`
- Database: `millionaire_quiz`
- User: `millionaire`
- Password: `millionaire`

После подключения можно открыть таблицы и проверить данные:

- `players`
- `categories`
- `difficulty_levels`
- `questions`
- `answers`
- `games`
- `game_answers`

Для просмотра лидерборда можно выполнить запрос:

```sql
SELECT player_name, final_money, finished_at
FROM leaderboard
ORDER BY final_money DESC, finished_at ASC
LIMIT 10;
```

## Проверка, что база работает

Проверить контейнеры:

```bash
cd database
docker compose ps
```

Проверить количество таблиц в PostgreSQL:

```bash
docker exec millionaire_postgres \
  psql -U millionaire -d millionaire_quiz \
  -c "SELECT COUNT(*) AS tables_count FROM information_schema.tables WHERE table_schema = 'public' AND table_type = 'BASE TABLE';"
```

Проверить лидерборд в PostgreSQL:

```bash
docker exec millionaire_postgres \
  psql -U millionaire -d millionaire_quiz \
  -c "SELECT player_name, final_money FROM leaderboard ORDER BY final_money DESC LIMIT 5;"
```

Проверить лидерборд в MySQL:

```bash
docker exec millionaire_mysql \
  mysql -umillionaire -pmillionaire millionaire_quiz \
  -e "SELECT player_name, final_money FROM leaderboard ORDER BY final_money DESC LIMIT 5;"
```

## Тесты

Запуск автоматических тестов:

```bash
./mvnw clean test
```

На момент подготовки проекта тесты проходят успешно:

```text
Tests run: 16, Failures: 0, Errors: 0, Skipped: 0
```

## Сборка готового приложения

Собрать проект:

```bash
./mvnw clean test package
```

После сборки появится папка `dist`.

Запустить собранную версию на macOS:

```bash
sh dist/run.sh
```

По умолчанию собранная версия использует PostgreSQL. Для запуска с MySQL можно указать другой конфиг:

```bash
MILLIONAIRE_DB_CONFIG=../config/db-mysql.properties sh dist/run.sh
```

## Конфигурационные файлы

- `src/main/resources/db.properties` — настройки по умолчанию для PostgreSQL.
- `config/db-postgres.properties` — внешний конфиг PostgreSQL.
- `config/db-mysql.properties` — внешний конфиг MySQL.

Приложение выбирает конфиг так:

1. Если указан параметр `millionaire.db.config`, используется этот файл.
2. Если параметр не указан, используется `src/main/resources/db.properties`.

## Основные папки проекта

- `src/main/java` — Java-код приложения.
- `src/main/resources` — FXML, CSS и настройки по умолчанию.
- `database` — Docker Compose и SQL-скрипты баз данных.
- `config` — внешние настройки подключения к БД.
- `docs` — описание предметной области, модели данных и нормализации.
- `data` — старые файлы из первой версии проекта, оставлены как исходный материал.
- `.idea` — настройки IntelliJ IDEA и готовая конфигурация запуска.

## Как остановить базы данных

```bash
cd database
docker compose down
```

## Как пересоздать базы данных с нуля

Если нужно удалить старые данные и заново выполнить SQL-скрипты:

```bash
cd database
docker compose down -v
docker compose up -d
```

## Что показать преподавателю

1. Код Java-приложения в IntelliJ IDEA.
2. Пакет `repository`, где находятся JDBC-запросы к базе.
3. Пакет `db`, где находится подключение и загрузка конфигурации.
4. SQL-скрипты `schema.sql` и `data.sql` для PostgreSQL и MySQL.
5. Диаграмму или структуру таблиц в DBeaver.
6. Запущенную игру, где вопросы и лидерборд берутся из базы.
7. Результат тестов `./mvnw clean test`.

