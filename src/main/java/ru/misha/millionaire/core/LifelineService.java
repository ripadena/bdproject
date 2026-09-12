package ru.misha.millionaire.core;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class LifelineService {
    private final Random random = new Random();

    public int[] fiftyFifty(Question question) {
        int correct = question.getCorrectIndex();
        List<Integer> wrong = new ArrayList<>();
        for (int i = 0; i < 4; i++) {
            if (i != correct) {
                wrong.add(i);
            }
        }
        int randomWrong = wrong.get(random.nextInt(wrong.size()));
        int first = Math.min(correct, randomWrong);
        int second = Math.max(correct, randomWrong);
        return new int[]{first, second};
    }

    public int[] audiencePoll(Question question) {
        int correctIndex = question.getCorrectIndex();
        int correct = 45 + random.nextInt(26);
        int remaining = 100 - correct;
        int r1 = random.nextInt(remaining + 1);
        int r2 = random.nextInt(remaining - r1 + 1);
        int r3 = remaining - r1 - r2;

        List<Integer> wrongIdx = new ArrayList<>();
        for (int i = 0; i < 4; i++) {
            if (i != correctIndex) {
                wrongIdx.add(i);
            }
        }

        int[] percents = new int[4];
        percents[correctIndex] = correct;
        percents[wrongIdx.get(0)] = r1;
        percents[wrongIdx.get(1)] = r2;
        percents[wrongIdx.get(2)] = r3;
        return percents;
    }

    public String friendCall(Question question) {
        String hint = question.getFriendHint();
        if (hint == null || hint.isBlank()) {
            return "Friend is unsure, but leans to option " + indexToLetter(question.getCorrectIndex());
        }
        return "Friend says: " + hint;
    }

    private String indexToLetter(int index) {
        return switch (index) {
            case 0 -> "A";
            case 1 -> "B";
            case 2 -> "C";
            case 3 -> "D";
            default -> "?";
        };
    }
}