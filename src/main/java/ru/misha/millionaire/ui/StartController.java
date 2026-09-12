package ru.misha.millionaire.ui;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import ru.misha.millionaire.App;
import ru.misha.millionaire.core.GameEngine;
import ru.misha.millionaire.core.Question;
import ru.misha.millionaire.db.DatabaseException;
import ru.misha.millionaire.repository.GameRepository;
import ru.misha.millionaire.repository.HighscoreRepository;
import ru.misha.millionaire.repository.PlayerRepository;
import ru.misha.millionaire.repository.QuestionRepository;

import java.awt.Desktop;
import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

public class StartController {

    @FXML
    private TextField nameField;

    @FXML
    private Label errorLabel;

    private final QuestionRepository questionRepository = new QuestionRepository();
    private final PlayerRepository playerRepository = new PlayerRepository();
    private final GameRepository gameRepository = new GameRepository();
    private final HighscoreRepository highscoreRepository = new HighscoreRepository();

    @FXML
    private void initialize() {
        errorLabel.setText("");
    }

    @FXML
    private void onStart() {
        String name = nameField.getText() == null ? "" : nameField.getText();
        try {
            String playerName = GameEngine.validatePlayerName(name);
            List<Question> questions = questionRepository.findActiveQuestions();
            if (questions.size() < 10) {
                showError("At least 10 active questions are required in database. Current count: " + questions.size());
                return;
            }

            GameEngine engine = new GameEngine();
            engine.startGame(name, questions);
            long playerId = playerRepository.getOrCreatePlayer(playerName);
            long gameId = gameRepository.createGame(playerId);
            App.getState().setEngine(engine);
            App.getState().setCurrentGameId(gameId);
            App.getState().setHighscores(highscoreRepository.findTop10());
            App.setRoot("quiz");
        } catch (IllegalArgumentException e) {
            showError(e.getMessage());
        } catch (DatabaseException | IllegalStateException e) {
            showError("Database error: " + e.getMessage());
        } catch (IOException e) {
            showError("I/O error: " + e.getMessage());
        }
    }

    @FXML
    private void onOpenData() {
        openProjectFolder("database");
    }

    @FXML
    private void onCreateSample() {
        showInfo("Test questions are now loaded with SQL scripts from the database folder.");
    }

    private void openProjectFolder(String folderName) {
        try {
            Path folder = Path.of(System.getProperty("user.dir")).resolve(folderName).toAbsolutePath().normalize();
            if (Desktop.isDesktopSupported()) {
                Desktop.getDesktop().open(folder.toFile());
            } else {
                showError("Opening folders is not supported on this system");
            }
        } catch (IOException e) {
            showError("Cannot open project folder: " + e.getMessage());
        }
    }

    private void showError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void showInfo(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Done");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
