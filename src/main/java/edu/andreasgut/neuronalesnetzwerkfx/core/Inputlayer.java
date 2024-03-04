package edu.andreasgut.neuronalesnetzwerkfx.core;

import javafx.scene.paint.Color;

public class Inputlayer extends Layer {

    private Layer nextLayer;

    public Inputlayer(int numberOfNodes) {

        for (int i = 0; i < numberOfNodes; i++){
            Node node = new Node();
            getNodes().add(node);
        }
    }

    public void feedDataToInputNodes(double[] inputdata){
        for (int i = 0; i < getNumberOfNodes(); i++){
            Node currentNode = getNodes().get(i);
            currentNode.setOutput(inputdata[i]);
        }
        nextLayer.activateLayer();
    }

    public void activateLayer(){
        //muss Methode der Oberklasse überschreiben
        System.out.println("Soll nicht aufgerufen werden");
    }


    public void setNextLayer(Layer nextLayer) {
        this.nextLayer = nextLayer;
    }




}
