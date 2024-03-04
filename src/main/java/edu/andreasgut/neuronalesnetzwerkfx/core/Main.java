package edu.andreasgut.neuronalesnetzwerkfx.core;

public class Main {

    public static void main(String[] args) {

        double[] inputs = new double[]{0.3, 0.5, 0.9, 0.2, 0.4};

        NeuralNetwork neuralNetwork = new NeuralNetwork(5, 2, 5, 2);
        neuralNetwork.startCalculations(inputs);


    }
}
