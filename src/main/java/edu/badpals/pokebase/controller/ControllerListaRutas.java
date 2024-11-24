package edu.badpals.pokebase.controller;

import edu.badpals.pokebase.criteria.CriteriaRuta;
import edu.badpals.pokebase.model.Ruta;
import edu.badpals.pokebase.model.RutaBD;
import edu.badpals.pokebase.service.DocumentExporter;
import edu.badpals.pokebase.view.View;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.File;
import java.util.*;

/**
 * Controlador para la vista que gestiona la lista de rutas.
 * Proporciona funcionalidades para filtrar rutas, ver detalles de una determinada ruta y exportar los datos obtenidos.
 *
 */
public class ControllerListaRutas {
    /**
     * Declaración de los objetos corresponientes a elementos de la interfaz
     */
    @FXML
    private ListView<Ruta> listaRutas;

    @FXML
    private TextField txtPokemon;

    @FXML
    private ComboBox<String> cmbRegion, cmbCriterio, cmbOrden;

    @FXML
    private Button btnBuscarRuta, btnFiltrarRutas;

    /**
     * Objetos necesarios para configurar la interfaz
     */
    private RutaBD rutaBD;
    private CriteriaRuta criteriaRuta;
    private List<Ruta> rutas;

    /**
     * Inicializa los componentes y configura los valores iniciales.
     * Recupera el acceso a la base de datos.
     * Configura los ComboBox con las regionaes cargads desde la base de datos, y los criterios de ordenación.
     * Recupera los criterios de búsqueda de SceneManager si existen
     */
    public void initialize(){
        rutaBD = SceneManager.getRutaBD();

        cmbCriterio.setItems(FXCollections.observableArrayList("id", "nombre", "region"));
        cmbCriterio.setValue("id");
        cmbOrden.setItems(FXCollections.observableArrayList("ASC", "DESC"));
        cmbOrden.setValue("ASC");

        List<String> regiones = rutaBD.getAllRegions();
        regiones.add("Todas");
        cmbRegion.setItems(FXCollections.observableArrayList(regiones
        ));
        cmbRegion.setValue("Todas");

        Map<String, Object> datos = SceneManager.getDatos();
        if (datos.containsKey("criteriaRuta")){
            setCriteria((CriteriaRuta) datos.get("criteriaRuta"));
            refiltrarRutas();
        }
    };

    /**
     * Establece las rutas que se mostrarán en la lista.
     *
     * @param rutas lista de rutas a mostrar.
     */

    public void setRutas(List<Ruta> rutas) {
        this.rutas = rutas;
        listaRutas.setItems(FXCollections.observableArrayList(rutas));
    }

    /**
     * Aplica los filtros actuales para actualizar la lista de rutas.
     */
    public void refiltrarRutas(){
        String pokemon = txtPokemon.getText();
        String regionSeleccionada = cmbRegion.getSelectionModel().getSelectedItem();
        String criterio = cmbCriterio.getSelectionModel().getSelectedItem();
        String orden = cmbOrden.getSelectionModel().getSelectedItem();
        criteriaRuta = new CriteriaRuta(pokemon, regionSeleccionada, criterio, orden);
        List<Ruta> rutas = rutaBD.getRutasByFilters(criteriaRuta);
        setRutas(rutas);
    }

    /**
     * Configura los criterios de búsqueda y actualiza los componentes de la vista de forma acorde.
     *
     * @param criteria criterios de búsqueda a aplicar.
     */
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

    /**
     * Activa o desactiva el botón "Buscar Ruta" según si hay una ruta seleccionada o no.
     */
    public void activateBotonBuscar(){
        Ruta ruta = listaRutas.getSelectionModel().getSelectedItem();
        if(ruta!= null){
            btnBuscarRuta.setDisable(false);
        } else {
            btnBuscarRuta.setDisable(true);
        }
    }

    /**
     * Exporta la lista actual de rutas a un archivo JSON.
     *
     * @param action evento de acción generado por el usuario.
     */
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

    /**
     * Navega a la vista que muestra los detalles de la ruta seleccionada.
     *
     * @param actionEvent evento de acción generado por el usuario.
     */
    public void goToRutaInfo(ActionEvent actionEvent){
        Ruta ruta = listaRutas.getSelectionModel().getSelectedItem();
        if (ruta!= null) {
            Map<String, Object> datos = new HashMap<>();
            datos.put("rutas", rutas);
            datos.put("ruta", ruta);
            datos.put("criteriaRuta", criteriaRuta);
            SceneManager.setDatos(datos);
            SceneManager.goToView(actionEvent, "datosRuta.fxml", this.getClass(), 650, 800);
        }
    }

    /**
     * Limpia los campos de búsqueda a sus valores predeterminados y vacía la lista de rutas en la interfaz.
     */
    public void cleanFields(){
        txtPokemon.setText("");
        cmbCriterio.setValue("id");
        cmbOrden.setValue("ASC");
        cmbRegion.setValue("Todas");
        setRutas(new ArrayList<>());

    }

    /**
     * Navega al menú principal de la aplicación.
     *
     * @param actionEvent evento de acción generado por el usuario.
     */
    public void volverAlInicio(ActionEvent actionEvent){
        SceneManager.volverAlInicio(actionEvent, this.getClass());
    }

    /**
     * Navega de vuelta a la vista anterior.
     *
     * @param event evento de acción generado por el usuario.
     */
    public void handleVolver(ActionEvent event) {
        SceneManager.volver(event, this.getClass());
    }

}
