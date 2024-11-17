package edu.badpals.pokebase.controller;

import edu.badpals.pokebase.model.Ruta;
import edu.badpals.pokebase.model.RutaBD;

import java.util.List;

public class ControllerListaRutas {
    private RutaBD rutaBD;

    public void setRutas(List<Ruta> rutas) {
    }

    public void setAcceso(RutaBD rutaBD) {
        this.rutaBD = rutaBD;
    }
}
