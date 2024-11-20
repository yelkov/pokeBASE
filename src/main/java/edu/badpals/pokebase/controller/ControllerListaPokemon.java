package edu.badpals.pokebase.controller;

import edu.badpals.pokebase.criteria.CriteriaPokemon;
import edu.badpals.pokebase.model.AccesoBD;
import edu.badpals.pokebase.model.Pokemon;
import edu.badpals.pokebase.model.PokemonBD;
import edu.badpals.pokebase.model.RutaBD;
import edu.badpals.pokebase.view.View;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
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

    private CriteriaPokemon criteria;
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
        cmbOrden.setItems(FXCollections.observableArrayList("ASC","DESC"));
        cmbOrden.setValue("ASC");

        tableListaPokemon.widthProperty().addListener((obs, oldWidth, newWidth) -> {
            double totalWidth = newWidth.doubleValue();
            double columnWidth = totalWidth / tableListaPokemon.getColumns().size();

            for (TableColumn<Pokemon, ?> column : tableListaPokemon.getColumns()) {
                column.setPrefWidth(columnWidth);
            }
        });
    }

    public void setPokemons(List<Pokemon> listaPokemon, CriteriaPokemon criteria) {
        this.listaPokemon = listaPokemon;
        accesoBD = new AccesoBD();
        accesoBD.connect();
        pokemonBD = new PokemonBD(accesoBD);


        setTablaPokemon();
        setCriteria(criteria);
    }

    public void setCriteria(CriteriaPokemon criteriaPokemon){
        this.criteria = criteriaPokemon;
        if(criteria != null){
            txtTipo1.setText(criteria.getTipo1());
            txtTipo2.setText((criteria.getTipo2() == null ? "" : criteria.getTipo2()));
            cmbCriterio.setValue(criteria.getCriterio());
            cmbOrden.setValue(criteria.getOrden());
        }else{
            txtTipo1.setText("");
            txtTipo2.setText("");
            cmbCriterio.setValue("---");
            cmbOrden.setValue("ASC");
        }

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
        ObservableList oal = FXCollections.observableArrayList();
        if(listaPokemon != null){
            oal.addAll(listaPokemon);
            lblTotal.setText(String.valueOf(listaPokemon.size()));
        }else{
            lblTotal.setText("0");
        }
        tableListaPokemon.setItems(oal);


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

    public void filtrarPokemon() {
        String tipo1 = txtTipo1.getText();

        if (!tipo1.equals("")){
            String tipo2 = txtTipo2.getText();
            String criterio = cmbCriterio.getSelectionModel().getSelectedItem();
            String orden = cmbOrden.getSelectionModel().getSelectedItem();
            CriteriaPokemon criteriaPokemon = new CriteriaPokemon(tipo1,tipo2,criterio,orden);
            this.listaPokemon = pokemonBD.getPokemonByFilters(criteriaPokemon);

            setTablaPokemon();
            setCriteria(criteriaPokemon);

        }else{
            View.lanzarMensajeError("Error","Tipo no seleccionado","Para realizar una búsqueda, es imprescindible introducir un tipo en la primera casilla (tipo 1)");
        }

    }

    public void limpiar(){
        this.listaPokemon = null;
        this.criteria = null;
        setTablaPokemon();
        setCriteria(criteria);
    }
}
