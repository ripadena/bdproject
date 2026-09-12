# Millionaire Quiz Database Project

JavaFX quiz application connected to relational databases through plain JDBC.

## Project Idea

The application runs a "Who Wants to Be a Millionaire?" style quiz. It stores players, categories, difficulty levels, questions, answers, game sessions and selected answers in a relational database. The leaderboard is calculated from completed games, so it is not duplicated in a separate table.

## Main Requirements Covered

- Java desktop application with graphical interface.
- JDBC database access without ORM frameworks.
- Relational database with 7 linked tables.
- PostgreSQL and MySQL versions of the same database.
- SQL scripts for schema creation and test data.
- At least 5 test records for every table.
- Database connection settings are loaded from configuration files.
- Maven project adapted for IntelliJ IDEA.

## Database Tables

1. `players`
2. `categories`
3. `difficulty_levels`
4. `questions`
5. `answers`
6. `games`
7. `game_answers`

The leaderboard is provided by the `leaderboard` SQL view and by a JDBC query over `games` and `players`.

## Quick Start

1. Open Docker Desktop.
2. Start databases:

```bash
cd database
/Applications/Docker.app/Contents/Resources/bin/docker compose up -d
```

3. Run the app with PostgreSQL:

```bash
./mvnw javafx:run
```

4. Run the app with MySQL:

```bash
./mvnw javafx:run -Dmillionaire.db.config=config/db-mysql.properties
```

5. Run tests:

```bash
./mvnw clean test
```

## IntelliJ IDEA

Open the project folder that contains `pom.xml`. IntelliJ IDEA should import it as a Maven project. The run configuration `Millionaire Quiz` starts the app with Maven.

## DBeaver Connections

PostgreSQL:

- Host: `localhost`
- Port: `5432`
- Database: `millionaire_quiz`
- User: `millionaire`
- Password: `millionaire`

MySQL:

- Host: `localhost`
- Port: `3306`
- Database: `millionaire_quiz`
- User: `millionaire`
- Password: `millionaire`

## Useful Files

- `database/postgres/schema.sql` - PostgreSQL structure.
- `database/postgres/data.sql` - PostgreSQL test data.
- `database/mysql/schema.sql` - MySQL structure.
- `database/mysql/data.sql` - MySQL test data.
- `database/docker-compose.yml` - local database containers.
- `config/db-postgres.properties` - PostgreSQL app settings.
- `config/db-mysql.properties` - MySQL app settings.
- `docs/PROJECT_OVERVIEW.md` - subject area, model, normalization and testing notes.
