package edu.badpals.pokebase.controller;

import edu.badpals.pokebase.model.AccesoBD;
import edu.badpals.pokebase.model.Pokemon;
import edu.badpals.pokebase.model.PokemonBD;
import edu.badpals.pokebase.model.RutaBD;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import java.io.ByteArrayInputStream;

public class ControllerPokemon {
    @FXML
    private Button btnVolver, btnNuevaBusqueda, btnEvolucionaDe,btnMostrarAnterior,btnMostrarPosterior, btnCrear, btnModificar, btnEliminar;
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

    private Stage primaryStage;
    private AccesoBD accesoBD;
    private PokemonBD pokemonBD;
    private RutaBD rutaBD;

    public void initialize() {
        accesoBD = new AccesoBD();
        accesoBD.connect();
        pokemonBD = new PokemonBD(accesoBD);
        rutaBD = new RutaBD(accesoBD);

    }

    public void mostrar(Node nodo, boolean visibility, boolean managed){
        nodo.setVisible(visibility);
        nodo.setManaged(managed);
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

    public void setPokemon(Pokemon pokemon) {
        this.pokemon = pokemon;
        visualizarDatos();
        establecerSiguientesPokemon();
    }

    public void setImage() {
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
        }
    }

    @FXML
    private void handleVolver(ActionEvent event) {
        Stage stage = (Stage) btnVolver.getScene().getWindow();
        stage.close();
    }
}
