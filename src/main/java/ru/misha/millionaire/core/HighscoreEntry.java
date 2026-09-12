package ru.misha.millionaire.core;

import java.time.LocalDateTime;

public class HighscoreEntry implements Comparable<HighscoreEntry> {
    private final String name;
    private final int money;
    private final LocalDateTime timestamp;

    public HighscoreEntry(String name, int money, LocalDateTime timestamp) {
        this.name = name;
        this.money = money;
        this.timestamp = timestamp;
    }

    public String getName() {
        return name;
    }

    public int getMoney() {
        return money;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    @Override
    public int compareTo(HighscoreEntry other) {
        int moneyCompare = Integer.compare(other.money, this.money);
        if (moneyCompare != 0) {
            return moneyCompare;
        }
        return other.timestamp.compareTo(this.timestamp);
    }
}
