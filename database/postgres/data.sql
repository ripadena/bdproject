INSERT INTO players (player_name) VALUES
('Misha'),
('Anna'),
('Ivan'),
('Sofia'),
('Nikita');

INSERT INTO categories (category_name, description) VALUES
('Geography', 'Countries, capitals, oceans and mountains'),
('Science', 'Physics, chemistry and nature'),
('History', 'Historical dates and people'),
('Literature', 'Books and authors'),
('Technology', 'Programming and modern technology');

INSERT INTO difficulty_levels (round_number, prize_money) VALUES
(1, 500),
(2, 1000),
(3, 2000),
(4, 3000),
(5, 5000),
(6, 10000),
(7, 20000),
(8, 40000),
(9, 80000),
(10, 150000);

INSERT INTO questions (category_id, difficulty_id, question_text, friend_hint, is_active) VALUES
(1, 1, 'Capital of France?', 'Friend is almost sure it is Paris', TRUE),
(2, 1, 'How much is 2 + 2?', 'Simple math says B', TRUE),
(1, 2, 'Largest planet in Solar System?', 'It should be Jupiter', TRUE),
(4, 2, 'Author of War and Peace?', 'Friend remembers Tolstoy', TRUE),
(1, 3, 'Largest ocean?', 'Pacific seems right', TRUE),
(1, 3, 'Capital of Japan?', 'Tokyo is the likely answer', TRUE),
(2, 4, 'Chemical symbol of water?', 'H2O is correct', TRUE),
(1, 4, 'How many continents are there?', 'Usually 7', TRUE),
(2, 5, 'Approximate speed of light?', 'It is around 300000 km/s', TRUE),
(5, 5, 'Main language for Android now?', 'Kotlin is the common choice', TRUE),
(1, 6, 'Highest mountain in the world?', 'Everest', TRUE),
(3, 6, 'Official first mention of Moscow?', '1147', TRUE),
(2, 7, 'Which gas is most in Earth atmosphere?', 'Nitrogen', TRUE),
(2, 8, 'How many minutes in an hour?', '60', TRUE),
(2, 9, 'Which planet is famous for rings?', 'Saturn', TRUE);

INSERT INTO answers (question_id, position_number, answer_text, is_correct) VALUES
(1, 1, 'Paris', TRUE), (1, 2, 'Berlin', FALSE), (1, 3, 'Madrid', FALSE), (1, 4, 'Rome', FALSE),
(2, 1, '3', FALSE), (2, 2, '4', TRUE), (2, 3, '5', FALSE), (2, 4, '22', FALSE),
(3, 1, 'Earth', FALSE), (3, 2, 'Mars', FALSE), (3, 3, 'Jupiter', TRUE), (3, 4, 'Venus', FALSE),
(4, 1, 'Pushkin', FALSE), (4, 2, 'Tolstoy', TRUE), (4, 3, 'Dostoevsky', FALSE), (4, 4, 'Lermontov', FALSE),
(5, 1, 'Indian', FALSE), (5, 2, 'Atlantic', FALSE), (5, 3, 'Arctic', FALSE), (5, 4, 'Pacific', TRUE),
(6, 1, 'Beijing', FALSE), (6, 2, 'Tokyo', TRUE), (6, 3, 'Seoul', FALSE), (6, 4, 'Bangkok', FALSE),
(7, 1, 'H2O', TRUE), (7, 2, 'CO2', FALSE), (7, 3, 'O2', FALSE), (7, 4, 'NaCl', FALSE),
(8, 1, '5', FALSE), (8, 2, '6', FALSE), (8, 3, '7', TRUE), (8, 4, '8', FALSE),
(9, 1, '300000 km/s', TRUE), (9, 2, '150000 km/s', FALSE), (9, 3, '1000 km/s', FALSE), (9, 4, '1000000 km/s', FALSE),
(10, 1, 'Kotlin', TRUE), (10, 2, 'Swift', FALSE), (10, 3, 'Ruby', FALSE), (10, 4, 'Go', FALSE),
(11, 1, 'Elbrus', FALSE), (11, 2, 'Mont Blanc', FALSE), (11, 3, 'Everest', TRUE), (11, 4, 'Kilimanjaro', FALSE),
(12, 1, '1147', TRUE), (12, 2, '987', FALSE), (12, 3, '1812', FALSE), (12, 4, '1917', FALSE),
(13, 1, 'Oxygen', FALSE), (13, 2, 'Nitrogen', TRUE), (13, 3, 'Carbon dioxide', FALSE), (13, 4, 'Argon', FALSE),
(14, 1, '30', FALSE), (14, 2, '45', FALSE), (14, 3, '60', TRUE), (14, 4, '90', FALSE),
(15, 1, 'Mars', FALSE), (15, 2, 'Saturn', TRUE), (15, 3, 'Mercury', FALSE), (15, 4, 'Neptune', FALSE);

INSERT INTO games (player_id, started_at, finished_at, final_money, end_reason) VALUES
(1, CURRENT_TIMESTAMP - INTERVAL '5 days', CURRENT_TIMESTAMP - INTERVAL '5 days' + INTERVAL '8 minutes', 150000, 'WIN'),
(2, CURRENT_TIMESTAMP - INTERVAL '4 days', CURRENT_TIMESTAMP - INTERVAL '4 days' + INTERVAL '5 minutes', 40000, 'TAKE'),
(3, CURRENT_TIMESTAMP - INTERVAL '3 days', CURRENT_TIMESTAMP - INTERVAL '3 days' + INTERVAL '4 minutes', 0, 'WRONG'),
(4, CURRENT_TIMESTAMP - INTERVAL '2 days', CURRENT_TIMESTAMP - INTERVAL '2 days' + INTERVAL '7 minutes', 80000, 'TAKE'),
(5, CURRENT_TIMESTAMP - INTERVAL '1 day', CURRENT_TIMESTAMP - INTERVAL '1 day' + INTERVAL '3 minutes', 5000, 'WRONG');

INSERT INTO game_answers (game_id, round_number, question_id, selected_answer_id, is_correct) VALUES
(1, 1, 1, 1, TRUE),
(1, 2, 2, 6, TRUE),
(1, 3, 3, 11, TRUE),
(2, 1, 1, 1, TRUE),
(3, 1, 4, 13, FALSE),
(4, 1, 7, 25, TRUE),
(5, 1, 10, 37, TRUE);
