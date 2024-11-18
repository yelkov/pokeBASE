package edu.badpals.pokebase.controller;

import edu.badpals.pokebase.model.AccesoBD;
import edu.badpals.pokebase.model.Pokemon;
import edu.badpals.pokebase.model.PokemonBD;
import edu.badpals.pokebase.model.RutaBD;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.stage.Stage;
import javafx.util.Callback;

import java.io.IOException;
import java.util.Calendar;
import java.util.List;

public class ControllerListaPokemon {
    @FXML
    ListView<Pokemon> lvPokemon;
    @FXML
    ComboBox cmbCriterio, cmbOrden;
    @FXML
    Button btnVerPokemon;

    private List<Pokemon> listaPokemon;
    private AccesoBD accesoBD;
    private PokemonBD pokemonBD;
    private RutaBD rutaBD;

    public void initialize() {
        accesoBD = new AccesoBD();
        accesoBD.connect();
        pokemonBD = new PokemonBD(accesoBD);
        rutaBD = new RutaBD(accesoBD);
        cmbCriterio.setItems(FXCollections.observableArrayList("---","Nombre","Id"));
        cmbOrden.setItems(FXCollections.observableArrayList("Ascendente","Descendente"));
    }

    public void setPokemons(List<Pokemon> listaPokemon) {
        this.listaPokemon = listaPokemon;
        lvPokemon.setItems(FXCollections.observableArrayList(listaPokemon));
        accesoBD = new AccesoBD();
        accesoBD.connect();
        pokemonBD = new PokemonBD(accesoBD);
        setListaPokemon();
    }

    private FXMLLoader getFxmlLoader(ActionEvent actionEvent, String sceneFxml) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource(sceneFxml));
        Scene scene = new Scene(loader.load(),600,600);
        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow(); // Obtener el Stage actual
        // Crear una nueva escena con el contenido cargado
        stage.setScene(scene); // Establecer la nueva escena en el Stage
        stage.show();
        return loader;
    }

    public void verPokemon(ActionEvent actionEvent) {
        try{
            Pokemon pokemon = lvPokemon.getSelectionModel().getSelectedItem();
            if(pokemon != null){
                FXMLLoader loader = getFxmlLoader(actionEvent, "datosPokemon.fxml");
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

    public void setListaPokemon(){
        lvPokemon.setCellFactory(new Callback<ListView<Pokemon>, ListCell<Pokemon>>() {
            @Override
            public ListCell<Pokemon> call(ListView<Pokemon> pokemonListView) {
                return new ListCell<Pokemon>() {
                    @Override
                    protected void updateItem(Pokemon pokemon, boolean b) {
                        super.updateItem(pokemon, b);

                        if(b || pokemon == null){
                            setText(null);
                            setGraphic(null);
                        }else{
                            int espacio = 120;
                            HBox hBox = new HBox(espacio);

                            Label nombreLista = new Label(pokemon.getNombre());

                            Label idLista = new Label(String.valueOf(pokemon.getId()));
                            Label tipo1Lista = new Label(pokemon.getTipo1());

                            HBox.setHgrow(nombreLista, Priority.ALWAYS);
                            HBox.setHgrow(idLista, Priority.ALWAYS);
                            HBox.setHgrow(tipo1Lista, Priority.ALWAYS);

                            hBox.getChildren().addAll(nombreLista,idLista,tipo1Lista);
                            if(pokemon.getTipo2() != null){
                                Label tipo2Lista = new Label(pokemon.getTipo2());
                                HBox.setHgrow(tipo2Lista, Priority.ALWAYS);
                                hBox.getChildren().add(tipo2Lista);
                            }else{
                                Region espacio1 = new Region();
                                espacio1.setPrefWidth(espacio);
                                hBox.getChildren().add(espacio1);
                            }
                            if(pokemon.getEvolucionaDe() != null){
                                Pokemon preEvolucion = pokemonBD.getPokemonById(pokemon.getEvolucionaDe());
                                Label evolucionaLista = new Label(preEvolucion.getNombre().toString());
                                Label metodoLista = new Label(pokemon.getMetodoEvolucion());
                                HBox.setHgrow(evolucionaLista, Priority.ALWAYS);
                                HBox.setHgrow(metodoLista, Priority.ALWAYS);
                                hBox.getChildren().addAll(evolucionaLista,metodoLista);
                            }else{
                                Region espacio2 = new Region();
                                espacio2.setPrefWidth(espacio);
                                hBox.getChildren().add(espacio2);
                            }
                            setGraphic(hBox);

                        }
                    }
                };
            }
        });
    }
}
