package edu.badpals.pokebase.model;

import java.util.Objects;

/**
 * Representa una relación entre una ruta y un pokémon que pueden ser encontrado en ella.
 * Además, especifica el rango de niveles del Pokémon en esa ruta.
 */
public class RutaPokemon {
    /**
     * El nombre del Pokémon que aparece en la ruta.
     */
    private String pokemon;
    /**
     * El identificador de la ruta donde se encuentra el Pokémon.
     */
    private int idRuta;
    /**
     * El nivel mínimo que el Pokémon puede tener en la ruta.
     */
    private int nivel_minimo;
    /**
     * El nivel máximo que el Pokémon puede tener en la ruta.
     */
    private int nivel_maximo;

    /**
     * Constructor que inicializa un objeto <code>RutaPokemon</code> con los valores proporcionados.
     *
     * @param pokemon El nombre del Pokémon encontrado en la ruta.
     * @param idRuta El identificador único de la ruta.
     * @param nivel_minimo El nivel mínimo que el Pokémon puede tener en esta ruta.
     * @param nivel_maximo El nivel máximo que el Pokémon puede tener en esta ruta.
     */
    public RutaPokemon(String pokemon, int idRuta, int nivel_minimo, int nivel_maximo) {
        this.pokemon = pokemon;
        this.idRuta = idRuta;
        this.nivel_minimo = nivel_minimo;
        this.nivel_maximo = nivel_maximo;
    }

    /**
     * Devuelve el nombre del Pokémon en esta ruta.
     *
     * @return El nombre del Pokémon.
     */
    public String getPokemon() {
        return pokemon;
    }

    /**
     * Devuelve el identificador de la ruta en la que se encuentra el Pokémon.
     *
     * @return El identificador de la ruta.
     */
    public int getIdRuta() {
        return idRuta;
    }

    /**
     * Devuelve el nivel mínimo que el Pokémon puede tener en esta ruta.
     *
     * @return El nivel mínimo del Pokémon.
     */
    public int getNivel_minimo() {
        return nivel_minimo;
    }

    /**
     * Devuelve el nivel máximo que el Pokémon puede tener en esta ruta.
     *
     * @return El nivel máximo del Pokémon.
     */
    public int getNivel_maximo() {
        return nivel_maximo;
    }

    /**
     * Devuelve una representación en forma de cadena del Pokémon y su rango de niveles en la ruta.
     * El formato es: "nombre del Pokémon [nivel mínimo, nivel máximo]".
     *
     * @return Una cadena representando el Pokémon y su rango de niveles.
     */
    @Override
    public String toString() {
        return getPokemon() +
                " [" + nivel_minimo +
                "," + nivel_maximo +
                ']';
    }

    /**
     * Compara dos objetos <code>RutaPokemon</code> para determinar si son iguales.
     * Dos objetos <code>RutaPokemon</code> son iguales si tienen el mismo nombre de Pokémon, el mismo identificador de ruta,
     * el mismo nivel mínimo y el mismo nivel máximo.
     *
     * @param o El objeto a comparar con el objeto actual.
     * @return <code>true</code> si los objetos son iguales, <code>false</code> de lo contrario.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof RutaPokemon that)) return false;
        return idRuta == that.idRuta && nivel_minimo == that.nivel_minimo && nivel_maximo == that.nivel_maximo && Objects.equals(pokemon, that.pokemon);
    }

    /**
     * Genera un código hash único para el objeto <code>RutaPokemon</code>, basado en el nombre del Pokémon, el identificador de ruta,
     * el nivel mínimo y el nivel máximo.
     *
     * @return El código hash del objeto.
     */
    @Override
    public int hashCode() {
        return Objects.hash(pokemon, idRuta, nivel_minimo, nivel_maximo);
    }
}
