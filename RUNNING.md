# Running

Requirements:
- Java 21
- Maven 3.9+ or the included Maven wrapper
- Docker Desktop for local PostgreSQL/MySQL databases

Start databases:
`cd database && /Applications/Docker.app/Contents/Resources/bin/docker compose up -d`

Run from IDE/project root with PostgreSQL:
`./mvnw javafx:run`

Run with MySQL:
`./mvnw javafx:run -Dmillionaire.db.config=config/db-mysql.properties`

Run tests:
`./mvnw clean test`

Build portable dist:
`./mvnw clean package`

After package:
- Windows: run `dist/run.bat`
- macOS/Linux: run `chmod +x dist/run.sh && ./dist/run.sh`

Database settings:
- default file: `src/main/resources/db.properties`
- PostgreSQL example: `config/db-postgres.properties`
- MySQL example: `config/db-mysql.properties`

The old `data/questions.txt` and `data/scores.csv` files are kept only as legacy source material. The app now uses relational databases through JDBC.
