package edu.andreasgut.neuronalesnetzwerkfx.core;

public class Hiddenlayer extends Layer {


    private double[][] weights;
    private Layer previousLayer;
    private Layer nextLayer;
    private double[] outputs;


    public Hiddenlayer(int numberOfNodes, Layer previousLayer, NeuralNetwork neuralNetwork){
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
            currentNode.calculateOutput();
        }
        nextLayer.activateLayer();
    }



    public double[] getOutputs() {
        return outputs;
    }

    public void setNextLayer(Layer nextConnectableLayer) {
        this.nextLayer = nextConnectableLayer;
    }

    @Override
    public String toString(){
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < weights.length; i++){
            stringBuilder.append("\n");
            for (int j = 0; j < weights[0].length; j++){
                stringBuilder.append(weights[i][j]);
                stringBuilder.append("\t\t");
            }
        }

        return stringBuilder.toString();
    }
}
