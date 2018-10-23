/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package dodger.entities;

import customgame.Game;
import dodger.Dodger;
import java.awt.Color;
import java.awt.Graphics;

public class Hunter extends Enemy
{
    private int xTarget, yTarget;
    
    public Hunter(Game game, Color color, double x, double y, double size, double speed, int damage) {
        super(game, color, x, y, size, speed, speed, damage);
        xTarget = Dodger.WIDTH/2;
        yTarget = Dodger.HEIGHT/2;
    }
    
    @Override
    public void update() 
    {
        double midX = x + width/2;
        if(midX < xTarget)
            x += Math.min(xSpeed, xTarget - midX);
        if(midX > xTarget)
            x -= Math.min(xSpeed, midX - xTarget);
        double midY = y + height/2;
        if(midY < yTarget)
            y += Math.min(ySpeed, yTarget - midY);
        if(midY > yTarget)
            y -= Math.min(ySpeed, midY - yTarget);
    }
    
    @Override
    public void render(Graphics g) 
    {
        g.setColor(color);
        g.fillRect((int) x, (int) y, (int) width, (int) height);
        //g.drawOval(xTarget, yTarget, 5, 5);
    }
    
    public void setTarget(int xTarget, int yTarget)
    {
        this.xTarget = xTarget;
        this.yTarget = yTarget;
    }
}
