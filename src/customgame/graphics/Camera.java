/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package customgame.graphics;

import customgame.Gui;
import customgame.entities.Entity;

public class Camera 
{
    private Gui gui;
    private int xOfset, yOfset;
    private int worldWidth, worldHeight;
    private boolean checkBlankSpace;
    
    public Camera(Gui gui)
    {
        this.gui = gui;
        this.xOfset = 0;
        this.yOfset = 0;
        worldWidth = gui.getWidth();
        worldHeight = gui.getHeight();
        checkBlankSpace = false;
    }
    
    private void checkBlankSpace()
    {
        if(xOfset < 0)
        {
            xOfset = 0;
        }
        else if(xOfset > getWorldWidth() - gui.getWidth())
        {
            xOfset = getWorldWidth() - gui.getWidth();
        }
        if(yOfset < 0)
        {
            yOfset = 0;
        }
        else if(yOfset > getWorldHeight() - gui.getHeight())
        {
            yOfset = getWorldHeight() - gui.getHeight();
        }
    }
    
    public void centerOnEntity(Entity e)
    {
        xOfset = e.getX() + e.getWidth()/2 - gui.getWidth()/2;
        yOfset = e.getY() + e.getHeight()/2 - gui.getHeight()/2;
        if(isCheckBlankSpace())
            checkBlankSpace();
    }
    
    public void move(int xAmount, int yAmount)
    {
        xOfset += xAmount;
        yOfset += yAmount;
        if(isCheckBlankSpace())
            checkBlankSpace();
    }

    public int getxOfset() {
        return xOfset;
    }

    public void setXOfset(int xOfset) {
        this.xOfset = xOfset;
    }

    public int getyOfset() {
        return yOfset;
    }

    public void setYOfset(int yOfset) {
        this.yOfset = yOfset;
    }
    
    public int getWorldWidth() {
        return worldWidth;
    }

    public void setWorldWidth(int worldWidth) {
        this.worldWidth = worldWidth;
    }

    public int getWorldHeight() {
        return worldHeight;
    }

    public void setWorldHeight(int worldHeight) {
        this.worldHeight = worldHeight;
    }

    public boolean isCheckBlankSpace() {
        return checkBlankSpace;
    }
    
    public void setCheckBlankSpace(boolean checkBlankSpace) {
        this.checkBlankSpace = checkBlankSpace;
    }
}
