/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package dodger.neuralnetwork;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import static java.lang.Math.exp;
import java.text.DecimalFormat;
import java.util.ArrayList;

public class Neuron
{
    private float[] weights;
    private float bias;
    private ArrayList<Neuron> inputNeurons;
    private boolean perceptron;
    private DecimalFormat DF = new DecimalFormat("#.##");
    
    public Neuron(float[] weights, float bias, boolean perceptron)
    {
        this.weights = weights;
        this.bias = bias;
        this.perceptron = perceptron;
        inputNeurons = new ArrayList();
    }
    
    public void connectInput(Neuron n)
    {
        inputNeurons.add(n);
    }
    
    public float getOutput(float[] inputs)
    {
        float output = 0;
        if(!perceptron)
        {
            if(inputNeurons.isEmpty())
            {
                if(inputs.length == weights.length)
                {
                    output = (float) (1 / (1 + exp(-dotProduct(weights, inputs) - bias)));
                }
                else
                {
                    System.out.println("Neuron: getOutput(): wrong input. " + 
                            "inputs.length = " + inputs.length + " vs " + weights.length);
                }
            }
            else
            {
                float[] subInputs = new float[inputNeurons.size()];
                for(int i = 0; i < inputNeurons.size(); i++)
                {
                    subInputs[i] = inputNeurons.get(i).getOutput(inputs);
                }
                if(subInputs.length == weights.length)
                {
                    output = (float) (1 / (1 + exp(-dotProduct(weights, subInputs) - bias)));
                }
                else
                {
                    System.out.println("Neuron: getOutput(): wrong input. " + "subInputs.length = "  + subInputs.length + " vs " + weights.length);
                }
            }
        }
        else
        {
            if(inputNeurons.isEmpty())
            {
                if(inputs.length == weights.length)
                {
                    double condition = dotProduct(weights, inputs) + bias;
                    if(condition <= 0)
                        output = 0;
                    else
                        output = 1;
                }
                else
                {
                    System.out.println("Neuron: getOutput(): wrong input. " + inputs.length + " vs " + weights.length);
                }
            }
            else
            {
                float[] subInputs = new float[inputNeurons.size()];
                for(int i = 0; i < inputNeurons.size(); i++)
                {
                    subInputs[i] = inputNeurons.get(i).getOutput(inputs);
                }
                if(subInputs.length == weights.length)
                {
                    float condition = dotProduct(weights, subInputs) + bias;
                    if(condition <= 0)
                        output = 0;
                    else
                        output = 1;
                }
                else
                {
                    System.out.println("Neuron: getOutput(): wrong input. " + inputs.length + " vs " + weights.length);
                }
            }
        }
        return output;
    }
    
    public float getOutput2(float[] inputs)
    {
        float output = 0;
        if(!perceptron)
        {
            if(inputs.length == weights.length)
            {
                output = (float) (1 / (1 + exp(-dotProduct(weights, inputs) - bias)));
            }
            else
            {
                System.out.println("Neuron: getOutput(): wrong input. " + 
                        "inputs.length = " + inputs.length + " vs " + weights.length);
            }
        }
        else
        {
            if(inputs.length == weights.length)
            {
                double condition = dotProduct(weights, inputs) + bias;
                if(condition <= 0)
                    output = 0;
                else
                    output = 1;
            }
            else
            {
                System.out.println("Neuron: getOutput(): wrong input. " + inputs.length + " vs " + weights.length);
            }
           
        }
        return output;
    }
    
    private float dotProduct(float[] a, float[] b)
    {
        float dotProduct = 0;
        if(a.length == b.length)
        {
            for(int i = 0; i < a.length; i++)
            {
                dotProduct += a[i] * b[i];
            }
        }
        else
        {
            System.out.println("Neuron: dotProduct(): wrong input. " + + a.length + " vs " + b.length);
        }
        return dotProduct;
        
    }
    
    public float[] getWeights()
    {
        return weights;
    }
    
    public float getBias()
    {
        return bias;
    }
    
    public boolean isPerceptron()
    {
        return perceptron;
    }
    
    public ArrayList<Neuron> getInputNeurons()
    {
        return inputNeurons;
    }
    
    public void render(Graphics g, int renderX, int renderY, int renderD)
    {
        if(perceptron)
            g.setColor(Color.BLUE);
        else
            g.setColor(Color.WHITE);
        g.fillOval(renderX - renderD/2, renderY - renderD/2, renderD, renderD);
        Font original = g.getFont();
        g.setFont(new Font(original.getFontName(), Font.PLAIN, 1 + renderD/4));
        g.setColor(Color.BLACK);
        g.drawString(DF.format(bias), renderX - renderD/4, renderY + g.getFont().getSize()/2);
        g.setColor(Color.WHITE);
        g.setFont(new Font(original.getFontName(), Font.PLAIN, 1 + renderD/8));
        for(int i = 0; i < weights.length; i++)
        {
            g.drawString(DF.format(weights[i]), renderX - renderD/2 - g.getFont().getSize()*3, 
                    renderY - renderD/2 + renderD/(weights.length + 1) + i * g.getFont().getSize());
        }
        
    }
    
    public void render(Graphics g, int renderX, int renderY, int renderD, float[] inputs)
    {
        double output = getOutput(inputs);
        if(perceptron)
            if(output > 0)
                g.setColor(Color.BLUE);
            else
                g.setColor(Color.RED);
        else
        {
            int brightness = 50 + (int) (output*205.0);
            g.setColor(new Color(brightness, brightness, brightness));
        }
        g.fillOval(renderX - renderD/2, renderY - renderD/2, renderD, renderD);
        Font original = g.getFont();
        g.setFont(new Font(original.getFontName(), Font.PLAIN, 1 + renderD/4));
        g.setColor(Color.BLACK);
        g.drawString(DF.format(output), renderX - renderD/4, renderY + g.getFont().getSize()/2);
        g.setFont(original);
    }
    
    public String createSaveData() 
    {
        String s = "<Neuron>";
        s+="<perceptron>";
        if(perceptron)
            s+="true";
        else
            s+="false";
        s+="</perceptron>";
        s+="<bias>" + bias + "</bias>";
        s+="<weights>";
        for(int w = 0; w < weights.length; w++)
        {
            s+=weights[w];
            if(w != weights.length - 1)
                s+=",";
        }
        s+="</weights>";
        s+="</Neuron>";
        return s;
    }
}
