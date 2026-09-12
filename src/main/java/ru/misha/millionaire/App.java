package ru.misha.millionaire;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import ru.misha.millionaire.core.AppPaths;
import ru.misha.millionaire.core.AppState;

import java.io.IOException;

public class App {

    private static Scene scene;
    private static final AppState STATE = new AppState();

    public static AppState getState() {
        return STATE;
    }

    public static void setRoot(String fxml) throws IOException {
        scene.setRoot(loadFXML(fxml));
    }

    private static Parent loadFXML(String fxml) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource("/ru/misha/millionaire/ui/" + fxml + ".fxml"));
        return fxmlLoader.load();
    }

    public static void main(String[] args) {
        Application.launch(FxApplication.class, args);
    }

    public static class FxApplication extends Application {
        @Override
        public void start(Stage stage) throws IOException {
            AppPaths.getDataDir();
            scene = new Scene(loadFXML("start"), 900, 640);
            scene.getStylesheets().add(App.class.getResource("/ru/misha/millionaire/ui/app.css").toExternalForm());
            stage.setTitle("Who Wants to Be a Millionaire?");
            stage.setScene(scene);
            stage.show();
        }
    }
}
