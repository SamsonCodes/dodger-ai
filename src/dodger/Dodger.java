/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package dodger;

import customgame.Game;
import dodger.entities.Enemy;
import dodger.entities.Player;
import dodger.neuralnetwork.Network;
import dodger.neuralnetwork.SaveManager;
import dodger.neuralnetwork.XMLReader;
import java.awt.Color;
import java.util.ArrayList;
import java.util.Random;


public class Dodger 
{
    private Game game;
    public final static String TITLE = "DODGER";
    public final static int WIDTH = 1100, HEIGHT = 800, OUTPUT_NR = 2, ENEMY_NUMBER = 5, ENEMY_SPEED = 5;
    private Network ai, bestNetwork;
    private Network[] currentGen;
    private int[] currentGenScore;
    private int topScore, generation;
    private Random random = new Random();
    
    public Dodger()
    {
        game = new Game(TITLE, WIDTH, HEIGHT);
        game.getStateMachine().add("base", new BaseState(this, game));
        game.getStateMachine().add("menu", new MenuState(game));
        game.getStateMachine().change("menu");
        crossGen(new int[]{12, 6, 3}, 50, 0, "5E");
        game.run();
    }
    
    private void crossGen(int[] hiddenNeurons, int popSize, int generations, String subTag)
    {
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
                currentGen[n] = new Network((ENEMY_NUMBER * 2 + 4), hiddenNeurons, OUTPUT_NR, false, false);
            }
        }
        for(int n = 0; n < popSize; n++)
        {
            currentGenScore[n] = 0;
        }
        System.out.print("Gen: " + generation + ", ");
        //fitness function, evaluation
        testCurrentGen();
        sortCurrentGen();
        bestNetwork = currentGen[0];
        topScore = currentGenScore[0];
        int bestGen = generation;
        System.out.println("Topscore = " + currentGenScore[0]);
        
        System.out.print("Gen: ");
        String s = "";
        s+="Generation" + generation + "\n";
        s+="Network"+0 + " cost = " +currentGenScore[0] + "\n";
        s+="Network"+(popSize/2-1) + " cost = " + currentGenScore[popSize/2 - 1] + "\n";
        s+="Network"+(popSize-2) + " cost = " + currentGenScore[popSize - 2] + "\n";
        //for generations
        for(int g = 1; g <= generations; g++)
        {
            generation++;
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
            
            s+="Generation" + generation + "\n";
            s+="Network"+0 + " cost = " +currentGenScore[0] + "\n";
            s+="Network"+(popSize/2-1) + " cost = " + currentGenScore[popSize/2 - 1] + "\n";
            s+="Network"+(popSize-2) + " cost = " + currentGenScore[popSize - 2] + "\n \n";
            System.out.print(generation + ", ");
            if(currentGenScore[0] > topScore)
            {
                
                System.out.println("New topscore = " + currentGenScore[0]);
                if(g < generations)
                    System.out.print("Gen: ");
                topScore = currentGenScore[0];
                bestNetwork = currentGen[0];
                bestGen = (generation);
            }
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
        saveProgress(tag);
        System.out.println("CalculationTime = " + (System.currentTimeMillis() - startTime)/1000 + "s");
    }
    
    private void testCurrentGen()
    {
        for(int n = 0; n < currentGen.length - 1; n++)
        {
            //to check that gens are unique
//            System.out.println("CurrentGen[" + n + "]");
//            for(int x = 0; x < 1; x++)
//            {
//                System.out.println("hiddenBiases[" + x + "] = " + currentGen[n].hiddenLayer[x].bias); 
//            }
//            System.out.println("");
            
            ArrayList<Enemy> testEnemies = new ArrayList(); 

                double eWidth = 25;
                double eHeight = eWidth;
                for(int i = 0; i < ENEMY_NUMBER; i++)
                {
                    double x1 = (Dodger.WIDTH - eWidth)*random.nextDouble(); 
                    double y1 = (Dodger.HEIGHT - eHeight)*random.nextDouble(); 
                    double xSpeed = Math.signum(0.5 - random.nextDouble())*ENEMY_SPEED*(1 + random.nextDouble());
                    double ySpeed = Math.signum(0.5 - random.nextDouble())*ENEMY_SPEED*(1 + random.nextDouble());
                    Enemy e1 = new Enemy(game, Color.RED, x1, y1, eWidth, eHeight, xSpeed, ySpeed, 1);
                    testEnemies.add(e1);
                }

            double pWidth = 50;
            double pHeight = pWidth;
            //int[] inputIndices = new int[testEnemies.size() + 4];
//            for(int q = 0; q < inputIndices.length; q++)
//            {
//                inputIndices[q] = q;
//            }
            Player testPlayer = new Player(game, Dodger.WIDTH/2 - pWidth/2, Dodger.HEIGHT/2 - pHeight/2, pWidth, pHeight, currentGen[n], testEnemies);

            int cycles = 0;
            int score = 0;
            int cycleLimit = 100000;

            boolean alive = true;
            while(alive && cycles < cycleLimit)
            { 
                if(testPlayer.isActive())
                {
                    testPlayer.update();
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
                if(cycles % 100 == 0)
                {
                    score += 136 - (testPlayer.distanceToMid())/10;
                }
            }
            currentGenScore[n] = cycles;
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
    
    public Network getAI()
    {
        return ai;
    }

    public static void main(String[] args) 
    {
       Dodger dodger = new Dodger();
    }

}
