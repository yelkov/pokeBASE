package edu.badpals.pokebase.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

/**
 * Proporciona un método para convertir imágenes a un arreglo de bytes,
 * para almacenarlas en bases de datos.
 */
public class ImageToBytes {
    /**
     * Convierte una imagen a un arreglo de bytes.
     *
     * Este método toma la ruta de un archivo de imagen, lo lee y lo convierte en un arreglo de bytes.
     *
     * @param imagePath La ruta del archivo de imagen que se va a convertir a bytes.
     * @return Un arreglo de bytes que representa la imagen,
     * o {@code null} si ocurre un error al leer el archivo o convertirlo.
     */
    public static byte[] toByteArray(String imagePath){
            File file = new File(imagePath);
            try (FileInputStream fis = new FileInputStream(file)) {
                byte[] bytes = new byte[(int) file.length()];
                fis.read(bytes);
                return bytes;
            }catch(IOException e){
            ErrorLogger.saveErrorLog("Error al convertir imagen a bytes" + e.getMessage());
            return null;
        }
    }


}
