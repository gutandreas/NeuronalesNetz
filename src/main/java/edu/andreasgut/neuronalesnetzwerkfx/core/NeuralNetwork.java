package edu.andreasgut.neuronalesnetzwerkfx.core;

import java.util.LinkedList;

public class NeuralNetwork {

    Inputlayer inputlayer;
    LinkedList<Hiddenlayer> hiddenlayers = new LinkedList<>();
    Outputlayer outputlayer;

    public NeuralNetwork(int numberOfInputNodes, int numberOfHiddenLayers, int numberOfHiddenLayerNodes, int numberOfOutputNodes) {
        this.inputlayer = new Inputlayer(numberOfInputNodes);
        Layer previousLayer = inputlayer;
        for (int i = 0; i < numberOfHiddenLayers; i++){
            Hiddenlayer tempHiddenLayer = new Hiddenlayer(numberOfHiddenLayerNodes, previousLayer);
            hiddenlayers.add(tempHiddenLayer);
            previousLayer = tempHiddenLayer;
        }
        this.outputlayer = new Outputlayer(numberOfOutputNodes, previousLayer);
    }

    public void activate(double[] inputs){
        int inputLength = inputs.length;
        int inputNodes = inputlayer.getNumberOfNodes();
        if (inputLength != inputNodes){
            System.out.println("Fehler. Die Anzahl der Inputwerte Stimmen nicht mit der Anzahl Knoten des Inputlayers überein.");
            System.out.println("Die Inputlänge beträgt " + inputLength + " und die Anzahl Knoten im Inputlayer beträgt " + inputNodes + ".");
        } else {
            inputlayer.activate(inputs);
        }
    }

    public Inputlayer getInputlayer() {
        return inputlayer;
    }

    public LinkedList<Hiddenlayer> getHiddenlayers() {
        return hiddenlayers;
    }

    public Outputlayer getOutputlayer() {
        return outputlayer;
    }
}
