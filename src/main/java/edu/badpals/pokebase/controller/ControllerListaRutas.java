package edu.badpals.pokebase.controller;

import edu.badpals.pokebase.model.Ruta;
import edu.badpals.pokebase.model.RutaBD;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.w3c.dom.events.MouseEvent;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

public class ControllerListaRutas {
    @FXML
    private ListView<Ruta> listaRutas;

    @FXML
    private TextField txtPokemon;

    @FXML
    private ComboBox<String> cmbRegion, cmbCriterio, cmbOrden;

    @FXML
    private Button btnBuscarRuta, btnFiltrarRutas;

    private RutaBD rutaBD;

    public void initialize(){
        cmbCriterio.setItems(FXCollections.observableArrayList("id", "nombre", "region"));
        cmbCriterio.setValue("id");
        cmbOrden.setItems(FXCollections.observableArrayList("ASC", "DESC"));
        cmbOrden.setValue("ASC");

    };

    public void setRutas(List<Ruta> rutas) {
        listaRutas.setItems(FXCollections.observableArrayList(rutas));
    }

    public void refiltrarRutas(){
        Optional<String> pokemon = txtPokemon.getText().equals("")?Optional.empty():Optional.of(txtPokemon.getText());
        String regionSeleccionada = cmbRegion.getSelectionModel().getSelectedItem();
        Optional<String> region = regionSeleccionada.equals("Todas")? Optional.empty():Optional.of(regionSeleccionada);
        String criterio = cmbCriterio.getSelectionModel().getSelectedItem();
        String orden = cmbOrden.getSelectionModel().getSelectedItem();
        List<Ruta> rutas = rutaBD.getRutasByFilters(pokemon, region, criterio, orden);
        setRutas(rutas);
    }

    public void setAcceso(RutaBD rutaBD) {
        this.rutaBD = rutaBD;
        List<String> regiones = rutaBD.getAllRegions();
        regiones.add("Todas");
        cmbRegion.setItems(FXCollections.observableArrayList(regiones
        ));
        cmbRegion.setValue("Todas");
    }

    public void showRuta(ActionEvent actionEvent){
        try {
            Ruta ruta = listaRutas.getSelectionModel().getSelectedItem();
            if (ruta!= null) {
                FXMLLoader loader = getFxmlLoader(actionEvent, "datosRuta.fxml");
                ControllerRuta controllerRuta = loader.getController();
                controllerRuta.setAcceso(rutaBD);
                controllerRuta.setRuta(ruta);
            } else{
                System.out.println("no se ha clickado bien");
            }
        } catch (IOException e){
            System.out.println("Error al cambiar de ventara" + e.getMessage());
        } catch (Exception todas){
            todas.getMessage();
        }
    }

    public void activateBotonBuscar(){
        Ruta ruta = listaRutas.getSelectionModel().getSelectedItem();
        if(ruta!= null){
            btnBuscarRuta.setDisable(false);
        } else {
            btnBuscarRuta.setDisable(true);
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
}
