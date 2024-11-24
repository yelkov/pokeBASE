package edu.badpals.pokebase.controller;

import edu.badpals.pokebase.model.AccesoBD;
import edu.badpals.pokebase.model.Pokemon;
import edu.badpals.pokebase.model.PokemonBD;
import edu.badpals.pokebase.model.RutaBD;
import edu.badpals.pokebase.service.DocumentExporter;
import edu.badpals.pokebase.service.ErrorLogger;
import edu.badpals.pokebase.view.View;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * Maneja la interacción entre la vista y el modelo para la
 * visualización, y exportación de los datos de los Pokémon en la aplicación.
 * Incluye métodos para visualizar los datos del Pokémon, gestionar la búsqueda de Pokémon, y realizar acciones sobre los mismos (como eliminar o exportar).
 */
public class ControllerPokemon {
    // Componentes de la interfaz de usuario
    @FXML
    private Button btnVolver, btnLimpiar, btnNuevaBusqueda, btnEvolucionaDe,btnMostrarAnterior,btnMostrarPosterior, btnCrear, btnModificar, btnEliminar, btnExportar, btnMenuPrincipal;
    @FXML
    private ImageView imgPokemon;
    @FXML
    private Label lblNombrePokemon, lblId, lblTipo1, lblTipo2, lblMetodo;
    @FXML
    private TextField tfNombrePokemon;
    @FXML
    private GridPane gridEvolucionaDe;

    // Objetos que guardan la información de pokémons relevantes
    private Pokemon pokemon;
    private Pokemon pokemonPosterior;
    private Pokemon pokemonAnterior;
    private Pokemon pokemonPreEvolucion;

    // Objeto para acceder a la base de datos
    private PokemonBD pokemonBD;

    /**
     * Inicializa el controlador y prepara la conexión con la base de datos de Pokémon.
     * Además, si se pasan datos de un Pokémon, los carga y los visualiza.
     */
    public void initialize() {
        pokemonBD = SceneManager.getPokemonBD();

        Map<String, Object> datos = SceneManager.getDatos();
        if (datos.containsKey("pokemon")){
            setPokemon((Pokemon) datos.get("pokemon"));
        }
    }

    /**
     * Establece el Pokémon actual y actualiza la vista con sus datos.
     *
     * @param pokemon El Pokémon a establecer.
     */
    public void setPokemon(Pokemon pokemon) {
        this.pokemon = pokemon;
        visualizarDatos();
        establecerSiguientesPokemon();
        habilitarBotonesLaterales();
        btnExportar.setDisable(false);
        btnModificar.setDisable(false);
        btnEliminar.setDisable(false);
    }

    /**
     * Controla la visibilidad de un nodo en la interfaz de usuario.
     *
     * @param nodo El nodo a mostrar o esconder.
     * @param visibility Si el nodo debe ser visible o no.
     * @param managed Si el nodo debe ser administrado por el layout.
     */
    public void mostrar(Node nodo, boolean visibility, boolean managed){
            nodo.setVisible(visibility);
            nodo.setManaged(managed);
        }

    /**
     * Habilita o deshabilita los botones de navegación hacia los Pokémon anteriores y posteriores dependiendo de si existen o no.
     */
    public void habilitarBotonesLaterales(){
        if(this.pokemonAnterior != null){
            btnMostrarAnterior.setDisable(false);
        }else{
            btnMostrarAnterior.setDisable(true);
        }
        if(this.pokemonPosterior != null){
            btnMostrarPosterior.setDisable(false);
        }else{
            btnMostrarPosterior.setDisable(true);
        }
    }

    /**
     * Establece los Pokémon anterior y posterior a partir del ID del Pokémon actual.
     */
    private void establecerSiguientesPokemon() {
        int anteriorId = pokemon.getId()-1;
        int siguienteId = pokemon.getId()+1;
        pokemonAnterior = pokemonBD.getPokemonById(anteriorId);
        pokemonPosterior = pokemonBD.getPokemonById(siguienteId);
    }

    /**
     * Cambia el Pokémon mostrado en función del botón presionado (anterior, posterior o preevolución).
     *
     * @param actionEvent El evento que dispara la acción (el botón presionado).
     */
    public void cambiarPokemon(ActionEvent actionEvent) {
        if(actionEvent.getSource() == btnMostrarAnterior) {
            this.pokemon = pokemonAnterior;
            visualizarDatos();
            establecerSiguientesPokemon();
        }else if(actionEvent.getSource() == btnMostrarPosterior) {
            this.pokemon = pokemonPosterior;
            visualizarDatos();
            establecerSiguientesPokemon();
        }else if(actionEvent.getSource() == btnEvolucionaDe) {
            this.pokemon = pokemonPreEvolucion;
            visualizarDatos();
            establecerSiguientesPokemon();
        }
        habilitarBotonesLaterales();
        btnExportar.setDisable(false);
    }

    /**
     * Actualiza la vista con los datos del Pokémon actual.
     */
    public void visualizarDatos() {
        lblNombrePokemon.setText(pokemon.getNombre());
        lblId.setText(pokemon.getId().toString());
        lblTipo1.setText(pokemon.getTipo1().toString());
        if(pokemon.getTipo2()!=null){
            lblTipo2.setText(pokemon.getTipo2().toString());
        }else{
            lblTipo2.setText("");
        }
        if(pokemon.getEvolucionaDe()!=null){
            mostrar(gridEvolucionaDe,true,true);
            pokemonPreEvolucion = pokemonBD.getPokemonById(pokemon.getEvolucionaDe());
            btnEvolucionaDe.setText(pokemonPreEvolucion.getNombre());
            lblMetodo.setText(pokemon.getMetodoEvolucion());
        }else{
            mostrar(gridEvolucionaDe,false,false);
            pokemonPreEvolucion = null;
            btnEvolucionaDe.setText("");
            lblMetodo.setText("");
        }
        setImage();
    }

    /**
     * Establece la imagen del Pokémon, primero intentándolo con el GIF,
     * y si no existe con una imagen estática.
     */
    public void setImage() {
        mostrar(imgPokemon,true,true);
        imgPokemon.setImage(null);
        Image image = null;
        if(pokemon.getGif() != null) {
            ByteArrayInputStream bais = new ByteArrayInputStream(pokemon.getGif());
            image = new Image(bais);
        }else if(pokemon.getImagen() != null) {
            ByteArrayInputStream bais = new ByteArrayInputStream(pokemon.getImagen());
            image = new Image(bais);
        }
        if (image != null) {
            imgPokemon.setImage(image);

            double originalWidth = image.getWidth();
            double originalHeight = image.getHeight();

            if (originalWidth > 150 || originalHeight > 150) {
                imgPokemon.setFitWidth(150);
                imgPokemon.setFitHeight(150);
                imgPokemon.setPreserveRatio(true);
            } else {
                imgPokemon.setFitWidth(originalWidth);
                imgPokemon.setFitHeight(originalHeight);
            }
        }

    }

    /**
     * Busca un Pokémon por su nombre o ID y actualiza la vista si se encuentra.
     * Si no, lanza un mensaje.
     */
    public void buscarPokemon(){
        String pokemonSearch = tfNombrePokemon.getText();
        Pokemon nuevoPokemon;
        try{
            int id = Integer.valueOf(pokemonSearch);
            nuevoPokemon = pokemonBD.getPokemonById(id);
        } catch (NumberFormatException e){
            nuevoPokemon = pokemonBD.getPokemonByName(pokemonSearch);
        }
        if(nuevoPokemon != null){
            this.pokemon = nuevoPokemon;
            visualizarDatos();
            establecerSiguientesPokemon();
            habilitarBotonesLaterales();
            btnExportar.setDisable(false);
            btnModificar.setDisable(false);
            btnEliminar.setDisable(false);

        }else{
            View.lanzarMensajeError("Error","Pokémon no encontrado.","En la base de datos no se encuentra el nombre del pokémon o el id introducido.");
        }
    }

    /**
     * Vuelve a la pantalla anterior.
     *
     * @param event El evento que dispara la acción.
     */
    @FXML
    private void handleVolver(ActionEvent event) {
        SceneManager.volver(event, this.getClass());
    }

    /**
     * Vuelve al menú principal de la aplicación.
     *
     * @param actionEvent El evento que dispara la acción.
     */
    public void volverAlInicio(ActionEvent actionEvent){
        SceneManager.volverAlInicio(actionEvent, this.getClass());
    }

    /**
     * Limpia los datos y resetea la vista del panel sin ningún pokémon cargado.
     */
    public void limpiarPanel(){
        this.pokemon = null;
        this.pokemonPosterior = null;
        this.pokemonAnterior = null;
        this.pokemonPreEvolucion = null;
        habilitarBotonesLaterales();
        mostrar(imgPokemon,false,false);
        lblNombrePokemon.setText("");
        lblId.setText("");
        lblTipo1.setText("");
        lblTipo2.setText("");
        mostrar(gridEvolucionaDe,false,false);
        btnExportar.setDisable(true);
        btnModificar.setDisable(true);
        btnEliminar.setDisable(true);
    }

    /**
     * Elimina el Pokémon actual (cargado en memoria) de la base de datos.
     */
    public void eliminarPokemon(){
        if(this.pokemon == null){
            View.lanzarMensajeError("Error","Error al eliminar pokémon","No se encuentra seleccionado ningún pokémon.");
            return;
        }
        Optional<ButtonType> respuesta = View.lanzarMensajeConfirmacion("Eliminar","Se va a eliminar un pokémon.","¿Está seguro de que desea eliminar el pokémon actual de la base de datos? Esta operación es irreversible.");
        if(respuesta.isPresent() && respuesta.get().getText().equals("Sí")){
            pokemonBD.deletePokemon(pokemon);
            View.lanzarMensajeAviso("Aviso","Eliminación completada","Se ha borrado al pokémon exitosamente");
            limpiarPanel();
        }
    }

    /**
     * Permite crear o modificar un Pokémon,
     * accediendo a la vista destinada a tal fin
     *
     * @param actionEvent El evento que dispara la acción (botón presionado).
     */
    public void editarPokemon(ActionEvent actionEvent){
        Map<String, Object> datos = new HashMap<>();

        if(actionEvent.getSource() == btnCrear){
        }
        else if(actionEvent.getSource() == btnModificar){
            datos.put("pokemon", pokemon);
        }
        SceneManager.setDatos(datos);
        SceneManager.goToView(actionEvent,"editarPokemon.fxml", this.getClass(), 650,600);

    }

    /**
     * Exporta los datos del Pokémon cargado en memoria a un archivo JSON.
     *
     * @param action El evento que dispara la acción (botón presionado).
     */
    public void exportar(ActionEvent action){
        Stage stage = (Stage) ((Node) action.getSource()).getScene().getWindow();
        Optional<File> posibleDirectorio = View.abrirFileChooserExp(stage);
        if(posibleDirectorio.isPresent()){
            String ruta = posibleDirectorio.get().getAbsolutePath();
            boolean exportaCorrecto = DocumentExporter.exportToJson(pokemon,ruta);
            if(exportaCorrecto){
                View.lanzarMensajeAviso(
                        "Aviso",
                        "Exportación exitosa",
                        "Se han exportado los datos de pokémon a: "+ ruta);
            }else{
                View.lanzarMensajeError(
                        "Error",
                        "Error de exportación",
                        "Se ha producido un error inesperado. Consulte el log para más información.");
            }
        }
    }

}
