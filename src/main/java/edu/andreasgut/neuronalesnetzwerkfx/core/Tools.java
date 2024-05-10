package edu.andreasgut.neuronalesnetzwerkfx.core;

import java.util.LinkedList;
import java.util.Random;

public class Tools {

    public static double sigmoid(LinkedList<NetworkEdge> inputEdges){
        double sum = 0;
        for (NetworkEdge edge : inputEdges){
            sum += edge.getFrom().getOutput() * edge.getWeight();
        }
        return  1 / (1 + Math.exp(-sum));
    }

    public static double[] getRandomValues(int numberOfValues){
        Random random = new Random();
        double[] values = new double[numberOfValues];
        for (int i = 0; i < numberOfValues; i++){
            values[i] = random.nextDouble();
        }
        return values;
    }

    public static double[] getRandomValuesWithOnePeak(int numberOfValues){
        Random random = new Random();
        double[] values = new double[numberOfValues];
        for (int i = 0; i < numberOfValues; i++){
            values[i] = random.nextDouble() * 0.5;
        }
        values[random.nextInt(numberOfValues)] = 0.95;
        return values;
    }


}
