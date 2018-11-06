/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package customgame.customui;

import customgame.Gui;
import java.awt.Color;
import java.awt.Graphics;

public class Label extends UIElement
{
    private String text;

    public Label(Gui gui, String text, int x, int y, int width, int height)
    {
        super(gui);
        this.text = text;
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }
    
    
    @Override
    public void update() 
    {
        
    }

    @Override
    public void render(Graphics g) 
    {
        g.setColor(Color.LIGHT_GRAY);
        g.fillRect(x, y, width, height);
        g.setColor(Color.BLACK);
        g.drawString(text, x + 10, y + height/2);
    }

}
