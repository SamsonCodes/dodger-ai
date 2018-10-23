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
    private int score, maxScore;
    private Random random = new Random();
    private boolean play, showNetwork;
    private final long SCORE_INTERVAL = 1000;
    private long lastScore;
    
    public GameState(Dodger dodger, Game game)
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
        
            double eSize = 25;
        for(int i = 0; i < Dodger.ENEMY_NUMBER - 1; i++)
        {
            double x1 = (Dodger.WIDTH - eSize)*random.nextDouble(); 
            double y1 = (Dodger.HEIGHT - eSize)*random.nextDouble(); 
            double xSpeed = Math.signum(0.5 - random.nextDouble())*Dodger.ENEMY_SPEED*(1 + random.nextDouble());
            double ySpeed = Math.signum(0.5 - random.nextDouble())*Dodger.ENEMY_SPEED*(1 + random.nextDouble());
            Roamer r = new Roamer(game, Color.YELLOW, x1, y1, eSize, xSpeed, ySpeed, 1);
            enemies.add(r);
        }
        enemies.add(new Hunter(game, Color.RED, 0, 0, 25, 5, 1));
        double pWidth = 50;
        double pHeight = pWidth;
        showNetwork = false;
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
        lastScore = System.currentTimeMillis();
    }
    
    @Override
    public void onExit() 
    {
        System.out.println("Exiting BaseState");
    }

    @Override
    public void update() 
    {
        if(!play)
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
            if(System.currentTimeMillis() - lastScore > SCORE_INTERVAL)
            {
                score++;
                lastScore = System.currentTimeMillis();
            }
            if(player.isActive())
            {
                player.update();
            }
            else
            {
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
                if(game.getGui().getKeyManager().keys[KeyEvent.VK_9])
                    showNetwork = true;
                if(game.getGui().getKeyManager().keys[KeyEvent.VK_0])
                    showNetwork = false;
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
        if(showNetwork)
        {
            dodger.getAI().render(g, 0, 0, Dodger.WIDTH, Dodger.HEIGHT, player.getInputs(), Dodger.WIDTH);
        }
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
