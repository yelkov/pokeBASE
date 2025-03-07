package edu.badpals.pokebase.model;

import org.junit.jupiter.api.*;

import java.rmi.UnexpectedException;
import java.sql.SQLIntegrityConstraintViolationException;
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
    void insertRuta() throws Exception{
        int totalRutas = rutaBD.getRoutesCount();
        Ruta ruta = new Ruta(1, "Ruta 1", "Unova");
        try {
            assertTrue(rutaBD.insertRuta(ruta));
            assertEquals(totalRutas + 1, rutaBD.getRoutesCount());
        } catch (SQLIntegrityConstraintViolationException e){
            throw new Exception("No se debería lanzar una excepción");
        }
    }

    @Test
    void insertRuta_YaExiste() {
        int totalRutas = rutaBD.getRoutesCount();
        Ruta ruta = new Ruta(1, "Ruta 1", "Kanto");
        assertThrows(SQLIntegrityConstraintViolationException.class, () -> rutaBD.insertRuta(ruta));
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
    void modifyRuta() throws Exception{
        try {
            int rutasKanto = rutaBD.getRoutesCount("Kanto");
            Ruta ruta = rutaBD.getRuta("Ruta 1", "Kanto").get();
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
        } catch (SQLIntegrityConstraintViolationException e){
            throw new Exception("No se debería lanzar la excepción de integridad aquí");
        }
    }

    @Test
    void modifyRuta_AlreadyExists(){
        int rutasKanto = rutaBD.getRoutesCount("Kanto");
        Ruta ruta  = rutaBD.getRuta("Ruta 1", "Kanto").get();
        int id = ruta.getId();

        ruta.setNombre("Ruta 101");
        ruta.setRegion("Johto");

        assertThrows(SQLIntegrityConstraintViolationException.class, () -> rutaBD.updateRuta(ruta));
        assertEquals(rutasKanto, rutaBD.getRoutesCount("Kanto"));
        Optional<Ruta> isRuta = rutaBD.getRuta(id);
        assertTrue(isRuta.isPresent());
        Ruta rutaModificada = isRuta.get();
        assertEquals("Ruta 1", rutaModificada.getNombre());
        assertEquals("Kanto", rutaModificada.getRegion());
    }


    @Test
    void deleteRuta() throws Exception{
        try {
            int numRutas = rutaBD.getRoutesCount();
            Ruta ruta = new Ruta(0, "Toledo", "España");
            rutaBD.insertRuta(ruta);
            assertEquals(numRutas + 1, rutaBD.getRoutesCount());
            Ruta createdRuta = rutaBD.getRuta("Toledo", "España").get();
            int id = createdRuta.getId();
            assertTrue(rutaBD.deleteRuta(id));
            assertEquals(numRutas, rutaBD.getRoutesCount());
        } catch (SQLIntegrityConstraintViolationException e){
            throw new Exception("No se debería lanzar la excepción de integridad aquí");
        }
    }

    @Test
    void deleteRutaByNameAndRegion() throws Exception{
        try {
            int numRutas = rutaBD.getRoutesCount();
            Ruta ruta = new Ruta(0, "Toledo", "España");
            rutaBD.insertRuta(ruta);
            assertEquals(numRutas + 1, rutaBD.getRoutesCount());
            assertTrue(rutaBD.deleteRuta("Toledo", "España"));
            assertEquals(numRutas, rutaBD.getRoutesCount());
        } catch (SQLIntegrityConstraintViolationException e){
            throw new Exception("No se debería lanzar la excepción de integridad aquí");
        }
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
    void addPokemon() throws Exception{
        try {
            Ruta ruta = rutaBD.getRuta("Ruta 1", "Kanto").get();
            int numberPkRuta = rutaBD.getPokemons(ruta.getId()).size();
            assertTrue(rutaBD.addPokemon(ruta.getId(), "Caterpie"));
            List<RutaPokemon> pokemonsRuta = rutaBD.getPokemons(ruta.getId());
            assertEquals(numberPkRuta + 1, pokemonsRuta.size());
            RutaPokemon pokemon = new RutaPokemon("caterpie", ruta.getId(), 0, 100);
            assertTrue(pokemonsRuta.contains(pokemon));
        } catch (SQLIntegrityConstraintViolationException e){
            throw new Exception("No debería dar un error de integridad aquí");
        }
    }

    @Test
    void addPokemonConNiveles() throws Exception{
        try {
            Ruta ruta = rutaBD.getRuta("Ruta 1", "Kanto").get();
            int numberPkRuta = rutaBD.getPokemons(ruta.getId()).size();
            assertTrue(rutaBD.addPokemon(ruta.getId(), "Pikachu", 3, 5));
            List<RutaPokemon> pokemonsRuta = rutaBD.getPokemons(ruta.getId());
            RutaPokemon pokemon = pokemonsRuta.get(1);
            assertEquals("pikachu", pokemon.getPokemon());
            assertEquals(3, pokemon.getNivel_minimo());
            assertEquals(5, pokemon.getNivel_maximo());
        } catch (SQLIntegrityConstraintViolationException e){
            throw new Exception("No debería dar un error de integridad aquí");
        }
    }

    @Test
    void addPokemon_YaEsta(){
        Ruta ruta = rutaBD.getRuta("Ruta 1", "Kanto").get();
        int numberPkRuta = rutaBD.getPokemons(ruta.getId()).size();
        assertThrows(SQLIntegrityConstraintViolationException.class, () -> rutaBD.addPokemon(ruta.getId(), "Bulbasaur"));
        List<RutaPokemon> pokemonsRuta = rutaBD.getPokemons(ruta.getId());
        assertEquals(numberPkRuta , pokemonsRuta.size());
    }

    @Test
    void addPokemon_NoEsxiste() throws Exception{
        try {
            Ruta ruta = rutaBD.getRuta("Ruta 1", "Kanto").get();
            int numberPkRuta = rutaBD.getPokemons(ruta.getId()).size();
            assertFalse(rutaBD.addPokemon(ruta.getId(), "Jajbse"));
            List<RutaPokemon> pokemonsRuta = rutaBD.getPokemons(ruta.getId());
            assertEquals(numberPkRuta, pokemonsRuta.size());
        } catch (SQLIntegrityConstraintViolationException e){
            throw new Exception("No debería dar un error de integridad aquí");
        }
    }

    @Test
    void subirNivelesRuta(){
        Ruta ruta = rutaBD.getRuta("Ruta 1", "Kanto").get();
        int numberPkRuta = rutaBD.getPokemons(ruta.getId()).size();
        List<RutaPokemon> pokemonRutaViejo = rutaBD.getPokemons(ruta.getId());
        assertTrue(rutaBD.subirNivelesRuta(ruta.getId(), 4));
        List<RutaPokemon> pokemonRutaNuevo = rutaBD.getPokemons(ruta.getId());
        assertEquals(pokemonRutaViejo.get(0).getNivel_maximo() + 4 , pokemonRutaNuevo.get(0).getNivel_maximo());
    }

    @Test
    void removePokemonRuta(){
        int numberPkRuta = rutaBD.getPokemons(12).size();
        assertTrue(rutaBD.removePokemonRuta(12, 37));
        int nuevoNumberPkRuta = rutaBD.getPokemons(12).size();
        assertEquals(numberPkRuta -1, nuevoNumberPkRuta);
    }

    @Test
    void removePokemonRuta_NoEsta(){
        int numberPkRuta = rutaBD.getPokemons(12).size();
        assertFalse(rutaBD.removePokemonRuta(12, 1));
        int nuevoNumberPkRuta = rutaBD.getPokemons(12).size();
        assertEquals(numberPkRuta, nuevoNumberPkRuta);
    }
}