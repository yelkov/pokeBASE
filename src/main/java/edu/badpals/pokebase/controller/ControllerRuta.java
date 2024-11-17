package edu.badpals.pokebase.controller;

import edu.badpals.pokebase.model.Ruta;
import edu.badpals.pokebase.model.RutaBD;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.List;

public class ControllerRuta {
    @FXML
    private TextField txtRutaNombre, txtRutaRegion;

    @FXML
    private Label lblRutaId;

    @FXML
    private ListView<String> listPokemonsRuta;

    @FXML
    private HBox menuRutaCargada, menuRutaNueva;

    @FXML
    private VBox menuPokemon;

    private RutaBD rutaBD;

    public void initialize() {

    }

    public void setAcceso(RutaBD rutaBD) {
        this.rutaBD = rutaBD;
    }

    public void setRuta(Ruta ruta){
        configureMenu(true);
        lblRutaId.setText(String.valueOf(ruta.getId()));
        txtRutaNombre.setText(ruta.getNombre());
        txtRutaRegion.setText(ruta.getRegion());
        List<String> pokemons = rutaBD.getPokemons(ruta.getId());
        if (pokemons.size() > 0){
            showNode(menuPokemon, true);
            listPokemonsRuta.setItems(FXCollections.observableArrayList(pokemons));
        } else {
            showNode(menuPokemon, false);
        }
    }

    public void showNode(Node node, boolean shown){
        node.setVisible(shown);
        node.setManaged(shown);
    }

    public void configureMenu (boolean isRutaLoaded){
        if (isRutaLoaded){
            showNode(menuRutaCargada, true);
            showNode(menuRutaNueva, false);
        } else {
            showNode(menuRutaCargada, false);
            showNode(menuRutaNueva, true);
        }
    }
    public void crearRuta(){
        String nombre = txtRutaNombre.getText();
        String region = txtRutaRegion.getText();
        Ruta rutaNueva = new Ruta(0,nombre, region);
        boolean wasRutaCreated = rutaBD.insertRuta(rutaNueva);
        if (wasRutaCreated){
            Ruta rutaLoaded = rutaBD.getRuta(nombre, region).get();
            setRuta(rutaLoaded);
        }
    }

    public void modificarRuta(){
        int id = Integer.valueOf(lblRutaId.getText());
        String nombre = txtRutaNombre.getText();
        String region = txtRutaRegion.getText();
        Ruta rutaUpdated = new Ruta(id, nombre, region);
        boolean wasRutaUpdated = rutaBD.updateRuta(rutaUpdated);
        if(wasRutaUpdated){
            setRuta(rutaUpdated);
        }
    }

    public void borrarRuta(){
        String idValue = lblRutaId.getText();
        try{
            int id = Integer.parseInt(idValue);
            boolean wasRutaDeleted = rutaBD.deleteRuta(id);
            if(wasRutaDeleted){
                configureMenu(false);
                cleanFields();
            }
        } catch (NumberFormatException e){
            System.out.println("No se ha podido borrar la ruta");
        }
    }

    public void cleanFields(){
        lblRutaId.setText("");
        txtRutaNombre.setText("");
        txtRutaRegion.setText("");
        showNode(menuPokemon, false);
    }

}
