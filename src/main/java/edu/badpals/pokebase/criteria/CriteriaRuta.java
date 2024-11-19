package edu.badpals.pokebase.criteria;

import java.util.Optional;

public class CriteriaRuta {
    Optional<String> pokemon;
    Optional<String> region;
    String criterio;
    String orden;

    public CriteriaRuta(String pokemon, String region, String criterio, String orden){
        this.pokemon = pokemon.equals("")?Optional.empty():Optional.of(pokemon);
        this.region = region.equals("Todas")?Optional.empty():Optional.of(region);
        this.criterio = criterio;
        this.orden = orden;
    }

    public CriteriaRuta(String pokemon, String region){
        this.pokemon = pokemon.equals("")?Optional.empty():Optional.of(pokemon);
        this.region = region.equals("Todas")?Optional.empty():Optional.of(region);
        this.criterio = "id";
        this.orden = "ASC";
    }

    public Optional<String> getPokemon() {
        return pokemon;
    }

    public Optional<String> getRegion() {
        return region;
    }

    public String getCriterio() {
        return criterio;
    }

    public String getOrden() {
        return orden;
    }

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
