package edu.badpals.pokebase.controller;

import edu.badpals.pokebase.criteria.CriteriaRuta;
import edu.badpals.pokebase.model.*;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

public class Controller {
    @FXML
    private Button btnBuscarPokemon, btnBuscarRuta, btnFiltrarPokemon, btnFiltrarRuta;

    @FXML
    private Button btnCrearPokemon, btnCrearRuta;

    @FXML
    private TextField txtNombrePokemon, txtNombreRuta, txtPokemonRuta;

    @FXML
    private TextField txtTipo1, txtTipo2;

    @FXML
    private ComboBox<String> cmbRegiones, cmbCriterio, cmbOrden, cmbFiltrarRegiones;

    private AccesoBD accesoBD;
    private PokemonBD pokemonBD;
    private RutaBD rutaBD;

    @FXML
    public void initialize() {
        accesoBD = new AccesoBD();
        accesoBD.connect();
        pokemonBD = new PokemonBD(accesoBD);
        rutaBD = new RutaBD(accesoBD);

        List<String> regiones = rutaBD.getAllRegions();
        cmbRegiones.setItems(FXCollections.observableArrayList(regiones
        ));
        if(regiones.size()>0){
            cmbRegiones.setValue(regiones.get(0));
        }

        regiones.add("Todas");
        cmbFiltrarRegiones.setItems(FXCollections.observableArrayList(regiones
        ));
        cmbFiltrarRegiones.setValue("Todas");

        cmbCriterio.setItems(FXCollections.observableArrayList("id","nombre"));
        cmbCriterio.setValue("id");

        cmbOrden.setItems(FXCollections.observableArrayList("asc","desc"));
        cmbCriterio.setValue("asc");

    }


    public void crearRuta(ActionEvent actionEvent) {
        try{
            FXMLLoader loader = Controller.getFxmlLoader(actionEvent,"datosRuta.fxml",this.getClass(),500,900);
            ControllerRuta controller = loader.getController();
            controller.setAcceso(rutaBD);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void crearPokemon(ActionEvent actionEvent) {
        try{
            Controller.getFxmlLoader(actionEvent,"datosPokemon.fxml",this.getClass(),600,600);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void buscarPokemon(ActionEvent actionEvent) {
        String pokemonSearch = txtNombrePokemon.getText();
        Pokemon pokemon;
        try{
            int id = Integer.valueOf(pokemonSearch);
            pokemon = pokemonBD.getPokemonById(id);
        } catch (NumberFormatException e){
            pokemon = pokemonBD.getPokemonByName(pokemonSearch);
        }
        if(pokemon != null){
            try{
                FXMLLoader loader = Controller.getFxmlLoader(actionEvent,"datosPokemon.fxml",this.getClass(), 600,500);
                ControllerPokemon pokemonController = loader.getController();
                pokemonController.setPokemon(pokemon);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static FXMLLoader getFxmlLoader(ActionEvent actionEvent,String sceneFxml, Class clase, int ancho, int largo) throws IOException {
        FXMLLoader loader = new FXMLLoader(clase.getResource(sceneFxml));
        Scene scene = new Scene(loader.load(),ancho,largo);
        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow(); // Obtener el Stage actual
        // Crear una nueva escena con el contenido cargado
        stage.setScene(scene); // Establecer la nueva escena en el Stage
        stage.show();
        return loader;
    }

    public void buscarRuta(ActionEvent actionEvent) {
        String nombreRuta = txtNombreRuta.getText();
        String nombreRegion = cmbRegiones.getSelectionModel().getSelectedItem();
        Optional<Ruta> ruta = rutaBD.getRuta(nombreRuta, nombreRegion);
        if (ruta.isPresent()){
            try{
                FXMLLoader loader =Controller.getFxmlLoader(actionEvent,"datosRuta.fxml",this.getClass(), 600,700);
                ControllerRuta controller = loader.getController();
                controller.setAcceso(rutaBD);
                controller.setRuta(ruta.get());
            }catch (IOException e) {
                e.printStackTrace();
            }
        }else{
            System.out.println("No hay ruta.");
        }

    }

    public void filtrarPokemon(ActionEvent actionEvent) {
        String tipo1 = txtTipo1.getText();
        String tipo2 = txtTipo2.getText();
        String criterio = cmbCriterio.getSelectionModel().getSelectedItem();
        String orden = cmbOrden.getSelectionModel().getSelectedItem();
        if (!tipo1.equals("")){
            tipo2 = tipo2.equals("")? null : tipo2;
            List<Pokemon> pokemons = pokemonBD.getPokemonsByType(tipo1, tipo2, criterio + " " +orden);
            System.out.println(pokemons);
            try{
                FXMLLoader loader = Controller.getFxmlLoader(actionEvent,"listaPokemon.fxml",this.getClass(),1000,700);
                ControllerListaPokemon controller = loader.getController();
                controller.setPokemons(pokemons);
            }catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    public void filtrarRuta(ActionEvent actionEvent){
        String pokemon = txtPokemonRuta.getText();
        String regionSeleccionada = cmbFiltrarRegiones.getSelectionModel().getSelectedItem();
        CriteriaRuta criteriaRuta = new CriteriaRuta(pokemon, regionSeleccionada);
        List<Ruta> rutas = rutaBD.getRutasByFilters(criteriaRuta);
        try{
            FXMLLoader loader = Controller.getFxmlLoader(actionEvent,"listaRutas.fxml",this.getClass(),550,600);
            ControllerListaRutas controller = loader.getController();
            controller.setRutas(rutas);
            controller.setAcceso(rutaBD);
            controller.setCriteria(criteriaRuta);
        }catch (IOException e) {
            e.printStackTrace();
        }

    }


}