/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package customgame.entities;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;

public abstract class Entity 
{
    private EntityManager entityManager;
    private String name;
    protected int x, y, width, height;
    protected Rectangle bounds;
    private boolean active;
    
    public Entity(EntityManager entityManager, String name, int x, int y, int width, int height)
    {
        this.entityManager = entityManager;
        this.name = name;
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        bounds = new Rectangle(0, 0, this.width, this.height);
        active = true;
    }
    
    public abstract void update();
    public abstract void render(Graphics g, int xOfset, int yOfset);
    
    public boolean checkEntityCollisions(int xOfset, int yOfset)
    {
        for(Entity e: entityManager.getEntities())
        {
            if(e.equals(this))
                continue;
            if(e.getBounds(0,0).intersects(getBounds(xOfset, yOfset)))
                return true;
        }
        return false;
    } 
    
    public void drawBounds(Graphics g, int xOfset, int yOfset)
    {
        if(!checkEntityCollisions(0,0))
            g.setColor(Color.GREEN);
        else
            g.setColor(Color.RED);
            
        g.drawRect(x - xOfset, y - yOfset, bounds.width, bounds.height);
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @return the x
     */
    public int getX() {
        return x;
    }

    /**
     * @return the y
     */
    public int getY() {
        return y;
    }

    /**
     * @return the width
     */
    public int getWidth() {
        return width;
    }

    /**
     * @return the height
     */
    public int getHeight() {
        return height;
    }
        
    public boolean isActive()
    {
        return active;
    }
    
    public Rectangle getBounds(int xOfset, int yOfset)
    {
        Rectangle r = new Rectangle(x+bounds.x+xOfset, y + bounds.y+yOfset,
                bounds.width, bounds.height);
        return r;
    }    
}
