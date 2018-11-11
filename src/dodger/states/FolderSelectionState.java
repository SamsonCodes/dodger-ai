/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dodger.states;

import customgame.Game;
import customgame.Gui;
import customgame.customui.Button;
import customgame.customui.Label;
import customgame.customui.TextInputBox;
import customgame.states.IState;
import dodger.Dodger;
import java.awt.Graphics;
import java.io.File;

public class FolderSelectionState implements IState
{

    private Dodger dodger;
    private Game game;
    private Gui gui;
    private TextInputBox tib;
    private Label label;
    private Button button;

    public FolderSelectionState(Dodger dodger, Game game)
    {
        this.dodger = dodger;
        this.game = game;
        this.gui = game.getGui();
    }

    @Override
    public void onEnter()
    {
        System.out.println("Entering FolderSelectionState");
        int labelX = 10;
        int labelY = 10;
        int labelWidth = 200;
        int labelHeight = 50;
        label = new Label(gui, "Enter save folder: ", labelX, labelY, labelWidth, labelHeight);
        int spacing = 10;
        int tibWidth = 500;
        int tibHeight = labelHeight;
        tib = new TextInputBox(gui, labelX + labelWidth + spacing, labelY, tibWidth, tibHeight, dodger.getPath());
        int buttonWidth = labelWidth;
        int buttonHeight = labelHeight;
        button = new Button(gui, labelX, labelY + labelHeight + spacing, buttonWidth, buttonHeight, "Start");
    }

    @Override
    public void onExit()
    {
        System.out.println("Exiting FolderSelectionState");
    }

    @Override
    public void update()
    {
        label.update();
        tib.update();
        button.update();
        if (button.getPressed())
        {
            if (tib.getInput() != null)
            {
                String dir = tib.getInput();
                if (createDirectory(dir))
                {
                    dodger.setPath(dir);
                    game.getStateMachine().change("menu");
                }
                else
                {
                    button.reset();
                }
            }
            else
            {
                button.reset();
            }
        }
    }

    private boolean createDirectory(String dir)
    {
        File file = new File(dir);
        if (!file.exists())
        {
            if (file.mkdir())
            {
                System.out.println("Directory is created!");
                return true;
            }
            else
            {
                System.out.println("Failed to create directory!");
                return false;
            }
        }
        else
        {
            System.out.println("Directory already exists!");
            return true;
        }
    }

    @Override
    public void render(Graphics g)
    {
        label.render(g);
        tib.render(g);
        button.render(g);
    }

}
