package edu.badpals.pokebase.controller;

import edu.badpals.pokebase.model.AccesoBD;
import edu.badpals.pokebase.model.Pokemon;
import edu.badpals.pokebase.model.PokemonBD;
import edu.badpals.pokebase.model.RutaBD;
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
import java.io.IOException;
import java.util.Optional;

public class ControllerPokemon {
    @FXML
    private Button btnVolver, btnLimpiar, btnNuevaBusqueda, btnEvolucionaDe,btnMostrarAnterior,btnMostrarPosterior, btnCrear, btnModificar, btnEliminar;
    @FXML
    private ImageView imgPokemon;
    @FXML
    private Label lblNombrePokemon, lblId, lblTipo1, lblTipo2, lblMetodo;
    @FXML
    private TextField tfNombrePokemon;
    @FXML
    private GridPane gridEvolucionaDe;

    private Pokemon pokemon;
    private Pokemon pokemonPosterior;
    private Pokemon pokemonAnterior;
    private Pokemon pokemonPreEvolucion;

    private AccesoBD accesoBD;
    private PokemonBD pokemonBD;
    private RutaBD rutaBD;

    public void initialize() {
        accesoBD = new AccesoBD();
        accesoBD.connect();
        pokemonBD = new PokemonBD(accesoBD);
        rutaBD = new RutaBD(accesoBD);

    }

    public void setPokemon(Pokemon pokemon) {
        this.pokemon = pokemon;
        visualizarDatos();
        establecerSiguientesPokemon();
        habilitarBotonesLaterales();
    }


    public void mostrar(Node nodo, boolean visibility, boolean managed){
        nodo.setVisible(visibility);
        nodo.setManaged(managed);
    }

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



    private void establecerSiguientesPokemon() {
        int anteriorId = pokemon.getId()-1;
        int siguienteId = pokemon.getId()+1;
        pokemonAnterior = pokemonBD.getPokemonById(anteriorId);
        pokemonPosterior = pokemonBD.getPokemonById(siguienteId);
    }

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
    }

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

    public void setImage() {
        mostrar(imgPokemon,true,true);
        Image image = null;
        if(pokemon.getGif() != null) {
            ByteArrayInputStream bais = new ByteArrayInputStream(pokemon.getGif());
            image = new Image(bais);
        }else if(pokemon.getImagen() != null) {
            ByteArrayInputStream bais = new ByteArrayInputStream(pokemon.getImagen());
            image = new Image(bais);
        }
        imgPokemon.setImage(image);
    }

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

        }else{
            View.lanzarMensajeError("Error","Pokémon no encontrado.","En la base de datos no se encuentra el nombre del pokémon o el id introducido.");
        }
    }

    @FXML
    private void handleVolver(ActionEvent event) {
        Stage stage = (Stage) btnVolver.getScene().getWindow();
        stage.hide();
    }

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
    }

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

    public void editarPokemon(ActionEvent actionEvent){
        try{
            FXMLLoader loader = Controller.getFxmlLoader(actionEvent,"editarPokemon.fxml", this.getClass(), 600,500);
            ControllerEditarPokemon controller = loader.getController();
            if(actionEvent.getSource() == btnCrear){
                controller.setPokemon(null);
            }else if(actionEvent.getSource() == btnModificar){
                controller.setPokemon(pokemon);
            }
        }catch (IOException e) {
            e.printStackTrace();
        }

    }

    private FXMLLoader getFxmlLoader(ActionEvent actionEvent, String sceneFxml) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource(sceneFxml));
        Scene scene = new Scene(loader.load(),600,500);
        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();

        stage.setScene(scene);
        stage.show();
        return loader;
    }

}
