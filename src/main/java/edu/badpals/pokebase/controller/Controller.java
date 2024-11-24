package edu.badpals.pokebase.controller;

import edu.badpals.pokebase.criteria.CriteriaPokemon;
import edu.badpals.pokebase.criteria.CriteriaRuta;
import edu.badpals.pokebase.model.*;
import edu.badpals.pokebase.view.View;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Controlador principal para gestionar las interacciones de la vista del menú principal.
 * Se encarga de realizar operaciones relacionadas con buscar Pokémon y rutas o su filtrado,
 * para lo cuál maneja la navegación a otras vistas.
 */
public class Controller {

    @FXML
    private ImageView logo, imgPokemon, imgRutas;

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

    /**
     * Método de inicialización llamado automáticamente al cargar la vista FXML.
     * Configura las conexiones con la base de datos y llena las listas desplegables con datos iniciales.
     */
    @FXML
    public void initialize() {
        accesoBD = SceneManager.getAccesoBD();
        pokemonBD = SceneManager.getPokemonBD();
        rutaBD = SceneManager.getRutaBD();

        // Cargar regiones en los ComboBox
        List<String> regiones = rutaBD.getAllRegions();
        cmbRegiones.setItems(FXCollections.observableArrayList(regiones));
        if (regiones.size() > 0) {
            cmbRegiones.setValue(regiones.get(0));
        }

        regiones.add("Todas");
        cmbFiltrarRegiones.setItems(FXCollections.observableArrayList(regiones));
        cmbFiltrarRegiones.setValue("Todas");

        // Configurar los comboBox de ordenación de resultados
        cmbCriterio.setItems(FXCollections.observableArrayList("Id", "Nombre"));
        cmbCriterio.setValue("Id");

        cmbOrden.setItems(FXCollections.observableArrayList("ASC", "DESC"));
        cmbOrden.setValue("ASC");

        // Configurar imágenes
        logo.setImage(new Image(getClass().getResource("/images/logo.png").toExternalForm()));
        imgPokemon.setImage(new Image(getClass().getResource("/images/imgPokemon.png").toExternalForm()));
        imgRutas.setImage(new Image(getClass().getResource("/images/imgRutas.png").toExternalForm()));
    }

    /**
     * Navega a la vista para crear una nueva ruta.
     *
     * @param actionEvent El evento de acción que desencadenó este método.
     */
    public void crearRuta(ActionEvent actionEvent) {
        SceneManager.goToView(actionEvent, "datosRuta.fxml", this.getClass(), 500, 900);
    }

    /**
     * Navega a la vista para crear un nuevo Pokémon.
     *
     * @param actionEvent El evento de acción que desencadenó este método.
     */
    public void crearPokemon(ActionEvent actionEvent) {
        SceneManager.goToView(actionEvent, "editarPokemon.fxml", this.getClass(), 650, 600);
    }

    /**
     * Busca una ruta en la base de datos según su nombre y región seleccionada.
     * Si se encuentra, navega a la vista de detalles de la ruta.
     *
     * @param actionEvent El evento de acción que desencadenó este método.
     */
    public void buscarRuta(ActionEvent actionEvent) {
        String nombreRuta = txtNombreRuta.getText();
        String nombreRegion = cmbRegiones.getSelectionModel().getSelectedItem();
        Optional<Ruta> ruta = rutaBD.getRuta(nombreRuta, nombreRegion);

        if (ruta.isPresent()) {
            Map<String, Object> datos = new HashMap<>();
            datos.put("ruta", ruta.get());
            SceneManager.setDatos(datos);
            SceneManager.goToView(actionEvent, "datosRuta.fxml", this.getClass(), 650, 700);
        } else {
            View.lanzarMensajeError("Error", "Ruta no encontrada",
                    "No existe ninguna ruta con los datos especificados en la base de datos.");
        }
    }

    /**
     * Busca un Pokémon en la base de datos según su nombre o ID.
     * Si se encuentra, navega a la vista de detalles del Pokémon.
     *
     * @param actionEvent El evento de acción que desencadenó este método.
     */
    public void buscarPokemon(ActionEvent actionEvent) {
        String pokemonSearch = txtNombrePokemon.getText();
        Pokemon pokemon;

        try {
            int id = Integer.parseInt(pokemonSearch);
            pokemon = pokemonBD.getPokemonById(id);
        } catch (NumberFormatException e) {
            pokemon = pokemonBD.getPokemonByName(pokemonSearch);
        }

        if (pokemon != null) {
            Map<String, Object> datos = new HashMap<>();
            datos.put("pokemon", pokemon);
            SceneManager.setDatos(datos);
            SceneManager.goToView(actionEvent, "datosPokemon.fxml", this.getClass(), 750, 650);
        } else {
            View.lanzarMensajeError("Error", "Pokémon no encontrado.",
                    "En la base de datos no se encuentra el nombre del pokémon o el ID introducido.");
        }
    }

    /**
     * Crea un objeto criteria con los criterios de búsqueda seleccionados.
     * Navega a la vista de la lista filtrada si los parámetros son válidos,
     * y envía los criterios seleccionados.
     *
     * @param actionEvent El evento de acción que desencadenó este método.
     */
    public void filtrarPokemon(ActionEvent actionEvent) {
        String tipo1 = txtTipo1.getText();

        if (!tipo1.isEmpty()) {
            String tipo2 = txtTipo2.getText();
            String criterio = cmbCriterio.getSelectionModel().getSelectedItem();
            String orden = cmbOrden.getSelectionModel().getSelectedItem();

            CriteriaPokemon criteria = new CriteriaPokemon(tipo1, tipo2, criterio, orden);
            Map<String, Object> datos = new HashMap<>();
            datos.put("criteriaPokemon", criteria);
            SceneManager.setDatos(datos);
            SceneManager.goToView(actionEvent, "listaPokemon.fxml", this.getClass(), 800, 650);
        } else {
            View.lanzarMensajeError("Error", "Tipo no seleccionado",
                    "Para realizar un filtrado de Pokémon, es imprescindible introducir un tipo en la primera casilla (tipo 1).");
        }
    }

    /**
     * Crea un objeto criteria con el valor de nombre de pokémon y región seleccionados
     * Navega a la vista de la lista filtrada y envía los datos de criteria.
     *
     * @param actionEvent El evento de acción que desencadenó este método.
     */
    public void filtrarRuta(ActionEvent actionEvent) {
        String pokemon = txtPokemonRuta.getText();
        String regionSeleccionada = cmbFiltrarRegiones.getSelectionModel().getSelectedItem();

        CriteriaRuta criteriaRuta = new CriteriaRuta(pokemon, regionSeleccionada);
        Map<String, Object> datos = new HashMap<>();
        datos.put("criteriaRuta", criteriaRuta);
        SceneManager.setDatos(datos);
        SceneManager.goToView(actionEvent, "listaRutas.fxml", this.getClass(), 550, 600);
    }
}
