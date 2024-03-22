package edu.andreasgut.neuronalesnetzwerkfx.core;

import java.util.LinkedList;

public class NeuralNetwork {

    Inputlayer inputlayer;
    LinkedList<Hiddenlayer> hiddenlayers = new LinkedList<>();
    Outputlayer outputlayer;

    public NeuralNetwork(int numberOfInputNodes, int numberOfHiddenLayers, int numberOfHiddenLayerNodes, int numberOfOutputNodes) {
        this.inputlayer = new Inputlayer(numberOfInputNodes, this);
        Layer previousLayer = inputlayer;
        for (int i = 0; i < numberOfHiddenLayers; i++){
            Hiddenlayer tempHiddenLayer = new Hiddenlayer(numberOfHiddenLayerNodes, previousLayer, this);
            hiddenlayers.add(tempHiddenLayer);
            previousLayer = tempHiddenLayer;
        }
        this.outputlayer = new Outputlayer(numberOfOutputNodes, previousLayer, this);
    }

    public void startCalculations(double[] inputs){
        int inputLength = inputs.length;
        int inputNodes = inputlayer.getNumberOfNodes();
        if (inputLength != inputNodes){
            System.out.println("Fehler. Die Anzahl der Inputwerte Stimmen nicht mit der Anzahl Knoten des Inputlayers überein.");
            System.out.println("Die Inputlänge beträgt " + inputLength + " und die Anzahl Knoten im Inputlayer beträgt " + inputNodes + ".");
        } else {
            inputlayer.feedDataToInputNodes(inputs);
        }
    }

    public Inputlayer getInputlayer() {
        return inputlayer;
    }

    public LinkedList<Hiddenlayer> getHiddenlayers() {
        return hiddenlayers;
    }

    public Outputlayer getOutputlayer() {
        return outputlayer;
    }

    public int getNumberOfLayers(){
        return hiddenlayers.size() + 2;
    }

    public Layer getPreviousLayer(Layer currentLayer){
        for (Layer layer : getHiddenlayers()){
            if (layer == currentLayer){
                return layer;
            }
        }
        return inputlayer;
    }

    public int getMaxNumberOfNodePerLayer(){
        return Math.max(Math.max(inputlayer.getNumberOfNodes(), hiddenlayers.get(0).getNumberOfNodes()), outputlayer.getNumberOfNodes());
    }

    public void updateLineColorOfAllEdges(){
        for (NetworkEdge edge : getAllEdges()){
            edge.updateLineColor();
        }
    }

    public LinkedList<NetworkEdge> getAllEdges(){
        LinkedList<NetworkEdge> listOfEdges = new LinkedList<>();
        for (Layer layer : getAllLayers()){
            for (NetworkNode node : layer.getNodes()){
                for (NetworkEdge edge : node.getOutputEdges()){
                    listOfEdges.add(edge);
                }
            }
        }
        return listOfEdges;
    }

    public LinkedList<Layer> getAllLayers(){
        LinkedList<Layer> layers = new LinkedList<>();
        layers.add(inputlayer);
        for (Layer layer : hiddenlayers){
            layers.add(layer);
        }
        layers.add(outputlayer);

        return layers;
    }
}
