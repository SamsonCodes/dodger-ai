/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package dodger.neuralnetwork;

public class TrainingEntry 
{
    public double[] inputs;
    public double[] desiredOutputs;
    
    public TrainingEntry(double[] inputs, double[] desiredOutputs)
    {
        this.inputs = inputs;
        this.desiredOutputs = desiredOutputs;
    }

}
