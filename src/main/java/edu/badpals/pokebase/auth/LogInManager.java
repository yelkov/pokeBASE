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
 * Clase que gestiona la autenticación de usuarios, incluyendo el inicio de sesión y la creación de nuevos usuarios.
 *
 * Esta clase se basa en un archivo de propiedades que almacena las credenciales (usuario y contraseñas cifradas)
 * y utiliza el algoritmo de hash SHA-256 para la seguridad de las contraseñas.
 *
 * @author David Búa - @BuaTeijeiro
 * @author Yelko Veiga - @yelkov
 * @version 1.0
 */
public class LogInManager {

    // Ruta al archivo que contiene las credenciales de los usuarios.
    private final String DATABASE_SERVICE = "jdbc:mysql://localhost:3306/";
    private final String DATABASE_DEFAULT = "bdpokemon_users";

    private final String USERNAME = "root";
    private final String PASSWORD = "root";
    private Connection connection;

    public void connect(){
        try{
            if(connection == null){
                Properties propiedadesConexion = new Properties();
                propiedadesConexion.put("user", USERNAME);
                propiedadesConexion.put("password", PASSWORD);
                connection = DriverManager.getConnection(DATABASE_SERVICE + DATABASE_DEFAULT, propiedadesConexion);
            };

        } catch (SQLException e){
            ErrorLogger.saveErrorLog("Error al conectarse a la base de datos de autentificación, " + e.getMessage());
        }
    }

    public void connect(String database){
        try{
            if(connection == null){
                Properties propiedadesConexion = new Properties();
                propiedadesConexion.put("user", USERNAME);
                propiedadesConexion.put("password", PASSWORD);
                connection = DriverManager.getConnection(DATABASE_SERVICE + database, propiedadesConexion);
            };

        } catch (SQLException e){
            ErrorLogger.saveErrorLog("Error al conectarse a la base de datos de autentificación, " + e.getMessage());
        }
    }

    public void desconectarBD(){
        if(connection != null){
            try{
                connection.close();
            }catch (SQLException e){
                ErrorLogger.saveErrorLog("Error al desconectarse a la base de datos de autentificación, " + e.getMessage());
            }finally{
                connection = null;
            }
        }
    }

    public void setAutoCommit(boolean autoCommit){
        try{
            connection.setAutoCommit(autoCommit);
        } catch (SQLException e){
            System.out.println("Error al hacer el autocommit");
        }
    }

    public void rollback(){
        try{
            connection.rollback();
        } catch (SQLException e){
            System.out.println("Error al hacer el autocommit");
        }
    }

    /**
     * Autentica a un usuario verificando el nombre de usuario y la contraseña proporcionados.
     *
     * @param user El nombre de usuario proporcionado.
     * @param password La contraseña proporcionada (en texto plano).
     * @return {@code true} si el nombre de usuario y la contraseña son correctos, {@code false} en caso contrario.
     */
    public boolean authenticate(String user, String password) {
        try (PreparedStatement statement = connection.prepareStatement("select contrasinal from users where usuario = ?")) {

            statement.setString(1, user);

            ResultSet results = statement.executeQuery();

            if(results.next()){
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
     * Registra a un nuevo usuario, almacenando su nombre de usuario y la contraseña cifrada en el archivo de propiedades.
     *
     * @param user El nombre de usuario que desea registrarse.
     * @param password La contraseña del nuevo usuario (en texto plano).
     * @return {@code true} si el registro fue exitoso, {@code false} si el usuario ya existe.
     */
    public boolean signUp(String user, String password) throws SQLIntegrityConstraintViolationException {
        try (PreparedStatement statement = connection.prepareStatement("insert into users(usuario, contrasinal) values (?,?)");) {
            statement.setString(1, user);
            statement.setString(2, generateHash(password));
            int rowsAffected = statement.executeUpdate();
            return rowsAffected == 1;
        } catch (SQLIntegrityConstraintViolationException pk) {
            throw pk;
        } catch (SQLException e) {
            // En caso de error, registrar el error utilizando el ErrorLogger
            ErrorLogger.saveErrorLog("Error al guardar en la base de datos, " + e.getMessage());
            return false;
        }
    }

    /**
     * Genera un hash SHA-256 para la contraseña proporcionada.
     *
     * @param text La contraseña en texto plano.
     * @return El hash de la contraseña en formato hexadecimal.
     */
    private String generateHash(String text){
        try{
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(text.getBytes(StandardCharsets.UTF_8));

            // Formatea el hash en una cadena hexadecimal
            return HexFormat.of().formatHex(hash);

        } catch (NoSuchAlgorithmException e) {
            // Si el algoritmo de hashing no está disponible, registrar el error
            ErrorLogger.saveErrorLog("Hashing algorithm not found.");
        }
        return null;
    }
}
