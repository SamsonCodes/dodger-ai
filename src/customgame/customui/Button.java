/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package customgame.customui;

import customgame.Gui;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Rectangle;

public class Button extends UIElement
{
    String buttonText;
    boolean pressed, hover, selected;
    Font buttonFont = new Font("Arial", Font.BOLD, 18);
    
    public Button(Gui gui, int x, int y, int width, int height, String buttonText)
    {
        super(gui);        
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        if(buttonText != null)
            this.buttonText = buttonText;
        else
            this.buttonText = "";
        active = true;
    }
    
    public boolean getPressed()
    {
        return pressed;
    }

    @Override
    public void update() 
    {
        if(!selected)
        {
            int pointerX = gui.getMouseManager().x;
            int pointerY = gui.getMouseManager().y;
            int range = 1;
            Rectangle pointer = new Rectangle(pointerX-range/2, pointerY-range/2, range, range);
            Rectangle buttonBounds = new Rectangle(x,y,width,height);
            if(pointer.intersects(buttonBounds) &! buttonText.equals(""))
            {
                hover = true;
            }
            else
            {
                hover = false;
            }
            if(gui.getMouseManager().leftClick && hover)
            {
                pressed = true;
            }
            else
                pressed = false;
        }
    }
    
    public void setSelected(boolean b)
    {
        selected = b;
        pressed = false;
        hover = false;
    }

    @Override
    public void render(Graphics g) 
    {
        if(active)
        {
            if(selected)
                g.setColor(Color.BLUE);
            else if(hover)
                g.setColor(Color.WHITE);
            else
                g.setColor(Color.LIGHT_GRAY);
            g.fillRect(x, y, width, height);
            //g.drawImage(Assets.button, x, y, width, height, null);
            g.setColor(Color.BLACK);
            g.setFont(buttonFont);
            g.drawString(buttonText, x+10, y+height/2);
            g.drawRect(x, y, width, height);
        }
    }
}
