package edu.andreasgut.neuronalesnetzwerkfx.core;

import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.shape.Circle;

import java.util.LinkedList;

public class NetworkNode {

    private double output;
    private LinkedList<NetworkEdge> inputEdges = new LinkedList<>();
    private LinkedList<NetworkEdge> outputEdges = new LinkedList<>();
    private Group graphicGroup;


    public void addInputEdge(NetworkEdge edge){
        inputEdges.add(edge);
    }

    public void addOutputEdge(NetworkEdge edge){
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

    public LinkedList<NetworkEdge> getInputEdges() {
        return inputEdges;
    }

    public LinkedList<NetworkEdge> getOutputEdges() {
        return outputEdges;
    }


    public void setGraphicGroup(Group graphicGroup) {
        this.graphicGroup = graphicGroup;
    }

    public Group getGraphicGroup() {
        return graphicGroup;
    }

    public void learn(double target, double learningRate) {
        for (NetworkEdge edge : getInputEdges()) {
            double correctedWeight = edge.getWeight() - learningRate * (-(target - output) * output * (1 - output) * edge.getFrom().getOutput());
            edge.setWeight(correctedWeight);
            edge.updateLineGraphic();
            edge.getFrom().learn(target, learningRate);
        }
    }
}
