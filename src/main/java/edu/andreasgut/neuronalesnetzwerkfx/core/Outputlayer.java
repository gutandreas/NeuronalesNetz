package edu.andreasgut.neuronalesnetzwerkfx.core;

import java.util.Random;

public class Outputlayer extends Layer {

    int numberOfNodes;
    Layer previousLayer;
    double[][] weights;

    public Outputlayer(int numberOfNodes, Layer previousLayer) {
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

    public int getNumberOfNodes() {
        return numberOfNodes;
    }

    @Override
    public void setNextLayer(Layer connectableLayer) {
        System.out.print("Outputlayer hat keinen nächsten ConnectableLayer");
    }

    @Override
    public void activate(double[] outputsPreviousLayer) {
        double[] outputs = Tools.sigmoid(weights, outputsPreviousLayer);
        double max = 0;
        int maxIndex = 0;
        for (int i = 0; i < outputs.length; i++){
            if (outputs[i] > max){
                max = outputs[i];
                maxIndex = i;
            }
        }
        System.out.print("Resultat: " + maxIndex + " mit Wert " + max);
    }



}
