package edu.badpals.pokebase.model;

import org.junit.jupiter.api.*;

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
        assertEquals(38, totalRutas);
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
        assertEquals(39, totalRutas);
        //Problemas con los id's autogenerados
    }

    @Test
    void testGetRoutesCount() {
        int rutasKanto = rutaBD.getRoutesCount("Kanto");
        int rutasUnova = rutaBD.getRoutesCount("Unova");
        int rutasJohto = rutaBD.getRoutesCount("Johto");
        assertEquals(38, rutasKanto);
        assertEquals(1, rutasUnova);
        assertEquals(0, rutasJohto);
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



}