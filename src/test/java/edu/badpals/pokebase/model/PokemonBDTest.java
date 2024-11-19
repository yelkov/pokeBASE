package edu.badpals.pokebase.model;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
class PokemonBDTest {

    static PokemonBD pokemonBD;
    static AccesoBD accesoBD;

    @BeforeAll
    static void setUp() {
        accesoBD = new AccesoBD();
        accesoBD.connect("bdpokemon_test");
        accesoBD.setAutoCommit(false);
        pokemonBD = new PokemonBD(accesoBD);
    }

    @AfterAll
    static void tearDown() {
        accesoBD.rollback();
        accesoBD.desconectarBD();
    }

    @Test
    void test_isIdPresent(){
        int idPresent = 1;
        int idNotPresent = 2000;
        assertTrue(pokemonBD.isIdPresent(idPresent));
        assertFalse(pokemonBD.isIdPresent(idNotPresent));
    }

    @Test
    void test_isNombrePresent(){
        String nombrePresent = "bulbasaur";
        String nombreNotPresent = "pepito";
        assertTrue(pokemonBD.isNombrePresent(nombrePresent));
        assertFalse(pokemonBD.isNombrePresent(nombreNotPresent));
    }

    @Test
    void test_getPokemonById_oneType() {
        Pokemon pokemon = pokemonBD.getPokemonById(150);
        assertNotNull(pokemon);
        assertEquals(150,pokemon.getId());
        assertEquals("mewtwo",pokemon.getNombre());
        assertEquals("Psíquico",pokemon.getTipo1());
        assertNull(pokemon.getTipo2());
        assertNull(pokemon.getEvolucionaDe());
        assertNull(pokemon.getMetodoEvolucion());
    }
    @Test
    void test_getPokemonById_twoTypes() {
        Pokemon pokemon = pokemonBD.getPokemonById(1);
        assertNotNull(pokemon);
        assertEquals(1,pokemon.getId());
        assertEquals("bulbasaur",pokemon.getNombre());
        assertEquals("Planta",pokemon.getTipo1());
        assertEquals("Veneno",pokemon.getTipo2());
    }
    @Test
    void test_getPokemonById_evolution() {
        Pokemon pokemon = pokemonBD.getPokemonById(2);
        assertNotNull(pokemon);
        assertEquals(2,pokemon.getId());
        assertEquals("ivysaur",pokemon.getNombre());
        assertEquals("Planta",pokemon.getTipo1());
        assertEquals("Veneno",pokemon.getTipo2());
        assertEquals(1,pokemon.getEvolucionaDe());
        assertEquals("Nivel 16",pokemon.getMetodoEvolucion());
    }
    @Test
    void test_getPokemonByName_oneType() {
        Pokemon pokemon = pokemonBD.getPokemonByName("mew");
        assertNotNull(pokemon);
        assertEquals(151,pokemon.getId());
        assertEquals("mew",pokemon.getNombre());
        assertEquals("Psíquico",pokemon.getTipo1());
        assertNull(pokemon.getTipo2());
        assertNull(pokemon.getEvolucionaDe());
        assertNull(pokemon.getMetodoEvolucion());
    }
    @Test
    void test_getPokemonByName_twoTypes() {
        Pokemon pokemon = pokemonBD.getPokemonByName("pidgey");
        assertNotNull(pokemon);
        assertEquals(16,pokemon.getId());
        assertEquals("pidgey",pokemon.getNombre());
        assertEquals("Normal",pokemon.getTipo1());
        assertEquals("Volador",pokemon.getTipo2());
    }
    @Test
    void test_getPokemonByName_evoluciona() {
        Pokemon pokemon = pokemonBD.getPokemonByName("charizard");
        assertNotNull(pokemon);
        assertEquals(6,pokemon.getId());
        assertEquals("charizard",pokemon.getNombre());
        assertEquals("Fuego",pokemon.getTipo1());
        assertEquals("Volador",pokemon.getTipo2());
        assertEquals(5,pokemon.getEvolucionaDe());
        assertEquals("Nivel 36",pokemon.getMetodoEvolucion());
    }

    @Test
    void test_getPokemonsByType(){
        List<Pokemon> pokemonsAgua = pokemonBD.getPokemonsByType("Agua",null,"ID ASC");
        assertNotNull(pokemonsAgua);
        assertEquals(32,pokemonsAgua.size());
        assertEquals(7,pokemonsAgua.get(0).getId());
    }
    @Test
    void test_getPokemonsByTypeOrderName(){
        List<Pokemon> pokemonsAgua = pokemonBD.getPokemonsByType("Agua",null,"NOMBRE ASC");
        assertNotNull(pokemonsAgua);
        assertEquals(32,pokemonsAgua.size());
        assertEquals(9,pokemonsAgua.get(0).getId());
        assertEquals("blastoise",pokemonsAgua.get(0).getNombre());
    }
    @Test
    void test_getPokemonsByTwoTypes(){
        List<Pokemon> pokemonsPlantaVeneno = pokemonBD.getPokemonsByType("Planta","Veneno","ID ASC");
        assertNotNull(pokemonsPlantaVeneno);
        assertEquals(9,pokemonsPlantaVeneno.size());
        assertEquals(1,pokemonsPlantaVeneno.get(0).getId());
    }

    @Test
    void test_getPokemons2TypesName(){
        List<Pokemon> pokemonsPlantaVeneno = pokemonBD.getPokemonsByType("Planta","Veneno","NOMBRE DESC");
        assertNotNull(pokemonsPlantaVeneno);
        assertEquals(9,pokemonsPlantaVeneno.size());
        assertEquals(70,pokemonsPlantaVeneno.get(0).getId());
        assertEquals("weepinbell",pokemonsPlantaVeneno.get(0).getNombre());
    }
    @Test
    void test_modificarPokemon(){
        Pokemon dragonite = pokemonBD.getPokemonByName("dragonite");
        Integer antiguoId = dragonite.getId();
        dragonite.setNombre("otro");
        dragonite.setId(250);
        dragonite.setTipo1("Agua");
        dragonite.setTipo2("Tierra");
        dragonite.setEvolucionaDe(10);
        dragonite.setMetodoEvolucion("Nivel 2");
        assertTrue(pokemonBD.updatePokemon(antiguoId,dragonite));

        Pokemon dragoniteModif = pokemonBD.getPokemonByName("otro");
        assertNotNull(dragoniteModif);
        assertEquals("otro",dragoniteModif.getNombre());
        assertEquals(250,dragoniteModif.getId());
        assertEquals("Agua",dragoniteModif.getTipo1());
        assertEquals("Tierra",dragoniteModif.getTipo2());
        assertEquals(10,dragoniteModif.getEvolucionaDe());
        assertEquals("Nivel 2",dragoniteModif.getMetodoEvolucion());

        //Deshacemos los cambios
        Integer idModificado = dragoniteModif.getId();
        dragoniteModif.setNombre("dragonite");
        dragoniteModif.setId(149);
        dragoniteModif.setTipo1("Dragón");
        dragoniteModif.setTipo2("Volador");
        dragoniteModif.setEvolucionaDe(148);
        dragoniteModif.setMetodoEvolucion("Nivel 55");
        pokemonBD.updatePokemon(idModificado,dragoniteModif);
    }
    @Test
    public void test_insertPokemon(){
        Pokemon pokemon = new Pokemon(152,"inventado",null,null,null,"Psíquico",null,151,"Nivel 50");
        assertTrue(pokemonBD.insertPokemon(pokemon));
        Pokemon inventado = pokemonBD.getPokemonByName("inventado");
        assertNotNull(inventado);
        assertEquals("inventado",inventado.getNombre());
        assertNull(inventado.getTipo2());
    }

    @Test
    public void test_deletePokemon(){
        Pokemon inventado = pokemonBD.getPokemonByName("inventado");
        assertTrue(pokemonBD.deletePokemon(inventado));

        Pokemon inventado2 = pokemonBD.getPokemonByName("inventado");
        assertNull(inventado2);
    }

    /*
    @Test
    void test_getAllPokemon(){
        PokemonBD pokemonBD

        = new PokemonBD();
        pokemonBD

       .conectarBD();
        List<Pokemon> pokemons = pokemonBD

       .getAllPokemons("ID ASC");
        assertNotNull(pokemons);
        assertEquals(151,pokemons.size());
        assertEquals(1,pokemons.get(0).getId());
        assertEquals("mew",pokemons.get(150).getNombre());
    }
    */
}