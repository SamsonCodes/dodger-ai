/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package customgame.input;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

public class MouseManager implements MouseListener, MouseMotionListener
{
    
    public int x, y;
    public boolean leftClick;
    
    @Override
    public void mouseClicked(MouseEvent me){}

    @Override
    public void mousePressed(MouseEvent me) 
    {
        //System.out.println("pressed");
        if(me.getButton() == 1)
            leftClick = true;
    }

    @Override
    public void mouseReleased(MouseEvent me) 
    {
        //System.out.println("released");
        if(me.getButton() == 1)
            leftClick = false;
    }

    @Override
    public void mouseEntered(MouseEvent me) {}

    @Override
    public void mouseExited(MouseEvent me){}

    @Override
    public void mouseDragged(MouseEvent me) {}

    @Override
    public void mouseMoved(MouseEvent me) 
    {
        //System.out.println("moved");
        x = me.getX();
        y = me.getY();
    }
}
