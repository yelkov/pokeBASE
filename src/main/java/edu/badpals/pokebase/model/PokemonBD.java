package edu.badpals.pokebase.model;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class PokemonBD {

    private Connection connection = null;

    public PokemonBD(AccesoBD accesoBD) {
        this.connection = accesoBD.getConnection();
    }


    public Pokemon getPokemonById(int id) {
        Pokemon pokemon = null;
        String query = "SELECT * FROM pokemons WHERE id = ?";
        try (PreparedStatement ps = connection.prepareStatement(query)){
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            while (rs.next()){
                Integer idPokemon = rs.getInt("id");
                String nombre = rs.getString("nombre");
                byte[] imagen = rs.getBytes("imagen");
                byte[] imagenShiny = rs.getBytes("imagen_shiny");
                byte[] gif = rs.getBytes("gif");
                String tipo1 = rs.getString("tipo_1");
                String tipo2 = rs.getString("tipo_2");
                Integer evolucionaDe = rs.getInt("evoluciona_de");
                if (evolucionaDe == 0) evolucionaDe = null;
                String metodoEvolucion = rs.getString("metodo_evolucion");
                pokemon = new Pokemon(idPokemon,nombre,imagen,imagenShiny,gif,tipo1,tipo2,evolucionaDe,metodoEvolucion);
            }
            rs.close();
        } catch (SQLException e) {
            System.out.println("Error al buscar pokemon por id en la base de datos.");
            e.printStackTrace();
        }
        return pokemon;
    }

    public Pokemon getPokemonByName(String nombrePokemon) {
        Pokemon pokemon = null;
        String query = "SELECT * FROM pokemons WHERE nombre = ?";
        try(PreparedStatement ps = connection.prepareStatement(query)){
            ps.setString(1, nombrePokemon);
            ResultSet rs = ps.executeQuery();
            while (rs.next()){
                Integer id = rs.getInt("id");
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
        }catch (SQLException e) {
            System.out.println("Error al buscar pokemon por nombre en la base de datos.");
            e.printStackTrace();
        }
        return pokemon;
    }

    public List<Pokemon> getPokemonsByType(String tipo1, String tipo2 , String ordenacion) {
        List<Pokemon> pokemons = new ArrayList();
        String query = "SELECT * FROM pokemons WHERE ? IN (TIPO_1,TIPO_2)";
        if(tipo2 != null){
            query += " AND ? IN (TIPO_1,TIPO_2)";
        }
        if(ordenacion != null){
            query += " ORDER BY "+ordenacion;
        }
        try(PreparedStatement ps = connection.prepareStatement(query)){
            ps.setString(1, tipo1);
            if(tipo2 != null){
                ps.setString(2, tipo2);
            }

            ResultSet rs = ps.executeQuery();
            while(rs.next()){
                Integer id = rs.getInt("id");
                String nombre = rs.getString("nombre");
                byte[] imagen = rs.getBytes("imagen");
                byte[] imagenShiny = rs.getBytes("imagen_shiny");
                byte[] gif = rs.getBytes("gif");
                String tipo1pokemon = rs.getString("tipo_1");
                String tipo2pokemon = rs.getString("tipo_2");
                Integer evolucionaDe = rs.getInt("evoluciona_de");
                if (evolucionaDe == 0) evolucionaDe = null;
                String metodoEvolucion = rs.getString("metodo_evolucion");
                Pokemon pokemon = new Pokemon(id,nombre,imagen,imagenShiny,gif,tipo1pokemon,tipo2pokemon,evolucionaDe,metodoEvolucion);
                pokemons.add(pokemon);
            }
            rs.close();

        }catch (SQLException e) {
            System.out.println("Error al buscar lista de pokemon por tipo en la base de datos.");
            e.printStackTrace();
        }
        return pokemons;
    }

    public boolean updatePokemon(Pokemon pokemon) {
        String query = """
                        UPDATE pokemons
                        SET NOMBRE = ?, IMAGEN = ?, IMAGEN_SHINY = ?, GIF = ?, TIPO_1 = ?, TIPO_2 = ?, EVOLUCIONA_DE = ?, METODO_EVOLUCION = ?
                        WHERE ID = ?
                        """;
        try(PreparedStatement ps = connection.prepareStatement(query)){
            ps.setString(1, pokemon.getNombre());
            ps.setBytes(2, pokemon.getImagen());
            ps.setBytes(3, pokemon.getImagenShiny());
            ps.setBytes(4, pokemon.getGif());
            ps.setString(5, pokemon.getTipo1());
            ps.setString(6, pokemon.getTipo2());
            ps.setInt(7, pokemon.getEvolucionaDe());
            ps.setString(8, pokemon.getMetodoEvolucion());
            ps.setInt(9, pokemon.getId());
            ps.executeUpdate();

            return true;
        }catch (SQLException e) {
            System.out.println("Error al modificar pokemon en la base de datos.");
            e.printStackTrace();
            return false;
        }
    }

    public boolean insertPokemon(Pokemon pokemon){
        String query = """
                        INSERT INTO pokemons(ID,NOMBRE,IMAGEN,IMAGEN_SHINY,GIF,TIPO_1,TIPO_2,EVOLUCIONA_DE,METODO_EVOLUCION)
                        VALUES (?,?,?,?,?,?,?,?,?)
                        """;
        try(PreparedStatement ps = connection.prepareStatement(query)){
            ps.setInt(1, pokemon.getId());
            ps.setString(2, pokemon.getNombre());
            ps.setBytes(3, pokemon.getImagen());
            ps.setBytes(4, pokemon.getImagenShiny());
            ps.setBytes(5, pokemon.getGif());
            ps.setString(6, pokemon.getTipo1());
            ps.setString(7, pokemon.getTipo2());
            ps.setInt(8, pokemon.getEvolucionaDe());
            ps.setString(9, pokemon.getMetodoEvolucion());
            ps.executeUpdate();

            return true;

        }catch (SQLException e) {
            System.out.println("Error al insertar pokemon en la base de datos.");
            e.printStackTrace();

            return false;
        }
    }

    public boolean deletePokemon(Pokemon pokemon) {
        String query = """
                    
                DELETE FROM pokemons
                    WHERE ID = ?""";
        try(PreparedStatement ps = connection.prepareStatement(query)){
            ps.setInt(1, pokemon.getId());
            ps.executeUpdate();

            return true;
        } catch (SQLException e) {
            System.out.println("Error al borrar pokemon en la base de datos.");
            e.printStackTrace();

            return false;
        }
    }
}
