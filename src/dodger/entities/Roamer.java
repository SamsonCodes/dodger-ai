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

public class Roamer extends Enemy
{

    public Roamer(Game game, Color color, double x, double y, double size, double xSpeed, double ySpeed, int damage) {
        super(game, color, x, y, size, xSpeed, ySpeed, damage);
    }

    @Override
    public void update() 
    {
        x+=xSpeed;
        if(x > Dodger.WIDTH - width)
        {
            x = Dodger.WIDTH - width;
            xSpeed = - xSpeed;
        }
        else if(x < 0)
        {
            x = 0;
            xSpeed = - xSpeed;
        }
        
        y+=ySpeed;
        if(y > Dodger.HEIGHT - height)
        {
            y = Dodger.HEIGHT - height;
            ySpeed = - ySpeed;
        }
        else if(y < 0)
        {
            y = 0;
            ySpeed = - ySpeed;
        }
    }
    
    @Override
    public void render(Graphics g) 
    {
        g.setColor(color);
        g.fillRect((int) x, (int) y, (int) width, (int) height);
    }

}
