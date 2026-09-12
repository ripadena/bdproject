package ru.misha.millionaire.core;

import ru.misha.millionaire.App;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public final class AppPaths {

    private AppPaths() {
    }

    public static Path getAppBaseDir() {
        try {
            URL location = App.class.getProtectionDomain().getCodeSource().getLocation();
            Path path = Paths.get(location.toURI()).toAbsolutePath().normalize();
            if (Files.isRegularFile(path) && path.toString().endsWith(".jar")) {
                return path.getParent();
            }
            if (Files.isDirectory(path)) {
                Path current = path;
                for (int i = 0; i < 6; i++) {
                    if (Files.exists(current.resolve("pom.xml"))) {
                        return current;
                    }
                    Path parent = current.getParent();
                    if (parent == null) {
                        break;
                    }
                    current = parent;
                }
                return path;
            }
        } catch (URISyntaxException | RuntimeException ignored) {
            return Paths.get(System.getProperty("user.dir")).toAbsolutePath().normalize();
        }
        return Paths.get(System.getProperty("user.dir")).toAbsolutePath().normalize();
    }

    public static Path getDataDir() {
        Path dataDir = getAppBaseDir().resolve("data").toAbsolutePath().normalize();
        try {
            Files.createDirectories(dataDir);
        } catch (IOException e) {
            throw new IllegalStateException("Failed to create data directory: " + e.getMessage(), e);
        }
        return dataDir;
    }

    public static Path getQuestionsPath() {
        return getDataDir().resolve("questions.txt");
    }

    public static Path getScoresPath() {
        return getDataDir().resolve("scores.csv");
    }
}