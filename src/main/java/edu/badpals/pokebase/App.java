package edu.badpals.pokebase;

import edu.badpals.pokebase.controller.SceneManager;
import edu.badpals.pokebase.model.AccesoBD;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Connection;

/**
 * Clase principal de la aplicación, que extiende {@link Application} de JavaFX.
 * Se encarga de configurar y mostrar la ventana principal de la aplicación,
 * incluyendo la carga de la interfaz de usuario, la configuración de la escena y el manejo del cierre de la aplicación.
 */
public class App extends Application {

    /**
     * Se encarga de cargar el archivo FXML de la interfaz de usuario, configurar la escena y
     * establecer las propiedades de la ventana principal (como el título y el ícono).
     * También define el comportamiento a seguir cuando la ventana se cierra, desconectando la base de datos.
     *
     * @param stage El {@link Stage} principal de la aplicación.
     * @throws IOException Si ocurre un error al cargar el archivo FXML o al acceder a los recursos.
     */
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource("controller/login.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 800, 600);
        stage.setTitle("Pokemon World");
        Image icono = new Image(String.valueOf(getClass().getResource("/images/icono.png")));
        stage.getIcons().add(icono);
        stage.setScene(scene);

        stage.setOnCloseRequest(event -> {
            AccesoBD accesoBD = SceneManager.getAccesoBD();
            if (accesoBD != null) {
                accesoBD.desconectarBD();
            }
        });

        stage.show();
    }

    /**
     * Método main, que llama al método {@link Application#launch} para iniciar la aplicación JavaFX.
     *
     * @param args Los argumentos de línea de comandos.
     */
    public static void main(String[] args) {
        launch();
    }
}