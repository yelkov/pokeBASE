package edu.badpals.pokebase.controller;

import edu.badpals.pokebase.auth.LogInManager;
import edu.badpals.pokebase.model.AccesoBD;
import edu.badpals.pokebase.model.PokemonBD;
import edu.badpals.pokebase.model.RutaBD;
import edu.badpals.pokebase.service.ErrorLogger;
import edu.badpals.pokebase.view.View;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.io.IOException;
import java.sql.SQLIntegrityConstraintViolationException;

/**
 * Controlador para la vista de inicio de sesión y registro.
 * Gestiona la autenticación de usuarios y genera la conexión a la base de datos de la aplicación
 * si se realiza exitosamente.
 */
public class ControllerLogIn {

    /**
     * Objetos que representan los elementos de la interfaz que hay que controlar por código
     */
    @FXML
    private ImageView logo;
    @FXML
    private TextField txtUsuario;
    @FXML
    private PasswordField txtContrasena;

    /**
     * Gestor de autenticación para manejar el inicio de sesión y registro de usuarios.
     */
    private LogInManager manager = new LogInManager();

    /**
     * Inicializa el controlador al cargar la vista.
     * Configura el logo y establece la conexión inicial a la base de datos de usuarios.
     */
    public void initialize() {
        logo.setImage(new Image(getClass().getResource("/images/logo.png").toExternalForm()));
        manager.connect();
    }

    /**
     * Maneja el proceso de inicio de sesión de un usuario.
     * Verifica las credenciales ingresadas y, si son correctas, navega a la vista principal.
     * Si las credenciales no son válidas, muestra un mensaje de error.
     *
     * @param event el evento generado al pulsar el botón de inicio de sesión.
     */
    public void login(ActionEvent event) {
        String user = txtUsuario.getText();
        String pass = txtContrasena.getText();
        boolean isLoginOk = manager.authenticate(user, pass);
        if (isLoginOk) {
            enterMain(event);
        } else {
            View.lanzarMensajeError("Error", "Autentificación no válida", "El usuario o contraseña no son correctos");
        }
        limpiar();
    }

    /**
     * Maneja el proceso de registro de un nuevo usuario.
     * Intenta registrar al usuario en la base de datos y, si es exitoso, navega a la vista principal.
     * Si el registro falla por un usuario ya existente, o por otro error, muestra el mensaje correspondiente.
     *
     * @param event el evento generado al pulsar el botón de registro.
     */
    public void registrar(ActionEvent event) {
        String user = txtUsuario.getText();
        String pass = txtContrasena.getText();
        try {
            boolean isRegisterOk = manager.signUp(user, pass);
            if (isRegisterOk) {
                enterMain(event);
            } else {
                View.lanzarMensajeError("Error", "Registro no válido", "No se ha podido registrar al usuario en la base de datos, consulte el login para más información");
            }
        } catch (SQLIntegrityConstraintViolationException e) {
            View.lanzarMensajeError("Error", "Registro no válido", "El usuario ya se halla registrado en la aplicación");
        } finally {
            limpiar();
        }
    }

    /**
     * Navega a la vista principal de la aplicación tras un inicio de sesión o registro exitoso.
     * Establece las conexiones con la base de datos necesarias para acceder a Pokémon y rutas
     * y las envía al SceneManager, encargado de compartirlas con el resto de controladores.
     *
     * @param event el evento generado al completar la autenticación o registro.
     */
    public void enterMain(ActionEvent event) {
        manager.desconectarBD();
        AccesoBD accesoBD = new AccesoBD();
        accesoBD.connect();
        PokemonBD pokemonBD = new PokemonBD(accesoBD);
        RutaBD rutaBD = new RutaBD(accesoBD);

        SceneManager.setAccesoBD(accesoBD);
        SceneManager.setRutaBD(rutaBD);
        SceneManager.setPokemonBD(pokemonBD);
        SceneManager.volverAlInicio(event, this.getClass());
    }

    /**
     * Limpia los campos de texto del formulario.
     * Se utiliza tras completar una acción o al ocurrir un error.
     */
    private void limpiar() {
        txtUsuario.clear();
        txtContrasena.clear();
    }
}
