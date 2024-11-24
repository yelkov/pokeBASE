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

/**
 * Clase que gestiona las transiciones de vistas (escenas) en la aplicación.
 * También mantiene un historial de vistas previas y almacena datos compartidos entre vistas.
 */
public class SceneManager {
    /**
     * Pila para almacenar el historial de escenas, permitiendo navegar hacia atrás en la aplicación.
     */
    private static Stack<Scene> stack = new Stack<>();
    /**
     * Almacena datos compartidos entre vistas para facilitar la comunicación entre ellas.
     */
    private static Map<String, Object> datos = new HashMap<>();
    /**
     * Instancias de las clases que gestionan el acceso a la base de datos
     */
    private static RutaBD rutaBD;
    private static PokemonBD pokemonBD;
    private static AccesoBD accesoBD;

    /**
     * Agrega una nueva escena al historial de vistas.
     *
     * @param view la escena a agregar a la pila.
     */
    public static void addToPile(Scene view){
        stack.push(view);
    }

    /**
     * Elimina la última escena de la pila y devuelve la anterior.
     *
     * @return la escena anterior en la pila.
     */
    public static Scene getPreviousView(){
        stack.pop();
        return stack.lastElement();
    }

    /**
     * Establece la instancia de `RutaBD` para la gestión de rutas en la base de datos.
     *
     * @param newRutaBD la nueva instancia de `RutaBD`.
     */
    public static void setRutaBD(RutaBD newRutaBD){
        rutaBD = newRutaBD;
    }

    /**
     * Obtiene la instancia actual de `RutaBD` para la gestión de rutas en la base de datos.
     *
     * @return la instancia de `RutaBD`.
     */
    public static RutaBD getRutaBD(){
        return rutaBD;
    }

    /**
     * Establece la instancia de `PokemonBD` para la gestión de Pokémon en la base de datos
     *
     * @param newPokemonBD la nueva instancia de `PokemonBD`.
     */
    public static void setPokemonBD(PokemonBD newPokemonBD){
        pokemonBD = newPokemonBD;
    }

    /**
     * Obtiene la instancia actual de `PokemonBD` para la gestión de pokémon en la base de datos.
     *
     * @return la instancia de `PokemonBD`.
     */
    public static PokemonBD getPokemonBD(){
        return pokemonBD;
    }

    /**
     * Obtiene la instancia actual de `AccesoBD` para la gestión de la conexión a la base de datos.
     *
     * @return la instancia de `AccesoBD`.
     */
    public static AccesoBD getAccesoBD() {return accesoBD;}

    /**
     * Establece la instancia de `AccesoBD` para la conexión a la base de datos.
     *
     * @param accesoBD la nueva instancia de `AccesoBD`.
     */
    public static void setAccesoBD(AccesoBD accesoBD) {SceneManager.accesoBD = accesoBD;}

    /**
     * Establece los datos compartidos para su uso entre vistas.
     *
     * @param datosNuevos los nuevos datos a compartir.
     */
    public static void setDatos(Map<String,Object> datosNuevos){
        datos = datosNuevos;
    }

    /**
     * Obtiene los datos compartidos entre vistas.
     *
     * @return un mapa con los datos compartidos.
     */
    public static Map<String, Object> getDatos() {
        return datos;
    }

    /**
     * Carga una nueva escena desde un archivo FXML.
     *
     * @param sceneFxml la ruta del archivo FXML.
     * @param clase     la clase que solicita la carga.
     * @param ancho     el ancho de la ventana.
     * @param largo     el alto de la ventana.
     * @return la nueva escena creada.
     * @throws IOException si ocurre un error al cargar el archivo FXML.
     */
    private static Scene loadFxml(String sceneFxml, Class clase, int ancho, int largo) throws IOException {
        FXMLLoader loader = new FXMLLoader(clase.getResource(sceneFxml));
        Scene scene = new Scene(loader.load(),ancho,largo);
        return scene;
    }

    /**
     * Cambia a una nueva vista y almacena la vista actual en el historial.
     *
     * @param actionEvent el evento que desencadenó el cambio de vista.
     * @param sceneFxml   la ruta del archivo FXML de la nueva vista.
     * @param clase       la clase que solicita el cambio.
     * @param ancho       el ancho de la ventana.
     * @param largo       el alto de la ventana.
     */
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

    /**
     * Vuelve a la vista anterior en el historial.
     *
     * @param actionEvent el evento que desencadenó la acción de volver.
     * @param clase       la clase que solicita el cambio.
     */
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

    /**
     * Vuelve a la vista del menú principal y limpia el historial de vistas.
     *
     * @param actionEvent el evento que desencadenó la acción de volver al inicio.
     * @param clase       la clase que solicita el cambio.
     */
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
