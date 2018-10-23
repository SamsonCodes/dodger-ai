/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package dodger.neuralnetwork;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Random;

public class Network 
{
    private int inputNr;
    private ArrayList<Neuron[]> hiddenLayers;
    private Neuron[] outputLayer;
    private Random random = new Random();
    private DecimalFormat DF = new DecimalFormat("#.##");
    
    //Create a new network with random weights and biases
    public Network(int inputNr, int[] hiddenNeurons, int outputNeurons, boolean hiddenPerceptrons, boolean outputPerceptrons)
    {
        float maxWeight = 10;
        float maxBias = 10;
        this.inputNr = inputNr;
        hiddenLayers = new ArrayList();
        
        //create hiddenNeurons.length hiddenLayers with hiddenNeurons[i] neurons each.
        for(int i = 0; i < hiddenNeurons.length; i++)
        {
            hiddenLayers.add(new Neuron[hiddenNeurons[i]]);
            for(int j = 0; j < hiddenLayers.get(i).length; j++)
            {
                float[] weights = new float[inputNr];
                if(i > 0)
                    weights = new float[hiddenLayers.get(i-1).length];
                for(int w = 0; w < weights.length; w++)
                {
                    weights[w] = (float) ((0.5 - random.nextFloat()) * 2 * maxWeight);
                }
                float bias = -random.nextFloat()*maxBias;
                hiddenLayers.get(i)[j] = new Neuron(weights, bias, hiddenPerceptrons);
            }
            if(i > 0)
            {
                //connect each layers neurons to those of the previous one.
                for(Neuron h2 : hiddenLayers.get(i))
                {
                    for(Neuron h1: hiddenLayers.get(i - 1))
                    {
                        h2.connectInput(h1);
                    }
                }
            }
        }
        
        //Create outputNeurons output neurons. 
        outputLayer = new Neuron[outputNeurons];
        for(int i = 0; i < outputLayer.length; i++)
        {
            float[] outputWeigths = new float[hiddenNeurons[hiddenNeurons.length - 1]];
            for(int w = 0; w < outputWeigths.length; w++)
            {
                outputWeigths[w] = (float) ((0.5 -random.nextFloat()) * 2 * maxWeight);
            }
            float outputBias = -random.nextFloat()*maxBias;
            outputLayer[i] = new Neuron(outputWeigths,outputBias, outputPerceptrons);
        }
        //connect the outputLayer neurons to the neurons of the last hidden layer.
        for(Neuron o : outputLayer)
        {
            for(Neuron h: hiddenLayers.get(hiddenLayers.size() - 1))
                o.connectInput(h);
        }
        //System.out.println(createSaveData());
    }
    
    //Create a new network from another one with mutated values for the weights and biases.
    public Network(Network network, float mutation)
    {
        inputNr = network.getInputNr();
        hiddenLayers = new ArrayList();
        
        for(int j = 0; j < network.getHiddenLayers().size(); j++)
        {
            hiddenLayers.add(new Neuron[network.getHiddenLayers().get(j).length]);
            for(int i = 0; i < hiddenLayers.get(j).length; i++)
            {
                Neuron neuron = network.getHiddenLayers().get(j)[i];
                float[] weights = new float[neuron.getWeights().length];
                for(int w = 0; w < weights.length; w++)
                {
                    weights[w] = neuron.getWeights()[w]*(1 - mutation * (1 - 2*random.nextFloat()));
                }
                float bias = neuron.getBias() * (1 - mutation * (1 - 2*random.nextFloat()));
                hiddenLayers.get(j)[i] = new Neuron(weights, bias, false);
            }
            if(j > 0)
            {
                for(Neuron h2 : hiddenLayers.get(j))
                {
                    for(Neuron h1: hiddenLayers.get(j - 1))
                        h2.connectInput(h1);
                }
            }
        }
        outputLayer = new Neuron[network.getOutputLayer().length];
        for(int i = 0; i < network.getOutputLayer().length; i++)
        {
            Neuron neuron = network.getOutputLayer()[i];
            float[] weights = new float[neuron.getWeights().length];
            for(int w = 0; w < weights.length; w++)
            {
                weights[w] = neuron.getWeights()[w]*(1 - mutation * (1 - 2*random.nextFloat()));
            }
            float bias = neuron.getBias() * (1 - mutation * (1 - 2*random.nextFloat()));
            outputLayer[i] = new Neuron(weights, bias, false);
        }
        for(Neuron o : outputLayer)
        {
            for(Neuron h: hiddenLayers.get(hiddenLayers.size() - 1))
                o.connectInput(h);
        }
    }
    
    //Create network from save data
    public Network(String data)
    {
        //System.out.println("Data");
        //System.out.println(data);
        //System.out.println("");
        inputNr = Integer.parseInt(XMLReader.getElement(data, "inputNr"));
        
        String hiddenLayer = XMLReader.getElement(data, "hiddenLayers");
        ArrayList<String> layerData = XMLReader.getElements(hiddenLayer, "layer");
        //System.out.println("layerData.size()=" + layerData.size());
        hiddenLayers = new ArrayList();
        for(int j = 0; j < layerData.size(); j++)
        {
            String layer = layerData.get(j);
            //System.out.println("Layer:");
            ArrayList<String> neuronData = XMLReader.getElements(layer, "Neuron");
            hiddenLayers.add(new Neuron[neuronData.size()]);
            for(int i = 0; i < hiddenLayers.get(j).length; i++)
            {
                String neuron = neuronData.get(i);
                //System.out.println("Neuron:");
                String biasString = XMLReader.getElement(neuron, "bias");
                float biasValue = Float.parseFloat(biasString);
                //System.out.println("Bias = " + biasValue);
                String weightRibbon = XMLReader.getElement(neuron, "weights");
                String[] weightStrings = weightRibbon.split(",");
                float[] weightValues = new float[weightStrings.length];
                for(int w = 0; w < weightStrings.length; w++)
                {
                    weightValues[w] = Float.parseFloat(weightStrings[w]);
                }
//                for(double w: weightValues)
//                {
//                    System.out.println("Weight = " + w);
//                }
                String perceptronString = XMLReader.getElement(neuron, "perceptron");
                boolean perceptron = false;
                if(perceptronString.equals("true"))
                    perceptron = true; 
                else if(perceptronString.equals("false"))
                    perceptron = false;
                else
                    System.out.println("Something fishy!!!");
                hiddenLayers.get(j)[i] = new Neuron(weightValues, biasValue, perceptron);
            }
            if(j > 0)
            {
                for(Neuron h2 : hiddenLayers.get(j))
                {
                    for(Neuron h1: hiddenLayers.get(j - 1))
                        h2.connectInput(h1);
                }
            }
        }
        
        String outputLayerString = XMLReader.getElement(data, "outputLayer");
        ArrayList<String> neuronData = XMLReader.getElements(outputLayerString, "Neuron");
        outputLayer = new Neuron[neuronData.size()];
        for(int i = 0; i < outputLayer.length; i++)
        {
            String neuron = neuronData.get(i);
                //System.out.println("Neuron:");
                String biasString = XMLReader.getElement(neuron, "bias");
                float biasValue = Float.parseFloat(biasString);
                //System.out.println("Bias = " + biasValue);
                String weightRibbon = XMLReader.getElement(neuron, "weights");
                String[] weightStrings = weightRibbon.split(",");
                float[] weightValues = new float[weightStrings.length];
                for(int w = 0; w < weightStrings.length; w++)
                {
                    weightValues[w] = Float.parseFloat(weightStrings[w]);
                }
//                for(double w: weightValues)
//                {
//                    System.out.println("Weight = " + w);
//                }
                String perceptronString = XMLReader.getElement(neuron, "perceptron");
                boolean perceptron = false;
                if(perceptronString.equals("true"))
                    perceptron = true; 
                outputLayer[i] = new Neuron(weightValues, biasValue, perceptron);
        }
        for(Neuron o : outputLayer)
        {
            for(Neuron h: hiddenLayers.get(hiddenLayers.size() - 1))
                o.connectInput(h);
        }
    }
    
    //create a network child that randomly chooses between properties for each neuron between those of two parent networks
    //this requires networks of identical layersizes to work
    public Network(Network parent1, Network parent2)
    {
        boolean compatible = true;
        if(parent1.getHiddenLayers().size() == parent2.getHiddenLayers().size())
        {
            for(int i = 0; i < parent1.getHiddenLayers().size(); i++)
            {
                if(parent1.getHiddenLayers().get(i).length != parent2.getHiddenLayers().get(i).length)
                    compatible = false;
            }
        }
        if(compatible)
        {
            inputNr = parent1.getInputNr();
            hiddenLayers = new ArrayList();
            for(int l = 0; l < parent1.getHiddenLayers().size(); l++)
            {
                Neuron[] layer = new Neuron[parent1.getHiddenLayers().get(l).length];
                for(int n = 0; n < layer.length; n++)
                {
                    if(random.nextBoolean())
                    {
                        Neuron parentNeuron = parent1.getHiddenLayers().get(l)[n];
                        layer[n] = new Neuron(parentNeuron.getWeights(), parentNeuron.getBias(), parentNeuron.isPerceptron());
                    }
                    else
                    {
                        Neuron parentNeuron = parent2.getHiddenLayers().get(l)[n];
                        layer[n] = new Neuron(parentNeuron.getWeights(), parentNeuron.getBias(), parentNeuron.isPerceptron());
                    }
                }
                hiddenLayers.add(layer);
                if(l > 0)
                {
                    //connect each layers neurons to those of the previous one.
                    for(Neuron h2 : hiddenLayers.get(l))
                    {
                        for(Neuron h1: hiddenLayers.get(l - 1))
                        {
                            h2.connectInput(h1);
                        }
                    }
                }
            }
            outputLayer = new Neuron[parent1.getOutputLayer().length];
            for(int n = 0; n < outputLayer.length; n++)
            {
                if(random.nextBoolean())
                {
                    Neuron parentNeuron = parent1.getOutputLayer()[n];
                    outputLayer[n] = new Neuron(parentNeuron.getWeights(), parentNeuron.getBias(), parentNeuron.isPerceptron());
                }
                else
                {
                    Neuron parentNeuron = parent2.getOutputLayer()[n];
                    outputLayer[n] = new Neuron(parentNeuron.getWeights(), parentNeuron.getBias(), parentNeuron.isPerceptron());
                }
            }
            for(Neuron o : outputLayer)
            {
                for(Neuron h: hiddenLayers.get(hiddenLayers.size() - 1))
                    o.connectInput(h);
            }
        }
        else
        {
            System.out.println("Network(parent1, parent2): parents not compatible!");
        }
    }
    
    public Network(Network parent1, Network parent2, float resetChance)
    {
        boolean compatible = true;
        if(parent1.getHiddenLayers().size() == parent2.getHiddenLayers().size())
        {
            for(int i = 0; i < parent1.getHiddenLayers().size(); i++)
            {
                if(parent1.getHiddenLayers().get(i).length != parent2.getHiddenLayers().get(i).length)
                    compatible = false;
            }
        }
        if(compatible)
        {
            inputNr = parent1.getInputNr();
            hiddenLayers = new ArrayList();
            for(int l = 0; l < parent1.getHiddenLayers().size(); l++)
            {
                Neuron[] layer = new Neuron[parent1.getHiddenLayers().get(l).length];
                for(int n = 0; n < layer.length; n++)
                {
                    //chance to mutate a neuron completely
                    if(random.nextFloat() < resetChance)
                    {
                        float maxWeight = 10;
                        float maxBias = 10;
                        float[] weights = new float[inputNr];
                        if(l > 0)
                            weights = new float[hiddenLayers.get(l-1).length];
                        for(int w = 0; w < weights.length; w++)
                        {
                            weights[w] = (float) ((0.5 - random.nextFloat()) * 2 * maxWeight);
                        }
                        float bias = -random.nextFloat()*maxBias;
                        layer[n] = new Neuron(weights, bias, random.nextBoolean());
                    }
                    //otherwise it gets chosen from one of the parents at random
                    else
                    {
                        if(random.nextBoolean())
                        {
                            Neuron parentNeuron = parent1.getHiddenLayers().get(l)[n];
                            layer[n] = new Neuron(parentNeuron.getWeights(), parentNeuron.getBias(), parentNeuron.isPerceptron());
                        }
                        else
                        {
                            Neuron parentNeuron = parent2.getHiddenLayers().get(l)[n];
                            layer[n] = new Neuron(parentNeuron.getWeights(), parentNeuron.getBias(), parentNeuron.isPerceptron());
                        }
                    }
                }
                hiddenLayers.add(layer);
                if(l > 0)
                {
                    //connect each layers neurons to those of the previous one.
                    for(Neuron h2 : hiddenLayers.get(l))
                    {
                        for(Neuron h1: hiddenLayers.get(l - 1))
                        {
                            h2.connectInput(h1);
                        }
                    }
                }
            }
            outputLayer = new Neuron[parent1.getOutputLayer().length];
            for(int n = 0; n < outputLayer.length; n++)
            {
                //chance to mutate a neuron completely
                if(random.nextFloat() < resetChance)
                {
                    float maxWeight = 10;
                    float maxBias = 10;
                    float[] weights = new float[hiddenLayers.get(hiddenLayers.size() - 1).length];
                    for(int w = 0; w < weights.length; w++)
                    {
                        weights[w] = (float) ((0.5 - random.nextFloat()) * 2 * maxWeight);
                    }
                    float bias = -random.nextFloat()*maxBias;
                    outputLayer[n] = new Neuron(weights, bias, random.nextBoolean());
                }
                //otherwise choose from parents at random
                else
                {
                    if(random.nextBoolean())
                    {
                        Neuron parentNeuron = parent1.getOutputLayer()[n];
                        outputLayer[n] = new Neuron(parentNeuron.getWeights(), parentNeuron.getBias(), parentNeuron.isPerceptron());
                    }
                    else
                    {
                        Neuron parentNeuron = parent2.getOutputLayer()[n];
                        outputLayer[n] = new Neuron(parentNeuron.getWeights(), parentNeuron.getBias(), parentNeuron.isPerceptron());
                    }
                }
            }
            for(Neuron o : outputLayer)
            {
                for(Neuron h: hiddenLayers.get(hiddenLayers.size() - 1))
                    o.connectInput(h);
            }
        }
        else
        {
            System.out.println("Network(parent1, parent2): parents not compatible!");
        }
    }
    
    public float[] getOutputs(float[] inputs)
    {
        float[] outputs = new float[outputLayer.length];
        for(int i = 0; i < outputs.length; i++)
        {
            outputs[i] = outputLayer[i].getOutput(inputs);
        }
        return outputs;
    }
    
    public float[] getOutputs2(float[] inputs)
    {
        float[] outputs = new float[outputLayer.length];
        float[] lastLayerOutputs = new float[hiddenLayers.get(0).length]; 
        for(int j = 0; j < lastLayerOutputs.length; j++)
        {
            lastLayerOutputs[j] = hiddenLayers.get(0)[j].getOutput2(inputs);
        }
        if(hiddenLayers.size() > 1)
        {
            for(int i = 1; i < hiddenLayers.size(); i++)
            {
                float[] layerOutputs = new float[hiddenLayers.get(i).length]; 
                for(int j = 0; j < layerOutputs.length; j++)
                {
                    layerOutputs[j] = hiddenLayers.get(i)[j].getOutput2(lastLayerOutputs);
                }
                lastLayerOutputs = layerOutputs;
            }
        }
        for(int i = 0; i < outputs.length; i++)
        {
            outputs[i] = outputLayer[i].getOutput2(lastLayerOutputs);
        }
        return outputs;
    }
    
    public void render(Graphics g, int renderX, int renderY, int boxWidth, int boxHeight)
    {
        g.setColor(Color.BLACK);
        g.fillRect(renderX, renderY, boxWidth, boxHeight);
        int columns = 1 + hiddenLayers.size() + 1;
        int[] rowSize = new int[columns];
        rowSize[0] = inputNr;
        for(int i = 1; i <= rowSize.length - 2; i++)
        {
            rowSize[i] = hiddenLayers.get(i - 1).length;
        }
        rowSize[rowSize.length - 1] = outputLayer.length;
        int maxRowSize = rowSize[0];
        for(int i = 0; i <= rowSize.length - 1; i++)
        {
            if(rowSize[i] > maxRowSize)
            {
                maxRowSize = rowSize[i];
            }
        }
        int radius = Math.min(boxHeight/(3 * maxRowSize), boxWidth/(3 * (columns + 1)));
        int xInput = boxWidth/(columns + 1);
        for(int i = 0; i <= rowSize[0] - 1; i++)
        {
            int yNeuron = (int) ((2 + 3 * i + (maxRowSize - rowSize[0]) * 1.5) * radius);
            g.setColor(Color.WHITE);
            g.fillOval(renderX + xInput - radius, renderY + yNeuron - radius, 2*radius, 2*radius);
        }
        for(int l = 0; l < hiddenLayers.size(); l++)
        {
            int xLayer = (2 + l) * boxWidth/(columns + 1);
            for(int i = 0; i <= rowSize[l + 1] - 1; i++)
            {
                int yNeuron = (int) ((2 + 3 * i + (maxRowSize - rowSize[l + 1]) * 1.5) * radius);
                hiddenLayers.get(l)[i].render(g, renderX + xLayer, renderY + yNeuron, 2 * radius);
            }
        }
        int xOutput = (hiddenLayers.size() + 2) * boxWidth/(columns + 1);
        for(int i = 0; i <= rowSize[rowSize.length - 1] - 1; i++)
        {
            int yNeuron = (int) ((2 + 3 * i + (maxRowSize - rowSize[rowSize.length - 1]) * 1.5) * radius);
            outputLayer[i].render(g, renderX + xOutput, renderY + yNeuron, 2 * radius);
        }
    }
    
    public void render(Graphics g, int renderX, int renderY, int boxWidth, int boxHeight, float[] inputs)
    {
        g.setColor(Color.BLACK);
        g.fillRect(renderX, renderY, boxWidth, boxHeight);
        int columns = 1 + hiddenLayers.size() + 1;
        int[] rowSize = new int[columns];
        rowSize[0] = inputNr;
        for(int i = 1; i <= rowSize.length - 2; i++)
        {
            rowSize[i] = hiddenLayers.get(i - 1).length;
        }
        rowSize[rowSize.length - 1] = outputLayer.length;
        int maxRowSize = rowSize[0];
        for(int i = 0; i <= rowSize.length - 1; i++)
        {
            if(rowSize[i] > maxRowSize)
            {
                maxRowSize = rowSize[i];
            }
        }
        int radius = Math.min(boxHeight/(3 * maxRowSize), boxWidth/(3 * (columns + 1)));
        int xInput = boxWidth/(columns + 1);
        int[] yInput = new int[rowSize[0]];
        //calculate coordinates and draw connectors
        for(int i = 0; i <= rowSize[0] - 1; i++)
        {
            yInput[i] = (int) ((2 + 3 * i + (maxRowSize - rowSize[0]) * 1.5) * radius);
        }
        int[] xLayer = new int[hiddenLayers.size()];
        int[][] yLayer = new int[hiddenLayers.size()][maxRowSize];
        for(int l = 0; l < hiddenLayers.size(); l++)
        {
            xLayer[l] = (2 + l) * boxWidth/(columns + 1);
            for(int i = 0; i <= rowSize[l + 1] - 1; i++)
            {
                yLayer[l][i] = (int) ((2 + 3 * i + (maxRowSize - rowSize[l + 1]) * 1.5) * radius);
                int[] brightness;
                if(l == 0)
                {
                    brightness = new int[inputs.length];
                    for(int n = 0; n < brightness.length; n++)
                    {
                        brightness[n] = (int) (50 + Math.abs(inputs[n])*205.0);
                    }
                }
                else
                {
                    brightness = new int[hiddenLayers.get(l - 1).length];
                    for(int n = 0; n < brightness.length; n++)
                    {
                        brightness[n] = (int) (50 + Math.abs(hiddenLayers.get(l - 1)[n].getOutput(inputs))*205.0);
                    }
                }
                if(l > 0)
                {      
                    for(int n = 0; n < hiddenLayers.get(l)[i].getInputNeurons().size(); n++)
                    {
                        g.setColor(new Color(brightness[n], brightness[n], brightness[n]));
                        g.drawLine(renderX + xLayer[l], renderY + yLayer[l][i], renderX + xLayer[l - 1], renderY + yLayer[l - 1][n]);
                    }
                }
                else
                {
                    for(int n = 0; n < rowSize[0]; n++)
                    {
                        if(inputs[n] > 0)
                            g.setColor(new Color(brightness[n], brightness[n], brightness[n]));
                        else
                            g.setColor(new Color(brightness[n], 50, 50));
                        
                        g.drawLine(renderX + xLayer[l], renderY + yLayer[l][i], renderX + xInput, renderY + yInput[n]);
                    }
                }
            }
        }
        int xOutput = (hiddenLayers.size() + 2) * boxWidth/(columns + 1);
        int[] yOutput = new int[rowSize[rowSize.length - 1]];
        for(int i = 0; i <= rowSize[rowSize.length - 1] - 1; i++)
        {
            yOutput[i] = (int) ((2 + 3 * i + (maxRowSize - rowSize[rowSize.length - 1]) * 1.5) * radius);
            int[] brightness = new int[hiddenLayers.get(hiddenLayers.size() - 1).length];
            for(int n = 0; n < brightness.length; n++)
            {
                brightness[n] = (int) (50 + Math.abs(hiddenLayers.get(hiddenLayers.size() - 1)[n].getOutput(inputs))*205.0);
            }
            for(int n = 0; n < rowSize[rowSize.length - 2]; n++)
            {
                g.setColor(new Color(brightness[n], brightness[n], brightness[n]));
                g.drawLine(renderX + xOutput, renderY + yOutput[i], renderX +  xLayer[hiddenLayers.size() - 1], renderY + yLayer[hiddenLayers.size() - 1][n]);
            }
        }
        
        //draw neurons
        for(int i = 0; i <= rowSize[0] - 1; i++)
        {
            int brightness = 50 + (int) Math.abs(inputs[i]*205.0);
            if(inputs[i] > 0)
                g.setColor(new Color(brightness, brightness, brightness));
            else
                g.setColor(new Color(brightness, 50, 50));
            g.fillOval(renderX + xInput - radius, renderY + yInput[i] - radius, 2*radius, 2*radius);
            Font original = g.getFont();
            g.setFont(new Font(original.getFontName(), Font.PLAIN, 1 + radius/2));
            g.setColor(Color.BLACK);
            g.drawString(DF.format(inputs[i]), renderX + xInput - g.getFont().getSize(), renderY + yInput[i] + g.getFont().getSize()/2);
            g.setFont(original);
        }
        for(int l = 0; l < hiddenLayers.size(); l++)
        {
            for(int i = 0; i <= rowSize[l + 1] - 1; i++)
            {
                hiddenLayers.get(l)[i].render(g, renderX + xLayer[l], renderY + yLayer[l][i], 2 * radius, inputs);
            }
        }
        for(int i = 0; i <= rowSize[rowSize.length - 1] - 1; i++)
        {
            outputLayer[i].render(g, renderX + xOutput, renderY + yOutput[i], 2 * radius, inputs);
        }
    }
    
    public void render(Graphics g, int renderX, int renderY, int boxWidth, int boxHeight, float[] inputs, int inputRange)
    {
        g.setColor(Color.BLACK);
        g.fillRect(renderX, renderY, boxWidth, boxHeight);
        int columns = 1 + hiddenLayers.size() + 1;
        int[] rowSize = new int[columns];
        rowSize[0] = inputNr;
        for(int i = 1; i <= rowSize.length - 2; i++)
        {
            rowSize[i] = hiddenLayers.get(i - 1).length;
        }
        rowSize[rowSize.length - 1] = outputLayer.length;
        int maxRowSize = rowSize[0];
        for(int i = 0; i <= rowSize.length - 1; i++)
        {
            if(rowSize[i] > maxRowSize)
            {
                maxRowSize = rowSize[i];
            }
        }
        int radius = Math.min(boxHeight/(3 * maxRowSize), boxWidth/(3 * (columns + 1)));
        int xInput = boxWidth/(columns + 1);
        int[] yInput = new int[rowSize[0]];
        //calculate coordinates and draw connectors
        for(int i = 0; i <= rowSize[0] - 1; i++)
        {
            yInput[i] = (int) ((2 + 3 * i + (maxRowSize - rowSize[0]) * 1.5) * radius);
        }
        int[] xLayer = new int[hiddenLayers.size()];
        int[][] yLayer = new int[hiddenLayers.size()][maxRowSize];
        for(int l = 0; l < hiddenLayers.size(); l++)
        {
            xLayer[l] = (2 + l) * boxWidth/(columns + 1);
            for(int i = 0; i <= rowSize[l + 1] - 1; i++)
            {
                yLayer[l][i] = (int) ((2 + 3 * i + (maxRowSize - rowSize[l + 1]) * 1.5) * radius);
               
                if(l > 0)
                {      
                    for(int n = 0; n < hiddenLayers.get(l)[i].getInputNeurons().size(); n++)
                    {
                        g.setColor(new Color(255, 255, 255));
                        g.drawLine(renderX + xLayer[l], renderY + yLayer[l][i], renderX + xLayer[l - 1], renderY + yLayer[l - 1][n]);
                    }
                }
                else
                {
                    for(int n = 0; n < rowSize[0]; n++)
                    {
                        if(inputs[n] > 0)
                            g.setColor(new Color(255, 255, 255));
                        else
                            g.setColor(new Color(255, 50, 50));
                        
                        g.drawLine(renderX + xLayer[l], renderY + yLayer[l][i], renderX + xInput, renderY + yInput[n]);
                    }
                }
            }
        }
        int xOutput = (hiddenLayers.size() + 2) * boxWidth/(columns + 1);
        int[] yOutput = new int[rowSize[rowSize.length - 1]];
        for(int i = 0; i <= rowSize[rowSize.length - 1] - 1; i++)
        {
            yOutput[i] = (int) ((2 + 3 * i + (maxRowSize - rowSize[rowSize.length - 1]) * 1.5) * radius);
            int[] brightness = new int[hiddenLayers.get(hiddenLayers.size() - 1).length];
            for(int n = 0; n < brightness.length; n++)
            {
                brightness[n] = (int) (50 + Math.abs(hiddenLayers.get(hiddenLayers.size() - 1)[n].getOutput(inputs))*205.0);
            }
            for(int n = 0; n < rowSize[rowSize.length - 2]; n++)
            {
                g.setColor(new Color(brightness[n], brightness[n], brightness[n]));
                g.drawLine(renderX + xOutput, renderY + yOutput[i], renderX +  xLayer[hiddenLayers.size() - 1], renderY + yLayer[hiddenLayers.size() - 1][n]);
            }
        }
        
        //draw neurons
        for(int i = 0; i <= rowSize[0] - 1; i++)
        {
            int brightness = 50 + (int) Math.abs((inputs[i]/inputRange)*205.0);
            if(inputs[i] > 0)
                g.setColor(new Color(brightness, brightness, brightness));
            else
                g.setColor(new Color(brightness, 50, 50));
            g.fillOval(renderX + xInput - radius, renderY + yInput[i] - radius, 2*radius, 2*radius);
            Font original = g.getFont();
            g.setFont(new Font(original.getFontName(), Font.PLAIN, 1 + radius/2));
            g.setColor(Color.BLACK);
            g.drawString(DF.format(inputs[i]), renderX + xInput - g.getFont().getSize(), renderY + yInput[i] + g.getFont().getSize()/2);
            g.setFont(original);
        }
        for(int l = 0; l < hiddenLayers.size(); l++)
        {
            for(int i = 0; i <= rowSize[l + 1] - 1; i++)
            {
                hiddenLayers.get(l)[i].render(g, renderX + xLayer[l], renderY + yLayer[l][i], 2 * radius, inputs);
            }
        }
        for(int i = 0; i <= rowSize[rowSize.length - 1] - 1; i++)
        {
            outputLayer[i].render(g, renderX + xOutput, renderY + yOutput[i], 2 * radius, inputs);
        }
    }
    
    public int getInputNr()
    {
        return inputNr;
    }
    
    public Neuron[] getOutputLayer()
    {
        return outputLayer;
    }
    
    public ArrayList<Neuron[]> getHiddenLayers()
    {
        return hiddenLayers;
    }
//    private int inputNr;
//    private ArrayList<Neuron[]> hiddenLayers;
//    private Neuron[] outputLayer;
    public String createSaveData() 
    {
        String s = "<Network>";
        s+="<inputNr>" + inputNr + "</inputNr>";
        s+="<hiddenLayers>";
        for(int i = 0; i < hiddenLayers.size(); i++)
        {
            s+="<layer>";
            for(Neuron n: hiddenLayers.get(i))
            {
                s+=n.createSaveData();
            }
            s+="</layer>";
        }
        s+="</hiddenLayers>";
        s+="<outputLayer>";
        for(Neuron n: outputLayer)
        {
            s+=n.createSaveData();
        }
        s+="</outputLayer>";
        s+="</Network>";
        return s;
    }
    
    //returns maximum absolute value of all weights
    private float maxWeight()
    {
        float maxWeight = 0;
        for(Neuron[] layer: hiddenLayers)
        {
            for(Neuron n: layer)
            {
                for(float w: n.getWeights())
                {
                    if(Math.abs(w) > maxWeight)
                        maxWeight = Math.abs(w);
                }
            }
        }
        for(Neuron n: outputLayer)
        {
            for(float w: n.getWeights())
            {
                if(Math.abs(w) > maxWeight)
                    maxWeight = Math.abs(w);
            }
        }
        return maxWeight;
    }
    
    //returns maximum absolute value of all biases
    private float maxBias()
    {
        float maxBias = 0;
        for(Neuron[] layer: hiddenLayers)
        {
            for(Neuron n: layer)
            {
                if(Math.abs(n.getBias()) > maxBias)
                    maxBias = Math.abs(n.getBias());
            }
        }
        for(Neuron n: outputLayer)
        {
            if(Math.abs(n.getBias()) > maxBias)
                maxBias = Math.abs(n.getBias());
        }
        return maxBias;
    }
}
