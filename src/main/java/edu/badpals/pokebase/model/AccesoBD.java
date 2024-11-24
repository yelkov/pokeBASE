package edu.badpals.pokebase.model;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

/**
 * Gestiona la conexión con una base de datos MySQL.
 * Proporciona métodos para conectar, desconectar, configurar transacciones.
 */
public class AccesoBD {
    /**
     * La ruta de conexión al servidor MySQL.
     */
    private static final String CONNECTION_ROUTE = "jdbc:mysql://localhost:3306/";
    /**
     * El nombre de usuario para la conexión a la base de datos.
     */
    private static final String USER = "root";
    /**
     * La contraseña del usuario para la conexión a la base de datos.
     */
    private static final String PASSWORD = "root";
    /**
     * La base de datos por defecto a la que se conecta.
     */
    private static final String DEFAULT_DATABASE = "bdpokemon";

    /**
     * La conexión a la base de datos.
     */
    private Connection connection;

    /**
     * Conecta a la base de datos por defecto utilizando las credenciales predeterminadas.
     * Si la conexión ya está establecida, no realiza ninguna acción, si falla la conexión da un mensaje de error.
     */
    public void connect(){
        try{
            if(connection == null){
                Properties propiedadesConexion = new Properties();
                propiedadesConexion.put("user", USER);
                propiedadesConexion.put("password", PASSWORD);
                connection = DriverManager.getConnection(CONNECTION_ROUTE+DEFAULT_DATABASE, propiedadesConexion);
            };

        } catch (SQLException e){
            System.out.println("Error al conectarse a la base de datos");
            System.out.println(e.getMessage());
        }
    }

    /**
     * Conecta a una base de datos concreta utilizando las credenciales predeterminadas.
     *
     * @param database el nombre de la base de datos a la que se conecta.
     */
    public void connect(String database){
        try{
            if(connection == null){
                Properties propiedadesConexion = new Properties();
                propiedadesConexion.put("user", USER);
                propiedadesConexion.put("password", PASSWORD);
                connection = DriverManager.getConnection(CONNECTION_ROUTE+database, propiedadesConexion);
            };

        } catch (SQLException e){
            System.out.println("Error al conectarse a la base de datos");
            System.out.println(e.getMessage());
        }
    }

    /**
     * Desconecta la conexión a la base de datos si está establecida.
     * Si ocurre un error al intentar cerrar la conexión, se maneja la excepción y la conexión se establece en <code>null</code>.
     */
    public void desconectarBD(){
        if(connection != null){
            try{
                connection.close();
            }catch (SQLException e){
                System.out.println("Error al cerrar la base de datos.");
                e.printStackTrace();
            }finally{
                connection = null;
            }
        }
    }

    /**
     * Configura el comportamiento de la transacción en la base de datos.
     * Si se establece a <code>true</code>, se habilita el auto-commit de las transacciones, lo que significa que cada sentencia SQL se ejecutará de forma inmediata.
     * Si se establece a <code>false</code>, el commit debe ser realizado manualmente.
     * Se emplea para realizar los tests y luego hacer un rollback para devolver la base de datos al estado inicial.
     *
     * @param autoCommit si se debe habilitar el auto-commit (true) o no (false).
     */
    public void setAutoCommit(boolean autoCommit){
        try{
            connection.setAutoCommit(autoCommit);
        } catch (SQLException e){
            System.out.println("Error al hacer el autocommit");
        }
    }

    /**
     * Realiza un commit en la base de datos, confirmando todas las operaciones realizadas dentro de una transacción.
     */
    public void commit(){
        try{
            connection.commit();
        } catch (SQLException e){
            System.out.println("Error al hacer el autocommit");
        }
    }

    /**
     * Realiza un rollback en la base de datos, deshaciendo todas las operaciones realziadas desde el último commit.
     */
    public void rollback(){
        try{
            connection.rollback();
        } catch (SQLException e){
            System.out.println("Error al hacer el autocommit");
        }
    }

    /**
     * Devuelve la conexión actual a la base de datos.
     *
     * @return la conexión activa a la base de datos.
     */
    public Connection getConnection() {
        return connection;
    }
}
