package edu.badpals.pokebase.controller;

import edu.badpals.pokebase.auth.LogInManager;
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
            try {
                Controller.getFxmlLoader(event, "main.fxml", this.getClass(), 800, 600);
            } catch (IOException e) {
                ErrorLogger.saveErrorLog("Error al cambiar de ventana desde el login al main, " + e.getMessage());
                View.lanzarMensajeError("Error", "Error al cambiar de ventana", "No se pudo cambiar de ventana, consulte el archivo de log para más información");
            }
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
                try {
                    Controller.getFxmlLoader(event, "main.fxml", this.getClass(), 800, 600);
                } catch (IOException e) {
                    ErrorLogger.saveErrorLog("Error al cambiar de ventana desde el login al main, " + e.getMessage());
                    View.lanzarMensajeError("Error", "Error al cambiar de ventana", "No se pudo cambiar de ventana, consulte el archivo de log para más información");

                }
            } else {
                View.lanzarMensajeError("Error", "Registro no válido", "No se ha podido registrar al usuario en la base de datos, consulte el login para más información");
            }
        } catch (SQLIntegrityConstraintViolationException e){
            View.lanzarMensajeError("Error", "Registro no válido", "El usuario ya se halla registrado en la aplicación");
        } finally {
            limpiar();
        }
    }

    private void limpiar(){
        txtUsuario.clear();
        txtContrasena.clear();
    }

}
