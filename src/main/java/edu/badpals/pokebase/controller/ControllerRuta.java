package edu.badpals.pokebase.controller;

import edu.badpals.pokebase.criteria.CriteriaRuta;
import edu.badpals.pokebase.model.Pokemon;
import edu.badpals.pokebase.model.Ruta;
import edu.badpals.pokebase.model.RutaBD;
import edu.badpals.pokebase.model.RutaPokemon;
import edu.badpals.pokebase.service.DocumentExporter;
import edu.badpals.pokebase.service.ErrorLogger;
import edu.badpals.pokebase.view.View;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.*;

/**
 * Controlador para gestionar una ruta en la aplicación.
 * Maneja la creación, modificación, eliminación y visualización de rutas, así como la interacción con los pokémon en cada ruta.
 */
public class ControllerRuta {
    //Elementos de la interfaz de usuario.
    @FXML
    private TextField txtRutaNombre, txtRutaRegion, txtPokemonAnadir, txtNiveles, txtMinimoNivel, txtMaximoNivel;

    @FXML
    private Label lblRutaId, lblCriterios;

    @FXML
    private ListView<RutaPokemon> listPokemonsRuta;

    @FXML
    private HBox menuRutaNueva;

    @FXML
    private VBox menuPokemon, menuParteLista, menuRutaCargada;

    @FXML
    private Button btnAnterior, btnSiguiente, btnBuscarPokemon, btnRetirarPokemon;

    //Acceso a la base de datos
    private RutaBD rutaBD;
    //Objetos que representan componentes necesarios para la representación gráfica
    private Ruta ruta;
    private List<Ruta> rutas;
    private int currentIndex;
    private CriteriaRuta criteriaRuta;

    /**
     * Inicializa el controlador configurando las referencias a la base de datos.
     * Si recibe del SceneManager un objeto tipo ruta, lo muestra en la vista.
     * También configura los campos de entrada para aceptar solo números en ciertos campos.
     */
    public void initialize() {
        rutaBD = SceneManager.getRutaBD();
        Map<String, Object> datos = SceneManager.getDatos();
        if (datos.containsKey("ruta")){
            setRuta((Ruta) datos.get("ruta"));
            if (datos.containsKey("rutas") && datos.containsKey("criteriaRuta")){
                rutas = (List<Ruta>) datos.get("rutas");
                currentIndex = rutas.indexOf(ruta);
                criteriaRuta = (CriteriaRuta) datos.get("criteriaRuta");
                setPartOfList();
            }
        }

        permitirSoloEnteros(txtMaximoNivel);
        permitirSoloEnteros(txtMinimoNivel);
        permitirSoloEnteros(txtNiveles);
    }

    /**
     * Configura un formato que solo permite números enteros en el campo de texto proporcionado.
     *
     * @param textField El campo de texto al que se le aplicará el formato.
     */
    private void permitirSoloEnteros(TextField textField) {
        TextFormatter<String> integerFormatter = new TextFormatter<>(change ->
                change.getControlNewText().matches("\\d*") ? change : null);
        textField.setTextFormatter(integerFormatter);
    }

    /**
     * Establece la ruta y configura los elementos de la interfaz de usuario para mostrar la información de esta.
     *
     * @param ruta La ruta seleccionada.
     */
    public void setRuta(Ruta ruta){
        this.ruta = ruta;
        configureMenu(true);
        lblRutaId.setText(String.valueOf(ruta.getId()));
        txtRutaNombre.setText(ruta.getNombre());
        txtRutaRegion.setText(ruta.getRegion());
        setPokemonList(ruta.getId());
    }

    /**
     * Establece la lista de pokémon asociados a una ruta.
     *
     * @param rutaId El ID de la ruta de la cual se cargarán los pokémon.
     */
    private void setPokemonList(int rutaId){
        List<RutaPokemon> pokemons = rutaBD.getPokemons(rutaId);
        if (pokemons.size() > 0){
            showNode(menuPokemon, true);
            listPokemonsRuta.setItems(FXCollections.observableArrayList(pokemons));
        } else {
            showNode(menuPokemon, false);
        }
    }

    /**
     * Añade un pokémon a la ruta actual, asegurándose de que el nombre del pokémon y los niveles sean válidos.
     * Si el pokémon ya existe en la ruta o no existe en la base de datos no permite realizar la operación
     */
    public void addPokemonRuta(){
        int id = Integer.valueOf(lblRutaId.getText());
        String nombre = txtPokemonAnadir.getText();
        if (!nombre.equals("")){
            try{
                int minimo = Integer.valueOf(txtMinimoNivel.getText());
                int maximo = Integer.valueOf(txtMaximoNivel.getText());
                boolean isAddOk = rutaBD.addPokemon(id, nombre, minimo, maximo);
                if (isAddOk){
                    setPokemonList(id);
                } else{
                    View.lanzarMensajeError("Error", "Inserción fallida", "No se ha podido añadir el pokemon a la ruta porque el pokemon no existe en la base de datos, debe crearlo primero. Para otros posibles motivos consulte el log");
                }
            } catch (SQLIntegrityConstraintViolationException e){
                View.lanzarMensajeError("Error", "Inserción fallida", "El pokemon no se puede añadir a la ruta porque ya está registrado en ella");
            } catch (NumberFormatException e){
                View.lanzarMensajeError("Error", "Error de formato", "Los campos de nivel mínimo y máximo son obligatorios");
            }
        } else{
            View.lanzarMensajeError("Error", "Formato incorrecto", "Debe introducir el nombre del pokemon a añadir");
        }
        txtPokemonAnadir.setText("");
        txtMaximoNivel.setText("");
        txtMinimoNivel.setText("");
    }

    /**
     * Elimina un pokémon de la ruta actual, recargando la lista de pokemons de la ruta.
     */
    public void removePokemonRuta(){
        int rutaId = Integer.valueOf(lblRutaId.getText());
        String pokemonName = listPokemonsRuta.getSelectionModel().getSelectedItem().getPokemon();
        Pokemon pokemon = rutaBD.getPokemonBD().getPokemonByName(pokemonName);
        int pokemonId = pokemon.getId();
        boolean isOk = rutaBD.removePokemonRuta(rutaId, pokemonId);
        if(isOk){
            setPokemonList(rutaId);
        } else {
            View.lanzarMensajeError("Error", "Eliminación de pokemon de ruta no realizada", "No se ha podido realizar la operación correctamente, consulte los motivos en el archivo de log");
        }
    }

    /**
     * Modifica los niveles de los pokémon en la ruta actual.
     *
     * @param id El ID de la ruta que se va a modificar.
     * @param niveles El número de niveles a sumar o restar.
     */
    private void modificarNiveles(int id, int niveles) {
        boolean isModificarOk = rutaBD.subirNivelesRuta(id, niveles);
        if (isModificarOk) {
            setPokemonList(id);
        } else {
            View.lanzarMensajeError("Error", "Modificación de niveles abortada", "No se ha podido realizar la operación correctamente, consulte los motivos en el archivo de log");
        }
        txtNiveles.setText("");
    }

    /**
     * Sube el nivel de los pokémon en la ruta actual, según lo indicado en la interfaz.
     */
    public void subirNiveles(){
        int id = Integer.valueOf(lblRutaId.getText());
        try{
            int niveles = Integer.valueOf(txtNiveles.getText());
            modificarNiveles(id, niveles);
        } catch (NumberFormatException e){
            View.lanzarMensajeError("Error", "Error de formato", "Debe introducir el número de niveles a modificar");
        }
    }

    /**
     * Baja el nivel de los pokémon en la ruta actual, según lo indicado en la interfaz.
     */
    public void bajarNiveles(){
        int id = Integer.valueOf(lblRutaId.getText());
        try{
            int niveles = Integer.valueOf(txtNiveles.getText());
            modificarNiveles(id, 0-niveles);
        } catch (NumberFormatException e){
            View.lanzarMensajeError("Error", "Error de formato", "Debe introducir el número de niveles a modificar");
        }
    }

    /**
     * Muestra los criterios de búsqueda de ruta desde los que se accedió a la ruta
     * y habilita los botones para navegar entre las rutas,
     */
    public void setPartOfList(){
        showNode(menuParteLista, true);
        lblCriterios.setText(criteriaRuta.toString());
        if (rutas.size()==1){
            btnAnterior.setDisable(true);
            btnSiguiente.setDisable(true);
        }
    }

    /**
     * Activa o desactiva los botones de acción para un pokémon seleccionado en la lista.
     */
    public void activateBotonesPokemon(){
        String pokemon = listPokemonsRuta.getSelectionModel().getSelectedItem().getPokemon();
        if(pokemon!= null){
            btnBuscarPokemon.setDisable(false);
            btnRetirarPokemon.setDisable(false);
        } else {
            btnBuscarPokemon.setDisable(true);
            btnRetirarPokemon.setDisable(true);
        }
    }

    /**
     * Muestra la información de la siguiente ruta en la lista de rutas disponibles.
     */
    public void siguienteRuta(){
        currentIndex++;
        try{
            setRuta(rutas.get(currentIndex));
        } catch (IndexOutOfBoundsException e){
            currentIndex = 0;
            setRuta(rutas.get(currentIndex));
        }
    }

    /**
     * Muestra la información de la ruta anterior en la lista de rutas disponibles.
     */
    public void anteriorRuta(){
        currentIndex--;
        try{
            setRuta(rutas.get(currentIndex));
        } catch (IndexOutOfBoundsException e){
            currentIndex = rutas.size()-1;
            setRuta(rutas.get(currentIndex));
        }
    }

    /**
     * Muestra u oculta un nodo de la interfaz de usuario.
     *
     * @param node El nodo a mostrar u ocultar.
     * @param shown Determina si el nodo debe ser visible o no.
     */
    public void showNode(Node node, boolean shown){
        node.setVisible(shown);
        node.setManaged(shown);
    }

    /**
     * Configura el menú de la interfaz de usuario dependiendo de si hay una ruta cargada o no.
     *
     * @param isRutaLoaded Indica si se ha cargado una ruta o no.
     */
    public void configureMenu (boolean isRutaLoaded){
        if (isRutaLoaded){
            showNode(menuRutaCargada, true);
            showNode(menuRutaNueva, false);
        } else {
            showNode(menuRutaCargada, false);
            showNode(menuRutaNueva, true);
        }
    }

    /**
     * Crea una nueva ruta en la base de datos a partir de la información introducida en la interfaz.
     */
    public void crearRuta(){
        String nombre = txtRutaNombre.getText();
        String region = txtRutaRegion.getText();
        Ruta rutaNueva = new Ruta(0,nombre, region);
        try{
            boolean wasRutaCreated = rutaBD.insertRuta(rutaNueva);
            if (wasRutaCreated){
                Ruta rutaLoaded = rutaBD.getRuta(nombre, region).get();
                setRuta(rutaLoaded);
            } else {
                View.lanzarMensajeError("Error", "Error al insertar la nueva ruta", "No se ha podido realizar el insert en la base de datos, consulte los motivos en el archivo log");
            }
        } catch (SQLIntegrityConstraintViolationException e){
            View.lanzarMensajeError("Error", "Error al insertar la nueva ruta", "La ruta que se ha intentado crear ya existe en la base de datos");
        }
    }

    /**
     * Modifica los detalles de la ruta actual en la base de datos, según lo especificado en la interfaz.
     */
    public void modificarRuta(){
        int id = Integer.valueOf(lblRutaId.getText());
        String nombre = txtRutaNombre.getText();
        String region = txtRutaRegion.getText();
        Ruta rutaUpdated = new Ruta(id, nombre, region);
        try {
            boolean wasRutaUpdated = rutaBD.updateRuta(rutaUpdated);
            if (wasRutaUpdated) {
                setRuta(rutaUpdated);
            } else {
                View.lanzarMensajeError("Error", "Error al modificar la ruta", "No se ha podido realizar el update en la base de datos, consulte los motivos en el archivo log");
            }
        } catch (SQLIntegrityConstraintViolationException e){
            View.lanzarMensajeError("Error", "Error al modificar la ruta", "Ya existe una ruta diferente con el nombre y región indicados");
            setRuta(ruta);
        }
    }

    /**
     * Elimina la ruta cargada en la interfaz de la base de datos
     * y limpia la información de esta de la vista
     */
    public void borrarRuta(){
        String idValue = lblRutaId.getText();
        int id = Integer.parseInt(idValue);
        boolean wasRutaDeleted = rutaBD.deleteRuta(id);
        if(wasRutaDeleted){
            configureMenu(false);
            cleanFields();
        } else {
            View.lanzarMensajeError("Error", "Error al borrar la ruta", "No se ha podido realizar el delete en la base de datos, consulte los motivos en el archivo log");
        }
    }

    /**
     * Configura la vista para que solo se muestren los elementos relativos a crear una nueva ruta
     */
    public void toMenuCrearRuta(){
        configureMenu(false);
        cleanFields();
        showNode(menuParteLista, false);
    }

    /**
     * Limpia los campos de entrada y oculta la información de la ruta cargada.
     */
    public void cleanFields(){
        lblRutaId.setText("");
        txtRutaNombre.setText("");
        txtRutaRegion.setText("");
        showNode(menuPokemon, false);
    }

    /**
     * Exporta los datos de la ruta a un archivo JSON,
     * abriendo una ventana para que el usuario escoja la ruta destino de la exportación.
     *
     * @param action La acción que dispara el evento de exportación.
     */
    public void exportar(ActionEvent action){
        Stage stage = (Stage) ((Node) action.getSource()).getScene().getWindow();
        Optional<File> posibleDirectorio = View.abrirFileChooserExp(stage);
        if(posibleDirectorio.isPresent()){
            String rutaExportacion = posibleDirectorio.get().getAbsolutePath();
            boolean exportaCorrecto = DocumentExporter.exportToJson(ruta,rutaExportacion);
            if(exportaCorrecto){
                View.lanzarMensajeAviso(
                        "Aviso",
                        "Exportación exitosa",
                        "Se han exportado los datos de pokémon a: "+ rutaExportacion);
            }else{
                View.lanzarMensajeError(
                        "Error",
                        "Error de exportación",
                        "Se ha producido un error inesperado. Consulte el log para más información.");
            }
        }
    }

    /**
     * Navega a la escena de detalles del pokémon seleccionado en la lista.
     *
     * @param actionEvent El evento que dispara la acción.
     */
    public void buscarInfoPokemon(ActionEvent actionEvent){
        String pokemonName = listPokemonsRuta.getSelectionModel().getSelectedItem().getPokemon();
        Pokemon pokemon = rutaBD.getPokemonBD().getPokemonByName(pokemonName);
        Map<String, Object> datos = new HashMap<>();
        datos.put("pokemon", pokemon);
        SceneManager.setDatos(datos);
        SceneManager.goToView(actionEvent, "datosPokemon.fxml", this.getClass(), 750, 650);
    }

    /**
     * Maneja la acción de volver a la vista anterior.
     *
     * @param event El evento que dispara la acción de volver.
     */
    @FXML
    private void handleVolver(ActionEvent event) {

        if(criteriaRuta != null) {
            Map<String, Object> datos = new HashMap<>();
            datos.put("criteriaRuta", criteriaRuta);
            SceneManager.setDatos(datos);
        }
        SceneManager.volver(event, this.getClass());
    }

    /**
     * Vuelve al menú principal de la aplicación.
     *
     * @param actionEvent El evento que dispara la acción de volver al inicio.
     */
    public void volverAlInicio(ActionEvent actionEvent){
        SceneManager.volverAlInicio(actionEvent, this.getClass());
    }
}
