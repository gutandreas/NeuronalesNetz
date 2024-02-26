package edu.andreasgut.neuronalesnetzwerkfx.core;

public abstract class Layer {


    int numberOfNodes;
    double[] outputs;
    public int getNumberOfNodes() {
        return numberOfNodes;
    }

    public double[] getOutputs() {
        return outputs;
    }

    abstract public void activate(double[] outputsPreviousLayer);

    abstract public void setNextLayer(Layer connectableLayer);


}
