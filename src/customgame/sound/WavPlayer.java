/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package customgame.sound;

import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

public class WavPlayer 
{
    public void playSound(String path)
    {
        AudioInputStream audioInputStream;
        try {
            audioInputStream = AudioSystem.getAudioInputStream(new File(path));
            Clip clip = AudioSystem.getClip();
            clip.open(audioInputStream);
            clip.start();
        } 
        catch (UnsupportedAudioFileException | IOException | LineUnavailableException ex) 
        {
            System.out.println("Wav Player: Error playing sound!");
            Logger.getLogger(WavPlayer.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
    
    public void playSound(String path, float gain)
    {
        if(gain > 6.0f)
            gain = 6.0f;
        else if(gain < -80.0f)
            gain = -80.0f;
        AudioInputStream audioInputStream;
        try {
            audioInputStream = AudioSystem.getAudioInputStream(new File(path));
            Clip clip = AudioSystem.getClip();
            clip.open(audioInputStream);
            FloatControl gainControl = 
                (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
            gainControl.setValue(gain);
            clip.start();
        } 
        catch (UnsupportedAudioFileException | IOException | LineUnavailableException ex) 
        {
            System.out.println("Wav Player: Error playing sound!");
            Logger.getLogger(WavPlayer.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
}
