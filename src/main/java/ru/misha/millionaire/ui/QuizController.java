package ru.misha.millionaire.ui;

import javafx.animation.PauseTransition;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.util.Duration;
import ru.misha.millionaire.App;
import ru.misha.millionaire.core.AppState;
import ru.misha.millionaire.core.GameEngine;
import ru.misha.millionaire.core.Question;
import ru.misha.millionaire.db.DatabaseException;
import ru.misha.millionaire.repository.GameRepository;
import ru.misha.millionaire.repository.HighscoreRepository;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class QuizController {
    private static final Duration NEXT_STEP_DELAY = Duration.seconds(2.0);

    private boolean awaitingTransition;
    private PauseTransition revealTransition;
    private final GameRepository gameRepository = new GameRepository();
    private final HighscoreRepository highscoreRepository = new HighscoreRepository();

    @FXML
    private Label roundLabel;

    @FXML
    private Label moneyLabel;

    @FXML
    private Label questionLabel;

    @FXML
    private Button answerA;

    @FXML
    private Button answerB;

    @FXML
    private Button answerC;

    @FXML
    private Button answerD;

    @FXML
    private Button fiftyButton;

    @FXML
    private Button audienceButton;

    @FXML
    private Button friendButton;

    @FXML
    private Button takeButton;

    @FXML
    private void initialize() {
        awaitingTransition = false;
        revealTransition = new PauseTransition(NEXT_STEP_DELAY);
        renderQuestion();
    }

    private void renderQuestion() {
        GameEngine engine = App.getState().getEngine();
        Question question = engine.getCurrentQuestion();
        if (question == null) {
            return;
        }

        roundLabel.setText("Round " + engine.getRoundNumber() + "/10");
        moneyLabel.setText("Current money: " + engine.getCurrentMoney());
        questionLabel.setText(question.getText());

        String[] options = question.getOptions();
        answerA.setText("A. " + options[0]);
        answerB.setText("B. " + options[1]);
        answerC.setText("C. " + options[2]);
        answerD.setText("D. " + options[3]);

        resetAnswers();
        fiftyButton.setDisable(!engine.canUseFiftyFifty());
        audienceButton.setDisable(!engine.canUseAudience());
        friendButton.setDisable(!engine.canUseFriend());
        takeButton.setDisable(false);
        awaitingTransition = false;
    }

    private List<Button> answerButtons() {
        return Arrays.asList(answerA, answerB, answerC, answerD);
    }

    private void resetAnswers() {
        for (Button button : answerButtons()) {
            button.setDisable(false);
            button.setVisible(true);
            button.getStyleClass().removeAll("answer-correct", "answer-wrong", "answer-disabled");
        }
    }

    @FXML
    private void onAnswerA() {
        handleAnswer(0);
    }

    @FXML
    private void onAnswerB() {
        handleAnswer(1);
    }

    @FXML
    private void onAnswerC() {
        handleAnswer(2);
    }

    @FXML
    private void onAnswerD() {
        handleAnswer(3);
    }

    private void handleAnswer(int index) {
        if (awaitingTransition) {
            return;
        }
        awaitingTransition = true;

        GameEngine engine = App.getState().getEngine();
        Question question = engine.getCurrentQuestion();
        int roundNumber = engine.getRoundNumber();
        boolean correct = engine.answer(index);
        saveAnswer(roundNumber, question, index, correct);

        disableAllInputs();
        Button selected = answerButtons().get(index);
        selected.getStyleClass().add(correct ? "answer-correct" : "answer-wrong");
        for (Button button : answerButtons()) {
            if (button != selected) {
                button.getStyleClass().add("answer-disabled");
            }
        }

        revealTransition.stop();
        revealTransition.setOnFinished(event -> {
            try {
                if (engine.isGameOver()) {
                    Platform.runLater(this::showFinalDialogAndGoToLeaderboard);
                } else {
                    renderQuestion();
                }
            } catch (RuntimeException ex) {
                showError("Failed to complete transition: " + ex.getMessage());
                awaitingTransition = false;
            }
        });
        revealTransition.playFromStart();
    }

    @FXML
    private void onFiftyFifty() {
        if (awaitingTransition) {
            return;
        }
        GameEngine engine = App.getState().getEngine();
        int[] keep = engine.useFiftyFifty();
        if (keep.length == 2) {
            boolean keepA = keep[0] == 0 || keep[1] == 0;
            boolean keepB = keep[0] == 1 || keep[1] == 1;
            boolean keepC = keep[0] == 2 || keep[1] == 2;
            boolean keepD = keep[0] == 3 || keep[1] == 3;
            answerA.setDisable(!keepA);
            answerA.setVisible(keepA);
            answerB.setDisable(!keepB);
            answerB.setVisible(keepB);
            answerC.setDisable(!keepC);
            answerC.setVisible(keepC);
            answerD.setDisable(!keepD);
            answerD.setVisible(keepD);
        }
        fiftyButton.setDisable(true);
    }

    @FXML
    private void onAudience() {
        if (awaitingTransition) {
            return;
        }
        GameEngine engine = App.getState().getEngine();
        int[] percents = engine.useAudiencePoll();
        String text = "A: " + percents[0] + "%\n"
                + "B: " + percents[1] + "%\n"
                + "C: " + percents[2] + "%\n"
                + "D: " + percents[3] + "%";
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Audience help");
        alert.setHeaderText("Voting stats");
        alert.setContentText(text);
        alert.showAndWait();
        audienceButton.setDisable(true);
    }

    @FXML
    private void onFriend() {
        if (awaitingTransition) {
            return;
        }
        GameEngine engine = App.getState().getEngine();
        String hint = engine.useFriendCall();
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Call a friend");
        alert.setHeaderText(null);
        alert.setContentText(hint);
        alert.showAndWait();
        friendButton.setDisable(true);
    }

    @FXML
    private void onTakeMoney() {
        if (awaitingTransition) {
            return;
        }
        awaitingTransition = true;
        if (revealTransition != null) {
            revealTransition.stop();
        }

        GameEngine engine = App.getState().getEngine();
        engine.takeMoney();
        disableAllInputs();
        showFinalDialogAndGoToLeaderboard();
    }

    private void saveAnswer(int roundNumber, Question question, int selectedIndex, boolean correct) {
        Long gameId = App.getState().getCurrentGameId();
        if (gameId == null || question == null) {
            return;
        }
        try {
            gameRepository.saveAnswer(gameId, roundNumber, question, selectedIndex, correct);
        } catch (DatabaseException e) {
            showError("Failed to save answer: " + e.getMessage());
        }
    }

    private void disableAllInputs() {
        for (Button button : answerButtons()) {
            button.setDisable(true);
        }
        fiftyButton.setDisable(true);
        audienceButton.setDisable(true);
        friendButton.setDisable(true);
        takeButton.setDisable(true);
    }

    private void showFinalDialogAndGoToLeaderboard() {
        GameEngine engine = App.getState().getEngine();
        int answered = engine.getAnsweredCount();
        String reason = engine.getEndReason();
        String stoppedText;

        if ("WIN".equals(reason)) {
            stoppedText = "Finished all 10";
        } else {
            int stoppedAt = Math.min(10, answered + 1);
            stoppedText = "Stopped at question: " + stoppedAt;
        }

        Alert summary = new Alert(Alert.AlertType.INFORMATION);
        summary.setTitle("Game summary");
        summary.setHeaderText("Game finished");
        summary.setContentText(
                "Completed: " + answered + "/10\n"
                        + stoppedText + "\n"
                        + "Final balance: " + engine.getCurrentMoney()
        );
        ButtonType nextButton = new ButtonType("Go to leaderboard", ButtonBar.ButtonData.OK_DONE);
        summary.getButtonTypes().setAll(nextButton);
        summary.showAndWait();

        saveAndOpenResultScreen();
    }

    private void saveAndOpenResultScreen() {
        GameEngine engine = App.getState().getEngine();
        int money = engine.getCurrentMoney();
        String reason = engine.getEndReason();

        AppState state = App.getState();
        Long gameId = state.getCurrentGameId();
        if (gameId != null) {
            try {
                gameRepository.finishGame(gameId, money, reason);
                state.setHighscores(highscoreRepository.findTop10());
            } catch (DatabaseException e) {
                showError("Failed to save game result: " + e.getMessage());
            }
        }

        state.setResultName(engine.getPlayerName());
        state.setResultMoney(money);
        state.setResultReason(reason);

        try {
            App.setRoot("result");
        } catch (IOException e) {
            showError("Cannot open result screen: " + e.getMessage());
        }
    }

    private void showError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
