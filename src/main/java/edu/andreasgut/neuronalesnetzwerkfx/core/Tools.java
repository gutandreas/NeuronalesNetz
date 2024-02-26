package edu.andreasgut.neuronalesnetzwerkfx.core;

public class Tools {

    public static double[] sigmoid(double[][] weights, double[] outputs) {
        double[] activations = new double[weights.length];
        for (int i = 0; i < weights.length; i++) {
            double sum = 0;
            for (int j = 0; j < weights[i].length; j++) {
                sum += weights[i][j] * outputs[j];
            }
            activations[i] = 1 / (1 + Math.exp(-sum));
        }
        return activations;
    }

}
