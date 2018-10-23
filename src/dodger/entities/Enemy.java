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

public abstract class Enemy extends Entity
{

    double xSpeed,ySpeed;
    int damage;
    Color color;
    
    public Enemy(Game game, Color color, double x, double y, double size, double xSpeed, double ySpeed, int damage) 
    {
        super(game, x, y, size, size);
        this.color = color;
        this.xSpeed = xSpeed;
        this.ySpeed = ySpeed;
        this.damage = damage;
    }
    
    public int getDamage()
    {
        return damage;
    }
    
//    public void setSpeed(int s)
//    {
//        speed = s;
//    }
}
