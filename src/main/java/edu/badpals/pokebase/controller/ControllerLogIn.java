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

import java.io.IOException;
import java.sql.SQLIntegrityConstraintViolationException;

public class ControllerLogIn {
    @FXML
    private TextField txtUsuario;

    @FXML
    private PasswordField txtContrasena;

    private LogInManager manager = new LogInManager();

    public void initialize(){
        manager.connect();
    }

    public void login(ActionEvent event){
        String user = txtUsuario.getText();
        String pass = txtContrasena.getText();
        boolean isLoginOk = manager.authenticate(user, pass);
        if(isLoginOk){
            enterMain(event);
        } else {
            View.lanzarMensajeError("Error", "Autentificación no válida", "El usuario o contraseña no son correctos");
        }
        limpiar();
    }

    public void registrar(ActionEvent event){
        String user = txtUsuario.getText();
        String pass = txtContrasena.getText();
        try {
            boolean isRegisterOk = manager.signUp(user, pass);
            if (isRegisterOk) {
                    enterMain(event);
            } else {
                View.lanzarMensajeError("Error", "Registro no válido", "No se ha podido registrar al usuario en la base de datos, consulte el login para más información");
            }
        } catch (SQLIntegrityConstraintViolationException e){
            View.lanzarMensajeError("Error", "Registro no válido", "El usuario ya se halla registrado en la aplicación");
        } finally {
            limpiar();
        }
    }

    public void enterMain(ActionEvent event){
        AccesoBD accesoBD = new AccesoBD();
        accesoBD.connect();
        PokemonBD pokemonBD = new PokemonBD(accesoBD);
        RutaBD rutaBD = new RutaBD(accesoBD);

        SceneManager.setAccesoBD(accesoBD);
        SceneManager.setRutaBD(rutaBD);
        SceneManager.setPokemonBD(pokemonBD);
        SceneManager.goToView(event, "main.fxml", this.getClass(), 800, 600);
    }

    private void limpiar(){
        txtUsuario.clear();
        txtContrasena.clear();
    }

}
