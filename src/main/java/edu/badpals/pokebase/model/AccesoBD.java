package edu.badpals.pokebase.model;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

public class AccesoBD {
    private static final String CONNECTION_ROUTE = "jdbc:mysql://localhost:3306/";
    private static final String USER = "root";
    private static final String PASSWORD = "root";
    private static final String DEFAULT_DATABASE = "bdpokemon";

    private Connection connection;

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

    public void setAutoCommit(boolean autoCommit){
        try{
            connection.setAutoCommit(autoCommit);
        } catch (SQLException e){
            System.out.println("Error al hacer el autocommit");
        }
    }

    public void commit(){
        try{
            connection.commit();
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

    public Connection getConnection() {
        return connection;
    }
}
