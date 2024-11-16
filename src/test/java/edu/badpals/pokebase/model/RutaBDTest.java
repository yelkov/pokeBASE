package edu.badpals.pokebase.model;

import org.junit.jupiter.api.*;

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
    void insertRuta() {
        Ruta ruta = new Ruta(1, "Ruta 1", "Unova");
        rutaBD.insertRuta(ruta);
        int totalRutas = rutaBD.getRoutesCount();
        assertEquals(39, totalRutas);
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
}