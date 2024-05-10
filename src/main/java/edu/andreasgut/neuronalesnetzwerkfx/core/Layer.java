package edu.andreasgut.neuronalesnetzwerkfx.core;

import java.util.LinkedList;

public abstract class Layer {

    private LinkedList<NetworkNode> nodes = new LinkedList<>();
    private double[] outputs;
    private NeuralNetwork neuralNetwork;

    public Layer(NeuralNetwork neuralNetwork) {
        this.neuralNetwork = neuralNetwork;
    }

    public double[] getOutputs() {
        return outputs;
    }

    abstract public void setNextLayer(Layer connectableLayer);

    abstract public void activateLayer();

    public LinkedList<NetworkNode> getNodes() {
        return nodes;
    }

    public int getNumberOfNodes(){
        return nodes.size();
    }

}
