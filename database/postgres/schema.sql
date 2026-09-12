DROP VIEW IF EXISTS leaderboard;
DROP TABLE IF EXISTS game_answers;
DROP TABLE IF EXISTS games;
DROP TABLE IF EXISTS answers;
DROP TABLE IF EXISTS questions;
DROP TABLE IF EXISTS difficulty_levels;
DROP TABLE IF EXISTS categories;
DROP TABLE IF EXISTS players;

CREATE TABLE players (
    player_id BIGSERIAL PRIMARY KEY,
    player_name VARCHAR(20) NOT NULL UNIQUE,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT chk_players_name_not_blank CHECK (length(trim(player_name)) > 0)
);

CREATE TABLE categories (
    category_id BIGSERIAL PRIMARY KEY,
    category_name VARCHAR(50) NOT NULL UNIQUE,
    description VARCHAR(200)
);

CREATE TABLE difficulty_levels (
    difficulty_id BIGSERIAL PRIMARY KEY,
    round_number INTEGER NOT NULL UNIQUE,
    prize_money INTEGER NOT NULL UNIQUE,
    CONSTRAINT chk_difficulty_round CHECK (round_number BETWEEN 1 AND 10),
    CONSTRAINT chk_difficulty_prize CHECK (prize_money > 0)
);

CREATE TABLE questions (
    question_id BIGSERIAL PRIMARY KEY,
    category_id BIGINT NOT NULL REFERENCES categories(category_id),
    difficulty_id BIGINT NOT NULL REFERENCES difficulty_levels(difficulty_id),
    question_text VARCHAR(200) NOT NULL UNIQUE,
    friend_hint VARCHAR(200) NOT NULL,
    is_active BOOLEAN NOT NULL DEFAULT TRUE,
    CONSTRAINT chk_questions_text_not_blank CHECK (length(trim(question_text)) > 0),
    CONSTRAINT chk_questions_hint_not_blank CHECK (length(trim(friend_hint)) > 0)
);

CREATE TABLE answers (
    answer_id BIGSERIAL PRIMARY KEY,
    question_id BIGINT NOT NULL REFERENCES questions(question_id) ON DELETE CASCADE,
    position_number INTEGER NOT NULL,
    answer_text VARCHAR(120) NOT NULL,
    is_correct BOOLEAN NOT NULL DEFAULT FALSE,
    CONSTRAINT uq_answers_question_position UNIQUE (question_id, position_number),
    CONSTRAINT chk_answers_position CHECK (position_number BETWEEN 1 AND 4),
    CONSTRAINT chk_answers_text_not_blank CHECK (length(trim(answer_text)) > 0)
);

CREATE UNIQUE INDEX uq_answers_one_correct_per_question
    ON answers(question_id)
    WHERE is_correct;

CREATE TABLE games (
    game_id BIGSERIAL PRIMARY KEY,
    player_id BIGINT NOT NULL REFERENCES players(player_id),
    started_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    finished_at TIMESTAMP,
    final_money INTEGER NOT NULL DEFAULT 0,
    end_reason VARCHAR(10),
    CONSTRAINT chk_games_final_money CHECK (final_money >= 0),
    CONSTRAINT chk_games_end_reason CHECK (end_reason IS NULL OR end_reason IN ('WIN', 'WRONG', 'TAKE')),
    CONSTRAINT chk_games_finished_after_start CHECK (finished_at IS NULL OR finished_at >= started_at)
);

CREATE TABLE game_answers (
    game_answer_id BIGSERIAL PRIMARY KEY,
    game_id BIGINT NOT NULL REFERENCES games(game_id) ON DELETE CASCADE,
    round_number INTEGER NOT NULL,
    question_id BIGINT NOT NULL REFERENCES questions(question_id),
    selected_answer_id BIGINT NOT NULL REFERENCES answers(answer_id),
    is_correct BOOLEAN NOT NULL,
    answered_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT uq_game_answers_round UNIQUE (game_id, round_number),
    CONSTRAINT uq_game_answers_question UNIQUE (game_id, question_id),
    CONSTRAINT chk_game_answers_round CHECK (round_number BETWEEN 1 AND 10)
);

CREATE VIEW leaderboard AS
SELECT
    p.player_name,
    g.final_money,
    g.finished_at
FROM games g
JOIN players p ON p.player_id = g.player_id
WHERE g.finished_at IS NOT NULL;
