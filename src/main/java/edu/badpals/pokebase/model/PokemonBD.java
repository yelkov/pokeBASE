package edu.badpals.pokebase.model;

import java.sql.*;
import java.util.Properties;

public class PokemonBD {
    private static final String URL = "jdbc:mysql://localhost:3306/bdpokemon";
    private static final String USER = "root";
    private static final String PASS = "root";
    private Connection connection = null;
    private Statement statement = null;

    public void conectarBD(){
        try{
            if(connection == null){
                Properties propiedadesConexion = new Properties();
                propiedadesConexion.put("user", USER);
                propiedadesConexion.put("password", PASS);
                connection = DriverManager.getConnection(URL, propiedadesConexion);
            }if (statement == null){
                statement = connection.createStatement();
            }
        }catch (SQLException e){
        System.out.println("Error al conectar la base de datos");
        e.printStackTrace();
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

    public Pokemon getPokemonById(int id) {
        Pokemon pokemon = null;
        String query = "SELECT * FROM pokemons WHERE id = ?";
        try (PreparedStatement ps = connection.prepareStatement(query)){
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            while (rs.next()){
                String nombre = rs.getString("nombre");
                byte[] imagen = rs.getBytes("imagen");
                byte[] imagenShiny = rs.getBytes("imagen_shiny");
                byte[] gif = rs.getBytes("gif");
                String tipo1 = rs.getString("tipo_1");
                String tipo2 = rs.getString("tipo_2");
                Integer evolucionaDe = rs.getInt("evoluciona_de");
                if (evolucionaDe == 0) evolucionaDe = null;
                String metodoEvolucion = rs.getString("metodo_evolucion");
                pokemon = new Pokemon(id,nombre,imagen,imagenShiny,gif,tipo1,tipo2,evolucionaDe,metodoEvolucion);
            }
            rs.close();
        } catch (SQLException e) {
            System.out.println("Error al cerrar la base de datos.");
            e.printStackTrace();
        }
        return pokemon;
    }
}
