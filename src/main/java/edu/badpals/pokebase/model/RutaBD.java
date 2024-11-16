package edu.badpals.pokebase.model;

import java.sql.*;

public class RutaBD {
    private static final String CONNECTION_ROUTE = "jdbc:mysql://localhost:3306/";
    private static final String USER = "root";
    private static final String PASSWORD = "root";
    private static final String DEFAULT_DATABASE = "bdpokemon";

    private Connection connection;
    private Statement statement;

    public RutaBD() {
    }

    public void connect(){
        try{
            connection = DriverManager.getConnection(CONNECTION_ROUTE + DEFAULT_DATABASE, USER, PASSWORD);
            statement = connection.createStatement();

        } catch (SQLException e){
            System.out.println("Error al conectarse a la base de datos");
            System.out.println(e.getMessage());
        }
    }

    public void connect(String dbName){
        try{
            connection = DriverManager.getConnection(CONNECTION_ROUTE + dbName, USER, PASSWORD);
            statement = connection.createStatement();

        } catch (SQLException e){
            System.out.println("Error al conectarse a la base de datos");
            System.out.println(e.getMessage());
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

    public void desconectarBD(){
        if(statement != null){
            try{
                statement.close();
            }catch (SQLException e){
                System.out.println("Error al cerrar el statement");
                e.printStackTrace();
            }finally{
                statement = null;
            }
        }if(connection != null){
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

    public int getRoutesCount(){
        try{
            ResultSet resultSet = statement.executeQuery("select countAllRoutes()");
            resultSet.next();
            return resultSet.getInt(1);
        } catch (SQLException e){
            System.out.println("Error al ejecutar la query");
            return 0;
        }
    }

    public int getRoutesCount(String Region){
        try(PreparedStatement statement = connection.prepareStatement("select countRoutesInRegion(?)")){
            statement.setString(1, Region);
            ResultSet resultSet = statement.executeQuery();
            resultSet.next();
            return resultSet.getInt(1);
        } catch (SQLException e){
            System.out.println("Error al ejecutar la query");
            return 0;
        }
    }

    public void insertRuta(Ruta ruta){
        String sqlSentence = """
            insert into rutas(NOMBRE, REGION)
            values (?,?)
            """;
        try(PreparedStatement statement = connection.prepareStatement(sqlSentence);){
            statement.setString(1, ruta.getNombre());
            statement.setString(2, ruta.getRegion());
            statement.executeUpdate();
        } catch (SQLException e){
            System.out.println("Error al hacer la inserción a la base de datos");
        }

    }


}
