package edu.badpals.pokebase.controller;

import edu.badpals.pokebase.model.AccesoBD;
import edu.badpals.pokebase.model.Pokemon;
import edu.badpals.pokebase.model.PokemonBD;
import edu.badpals.pokebase.model.RutaBD;
import edu.badpals.pokebase.view.View;
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
    ComboBox<String> cmbCriterio, cmbOrden;
    @FXML
    Button btnVerPokemon;
    @FXML
    private TableView<Pokemon> tableListaPokemon;
    @FXML
    private TableColumn<Pokemon, String> columnaNombre, columnaTipo1, columnaTipo2, columnaEvoluciona, columnaMetodo;
    @FXML
    private TableColumn<Pokemon, Integer> columnaId;
    @FXML
    private TextField txtTipo1, txtTipo2;
    @FXML
    private Label lblTotal;


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
        tableListaPokemon.setItems(FXCollections.observableArrayList());

        tableListaPokemon.setItems(FXCollections.observableArrayList(listaPokemon));


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

        lblTotal.setText(String.valueOf(listaPokemon.size()));
    }

    public void filtrarPokemon() {
        String tipo1 = txtTipo1.getText();
        String tipo2 = txtTipo2.getText();
        String criterio = cmbCriterio.getSelectionModel().getSelectedItem();
        String orden = cmbOrden.getSelectionModel().getSelectedItem();
        if (!tipo1.equals("")){
            tipo2 = tipo2.equals("")? null : tipo2;
            criterio = criterio.equals("---")? "ID" : criterio;
            orden = orden.equals("Ascendente")? "ASC" : "DESC";
            List<Pokemon> pokemons = pokemonBD.getPokemonsByType(tipo1, tipo2, criterio + " " +orden);

            this.listaPokemon = pokemons;
            setTablaPokemon();
        }else{
            View.lanzarMensajeError("Error","Tipo no seleccionado","Para realizar una búsqueda, es imprescindible introducir un tipo en la primera casilla (tipo 1)");
        }

    }
}
