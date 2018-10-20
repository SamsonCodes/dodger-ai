/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package customgame.graphics;

import java.awt.image.BufferedImage;

public class Animation 
{
    private long lastTime, timePerFrame;
    private BufferedImage[] frames;
    private int current;
    
    public Animation(BufferedImage[] frames, long timePerFrame)
    {
        this.frames = frames;
        this.timePerFrame = timePerFrame;
        lastTime = System.currentTimeMillis();
        current = 0;
    }
    
    public void update()
    {
        if(System.currentTimeMillis() - lastTime > timePerFrame)
        {
            lastTime = System.currentTimeMillis();
            current++;
            if(current == frames.length)
            {
                current = 0;
            }
        }
    }
    
    public BufferedImage getCurrentFrame()
    {
        return frames[current];
    }
    
    public void reset()
    {
        lastTime = System.currentTimeMillis();
        current = 0;
    }
}
