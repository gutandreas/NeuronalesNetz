package edu.andreasgut.neuronalesnetzwerkfx.core;

import javafx.scene.Group;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;

import java.util.LinkedList;

public class NetworkNode {

    private double output;
    private double delta;
    private final LinkedList<NetworkEdge> inputEdges = new LinkedList<>();
    private final LinkedList<NetworkEdge> outputEdges = new LinkedList<>();
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

    public double getDelta() {
        return delta;
    }

    public void setDelta(double delta) {
        this.delta = delta;
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

    public void updateNodeGraphic(Circle circle, Text text){
        this.graphicGroup = new Group(circle, text);
    }
    public void learn(double target, double learningRate, boolean outputLayer) {
        for (NetworkEdge inputEdge : getInputEdges()) {
            double error = target - output;
            double sigmoidDerivation = output * (1 - output);
            double input = inputEdge.getFrom().getOutput();
            if (outputLayer){
                delta = error * sigmoidDerivation;
            } else {
                delta = 0;
                for (NetworkEdge outputEdge : getOutputEdges()){
                    delta += outputEdge.getTo().getDelta() * outputEdge.getWeight();
                }
            }
            double correctedWeight = inputEdge.getWeight() - learningRate * delta * input;
            inputEdge.setWeight(correctedWeight);
            inputEdge.updateLineWeightGraphic();
            inputEdge.getFrom().learn(target, learningRate, false);
        }
    }
}
