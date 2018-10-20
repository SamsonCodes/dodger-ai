/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package customgame.customui;

import customgame.Gui;
import java.awt.Graphics;

public abstract class UIElement 
{
    int x, y, width, height;
    boolean active;
    protected Gui gui;
    
    public UIElement(Gui gui)
    {
        this.gui = gui;
    }
    
    public abstract void update();
    public abstract void render(Graphics g);
    
    public void setActive(boolean b)
    {
        active = b;
    }
    
    public boolean getActive()
    {
        return active;
    }
}
