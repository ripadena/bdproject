# Database Setup

This folder contains two equivalent relational database implementations:

- PostgreSQL: `postgres/schema.sql`, `postgres/data.sql`
- MySQL: `mysql/schema.sql`, `mysql/data.sql`

## Start Both Databases

```bash
/Applications/Docker.app/Contents/Resources/bin/docker compose up -d
```

Docker creates two databases with the same logical structure and test data.

## Stop Databases

```bash
/Applications/Docker.app/Contents/Resources/bin/docker compose down
```

## Recreate From Scratch

```bash
/Applications/Docker.app/Contents/Resources/bin/docker compose down -v
/Applications/Docker.app/Contents/Resources/bin/docker compose up -d
```

## PostgreSQL Connection

- Host: `localhost`
- Port: `5432`
- Database: `millionaire_quiz`
- User: `millionaire`
- Password: `millionaire`

## MySQL Connection

- Host: `localhost`
- Port: `3306`
- Database: `millionaire_quiz`
- User: `millionaire`
- Password: `millionaire`

## Leaderboard Query

```sql
SELECT player_name, final_money, finished_at
FROM leaderboard
ORDER BY final_money DESC, finished_at ASC
LIMIT 10;
```
