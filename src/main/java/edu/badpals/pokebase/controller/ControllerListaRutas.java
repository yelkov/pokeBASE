package edu.badpals.pokebase.controller;

import edu.badpals.pokebase.criteria.CriteriaRuta;
import edu.badpals.pokebase.model.Ruta;
import edu.badpals.pokebase.model.RutaBD;
import edu.badpals.pokebase.service.DocumentExporter;
import edu.badpals.pokebase.service.ErrorLogger;
import edu.badpals.pokebase.view.View;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import org.w3c.dom.events.MouseEvent;

import java.io.File;
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
    private CriteriaRuta criteriaRuta;
    private List<Ruta> rutas;

    public void initialize(){
        cmbCriterio.setItems(FXCollections.observableArrayList("id", "nombre", "region"));
        cmbCriterio.setValue("id");
        cmbOrden.setItems(FXCollections.observableArrayList("ASC", "DESC"));
        cmbOrden.setValue("ASC");

    };

    public void setRutas(List<Ruta> rutas) {
        this.rutas = rutas;
        listaRutas.setItems(FXCollections.observableArrayList(rutas));
    }

    public void refiltrarRutas(){
        String pokemon = txtPokemon.getText();
        String regionSeleccionada = cmbRegion.getSelectionModel().getSelectedItem();
        String criterio = cmbCriterio.getSelectionModel().getSelectedItem();
        String orden = cmbOrden.getSelectionModel().getSelectedItem();
        criteriaRuta = new CriteriaRuta(pokemon, regionSeleccionada, criterio, orden);
        List<Ruta> rutas = rutaBD.getRutasByFilters(criteriaRuta);
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

    public void setCriteria(CriteriaRuta criteria){
        if(criteria.getPokemon().isPresent()){
            txtPokemon.setText(criteria.getPokemon().get());
        }
        if(criteria.getRegion().isPresent()){
            cmbRegion.setValue(criteria.getRegion().get());
        } else{
            cmbRegion.setValue("Todas");
        }
        cmbCriterio.setValue(criteria.getCriterio());
        cmbOrden.setValue(criteria.getOrden());
        this.criteriaRuta = criteria;
    }

    public void showRuta(ActionEvent actionEvent){
        try {
            Ruta ruta = listaRutas.getSelectionModel().getSelectedItem();
            if (ruta!= null) {
                FXMLLoader loader = Controller.getFxmlLoader(actionEvent, "datosRuta.fxml", this.getClass(), 600, 700);
                ControllerRuta controllerRuta = loader.getController();
                controllerRuta.setAcceso(rutaBD);
                controllerRuta.setRuta(ruta);
                List<Ruta> rutas = listaRutas.getItems().stream().toList();
                int index = rutas.indexOf(ruta);
                controllerRuta.setPartOfList(rutas, index, criteriaRuta);
            }
        } catch (IOException e){
            View.lanzarMensajeError("Error", "No se ha podido cambiar de ventana", "Consulte el log para ver el error más detalladamente");
            ErrorLogger.saveErrorLog(e.getMessage());
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

    public void volverAlInicio(ActionEvent actionEvent){
        try {
            FXMLLoader loader = Controller.getFxmlLoader(actionEvent, "main.fxml", this.getClass(), 800, 600);
        } catch (IOException e){
            View.lanzarMensajeError("Error", "No se ha podido cambiar de ventana", "Consulte el log para ver el error más detalladamente");
            ErrorLogger.saveErrorLog(e.getMessage());
        }
    }

    public void exportar(ActionEvent action){
        Stage stage = (Stage) ((Node) action.getSource()).getScene().getWindow();
        Optional<File> posibleDirectorio = View.abrirFileChooserExp(stage);
        if(posibleDirectorio.isPresent()){
            String ruta = posibleDirectorio.get().getAbsolutePath();
            boolean exportaCorrecto = DocumentExporter.exportToJson(rutas,ruta);
            if(exportaCorrecto){
                View.lanzarMensajeAviso(
                        "Aviso",
                        "Exportación exitosa",
                        "Se han exportado los datos de pokémon a: "+ ruta);
            }else{
                View.lanzarMensajeError(
                        "Error",
                        "Error de exportación",
                        "Se ha producido un error inesperado. Consulte el log para más información.");
            }
        }
    }

}
