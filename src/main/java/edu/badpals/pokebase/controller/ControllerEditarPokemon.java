package edu.badpals.pokebase.controller;

import edu.badpals.pokebase.model.AccesoBD;
import edu.badpals.pokebase.model.Pokemon;
import edu.badpals.pokebase.model.PokemonBD;
import edu.badpals.pokebase.model.RutaBD;
import javafx.event.ActionEvent;

public class ControllerEditarPokemon {

    private Pokemon pokemon;

    private AccesoBD accesoBD;
    private PokemonBD pokemonBD;
    private RutaBD rutaBD;

    public void initialize() {
        accesoBD = new AccesoBD();
        accesoBD.connect();
        pokemonBD = new PokemonBD(accesoBD);
        rutaBD = new RutaBD(accesoBD);
    }

    public void limpiarPanel(ActionEvent actionEvent) {
    }

    public void handleVolver(ActionEvent actionEvent) {
    }

    public void eliminarPokemon(ActionEvent actionEvent) {
    }
}
