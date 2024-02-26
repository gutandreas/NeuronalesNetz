package edu.andreasgut.neuronalesnetzwerkfx.core;

import java.util.Random;

public class Hiddenlayer extends Layer {

    int numberOfNodes;
    double[][] weights;
    Layer previousLayer;
    Layer nextLayer;
    double[] outputs;

    public Hiddenlayer(int numberOfNodes, Layer previousLayer){
        this.previousLayer = previousLayer;
        previousLayer.setNextLayer(this);
        this.numberOfNodes = numberOfNodes;
        double[][] weights = new double[numberOfNodes][previousLayer.getNumberOfNodes()];
        Random random = new Random();
        for (int i = 0; i < weights.length; i++){
            for (int j = 0; j < weights[0].length; j++){
                weights[i][j] = random.nextDouble();
            }
        }
        this.weights = weights;
    }

    public void activate(double[] outputsPreviousLayer){
        outputs = Tools.sigmoid(weights, outputsPreviousLayer);
        nextLayer.activate(outputs);

    }

    public int getNumberOfNodes() {
        return numberOfNodes;
    }

    public double[] getOutputs() {
        return outputs;
    }

    public void setNextLayer(Layer nextConnectableLayer) {
        this.nextLayer = nextConnectableLayer;
    }

    @Override
    public String toString(){
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < weights.length; i++){
            stringBuilder.append("\n");
            for (int j = 0; j < weights[0].length; j++){
                stringBuilder.append(weights[i][j]);
                stringBuilder.append("\t\t");
            }
        }

        return stringBuilder.toString();
    }
}
