package edu.andreasgut.neuronalesnetzwerkfx.core;

import java.util.LinkedList;

public class Tools {

    /*public static double[] sigmoid(double[][] weights, double[] outputs) {
        double[] activations = new double[weights.length];
        for (int i = 0; i < weights.length; i++) {
            double sum = 0;
            for (int j = 0; j < weights[i].length; j++) {
                sum += weights[i][j] * outputs[j];
            }
            activations[i] = 1 / (1 + Math.exp(-sum));
        }
        return activations;
    }*/

    public static double sigmoid(LinkedList<NetworkEdge> inputEdges){
        double sum = 0;
        for (NetworkEdge edge : inputEdges){
            sum += edge.getFrom().getOutput() * edge.getWeight();
        }
        double output = 1 / (1 + Math.exp(-sum));
        return output;
    }




}
