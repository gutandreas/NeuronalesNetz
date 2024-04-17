package edu.andreasgut.neuronalesnetzwerkfx.core;

import java.util.LinkedList;
import java.util.Random;

public class NeuralNetwork {

    Inputlayer inputlayer;
    LinkedList<Hiddenlayer> hiddenlayers = new LinkedList<>();
    Outputlayer outputlayer;
    LinkedList<NetworkEdge> selectedEdges = new LinkedList<>();
    double error;

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

    public LinkedList<NetworkEdge> getSelectedEdges() {
        return selectedEdges;
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

    public void adjustWeightsOfSelectedEdgesRandomly(){
        System.out.println("Anzahl angepasster Gewichte: " + selectedEdges.size());
        Random random = new Random();
        for (NetworkEdge edge : selectedEdges){
            edge.setWeight(random.nextDouble() * 2 - 1);
        }
    }

    public void selectRandomEdges(double percentage){
        selectedEdges.clear();
        int numberOfEdgesToSelect = (int) Math.round(getAllEdges().size() * percentage / 100);
        System.out.println("Anzahl ausgewählter Kanten: " + numberOfEdgesToSelect);
        Random random = new Random();
        while (selectedEdges.size() < numberOfEdgesToSelect){
            NetworkEdge randomEdge = getAllEdges().get(random.nextInt(getAllEdges().size()));
            if (!selectedEdges.contains(randomEdge)){
                selectedEdges.add(randomEdge);
            }
        }
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

    public void train(double[][] inputs, int[] indexOfCorrectOutput){

        if (inputs.length != indexOfCorrectOutput.length){
            System.out.println("Anzahl Objekte stimmt nicht mit der Anzahl Indizes überein");
        }
        else {
            for (int i = 0; i < inputs.length; i++) {
                startCalculations(inputs[i]);
                double error = 0;
                for (int j = 0; j < getOutputlayer().getNumberOfNodes(); j++){
                    double target;
                    if (j == indexOfCorrectOutput[i]){
                        target = 1;
                    }
                    else {
                        target = 0;
                    }
                    error += Math.sqrt(target-getOutputlayer().getNodes().get(j).getOutput());
                }

            }

        }


    }
}
