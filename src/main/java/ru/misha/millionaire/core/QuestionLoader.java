package ru.misha.millionaire.core;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public final class QuestionLoader {

    private QuestionLoader() {
    }

    public static List<Question> load(Path path) {
        try {
            List<String> rawLines = Files.readAllLines(path, StandardCharsets.UTF_8);
            List<String> lines = new ArrayList<>();
            for (String line : rawLines) {
                if (line != null && !line.trim().isEmpty()) {
                    lines.add(line.trim());
                }
            }
            if (lines.isEmpty()) {
                throw new IllegalArgumentException("Questions file is empty: " + path);
            }
            if (lines.size() % 6 != 0) {
                throw new IllegalArgumentException("Invalid questions format: total non-empty lines must be multiple of 6");
            }

            List<Question> questions = new ArrayList<>();
            for (int i = 0; i < lines.size(); i += 6) {
                String text = lines.get(i);
                String a = lines.get(i + 1);
                String b = lines.get(i + 2);
                String c = lines.get(i + 3);
                String d = lines.get(i + 4);
                String meta = lines.get(i + 5);

                String[] parts = meta.split("\\|", 2);
                if (parts.length != 2) {
                    throw new IllegalArgumentException("Invalid line 6 format (Correct|FriendHint) for question: " + text);
                }

                String correct = parts[0].trim().toUpperCase();
                String friendHint = parts[1].trim();
                if (text.length() > 200) {
                    throw new IllegalArgumentException("Question text is longer than 200 chars: " + text);
                }
                if (friendHint.length() > 200) {
                    throw new IllegalArgumentException("Friend hint is longer than 200 chars for question: " + text);
                }

                int correctIndex = letterToIndex(correct);
                questions.add(new Question(text, new String[]{a, b, c, d}, correctIndex, friendHint));
            }
            return questions;
        } catch (IOException e) {
            throw new IllegalArgumentException("Cannot read questions file: " + e.getMessage(), e);
        }
    }

    public static List<Question> loadOrCreateDefault(Path path) {
        if (!Files.exists(path)) {
            createDefaultQuestions(path);
        }
        return load(path);
    }

    public static void createDefaultQuestions(Path path) {
        try {
            Files.createDirectories(path.getParent());
            Files.writeString(path, defaultQuestionsText(), StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new IllegalArgumentException("Cannot create default questions file: " + e.getMessage(), e);
        }
    }

    private static int letterToIndex(String letter) {
        return switch (letter) {
            case "A" -> 0;
            case "B" -> 1;
            case "C" -> 2;
            case "D" -> 3;
            default -> throw new IllegalArgumentException("Invalid correct answer letter: " + letter);
        };
    }

    private static String defaultQuestionsText() {
        return String.join("\n",
                "Capital of France?",
                "Paris",
                "Berlin",
                "Madrid",
                "Rome",
                "A|Friend is almost sure it is Paris",
                "",
                "How much is 2 + 2?",
                "3",
                "4",
                "5",
                "22",
                "B|Simple math says B",
                "",
                "Largest planet in Solar System?",
                "Earth",
                "Mars",
                "Jupiter",
                "Venus",
                "C|It should be Jupiter",
                "",
                "Author of 'War and Peace'?",
                "Pushkin",
                "Tolstoy",
                "Dostoevsky",
                "Lermontov",
                "B|Friend remembers Tolstoy",
                "",
                "Largest ocean?",
                "Indian",
                "Atlantic",
                "Arctic",
                "Pacific",
                "D|Pacific seems right",
                "",
                "Capital of Japan?",
                "Beijing",
                "Tokyo",
                "Seoul",
                "Bangkok",
                "B|Tokyo is the likely answer",
                "",
                "Chemical symbol of water?",
                "H2O",
                "CO2",
                "O2",
                "NaCl",
                "A|H2O is correct",
                "",
                "How many continents are there?",
                "5",
                "6",
                "7",
                "8",
                "C|Usually 7",
                "",
                "Approximate speed of light?",
                "300000 km/s",
                "150000 km/s",
                "1000 km/s",
                "1000000 km/s",
                "A|It is around 300000 km/s",
                "",
                "Main language for Android now?",
                "Kotlin",
                "Swift",
                "Ruby",
                "Go",
                "A|Kotlin is the common choice",
                "",
                "Highest mountain in the world?",
                "Elbrus",
                "Mont Blanc",
                "Everest",
                "Kilimanjaro",
                "C|Everest",
                "",
                "Official first mention of Moscow?",
                "1147",
                "987",
                "1812",
                "1917",
                "A|1147",
                "",
                "Which gas is most in Earth's atmosphere?",
                "Oxygen",
                "Nitrogen",
                "Carbon dioxide",
                "Argon",
                "B|Nitrogen",
                "",
                "How many minutes in an hour?",
                "30",
                "45",
                "60",
                "90",
                "C|60",
                "",
                "Which planet is famous for rings?",
                "Mars",
                "Saturn",
                "Mercury",
                "Neptune",
                "B|Saturn"
        ) + "\n";
    }
}