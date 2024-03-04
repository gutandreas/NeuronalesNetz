package edu.andreasgut.neuronalesnetzwerkfx.core;

import java.util.LinkedList;

public class Node {

    private double output;
    private LinkedList<Edge> inputEdges = new LinkedList<>();
    private LinkedList<Edge> outputEdges = new LinkedList<>();



    public void addInputEdge(Edge edge){
        inputEdges.add(edge);
    }

    public void addOutputEdge(Edge edge){
        outputEdges.add(edge);
    }

    public void calculateOutput(){
        output = Tools.sigmoid(inputEdges);

        if (outputEdges.size() == 0){
            System.out.println("Output: " + output);
        }
    }

    public void setOutput(double output){
        this.output = output;
    }

    public double getOutput(){
        return output;
    }

    public LinkedList<Edge> getInputEdges() {
        return inputEdges;
    }

    public LinkedList<Edge> getOutputEdges() {
        return outputEdges;
    }
}
