package edu.andreasgut.neuronalesnetzwerkfx.core;

public class Outputlayer extends Layer {


    Layer previousLayer;



    public Outputlayer(int numberOfNodes, Layer previousLayer){
        this.previousLayer = previousLayer;
        previousLayer.setNextLayer(this);


        for (int i = 0; i < numberOfNodes; i++){
            Node nodeInThisLayer = new Node();
            getNodes().add(nodeInThisLayer);
            for (Node nodeInPreviousLayer : previousLayer.getNodes()){
                Edge edge = new Edge(nodeInPreviousLayer, nodeInThisLayer);
                nodeInPreviousLayer.addOutputEdge(edge);
                nodeInThisLayer.addInputEdge(edge);
            }
        }
    }

    public void activateLayer(){
        for (int i = 0; i < getNumberOfNodes(); i++){
            Node currentNode = getNodes().get(i);
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
