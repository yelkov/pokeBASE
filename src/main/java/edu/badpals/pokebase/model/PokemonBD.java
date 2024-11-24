package edu.badpals.pokebase.model;

import edu.badpals.pokebase.criteria.CriteriaPokemon;
import edu.badpals.pokebase.service.ErrorLogger;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Proporciona métodos para interactuar con la base de datos, permitiendo realizar operaciones
 * CRUD (crear, leer, actualizar, eliminar) sobre los datos de los Pokémon almacenados en la base de datos.
 * Esta clase también permite filtrar los Pokémon por tipo y ordenar los resultados.
 */
public class PokemonBD {

    /**
     * La conexión a la base de datos.
     */
    private Connection connection = null;

    /**
     * Constructor que inicializa la conexión a la base de datos utilizando el objeto <code>AccesoBD</code>, para obtener de él la conexión.
     *
     * @param accesoBD Objeto que maneja la conexión a la base de datos.
     */
    public PokemonBD(AccesoBD accesoBD) {
        this.connection = accesoBD.getConnection();
    }

    /**
     * Verifica si un Pokémon con el identificador proporcionado ya existe en la base de datos.
     *
     * @param id El identificador del Pokémon a verificar.
     * @return <code>true</code> si el Pokémon existe, <code>false</code> si no.
     */
    public boolean isIdPresent(int id){
        String query = "SELECT 1 FROM pokemons WHERE id = ?";
        try(PreparedStatement ps = connection.prepareStatement(query)){
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if(rs.next()){
                return true;
            }else{
                return false;
            }
        }catch (SQLException e) {
            ErrorLogger.saveErrorLog("Error al comprobar existencia de id "+e.getMessage());
            return false;
        }
    }

    /**
     * Verifica si un Pokémon con el nombre proporcionado ya existe en la base de datos.
     *
     * @param nombre El nombre del Pokémon a verificar.
     * @return <code>true</code> si el Pokémon existe, <code>false</code> si no.
     */
    public boolean isNombrePresent(String nombre){
        String query = "SELECT 1 FROM pokemons WHERE NOMBRE = ?";
        try(PreparedStatement ps = connection.prepareStatement(query)){
            ps.setString(1, nombre);
            ResultSet rs = ps.executeQuery();
            if(rs.next()){
                return true;
            }else{
                return false;
            }
        }catch (SQLException e) {
            ErrorLogger.saveErrorLog("Error al comprobar existencia de nombre "+e.getMessage());
            return false;
        }
    }

    /**
     * Obtiene un Pokémon de la base de datos a partir de su identificador.
     *
     * @param id El identificador del Pokémon a obtener.
     * @return El objeto <code>Pokemon</code> correspondiente, o <code>null</code> si no se encuentra.
     */
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
            ErrorLogger.saveErrorLog("Error al buscar pokemon por id en la base de datos "+e.getMessage());
        }
        return pokemon;
    }

    /**
     * Obtiene un Pokémon de la base de datos a partir de su nombre.
     *
     * @param nombrePokemon El nombre del Pokémon a obtener.
     * @return El objeto <code>Pokemon</code> correspondiente, o <code>null</code> si no se encuentra.
     */
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
            ErrorLogger.saveErrorLog("Error al buscar pokemon por nombre en la base de datos "+e.getMessage());
        }
        return pokemon;
    }

    /**
     * Obtiene una lista de Pokémon que cumplen con los filtros especificados en el objeto <code>CriteriaPokemon</code>.
     *
     * @param criteria El objeto con los criterios de búsqueda.
     * @return Una lista de objetos <code>Pokemon</code> que coinciden con los filtros.
     */
    public List<Pokemon> getPokemonByFilters(CriteriaPokemon criteria){
        String ordenacion = criteria.getCriterio() + " " + criteria.getOrden();
        return getPokemonsByType(criteria.getTipo1(), criteria.getTipo2(), ordenacion);
    }

    /**
     * Obtiene una lista de Pokémon de la base de datos filtrados por tipos y ordenados según se indica
     *
     * @param tipo1 El primer tipo de Pokémon a filtrar.
     * @param tipo2 El segundo tipo de Pokémon a filtrar (puede ser <code>null</code>).
     * @param ordenacion La ordenación para los resultados (puede ser <code>null</code>).
     * @return Una lista de objetos <code>Pokemon</code> que coinciden con los filtros y ordenados.
     */
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
            ErrorLogger.saveErrorLog("Error al buscar lista de pokemon por tipo en la base de datos "+e.getMessage());
        }
        return pokemons;
    }

    /**
     * Actualiza un Pokémon existente en la base de datos.
     *
     * @param antiguoId El identificador del Pokémon que se desea actualizar.
     * @param pokemon El objeto <code>Pokemon</code> con los nuevos datos.
     * @return <code>true</code> si la actualización fue exitosa, <code>false</code> si ocurrió un error.
     */
    public boolean updatePokemon(Integer antiguoId, Pokemon pokemon) {
        String query = """
                        UPDATE pokemons
                        SET NOMBRE = ?, ID = ? , IMAGEN = ?, IMAGEN_SHINY = ?, GIF = ?, TIPO_1 = ?, TIPO_2 = ?, EVOLUCIONA_DE = ?, METODO_EVOLUCION = ?
                        WHERE ID = ?
                        """;
        try(PreparedStatement ps = connection.prepareStatement(query)){
            ps.setString(1, pokemon.getNombre());
            ps.setInt(2, pokemon.getId());
            ps.setBytes(3, pokemon.getImagen());
            ps.setBytes(4, pokemon.getImagenShiny());
            ps.setBytes(5, pokemon.getGif());
            ps.setString(6, pokemon.getTipo1());
            ps.setString(7, pokemon.getTipo2());
            Integer evolucionaDe = pokemon.getEvolucionaDe();
            if (evolucionaDe != null) {
                ps.setInt(8, evolucionaDe);
            } else {
                ps.setObject(8, null, java.sql.Types.INTEGER);
            }
            ps.setString(9, pokemon.getMetodoEvolucion());
            ps.setInt(10, antiguoId);
            ps.executeUpdate();

            return true;
        }catch (SQLException e) {
            ErrorLogger.saveErrorLog("Error al modificar pokemon en la base de datos "+e.getMessage());
            return false;
        }
    }

    /**
     * Inserta un nuevo Pokémon en la base de datos.
     *
     * @param pokemon El objeto <code>Pokemon</code> a insertar.
     * @return <code>true</code> si la inserción fue exitosa, <code>false</code> si ocurrió un error.
     */
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

            Integer evolucionaDe = pokemon.getEvolucionaDe();
            if (evolucionaDe != null) {
                ps.setInt(8, evolucionaDe);
            } else {
                ps.setObject(8, null, java.sql.Types.INTEGER);
            }
            ps.setString(9, pokemon.getMetodoEvolucion());
            ps.executeUpdate();

            return true;

        }catch (SQLException e) {
            ErrorLogger.saveErrorLog("Error al insertar pokemon en la base de datos " + e.getMessage());
            return false;
        }
    }

    /**
     * Elimina un Pokémon de la base de datos.
     *
     * @param pokemon El objeto <code>Pokemon</code> a eliminar.
     * @return <code>true</code> si la eliminación fue exitosa, <code>false</code> si ocurrió un error.
     */
    public boolean deletePokemon(Pokemon pokemon) {
        String query = """
                    
                DELETE FROM pokemons
                    WHERE ID = ?""";
        try(PreparedStatement ps = connection.prepareStatement(query)){
            ps.setInt(1, pokemon.getId());
            ps.executeUpdate();

            return true;
        } catch (SQLException e) {
            ErrorLogger.saveErrorLog("Error al borrar pokemon en la base de datos "+e.getMessage());
            return false;
        }
    }
}
