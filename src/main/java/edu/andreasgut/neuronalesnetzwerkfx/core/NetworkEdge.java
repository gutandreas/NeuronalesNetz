package edu.andreasgut.neuronalesnetzwerkfx.core;

import javafx.scene.Group;
import javafx.scene.control.Label;
import javafx.scene.shape.Line;

import java.util.Random;

public class NetworkEdge {

    private NetworkNode from;
    private NetworkNode to;
    private double weight;
    private Line line;


    public NetworkEdge(NetworkNode from, NetworkNode to) {
        this.from = from;
        this.to = to;
        Random random = new Random();
        this.weight = random.nextDouble();
    }

    public NetworkNode getFrom() {
        return from;
    }

    public NetworkNode getTo() {
        return to;
    }

    public double getWeight() {
        return weight;
    }

    public Line getLine() {
        return line;
    }

    public void setLine(Line line) {
        this.line = line;
    }
}
