package edu.badpals.pokebase.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.*;


public class DocumentExporter {
    /**
     * Exporta los datos de un Pokémon a un archivo JSON.
     *
     * @param data El objeto PokemonData a exportar.
     * @param path La ruta donde se guardará el archivo JSON (sin extensión).
     * @return true si la exportación fue exitosa, false en caso contrario.
     */
    public static boolean exportToJson(Object data, String path) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            mapper.writerWithDefaultPrettyPrinter().writeValue(new File(path), data);
        } catch (IOException e) {
            ErrorLogger.saveErrorLog("Error de exportación: " + e.getMessage());
            return false;
        }
        return true;
    }

}

