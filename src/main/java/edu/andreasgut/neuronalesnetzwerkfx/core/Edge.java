package edu.andreasgut.neuronalesnetzwerkfx.core;

import java.util.Random;

public class Edge {

    private Node from;
    private Node to;
    private double weight;


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
}
