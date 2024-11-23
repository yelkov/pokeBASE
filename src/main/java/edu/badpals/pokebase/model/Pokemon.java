package edu.badpals.pokebase.model;

import java.io.Serializable;
import java.util.Objects;

public class Pokemon implements Serializable {
    private Integer id;
    private String nombre;
    private byte[] imagen;
    private byte[] imagenShiny;
    private byte[] gif;
    private String tipo1;
    private String tipo2;
    private Integer evolucionaDe;
    private String metodoEvolucion;

    public Pokemon() {}

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

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public byte[] getImagen() {
        return imagen;
    }

    public void setImagen(byte[] imagen) {
        this.imagen = imagen;
    }

    public byte[] getImagenShiny() {
        return imagenShiny;
    }

    public void setImagenShiny(byte[] imagenShiny) {
        this.imagenShiny = imagenShiny;
    }

    public byte[] getGif() {
        return gif;
    }

    public void setGif(byte[] gif) {
        this.gif = gif;
    }

    public String getTipo1() {
        return tipo1;
    }

    public void setTipo1(String tipo1) {
        this.tipo1 = tipo1;
    }

    public String getTipo2() {
        return tipo2;
    }

    public void setTipo2(String tipo2) {
        this.tipo2 = tipo2;
    }

    public Integer getEvolucionaDe() {
        return evolucionaDe;
    }

    public void setEvolucionaDe(Integer evolucionaDe) {
        this.evolucionaDe = evolucionaDe;
    }

    public String getMetodoEvolucion() {
        return metodoEvolucion;
    }

    public void setMetodoEvolucion(String metodoEvolucion) {
        this.metodoEvolucion = metodoEvolucion;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Pokemon pokemon)) return false;
        return Objects.equals(id, pokemon.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

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
