package edu.badpals.pokebase.controller;

import edu.badpals.pokebase.model.AccesoBD;
import edu.badpals.pokebase.model.Pokemon;
import edu.badpals.pokebase.model.PokemonBD;
import edu.badpals.pokebase.model.RutaBD;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

public class Controller {
    @FXML
    private Label lblTipo1, lblTipo2;

    @FXML
    private Button btnBuscarPokemon;

    @FXML
    private TextField txtNombrePokemon;

    private AccesoBD accesoBD;
    private PokemonBD pokemonBD;
    private RutaBD rutaBD;

    @FXML
    public void initialize() {
        accesoBD = new AccesoBD();
        accesoBD.connect();
        pokemonBD = new PokemonBD(accesoBD);
    }

    public void buscarPokemon() {
        System.out.println("Buscando pokemon");
        lblTipo1.setText("hola");
        lblTipo2.setText("hola");
        /*String nombrePokemon = txtNombrePokemon.getText();
        Pokemon pokemon = pokemonBD.getPokemonByName(nombrePokemon);
        lblTipo1.setText(pokemon.getTipo1());
        lblTipo2.setText(pokemon.getTipo2());*/

    }


}