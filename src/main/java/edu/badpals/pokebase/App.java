package edu.badpals.pokebase;

import edu.badpals.pokebase.controller.Controller;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class App extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource("main.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 800, 600);
        Controller controller = fxmlLoader.getController();
        controller.setPrimaryStage(stage);
        stage.setTitle("Pokemon World");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}