/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package customgame.customui;

import customgame.Gui;
import java.awt.Graphics;

public class OptionPanel extends UIElement
{
    private Button[] buttons;
    private int options;
    private int[] choice;
    private int currentIndex;
    
    public OptionPanel(Gui gui, String[] options, int x, int y, int bWidth, int bHeight, int panelWidth, int choiceAmount)
    {
        super(gui);
        this.options = options.length;
        buttons = new Button[options.length];
        for(int i = 0; i < options.length; i++)
        {
            buttons[i] = new Button(gui, x+(i%panelWidth)*bWidth, y+(i/panelWidth)*bHeight, bWidth, bHeight, options[i]);
        }
        choice = new int[choiceAmount];
        for(int i = 0; i < choice.length; i++)
        {
            choice[i] = -1;
        }
        currentIndex = 0;
    }
    
    public int[] getChoices()
    {
        return choice;
    }
    
    public int getChoice(int i)
    {
        return choice[i];
    }

    @Override
    public void update() 
    {
        for(Button b: buttons)
            b.update();
        for(int i = 0; i < buttons.length; i++)
        {
            if(buttons[i].getPressed() && choice[currentIndex] == -1 && i < options)
            {
                choice[currentIndex] = i;
                buttons[i].setSelected(true);
                currentIndex++;
                if(currentIndex == choice.length)
                {
                    currentIndex = 0;
                    //System.out.println("OptionPanel: last input received.");
                }
//                buttons[choice[currentIndex]].setSelected(false);
//                choice[currentIndex] = -1;
            }
        }
    }

    @Override
    public void render(Graphics g) 
    {
        for(int i = 0; i < buttons.length; i++)
        {
            buttons[i].render(g);
        }
    }
    
}
