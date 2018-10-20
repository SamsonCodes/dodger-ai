/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package customgame.entities;

import java.awt.Graphics;
import java.util.ArrayList;
import java.util.Iterator;

public class EntityManager 
{
    private ArrayList<Entity> entities, garbageCan;
//    private Comparator<Entity> renderSorter = new Comparator<Entity>() {
//
//        @Override
//        public int compare(Entity a, Entity b) 
//        {
//            if(a.getY() < b.getY())
//                return -1;
//            return 1;
//        }
//    };
    
    public EntityManager()
    {
        entities = new ArrayList<>();
        garbageCan = new ArrayList<>();
    }
    
    public void update()
    {
        Iterator<Entity> itr = entities.iterator();
        while(itr.hasNext())
        {
            Entity e = itr.next();
            if(!e.isActive())
            {
                if(!garbageCan.contains(e))
                    garbageCan.add(e);
                System.out.println("EntityManager: " + e.getName() + " was removed.");
                itr.remove();
            }
            else
                e.update();
        }
        //Collections.sort(entities, renderSorter);
    }
    
    public void render(Graphics g, int xOfset, int yOfset)
    {
        for(Entity e: entities)
        {
            e.render(g, xOfset, yOfset);
        }
    }
    
    public ArrayList<Entity> getEntities()
    {
        return entities;
    }
    
    public ArrayList<Entity> getGarbageCan()
    {
        return garbageCan;
    }
    
    public void addEntity(Entity e)
    {
        entities.add(e);
    }
    
    public void empty()
    {
        entities.clear();
    }
}
