package edu.badpals.pokebase.controller;

import edu.badpals.pokebase.model.AccesoBD;
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
    private static Stack<Scene> stack = new Stack<>();
    private static Map<String, Object> datos = new HashMap<>();
    private static RutaBD rutaBD;
    private static PokemonBD pokemonBD;
    private static AccesoBD accesoBD;

    public static void addToPile(Scene view){
        stack.push(view);
    }

    public static Scene getPreviousView(){
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

    public static AccesoBD getAccesoBD() {return accesoBD;}

    public static void setAccesoBD(AccesoBD accesoBD) {SceneManager.accesoBD = accesoBD;}

    public static void setDatos(Map<String,Object> datosNuevos){
        datos = datosNuevos;
    }

    public static Map<String, Object> getDatos() {
        return datos;
    }

    private static Scene loadFxml(String sceneFxml, Class clase, int ancho, int largo) throws IOException {
        FXMLLoader loader = new FXMLLoader(clase.getResource(sceneFxml));
        Scene scene = new Scene(loader.load(),ancho,largo);
        return scene;
    }

    public static void goToView(ActionEvent actionEvent, String sceneFxml, Class clase, int ancho, int largo){
        try {
            Stage currentStage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
            Scene currentScene = currentStage.getScene();
            addToPile(currentScene);

            Scene newScene = loadFxml(sceneFxml, clase, ancho, largo);
            currentStage.setScene(newScene);
            currentStage.show();
        } catch (IOException e){
            View.lanzarMensajeError("Error", "No se ha podido cambiar de ventana", "Consulte el log para ver el error más detalladamente");
            ErrorLogger.saveErrorLog("Error al realizar la acción de ir a otra vista: " + e.getMessage());
        }
    }

    public static void volver(ActionEvent actionEvent, Class clase){
        try {
            Stage currentStage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();

            if(!stack.isEmpty()){
                Scene previousScene = stack.pop();
                currentStage.setScene(previousScene);
                currentStage.show();
            }else{
                View.lanzarMensajeError("Error", "No hay vistas anteriores en la pila", "");
            }
        } catch (Exception e){
            View.lanzarMensajeError("Error", "No se ha podido cambiar de ventana", "Consulte el log para ver el error más detalladamente");
            ErrorLogger.saveErrorLog("Error al realizar la acción volver: " + e.getMessage());
        }
    }

    public static void volverAlInicio(ActionEvent actionEvent, Class clase){
        try {
            stack.clear();
            Stage currentStage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
            Scene main = loadFxml( "main.fxml", clase, 950, 800);
            currentStage.setScene(main);
            currentStage.show();
        } catch (IOException e){
            View.lanzarMensajeError("Error", "No se ha podido cambiar de ventana", "Consulte el log para ver el error más detalladamente");
            ErrorLogger.saveErrorLog(e.getMessage());
        }
    }
}
