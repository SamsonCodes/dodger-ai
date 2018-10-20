/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package dodger.entities;

import customgame.Game;
import java.awt.Graphics;

public abstract class Entity 
{
    protected Game game;
    protected double x, y, width, height;
    protected boolean active;
    
    public Entity(Game game, double x, double y, double width, double height)
    {
        this.game = game;
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        active = true;
    }
    
    public abstract void update();
    public abstract void render(Graphics g);

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }

    public double getWidth() {
        return width;
    }

    public void setWidth(double width) {
        this.width = width;
    }

    public double getHeight() {
        return height;
    }

    public void setHeight(double height) {
        this.height = height;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }
}
