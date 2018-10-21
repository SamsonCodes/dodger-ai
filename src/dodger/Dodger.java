/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package dodger;

import customgame.Game;
import dodger.neuralnetwork.Network;

public class Dodger 
{
    private Game game;
    public final static String TITLE = "DODGER";
    public final static int WIDTH = 1100, HEIGHT = 800, OUTPUT_NR = 2, ENEMY_NUMBER = 5, ENEMY_SPEED = 5;
    private Network ai;
    
    public Dodger()
    {
        game = new Game(TITLE, WIDTH, HEIGHT);
        game.getStateMachine().add("play", new PlayState(game));
        game.getStateMachine().add("AI", new AIState(this, game));
        game.getStateMachine().add("menu", new MenuState(game));
        game.getStateMachine().add("evolve", new EvolveState(this, game));
        game.getStateMachine().change("menu");
        game.run();
    }

    public static void main(String[] args) 
    {
       Dodger dodger = new Dodger();
    }
    
    public void setAI(Network ai)
    {
        this.ai = ai;
    }

    public Network getAI()
    {
        return ai;
    }
}
