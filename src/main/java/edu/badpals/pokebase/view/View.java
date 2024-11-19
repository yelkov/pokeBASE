package edu.badpals.pokebase.view;


import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

import java.util.Optional;

public class View {
    public static void lanzarMensajeAviso(String titulo, String cabecera, String mensaje){
        Alert error = new Alert(Alert.AlertType.INFORMATION);
        error.setTitle(titulo);
        error.setHeaderText(cabecera);
        error.setContentText(mensaje);

        error.showAndWait();
    }

    public static void lanzarMensajeError(String titulo, String cabecera, String mensaje){
        Alert error = new Alert(Alert.AlertType.ERROR);
        error.setTitle(titulo);
        error.setHeaderText(cabecera);
        error.setContentText(mensaje);

        error.showAndWait();
    }

    public static Optional<ButtonType> lanzarMensajeConfirmacion(String titulo, String cabecera, String mensaje){
        Alert confirmacion = new Alert(Alert.AlertType.CONFIRMATION);
        confirmacion.setTitle(titulo);
        confirmacion.setHeaderText(cabecera);
        confirmacion.setContentText(mensaje);

        ButtonType btnSi = new ButtonType("Sí");
        ButtonType btnNo = new ButtonType("No");
        confirmacion.getButtonTypes().setAll(btnSi, btnNo);


        Optional<ButtonType> respuesta = confirmacion.showAndWait();
        return respuesta;
    }

}
