package edu.badpals.pokebase.controller;

import edu.badpals.pokebase.model.AccesoBD;
import edu.badpals.pokebase.model.Pokemon;
import edu.badpals.pokebase.model.PokemonBD;
import edu.badpals.pokebase.model.RutaBD;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.util.Callback;

import java.io.IOException;
import java.util.List;

public class ControllerListaPokemon {
    @FXML
    ComboBox cmbCriterio, cmbOrden;
    @FXML
    Button btnVerPokemon;
    @FXML
    private TableView<Pokemon> tableListaPokemon;
    @FXML
    private TableColumn<Pokemon, String> columnaNombre, columnaTipo1, columnaTipo2, columnaEvoluciona, columnaMetodo;
    @FXML
    private TableColumn<Pokemon, Integer> columnaId;


    private List<Pokemon> listaPokemon;
    private AccesoBD accesoBD;
    private PokemonBD pokemonBD;
    private RutaBD rutaBD;

    public void initialize() {
        accesoBD = new AccesoBD();
        accesoBD.connect();
        pokemonBD = new PokemonBD(accesoBD);
        rutaBD = new RutaBD(accesoBD);
        cmbCriterio.setItems(FXCollections.observableArrayList("---","Id","Nombre"));
        cmbCriterio.setValue("---");
        cmbOrden.setItems(FXCollections.observableArrayList("Ascendente","Descendente"));
        cmbOrden.setValue("Ascendente");

        tableListaPokemon.widthProperty().addListener((obs, oldWidth, newWidth) -> {
            double totalWidth = newWidth.doubleValue();
            double columnWidth = totalWidth / tableListaPokemon.getColumns().size();

            for (TableColumn<Pokemon, ?> column : tableListaPokemon.getColumns()) {
                column.setPrefWidth(columnWidth);
            }
        });
    }

    public void setPokemons(List<Pokemon> listaPokemon) {
        this.listaPokemon = listaPokemon;
        accesoBD = new AccesoBD();
        accesoBD.connect();
        pokemonBD = new PokemonBD(accesoBD);

        tableListaPokemon.setItems(FXCollections.observableArrayList(listaPokemon));
        setTablaPokemon();
    }


    public void verPokemon(ActionEvent actionEvent) {
        try{
            Pokemon pokemon = tableListaPokemon.getSelectionModel().getSelectedItem();
            if(pokemon != null){
                FXMLLoader loader = Controller.getFxmlLoader(actionEvent, "datosPokemon.fxml",this.getClass(),600,500);
                ControllerPokemon controllerPokemon = loader.getController();
                controllerPokemon.setPokemon(pokemon);

            }
        }catch (IOException e){
            System.out.println("Error al mostrar un pokemon" + e.getMessage());
            e.printStackTrace();
        }

    }

    public void handleVolver(ActionEvent actionEvent) {
    }


    private void setTablaPokemon(){
        columnaNombre.setCellValueFactory(new PropertyValueFactory<Pokemon, String>("nombre"));
        columnaId.setCellValueFactory(new PropertyValueFactory<Pokemon,Integer>("id"));
        columnaTipo1.setCellValueFactory(new PropertyValueFactory<Pokemon,String>("tipo1"));
        columnaTipo2.setCellValueFactory(new PropertyValueFactory<Pokemon,String>("tipo2"));

        columnaEvoluciona.setCellValueFactory(cellData -> {
            if(cellData.getValue().getEvolucionaDe() != null){
                Pokemon preEvolucion = pokemonBD.getPokemonById(cellData.getValue().getEvolucionaDe());
                if(preEvolucion != null){
                    return new SimpleStringProperty(preEvolucion.getNombre().toString());
                }else{
                    return new SimpleStringProperty("");
                }
            }
            return new SimpleStringProperty("");
        });
        columnaMetodo.setCellValueFactory(new PropertyValueFactory<Pokemon,String>("metodoEvolucion"));

    }
}
