package edu.badpals.pokebase.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public class ImageToBytes {
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
