package edu.andreasgut.neuronalesnetzwerkfx.core;

public class Outputlayer extends Layer {


    Layer previousLayer;



    public Outputlayer(int numberOfNodes, Layer previousLayer, NeuralNetwork neuralNetwork){
        super(neuralNetwork);
        this.previousLayer = previousLayer;
        previousLayer.setNextLayer(this);

        for (int i = 0; i < numberOfNodes; i++){
            NetworkNode nodeInThisLayer = new NetworkNode();
            getNodes().add(nodeInThisLayer);
            for (NetworkNode nodeInPreviousLayer : previousLayer.getNodes()){
                NetworkEdge edge = new NetworkEdge(nodeInPreviousLayer, nodeInThisLayer);
                nodeInPreviousLayer.addOutputEdge(edge);
                nodeInThisLayer.addInputEdge(edge);
            }
        }
    }

    public void activateLayer(){
        for (int i = 0; i < getNumberOfNodes(); i++){
            NetworkNode currentNode = getNodes().get(i);
            double output = Tools.sigmoid(currentNode.getInputEdges());
            currentNode.setOutput(output);
            currentNode.calculateOutput();
        }
    }

    @Override
    public void setNextLayer(Layer connectableLayer) {
        System.out.print("Outputlayer hat keinen nächsten ConnectableLayer");
    }





}
