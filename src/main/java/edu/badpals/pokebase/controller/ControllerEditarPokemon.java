package edu.badpals.pokebase.controller;

import edu.badpals.pokebase.model.*;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;

public class ControllerEditarPokemon {

    @FXML
    private TextField tfNombre, tfId, tfTipo1, tfTipo2, tfEvolucionaDe, tfMetodoEvolucion, tfImagen, tfGif, tfShiny;
    @FXML
    private Button btnCargarImagen, btnCargarGif, btnCargarShiny;

    private Pokemon pokemon;

    private AccesoBD accesoBD;
    private PokemonBD pokemonBD;
    private RutaBD rutaBD;

    public void initialize() {
        accesoBD = new AccesoBD();
        accesoBD.connect();
        pokemonBD = new PokemonBD(accesoBD);
        rutaBD = new RutaBD(accesoBD);

        permitirSoloEnteros(tfId);
        permitirSoloEnteros(tfEvolucionaDe);

    }

    private void permitirSoloEnteros(TextField textField) {
        TextFormatter<String> integerFormatter = new TextFormatter<>(change ->
                change.getControlNewText().matches("\\d*") ? change : null);
        textField.setTextFormatter(integerFormatter);
    }

    public void setPokemon(Pokemon pokemon) {
        this.pokemon = pokemon;
        rellenarCampos();
    }

    public void rellenarCampos(){
        if(this.pokemon != null){
            tfNombre.setText(pokemon.getNombre());
            tfId.setText(pokemon.getId().toString());
            tfTipo1.setText(pokemon.getTipo1());
            if(pokemon.getTipo2() != null){
                tfTipo2.setText(pokemon.getTipo2());
            }
            if(pokemon.getEvolucionaDe() != null){
                tfEvolucionaDe.setText(pokemon.getEvolucionaDe().toString());
                tfMetodoEvolucion.setText(pokemon.getMetodoEvolucion());
            }
            if(pokemon.getImagen() != null){
                tfImagen.setText("Imagen cargada");
            }
            if(pokemon.getGif() != null){
                tfGif.setText("Gif cargado");
            }
            if(pokemon.getImagenShiny() != null){
                tfShiny.setText("Imagen Shiny cargada");
            }


        }
    }

    public void cargarImagen(ActionEvent actionEvent) {

        String tipoArchivo = "";
        String titulo = "";
        TextField textFieldDestino = null;

        if (actionEvent.getSource() == btnCargarImagen) {
            tipoArchivo = "*.jpg, *.jpeg, *.png";
            titulo = "Seleccionar imagen";
            textFieldDestino = tfImagen;
        } else if (actionEvent.getSource() == btnCargarGif) {
            tipoArchivo = "*.gif";
            titulo = "Seleccionar GIF";
            textFieldDestino = tfGif;
        } else if (actionEvent.getSource() == btnCargarShiny) {
            tipoArchivo = "*.jpg, *.jpeg, *.png";
            titulo = "Seleccionar shiny";
            textFieldDestino = tfShiny;
        }

        abrirFileChooser(titulo, tipoArchivo, textFieldDestino, actionEvent);

    }

    private void abrirFileChooser(String titulo, String tipoArchivo, TextField destino, ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle(titulo);

        String[] tipos = tipoArchivo.split(", ");
        FileChooser.ExtensionFilter filter = new FileChooser.ExtensionFilter(titulo, tipos);
        fileChooser.getExtensionFilters().add(filter);

        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

        // Mostrar diálogo y procesar archivo seleccionado
        File archivoSeleccionado = fileChooser.showOpenDialog(stage);
        if (archivoSeleccionado != null) {
            destino.setText(archivoSeleccionado.getAbsolutePath());
        }
    }

    public void crearPokemon(){
        String nombre = tfNombre.getText();
        if(pokemonBD.isNombrePresent(nombre)){
            lanzarMensajeError("Error","Nombre de pokémon no válido","El nombre introducido ya se encuentra en la base de datos.");
            return;
        }
        int id = Integer.parseInt(tfId.getText());
        if(pokemonBD.isIdPresent(id)){
            lanzarMensajeError("Error","Id no válido","El id introducido ya se encuentra en la base de datos.");
            return;
        }
        String tipo1 = tfTipo1.getText();
        String tipo2 = tfTipo2.getText();
        int evolucionaDe = Integer.parseInt(tfEvolucionaDe.getText());
        if(!pokemonBD.isIdPresent(evolucionaDe)){
            lanzarMensajeError("Error","Preevolución no válida","El id introducido en 'Evoluciona de' no existe en la base de datos.");
            return;
        }
        String metodoEvolucion = tfMetodoEvolucion.getText();
        byte[] imagen = ImageToBytes.toByteArray(tfImagen.getText());
        byte[] gif = ImageToBytes.toByteArray(tfGif.getText());
        byte[] shiny = ImageToBytes.toByteArray(tfShiny.getText());


        Pokemon nuevoPokemon = new Pokemon(id,nombre,imagen,gif,shiny,tipo1,tipo2,evolucionaDe,metodoEvolucion);
        if(pokemonBD.insertPokemon(nuevoPokemon)){
            lanzarMensajeAviso("Aviso","Pokemon creado","Se completó la creación con éxito.");
        }else{
            lanzarMensajeError("Error","No se ha creado al Pokémon","Se ha producido un error inesperado y el proceso ha sido abortado.");
        }

    }

    public void limpiarPanel() {
        this.pokemon = null;
        tfNombre.setText("");
        tfId.setText("");
        tfTipo1.setText("");
        tfTipo2.setText("");
        tfEvolucionaDe.setText("");
        tfMetodoEvolucion.setText("");
        tfImagen.setText("");
        tfGif.setText("");
        tfShiny.setText("");

    }

    public void handleVolver(ActionEvent actionEvent) {
    }

    public void eliminarPokemon(ActionEvent actionEvent) {
    }

    public void lanzarMensajeAviso(String titulo, String cabecera, String mensaje){
        Alert error = new Alert(Alert.AlertType.INFORMATION);
        error.setTitle(titulo);
        error.setHeaderText(cabecera);
        error.setContentText(mensaje);

        error.showAndWait();
    }

    public void lanzarMensajeError(String titulo, String cabecera, String mensaje){
        Alert error = new Alert(Alert.AlertType.ERROR);
        error.setTitle(titulo);
        error.setHeaderText(cabecera);
        error.setContentText(mensaje);

        error.showAndWait();
    }
}
