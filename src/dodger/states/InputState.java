/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dodger.states;

import customgame.Game;
import customgame.Gui;
import customgame.customui.Button;
import customgame.customui.InputBox;
import customgame.customui.Label;
import customgame.states.IState;
import dodger.Dodger;
import java.awt.Graphics;

public class InputState implements IState
{

    private Button button;
    private InputBox[] hLayerBox;
    private Label[] hLabels;
    private InputBox popBox, genBox;
    private Label popLabel, genLabel;
    private Dodger dodger;
    private Game game;
    private Gui gui;
    int[] hLayer = new int[]{14, 7, 0};
    int population, generations;

    public InputState(Dodger dodger, Game game)
    {
        this.dodger = dodger;
        this.game = game;
        this.gui = game.getGui();
    }

    @Override
    public void onEnter()
    {
        System.out.println("Entering Input State");

        int x0 = 10;
        int y0 = 10;
        int bWidth = 100;
        int bHeight = 50;
        int spacing = 10;
        hLayerBox = new InputBox[hLayer.length];
        hLabels = new Label[hLayerBox.length];
        for (int i = 0; i < hLayerBox.length; i++)
        {
            hLayerBox[i] = new InputBox(gui, x0 + bWidth + spacing, y0 + i * (bHeight + spacing), bWidth, bHeight, hLayer[i]);
            hLabels[i] = new Label(gui, "Layer" + (i + 1) + ": ", x0, y0 + i * (bHeight + spacing), bWidth, bHeight);
        }
        popLabel = new Label(gui, "Population Size: ", x0 + 2 * (bWidth + spacing), y0, bWidth, bHeight);        
        population = 100;
        popBox = new InputBox(gui, x0 + 3 * (bWidth + spacing), y0, bWidth, bHeight, population);
        
        genLabel = new Label(gui, "Generations: ", x0 + 2 * (bWidth + spacing), y0 + bHeight + spacing, bWidth, bHeight);
        generations = 100;
        genBox = new InputBox(gui, x0 + 3 * (bWidth + spacing), y0 + bHeight + spacing, bWidth, bHeight, generations);        

        button = new Button(gui, x0, y0 + hLayerBox.length * (bHeight + spacing), bWidth, bHeight, "Start");
    }

    @Override
    public void onExit()
    {
        System.out.println("Exiting Input State");
    }

    @Override
    public void update()
    {
        for (InputBox h : hLayerBox)
        {
            h.update();
        }
        popBox.update();
        genBox.update();
        button.update();
        for (int i = 0; i < hLayerBox.length; i++)
        {
            if (hLayerBox[i].getInput() != -1)
            {
                hLayer[i] = hLayerBox[i].getInput();
            }
        }
        if (popBox.getInput() != -1)
        {
            population = popBox.getInput();
        }
        if (genBox.getInput() != -1)
        {
            generations = genBox.getInput();
        }
        if (button.getPressed())
        {
            if (hLayerFull() && population > 0 && generations > 0)
            {
                dodger.setHiddenLayers(trimArray(hLayer));
                dodger.setPopulation(population);
                dodger.setGenerations(generations);

                game.getStateMachine().change("evolve");
            }
        }
    }

    @Override
    public void render(Graphics g)
    {
        for (InputBox h : hLayerBox)
        {
            h.render(g);
        }
        for(Label l: hLabels)
        {
            l.render(g);
        }
        popBox.render(g);
        popLabel.render(g);
        genBox.render(g);
        genLabel.render(g);
        button.render(g);
    }
    
    private int[] trimArray(int[] array)
    {
        int size = array.length;
        for(int i = 0; i < array.length; i++)
        {
            if(array[i] == 0)
            {
                size--;
            }
        }
        int[] trimmed = new int[size];
        int ti = 0;
        for(int i = 0; i < array.length; i++)
        {
            if(array[i] != 0)
            {
                trimmed[ti] = array[i];
                ti++;
            }
        }
        return trimmed;
    }

    private boolean hLayerFull()
    {
        boolean full = true;
        for (int h : hLayer)
        {
            if (h == -1)
            {
                full = false;
            }
        }
        return full;
    }   

}
