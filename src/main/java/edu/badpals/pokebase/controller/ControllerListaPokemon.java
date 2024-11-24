package edu.badpals.pokebase.controller;

import edu.badpals.pokebase.criteria.CriteriaPokemon;
import edu.badpals.pokebase.model.*;
import edu.badpals.pokebase.service.DocumentExporter;
import edu.badpals.pokebase.view.View;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Controlador de la vista que gestiona la lista de Pokémon.
 * Proporciona funcionalidades para refiltrar, ver detalles de un pokemon, o exportar la tabla de Pokémon.
 */
public class ControllerListaPokemon {
    /**
     * Elementos de la interfaz que serán empleados por la clase
     */
    @FXML
    ComboBox<String> cmbCriterio, cmbOrden;
    @FXML
    Button btnVerPokemon, btnExportar;
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

    /**
     * Objetos de otras clases que son necesarios para controlar la vista
     */
    private CriteriaPokemon criteria;
    private List<Pokemon> listaPokemon;
    private AccesoBD accesoBD;
    private PokemonBD pokemonBD;
    private RutaBD rutaBD;

    /**
     * Inicializa los componentes y configura comportamientos iniciales.
     * Se recupera la conexión a la base de datos, se establece la configuración inicial de los ComboBox
     * según el criterio recibido de SceneManager y se carga la tabla si procede.
     */
    public void initialize() {
        pokemonBD = SceneManager.getPokemonBD();

        cmbCriterio.setItems(FXCollections.observableArrayList("---","Id","Nombre"));
        cmbCriterio.setValue("---");
        cmbOrden.setItems(FXCollections.observableArrayList("ASC","DESC"));
        cmbOrden.setValue("ASC");
        btnVerPokemon.setDisable(true);

        tableListaPokemon.widthProperty().addListener((obs, oldWidth, newWidth) -> {
            double totalWidth = newWidth.doubleValue();
            double columnWidth = totalWidth / tableListaPokemon.getColumns().size();

            for (TableColumn<Pokemon, ?> column : tableListaPokemon.getColumns()) {
                column.setPrefWidth(columnWidth);
            }
        });

        Map<String, Object> datos = SceneManager.getDatos();
        if (datos.containsKey("criteriaPokemon")){
            setCriteria((CriteriaPokemon) datos.get("criteriaPokemon"));
        }
    }

    /**
     * Actualiza la lista de Pokémon en la tabla según los criterios actuales.
     */
    public void setPokemons() {
        this.listaPokemon = pokemonBD.getPokemonByFilters(criteria);
        btnExportar.setDisable(false);
        setTablaPokemon();
    }

    /**
     * Establece los criterios de filtrado de Pokémon recibidos y actualiza la lista de Pokémon en la tabla según estos.
     *
     * @param criteriaPokemon criterios de filtrado recibidos.
     */
    public void setCriteria(CriteriaPokemon criteriaPokemon){
        this.criteria = criteriaPokemon;
        if(criteria != null){
            txtTipo1.setText(criteria.getTipo1());
            txtTipo2.setText((criteria.getTipo2() == null ? "" : criteria.getTipo2()));
            cmbCriterio.setValue(criteria.getCriterio());
            cmbOrden.setValue(criteria.getOrden());
            setPokemons();
        }else{
            txtTipo1.setText("");
            txtTipo2.setText("");
            cmbCriterio.setValue("---");
            cmbOrden.setValue("ASC");
        }

    }

    /**
     * Maneja el evento para ver los detalles de un Pokémon seleccionado, moviéndose a la lista correspondiente.
     *
     * @param actionEvent evento de acción generado por el usuario.
     */
    public void verPokemon(ActionEvent actionEvent) {
        Pokemon pokemon = tableListaPokemon.getSelectionModel().getSelectedItem();
        if(pokemon != null) {
            Map<String, Object> datos = new HashMap<>();
            datos.put("pokemon", pokemon);
            SceneManager.setDatos(datos);
            SceneManager.goToView(actionEvent, "datosPokemon.fxml", this.getClass(), 750, 650);
        }
    }

    /**
     * Navega de vuelta a la vista anterior.
     *
     * @param event evento de acción generado por el usuario.
     */
    @FXML
    private void handleVolver(ActionEvent event) {
        SceneManager.volver(event, this.getClass());
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
     * Configura la tabla de Pokémon con los datos actuales.
     */
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

    /**
     * Filtra la lista de Pokémon según los tipos y criterios seleccionados.
     */
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
            btnExportar.setDisable(false);

        }else{
            View.lanzarMensajeError("Error","Tipo no seleccionado","Para realizar una búsqueda, es imprescindible introducir un tipo en la primera casilla (tipo 1)");
        }

    }

    /**
     * Limpia los filtros y la tabla de Pokémon a sus valores predeterminados.
     */
    public void limpiar(){
        this.listaPokemon = null;
        this.criteria = null;
        setTablaPokemon();
        setCriteria(criteria);
        btnExportar.setDisable(true);
    }

    /**
     * Exporta la lista filtrada de Pokémon a un archivo JSON.
     *
     * @param action evento de acción generado por el usuario.
     */
    public void exportar(ActionEvent action){
        Stage stage = (Stage) ((Node) action.getSource()).getScene().getWindow();
        Optional<File> posibleDirectorio = View.abrirFileChooserExp(stage);
        if(posibleDirectorio.isPresent()){
            String ruta = posibleDirectorio.get().getAbsolutePath();
            boolean exportaCorrecto = DocumentExporter.exportToJson(listaPokemon,ruta);
            if(exportaCorrecto){
                View.lanzarMensajeAviso(
                        "Aviso",
                        "Exportación exitosa",
                        "Se han exportado los datos de la búsqueda filtrada de pokemon a: "+ ruta);
            }else{
                View.lanzarMensajeError(
                        "Error",
                        "Error de exportación",
                        "Se ha producido un error inesperado. Consulte el log para más información.");
            }
        }
    }

    /**
     * Activa o desactiva el botón "Ver Pokémon" según la selección en la tabla de una fila o no.
     */
    public void activateBotonVer(){
        Pokemon pokemon = tableListaPokemon.getSelectionModel().getSelectedItem();
        if(pokemon!= null){
            btnVerPokemon.setDisable(false);
        } else {
            btnVerPokemon.setDisable(true);
        }
    }
}
