/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package dodger.states;

import customgame.Game;
import customgame.customui.OptionPanel;
import customgame.states.IState;
import dodger.Dodger;
import java.awt.Graphics;

public class MenuState implements IState
{
    private Game game;
    private OptionPanel panel;
    private final String[] OPTIONS = {"Play", "AI", "Exit"};
    private long startTime;
    private final long START_COOLDOWN = 500;
    
    public MenuState(Game game)
    {
        this.game = game;
    }

    @Override
    public void onEnter() 
    {
        System.out.println("Entering MenuState");
        int bWidth = 200;
        int bHeight = 50;
        panel = new OptionPanel(game.getGui(), OPTIONS, Dodger.WIDTH/2 - bWidth/2, Dodger.HEIGHT/2 - bHeight*OPTIONS.length/2, bWidth, bHeight, 1, 1);
        startTime = System.currentTimeMillis();
    }

    @Override
    public void onExit() 
    {
        System.out.println("Exiting MenuState");
    }

    @Override
    public void update() 
    {
        if(System.currentTimeMillis() - startTime > START_COOLDOWN)
        {
            panel.update();
            if(panel.getChoice(0)!= -1)
            {
                switch (panel.getChoice(0))
                {
                    case 0:                        
                        game.getStateMachine().change("game");
                        break;
                    case 1: 
                        game.getStateMachine().change("input");
                        break;
                    case 2:
                        System.exit(0);
                        break;
                }
            }
        }
    }

    @Override
    public void render(Graphics g) 
    {
        panel.render(g);
    }

}
