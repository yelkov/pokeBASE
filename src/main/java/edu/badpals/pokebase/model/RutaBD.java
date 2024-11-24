package edu.badpals.pokebase.model;

import edu.badpals.pokebase.criteria.CriteriaRuta;
import edu.badpals.pokebase.service.ErrorLogger;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Maneja la interacción con la base de datos en relación con las rutas.
 * Permite la inserción, eliminación, actualización, y consulta de una ruta, así como de los Pokémon que pueden encontrarse en ella.
 */
public class RutaBD {
    /**
     * Conexión a la base de datos
     */
    private Connection connection;
    /**
     * Clase que permite conectarse a la parte de pokémons de la base de datos
     */
    private PokemonBD pokemonBD;

    /**
     * Constructor de la clase RutaBD.
     *
     * @param accesoBD Un objeto que proporciona la conexión a la base de datos. A partir de él se obtiene la conexión a la base de datos y un objeto para acceder a la información de pokémon.
     */
    public RutaBD(AccesoBD accesoBD) {
        this.connection = accesoBD.getConnection();
        pokemonBD = new PokemonBD(accesoBD);
    }

    /**
     * Obtiene el objeto PokemonBD asociado con esta instancia de RutaBD.
     *
     * @return El objeto PokemonBD asociado.
     */
    public PokemonBD getPokemonBD() {
        return pokemonBD;
    }

    /**
     * Obtiene el número total de rutas en la base de datos.
     *
     * @return El número total de rutas.
     */
    public int getRoutesCount(){
        try(Statement statement = connection.createStatement();){
            ResultSet resultSet = statement.executeQuery("select countAllRoutes()");
            resultSet.next();
            return resultSet.getInt(1);
        } catch (SQLException e){
            ErrorLogger.saveErrorLog("Error al ejecutar la query 'select countAllRoutes()' como parte del método getRoutesCount() de RutaBD: " + e.getMessage());
            return 0;
        }
    }

    /**
     * Obtiene el número de rutas en una región específica.
     *
     * @param region La región en la que contar las rutas.
     * @return El número de rutas en la región especificada.
     */
    public int getRoutesCount(String region){
        try(PreparedStatement statement = connection.prepareStatement("select countRoutesInRegion(?)")){
            statement.setString(1, region);
            ResultSet resultSet = statement.executeQuery();
            resultSet.next();
            return resultSet.getInt(1);
        } catch (SQLException e){
            ErrorLogger.saveErrorLog("Error al ejecutar la query 'select countAllRoutes(region)' como parte del método getRoutesCount(String region) de RutaBD: " + e.getMessage());
            return 0;
        }
    }

    /**
     * Obtiene el ID de una ruta a partir de su nombre y región.
     *
     * @param name El nombre de la ruta.
     * @param region La región donde se encuentra la ruta.
     * @return El ID de la ruta si existe, o 0 si no se encuentra.
     */
    public int getRutaId(String name, String region){
        try(PreparedStatement statement = connection.prepareStatement("Select FN_GET_ID_RUTA(?,?)");){
            statement.setString(1, name);
            statement.setString(2, region);
            ResultSet results = statement.executeQuery();
            if(results.next()) return results.getInt(1);
            else return 0;
        } catch (SQLException e){
            ErrorLogger.saveErrorLog("Error al ejecutar la query 'Select FN_GET_ID_RUTA(name,region)' como parte del método getRutaId(String name, String region) de RutaBD: " + e.getMessage());
            return 0;
        }
    }

    /**
     * Obtiene una ruta a partir de su ID, si existe.
     *
     * @param Id El ID de la ruta.
     * @return Un {@link Optional} que contiene la ruta si existe, o vacío si no se encuentra.
     */
     public Optional<Ruta> getRuta(int Id){
        try(PreparedStatement statement = connection.prepareStatement("select * from rutas where id = ?");){
            statement.setInt(1, Id);
            ResultSet results = statement.executeQuery();
            if (results.next()){
                int id = results.getInt(1);
                String name = results.getString(2);
                String region = results.getString(3);
                return Optional.of(new Ruta(id,name,region));
            } else {
                return Optional.empty();
            }
        } catch (SQLException e){
            ErrorLogger.saveErrorLog("Error al ejecutar la query 'select * from rutas where id = id' como parte del método getRuta(int Id) de RutaBD: " + e.getMessage());
            return Optional.empty();
        }
    }

    /**
     * Obtiene una ruta a partir de su nombre y región, si existe
     *
     * @param name El nombre de la ruta.
     * @param region La región en la que se encuentra la ruta.
     * @return Un {@link Optional} que contiene la ruta si existe, o vacío si no se encuentra.
     */
    public Optional<Ruta> getRuta(String name, String region){
        int id = getRutaId(name, region);
        return getRuta(id);
    }

    /**
     * Obtiene una lista con las rutas que coinciden con los filtros proporcionados.
     *
     * @param pokemon Un filtro opcional por nombre de Pokémon. Si no se especifica, no se aplica.
     * @param region Un filtro opcional por región. Si no se especifica, no se aplica.
     * @param criterio El campo por el que se ordenarán los resultados.
     * @param orden El orden de los resultados, que puede ser "ASC" o "DESC".
     * @return Una lista de rutas que coinciden con los filtros y en el orden establecido.
     */
    public List<Ruta> getRutasByFilters(Optional<String> pokemon, Optional<String> region, String criterio, String orden){
        StringBuilder basicSql = new StringBuilder("select * from rutas");
        List<Ruta> rutas = new ArrayList<>();
        List<String> filters = new ArrayList<>();

        if (pokemon.isPresent() || region.isPresent()){
            basicSql.append(" where ");
            if (region.isPresent()){
                filters.add("region = '" + region.get() + "'");
            }
            if (pokemon.isPresent()) {
                filters.add("id in (select ruta from rutas_pokemons where pokemon = (select FN_GET_ID_POKEMON('" + pokemon.get() + "')))");
            }
            basicSql.append(String.join(" AND ", filters));
        }

        basicSql.append(" Order by ")
                .append(criterio)
                .append(" ")
                .append(orden);

        try(PreparedStatement statement = connection.prepareStatement(basicSql.toString());){
            ResultSet resultSet = statement.executeQuery();
            while(resultSet.next()){
                int id = resultSet.getInt(1);
                String nombreRuta = resultSet.getString(2);
                String nombreRegion = resultSet.getString(3);
                rutas.add(new Ruta(id, nombreRuta, nombreRegion));
            }
        } catch (SQLException e){
            ErrorLogger.saveErrorLog("Error al ejecutar la query 'select * from rutas...' como parte del método getRutasByFilters(...) de RutaBD: " + e.getMessage());
        }
        return rutas;
    }

    /**
     * Obtiene todas las rutas que coinciden con los filtros proporcionados, usando siempre un criterio de orden ascendente por ID.
     *
     * @param pokemon Un filtro opcional por nombre de Pokémon. Si no se especifica, no se aplica.
     * @param region Un filtro opcional por región. Si no se especifica, no se aplica.
     * @return Una lista de rutas que coinciden con los filtros.
     */
    public List<Ruta> getRutasByFilters(Optional<String> pokemon, Optional<String> region) {
        return getRutasByFilters(pokemon, region, "id", "ASC");
    }

    /**
     * Obtiene todas las rutas que coinciden con los filtros proporcionados utilizando un objeto {@link CriteriaRuta}.
     *
     * @param criteriaRuta El objeto que contiene los filtros, criterio y orden.
     * @return Una lista de rutas que coinciden con los filtros.
     */
    public List<Ruta> getRutasByFilters(CriteriaRuta criteriaRuta){
        return getRutasByFilters(criteriaRuta.getPokemon(), criteriaRuta.getRegion(), criteriaRuta.getCriterio(),criteriaRuta.getOrden());
    }

    /**
     * Obtiene todas las regiones distintas guardadas en la base de datos.
     *
     * @return Una lista de nombres de regiones.
     */
    public List<String> getAllRegions(){
        List<String> regions = new ArrayList<>();
        try(PreparedStatement statement = connection.prepareStatement("select distinct region from rutas");){
            ResultSet resultSet = statement.executeQuery();
            while(resultSet.next()){
                regions.add(resultSet.getString(1));
            }
        } catch (SQLException e){
            ErrorLogger.saveErrorLog("Error al ejecutar la query 'select distinct region from rutas' como parte del método getAllRegions() de RutaBD: " + e.getMessage());
        }
        return regions;
    }

    /**
     * Inserta una nueva ruta en la base de datos.
     *
     * @param ruta El objeto {@link Ruta} que se insertará.
     * @return {@code true} si la ruta fue insertada exitosamente, {@code false} si ocurrió un error no esperado.
     * @throws SQLIntegrityConstraintViolationException Si ocurre una violación de la clave única al intentar insertar la ruta, es decir, si esta ya existe.
     */
    public boolean insertRuta(Ruta ruta) throws SQLIntegrityConstraintViolationException{
        String sqlSentence = """
            insert into rutas(NOMBRE, REGION)
            values (?,?)
            """;
        try(PreparedStatement statement = connection.prepareStatement(sqlSentence);){
            statement.setString(1, ruta.getNombre());
            statement.setString(2, ruta.getRegion());
            statement.executeUpdate();
            return true;
        } catch (SQLIntegrityConstraintViolationException pke){
            throw pke;
        } catch (SQLException e){
            ErrorLogger.saveErrorLog("Error al realizar el insert como parte del método insertRuta(Ruta ruta) de RutaBD: " + e.getMessage());
        }
        return false;
    }

    /**
     * Actualiza una ruta existente en la base de datos.
     *
     * @param ruta El objeto {@link Ruta} que contiene la información actualizada.
     * @return {@code true} si la ruta fue actualizada exitosamente, {@code false} si ocurrió un error inesperado.
     * @throws SQLIntegrityConstraintViolationException Si ocurre una violación de la clave única al intentar actualizar la ruta, es decir, si se introdujeron valores de rutas ya existentes.
     */
    public boolean updateRuta(Ruta ruta) throws SQLIntegrityConstraintViolationException{
        try(PreparedStatement statement = connection.prepareStatement("Update rutas set nombre = ?, region = ? where id = ?");){
            statement.setString(1, ruta.getNombre());
            statement.setString(2, ruta.getRegion());
            statement.setInt(3, ruta.getId());
            statement.executeUpdate();
            return true;
        } catch (SQLIntegrityConstraintViolationException ake) {
            throw ake;
        } catch (SQLException e){
            ErrorLogger.saveErrorLog("Error al realizar el insert como parte del método insertRuta(Ruta ruta) de RutaBD: " + e.getMessage());
        }
        return false;
    }

    /**
     * Elimina una ruta de la base de datos utilizando su ID.
     *
     * @param id El ID de la ruta que se desea eliminar.
     * @return {@code true} si la ruta fue eliminada exitosamente, {@code false} si no se encontró en la base de datos u ocurrió un error.
     */
    public boolean deleteRuta(int id){
        try(PreparedStatement statement = connection.prepareStatement("Delete from rutas where id = ?");){
            statement.setInt(1, id);
            int numberDeleted = statement.executeUpdate();
            if (numberDeleted == 1){
                return true;
            } else {
                return false;
            }
        } catch (SQLException e){
            ErrorLogger.saveErrorLog("Error al realizar el delete como parte del método deleteRuta(...) de RutaBD: " + e.getMessage());
            return false;
        }
    }

    /**
     * Elimina una ruta de la base de datos utilizando su nombre y región.
     *
     * @param name El nombre de la ruta a eliminar.
     * @param region La región donde se encuentra la ruta.
     * @return {@code true} si la ruta fue eliminada exitosamente, {@code false} si no se encontró en la base de datos u ocurrió un error.
     */
    public boolean deleteRuta(String name, String region){
        int id = getRutaId(name, region);
        return deleteRuta(id);
    }

    /**
     * Obtiene los Pokémon que pueden encontrarse en una ruta.
     *
     * @param rutaId El ID de la ruta.
     * @return Una lista de objetos {@link RutaPokemon} que representan los Pokémon de esa ruta, con su nivel mínimo y máximo.
     */
    public List<RutaPokemon> getPokemons(int rutaId){
        List<RutaPokemon> pokemons = new ArrayList<>();
        try(PreparedStatement statement = connection.prepareStatement("select p.nombre, rt.NIVEL_MINIMO, rt.NIVEL_MAXIMO from pokemons as p inner join rutas_pokemons as rt on p.id = rt.pokemon and rt.ruta = ?");){
            statement.setInt(1, rutaId);
            ResultSet results = statement.executeQuery();
            while (results.next()){
                RutaPokemon newPokemon = new RutaPokemon(results.getString(1), rutaId, results.getInt(2),results.getInt(3));
                pokemons.add(newPokemon);
            }
        } catch (SQLException e){
            ErrorLogger.saveErrorLog("Error al realizar la query 'select p.nombre, rt.NIVEL_MINIMO, rt.NIVEL_MAXIMO from pokemons as p inner join rutas_pokemons as rt on p.id = rt.pokemon and rt.ruta = rutaId' como parte del método getPokemons(int rutaId) de RutaBD: " + e.getMessage());
        }
        return pokemons;
    }

    /**
     * Añade un Pokémon a una ruta con un nivel mínimo y máximo especificado.
     *
     * @param rutaId El ID de la ruta a la que se le añadirá el Pokémon.
     * @param pokemonName El nombre del Pokémon que se añadirá.
     * @param nivel_minimo El nivel mínimo del Pokémon en la ruta.
     * @param nivel_maximo El nivel máximo del Pokémon en la ruta.
     * @return {@code true} si el Pokémon fue añadido exitosamente, {@code false} si ocurrió un error inesperado.
     * @throws SQLIntegrityConstraintViolationException Si ocurre una violación de la clave única al intentar añadir el Pokémon, es decir, si ya existe el pokemon en esa ruta.
     */
    public boolean addPokemon(int rutaId, String pokemonName, int nivel_minimo, int nivel_maximo) throws SQLIntegrityConstraintViolationException {
        Pokemon pokemon = pokemonBD.getPokemonByName(pokemonName);
        if (pokemon != null) {
            try (PreparedStatement statement = connection.prepareStatement("insert into rutas_pokemons(pokemon, ruta, NIVEL_MINIMO, NIVEL_MAXIMO) values(?,?, ?, ?)");) {
                statement.setInt(1, pokemon.getId());
                statement.setInt(2, rutaId);
                statement.setInt(3, nivel_minimo);
                statement.setInt(4, nivel_maximo);
                int rowsAffected = statement.executeUpdate();
                if (rowsAffected == 1) {
                    return true;
                } else {
                    return false;
                }
            } catch (SQLIntegrityConstraintViolationException pk) {
                throw pk;
            } catch (SQLException e) {
                ErrorLogger.saveErrorLog("Error al realizar la query 'insert into rutas_pokemons(pokemon, ruta, NIVEL_MINIMO, NIVEL_MAXIMO) values(rutaId,pokemonName, nivel_minimo, nivel_maximo)' como parte del método addPokemon(int rutaId, String pokemonName, int nivel_minimo, int nivel_maximo) de RutaBD: " + e.getMessage());
            }
            return false;
        }
        return false;
    }

    /**
     * Añade un Pokémon a una ruta con nivel mínimo 1 y nivel máximo 100.
     *
     * @param rutaId El ID de la ruta a la que se añadirá el Pokémon.
     * @param pokemonName El nombre del Pokémon que se añadirá.
     * @return {@code true} si el Pokémon fue añadido exitosamente, {@code false} si ocurrió un error inesperado.
     * @throws SQLIntegrityConstraintViolationException Si ocurre una violación de la clave única al intentar añadir el Pokémon.
     */
    public boolean addPokemon(int rutaId, String pokemonName) throws SQLIntegrityConstraintViolationException{
        return addPokemon(rutaId, pokemonName, 1, 100);
    }

    /**
     * Elimina un Pokémon de una ruta.
     *
     * @param rutaId El ID de la ruta.
     * @param pokemonId El ID del Pokémon a eliminar.
     * @return {@code true} si el Pokémon fue eliminado exitosamente, {@code false} si ocurrió un error.
     */
    public boolean removePokemonRuta(int rutaId, int pokemonId){
        try (PreparedStatement statement = connection.prepareStatement("delete from rutas_pokemons where ruta = ? and pokemon = ?");) {
            statement.setInt(1, rutaId);
            statement.setInt(2, pokemonId);
            int rowsAffected = statement.executeUpdate();
            if (rowsAffected == 1) {
                return true;
            } else {
                return false;
            }
        } catch (SQLException e) {
            ErrorLogger.saveErrorLog("Error al realizar la query 'delete from rutas_pokemons where ruta = rutaId and pokemon = pokemonId' como parte del método removePokemonRuta(int rutaId, int pokemonId) de RutaBD: " + e.getMessage());
            return false;
        }
    }

    /**
     * Incrementa (o disminuye) el nivel de los Pokémon en una ruta por una cantidad determinada.
     *
     * @param rutaId El ID de la ruta cuyo nivel de Pokémon se desea modificar.
     * @param niveles La cantidad de niveles a aumentar.
     * @return {@code true} si los niveles fueron modificados exitosamente, {@code false} si ocurrió un error.
     */
    public boolean subirNivelesRuta(int rutaId, int niveles){
        try(CallableStatement statement = connection.prepareCall("{call MODIFCIAR_NIVELES_EN_RUTA(?,?)}");){
            statement.setInt(1, rutaId);
            statement.setInt(2, niveles);
            statement.executeUpdate();
            return true;
        } catch (SQLException e){
            ErrorLogger.saveErrorLog("Error al realizar la query 'call MODIFCIAR_NIVELES_EN_RUTA(rutaId, niveles)' como parte del método subirNivelesRuta(int rutaId, int niveles) de RutaBD: " + e.getMessage());
            return false;
        }
    }
}
