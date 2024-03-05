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

    public Circle getFirstCircleOfGroup(){
        for (Node node : graphicGroup.getChildren()) {

            if (node instanceof Circle) {
                return (Circle) node;
            }
        }
        return null;
    }
}
