package edu.badpals.pokebase.criteria;

/**
 * Esta clase encapsula los criterios de filtrado y ordenación para consultar Pokémon en la base de datos.
 * Permite establecer filtros basados en tipos de Pokémon, los criterios de ordenación.
 */
public class CriteriaPokemon {
    /**
     * El primer tipo de Pokémon para el filtro.
     */
    String tipo1;
    /**
     * El segundo tipo de Pokémon para el filtro (puede ser nulo si no se especifica).
     */
    String tipo2;
    /**
     * El criterio por el cual se ordenarán los resultados (por ejemplo, id, nombre, etc.).
     */
    String criterio;
    /**
     * El orden de la consulta, que puede ser ascendente o descendente.
     */
    String orden;

    /**
     * Constructor para crear una instancia de CriteriaPokemon con los valores proporcionados.
     * Si el segundo tipo es vacío, se establece como null. Si el criterio es "---", se cambia a "Id" por defecto.
     *
     * @param tipo1    el primer tipo de Pokémon para el filtro.
     * @param tipo2    el segundo tipo de Pokémon para el filtro. Si está vacío, se asigna null.
     * @param criterio el criterio de ordenación.
     * @param orden    el orden de la consulta (ascendente o descendente).
     */
    public CriteriaPokemon(String tipo1, String tipo2, String criterio, String orden) {
        this.tipo1 = tipo1;
        if(tipo2.equals("")){
            this.tipo2 = null;
        }else{
            this.tipo2 = tipo2;
        }
        if(criterio.equals("---")){
            this.criterio = "Id";
        }else{
            this.criterio = criterio;
        }
        this.orden = orden;
    }

    /**
     * Obtiene el primer tipo de Pokémon.
     *
     * @return el primer tipo de Pokémon.
     */
    public String getTipo1() {
        return tipo1;
    }

    /**
     * Establece el primer tipo de Pokémon.
     *
     * @param tipo1 el nuevo primer tipo de Pokémon.
     */
    public void setTipo1(String tipo1) {
        this.tipo1 = tipo1;
    }

    /**
     * Obtiene el segundo tipo de Pokémon.
     *
     * @return el segundo tipo de Pokémon, o null si no está definido.
     */
    public String getTipo2() {
        return tipo2;
    }

    /**
     * Establece el segundo tipo de Pokémon.
     *
     * @param tipo2 el nuevo segundo tipo de Pokémon.
     */
    public void setTipo2(String tipo2) {
        this.tipo2 = tipo2;
    }

    /**
     * Obtiene el criterio de ordenación de los resultados.
     *
     * @return el criterio de ordenación.
     */
    public String getCriterio() {
        return criterio;
    }

    /**
     * Establece el criterio de ordenación de los resultados.
     *
     * @param criterio el nuevo criterio de ordenación.
     */
    public void setCriterio(String criterio) {
        this.criterio = criterio;
    }

    /**
     * Obtiene el orden de la consulta (ascendente o descendente).
     *
     * @return el orden de la consulta.
     */
    public String getOrden() {
        return orden;
    }

    /**
     * Establece el orden de la consulta (ascendente o descendente).
     *
     * @param orden el nuevo orden de la consulta.
     */
    public void setOrden(String orden) {
        this.orden = orden;
    }
}
