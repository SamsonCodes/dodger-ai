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
    private char lastChar;
    private boolean sentChar;
    private char[] knownChars = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ01234567890:\\ ".toCharArray();

    public KeyManager()
    {
        keys = new boolean[256];
    }

    @Override
    public void keyTyped(KeyEvent ke)
    {        
        if (contains(knownChars, ke.getKeyChar()))
        {
            lastChar = ke.getKeyChar();
            sentChar = false;
        }
    }

    @Override
    public void keyPressed(KeyEvent ke)
    {
        if (!keys[ke.getKeyCode()])
        {
            keys[ke.getKeyCode()] = true;
        }
    }

    @Override
    public void keyReleased(KeyEvent ke)
    {
        if (keys[ke.getKeyCode()])
        {
            keys[ke.getKeyCode()] = false;
        }

    }

    public char getChar()
    {
        if (!sentChar)
        {
            sentChar = true;
            return lastChar;
        }
        else
        {
            return '\u0000';
        }
    }

    private boolean contains(char[] array, char character)
    {
        boolean contains = false;
        for (char c : array)
        {
            if (c == character)
            {
                contains = true;
            }
        }
        return contains;
    }
}
