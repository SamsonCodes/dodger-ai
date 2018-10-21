/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package dodger;

import customgame.Game;
import customgame.customui.MessageBox;
import customgame.states.IState;
import java.awt.Color;
import java.awt.Graphics;

public class EvolveState implements IState
{
    private Dodger dodger;
    private Game game;
    private EvolveThread evoThread;
    private MessageBox msgBox;
    
    public EvolveState(Dodger dodger, Game game)
    {
        this.dodger = dodger;
        this.game = game;
    }

    @Override
    public void onEnter() 
    {
        System.out.println("Entering EvolveState");
        msgBox = new MessageBox(game.getGui(), Color.WHITE);
        msgBox.setActive(true);
        evoThread = new EvolveThread(dodger, game, this);
        evoThread.start();
    }

    @Override
    public void onExit() 
    {
        System.out.println("Exiting EvolveState");
    }

    @Override
    public void update() 
    {
        msgBox.update();
        if(evoThread.getCalculated())
        {
            System.out.println("EvoThread calculated!");
            game.getStateMachine().change("AI");
        }
    }

    @Override
    public void render(Graphics g) 
    {
        g.setColor(Color.WHITE);
        g.drawString("Evolve State", 0, g.getFont().getSize());
        msgBox.render(g);
    }
    
    public void setMessage(String msg)
    {
        msgBox.setText(msg);
    }
}
