package ru.misha.millionaire.core;

import java.util.Arrays;

public final class Question {
    private final Long id;
    private final String text;
    private final String[] options;
    private final int correctIndex;
    private final String friendHint;

    public Question(String text, String[] options, int correctIndex, String friendHint) {
        this(null, text, options, correctIndex, friendHint);
    }

    public Question(Long id, String text, String[] options, int correctIndex, String friendHint) {
        if (text == null || text.trim().isEmpty()) {
            throw new IllegalArgumentException("Question text is empty");
        }
        if (text.length() > 200) {
            throw new IllegalArgumentException("Question text is too long");
        }
        if (options == null || options.length != 4) {
            throw new IllegalArgumentException("Question must have 4 options");
        }
        for (String option : options) {
            if (option == null || option.trim().isEmpty()) {
                throw new IllegalArgumentException("Option is empty");
            }
        }
        if (correctIndex < 0 || correctIndex > 3) {
            throw new IllegalArgumentException("Correct index out of range");
        }
        this.text = text;
        this.options = Arrays.copyOf(options, options.length);
        this.correctIndex = correctIndex;
        String hint = friendHint == null ? "" : friendHint;
        if (hint.length() > 200) {
            throw new IllegalArgumentException("Friend hint is too long");
        }
        this.id = id;
        this.friendHint = hint;
    }

    public Long getId() {
        return id;
    }

    public String getText() {
        return text;
    }

    public String[] getOptions() {
        return Arrays.copyOf(options, options.length);
    }

    public String getOption(int index) {
        return options[index];
    }

    public int getCorrectIndex() {
        return correctIndex;
    }

    public String getFriendHint() {
        return friendHint;
    }

    public boolean isCorrect(int index) {
        return index == correctIndex;
    }
}
