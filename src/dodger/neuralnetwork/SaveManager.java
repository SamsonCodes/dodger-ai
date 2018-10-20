package dodger.neuralnetwork;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */



import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

public class SaveManager 
{
    public SaveManager(){}
    
    public void saveData(ArrayList<String> data, String fileName)
    {
        try{
            PrintWriter writer = new PrintWriter(fileName + ".txt", "UTF-8");
            for(String line: data)
            {
                writer.println(line);
            }
            writer.close();
            System.out.println("Saved to " + fileName + ".txt");
        } catch (FileNotFoundException | UnsupportedEncodingException ex) {
            Logger.getLogger(SaveManager.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void printData(String fileName)
    {
        try 
        { 
            FileReader fileReader = new FileReader(fileName + ".txt");
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            String line;
            while((line = bufferedReader.readLine()) != null) 
            {
                System.out.println(line);
            }
        } 
        catch (IOException ex)
        {
            System.out.println("Error reading file");
        }
    }
    
    public String readData(String fileName, int lineNumber, int wordIndex)
    {
        String dataRequested = "";
        try 
        { 
            FileReader fileReader = new FileReader(fileName + ".txt");
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            String line;
            int currentLine = 0;
            while((line = bufferedReader.readLine()) != null) 
            {
                currentLine++;
                if(currentLine == lineNumber)
                {
                    String[] words = line.split("\t");
                    if(words.length > wordIndex)
                    {
                        dataRequested = words[wordIndex];
                    }
                }
            }
        } 
        catch (IOException ex)
        {
            System.out.println("Error reading file");
        }
        return dataRequested;
    }
    
    public ArrayList<String> loadData(String fileName)
    {
        ArrayList<String> collectedData = new ArrayList<>();
        try 
        { 
            FileReader fileReader = new FileReader(fileName + ".txt");
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            String line;
            while((line = bufferedReader.readLine()) != null) 
            {
                collectedData.add(line);
            }
            System.out.println("Loaded from " + fileName + ".txt");
            return collectedData;
        } 
        catch (IOException ex)
        {
            System.out.println("Error reading file");
        }
        return null;
    }
}
