package edu.badpals.pokebase.controller;

import edu.badpals.pokebase.model.*;
import edu.badpals.pokebase.service.DocumentExporter;
import edu.badpals.pokebase.view.View;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.util.Optional;

public class ControllerEditarPokemon {

    @FXML
    private TextField tfNombre, tfId, tfTipo1, tfTipo2, tfEvolucionaDe, tfMetodoEvolucion, tfImagen, tfGif, tfShiny;
    @FXML
    private Button btnCargarImagen, btnCargarGif, btnCargarShiny, btnModificar, btnEliminar, btnExportar;

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
        if(pokemon == null){
            btnEliminar.setDisable(true);
            btnModificar.setDisable(true);
            btnExportar.setDisable(true);
        }
    }

    private void permitirSoloEnteros(TextField textField) {
        TextFormatter<String> integerFormatter = new TextFormatter<>(change ->
                change.getControlNewText().matches("\\d*") ? change : null);
        textField.setTextFormatter(integerFormatter);
    }

    public void setPokemon(Pokemon pokemon) {
        this.pokemon = pokemon;
        rellenarCampos();
        btnModificar.setDisable(false);
        btnEliminar.setDisable(false);
        btnExportar.setDisable(false);
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

        File archivoSeleccionado = fileChooser.showOpenDialog(stage);
        if (archivoSeleccionado != null) {
            destino.setText(archivoSeleccionado.getAbsolutePath());
        }
    }

    public void crearPokemon(){
        //Comprobamos que tenga todos los campos obligatorios (Nombre, Id y Tipo 1)
        if(tieneCamposObligatorios()){

            String nombre = tfNombre.getText();
            //Comprobamos si el nombre ya existe en la BD
            if(pokemonBD.isNombrePresent(nombre)){
                View.lanzarMensajeError(
                        "Error",
                        "Nombre de pokémon no válido",
                        "El nombre introducido ya se encuentra en la base de datos.");
                return;
            }


            int id = Integer.parseInt(tfId.getText());
            //Comprobamos si el Id ya existe en la BD
            if(pokemonBD.isIdPresent(id)){
                View.lanzarMensajeError(
                        "Error",
                        "Id no válido",
                        "El id introducido ya se encuentra en la base de datos.");
                return;
            }

            //Obtenemos los tipos
            String tipo1 = tfTipo1.getText();
            String tipo2 = tfTipo2.getText();
            if(tipo2.equals("")){
                tipo2 = null;
            }

            Integer idPreEvolucion;
            String evolucionaDe = tfEvolucionaDe.getText();
            String metodoEvolucion = tfMetodoEvolucion.getText();

            //Si la pre-evolucion es introducida, comprobamos que exista en la BD
            if (!evolucionaDe.equals("")) {
                idPreEvolucion = obtenerIdPreEvolucion(evolucionaDe);
                if (idPreEvolucion == null) {
                    return;
                }
                if (metodoEvolucion.equals("")) {
                    View.lanzarMensajeError(
                            "Error",
                            "Método de evolución no presente",
                            "Para añadir la pre-evolución de un pokémon es necesario indicar el método de evolución."
                    );
                    return;
                }
            } else {
                //En caso de que no sea introducida la pre-evolucion, seteamos a null los valores
                idPreEvolucion = null;
                metodoEvolucion = null;
            }

            byte[][] imagenes = obtenerImagenes();

            Pokemon nuevoPokemon = new Pokemon(id,nombre,imagenes[0],imagenes[1],imagenes[2],tipo1,tipo2,idPreEvolucion,metodoEvolucion);

            if(pokemonBD.insertPokemon(nuevoPokemon)){
                setPokemon(nuevoPokemon);
                View.lanzarMensajeAviso(
                        "Aviso",
                        "Pokemon creado",
                        "Se completó la creación con éxito.");
            }else{
                View.lanzarMensajeError(
                        "Error",
                        "No se ha creado al Pokémon",
                        "Se ha producido un error inesperado y el proceso ha sido abortado.");
            }

        }else{
            View.lanzarMensajeError(
                    "Error",
                    "No es posible crear pokémon",
                    "Alguno de los campos obligatorios no está cubierto.");
        }
    }

    public void modificarPokemon(){
        //Comprobamos que tenga todos los campos obligatorios (Nombre, Id y Tipo 1)
        if(tieneCamposObligatorios()){
            Integer antiguoId = pokemon.getId();

            String nuevoNombre = tfNombre.getText();
            //Comprobamos que si el nombre cambia, que no esté repetido
            if(!pokemon.getNombre().equals(nuevoNombre) && pokemonBD.isNombrePresent(nuevoNombre)){
                View.lanzarMensajeError(
                        "Error",
                        "Nombre de pokémon no válido",
                        "El nombre introducido para ser modificado ya se encuentra en la base de datos.");
                return;
            }

            int nuevoId = Integer.parseInt(tfId.getText());
            //Comprobamos que si el Id cambia, que no esté repetido
            if(!pokemon.getId().equals(nuevoId) && pokemonBD.isIdPresent(nuevoId)){
                View.lanzarMensajeError(
                        "Error",
                        "Id no válido",
                        "El id introducido para ser modificado ya se encuentra en la base de datos.");
                return;
            }

            //Obtenemos los tipos
            String nuevoTipo1 = tfTipo1.getText();
            String nuevoTipo2 = tfTipo2.getText();
            if(nuevoTipo2.equals("")){
                nuevoTipo2 = null;
            }

            Integer nuevoIdPreEvolucion;
            String evolucionaDe = tfEvolucionaDe.getText();
            String nuevoMetodoEvolucion = tfMetodoEvolucion.getText();

            //Si la pre-evolucion es introducida, comprobamos que exista en la BD
            if (!evolucionaDe.equals("")) {
                nuevoIdPreEvolucion = obtenerIdPreEvolucion(evolucionaDe);
                if (nuevoIdPreEvolucion == null) {
                    return;
                }
                if (nuevoMetodoEvolucion.equals("")) {
                    View.lanzarMensajeError(
                            "Error",
                            "Método de evolución no presente",
                            "Para añadir la pre-evolución de un pokémon es necesario indicar el método de evolución."
                    );
                    return;
                }
            } else {
                //En caso de que no sea introducida la pre-evolucion, seteamos a null los valores
                nuevoIdPreEvolucion = null;
                nuevoMetodoEvolucion = null;
            }

            byte[][] imagenes = obtenerImagenes();
            if(tfImagen.getText().equals("Imagen cargada")){
                imagenes[0] = pokemon.getImagen();
            }
            if(tfGif.getText().equals("Gif cargado")){
                imagenes[1] = pokemon.getGif();
            }
            if(tfShiny.getText().equals("Imagen Shiny cargada")){
                imagenes[2] = pokemon.getImagenShiny();
            }

            pokemon.setNombre(nuevoNombre);
            pokemon.setId(nuevoId);
            pokemon.setTipo1(nuevoTipo1);
            pokemon.setTipo2(nuevoTipo2);
            pokemon.setImagen(imagenes[0]);
            pokemon.setGif(imagenes[1]);
            pokemon.setImagenShiny(imagenes[2]);
            pokemon.setEvolucionaDe(nuevoIdPreEvolucion);
            pokemon.setMetodoEvolucion(nuevoMetodoEvolucion);

            if(pokemonBD.updatePokemon(antiguoId,pokemon)){
                View.lanzarMensajeAviso(
                        "Aviso",
                        "Pokémon modificado",
                        "Se completó la modificación con éxito.");
            }else{
                View.lanzarMensajeError(
                        "Error",
                        "No se ha modificado al Pokémon",
                        "Se ha producido un error inesperado y el proceso ha sido abortado.");
            }

        }else{
            View.lanzarMensajeError(
                    "Error",
                    "No es posible crear pokémon",
                    "Alguno de los campos obligatorios no está cubierto.");
        }
    }

    private Integer obtenerIdPreEvolucion(String evolucionaDe) {
        try {
            int idPosible = Integer.parseInt(evolucionaDe);
            if (pokemonBD.isIdPresent(idPosible)) {
                return idPosible;
            } else {
                View.lanzarMensajeError(
                        "Error",
                        "Pre-evolución no válida.",
                        "El id introducido como pre-evolución no se encuentra en la base de datos."
                );
            }
        } catch (NumberFormatException e) {
            if (pokemonBD.isNombrePresent(evolucionaDe)) {
                Pokemon preEvolucion = pokemonBD.getPokemonByName(evolucionaDe);
                return preEvolucion.getId();
            } else {
                View.lanzarMensajeError(
                        "Error",
                        "Pre-evolución no válida.",
                        "El nombre introducido como pre-evolución no se encuentra en la base de datos."
                );
            }
        }
        return null;
    }

    private byte[][] obtenerImagenes() {
        byte[][] imagenes = new byte[3][];
        String imagenTxt = tfImagen.getText();
        String gifTxt = tfGif.getText();
        String shinyTxt = tfShiny.getText();

        if(!imagenTxt.equals("")){
            imagenes[0] = ImageToBytes.toByteArray(imagenTxt);
        }else{
            imagenes[0] = null;
        }
        if(!gifTxt.equals("")){
            imagenes[1] = ImageToBytes.toByteArray(gifTxt);
        }else{
            imagenes[1] = null;
        }
        if(!shinyTxt.equals("")){
            imagenes[2] = ImageToBytes.toByteArray(shinyTxt);
        }else{
            imagenes[2] = null;
        }
        return imagenes;
    }

    public void limpiarPanel() {
        if(this.pokemon != null){
            Optional<ButtonType> respuesta = View.lanzarMensajeConfirmacion(
                    "Aviso",
                    "Atención. Se va a limpiar el panel.",
                    "Si limpia el panel, no será posible modificar o eliminar un pokémon, tan solo será posible añadir un nuevo pokemon en esta pantalla. Deberá volver al anterior panel para realizar una modificación.\n\n¿Está seguro?"
            );
            if(respuesta.isPresent() && respuesta.get().getText().equals("Sí")){
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
                btnModificar.setDisable(true);
                btnEliminar.setDisable(true);
                btnExportar.setDisable(true);
            }
        }else{
            tfNombre.setText("");
            tfId.setText("");
            tfTipo1.setText("");
            tfTipo2.setText("");
            tfEvolucionaDe.setText("");
            tfMetodoEvolucion.setText("");
            tfImagen.setText("");
            tfGif.setText("");
            tfShiny.setText("");
            btnModificar.setDisable(true);
            btnEliminar.setDisable(true);
            btnExportar.setDisable(true);
        }
    }

    public void handleVolver(ActionEvent actionEvent) {
    }

    public boolean tieneCamposObligatorios(){
        String nombre = tfNombre.getText();
        String id = tfId.getText();
        String tipo1 = tfTipo1.getText();

        if(nombre == "" || id == "" || tipo1 == ""){
            return false;
        }else{
            return true;
        }

    }

    public void eliminarPokemon() {
        if(this.pokemon == null){
            View.lanzarMensajeError("Error","Error al eliminar pokémon","No se encuentra seleccionado ningún pokémon.");
            return;
        }
        Optional<ButtonType> respuesta = View.lanzarMensajeConfirmacion("Eliminar","Se va a eliminar un pokémon.","¿Está seguro de que desea eliminar el pokémon actual de la base de datos? Esta operación es irreversible.");
        if(respuesta.isPresent() && respuesta.get().getText().equals("Sí")){
            pokemonBD.deletePokemon(pokemon);
            View.lanzarMensajeAviso("Aviso","Eliminación completada","Se ha borrado al pokémon exitosamente");
            this.pokemon = null;
            limpiarPanel();
        }
    }

    public void exportar(ActionEvent action){
        if(this.pokemon != null){
            Stage stage = (Stage) ((Node) action.getSource()).getScene().getWindow();
            Optional<File> posibleDirectorio = View.abrirFileChooserExp(stage);
            if(posibleDirectorio.isPresent()){
                String ruta = posibleDirectorio.get().getAbsolutePath();
                boolean exportaCorrecto = DocumentExporter.exportToJson(pokemon,ruta);
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

}
