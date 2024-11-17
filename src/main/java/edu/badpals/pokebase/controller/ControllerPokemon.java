package edu.badpals.pokebase.controller;

import edu.badpals.pokebase.model.Pokemon;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class ControllerPokemon {
    @FXML
    private Label lblPokemon;

    public void initialize() {}

    public void setPokemon(Pokemon pokemon) {
        lblPokemon.setText(pokemon.toString());
    }
}
