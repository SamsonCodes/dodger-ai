package customgame.states;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */



import java.awt.Graphics;
import java.util.HashMap;
import java.util.Stack;

public class StateMachine 
{
    private HashMap<String, IState> stateMap;
    private Stack<String> stateStack;
    
    public StateMachine()
    {
        stateMap = new HashMap();
        stateStack = new Stack();
    }
    
    public void add(String name, IState state)
    {
        if(!stateMap.containsKey(name) &! stateMap.containsValue(state))
        {
            stateMap.put(name, state);
        }
        else
        {
            if(stateMap.containsKey(name))
                System.out.println("StateMachine: add(" + name + ", IState): stateMap already contains key name!");
            else if(stateMap.containsValue(state))
                System.out.println("StateMachine: add(" + name + ", IState): stateMap already contains value state!");
        }
    }
    
    public void push(String name)
    {
        if(!stateStack.empty())
        {
            if(!stateStack.contains(name))
            {
                stateStack.push(name);
                stateMap.get(name).onEnter();
            }
        }
        else
        {
            stateStack.push(name);
            stateMap.get(name).onEnter();
        }
    }
    
    public void pop()
    {
        if(stateStack.size() > 1)
        {
            stateMap.get(stateStack.peek()).onExit();
            stateStack.pop();
            System.out.println("Resuming " + stateStack.peek());
        }
        else
        {
            System.out.println("StateMachine: pop(): not enough states on stack to pop!");
        }
    }
    
    public void change(String name)
    {
        if(!stateStack.empty())
        {
            String currentName = stateStack.peek();            
            if(!name.equals(currentName))
            {
                stateMap.get(currentName).onExit();
                stateStack.push(name);
                stateMap.get(name).onEnter();
            }
        }
        else
        {
            stateStack.push(name);
            stateMap.get(name).onEnter();
        }
    }
    
    public void update()
    {
        if(!stateStack.empty())
        {
            stateMap.get(stateStack.peek()).update();
        }
        else
        {
            System.out.println("StateMachine: update(): no states on stack!");
        }
    }
    
    public void render(Graphics g)
    {
        if(!stateStack.empty())
        {
            stateMap.get(stateStack.peek()).render(g);
        }
        else
        {
            System.out.println("StateMachine: render(Graphics g): no states on stack!");
        }
    }
    
}
