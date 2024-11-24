package edu.badpals.pokebase.criteria;

import java.util.Optional;

/**
 * Esta clase representa los criterios de filtrado y ordenación para consultar rutas en la base de datos.
 * Permite establecer filtros basados en un Pokémon y/o una región, y ordenarlos por un criterio de forma ascendente o descendente.
 */
public class CriteriaRuta {
    /**
     * El nombre del Pokémon para filtrar las rutas. Puede ser un valor presente o vacío (Optional.empty()) si no se especifica.
     */
    Optional<String> pokemon;
    /**
     * El nombre de la región para filtrar las rutas. Puede ser un valor presente o vacío (Optional.empty()) si toma el valor "Todas".
     */
    Optional<String> region;
    /**
     * El criterio de ordenación para las rutas (id, nombre, región).
     */
    String criterio;
    /**
     * El orden de la consulta, que puede ser ascendente ("ASC") o descendente ("DESC").
     */
    String orden;

    /**
     * Constructor que inicializa los criterios de filtrado y ordenación con los valores proporcionados.
     * Si el Pokémon o la región están vacíos, se asigna Optional.empty().
     *
     * @param pokemon el nombre del Pokémon para filtrar las rutas (si está vacío, se asigna Optional.empty()).
     * @param region el nombre de la región para filtrar las rutas (si es "Todas", se asigna Optional.empty()).
     * @param criterio el criterio de ordenación.
     * @param orden el orden de la consulta (ASC o DESC).
     */
    public CriteriaRuta(String pokemon, String region, String criterio, String orden){
        this.pokemon = pokemon.equals("")?Optional.empty():Optional.of(pokemon);
        this.region = region.equals("Todas")?Optional.empty():Optional.of(region);
        this.criterio = criterio;
        this.orden = orden;
    }

    /**
     * Constructor que inicializa los criterios de filtrado proporcionados y de ordenación por defecto (por id ascendente).
     * Si el Pokémon o la región están vacíos, se asigna Optional.empty().
     * Por defecto, se asignan los valores "id" para el criterio y "ASC" para el orden.
     *
     * @param pokemon el nombre del Pokémon para filtrar las rutas (si está vacío, se asigna Optional.empty()).
     * @param region el nombre de la región para filtrar las rutas (si es "Todas", se asigna Optional.empty()).
     */
    public CriteriaRuta(String pokemon, String region){
        this.pokemon = pokemon.equals("")?Optional.empty():Optional.of(pokemon);
        this.region = region.equals("Todas")?Optional.empty():Optional.of(region);
        this.criterio = "id";
        this.orden = "ASC";
    }

    /**
     * Obtiene el nombre del Pokémon para filtrar las rutas.
     *
     * @return un {@link Optional} que contiene el nombre del Pokémon si está presente, o vacío si no se especifica.
     */
    public Optional<String> getPokemon() {
        return pokemon;
    }

    /**
     * Obtiene el nombre de la región para filtrar las rutas.
     *
     * @return un {@link Optional} que contiene el nombre de la región si está presente, o vacío si no se especifica.
     */
    public Optional<String> getRegion() {
        return region;
    }

    /**
     * Obtiene el criterio de ordenación de las rutas.
     *
     * @return el criterio de ordenación ("id", "nombre", "región").
     */
    public String getCriterio() {
        return criterio;
    }

    /**
     * Obtiene el orden de la consulta (ascendente o descendente).
     *
     * @return el orden de la consulta ("ASC" o "DESC").
     */
    public String getOrden() {
        return orden;
    }

    /**
     * Devuelve una representación en cadena de los criterios de filtrado y ordenación de las rutas.
     * Si se especifican filtros para el Pokémon o la región, se incluye en la cadena. También se incluye el criterio
     * de ordenación y el tipo de orden.
     *
     * @return una cadena que describe los filtros y el orden aplicados.
     */
    @Override
    public String toString() {
        StringBuilder salida = new StringBuilder();
        if (pokemon.isPresent() || region.isPresent()){
            salida.append("filtrado por:");
            if(pokemon.isPresent()){
                salida.append(" pokemon = ")
                        .append(pokemon.get());
            }
            if (region.isPresent()){
                salida.append(" region = ")
                        .append(region.get());
            }
        } else{
            salida.append("Todas las rutas");
        }
        salida.append(" ordenado por ")
                .append(criterio)
                .append(" ")
                .append(orden);
        return salida.toString();
    }
}
