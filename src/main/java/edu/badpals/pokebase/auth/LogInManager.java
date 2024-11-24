package edu.badpals.pokebase.auth;

import edu.badpals.pokebase.service.ErrorLogger;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.*;
import java.util.Enumeration;
import java.util.HexFormat;
import java.util.Properties;

/**
 * La clase LogInManager gestiona la autentificación de usuarios en la aplicación,
 * mediante una base de datos MySQL dónde se almacenan los usuario con sus respectivas contraseñas cifradas.
 * Permite tanto autentificar usuarios como registrar nuevos.
 */
public class LogInManager {

    // Constantes para la conexión a la base de datos
    private final String DATABASE_SERVICE = "jdbc:mysql://localhost:3306/";
    private final String DATABASE_DEFAULT = "bdpokemon_users";
    private final String USERNAME = "root";
    private final String PASSWORD = "root";

    // Conexión a la base de datos
    private Connection connection;

    /**
     * Conecta a la base de datos predeterminada especificada en DATABASE_DEFAULT.
     */
    public void connect() {
        try {
            if (connection == null) {
                Properties propiedadesConexion = new Properties();
                propiedadesConexion.put("user", USERNAME);
                propiedadesConexion.put("password", PASSWORD);
                connection = DriverManager.getConnection(DATABASE_SERVICE + DATABASE_DEFAULT, propiedadesConexion);
            }
        } catch (SQLException e) {
            ErrorLogger.saveErrorLog("Error al conectarse a la base de datos de autentificación, " + e.getMessage());
        }
    }

    /**
     * Conecta a una base de datos específica, diferente de DATABASE_DEFAULT.
     *
     * @param database El nombre de la base de datos a la que se desea conectar.
     */
    public void connect(String database) {
        try {
            if (connection == null) {
                Properties propiedadesConexion = new Properties();
                propiedadesConexion.put("user", USERNAME);
                propiedadesConexion.put("password", PASSWORD);
                connection = DriverManager.getConnection(DATABASE_SERVICE + database, propiedadesConexion);
            }
        } catch (SQLException e) {
            ErrorLogger.saveErrorLog("Error al conectarse a la base de datos de autentificación, " + e.getMessage());
        }
    }

    /**
     * Cierra la conexión a la base de datos si está activa.
     */
    public void desconectarBD() {
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException e) {
                ErrorLogger.saveErrorLog("Error al desconectarse a la base de datos de autentificación, " + e.getMessage());
            } finally {
                connection = null;
            }
        }
    }

    /**
     * Establece si las operaciones deben ser confirmadas automáticamente.
     * Lo usamos para desactivar esta opción durante los tests.
     *
     * @param autoCommit {@code true} para activar auto-commit; {@code false} para desactivarlo.
     */
    public void setAutoCommit(boolean autoCommit) {
        try {
            connection.setAutoCommit(autoCommit);
        } catch (SQLException e) {
            System.out.println("Error al configurar auto-commit.");
        }
    }

    /**
     * Realiza un rollback de las transacciones pendientes.
     * Lo empleamos para deshacer los cambios hechos durante los tests.
     */
    public void rollback() {
        try {
            connection.rollback();
        } catch (SQLException e) {
            System.out.println("Error al realizar rollback.");
        }
    }

    /**
     * Autentifica a un usuario verificando su contraseña con la cifrada correspondiente almacenada en la base de datos.
     *
     * @param user     El nombre de usuario.
     * @param password La contraseña proporcionada.
     * @return {@code true} si las credenciales son correctas; {@code false} en caso contrario.
     */
    public boolean authenticate(String user, String password) {
        try (PreparedStatement statement = connection.prepareStatement("select contrasinal from users where usuario = ?")) {

            statement.setString(1, user);

            ResultSet results = statement.executeQuery();

            if (results.next()) {
                String correctPassword = results.getString(1);
                String hashedPassword = generateHash(password);
                return hashedPassword.equals(correctPassword);
            } else {
                return false;
            }

        } catch (SQLException e) {
            ErrorLogger.saveErrorLog("Error leyendo de la base de datos, " + e.getMessage());
            return false;
        }
    }

    /**
     * Registra un nuevo usuario en la base de datos.
     *
     * @param user     El nombre del usuario.
     * @param password La contraseña del usuario.
     * @return {@code true} si el registro fue exitoso; {@code false} si ocurrió un error.
     * @throws SQLIntegrityConstraintViolationException Si el nombre de usuario ya está registrado.
     */
    public boolean signUp(String user, String password) throws SQLIntegrityConstraintViolationException {
        try (PreparedStatement statement = connection.prepareStatement("insert into users(usuario, contrasinal) values (?,?)")) {
            statement.setString(1, user);
            statement.setString(2, generateHash(password));
            int rowsAffected = statement.executeUpdate();
            return rowsAffected == 1;
        } catch (SQLIntegrityConstraintViolationException pk) {
            throw pk;
        } catch (SQLException e) {
            ErrorLogger.saveErrorLog("Error al guardar en la base de datos, " + e.getMessage());
            return false;
        }
    }

    /**
     * Genera un hash SHA-256 para un texto proporcionado.
     * Lo usaremos para cifrar las contraseñas que guardamos en la base de datos.
     *
     * @param text El texto a ser hasheado.
     * @return Una cadena hexadecimal representando el hash o {@code null} si ocurre un error.
     */
    private String generateHash(String text) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(text.getBytes(StandardCharsets.UTF_8));
            return HexFormat.of().formatHex(hash);
        } catch (NoSuchAlgorithmException e) {
            ErrorLogger.saveErrorLog("Hashing algorithm not found.");
        }
        return null;
    }
}

