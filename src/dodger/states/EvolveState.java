/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package dodger.states;

import customgame.Game;
import customgame.customui.Button;
import customgame.customui.MessageBox;
import customgame.states.IState;
import dodger.Dodger;
import dodger.EvolveThread;
import java.awt.Color;
import java.awt.Graphics;

public class EvolveState implements IState
{
    private Dodger dodger;
    private Game game;
    private EvolveThread evoThread;
    private MessageBox msgBox;
    private Button interrupt;
    
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
        interrupt = new Button(game.getGui(), 0, 50, 100, 50, "interrupt");
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
            game.getStateMachine().change("game");
        }
        interrupt.update();
        if(interrupt.getPressed())
        {
            evoThread.interruptLoop();
        }
    }

    @Override
    public void render(Graphics g) 
    {
        g.setColor(Color.WHITE);
        g.drawString("Evolve State", 0, g.getFont().getSize());
        msgBox.render(g);
        interrupt.render(g);
    }
    
    public void setMessage(String msg)
    {
        msgBox.setText(msg);
    }
}
