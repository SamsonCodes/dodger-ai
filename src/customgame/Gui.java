package customgame;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */



import customgame.input.KeyManager;
import customgame.input.MouseManager;
import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class Gui implements Runnable
{
    private JFrame frame;
    private JPanel panel;
    private Canvas canvas;
    private int width, height;
    private KeyManager keyManager;
    private MouseManager mouseManager;
    
    public Gui(String title, int width, int height)
    {
        this.width = width;
        this.height = height;
        frame = new JFrame(title);
        canvas = new Canvas();
        canvas.setPreferredSize(new Dimension(width, height));
        canvas.setFocusable(false);
        canvas.setBackground(Color.BLACK);
        panel = new JPanel();
        panel.add(canvas);
        frame.add(panel);
        frame.setResizable(false);
        frame.setSize(new Dimension(width + 50, height + 50));
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        keyManager = new KeyManager();
        mouseManager = new MouseManager();
        frame.addKeyListener(keyManager);
        frame.addMouseListener(mouseManager);
        canvas.addMouseListener(mouseManager);
        canvas.addMouseMotionListener(mouseManager);
        
    }
    
    @Override
    public void run() 
    {
        frame.setVisible(true);
    }
    
    public Canvas getCanvas()
    {
        return canvas;
    }
    
    public JFrame getFrame()
    {
        return frame;
    }
    
    public int getWidth()
    {
        return width;
    }
    
    public int getHeight()
    {
        return height;
    }

    public KeyManager getKeyManager() {
        return keyManager;
    }
    
    public MouseManager getMouseManager() {
        return mouseManager;
    }

}

