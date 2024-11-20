package edu.badpals.pokebase.criteria;

public class CriteriaPokemon {
    String tipo1;
    String tipo2;
    String criterio;
    String orden;

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

    public String getCriterio() {
        return criterio;
    }

    public void setCriterio(String criterio) {
        this.criterio = criterio;
    }

    public String getOrden() {
        return orden;
    }

    public void setOrden(String orden) {
        this.orden = orden;
    }
}
