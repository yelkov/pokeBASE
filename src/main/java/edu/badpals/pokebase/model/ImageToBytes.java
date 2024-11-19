package edu.badpals.pokebase.model;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
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
            System.out.println("Error al convertir imagen a bytes");
            e.printStackTrace();
            return null;
        }
    }


}
