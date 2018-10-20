package customgame;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import customgame.states.StateMachine;
import customgame.states.TestState;
import java.awt.Graphics;
import java.awt.image.BufferStrategy;

public class Game implements Runnable
{
    private Gui gui;
    private StateMachine stateMachine;
    
    public Game(String title, int canvasWidth, int canvasHeight)
    {
        gui = new Gui(title, canvasWidth, canvasHeight);
        stateMachine = new StateMachine();
        stateMachine.add("testState", new TestState());
        stateMachine.push("testState");
    }
    
    public void run()
    {
        long fps = 60;
        long timePerFrame = 1000/fps;
        long nextTime = System.currentTimeMillis();
        int loops;
        int maxFrameSkip = 10;
        gui.run();
        
        while(true)
        {
            loops = 0;
            while(System.currentTimeMillis() > nextTime && loops < maxFrameSkip)
            {
                update();
                loops++;
                nextTime += timePerFrame;
            }
            render();
        }
    }
    
    private void update()
    {
        stateMachine.update();
    }
    
    private void render()
    {
        BufferStrategy bs = gui.getCanvas().getBufferStrategy();
        if(bs == null)
        {
            gui.getCanvas().createBufferStrategy(3);
            return;
        }
        Graphics g = bs.getDrawGraphics();        
        g.clearRect(0, 0, gui.getWidth(), gui.getHeight());
        
        stateMachine.render(g);
        
        bs.show();
        g.dispose();
    }
    
    public Gui getGui()
    {
        return gui;
    }
    
    public StateMachine getStateMachine()
    {
        return stateMachine;
    }
}
