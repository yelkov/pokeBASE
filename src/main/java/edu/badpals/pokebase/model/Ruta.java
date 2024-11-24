package edu.badpals.pokebase.model;

/**
 * Representa una ruta guardada en la base de datos
 * Se utiliza para modelar la información de las rutas en las que un Pokemon puede ser encontrado.
 */
public class Ruta {
    /**
     * El identificador único de la ruta.
     */
    private int id;
    /**
     * El nombre de la ruta.
     */
    private String nombre;
    /**
     * La región en la que se encuentra la ruta.
     */
    private String region;

    /**
     * Constructor que inicializa una nueva ruta con los valores especificados.
     *
     * @param id El identificador único de la ruta.
     * @param nombre El nombre de la ruta.
     * @param region La región en la que se encuentra la ruta.
     */
    public Ruta(int id, String nombre, String region) {
        this.id = id;
        this.nombre = nombre;
        this.region = region;
    }

    /**
     * Devuelve el identificador único de la ruta.
     *
     * @return El identificador de la ruta.
     */
    public int getId() {
        return id;
    }

    /**
     * Establece el identificador único de la ruta.
     *
     * @param id El nuevo identificador de la ruta.
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Devuelve el nombre de la ruta.
     *
     * @return El nombre de la ruta.
     */
    public String getNombre() {
        return nombre;
    }

    /**
     * Establece el nombre de la ruta.
     *
     * @param nombre El nuevo nombre de la ruta.
     */
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    /**
     * Devuelve la región en la que se encuentra la ruta.
     *
     * @return La región de la ruta.
     */
    public String getRegion() {
        return region;
    }

    /**
     * Establece la región en la que se encuentra la ruta.
     *
     * @param region La nueva región de la ruta.
     */
    public void setRegion(String region) {
        this.region = region;
    }

    /**
     * Devuelve una representación en forma de cadena de la ruta, que incluye su nombre y la región en la que se encuentra.
     *
     * @return Una cadena representando la ruta en el formato "nombre, región".
     */
    @Override
    public String toString() {
        return getNombre() + ", " + getRegion();
    }
}
