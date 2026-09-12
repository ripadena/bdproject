package ru.misha.millionaire.repository;

import ru.misha.millionaire.core.Question;
import ru.misha.millionaire.db.DatabaseConnectionFactory;
import ru.misha.millionaire.db.DatabaseException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class GameRepository {

    public long createGame(long playerId) {
        String sql = "INSERT INTO games (player_id) VALUES (?)";
        try (Connection connection = DatabaseConnectionFactory.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            statement.setLong(1, playerId);
            statement.executeUpdate();
            try (ResultSet keys = statement.getGeneratedKeys()) {
                if (keys.next()) {
                    return keys.getLong(1);
                }
            }
            throw new DatabaseException("Database did not return game id", null);
        } catch (SQLException e) {
            throw new DatabaseException("Cannot create game", e);
        }
    }

    public void saveAnswer(long gameId, int roundNumber, Question question, int selectedIndex, boolean correct) {
        Long questionId = question.getId();
        if (questionId == null) {
            throw new DatabaseException("Question id is required to save game answer", null);
        }
        String sql = """
                INSERT INTO game_answers
                    (game_id, round_number, question_id, selected_answer_id, is_correct)
                VALUES (
                    ?, ?, ?,
                    (SELECT answer_id FROM answers WHERE question_id = ? AND position_number = ?),
                    ?
                )
                """;
        try (Connection connection = DatabaseConnectionFactory.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setLong(1, gameId);
            statement.setInt(2, roundNumber);
            statement.setLong(3, questionId);
            statement.setLong(4, questionId);
            statement.setInt(5, selectedIndex + 1);
            statement.setBoolean(6, correct);
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new DatabaseException("Cannot save game answer", e);
        }
    }

    public void finishGame(long gameId, int finalMoney, String endReason) {
        String sql = """
                UPDATE games
                SET finished_at = CURRENT_TIMESTAMP,
                    final_money = ?,
                    end_reason = ?
                WHERE game_id = ?
                """;
        try (Connection connection = DatabaseConnectionFactory.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, finalMoney);
            statement.setString(2, endReason);
            statement.setLong(3, gameId);
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new DatabaseException("Cannot finish game", e);
        }
    }
}
