package edu.badpals.pokebase.controller;

import edu.badpals.pokebase.model.PokemonBD;
import edu.badpals.pokebase.model.RutaBD;
import edu.badpals.pokebase.service.ErrorLogger;
import edu.badpals.pokebase.view.View;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

public class SceneManager {
    private static Stack<String> stack = new Stack<>();
    private static Map<String, Object> datos = new HashMap<>();
    private static RutaBD rutaBD;
    private static PokemonBD pokemonBD;

    public static void addToPile(String view){
        stack.push(view);
    }

    public static String getPreviousView(){
        stack.pop();
        return stack.lastElement();
    }

    public static void setRutaBD(RutaBD newRutaBD){
        rutaBD = newRutaBD;
    }

    public static RutaBD getRutaBD(){
        return rutaBD;
    }

    public static void setPokemonBD(PokemonBD newPokemonBD){
        pokemonBD = newPokemonBD;
    }

    public static PokemonBD getPokemonBD(){
        return pokemonBD;
    }

    public static void setDatos(Map<String,Object> datosNuevos){
        datos = datosNuevos;
    }

    public static Map<String, Object> getDatos() {
        return datos;
    }

    private static void loadFxml(ActionEvent actionEvent, String sceneFxml, Class clase, int ancho, int largo) throws IOException {
        FXMLLoader loader = new FXMLLoader(clase.getResource(sceneFxml));
        Scene scene = new Scene(loader.load(),ancho,largo);
        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow(); // Obtener el Stage actual
        // Crear una nueva escena con el contenido cargado
        stage.setScene(scene); // Establecer la nueva escena en el Stage
        stage.show();
    }

    public static void goToView(ActionEvent actionEvent, String sceneFxml, Class clase, int ancho, int largo){
        try {
            addToPile(sceneFxml);
            loadFxml(actionEvent, sceneFxml, clase, ancho, largo);
        } catch (IOException e){
            View.lanzarMensajeError("Error", "No se ha podido cambiar de ventana", "Consulte el log para ver el error más detalladamente");
            ErrorLogger.saveErrorLog("Error al realizar la acción de ir a otra vista: " + e.getMessage());
        }
    }

    public static void volver(ActionEvent actionEvent, Class clase){
        try {
            String view = SceneManager.getPreviousView();
            loadFxml(actionEvent, view, clase, 800, 800);
        } catch (IOException e){
            View.lanzarMensajeError("Error", "No se ha podido cambiar de ventana", "Consulte el log para ver el error más detalladamente");
            ErrorLogger.saveErrorLog("Error al realizar la acción volver: " + e.getMessage());
        }
    }

    public static void volverAlInicio(ActionEvent actionEvent, Class clase){
        try {
            loadFxml(actionEvent, "main.fxml", clase, 800, 600);
        } catch (IOException e){
            View.lanzarMensajeError("Error", "No se ha podido cambiar de ventana", "Consulte el log para ver el error más detalladamente");
            ErrorLogger.saveErrorLog(e.getMessage());
        }
    }
}
