package edu.badpals.pokebase.controller;

import edu.badpals.pokebase.model.*;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import java.util.List;
import java.util.Optional;

public class Controller {
    @FXML
    private Button btnBuscarPokemon, btnBuscarRuta, btnFiltrarPokemon, btnFiltrarRuta;

    @FXML
    private Button btnCrearPokemon, btnCrearRuta;

    @FXML
    private TextField txtNombrePokemon, txtNombreRuta, txtPokemonRuta;

    @FXML
    private TextField txtTipo1, txtTipo2;

    @FXML
    private ComboBox<String> cmbRegiones, cmbCriterio, cmbOrden, cmbFiltrarRegiones;

    private AccesoBD accesoBD;
    private PokemonBD pokemonBD;
    private RutaBD rutaBD;

    @FXML
    public void initialize() {
        accesoBD = new AccesoBD();
        accesoBD.connect();
        pokemonBD = new PokemonBD(accesoBD);
        rutaBD = new RutaBD(accesoBD);

        List<String> regiones = rutaBD.getAllRegions();
        cmbRegiones.setItems(FXCollections.observableArrayList(regiones
        ));
        if(regiones.size()>0){
            cmbRegiones.setValue(regiones.get(0));
        }

        regiones.add("Todos");
        cmbFiltrarRegiones.setItems(FXCollections.observableArrayList(regiones
        ));
        cmbRegiones.setValue("Todas");

        cmbCriterio.setItems(FXCollections.observableArrayList("id","nombre"));
        cmbCriterio.setValue("id");

        cmbOrden.setItems(FXCollections.observableArrayList("asc","desc"));
        cmbCriterio.setValue("asc");

    }

    public void buscarPokemon() {
        String pokemonSearch = txtNombrePokemon.getText();
        Pokemon pokemon;
        try{
            int id = Integer.valueOf(pokemonSearch);
            pokemon = pokemonBD.getPokemonById(id);
        } catch (NumberFormatException e){
            pokemon = pokemonBD.getPokemonByName(pokemonSearch);
        }
    }

    public void buscarRuta(){
        String nombreRuta = txtNombreRuta.getText();
        String nombreRegion = cmbRegiones.getSelectionModel().getSelectedItem();
        Optional<Ruta> ruta = rutaBD.getRuta(nombreRuta, nombreRegion);
    }

    public void filtrarPokemon(){
        String tipo1 = txtTipo1.getText();
        String tipo2 = txtTipo2.getText();
        String criterio = cmbCriterio.getSelectionModel().getSelectedItem();
        String orden = cmbOrden.getSelectionModel().getSelectedItem();
        if (!tipo1.equals("")){
            tipo2 = tipo2.equals("")? null : tipo2;
            List<Pokemon> pokemons = pokemonBD.getPokemonsByType(tipo1, tipo2, criterio + " " +orden);
        }
    }

    public void filtrarRuta(){
        Optional<String> pokemon = txtPokemonRuta.getText().equals("")?Optional.empty():Optional.of(txtPokemonRuta.getText());
        String regionSeleccionada = cmbFiltrarRegiones.getSelectionModel().getSelectedItem();
        Optional<String> region = regionSeleccionada.equals("Todas")? Optional.empty():Optional.of(regionSeleccionada);
        List<Ruta> rutas = rutaBD.getRutasByFilters(pokemon, region);

    }


}