/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package customgame.input;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class KeyManager implements KeyListener
{
    public boolean[] keys;
    
    public KeyManager()
    {
        keys = new boolean[256];
    }
    
    @Override
    public void keyTyped(KeyEvent ke){}

    @Override
    public void keyPressed(KeyEvent ke) 
    {
        if(!keys[ke.getKeyCode()])
        {
            keys[ke.getKeyCode()] = true;
        }
    }

    @Override
    public void keyReleased(KeyEvent ke) 
    {
        if(keys[ke.getKeyCode()])
        {
            keys[ke.getKeyCode()]=false;
        }
        
    }
}
