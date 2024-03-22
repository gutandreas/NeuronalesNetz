package edu.andreasgut.neuronalesnetzwerkfx.core;

import java.util.Random;

public class TrainingObject {

    double[] values;
    double max;
    int maxIndex;


    public TrainingObject(int numberOfValues) {
        this.values = Tools.getRandomValuesWithOnePeak(numberOfValues);

        max = 0;

        int counter = 0;

        for (double d: values) {
            if (d > max){
                max = d;
                maxIndex = counter;
            }
            counter++;
        }
    }

    @Override
    public String toString(){
        String str = "Values: ";
        for (double d: values) {
            str += d;
            str += " ";
        }
        str += "\nMax: " + max + " on Index " + maxIndex;

        return str;
    }

    public int getMaxIndex() {
        return maxIndex;
    }
}
