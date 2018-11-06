/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package customgame;

import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;

/**
 * This class allows for saving text, loading text, deleting files and loading images.
 * @author Samson
 */
public class DataHandler
{

    public static void saveData(ArrayList<String> data, String filePath)
    {
        try
        {
            try (PrintWriter writer = new PrintWriter(filePath, "UTF-8"))
            {
                data.stream().forEach((line)
                        -> 
                        {
                            writer.println(line);
                }); 
                
                writer.close();
                data.stream().close();
            }
        }
        catch (FileNotFoundException | UnsupportedEncodingException ex)
        {
            Logger.getLogger(DataHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static void deleteData(String filePath)
    {
        java.nio.file.Path path = Paths.get(filePath);
        try
        {
            Files.delete(path);
        }
        catch (IOException ex)
        {
            Logger.getLogger(DataHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static String loadData(String path)
    {
        String data = "";
        try
        {
            FileReader fileReader = new FileReader(path);
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            String line;
            while ((line = bufferedReader.readLine()) != null)
            {
                data += line;
            }
        }
        catch (FileNotFoundException ex)
        {
            Logger.getLogger(DataHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
        catch (IOException ex)
        {
            Logger.getLogger(DataHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
        return data;
    }

    public static BufferedImage loadImage(String path)
    {
        BufferedImage image = null;
        try
        {
            image = ImageIO.read(new File(path));
        }
        catch (IOException ex)
        {
            System.out.println("TilePalet: loadImage: Error loading " + path);
        }
        return image;
    }
}
