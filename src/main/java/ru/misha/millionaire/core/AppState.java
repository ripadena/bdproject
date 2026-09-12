package ru.misha.millionaire.core;

import ru.misha.millionaire.core.GameEngine;
import ru.misha.millionaire.core.HighscoreEntry;
import ru.misha.millionaire.core.HighscoreTable;

import java.util.ArrayList;
import java.util.List;

public class AppState {
    private GameEngine engine;
    private final HighscoreTable highscoreTable = new HighscoreTable();
    private List<HighscoreEntry> highscores = new ArrayList<>();
    private Long currentGameId;

    private String resultName;
    private int resultMoney;
    private String resultReason;

    public GameEngine getEngine() {
        return engine;
    }

    public void setEngine(GameEngine engine) {
        this.engine = engine;
    }

    public HighscoreTable getHighscoreTable() {
        return highscoreTable;
    }

    public List<HighscoreEntry> getHighscores() {
        return highscores;
    }

    public void setHighscores(List<HighscoreEntry> highscores) {
        this.highscores = highscores;
    }

    public Long getCurrentGameId() {
        return currentGameId;
    }

    public void setCurrentGameId(Long currentGameId) {
        this.currentGameId = currentGameId;
    }

    public String getResultName() {
        return resultName;
    }

    public void setResultName(String resultName) {
        this.resultName = resultName;
    }

    public int getResultMoney() {
        return resultMoney;
    }

    public void setResultMoney(int resultMoney) {
        this.resultMoney = resultMoney;
    }

    public String getResultReason() {
        return resultReason;
    }

    public void setResultReason(String resultReason) {
        this.resultReason = resultReason;
    }
}
