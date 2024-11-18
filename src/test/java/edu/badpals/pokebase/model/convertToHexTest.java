package edu.badpals.pokebase.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class convertToHexTest {
    @Test
    void convertToHex() {
        String holaHex = convertToHex.toHexadecimal("Hola");
        assertEquals("486f6c61", holaHex);
    }

}