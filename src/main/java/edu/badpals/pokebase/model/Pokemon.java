package edu.badpals.pokebase.model;

import java.io.Serializable;
import java.util.Objects;

/**
 * Representa un Pokémon de la base de datos en la aplicación, incluyendo sus atributos
 * principales como nombre, tipo, imágenes y detalles sobre su evolución.
 * Esta clase es serializable para poder ser almacenada o transferida de forma persistente.
 */
public class Pokemon implements Serializable {

    /**
     * El identificador único del Pokémon.
     */
    private Integer id;
    /**
     * El nombre del Pokémon.
     */
    private String nombre;
    /**
     * La imagen en formato binario del Pokémon.
     */
    private byte[] imagen;
    /**
     * La imagen "shiny" en formato binario del Pokémon.
     */
    private byte[] imagenShiny;
    /**
     * El gif en formato binario del Pokémon.
     */
    private byte[] gif;
    /**
     * El primer tipo del Pokémon (por ejemplo, "Fuego").
     */
    private String tipo1;
    /**
     * El segundo tipo del Pokémon (por ejemplo, "Volador"). Puede ser <code>null</code> si no tiene un segundo tipo.
     */
    private String tipo2;
    /**
     * El ID de la preevolución, o <code>null</code> si no evoluciona de otro Pokémon.
     */
    private Integer evolucionaDe;
    /**
     * El método de evolución al Pokémon (por ejemplo, "nivel 16").
     */
    private String metodoEvolucion;

    /**
     * Constructor vacío para la clase.
     */
    public Pokemon() {}

    /**
     * Constructor con todos los atributos de un Pokémon.
     *
     * @param id El identificador único del Pokémon.
     * @param nombre El nombre del Pokémon.
     * @param imagen La imagen del Pokémon.
     * @param imagenShiny La imagen "shiny" del Pokémon.
     * @param gif El gif del Pokémon.
     * @param tipo1 El primer tipo del Pokémon.
     * @param tipo2 El segundo tipo del Pokémon (puede ser <code>null</code>).
     * @param evolucionaDe El ID de la preevolución.
     * @param metodoEvolucion El método de evolución al Pokémon.
     */
    public Pokemon(Integer id, String nombre, byte[] imagen, byte[] imagenShiny, byte[] gif, String tipo1, String tipo2, Integer evolucionaDe, String metodoEvolucion) {
        this.id = id;
        this.nombre = nombre;
        this.imagen = imagen;
        this.imagenShiny = imagenShiny;
        this.gif = gif;
        this.tipo1 = tipo1;
        this.tipo2 = tipo2;
        this.evolucionaDe = evolucionaDe;
        this.metodoEvolucion = metodoEvolucion;
    }

    /**
     * Devuelve el identificador del Pokémon.
     *
     * @return El ID del Pokémon.
     */
    public Integer getId() {
        return id;
    }

    /**
     * Establece el identificador del Pokémon.
     *
     * @param id El nuevo ID del Pokémon.
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * Devuelve el nombre del Pokémon.
     *
     * @return El nombre del Pokémon.
     */
    public String getNombre() {
        return nombre;
    }

    /**
     * Establece el nombre del Pokémon.
     *
     * @param nombre El nuevo nombre del Pokémon.
     */
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    /**
     * Devuelve la imagen del Pokémon en formato binario.
     *
     * @return La imagen del Pokémon.
     */
    public byte[] getImagen() {
        return imagen;
    }

    /**
     * Establece la imagen del Pokémon en formato binario.
     *
     * @param imagen La nueva imagen del Pokémon.
     */
    public void setImagen(byte[] imagen) {
        this.imagen = imagen;
    }

    /**
     * Devuelve la imagen "shiny" del Pokémon en formato binario.
     *
     * @return La imagen "shiny" del Pokémon.
     */
    public byte[] getImagenShiny() {
        return imagenShiny;
    }

    /**
     * Establece la imagen "shiny" del Pokémon en formato binario.
     *
     * @param imagenShiny La nueva imagen "shiny" del Pokémon.
     */
    public void setImagenShiny(byte[] imagenShiny) {
        this.imagenShiny = imagenShiny;
    }

    /**
     * Devuelve el gif del Pokémon en formato binario.
     *
     * @return El gif del Pokémon.
     */
    public byte[] getGif() {
        return gif;
    }

    /**
     * Establece el gif del Pokémon en formato binario.
     *
     * @param gif El nuevo gif del Pokémon.
     */
    public void setGif(byte[] gif) {
        this.gif = gif;
    }

    /**
     * Devuelve el primer tipo del Pokémon.
     *
     * @return El primer tipo del Pokémon.
     */
    public String getTipo1() {
        return tipo1;
    }

    /**
     * Establece el primer tipo del Pokémon.
     *
     * @param tipo1 El nuevo primer tipo del Pokémon.
     */
    public void setTipo1(String tipo1) {
        this.tipo1 = tipo1;
    }

    /**
     * Devuelve el segundo tipo del Pokémon (puede ser <code>null</code>).
     *
     * @return El segundo tipo del Pokémon.
     */
    public String getTipo2() {
        return tipo2;
    }

    /**
     * Establece el segundo tipo del Pokémon.
     *
     * @param tipo2 El nuevo segundo tipo del Pokémon.
     */
    public void setTipo2(String tipo2) {
        this.tipo2 = tipo2;
    }

    /**
     * Devuelve el ID del Pokémon del cual evoluciona este Pokémon.
     *
     * @return El ID del Pokémon del cual evoluciona este Pokémon.
     */
    public Integer getEvolucionaDe() {
        return evolucionaDe;
    }

    /**
     * Establece el ID del Pokémon del cual evoluciona este Pokémon.
     *
     * @param evolucionaDe El nuevo ID del Pokémon del cual evoluciona este Pokémon.
     */
    public void setEvolucionaDe(Integer evolucionaDe) {
        this.evolucionaDe = evolucionaDe;
    }

    /**
     * Devuelve el método por el que la preevolución evoluciona al Pokémon (por ejemplo, "nivel 16").
     *
     * @return El método de evolución del Pokémon.
     */
    public String getMetodoEvolucion() {
        return metodoEvolucion;
    }

    /**
     * Establece el método de evolución del Pokémon.
     *
     * @param metodoEvolucion El nuevo método de evolución del Pokémon.
     */
    public void setMetodoEvolucion(String metodoEvolucion) {
        this.metodoEvolucion = metodoEvolucion;
    }

    /**
     * Compara el objeto actual con otro objeto para determinar si son iguales.
     * Dos objetos <code>Pokemon</code> son iguales si tienen el mismo ID.
     *
     * @param o El objeto a comparar con el Pokémon actual.
     * @return <code>true</code> si los objetos son iguales, <code>false</code> en caso contrario.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Pokemon pokemon)) return false;
        return Objects.equals(id, pokemon.id);
    }

    /**
     * Devuelve el código hash del objeto Pokémon, basado en su ID.
     *
     * @return El código hash del Pokémon.
     */
    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    /**
     * Devuelve una representación en forma de cadena del Pokémon, mostrando su nombre, ID,
     * tipos, información de evolución y método de evolución.
     *
     * @return Una cadena que representa al Pokémon.
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("\n\tNombre: " + nombre);
        sb.append("\n\tId: " + id);
        sb.append("\n\tTipo 1: " + tipo1);
        sb.append((tipo2 != null? "\n\tTipo 2: " + tipo2 : ""));
        sb.append((evolucionaDe != null? "\n\tEvoluciona del pokemon con id: " + evolucionaDe : ""));
        sb.append((metodoEvolucion != null? "\n\t\tSu método de evolución es: " + metodoEvolucion : ""));
        return sb.toString();
    }
}
