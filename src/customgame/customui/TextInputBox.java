/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package customgame.customui;

import com.sun.glass.events.KeyEvent;
import customgame.Gui;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;

public class TextInputBox extends UIElement
{

    private long lastInput, inputCooldown;
    private String input;
    private boolean selected;

    public TextInputBox(Gui gui, int x, int y, int width, int height)
    {
        super(gui);
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        input = "";
        inputCooldown = 250;
        lastInput = System.currentTimeMillis();
        selected = false;
    }

    public TextInputBox(Gui gui, int x, int y, int width, int height, String input)
    {
        super(gui);
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.input = input;
        inputCooldown = 100;
        lastInput = System.currentTimeMillis();
        selected = false;
    }

    @Override
    public void update()
    {
        if (System.currentTimeMillis() - lastInput > inputCooldown)
        {
            if (gui.getMouseManager().leftClick)
            {
                lastInput = System.currentTimeMillis();
                int pointerX = gui.getMouseManager().x;
                int pointerY = gui.getMouseManager().y;
                int range = 1;
                Rectangle pointer = new Rectangle(pointerX - range / 2, pointerY - range / 2, range, range);
                Rectangle boxBounds = new Rectangle(x, y, width, height);
                if (pointer.intersects(boxBounds))
                {
                    if (!selected)
                    {
                        selected = true;
                    }
                    else
                    {
                        selected = false;
                    }
                }
                else
                {
                    selected = false;
                }
            }
            if (selected)
            {
                if (System.currentTimeMillis() - lastInput > inputCooldown)
                {
                    if (gui.getKeyManager().keys[KeyEvent.VK_BACKSPACE])
                    {
                        lastInput = System.currentTimeMillis();
                        input = removeLastChar(input);
                    }
                }
                char keyTyped = gui.getKeyManager().getChar();
                if (keyTyped != '\u0000')
                {
                    input += keyTyped;
                }
            }
        }

    }

    public String removeLastChar(String str)
    {
        if (str != null && str.length() > 0)
        {
            str = str.substring(0, str.length() - 1);
        }
        return str;
    }

    public String getInput()
    {
        if (!input.isEmpty())
        {
            return input;
        }
        else
        {
            return null;
        }
    }

    public void reset()
    {
        selected = true;
        input = "";
        lastInput = System.currentTimeMillis();
    }

    @Override
    public void render(Graphics g)
    {
        if (!selected)
        {
            g.setColor(Color.GRAY);
        }
        else
        {
            g.setColor(Color.WHITE);
        }
        g.fillRect(x, y, width, height);
        g.setColor(Color.BLACK);
        g.drawString(input, x, y + height / 2 + g.getFont().getSize());
    }

}
