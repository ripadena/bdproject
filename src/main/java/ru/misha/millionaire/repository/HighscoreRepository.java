package ru.misha.millionaire.repository;

import ru.misha.millionaire.core.HighscoreEntry;
import ru.misha.millionaire.db.DatabaseConnectionFactory;
import ru.misha.millionaire.db.DatabaseException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class HighscoreRepository {

    public List<HighscoreEntry> findTop10() {
        String sql = """
                SELECT p.player_name, g.final_money, g.finished_at
                FROM games g
                JOIN players p ON p.player_id = g.player_id
                WHERE g.finished_at IS NOT NULL
                ORDER BY g.final_money DESC, g.finished_at ASC
                LIMIT 10
                """;
        List<HighscoreEntry> highscores = new ArrayList<>();
        try (Connection connection = DatabaseConnectionFactory.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                Timestamp timestamp = resultSet.getTimestamp("finished_at");
                LocalDateTime finishedAt = timestamp == null ? LocalDateTime.now() : timestamp.toLocalDateTime();
                highscores.add(new HighscoreEntry(
                        resultSet.getString("player_name"),
                        resultSet.getInt("final_money"),
                        finishedAt
                ));
            }
            return highscores;
        } catch (SQLException e) {
            throw new DatabaseException("Cannot load leaderboard", e);
        }
    }
}
