/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package dodger;

import customgame.Game;
import customgame.sound.WavPlayer;
import dodger.entities.Enemy;
import dodger.entities.Hunter;
import dodger.entities.Player;
import dodger.entities.Roamer;
import dodger.neuralnetwork.Network;
import dodger.neuralnetwork.SaveManager;
import dodger.neuralnetwork.XMLReader;
import java.awt.Color;
import java.util.ArrayList;
import java.util.Random;

public class EvolveThread extends Thread
{    
    private Dodger dodger;    
    private Game game;
    private EvolveState evoState;
    private Network ai, bestNetwork;
    private Network[] currentGen;
    private int[] currentGenScore;
    private int topScore, generation;
    private Random random = new Random();  
    private boolean calculated;
    private boolean interrupt;
    
    public EvolveThread(Dodger dodger, Game game, EvolveState evoState)
    {
        this.dodger = dodger;
        this.game = game;
        this.evoState = evoState;
    }  
    
    @Override
    public void start(){
        super.start();
        calculated = false;
        interrupt = false;
        System.out.println("EvolveThread running");
        evoState.setMessage("Starting up");
    }
    
    @Override
    public void run()
    {
        WavPlayer wp = new WavPlayer();
        wp.playSound(Dodger.PATH + "\\sounds\\chime_up.wav");
        crossGen(new int[]{14,7}, 100, 1000, "_23-10-18-v5_");  
        calculated = true;
    }
    
    private void crossGen(int[] hiddenNeurons, int popSize, int generations, String subTag)
    {
        System.out.println("Crossgen initiated.");
        String tag = "crossGen" + subTag;
        for(int i = 0; i < hiddenNeurons.length; i++)
        {
            tag += "l" + hiddenNeurons[i];
        }
        tag += "p" + popSize;
        long startTime = System.currentTimeMillis();
        //initial population
        generation = getGeneration(tag);
        currentGen = new Network[popSize];
        currentGenScore = new int[popSize];
        if(!loadLastGen(tag))
        {
            for(int n = 0; n < popSize; n++)
            {
                currentGen[n] = new Network((Dodger.ENEMY_NUMBER * 2 + 4), hiddenNeurons, Dodger.OUTPUT_NR, false, false);
            }
        }
        for(int n = 0; n < popSize; n++)
        {
            currentGenScore[n] = 0;
        }
        System.out.print("Gen: " + generation + ", ");     
        evoState.setMessage("Generation" + generation + ": 0/" + generations + ", topscore = " + topScore);
        //fitness function, evaluation
        testCurrentGen();
        sortCurrentGen();
        bestNetwork = currentGen[0];
        topScore = currentGenScore[0];
        int bestGen = generation;
        System.out.println("Topscore = " + currentGenScore[0]);              
        evoState.setMessage("Generation" + generation + ": 0/" + generations + ", topscore = " + topScore);
        System.out.print("Gen: ");
        String s = "";
        s+="Generation" + generation + "\n";
        s+="Network"+0 + " cost = " +currentGenScore[0] + "\n";
        s+="Network"+(popSize/2-1) + " cost = " + currentGenScore[popSize/2 - 1] + "\n";
        s+="Network"+(popSize-2) + " cost = " + currentGenScore[popSize - 2] + "\n";
        //for generations
        int g = 1;
        while(g <= generations &! interrupt)
        {
            
            //selection + mating + mutation
            Network[] newGen = new Network[currentGen.length];
            for(int i = 0; i < newGen.length; i++)
            {
                Network[] parents = selectParents();
                newGen[i] = new Network(parents[0], parents[1], (float) 0.05);
            }
            //mutation
//            double mutationFactor = 0.1;
//            double mutationChance = 0.1;
//            for(int i = 0; i < newGen.length; i++)
//            {
//                if(random.nextDouble() < mutationChance)
//                    newGen[i] = new Network(newGen[i], mutationFactor);
//            }
            for(int i = 0; i < newGen.length; i++)
            {
                currentGen[i] = newGen[i];
            }
            
            
            //evaluation
            testCurrentGen();
            sortCurrentGen();
            
            generation++; 
            System.out.print(generation + ", ");
            evoState.setMessage("Generation" + generation + ": " + g + "/" + generations + ", topscore = " + topScore);
            
            s+="Generation" + generation + "\n";
            s+="Network"+0 + " cost = " +currentGenScore[0] + "\n";
            s+="Network"+(popSize/2-1) + " cost = " + currentGenScore[popSize/2 - 1] + "\n";
            s+="Network"+(popSize-2) + " cost = " + currentGenScore[popSize - 2] + "\n \n";
            

            if(currentGenScore[0] > topScore)
            {
                
                System.out.println("New topscore = " + currentGenScore[0]);
                if(g < generations)
                    System.out.print("Gen: ");
                topScore = currentGenScore[0];
                bestNetwork = currentGen[0];
                bestGen = (generation);
            }
            g++;
        }
        System.out.println();
        System.out.println(s);
        System.out.println("Final Score = " + currentGenScore[0]);
        System.out.println("Best Score = " + topScore + " from generation" + bestGen);
        if(lastRecord(tag) > topScore)
        {
            bestNetwork = loadBestNetwork(tag);
            topScore = lastRecord(tag);
            System.out.println("Best network retrieved from save file! Record = " + topScore);
        }
        ai = bestNetwork;
        setAI();
        saveProgress(tag);
        System.out.println("CalculationTime = " + (System.currentTimeMillis() - startTime)/1000 + "s");
        WavPlayer wp = new WavPlayer();
        wp.playSound(Dodger.PATH + "\\sounds\\chime_up.wav");
    }
    
    private void testCurrentGen()
    {
        for(int n = 0; n < currentGen.length - 1; n++)
        {
            
            int score = 0;
            int cycleLimit = 100000;
            int testAmount = 1;
            for(int t = 0; t < testAmount; t++)
            {
                ArrayList<Enemy> testEnemies = new ArrayList(); 

                double rSize = 25;        
                double xSpeed = 0.71 * Dodger.ENEMY_SPEED;
                double ySpeed = xSpeed;
                int rDamage = 3;

                double x1 = (Dodger.WIDTH - rSize)/4; 
                double y1 = (Dodger.HEIGHT - rSize)/4; 
                Roamer r1 = new Roamer(game, Color.YELLOW, x1, y1, rSize, -xSpeed, -ySpeed, rDamage);
                testEnemies.add(r1);
                double x2 = (Dodger.WIDTH - rSize)/4; 
                double y2 = 3*(Dodger.HEIGHT - rSize)/4; 
                Roamer r2 = new Roamer(game, Color.YELLOW, x2, y2, rSize, -xSpeed, ySpeed, rDamage);
                testEnemies.add(r2);
                double x3 = 3*(Dodger.WIDTH - rSize)/4; 
                double y3 = (Dodger.HEIGHT - rSize)/4; 
                Roamer r3 = new Roamer(game, Color.YELLOW, x3, y3, rSize, xSpeed, -ySpeed, rDamage);
                testEnemies.add(r3);
                double x4 = 3*(Dodger.WIDTH - rSize)/4; 
                double y4 = 3*(Dodger.HEIGHT - rSize)/4; 
                Roamer r4 = new Roamer(game, Color.YELLOW, x4, y4, rSize, xSpeed, ySpeed, rDamage);
                testEnemies.add(r4);            
                testEnemies.add(new Hunter(game, Color.RED, 0, 0, 25, Dodger.ENEMY_SPEED/4, 1));
        
                double pWidth = 50;
                double pHeight = pWidth;
                Player testPlayer = new Player(game, Dodger.WIDTH/2 - pWidth/2, Dodger.HEIGHT/2 - pHeight/2, pWidth, pHeight, currentGen[n], testEnemies);

                boolean alive = true;
                int cycles = 0;
                while(alive && cycles < cycleLimit)
                { 
                    if(testPlayer.isActive())
                    {
                        testPlayer.update();
                        for(Enemy e: testEnemies)
                        {
                            if(e instanceof Hunter)
                            {
                                Hunter r = (Hunter) e;
                                r.setTarget((int) (testPlayer.getX() + testPlayer.getWidth()/2), (int) (testPlayer.getY() + testPlayer.getHeight()/2));
                            }
                        }
                    }
                    else
                    {
                        alive = false;
                    }
                    for(Enemy e: testEnemies)
                    {
                        e.update();
                    }
                    if(!testPlayer.getEnemyCollisions(testEnemies).isEmpty())
                    {
                        for(Enemy e: testPlayer.getEnemyCollisions(testEnemies))
                        {
                            testPlayer.damage(e.getDamage());
                        }
                    }
                    cycles++;
                }
                score += (cycles /60);
            }
            currentGenScore[n] = score/testAmount;
        }
    }
    
    private void sortCurrentGen()
    {
        for(int n = 0; n < (currentGen.length - 1); n++)
        {
            int j = n;
            boolean nextIndex = false;
            while(j >= 0 &! nextIndex)
            { 
                if(currentGenScore[j+1] > currentGenScore[j])
                {
                    Network holder = currentGen[j + 1];
                    currentGen[j] = currentGen[j+1];
                    currentGen[j + 1] = holder;
                    int cycleHolder = currentGenScore[j + 1];
                    currentGenScore[j] = currentGenScore[j+1];
                    currentGenScore[j + 1] = cycleHolder;
                    j--;
                }
                else
                {
                    nextIndex = true;
                }
            }
        }
    }
    
    // returns two parents pseudorandomly chosen from the currentGeneration with more chance for networks with higher scores to be chosen
    private Network[] selectParents()
    {
        int sum = 0;
        for(int i = 0; i < currentGen.length; i++)
        {
            sum+=currentGenScore[i];
        }
        Network[] selection = new Network[2];
        for (int j = 0; j < selection.length; j++)
        {
            int randomNumber = random.nextInt(sum);
            int partialSum = 0;
            int i = 0;
            while(partialSum < randomNumber)
            {                
                partialSum+=currentGenScore[i];
                i++;
            }
            if(i > 0)
                i--;
            selection[j] = currentGen[i];
        }
        return selection;
    }
    
    private void saveProgress(String tag)
    {
        SaveManager saveManager = new SaveManager();
        if(currentGen != null)
        {
            ArrayList<String> data = new ArrayList();
            for(int n = 0; n < currentGen.length; n++)
                data.add(currentGen[n].createSaveData());
            
            data.add("<generation>" + generation + "</generation>");
            saveManager.saveData(data, tag + "evolution");
        }
        if(bestNetwork != null)
        {
            ArrayList<String> data2 = new ArrayList();
            data2.add(bestNetwork.createSaveData());
            data2.add("<score>" + topScore + "</score>");
            saveManager.saveData(data2, tag + "bestNetwork");
        }
    }
    
    private boolean loadLastGen(String tag)
    {
        SaveManager saveManager = new SaveManager();
        ArrayList<String> data = saveManager.loadData(tag + "evolution");
        if(data != null)
        {
            System.out.println("Loading data from: " + tag + "evolution.txt");
            for(int n = 0; n < currentGen.length; n++)
               currentGen[n] = new Network(data.get(n));
            return true;
        }
        else
            return false;
    }   
    
    private int getGeneration(String tag)
    {
        SaveManager saveManager = new SaveManager();
        ArrayList<String> data = saveManager.loadData(tag + "evolution");
        if(data != null)
        {
            String gen = XMLReader.getElement(data.get(data.size() - 1), "generation");
            if(!gen.isEmpty())
                return Integer.parseInt(gen);
            else
                return 0;
        }
        else return 0;
    }
    
    private Network loadBestNetwork(String tag)
    {
        Network network = null;
        SaveManager saveManager = new SaveManager();
        ArrayList<String> data = saveManager.loadData(tag + "bestNetwork");
        if(data != null)
            network = new Network(data.get(0));
        return network;
    }
    
    private int lastRecord(String tag)
    {
        int lastRecord = 0;
        SaveManager saveManager = new SaveManager();
        ArrayList<String> data = saveManager.loadData(tag + "bestNetwork");
        if(data != null)
            lastRecord = Integer.valueOf(XMLReader.getElement(data.get(1), "score"));
        return lastRecord;
    }
    
    public boolean getCalculated()
    {
        return calculated;
    }
    
    public void setAI()
    {
        dodger.setAI(ai);
    }
    
    public void interruptLoop()
    {
        interrupt = true;
    }

}
