package edu.badpals.pokebase.model;

import edu.badpals.pokebase.criteria.CriteriaRuta;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class RutaBD {
    private Connection connection;
    private PokemonBD pokemonBD;

    public RutaBD(AccesoBD accesoBD) {
        this.connection = accesoBD.getConnection();
        pokemonBD = new PokemonBD(accesoBD);
    }

    public PokemonBD getPokemonBD() {
        return pokemonBD;
    }

    public int getRoutesCount(){
        try(Statement statement = connection.createStatement();){
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

    int getRutaId(String name, String region){
        try(PreparedStatement statement = connection.prepareStatement("Select FN_GET_ID_RUTA(?,?)");){
            statement.setString(1, name);
            statement.setString(2, region);
            ResultSet results = statement.executeQuery();
            results.next();
            return results.getInt(1);
        } catch (SQLException e){
            System.out.println("Error al recuperar los datos");
            return 0;
        }
    }

     Optional<Ruta> getRuta(int Id){
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
            System.out.println("Error al recuperar la ruta");
            System.out.println(e.getMessage());
            return Optional.empty();
        }
    }

    public Optional<Ruta> getRuta(String name, String region){
        int id = getRutaId(name, region);
        return getRuta(id);
    }

    public List<Ruta> getRutasByFilters(Optional<String> pokemon, Optional<String> region) {
        return getRutasByFilters(pokemon, region, "id", "ASC");
    }

    public List<Ruta> getRutasByFilters(CriteriaRuta criteriaRuta){
        return getRutasByFilters(criteriaRuta.getPokemon(), criteriaRuta.getRegion(), criteriaRuta.getCriterio(),criteriaRuta.getOrden());
    }

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
            System.out.println("Error al recuperar la lista de rutas");
            System.out.println(e.getMessage());
        }
        return rutas;
    }

    public List<String> getAllRegions(){
        List<String> regions = new ArrayList<>();
        try(PreparedStatement statement = connection.prepareStatement("select distinct region from rutas");){
            ResultSet resultSet = statement.executeQuery();
            while(resultSet.next()){
                regions.add(resultSet.getString(1));
            }
        } catch (SQLException e){
            System.out.println("error al cargar las regiones");
        }
        return regions;
    }

    public boolean insertRuta(Ruta ruta){
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
            return false;
        } catch (SQLException e){
            System.out.println("Error al hacer la inserción a la base de datos");
            return false;
        }
    }

    public boolean updateRuta(Ruta ruta){
        try(PreparedStatement statement = connection.prepareStatement("Update rutas set nombre = ?, region = ? where id = ?");){
            statement.setString(1, ruta.getNombre());
            statement.setString(2, ruta.getRegion());
            statement.setInt(3, ruta.getId());
            statement.executeUpdate();
            return true;
        } catch (SQLIntegrityConstraintViolationException ake) {
            return false;
        } catch (SQLException e){
            System.out.println("Error al hacer la inserción a la base de datos");
            return false;
        }
    }

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
            System.out.println("Error al borrar de la base de datos");
            return false;
        }
    }

    public boolean deleteRuta(String name, String region){
        int id = getRutaId(name, region);
        return deleteRuta(id);
    }

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
            System.out.println("error al realizar la operación");
        }
        return pokemons;
    }

    public boolean addPokemon(int rutaId, String pokemonName){
        return addPokemon(rutaId, pokemonName, 0, 100);
    }

    public boolean addPokemon(int rutaId, String pokemonName, int nivel_minimo, int nivel_maximo){
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
                return false;
            } catch (SQLException e) {
                System.out.println("Error");
                return false;
            }
        }
        return false;
    }

    public boolean subirNivelesRuta(int rutaId, int niveles){
        try(CallableStatement statement = connection.prepareCall("{call MODIFCIAR_NIVELES_EN_RUTA(?,?)}");){
            statement.setInt(1, rutaId);
            statement.setInt(2, niveles);
            statement.executeUpdate();
            return true;
        } catch (SQLException e){
            System.out.println(e.getMessage());
            return false;
        }
    }
}
