package edu.andreasgut.neuronalesnetzwerkfx.core;

public abstract class Layer {


    int numberOfNodes;
    public int getNumberOfNodes() {
        return numberOfNodes;
    }

    abstract public void activate(double[] outputsPreviousLayer);

    abstract public void setNextLayer(Layer connectableLayer);


}
