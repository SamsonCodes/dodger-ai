package customgame.states;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */



import java.awt.Graphics;

public interface IState 
{
    public void onEnter();
    public void onExit();
    public void update();
    public void render(Graphics g);
}
