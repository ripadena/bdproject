package ru.misha.millionaire.db;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Locale;
import java.util.Properties;

public final class DatabaseConfig {
    private final String type;
    private final String url;
    private final String user;
    private final String password;

    private DatabaseConfig(String type, String url, String user, String password) {
        this.type = type;
        this.url = url;
        this.user = user;
        this.password = password;
    }

    public static DatabaseConfig load() {
        Properties properties = new Properties();
        String externalConfig = System.getProperty("millionaire.db.config");
        if (externalConfig != null && !externalConfig.trim().isEmpty()) {
            loadFromFile(properties, Path.of(externalConfig.trim()));
        } else {
            loadFromClasspath(properties);
        }

        String type = required(properties, "db.type").toLowerCase(Locale.ROOT);
        if (!type.equals("postgres") && !type.equals("mysql")) {
            throw new IllegalStateException("Unsupported db.type: " + type);
        }
        return new DatabaseConfig(
                type,
                required(properties, "db.url"),
                required(properties, "db.user"),
                required(properties, "db.password")
        );
    }

    private static void loadFromFile(Properties properties, Path path) {
        try (InputStream input = Files.newInputStream(path.toAbsolutePath().normalize())) {
            properties.load(input);
        } catch (IOException e) {
            throw new IllegalStateException("Cannot read database config file: " + e.getMessage(), e);
        }
    }

    private static void loadFromClasspath(Properties properties) {
        try (InputStream input = DatabaseConfig.class.getResourceAsStream("/db.properties")) {
            if (input == null) {
                throw new IllegalStateException("db.properties not found in resources");
            }
            properties.load(input);
        } catch (IOException e) {
            throw new IllegalStateException("Cannot read db.properties: " + e.getMessage(), e);
        }
    }

    private static String required(Properties properties, String key) {
        String value = properties.getProperty(key);
        if (value == null || value.trim().isEmpty()) {
            throw new IllegalStateException("Missing required database setting: " + key);
        }
        return value.trim();
    }

    public String getType() {
        return type;
    }

    public String getUrl() {
        return url;
    }

    public String getUser() {
        return user;
    }

    public String getPassword() {
        return password;
    }
}
