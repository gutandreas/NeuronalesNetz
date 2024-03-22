package edu.andreasgut.neuronalesnetzwerkfx.core;

import javafx.scene.paint.Color;
import javafx.scene.shape.Line;

import java.util.Random;

public class NetworkEdge {

    private final NetworkNode from;
    private final NetworkNode to;
    private double weight;
    private Line line;


    public NetworkEdge(NetworkNode from, NetworkNode to) {
        this.from = from;
        this.to = to;
        Random random = new Random();
        this.weight = random.nextDouble() * 0.7;
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

    public void setWeight(double weight) {
        if (weight > 0){
            this.weight = weight;
        }
        else {
            this.weight = 0;
        }

    }

    public Line getLine() {
        return line;
    }

    public void setLine(Line line) {
        this.line = line;
    }

    public void updateLineWeightGraphic(){
        line.setStrokeWidth(Math.min(5, weight*3));
    }

    public void updateLineColor(){
        line.setStroke(Color.rgb(defineColor( 255), defineColor(255), defineColor(100)));
    }

    private int defineColor(int maxValue) {
        double percentage = getFrom().getOutput();

        double colorValue = percentage * maxValue;

        colorValue = colorValue > 255 ? 255 : colorValue;
        colorValue = colorValue < 0 ? 0 : colorValue;

        return (int) Math.round(colorValue);
    }
}
