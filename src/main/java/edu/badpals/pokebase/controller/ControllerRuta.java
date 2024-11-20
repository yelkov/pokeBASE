package edu.badpals.pokebase.controller;

import edu.badpals.pokebase.criteria.CriteriaRuta;
import edu.badpals.pokebase.model.Pokemon;
import edu.badpals.pokebase.model.Ruta;
import edu.badpals.pokebase.model.RutaBD;
import edu.badpals.pokebase.model.RutaPokemon;
import edu.badpals.pokebase.service.ErrorLogger;
import edu.badpals.pokebase.view.View;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.sql.SQLIntegrityConstraintViolationException;
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
    private Button btnAnterior, btnSiguiente, btnBuscarPokemon, btnRetirarPokemon;

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
        if (!nombre.equals("")){
            try{
                int minimo = Integer.valueOf(txtMinimoNivel.getText());
                int maximo = Integer.valueOf(txtMaximoNivel.getText());
                boolean isAddOk = rutaBD.addPokemon(id, nombre, minimo, maximo);
                if (isAddOk){
                    setPokemonList(id);
                } else{
                    View.lanzarMensajeError("Error", "Inserción fallida", "No se ha podido añadir el pokemon a la ruta porque el pokemon no existe en la base de datos, debe crearlo primero. Para otros posibles motivos consulte el log");
                }
            } catch (SQLIntegrityConstraintViolationException e){
                View.lanzarMensajeError("Error", "Inserción fallida", "El pokemon no se puede añadir a la ruta porque ya está registrado en ella");
            } catch (NumberFormatException e){
                View.lanzarMensajeError("Error", "Error de formato", "Los campos de nivel mínimo y máximo son obligatorios");
            }
        } else{
            View.lanzarMensajeError("Error", "Formato incorrecto", "Debe introducir el nombre del pokemon a añadir");
        }
        txtPokemonAnadir.setText("");
        txtMaximoNivel.setText("");
        txtMinimoNivel.setText("");
    }

    public void removePokemonRuta(){
        int rutaId = Integer.valueOf(lblRutaId.getText());
        String pokemonName = listPokemonsRuta.getSelectionModel().getSelectedItem().getPokemon();
        Pokemon pokemon = rutaBD.getPokemonBD().getPokemonByName(pokemonName);
        int pokemonId = pokemon.getId();
        boolean isOk = rutaBD.removePokemonRuta(rutaId, pokemonId);
        if(isOk){
            setPokemonList(rutaId);
        } else {
            View.lanzarMensajeError("Error", "Eliminación de pokemon de ruta no realizada", "No se ha podido realizar la operación correctamente, consulte los motivos en el archivo de log");
        }
    }

    private void modificarNiveles(int id, int niveles) {
        boolean isModificarOk = rutaBD.subirNivelesRuta(id, niveles);
        if (isModificarOk) {
            setPokemonList(id);
        } else {
            View.lanzarMensajeError("Error", "Modificación de niveles abortada", "No se ha podido realizar la operación correctamente, consulte los motivos en el archivo de log");
        }
        txtNiveles.setText("");
    }

    public void subirNiveles(){
        int id = Integer.valueOf(lblRutaId.getText());
        try{
            int niveles = Integer.valueOf(txtNiveles.getText());
            modificarNiveles(id, niveles);
        } catch (NumberFormatException e){
            View.lanzarMensajeError("Error", "Error de formato", "Debe introducir el número de niveles a modificar");
        }
    }

    public void bajarNiveles(){
        int id = Integer.valueOf(lblRutaId.getText());
        try{
            int niveles = Integer.valueOf(txtNiveles.getText());
            modificarNiveles(id, 0-niveles);
        } catch (NumberFormatException e){
            View.lanzarMensajeError("Error", "Error de formato", "Debe introducir el número de niveles a modificar");
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
            View.lanzarMensajeError("Error", "No se ha podido cambiar de ventana", "Consulte el log para ver el error más detalladamente");
            ErrorLogger.saveErrorLog(e.getMessage());
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

    public void activateBotonesPokemon(){
        String pokemon = listPokemonsRuta.getSelectionModel().getSelectedItem().getPokemon();
        if(pokemon!= null){
            btnBuscarPokemon.setDisable(false);
            btnRetirarPokemon.setDisable(false);
        } else {
            btnBuscarPokemon.setDisable(true);
            btnRetirarPokemon.setDisable(true);
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
            View.lanzarMensajeError("Error", "No se ha podido cambiar de ventana", "Consulte el log para ver el error más detalladamente");
            ErrorLogger.saveErrorLog(e.getMessage());
        }
    }

    public void volverAlInicio(ActionEvent actionEvent){
        try {
            FXMLLoader loader = Controller.getFxmlLoader(actionEvent, "main.fxml", this.getClass(), 800, 600);
        } catch (IOException e){
            View.lanzarMensajeError("Error", "No se ha podido cambiar de ventana", "Consulte el log para ver el error más detalladamente");
            ErrorLogger.saveErrorLog(e.getMessage());
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
        try{
            boolean wasRutaCreated = rutaBD.insertRuta(rutaNueva);
            if (wasRutaCreated){
                Ruta rutaLoaded = rutaBD.getRuta(nombre, region).get();
                setRuta(rutaLoaded);
            } else {
                View.lanzarMensajeError("Error", "Error al insertar la nueva ruta", "No se ha podido realizar el insert en la base de datos, consulte los motivos en el archivo log");
            }
        } catch (SQLIntegrityConstraintViolationException e){
            View.lanzarMensajeError("Error", "Error al insertar la nueva ruta", "La ruta que se ha intentado crear ya existe en la base de datos");
        }
    }

    public void modificarRuta(){
        int id = Integer.valueOf(lblRutaId.getText());
        String nombre = txtRutaNombre.getText();
        String region = txtRutaRegion.getText();
        Ruta rutaUpdated = new Ruta(id, nombre, region);
        try {
            boolean wasRutaUpdated = rutaBD.updateRuta(rutaUpdated);
            if (wasRutaUpdated) {
                setRuta(rutaUpdated);
            } else {
                View.lanzarMensajeError("Error", "Error al modificar la ruta", "No se ha podido realizar el update en la base de datos, consulte los motivos en el archivo log");
            }
        } catch (SQLIntegrityConstraintViolationException e){
            View.lanzarMensajeError("Error", "Error al modificar la ruta", "Ya existe una ruta diferente con el nombre y región indicados");
        }
    }

    public void borrarRuta(){
        String idValue = lblRutaId.getText();
        int id = Integer.parseInt(idValue);
        boolean wasRutaDeleted = rutaBD.deleteRuta(id);
        if(wasRutaDeleted){
            configureMenu(false);
            cleanFields();
        } else {
            View.lanzarMensajeError("Error", "Error al borrar la ruta", "No se ha podido realizar el delete en la base de datos, consulte los motivos en el archivo log");
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
