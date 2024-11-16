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
    void getRutaId(){
        int ruta1 = rutaBD.getRutaId("Ruta 1", "Kanto");
        int ruta10 = rutaBD.getRutaId("Ruta 10", "Kanto");
        int rutaNoExiste = rutaBD.getRutaId("Ruta 10", "Unova");
        assertEquals(1, ruta1);
        assertEquals(10, ruta10);
        assertEquals(0, rutaNoExiste);

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
}