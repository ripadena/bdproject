package ru.misha.millionaire.core;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class QuestionLoaderTest {

    @Test
    void loadsQuestionsFromLegacyDataFile() {
        Path path = Paths.get("data", "questions.txt");
        assertTrue(Files.exists(path));
        List<Question> list = QuestionLoader.load(path);
        assertTrue(list.size() >= 10);
    }

    @Test
    void supportsUtf8Content(@TempDir Path tempDir) throws Exception {
        Path file = tempDir.resolve("questions.txt");
        String content = String.join("\n",
                "Кто написал Евгений Онегин?",
                "Пушкин",
                "Толстой",
                "Гоголь",
                "Достоевский",
                "A|Это классика",
                "",
                "Сколько будет 3+3?",
                "5",
                "6",
                "7",
                "8",
                "B|Базовая математика"
        ) + "\n";
        Files.writeString(file, content, StandardCharsets.UTF_8);

        List<Question> list = QuestionLoader.load(file);
        assertEquals(2, list.size());
        assertEquals("Кто написал Евгений Онегин?", list.get(0).getText());
        assertEquals("Пушкин", list.get(0).getOption(0));
    }

    @Test
    void createsDefaultWhenMissing(@TempDir Path tempDir) {
        Path file = tempDir.resolve("questions.txt");
        List<Question> list = QuestionLoader.loadOrCreateDefault(file);
        assertTrue(Files.exists(file));
        assertTrue(list.size() >= 15);
    }

    @Test
    void invalidFormatThrowsMeaningfulError(@TempDir Path tempDir) throws Exception {
        Path file = tempDir.resolve("bad.txt");
        String content = String.join("\n",
                "Q1",
                "A1",
                "B1",
                "C1",
                "D1"
        ) + "\n";
        Files.writeString(file, content, StandardCharsets.UTF_8);

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> QuestionLoader.load(file));
        assertTrue(ex.getMessage().toLowerCase().contains("format"));
    }

    @Test
    void tooLongQuestionOrHintFails(@TempDir Path tempDir) throws Exception {
        Path file = tempDir.resolve("long.txt");
        String longText = "A".repeat(201);
        String content = String.join("\n",
                longText,
                "A1",
                "B1",
                "C1",
                "D1",
                "A|Short hint"
        ) + "\n";
        Files.writeString(file, content, StandardCharsets.UTF_8);

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> QuestionLoader.load(file));
        assertTrue(ex.getMessage().contains("200"));

        String contentHint = String.join("\n",
                "Q1",
                "A1",
                "B1",
                "C1",
                "D1",
                "A|" + longText
        ) + "\n";
        Files.writeString(file, contentHint, StandardCharsets.UTF_8);

        IllegalArgumentException ex2 = assertThrows(IllegalArgumentException.class, () -> QuestionLoader.load(file));
        assertTrue(ex2.getMessage().contains("200"));
    }
}
