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
        this.weight = random.nextDouble() * 2 - 1;
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
        line.setStrokeWidth(Math.min(5, Math.abs(weight*3)));
    }

    public void updateLineColor(){
        if (getWeight() > 0){
            line.setStroke(Color.rgb(defineColor( 255), defineColor(255), defineColor(0)));
        }
        else {
            line.setStroke(Color.rgb(defineColor( 255), defineColor(0), defineColor(0)));
        }
    }

    private int defineColor(int maxValue) {
        double percentage = Math.abs(getFrom().getOutput());

        double colorValue = percentage * maxValue;

        colorValue = colorValue > 255 ? 255 : colorValue;

        return (int) Math.round(colorValue);
    }

    @Override
    public String toString() {
        return "NetworkEdge{" +
                "from=" + from +
                ", to=" + to +
                ", weight=" + weight +
                ", line=" + line +
                '}';
    }
}
