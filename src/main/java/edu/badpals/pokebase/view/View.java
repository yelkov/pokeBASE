package edu.badpals.pokebase.view;


import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.text.FieldPosition;
import java.util.Optional;

/**
 * Proporciona métodos estáticos para mostrar mensajes
 * y para la interfaz gráfica de selección de archivo utilizando JavaFX.
 */
public class View {
    /**
     *  Muestra una ventana emergente con un mensaje informativo
     *  que incluye un título, una cabecera y un mensaje para el usuario.
     *
     * @param titulo El título de la ventana de alerta.
     * @param cabecera El texto que se muestra como cabecera de la alerta.
     * @param mensaje El mensaje principal de la alerta.
     */
    public static void lanzarMensajeAviso(String titulo, String cabecera, String mensaje){
        Alert error = new Alert(Alert.AlertType.INFORMATION);
        error.setTitle(titulo);
        error.setHeaderText(cabecera);
        error.setContentText(mensaje);

        error.showAndWait();
    }

    /**
     * Muestra una ventana emergente con un mensaje de error
     * que incluye un título, una cabecera y un mensaje para el usuario.
     *
     * @param titulo El título de la ventana de alerta.
     * @param cabecera El texto que se muestra como cabecera de la alerta.
     * @param mensaje El mensaje principal de la alerta.
     */

    public static void lanzarMensajeError(String titulo, String cabecera, String mensaje){
        Alert error = new Alert(Alert.AlertType.ERROR);
        error.setTitle(titulo);
        error.setHeaderText(cabecera);
        error.setContentText(mensaje);
        error.setHeight(300.0);

        error.showAndWait();
    }

    /**
     * Muestra una ventana emergente con un mensaje de confirmación
     * que incluye un título, una cabecera y un mensaje para el usuario.
     * El usuario puede elegir entre dos opciones: "Sí" o "No".
     *
     * @param titulo El título de la ventana de alerta.
     * @param cabecera El texto que se muestra como cabecera de la alerta.
     * @param mensaje El mensaje principal de la alerta.
     * @return Un {@code Optional<ButtonType>} que puede contener el tipo de botón presionado ("Sí" o "No").
     */
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

    /**
     * Abre un cuadro de diálogo para seleccionar un archivo formato json y devolver el archivo seleccionado.
     *
     * @param stage El {@link Stage} en el que se abre el cuadro de diálogo.
     * @return Un {@code Optional<File>} que contiene el archivo seleccionado,
     * o un {@code Optional.empty()} si no se seleccionó ningún archivo.
     */
    public static Optional<File> abrirFileChooserExp(Stage stage){
        FileChooser fl = new FileChooser();
        fl.setInitialDirectory(new File(System.getProperty("user.home")));
        fl.setTitle("Guardar archivo");

        fl.getExtensionFilters().add(new FileChooser.ExtensionFilter("Archivos JSON (*.json)", "*.json"));

        File selectedFile = fl.showSaveDialog(stage);
        return selectedFile == null? Optional.empty(): Optional.of(selectedFile);
    }
}
