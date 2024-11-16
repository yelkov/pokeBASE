package edu.badpals.pokebase.model;

import org.junit.jupiter.api.*;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class RutaBDTest {
    static RutaBD rutaBD;

    @BeforeAll
    static void setUp() {
        rutaBD = new RutaBD();
        rutaBD.connect("bdpokemon_test");
        rutaBD.setAutoCommit(false);
    }

    @AfterAll
    static void tearDown() {
        rutaBD.rollback();
        rutaBD.desconectarBD();
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
    void insertRuta() {
        Ruta ruta = new Ruta(1, "Ruta 1", "Unova");
        rutaBD.insertRuta(ruta);
        int totalRutas = rutaBD.getRoutesCount();
        assertEquals(40, totalRutas);
        //Problemas con los id's autogenerados
    }

    @Test
    void testGetRoutesCount() {
        int rutasKanto = rutaBD.getRoutesCount("Kanto");
        int rutasUnova = rutaBD.getRoutesCount("Unova");
        int rutasJohto = rutaBD.getRoutesCount("Johto");
        assertEquals(38, rutasKanto);
        assertEquals(1, rutasUnova);
        assertEquals(1, rutasJohto);
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
    void modifyRuta(){
        Ruta ruta  = rutaBD.getRuta("Ruta 1", "Kanto").get();
        ruta.setNombre("Ruta 5");
        ruta.setRegion("Alola");
        int id = ruta.getId();
        rutaBD.updateRuta(ruta);
        Optional<Ruta> isRuta = rutaBD.getRuta(id);
        assertTrue(isRuta.isPresent());
        Ruta rutaModificada = isRuta.get();
        assertEquals("Ruta 5", rutaModificada.getNombre());
        assertEquals("Alola", rutaModificada.getRegion());
        ruta.setNombre("Ruta 1");
        ruta.setRegion("Kanto");
        rutaBD.updateRuta(ruta);
    }

    @Test
    void getRutabyNameRegion_DoesntExist(){
        Optional<Ruta> isRuta = rutaBD.getRuta("Ruta 10", "Unova");
        assertTrue(isRuta.isEmpty());
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
}