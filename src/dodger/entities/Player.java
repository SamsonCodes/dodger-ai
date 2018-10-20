/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package dodger.entities;

import customgame.Game;
import dodger.Dodger;
import dodger.neuralnetwork.Network;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import java.util.ArrayList;

public class Player extends Entity
{
    private long lastInput, inputCooldown;
    private double moveStep;
    private int xDirection, yDirection;
    private final int MAX_HEALTH = 100;
    private int health;
    private boolean ai;
    private Network network;
    private ArrayList<Enemy> enemies;
    //private int[] inputIndices;

    public Player(Game game, double x, double y, double width, double height) 
    {
        super(game, x, y, width, height);
        lastInput = System.currentTimeMillis();
        inputCooldown = 100;
        moveStep = 20;
        xDirection = 0;
        yDirection = 0;
        health = MAX_HEALTH;
        ai = false;
    }
    
    public Player(Game game, double x, double y, double width, double height, Network network, ArrayList<Enemy> enemies) 
    {
        super(game, x, y, width, height);
        this.network = network;
        this.enemies = enemies;
        lastInput = System.currentTimeMillis();
        inputCooldown = 100;
        moveStep = 10;
        xDirection = 0;
        yDirection = 0;
        health = MAX_HEALTH;
        ai = true;
//        if(inputIndices.length <= Dodger.INPUT_NR)
//            this.inputIndices = inputIndices;
//        else
//            System.out.println("Player: to many inputIncices!");
    }

    @Override
    public void update() 
    {
        if(ai)
        {
            float[] inputs = getInputs();
//            double[] realInputs = new double[14];
//            for(int i = 0; i < inputIndices.length; i++)
//            {
//                realInputs[i] = inputs[inputIndices[i]];
//            }
            float[] outputs = network.getOutputs2(inputs);
            if(outputs[0] > 0.75)
                xDirection = 1;
            else if(outputs[0] < 0.25)
                xDirection = -1;
            else
                xDirection = 0;
            if(outputs[1] > 0.75)
                yDirection = 1;
            else if(outputs[1] < 0.25)
                yDirection = -1;
            else
                yDirection = 0; 
        }
        else
        {
            if(System.currentTimeMillis() - lastInput > inputCooldown)
            {
                if(game.getGui().getKeyManager().keys[KeyEvent.VK_LEFT])
                {
                    lastInput = System.currentTimeMillis();
                    xDirection = -1;
                }
                else if(game.getGui().getKeyManager().keys[KeyEvent.VK_RIGHT])
                {
                    lastInput = System.currentTimeMillis();
                    xDirection = 1;
                }
                else
                {
                    xDirection = 0;
                }

                if(game.getGui().getKeyManager().keys[KeyEvent.VK_UP])
                {
                    lastInput = System.currentTimeMillis();
                    yDirection = -1;
                }
                else if(game.getGui().getKeyManager().keys[KeyEvent.VK_DOWN])
                {
                    lastInput = System.currentTimeMillis();
                    yDirection = 1;
                }
                else
                {
                    yDirection = 0;
                }
            }
        }
        switch(xDirection)
        {
            case -1:
                x -= moveStep;
                if(x < 0)
                    x = 0;
                break;
            case 1:
                x += moveStep;
                if(x > Dodger.WIDTH - width)
                    x = Dodger.WIDTH - width;
                break;
        }
        switch(yDirection)
        {
            case -1:
                y -= moveStep;
                if(y < 0)
                    y = 0;
                break;
            case 1:
                y += moveStep;
                if(y > Dodger.HEIGHT - height)
                    y = Dodger.HEIGHT - height;
                break;
        }
        
    }

    @Override
    public void render(Graphics g) 
    {
        g.setColor(Color.WHITE);
        g.fillRect((int) x, (int) y, (int) width, (int) height);
        renderHealthBar(g, 0, Dodger.HEIGHT - 5);
    }

    public ArrayList<Enemy> getEnemyCollisions(ArrayList<Enemy> enemies)
    {
        ArrayList<Enemy> collisions = new ArrayList();
        for(Enemy e: enemies)
        {
            Rectangle r1 = new Rectangle((int) x, (int) y, (int) width, (int) height);
            Rectangle r2 = new Rectangle((int) e.getX(), (int) e.getY(), (int) e.getWidth(), (int) e.getHeight());
            if(r1.intersects(r2))
            {
                collisions.add(e);
            }
        }
        return collisions;
    }
    
    public void damage(int amount)
    {
        health -= amount;
        if(health < 0)
        {
            health = 0;
            setActive(false);
            //System.out.println("Dead!");
        }
        else if(health > MAX_HEALTH)
            health = MAX_HEALTH;
    }
    
//    public void setAIMode(boolean mode)
//    {
//        ai = mode;
//    }
    
    private void renderHealthBar(Graphics g, int xBar, int yBar)
    {
        g.setColor(Color.RED);
        int barWidth = Math.min(200, Dodger.WIDTH);
        int barHeight = 5;
        g.fillRect(xBar, yBar, barWidth, barHeight);
        g.setColor(Color.GREEN);
        int greenWidth = (int) ((double) barWidth * ( (double) health/ (double) MAX_HEALTH ));
        g.fillRect(xBar, yBar, greenWidth, barHeight);
    }
    
    public float[] getInputs()
    {
        float[] inputs = new float[enemies.size()*2 + 4];
        for(int i = 0; i < enemies.size()*2 - 1; i++)
        {
            if(i % 2 == 0)
                inputs[i] = (float) enemies.get(i/2).getX();
            else
                inputs[i] = (float) enemies.get(i/2).getY();
        }
        inputs[enemies.size()*2] = (float) x;
        inputs[enemies.size()*2 + 1] = (float) y;
        inputs[enemies.size()*2 + 2] = Dodger.WIDTH/2;
        inputs[enemies.size()*2 + 3] = Dodger.HEIGHT/2; 
        return inputs;
    }
    
    public double distanceToMid()
    {
        double a = Math.abs(x - Dodger.WIDTH/2);
        double b = Math.abs(y - Dodger.HEIGHT/2);
        return Math.sqrt(Math.pow(a,2) + Math.pow(b, 2));
    }
}
