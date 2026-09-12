package ru.misha.millionaire.core;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class HighscoreTable {

    public List<HighscoreEntry> load(Path path) {
        List<HighscoreEntry> list = new ArrayList<>();
        if (!Files.exists(path)) {
            return list;
        }
        try {
            List<String> lines = Files.readAllLines(path, StandardCharsets.UTF_8);
            for (String line : lines) {
                if (line == null || line.trim().isEmpty()) {
                    continue;
                }
                String[] parts = line.split(";", 3);
                if (parts.length != 3) {
                    continue;
                }
                String name = parts[0].trim();
                int money = Integer.parseInt(parts[1].trim());
                LocalDateTime timestamp = LocalDateTime.parse(parts[2].trim());
                list.add(new HighscoreEntry(name, money, timestamp));
            }
        } catch (IOException | RuntimeException ignored) {
            return new ArrayList<>();
        }
        Collections.sort(list);
        if (list.size() > 10) {
            return new ArrayList<>(list.subList(0, 10));
        }
        return list;
    }

    public void save(Path path, List<HighscoreEntry> list) {
        try {
            Files.createDirectories(path.getParent());
            List<String> lines = new ArrayList<>();
            for (HighscoreEntry entry : list) {
                lines.add(entry.getName() + ";" + entry.getMoney() + ";" + entry.getTimestamp());
            }
            Files.write(path, lines, StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new IllegalArgumentException("Failed to save highscores: " + e.getMessage(), e);
        }
    }

    public List<HighscoreEntry> addAndKeepTop10(List<HighscoreEntry> list, HighscoreEntry entry) {
        List<HighscoreEntry> copy = new ArrayList<>(list);
        copy.add(entry);
        Collections.sort(copy);
        if (copy.size() > 10) {
            return new ArrayList<>(copy.subList(0, 10));
        }
        return copy;
    }

    public List<HighscoreEntry> addAndKeepTop10(List<HighscoreEntry> list, String name, int money) {
        return addAndKeepTop10(list, new HighscoreEntry(name, money, LocalDateTime.now()));
    }
}