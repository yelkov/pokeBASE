package edu.badpals.pokebase.model;

import java.util.Objects;

public class RutaPokemon {
    private String pokemon;
    private int idRuta;
    private int nivel_minimo;
    private int nivel_maximo;

    public RutaPokemon(String pokemon, int idRuta, int nivel_minimo, int nivel_maximo) {
        this.pokemon = pokemon;
        this.idRuta = idRuta;
        this.nivel_minimo = nivel_minimo;
        this.nivel_maximo = nivel_maximo;
    }

    public String getPokemon() {
        return pokemon;
    }

    public int getIdRuta() {
        return idRuta;
    }

    public int getNivel_minimo() {
        return nivel_minimo;
    }

    public int getNivel_maximo() {
        return nivel_maximo;
    }

    @Override
    public String toString() {
        return getPokemon() +
                " [" + nivel_minimo +
                "," + nivel_maximo +
                ']';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof RutaPokemon that)) return false;
        return idRuta == that.idRuta && nivel_minimo == that.nivel_minimo && nivel_maximo == that.nivel_maximo && Objects.equals(pokemon, that.pokemon);
    }

    @Override
    public int hashCode() {
        return Objects.hash(pokemon, idRuta, nivel_minimo, nivel_maximo);
    }
}
