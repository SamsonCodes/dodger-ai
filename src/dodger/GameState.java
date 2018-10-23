/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package dodger;

import customgame.Game;
import customgame.states.IState;
import dodger.entities.Enemy;
import dodger.entities.Hunter;
import dodger.entities.Player;
import dodger.entities.Roamer;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Random;

public class GameState implements IState
{
    private Dodger dodger;
    private Game game;
    private Player player;
    private ArrayList<Enemy> enemies;
    private int score, maxScore, lastScore;
    private Random random = new Random();
    private boolean play;
    private int showNetwork;
    private final long SCORE_INTERVAL = 1000, ENTRY_COOLDOWN = 1000;
    private long lastScoreIncrease, enterTime;
    
    public GameState(Dodger dodger, Game game)
    {
        this.dodger = dodger;
        this.game = game;
        maxScore = 0;        
        lastScore = 0;
    }
    
    @Override
    public void onEnter() 
    {
        System.out.println("Entering BaseState");
        enemies = new ArrayList(); 
        
        double rSize = 25;        
        double xSpeed = 0.71 * Dodger.ENEMY_SPEED;
        double ySpeed = xSpeed;
        int rDamage = 3;
        
        double x1 = (Dodger.WIDTH - rSize)/4; 
        double y1 = (Dodger.HEIGHT - rSize)/4; 
        Roamer r1 = new Roamer(game, Color.YELLOW, x1, y1, rSize, -xSpeed, -ySpeed, rDamage);
        enemies.add(r1);
        double x2 = (Dodger.WIDTH - rSize)/4; 
        double y2 = 3*(Dodger.HEIGHT - rSize)/4; 
        Roamer r2 = new Roamer(game, Color.YELLOW, x2, y2, rSize, -xSpeed, ySpeed, rDamage);
        enemies.add(r2);
        double x3 = 3*(Dodger.WIDTH - rSize)/4; 
        double y3 = (Dodger.HEIGHT - rSize)/4; 
        Roamer r3 = new Roamer(game, Color.YELLOW, x3, y3, rSize, xSpeed, -ySpeed, rDamage);
        enemies.add(r3);
        double x4 = 3*(Dodger.WIDTH - rSize)/4; 
        double y4 = 3*(Dodger.HEIGHT - rSize)/4; 
        Roamer r4 = new Roamer(game, Color.YELLOW, x4, y4, rSize, xSpeed, ySpeed, rDamage);
        enemies.add(r4);            
        enemies.add(new Hunter(game, Color.RED, 0, 0, 25, Dodger.ENEMY_SPEED/4, 1));
        
        double pWidth = 50;
        double pHeight = pWidth;
        showNetwork = 0;
        if(dodger.getAI() != null)
        {
            player = new Player(game, Dodger.WIDTH/2 - pWidth/2, Dodger.HEIGHT/2 - pHeight/2, pWidth, pHeight, dodger.getAI(), enemies);
            play = true;
        }
        else
        {
            player = new Player(game, Dodger.WIDTH/2 - pWidth/2, Dodger.HEIGHT/2 - pHeight/2, pWidth, pHeight);
            play = false;
        }
        score = 0;
        lastScoreIncrease = System.currentTimeMillis();
        enterTime = System.currentTimeMillis();
    }
    
    @Override
    public void onExit() 
    {
        System.out.println("Exiting BaseState");
    }

    @Override
    public void update() 
    {
        if(!play && System.currentTimeMillis() - enterTime > ENTRY_COOLDOWN)
            if(game.getGui().getKeyManager().keys[KeyEvent.VK_UP]
                    || game.getGui().getKeyManager().keys[KeyEvent.VK_DOWN]
                    || game.getGui().getKeyManager().keys[KeyEvent.VK_LEFT]
                    || game.getGui().getKeyManager().keys[KeyEvent.VK_RIGHT])
                play = true;
        if(play)
        {
            for(Enemy e: enemies)
            {
                if(e instanceof Hunter)
                {
                    Hunter r = (Hunter) e;
                    r.setTarget((int) (player.getX() + player.getWidth()/2), (int) (player.getY() + player.getHeight()/2));
                }
            }
            if(System.currentTimeMillis() - lastScoreIncrease > SCORE_INTERVAL)
            {
                score++;
                lastScoreIncrease = System.currentTimeMillis();
            }
            if(player.isActive())
            {
                player.update();
            }
            else
            {
                lastScore = score;
                maxScore = Math.max(score, maxScore);
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
            if(dodger.getAI() != null)
            {
                if(game.getGui().getKeyManager().keys[KeyEvent.VK_2])
                    showNetwork = 2;
                if(game.getGui().getKeyManager().keys[KeyEvent.VK_1])
                    showNetwork = 1;
                if(game.getGui().getKeyManager().keys[KeyEvent.VK_0])
                    showNetwork = 0;
            }
            if(game.getGui().getKeyManager().keys[KeyEvent.VK_P])
            {
                play = false;
            }
        }
        if(game.getGui().getKeyManager().keys[KeyEvent.VK_ESCAPE])
        {
            dodger.setAI(null);
            game.getStateMachine().change("menu");
        }
    }

    @Override
    public void render(Graphics g) 
    { 
        if(showNetwork == 2)
        {
            dodger.getAI().render(g, 0, 0, Dodger.WIDTH, Dodger.HEIGHT, player.getInputs(), Dodger.WIDTH);
        }
        if(showNetwork == 1)
            dodger.getAI().render(g, 0, 0, Dodger.WIDTH, Dodger.HEIGHT);
        
        g.setColor(Color.WHITE);
        String s = "MAXSCORE  = " + maxScore;
        g.drawString(s, 0, g.getFont().getSize());
        g.setColor(Color.WHITE);
        String s2 = "SCORE          = " + score;
        g.drawString(s2, 0, 2 * g.getFont().getSize());
        if(!play)
        {
            g.setColor(Color.WHITE);
            String s3 = "PRESS ARROW KEY TO START";
            g.drawString(s3, 0, 3*g.getFont().getSize());
        }
        else
        {
            g.setColor(Color.WHITE);
            String s3 = "LASTSCORE  = " + lastScore;
            g.drawString(s3, 0, 3*g.getFont().getSize());
        }
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
