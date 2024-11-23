package edu.badpals.pokebase;

import edu.badpals.pokebase.controller.SceneManager;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Connection;

public class App extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource("controller/login.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 800, 600);
        stage.setTitle("Pokemon World");
        Image icono = new Image(String.valueOf(getClass().getResource("/images/icono.png")));
        stage.getIcons().add(icono);
        stage.setScene(scene);

        stage.setOnCloseRequest(event -> {
            SceneManager.getAccesoBD().desconectarBD();
        });

        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}