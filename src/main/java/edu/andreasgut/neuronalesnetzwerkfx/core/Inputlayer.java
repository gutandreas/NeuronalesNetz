package edu.andreasgut.neuronalesnetzwerkfx.core;

public class Inputlayer extends Layer {

    int numberOfNodes;
    Layer nextLayer;
    double[] outputs;

    public Inputlayer(int numberOfNodes) {
        this.numberOfNodes = numberOfNodes;
    }


    public void setNextLayer(Layer nextLayer) {
        this.nextLayer = nextLayer;
    }

    @Override
    public void activate(double[] outputsPreviousLayer) {
        this.outputs = outputsPreviousLayer;
        nextLayer.activate(outputs);
    }

    public double[] getOutputs() {
        return outputs;
    }
}
