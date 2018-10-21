/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package dodger;

import customgame.Game;
import customgame.states.IState;
import dodger.entities.Enemy;
import dodger.entities.Player;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Random;

public class AIState implements IState
{
    private Dodger dodger;
    private Game game;
    private Player player;
    private ArrayList<Enemy> enemies;
    private int score, maxScore;
    private boolean showNetwork;
    private Random random = new Random();
    
    public AIState(Dodger dodger, Game game)
    {
        this.dodger = dodger;
        this.game = game;
        maxScore = 0;
    }
    
    @Override
    public void onEnter() 
    {
        System.out.println("Entering BaseState");
        enemies = new ArrayList(); 
        
            double eWidth = 25;
            double eHeight = eWidth;
        for(int i = 0; i < Dodger.ENEMY_NUMBER; i++)
        {
            double x1 = (Dodger.WIDTH - eWidth)*random.nextDouble(); 
            double y1 = (Dodger.HEIGHT - eHeight)*random.nextDouble(); 
            double xSpeed = Math.signum(0.5 - random.nextDouble())*Dodger.ENEMY_SPEED*(1 + random.nextDouble());
            double ySpeed = Math.signum(0.5 - random.nextDouble())*Dodger.ENEMY_SPEED*(1 + random.nextDouble());
            Enemy e1 = new Enemy(game, Color.RED, x1, y1, eWidth, eHeight, xSpeed, ySpeed, 1);
            enemies.add(e1);
        }
        
        double pWidth = 50;
        double pHeight = pWidth;
        player = new Player(game, Dodger.WIDTH/2 - pWidth/2, Dodger.HEIGHT/2 - pHeight/2, pWidth, pHeight, dodger.getAI(), enemies);
        score = 0;
        showNetwork = false;
    }
    
    @Override
    public void onExit() 
    {
        System.out.println("Exiting BaseState");
    }

    @Override
    public void update() 
    {
        if(player.isActive())
        {
            player.update();
        }
        else
        {
            onEnter();
        }
        for(Enemy e: enemies)
        {
            e.update();
            //System.out.println("EnemyX = " + enemies.get(0).getX());
        }
        
        if(!player.getEnemyCollisions(enemies).isEmpty())
        {
            for(Enemy e: player.getEnemyCollisions(enemies))
            {
                player.damage(e.getDamage());
            }
        }
        if(game.getGui().getKeyManager().keys[KeyEvent.VK_9])
            showNetwork = true;
        if(game.getGui().getKeyManager().keys[KeyEvent.VK_0])
            showNetwork = false;
    }

    @Override
    public void render(Graphics g) 
    {
        if(showNetwork)
        {
            dodger.getAI().render(g, 0, 0, Dodger.WIDTH, Dodger.HEIGHT, player.getInputs(), Dodger.WIDTH);
        }
            
//        g.setColor(Color.WHITE);
//        String s = "MAXSCORE  = " + maxScore;
//        g.drawString(s, 0, g.getFont().getSize());
//        g.setColor(Color.WHITE);
//        String s2 = "SCORE          = " + score;
//        g.drawString(s2, 0, 3 * g.getFont().getSize());
        if(player.isActive())
        {
            player.render(g);
        }
        for(Enemy e: enemies)
        {
            e.render(g);
        }
    }
}
