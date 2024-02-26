package edu.andreasgut.neuronalesnetzwerkfx.core;

public class Main {

    public static void main(String[] args) {



        Inputlayer inputlayer = new Inputlayer(5);
        Hiddenlayer hiddenlayer1 = new Hiddenlayer(5, inputlayer);
        Outputlayer outputlayer = new Outputlayer(2, hiddenlayer1);

        double[] inputs = new double[]{0.3, 0.5, 0.9, 0.2, 0.4};

        inputlayer.activate(inputs);


    }
}
