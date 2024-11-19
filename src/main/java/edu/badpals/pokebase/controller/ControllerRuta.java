package edu.badpals.pokebase.controller;

import edu.badpals.pokebase.criteria.CriteriaRuta;
import edu.badpals.pokebase.model.Pokemon;
import edu.badpals.pokebase.model.Ruta;
import edu.badpals.pokebase.model.RutaBD;
import edu.badpals.pokebase.model.RutaPokemon;
import edu.badpals.pokebase.view.View;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.List;

public class ControllerRuta {
    @FXML
    private TextField txtRutaNombre, txtRutaRegion, txtPokemonAnadir, txtNiveles, txtMinimoNivel, txtMaximoNivel;

    @FXML
    private Label lblRutaId, lblCriterios;

    @FXML
    private ListView<RutaPokemon> listPokemonsRuta;

    @FXML
    private HBox menuRutaNueva;

    @FXML
    private VBox menuPokemon, menuParteLista, menuRutaCargada;

    @FXML
    private Button btnAnterior, btnSiguiente, btnBuscarPokemon;

    private RutaBD rutaBD;
    private List<Ruta> rutas;
    private int currentIndex;
    private CriteriaRuta criteriaRuta;

    public void initialize() {
        permitirSoloEnteros(txtMaximoNivel);
        permitirSoloEnteros(txtMinimoNivel);
        permitirSoloEnteros(txtNiveles);
    }

    public void setAcceso(RutaBD rutaBD) {
        this.rutaBD = rutaBD;
    }

    private void permitirSoloEnteros(TextField textField) {
        TextFormatter<String> integerFormatter = new TextFormatter<>(change ->
                change.getControlNewText().matches("\\d*") ? change : null);
        textField.setTextFormatter(integerFormatter);
    }

    public void setRuta(Ruta ruta){
        configureMenu(true);
        lblRutaId.setText(String.valueOf(ruta.getId()));
        txtRutaNombre.setText(ruta.getNombre());
        txtRutaRegion.setText(ruta.getRegion());
        setPokemonList(ruta.getId());
    }

    private void setPokemonList(int rutaId){
        List<RutaPokemon> pokemons = rutaBD.getPokemons(rutaId);
        if (pokemons.size() > 0){
            showNode(menuPokemon, true);
            listPokemonsRuta.setItems(FXCollections.observableArrayList(pokemons));
        } else {
            showNode(menuPokemon, false);
        }
    }

    public void addPokemonRuta(){
        int id = Integer.valueOf(lblRutaId.getText());
        String nombre = txtPokemonAnadir.getText();
        try{
            int minimo = Integer.valueOf(txtMinimoNivel.getText());
            int maximo = Integer.valueOf(txtMaximoNivel.getText());
            if (!nombre.equals("")){
                boolean isAddOk = rutaBD.addPokemon(id, nombre, minimo, maximo);
                if (isAddOk){
                    setPokemonList(id);
                } else{
                    View.lanzarMensajeError("Error", "Inserción fallida", "No se ha podido añadir el pokemon a la ruta. Es posible que el pokemon no existe o ya se encuentre registrado en esta ruta");
                }
            } else{
                View.lanzarMensajeError("Error", "Formato incorrecto", "Debe introducir el nombre del pokemon a añadir");
            }
        } catch (NumberFormatException e){
            View.lanzarMensajeError("Error", "Formato incorrecto", "Los niveles de las rutas deben ser número");
        }
        txtPokemonAnadir.setText("");
        txtMaximoNivel.setText("");
        txtMinimoNivel.setText("");
    }

    public void modificarNiveles(){
        int id = Integer.valueOf(lblRutaId.getText());
        try{
            int niveles = Integer.valueOf(txtNiveles.getText());
            boolean isModificarOk = rutaBD.subirNivelesRuta(id, niveles);
            if (isModificarOk){
                setPokemonList(id);
            } else {
                View.lanzarMensajeError("Error", "Modificación de niveles abortada", "No se ha podido realizar la operación correctamente");
            }
        } catch (NumberFormatException e) {
            View.lanzarMensajeError("Error", "Formato incorrecto", "La variación de niveles debes ser un número");
        } finally {
            txtNiveles.setText("");
        }
    }

    public void buscarInfoPokemon(ActionEvent actionEvent){
        try{
            FXMLLoader loader = Controller.getFxmlLoader(actionEvent, "datosPokemon.fxml", this.getClass(), 600, 500);
            ControllerPokemon controller = loader.getController();
            String pokemonName = listPokemonsRuta.getSelectionModel().getSelectedItem().getPokemon();
            Pokemon pokemon = rutaBD.getPokemonBD().getPokemonByName(pokemonName);
            controller.setPokemon(pokemon);
        } catch (IOException e){
            System.out.println("Error");
        }
    }

    public void setPartOfList(List<Ruta> rutas, int currentIndex, CriteriaRuta criteria){
        this.rutas = rutas;
        this.currentIndex = currentIndex;
        showNode(menuParteLista, true);
        criteriaRuta = criteria;
        lblCriterios.setText(criteria.toString());
        if (rutas.size()==1){
            btnAnterior.setDisable(true);
            btnSiguiente.setDisable(true);
        }
    }

    public void activateBotonBuscar(){
        String pokemon = listPokemonsRuta.getSelectionModel().getSelectedItem().getPokemon();
        if(pokemon!= null){
            btnBuscarPokemon.setDisable(false);
        } else {
            btnBuscarPokemon.setDisable(true);
        }
    }

    public void siguienteRuta(){
        currentIndex++;
        try{
            setRuta(rutas.get(currentIndex));
        } catch (IndexOutOfBoundsException e){
            currentIndex = 0;
            setRuta(rutas.get(currentIndex));
        }
    }

    public void anteriorRuta(){
        currentIndex--;
        try{
            setRuta(rutas.get(currentIndex));
        } catch (IndexOutOfBoundsException e){
            currentIndex = rutas.size()-1;
            setRuta(rutas.get(currentIndex));
        }
    }

    public void volverListaRutas(ActionEvent actionEvent){
        try{
            FXMLLoader loader = Controller.getFxmlLoader(actionEvent, "listaRutas.fxml", this.getClass(),550, 600);
            ControllerListaRutas controller = loader.getController();
            controller.setRutas(rutas);
            controller.setAcceso(rutaBD);
            controller.setCriteria(criteriaRuta);
        } catch (IOException e){
            System.out.println("Error");
        }
    }

    public void volverAlInicio(ActionEvent actionEvent){
        try {
            FXMLLoader loader = Controller.getFxmlLoader(actionEvent, "main.fxml", this.getClass(), 800, 600);
        } catch (IOException e){
            View.lanzarMensajeError("Error", "No se pudo cambiar de vista", e.getMessage());
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
        } else {
            View.lanzarMensajeError("Error", "Error al insertar la nueva ruta", "Compruebe si ya está registrada en la base de datos");
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
        } else{
            View.lanzarMensajeError("Error", "Er", "No se ha modificar la ruta");
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
            View.lanzarMensajeError("Error", "Er", "No se ha podido borrar a la ruta");
        }
    }

    public void toMenuCrearRuta(){
        configureMenu(false);
        cleanFields();
        showNode(menuParteLista, false);
    }

    public void cleanFields(){
        lblRutaId.setText("");
        txtRutaNombre.setText("");
        txtRutaRegion.setText("");
        showNode(menuPokemon, false);
    }


}
