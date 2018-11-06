/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package dodger;

import dodger.states.GameState;
import dodger.states.EvolveState;
import dodger.states.MenuState;
import customgame.Game;
import dodger.neuralnetwork.Network;
import dodger.states.InputState;

public class Dodger 
{
    private Game game;
    public final static String TITLE = "DODGER", PATH = "C:\\Users\\Samson\\Documents\\NetBeansProjects\\Dodger\\src";
    public final static int WIDTH = 1100, HEIGHT = 800, OUTPUT_NR = 2, ENEMY_NUMBER = 5, ENEMY_SPEED = 20, PLAYER_SPEED = 10;
    private Network ai;
    private int[] hiddenLayers;
    private int population, generations;
    
    public Dodger()
    {
        game = new Game(TITLE, WIDTH, HEIGHT);
        game.getStateMachine().add("game", new GameState(this, game));
        game.getStateMachine().add("menu", new MenuState(game));
        game.getStateMachine().add("evolve", new EvolveState(this, game));
        game.getStateMachine().add("input", new InputState(this, game));
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

    public int[] getHiddenLayers()
    {
        return hiddenLayers;
    }

    public void setHiddenLayers(int[] hiddenLayers)
    {
        this.hiddenLayers = hiddenLayers;
    }

    public int getPopulation()
    {
        return population;
    }

    public void setPopulation(int population)
    {
        this.population = population;
    }

    public int getGenerations()
    {
        return generations;
    }

    public void setGenerations(int generations)
    {
        this.generations = generations;
    }
}
