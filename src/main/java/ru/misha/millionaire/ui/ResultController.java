package ru.misha.millionaire.ui;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import ru.misha.millionaire.App;
import ru.misha.millionaire.core.AppState;
import ru.misha.millionaire.core.HighscoreEntry;

import java.io.IOException;

public class ResultController {

    @FXML
    private Label resultLabel;

    @FXML
    private VBox scoresBox;

    @FXML
    private void initialize() {
        AppState state = App.getState();
        String reasonText = switch (state.getResultReason()) {
            case "WIN" -> "Win";
            case "TAKE" -> "Took money";
            case "WRONG" -> "Wrong answer";
            default -> "Finished";
        };

        resultLabel.setText("Player: " + state.getResultName() + "\n"
                + "Final amount: " + state.getResultMoney() + "\n"
                + "Reason: " + reasonText);

        scoresBox.getChildren().clear();
        int index = 1;
        for (HighscoreEntry entry : state.getHighscores()) {
            Label label = new Label(index + ". " + entry.getName() + " - " + entry.getMoney() + " (" + entry.getTimestamp() + ")");
            scoresBox.getChildren().add(label);
            index++;
        }
    }

    @FXML
    private void onPlayAgain() throws IOException {
        App.setRoot("start");
    }
}