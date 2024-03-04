package edu.andreasgut.neuronalesnetzwerkfx.core;

import javafx.scene.control.Label;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;

import java.util.Random;

public class Edge {

    private Node from;
    private Node to;
    private double weight;
    private Line line;
    private Label label;


    public Edge(Node from, Node to) {
        this.from = from;
        this.to = to;
        Random random = new Random();
        this.weight = random.nextDouble();
    }

    public Node getFrom() {
        return from;
    }

    public Node getTo() {
        return to;
    }

    public double getWeight() {
        return weight;
    }

    public void setGraphicElement(Line line, Label label){
        this.line = line;
        this.label = label;
    }
}
