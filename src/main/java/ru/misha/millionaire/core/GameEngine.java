package ru.misha.millionaire.core;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class GameEngine {
    private static final int TOTAL_ROUNDS = 10;
    private static final int[] LADDER = {500, 1000, 2000, 3000, 5000, 10000, 20000, 40000, 80000, 150000};

    private String playerName;
    private List<Question> questions = new ArrayList<>();
    private int roundIndex;
    private int currentMoney;
    private boolean gameOver;
    private String endReason;

    private boolean fiftyUsed;
    private boolean audienceUsed;
    private boolean friendUsed;

    private final LifelineService lifelineService = new LifelineService();

    public void startGame(String playerName, List<Question> allQuestions) {
        String normalizedName = validatePlayerName(playerName);
        if (allQuestions == null || allQuestions.size() < TOTAL_ROUNDS) {
            throw new IllegalArgumentException("At least 10 questions are required");
        }

        this.playerName = normalizedName;
        List<Question> copy = new ArrayList<>(allQuestions);
        Collections.shuffle(copy);
        this.questions = new ArrayList<>(copy.subList(0, TOTAL_ROUNDS));
        this.roundIndex = 0;
        this.currentMoney = 0;
        this.gameOver = false;
        this.endReason = null;
        this.fiftyUsed = false;
        this.audienceUsed = false;
        this.friendUsed = false;
    }

    public static String validatePlayerName(String name) {
        if (name == null) {
            throw new IllegalArgumentException("Player name is required");
        }
        String trimmed = name.trim().replaceAll("\\s+", " ");
        if (trimmed.isEmpty()) {
            throw new IllegalArgumentException("Player name is required");
        }
        if (trimmed.length() > 20) {
            throw new IllegalArgumentException("Player name must be <= 20 characters");
        }
        if (!trimmed.matches("[\\p{L}\\p{Nd} ]+")) {
            throw new IllegalArgumentException("Player name may contain only letters, digits and spaces");
        }
        return trimmed;
    }

    public String getPlayerName() {
        return playerName;
    }

    public Question getCurrentQuestion() {
        if (roundIndex < 0 || roundIndex >= questions.size()) {
            return null;
        }
        return questions.get(roundIndex);
    }

    public int getRoundNumber() {
        return roundIndex + 1;
    }

    public int getAnsweredCount() {
        return roundIndex;
    }

    public int getCurrentMoney() {
        return currentMoney;
    }

    public boolean answer(int optionIndex) {
        if (gameOver) {
            return false;
        }
        Question question = getCurrentQuestion();
        if (question == null) {
            return false;
        }

        if (question.isCorrect(optionIndex)) {
            currentMoney = LADDER[roundIndex];
            roundIndex++;
            if (roundIndex >= TOTAL_ROUNDS) {
                gameOver = true;
                endReason = "WIN";
            }
            return true;
        }

        currentMoney = 0;
        gameOver = true;
        endReason = "WRONG";
        return false;
    }

    public int takeMoney() {
        if (!gameOver) {
            gameOver = true;
            endReason = "TAKE";
        }
        return currentMoney;
    }

    public boolean isGameOver() {
        return gameOver;
    }

    public String getEndReason() {
        return endReason;
    }

    public boolean canUseFiftyFifty() {
        return !fiftyUsed && !gameOver;
    }

    public int[] useFiftyFifty() {
        if (!canUseFiftyFifty()) {
            return new int[0];
        }
        fiftyUsed = true;
        return lifelineService.fiftyFifty(getCurrentQuestion());
    }

    public boolean canUseAudience() {
        return !audienceUsed && !gameOver;
    }

    public int[] useAudiencePoll() {
        if (!canUseAudience()) {
            return new int[0];
        }
        audienceUsed = true;
        return lifelineService.audiencePoll(getCurrentQuestion());
    }

    public boolean canUseFriend() {
        return !friendUsed && !gameOver;
    }

    public String useFriendCall() {
        if (!canUseFriend()) {
            return "";
        }
        friendUsed = true;
        return lifelineService.friendCall(getCurrentQuestion());
    }
}
