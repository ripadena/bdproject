package ru.misha.millionaire.repository;

import ru.misha.millionaire.db.DatabaseConnectionFactory;
import ru.misha.millionaire.db.DatabaseException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class PlayerRepository {

    public long getOrCreatePlayer(String name) {
        Long existing = findPlayerId(name);
        if (existing != null) {
            return existing;
        }

        String sql = "INSERT INTO players (player_name) VALUES (?)";
        try (Connection connection = DatabaseConnectionFactory.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            statement.setString(1, name);
            statement.executeUpdate();
            try (ResultSet keys = statement.getGeneratedKeys()) {
                if (keys.next()) {
                    return keys.getLong(1);
                }
            }
            throw new DatabaseException("Database did not return player id", null);
        } catch (SQLException e) {
            throw new DatabaseException("Cannot create player", e);
        }
    }

    private Long findPlayerId(String name) {
        String sql = "SELECT player_id FROM players WHERE player_name = ?";
        try (Connection connection = DatabaseConnectionFactory.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, name);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getLong("player_id");
                }
            }
            return null;
        } catch (SQLException e) {
            throw new DatabaseException("Cannot find player", e);
        }
    }
}
