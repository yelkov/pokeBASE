package edu.badpals.pokebase.model;

import org.junit.jupiter.api.*;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class RutaBDTest {
    static RutaBD rutaBD;
    static AccesoBD accesoBD;

    @BeforeAll
    static void setUp() {
        accesoBD = new AccesoBD();
        accesoBD.connect("bdpokemon_test");
        accesoBD.setAutoCommit(false);
        rutaBD = new RutaBD(accesoBD);
    }

    @AfterAll
    static void tearDown() {
        accesoBD.rollback();
        accesoBD.desconectarBD();
    }

    @Test
    void getRoutesCount(){
        int totalRutas = rutaBD.getRoutesCount();
        assertEquals(39, totalRutas);
    }

    @Test
    void getRutaId(){
        int ruta1 = rutaBD.getRutaId("Ruta 1", "Kanto");
        int ruta10 = rutaBD.getRutaId("Ruta 10", "Kanto");
        int rutaNoExiste = rutaBD.getRutaId("Ruta 10", "Unova");
        assertEquals(1, ruta1);
        assertEquals(10, ruta10);
        assertEquals(0, rutaNoExiste);
    }

    @Test
    void getRutabyId(){
        Optional<Ruta> isRuta = rutaBD.getRuta(2);
        assertTrue(isRuta.isPresent());
        Ruta ruta = isRuta.get();
        assertEquals(2, ruta.getId());
        assertEquals("Ruta 2", ruta.getNombre());
        assertEquals("Kanto", ruta.getRegion());
    }

    @Test
    void gettAllRegions(){
        List<String> regions = rutaBD.getAllRegions();
        assertTrue(regions.size()<=3);
        assertTrue(regions.contains("Kanto"));
        assertTrue(regions.contains("Johto"));
    }

    @Test
    void insertRuta() {
        int totalRutas = rutaBD.getRoutesCount();
        Ruta ruta = new Ruta(1, "Ruta 1", "Unova");
        assertTrue(rutaBD.insertRuta(ruta));
        assertEquals(totalRutas + 1, rutaBD.getRoutesCount());
        //Problemas con los id's autogenerados
    }

    @Test
    void insertRuta_YaExiste() {
        int totalRutas = rutaBD.getRoutesCount();
        Ruta ruta = new Ruta(1, "Ruta 1", "Kanto");
        assertFalse(rutaBD.insertRuta(ruta));
        assertEquals(totalRutas, rutaBD.getRoutesCount());
        //Problemas con los id's autogenerados
    }

    @Test
    void getRoutesCount_ByRegion() {
        int rutasKanto = rutaBD.getRoutesCount("Kanto");
        int rutasJohto = rutaBD.getRoutesCount("Johto");
        int rutasEstonia = rutaBD.getRoutesCount("Estonia");
        assertEquals(38, rutasKanto);
        assertEquals(1, rutasJohto);
        assertEquals(0, rutasEstonia);
    }

    @Test
    void getRutabyNameAndRegion(){
        Optional<Ruta> isRuta = rutaBD.getRuta("Ruta 10", "Kanto");
        assertTrue(isRuta.isPresent());
        Ruta ruta = isRuta.get();
        assertEquals("Ruta 10", ruta.getNombre());
        assertEquals("Kanto", ruta.getRegion());
    }

    @Test
    void getRutabyNameRegion_DoesntExist(){
        Optional<Ruta> isRuta = rutaBD.getRuta("Ruta 10", "Unova");
        assertTrue(isRuta.isEmpty());
    }

    @Test
    void modifyRuta(){
        int rutasKanto = rutaBD.getRoutesCount("Kanto");
        Ruta ruta  = rutaBD.getRuta("Ruta 1", "Kanto").get();
        ruta.setNombre("Ruta 5");
        ruta.setRegion("Alola");
        int id = ruta.getId();
        assertTrue(rutaBD.updateRuta(ruta));
        assertEquals(rutasKanto - 1, rutaBD.getRoutesCount("Kanto"));
        Optional<Ruta> isRuta = rutaBD.getRuta(id);
        assertTrue(isRuta.isPresent());
        Ruta rutaModificada = isRuta.get();
        assertEquals("Ruta 5", rutaModificada.getNombre());
        assertEquals("Alola", rutaModificada.getRegion());

        //deshacemos los cambios
        ruta.setNombre("Ruta 1");
        ruta.setRegion("Kanto");
        rutaBD.updateRuta(ruta);
    }

    @Test
    void modifyRuta_AlreadyExists(){
        int rutasKanto = rutaBD.getRoutesCount("Kanto");
        Ruta ruta  = rutaBD.getRuta("Ruta 1", "Kanto").get();
        int id = ruta.getId();

        ruta.setNombre("Ruta 101");
        ruta.setRegion("Johto");

        assertFalse(rutaBD.updateRuta(ruta));
        assertEquals(rutasKanto, rutaBD.getRoutesCount("Kanto"));
        Optional<Ruta> isRuta = rutaBD.getRuta(id);
        assertTrue(isRuta.isPresent());
        Ruta rutaModificada = isRuta.get();
        assertEquals("Ruta 1", rutaModificada.getNombre());
        assertEquals("Kanto", rutaModificada.getRegion());
    }


    @Test
    void deleteRuta(){
        int numRutas = rutaBD.getRoutesCount();
        Ruta ruta = new Ruta(0, "Toledo", "España");
        rutaBD.insertRuta(ruta);
        assertEquals(numRutas + 1, rutaBD.getRoutesCount());
        Ruta createdRuta = rutaBD.getRuta("Toledo", "España").get();
        int id = createdRuta.getId();
        assertTrue(rutaBD.deleteRuta(id));
        assertEquals(numRutas, rutaBD.getRoutesCount());
    }

    @Test
    void deleteRutaByNameAndRegion() {
        int numRutas = rutaBD.getRoutesCount();
        Ruta ruta = new Ruta(0, "Toledo", "España");
        rutaBD.insertRuta(ruta);
        assertEquals(numRutas + 1, rutaBD.getRoutesCount());
        assertTrue(rutaBD.deleteRuta("Toledo", "España"));
        assertEquals(numRutas, rutaBD.getRoutesCount());
    }

    @Test
    void deleteRutaByNameAndRegion_DoesntExist() {
        int numRutas = rutaBD.getRoutesCount();
        assertFalse(rutaBD.deleteRuta("Praga", "Chequia"));
        assertEquals(numRutas, rutaBD.getRoutesCount());
    }

    @Test
    void getRutasByFiler_NoFilter(){
        Optional<String> empty = Optional.empty();
        List<Ruta> rutas = rutaBD.getRutasByFilters(empty, empty);
        assertEquals(rutaBD.getRoutesCount(), rutas.size());
    }

    @Test
    void getRutasByFiler_RegionFilter(){
        Optional<String> empty = Optional.empty();
        Optional<String> region = Optional.of("Kanto");
        List<Ruta> rutas = rutaBD.getRutasByFilters(empty, region);
        assertEquals(38, rutas.size());
    }

    @Test
    void getRutasByFiler_RegionFilter_NoResults(){
        Optional<String> empty = Optional.empty();
        Optional<String> region = Optional.of("Estonia");
        List<Ruta> rutas = rutaBD.getRutasByFilters(empty, region);
        assertEquals(0, rutas.size());
    }


    @Test
    void getRutasByFiler_PokemonFilter(){
        Optional<String> empty = Optional.empty();
        Optional<String> pokemon = Optional.of("Pikachu");
        List<Ruta> rutas = rutaBD.getRutasByFilters(pokemon, empty);
        assertEquals(2, rutas.size());
    }

    @Test
    void getRutasByFiler_PokemonFilter_NoResults(){
        Optional<String> empty = Optional.empty();
        Optional<String> pokemon = Optional.of("Volcarona");
        List<Ruta> rutas = rutaBD.getRutasByFilters(pokemon, empty);
        assertEquals(0, rutas.size());
    }

    @Test
    void getRutasByFiler_BothFilters(){
        Optional<String> region = Optional.of("Kanto");
        Optional<String> pokemon = Optional.of("Pikachu");
        List<Ruta> rutas = rutaBD.getRutasByFilters(pokemon, region);
        assertEquals(1, rutas.size());
        Ruta ruta = rutas.get(0);
        assertEquals("Ruta 10", ruta.getNombre());
        assertEquals("Kanto", ruta.getRegion());
    }

    @Test
    void getRutasByFiler_BothFilters_NoResultsPokemon(){
        Optional<String> region = Optional.of("Kanto");
        Optional<String> pokemon = Optional.of("Arceus");
        List<Ruta> rutas = rutaBD.getRutasByFilters(pokemon, region);
        assertEquals(0, rutas.size());
    }

    @Test
    void getRutasByFiler_BothFilters_NoResultsRegion(){
        Optional<String> region = Optional.of("Alola");
        Optional<String> pokemon = Optional.of("Pikachu");
        List<Ruta> rutas = rutaBD.getRutasByFilters(pokemon, region);
        assertEquals(0, rutas.size());
    }

    @Test
    void getRutasByFiler_BothFilters_NoResultsBoth(){
        Optional<String> region = Optional.of("Alola");
        Optional<String> pokemon = Optional.of("Arceus");
        List<Ruta> rutas = rutaBD.getRutasByFilters(pokemon, region);
        assertEquals(0, rutas.size());
    }

    @Test
    void getPokemons(){
        int rutaId = 11;
        List<RutaPokemon> pokemons = rutaBD.getPokemons(rutaId);
        assertEquals(2, pokemons.size());
        RutaPokemon pokemon1 = new RutaPokemon("nidoran-f", 11,3, 5);
        RutaPokemon pokemon2 = new RutaPokemon("nidoran-m", 11,3, 5);
        assertTrue(pokemons.contains(pokemon1));
        assertTrue(pokemons.contains(pokemon2));
    }

    @Test
    void getPokemons_NoPokemons(){
        int rutaId = 17;
        List<RutaPokemon> pokemons = rutaBD.getPokemons(rutaId);
        assertEquals(0, pokemons.size());
    }

    @Test
    void addPokemon(){
        Ruta ruta = rutaBD.getRuta("Ruta 1", "Kanto").get();
        int numberPkRuta = rutaBD.getPokemons(ruta.getId()).size();
        assertTrue(rutaBD.addPokemon(ruta.getId(), "Pikachu"));
        List<RutaPokemon> pokemonsRuta = rutaBD.getPokemons(ruta.getId());
        assertEquals(numberPkRuta + 1, pokemonsRuta.size());
        RutaPokemon pokemon = new RutaPokemon("pikachu", ruta.getId(), 0 ,0);
        assertTrue(pokemonsRuta.contains(pokemon));
    }

    @Test
    void addPokemon_YaEsta(){
        Ruta ruta = rutaBD.getRuta("Ruta 1", "Kanto").get();
        int numberPkRuta = rutaBD.getPokemons(ruta.getId()).size();
        assertFalse(rutaBD.addPokemon(ruta.getId(), "Bulbasaur"));
        List<RutaPokemon> pokemonsRuta = rutaBD.getPokemons(ruta.getId());
        assertEquals(numberPkRuta , pokemonsRuta.size());
    }

    @Test
    void addPokemon_NoEsxiste(){
        Ruta ruta = rutaBD.getRuta("Ruta 1", "Kanto").get();
        int numberPkRuta = rutaBD.getPokemons(ruta.getId()).size();
        assertFalse(rutaBD.addPokemon(ruta.getId(), "Jajbse"));
        List<RutaPokemon> pokemonsRuta = rutaBD.getPokemons(ruta.getId());
        assertEquals(numberPkRuta , pokemonsRuta.size());
    }
}