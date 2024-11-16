package edu.badpals.pokebase.model;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
class PokemonBDTest {

    @Test
    void test_getPokemonById_oneType() {
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
    void test_getPokemonById_twoTypes() {
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
    void test_getPokemonById_evolution() {
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
    @Test
    void test_getPokemonByName_oneType() {
        PokemonBD db = new PokemonBD();
        db.conectarBD();
        Pokemon pokemon = db.getPokemonByName("mew");
        assertNotNull(pokemon);
        assertEquals(151,pokemon.getId());
        assertEquals("mew",pokemon.getNombre());
        assertEquals("Psíquico",pokemon.getTipo1());
        assertNull(pokemon.getTipo2());
        assertNull(pokemon.getEvolucionaDe());
        assertNull(pokemon.getMetodoEvolucion());
        db.desconectarBD();
    }
    @Test
    void test_getPokemonByName_twoTypes() {
        PokemonBD db = new PokemonBD();
        db.conectarBD();
        Pokemon pokemon = db.getPokemonByName("pidgey");
        assertNotNull(pokemon);
        assertEquals(16,pokemon.getId());
        assertEquals("pidgey",pokemon.getNombre());
        assertEquals("Normal",pokemon.getTipo1());
        assertEquals("Volador",pokemon.getTipo2());
        db.desconectarBD();
    }
    @Test
    void test_getPokemonByName_evoluciona() {
        PokemonBD db = new PokemonBD();
        db.conectarBD();
        Pokemon pokemon = db.getPokemonByName("charizard");
        assertNotNull(pokemon);
        assertEquals(6,pokemon.getId());
        assertEquals("charizard",pokemon.getNombre());
        assertEquals("Fuego",pokemon.getTipo1());
        assertEquals("Volador",pokemon.getTipo2());
        assertEquals(5,pokemon.getEvolucionaDe());
        assertEquals("Nivel 36",pokemon.getMetodoEvolucion());
        db.desconectarBD();
    }

    @Test
    void test_getPokemonsByType(){
        PokemonBD db = new PokemonBD();
        db.conectarBD();
        List<Pokemon> pokemonsAgua = db.getPokemonsByType("Agua",null,"ID ASC");
        assertNotNull(pokemonsAgua);
        assertEquals(32,pokemonsAgua.size());
        assertEquals(7,pokemonsAgua.get(0).getId());
    }
    @Test
    void test_getPokemonsByTypeOrderName(){
        PokemonBD db = new PokemonBD();
        db.conectarBD();
        List<Pokemon> pokemonsAgua = db.getPokemonsByType("Agua",null,"NOMBRE ASC");
        assertNotNull(pokemonsAgua);
        assertEquals(32,pokemonsAgua.size());
        assertEquals(9,pokemonsAgua.get(0).getId());
        assertEquals("blastoise",pokemonsAgua.get(0).getNombre());
    }
    @Test
    void test_getPokemonsByTwoTypes(){
        PokemonBD db = new PokemonBD();
        db.conectarBD();
        List<Pokemon> pokemonsPlantaVeneno = db.getPokemonsByType("Planta","Veneno","ID ASC");
        assertNotNull(pokemonsPlantaVeneno);
        assertEquals(9,pokemonsPlantaVeneno.size());
        assertEquals(1,pokemonsPlantaVeneno.get(0).getId());
    }

    @Test
    void test_getPokemons2TypesName(){
        PokemonBD db = new PokemonBD();
        db.conectarBD();
        List<Pokemon> pokemonsPlantaVeneno = db.getPokemonsByType("Planta","Veneno","NOMBRE DESC");
        assertNotNull(pokemonsPlantaVeneno);
        assertEquals(9,pokemonsPlantaVeneno.size());
        assertEquals(70,pokemonsPlantaVeneno.get(0).getId());
        assertEquals("weepinbell",pokemonsPlantaVeneno.get(0).getNombre());
    }

    /*
    @Test
    void test_getAllPokemon(){
        PokemonBD db = new PokemonBD();
        db.conectarBD();
        List<Pokemon> pokemons = db.getAllPokemons("ID ASC");
        assertNotNull(pokemons);
        assertEquals(151,pokemons.size());
        assertEquals(1,pokemons.get(0).getId());
        assertEquals("mew",pokemons.get(150).getNombre());
    }
    */
}