package edu.badpals.pokebase.controller;

import edu.badpals.pokebase.criteria.CriteriaRuta;
import edu.badpals.pokebase.model.Pokemon;
import edu.badpals.pokebase.model.Ruta;
import edu.badpals.pokebase.model.RutaBD;
import edu.badpals.pokebase.model.RutaPokemon;
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

    }

    public void setAcceso(RutaBD rutaBD) {
        this.rutaBD = rutaBD;
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
                    lanzarMensajeError("Error", "Inserción fallida", "No se ha podido añadir el pokemon a la ruta. Es posible que el pokemon no existe o ya se encuentre registrado en esta ruta");
                }
            } else{
                lanzarMensajeError("Error", "Formato incorrecto", "Debe introducir el nombre del pokemon a añadir");
            }
        } catch (NumberFormatException e){
            lanzarMensajeError("Error", "Formato incorrecto", "Los niveles de las rutas deben ser número");
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
                lanzarMensajeError("Error", "Modificación de niveles abortada", "No se ha podido realizar la operación correctamente");
            }
        } catch (NumberFormatException e) {
            lanzarMensajeError("Error", "Formato incorrecto", "La variación de niveles debes ser un número");
        } finally {
            txtNiveles.setText("");
        }
    }

    public void buscarInfoPokemon(ActionEvent actionEvent){
        try{
            FXMLLoader loader = getFxmlLoader(actionEvent, "datosPokemon.fxml");
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
            FXMLLoader loader = getFxmlLoader(actionEvent, "listaRutas.fxml");
            ControllerListaRutas controller = loader.getController();
            controller.setRutas(rutas);
            controller.setAcceso(rutaBD);
            controller.setCriteria(criteriaRuta);
        } catch (IOException e){
            System.out.println("Error");
        }
    }

    private FXMLLoader getFxmlLoader(ActionEvent actionEvent, String sceneFxml) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource(sceneFxml));
        Scene scene = new Scene(loader.load(),900,900);
        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow(); // Obtener el Stage actual
        // Crear una nueva escena con el contenido cargado
        stage.setScene(scene); // Establecer la nueva escena en el Stage
        stage.show();
        return loader;
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
            lanzarMensajeError("Error", "Error al insertar la nueva ruta", "Compruebe si ya está registrada en la base de datos");
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
            lanzarMensajeError("Error", "Er", "No se ha modificar la ruta");
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
            lanzarMensajeError("Error", "Er", "No se ha podido borrar a la ruta");
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

    public void lanzarMensajeError(String titulo, String cabecera, String mensaje){
        Alert error = new Alert(Alert.AlertType.ERROR);
        error.setTitle(titulo);
        error.setHeaderText(cabecera);
        error.setContentText(mensaje);

        error.showAndWait();
    }

}
