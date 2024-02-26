package edu.andreasgut.neuronalesnetzwerkfx.core;

public class Inputlayer extends Layer {

    int numberOfNodes;
    Layer nextLayer;
    double[] outputs;

    public Inputlayer(int numberOfNodes) {
        this.numberOfNodes = numberOfNodes;
    }





    public void setNextLayer(Layer nextConnectableLayer) {
        this.nextLayer = nextConnectableLayer;
    }

    @Override
    public void activate(double[] outputsPreviousLayer) {
        outputs = outputsPreviousLayer;
        nextLayer.activate(outputs);
    }

    public double[] getOutputs() {
        return outputs;
    }
}
