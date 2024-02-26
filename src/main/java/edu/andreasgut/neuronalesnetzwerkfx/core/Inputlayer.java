package edu.andreasgut.neuronalesnetzwerkfx.core;

public class Inputlayer extends Layer {

    private Layer nextLayer;

    public Inputlayer(int numberOfNodes) {
        super.numberOfNodes = numberOfNodes;
    }


    public void setNextLayer(Layer nextLayer) {
        this.nextLayer = nextLayer;
    }

    @Override
    public void activate(double[] outputsPreviousLayer) {
        super.outputs = outputsPreviousLayer;
        nextLayer.activate(outputs);
    }

    public double[] getOutputs() {
        return outputs;
    }
}
