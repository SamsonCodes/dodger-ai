package customgame.states;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import java.awt.Color;
import java.awt.Graphics;

public class TestState implements IState
{
    private long enterTime, currentTime;
    
    public TestState()
    {
    }

    @Override
    public void onEnter() 
    {
        System.out.println("Entering Test State"); 
        enterTime = System.currentTimeMillis();
    }

    @Override
    public void onExit() 
    {
        System.out.println("Exiting Test State");
    }

    @Override
    public void update() 
    {
        currentTime = System.currentTimeMillis() - enterTime;
    }

    @Override
    public void render(Graphics g) 
    {
        g.setColor(Color.WHITE);
        g.drawString("TEST STATE, time = " + String.valueOf(currentTime), 0, g.getFont().getSize());
    }
}
