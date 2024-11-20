package edu.badpals.pokebase.auth;

import edu.badpals.pokebase.model.AccesoBD;
import edu.badpals.pokebase.model.RutaBD;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.sql.SQLIntegrityConstraintViolationException;

import static org.junit.jupiter.api.Assertions.*;

public class LogInManagerTest {
    static LogInManager manager;

    @BeforeAll
    static void setUp() {
        manager = new LogInManager();
        manager.connect("bdpokemon_users_test");
        manager.setAutoCommit(false);
    }

    @AfterAll
    static void tearDown() {
        manager.rollback();
        manager.desconectarBD();
    }

    @Test
    void signUp_Valid() throws Exception {
        try {
            assertTrue(manager.signUp("david", "1234"));
        } catch (SQLIntegrityConstraintViolationException e) {
            throw new Exception("No se debería lanzar una excepción de pk aquí");
        }
    }

    @Test
    void signUp_No_Valid() throws Exception {
        try {
            assertTrue(manager.signUp("administrador", "renaido"));
            assertThrows(SQLIntegrityConstraintViolationException.class, () -> manager.signUp("administrador", "renaido"));
        } catch (SQLIntegrityConstraintViolationException e) {
            throw new Exception("No se debería lanzar una excepción de pk en la primera inserción");
        }
    }

    @Test
    void logIn() throws Exception {
        try {
            assertTrue(manager.signUp("yelko", "1234"));
            assertTrue(manager.authenticate("yelko", "1234"));
            assertFalse(manager.authenticate("yelko", "renaido"));
        } catch (SQLIntegrityConstraintViolationException e) {
            throw new Exception("No se debería lanzar una excepción de pk aquí");
        }
    }
    @Test
    void logIn_No_Existe_Usuario() {
        assertFalse(manager.authenticate("kimMinseok", "1234"));
    }
}
