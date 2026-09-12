# Project Overview

## Subject Area

The project is a desktop quiz system based on the "Who Wants to Be a Millionaire?" format. A player starts a game, receives random active questions, chooses answers and can finish by winning, choosing to take money or making a wrong answer.

The database stores stable reference data and game history:

- players;
- question categories;
- difficulty levels and prize amounts;
- questions;
- answer options;
- game sessions;
- answers selected during games.

## Business Rules

- A player name is required and must be unique in the `players` table.
- A question belongs to one category and one difficulty level.
- Each question has exactly four answer positions in application logic; the database prevents duplicate positions.
- A difficulty level has a unique round number from 1 to 10.
- A game belongs to one player.
- A finished game has a final amount and one end reason: `WIN`, `WRONG` or `TAKE`.
- A game can store only one answer per round.
- The leaderboard is calculated from finished games and is not stored as a separate table.

## Logical Model

Tables and main relationships:

- `players 1:N games`
- `categories 1:N questions`
- `difficulty_levels 1:N questions`
- `questions 1:N answers`
- `games 1:N game_answers`
- `questions 1:N game_answers`
- `answers 1:N game_answers`

## Relational Model

Primary keys:

- `players.player_id`
- `categories.category_id`
- `difficulty_levels.difficulty_id`
- `questions.question_id`
- `answers.answer_id`
- `games.game_id`
- `game_answers.game_answer_id`

Important foreign keys:

- `questions.category_id -> categories.category_id`
- `questions.difficulty_id -> difficulty_levels.difficulty_id`
- `answers.question_id -> questions.question_id`
- `games.player_id -> players.player_id`
- `game_answers.game_id -> games.game_id`
- `game_answers.question_id -> questions.question_id`
- `game_answers.selected_answer_id -> answers.answer_id`

Important constraints:

- unique player names;
- unique category names;
- unique round numbers and prize amounts;
- round number range from 1 to 10;
- answer position range from 1 to 4;
- unique answer position inside each question;
- unique game answer per game round;
- allowed game end reasons only.

## BCNF Notes

Each table has a surrogate primary key. Non-key attributes describe only the entity identified by that key.

- In `players`, `player_name` and `created_at` depend on `player_id`; `player_name` is also unique.
- In `categories`, description depends only on `category_id`; category name is unique.
- In `difficulty_levels`, prize money and round number identify one difficulty level.
- In `questions`, text, hint, category and difficulty depend only on `question_id`.
- In `answers`, answer text, position and correctness depend only on `answer_id`; `(question_id, position_number)` is also a candidate key for answer position.
- In `games`, start time, finish time, result and player reference depend only on `game_id`.
- In `game_answers`, selected answer, question, round and correctness depend only on `game_answer_id`; round uniqueness inside a game is enforced.

There are no non-key attributes that determine other non-key attributes inside these tables, so the model is suitable for BCNF justification in the report.

## Application Structure

- `core` - quiz rules, questions, lifelines and scores.
- `db` - database configuration and JDBC connection factory.
- `repository` - SQL/JDBC access classes.
- `ui` - JavaFX controllers and screens.
- `database` - SQL scripts and Docker database setup.
- `config` - ready database connection files.

## Testing Plan

Application tests:

- player name validation;
- question format validation;
- game progress and final result logic;
- lifeline behavior;
- leaderboard sorting logic.

Database tests in DBeaver:

- run schema scripts successfully for PostgreSQL and MySQL;
- verify all 7 tables exist;
- verify at least 5 rows in every table;
- check foreign key links between questions, answers, games and selected answers;
- run leaderboard query;
- try invalid inserts, for example duplicate answer position or invalid round number.

Manual application tests:

- start PostgreSQL database and run the app;
- start a new game and answer several questions;
- finish the game and check that `games` and `game_answers` received new rows;
- switch to MySQL config and repeat the same flow.
