package ru.misha.millionaire.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public final class DatabaseConnectionFactory {
    private static final DatabaseConfig CONFIG = DatabaseConfig.load();

    private DatabaseConnectionFactory() {
    }

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(CONFIG.getUrl(), CONFIG.getUser(), CONFIG.getPassword());
    }

    public static DatabaseConfig getConfig() {
        return CONFIG;
    }
}
