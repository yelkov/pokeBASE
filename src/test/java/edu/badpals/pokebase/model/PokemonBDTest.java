package edu.badpals.pokebase.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
class PokemonBDTest {

    @Test
    void getPokemonById_oneType() {
        PokemonBD db = new PokemonBD();
        db.conectarBD();
        Pokemon pokemon = db.getPokemonById(150);
        assertNotNull(pokemon);
        assertEquals(150,pokemon.getId());
        assertEquals("mewtwo",pokemon.getNombre());
        assertEquals("Psíquico",pokemon.getTipo1());
        assertNull(pokemon.getTipo2());
        assertNull(pokemon.getEvolucionaDe());
        assertNull(pokemon.getMetodoEvolucion());
        db.desconectarBD();
    }
    @Test
    void getPokemonById_twoTypes() {
        PokemonBD db = new PokemonBD();
        db.conectarBD();
        Pokemon pokemon = db.getPokemonById(1);
        assertNotNull(pokemon);
        assertEquals(1,pokemon.getId());
        assertEquals("bulbasaur",pokemon.getNombre());
        assertEquals("Planta",pokemon.getTipo1());
        assertEquals("Veneno",pokemon.getTipo2());
        db.desconectarBD();
    }
    @Test
    void getPokemonById_evolution() {
        PokemonBD db = new PokemonBD();
        db.conectarBD();
        Pokemon pokemon = db.getPokemonById(2);
        assertNotNull(pokemon);
        assertEquals(2,pokemon.getId());
        assertEquals("ivysaur",pokemon.getNombre());
        assertEquals("Planta",pokemon.getTipo1());
        assertEquals("Veneno",pokemon.getTipo2());
        assertEquals(1,pokemon.getEvolucionaDe());
        assertEquals("Nivel 16",pokemon.getMetodoEvolucion());
        db.desconectarBD();
    }
}