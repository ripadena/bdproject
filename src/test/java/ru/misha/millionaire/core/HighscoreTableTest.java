package ru.misha.millionaire.core;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class HighscoreTableTest {

    @Test
    void saveAndLoadCsv(@TempDir Path tempDir) {
        HighscoreTable table = new HighscoreTable();
        Path path = tempDir.resolve("scores.csv");

        List<HighscoreEntry> entries = new ArrayList<>();
        entries.add(new HighscoreEntry("A", 1000, LocalDateTime.now().minusDays(1)));
        entries.add(new HighscoreEntry("B", 2000, LocalDateTime.now()));

        table.save(path, entries);
        assertTrue(Files.exists(path));

        List<HighscoreEntry> loaded = table.load(path);
        assertEquals(2, loaded.size());
        assertEquals("B", loaded.get(0).getName());
    }

    @Test
    void addAndKeepTop10SortsAndCuts() {
        HighscoreTable table = new HighscoreTable();
        List<HighscoreEntry> entries = new ArrayList<>();

        for (int i = 0; i < 12; i++) {
            entries.add(new HighscoreEntry("P" + i, i * 100, LocalDateTime.now().minusMinutes(i)));
        }

        List<HighscoreEntry> top = table.addAndKeepTop10(entries, "Winner", 99999);
        assertEquals(10, top.size());
        assertEquals("Winner", top.get(0).getName());
    }
}