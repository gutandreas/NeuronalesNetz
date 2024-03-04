package edu.andreasgut.neuronalesnetzwerkfx.core;

import java.util.LinkedList;

public abstract class Layer {



    private LinkedList<Node> nodes = new LinkedList<>();
    private double[] outputs;


    public double[] getOutputs() {
        return outputs;
    }

    abstract public void setNextLayer(Layer connectableLayer);

    abstract public void activateLayer();

    public LinkedList<Node> getNodes() {
        return nodes;
    }

    public int getNumberOfNodes(){
        return nodes.size();
    }
}
