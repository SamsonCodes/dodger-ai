/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package customgame.customui;

import customgame.Gui;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.KeyEvent;

public class InputBox extends UIElement
{
    private long lastInput, inputCooldown;
    private String input;
    private boolean selected, sent;
    
    public InputBox(Gui gui, int x, int y, int width, int height) 
    {
        super(gui);
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        input = "";
        inputCooldown = 100;
        lastInput = System.currentTimeMillis();
        selected = true;
        sent = false;
    }

    @Override
    public void update() 
    {
        if(!sent)
        {
            if(System.currentTimeMillis() - lastInput > inputCooldown)
            {
                if(gui.getMouseManager().leftClick)
                {
                    lastInput = System.currentTimeMillis();
                    if(!selected)
                        selected = true;
                    else
                        selected = false;
                }
                if(selected)
                {
                    if(gui.getKeyManager().keys[KeyEvent.VK_1])
                    {
                        lastInput = System.currentTimeMillis();
                        input+="1";
                    }
                    if(gui.getKeyManager().keys[KeyEvent.VK_2])
                    {
                        lastInput = System.currentTimeMillis();
                        input+="2";
                    }
                    if(gui.getKeyManager().keys[KeyEvent.VK_3])
                    {
                        lastInput = System.currentTimeMillis();
                        input+="3";
                    }
                    if(gui.getKeyManager().keys[KeyEvent.VK_4])
                    {
                        lastInput = System.currentTimeMillis();
                        input+="4";
                    }
                    if(gui.getKeyManager().keys[KeyEvent.VK_5])
                    {
                        lastInput = System.currentTimeMillis();
                        input+="5";
                    }
                    if(gui.getKeyManager().keys[KeyEvent.VK_6])
                    {
                        lastInput = System.currentTimeMillis();
                        input+="6";
                    }
                    if(gui.getKeyManager().keys[KeyEvent.VK_7])
                    {
                        lastInput = System.currentTimeMillis();
                        input+="7";
                    }
                    if(gui.getKeyManager().keys[KeyEvent.VK_8])
                    {
                        lastInput = System.currentTimeMillis();
                        input+="8";
                    }
                    if(gui.getKeyManager().keys[KeyEvent.VK_9])
                    {
                        lastInput = System.currentTimeMillis();
                        input+="9";
                    }
                    if(gui.getKeyManager().keys[KeyEvent.VK_0])
                    {
                        lastInput = System.currentTimeMillis();
                        input+="0";
                    }
                    if(gui.getKeyManager().keys[KeyEvent.VK_ENTER])
                    {
                        lastInput = System.currentTimeMillis();
                        if(selected && !input.equals(""))
                        {
                            sent = true;
                        }
                    }
                    if(gui.getKeyManager().keys[KeyEvent.VK_BACK_SPACE])
                    {
                        lastInput = System.currentTimeMillis();
                        if(!input.isEmpty())
                            input = input.substring(0, input.length()-1);
                    }
                }
            }
        }
    }
    
    public boolean isSent()
    {
        return sent;
    }
    
    public int getInput()
    {
        return Integer.parseInt(input);
    }
    
    public void reset()
    {
        selected = true;
        sent = false;
        input = "";
        lastInput = System.currentTimeMillis();
    }

    @Override
    public void render(Graphics g) 
    {
        if(!selected || sent)
            g.setColor(Color.GRAY);
        else
            g.setColor(Color.WHITE);
        g.fillRect(x, y, width, height);
        g.setColor(Color.BLACK);
        g.drawString(input, x, y + height/2 + g.getFont().getSize());
    }

}
