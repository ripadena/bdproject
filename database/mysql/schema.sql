DROP VIEW IF EXISTS leaderboard;
DROP TABLE IF EXISTS game_answers;
DROP TABLE IF EXISTS games;
DROP TABLE IF EXISTS answers;
DROP TABLE IF EXISTS questions;
DROP TABLE IF EXISTS difficulty_levels;
DROP TABLE IF EXISTS categories;
DROP TABLE IF EXISTS players;

CREATE TABLE players (
    player_id BIGINT PRIMARY KEY AUTO_INCREMENT,
    player_name VARCHAR(20) NOT NULL UNIQUE,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT chk_players_name_not_blank CHECK (char_length(trim(player_name)) > 0)
);

CREATE TABLE categories (
    category_id BIGINT PRIMARY KEY AUTO_INCREMENT,
    category_name VARCHAR(50) NOT NULL UNIQUE,
    description VARCHAR(200)
);

CREATE TABLE difficulty_levels (
    difficulty_id BIGINT PRIMARY KEY AUTO_INCREMENT,
    round_number INT NOT NULL UNIQUE,
    prize_money INT NOT NULL UNIQUE,
    CONSTRAINT chk_difficulty_round CHECK (round_number BETWEEN 1 AND 10),
    CONSTRAINT chk_difficulty_prize CHECK (prize_money > 0)
);

CREATE TABLE questions (
    question_id BIGINT PRIMARY KEY AUTO_INCREMENT,
    category_id BIGINT NOT NULL,
    difficulty_id BIGINT NOT NULL,
    question_text VARCHAR(200) NOT NULL UNIQUE,
    friend_hint VARCHAR(200) NOT NULL,
    is_active BOOLEAN NOT NULL DEFAULT TRUE,
    CONSTRAINT fk_questions_category FOREIGN KEY (category_id) REFERENCES categories(category_id),
    CONSTRAINT fk_questions_difficulty FOREIGN KEY (difficulty_id) REFERENCES difficulty_levels(difficulty_id),
    CONSTRAINT chk_questions_text_not_blank CHECK (char_length(trim(question_text)) > 0),
    CONSTRAINT chk_questions_hint_not_blank CHECK (char_length(trim(friend_hint)) > 0)
);

CREATE TABLE answers (
    answer_id BIGINT PRIMARY KEY AUTO_INCREMENT,
    question_id BIGINT NOT NULL,
    position_number INT NOT NULL,
    answer_text VARCHAR(120) NOT NULL,
    is_correct BOOLEAN NOT NULL DEFAULT FALSE,
    CONSTRAINT fk_answers_question FOREIGN KEY (question_id) REFERENCES questions(question_id) ON DELETE CASCADE,
    CONSTRAINT uq_answers_question_position UNIQUE (question_id, position_number),
    CONSTRAINT chk_answers_position CHECK (position_number BETWEEN 1 AND 4),
    CONSTRAINT chk_answers_text_not_blank CHECK (char_length(trim(answer_text)) > 0)
);

CREATE TABLE games (
    game_id BIGINT PRIMARY KEY AUTO_INCREMENT,
    player_id BIGINT NOT NULL,
    started_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    finished_at TIMESTAMP NULL,
    final_money INT NOT NULL DEFAULT 0,
    end_reason VARCHAR(10),
    CONSTRAINT fk_games_player FOREIGN KEY (player_id) REFERENCES players(player_id),
    CONSTRAINT chk_games_final_money CHECK (final_money >= 0),
    CONSTRAINT chk_games_end_reason CHECK (end_reason IS NULL OR end_reason IN ('WIN', 'WRONG', 'TAKE')),
    CONSTRAINT chk_games_finished_after_start CHECK (finished_at IS NULL OR finished_at >= started_at)
);

CREATE TABLE game_answers (
    game_answer_id BIGINT PRIMARY KEY AUTO_INCREMENT,
    game_id BIGINT NOT NULL,
    round_number INT NOT NULL,
    question_id BIGINT NOT NULL,
    selected_answer_id BIGINT NOT NULL,
    is_correct BOOLEAN NOT NULL,
    answered_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_game_answers_game FOREIGN KEY (game_id) REFERENCES games(game_id) ON DELETE CASCADE,
    CONSTRAINT fk_game_answers_question FOREIGN KEY (question_id) REFERENCES questions(question_id),
    CONSTRAINT fk_game_answers_answer FOREIGN KEY (selected_answer_id) REFERENCES answers(answer_id),
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
