package ru.misha.millionaire.ui;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import ru.misha.millionaire.App;
import ru.misha.millionaire.core.HighscoreEntry;
import ru.misha.millionaire.db.DatabaseException;
import ru.misha.millionaire.repository.HighscoreRepository;

import java.io.IOException;
import java.util.List;

public class LeaderboardController {

    @FXML
    private VBox scoresBox;

    @FXML
    private Label messageLabel;

    private final HighscoreRepository highscoreRepository = new HighscoreRepository();

    @FXML
    private void initialize() {
        messageLabel.setText("");
        try {
            List<HighscoreEntry> highscores = highscoreRepository.findTop10();
            if (highscores.isEmpty()) {
                messageLabel.setText("No completed games yet.");
                return;
            }

            int index = 1;
            for (HighscoreEntry entry : highscores) {
                Label label = new Label(index + ". " + entry.getName() + " - " + entry.getMoney()
                        + " (" + entry.getTimestamp() + ")");
                label.getStyleClass().add("score-row");
                scoresBox.getChildren().add(label);
                index++;
            }
        } catch (DatabaseException | IllegalStateException e) {
            messageLabel.setText("Database error: " + e.getMessage());
        }
    }

    @FXML
    private void onBack() throws IOException {
        App.setRoot("start");
    }
}
