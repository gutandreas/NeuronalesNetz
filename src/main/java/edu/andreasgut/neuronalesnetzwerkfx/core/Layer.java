package edu.andreasgut.neuronalesnetzwerkfx.core;

import java.util.LinkedList;

public abstract class Layer {



    private LinkedList<NetworkNode> nodes = new LinkedList<>();
    private double[] outputs;
    private NeuralNetwork neuralNetwork;

    public Layer(NeuralNetwork neuralNetwork) {
        this.neuralNetwork = neuralNetwork;
    }

    public double[] getOutputs() {
        return outputs;
    }

    abstract public void setNextLayer(Layer connectableLayer);

    abstract public void activateLayer();

    public LinkedList<NetworkNode> getNodes() {
        return nodes;
    }

    public int getNumberOfNodes(){
        return nodes.size();
    }

    public void adjustWeigths(NetworkNode correctNode, double learningRate, boolean outputLayer) {

        for (NetworkNode node : getNodes()) {
            for (NetworkEdge inputEdge : node.getInputEdges()) {


                // AnpassungGewichtZwischenNextLayerIundPreviousLayerJ = lerningrate * DeltaI * OutputJ
                // Output: DeltaI = sigmoidDerivation(input) * (target - output)
                //                = sigmoid(input) * (1 - sigmoid(input)) * (target - output)
                //                = output * (1 - output) * (target - output)

                if (outputLayer) {
                    double target;
                    double sigmoidDerivation = node.getOutput() * (1 - node.getOutput());
                    if (node == correctNode){
                        target = 1;
                    }
                    else {
                        target = 0.2;
                    }
                    double error = target - node.getOutput();
                    node.setDelta(sigmoidDerivation * error);
                } else {
                    double deltaSum = 0;
                    for (NetworkEdge outputEdge : node.getOutputEdges()) {
                        deltaSum += outputEdge.getTo().getDelta() * outputEdge.getWeight();
                    }
                    node.setDelta(deltaSum);
                }
                double correctedWeight = inputEdge.getWeight() + learningRate * node.getDelta() * inputEdge.getFrom().getOutput();
                inputEdge.setWeight(correctedWeight);
                inputEdge.updateLineWeightGraphic();
               /* if (neuralNetwork.getPreviousLayer(this) != null){
                    neuralNetwork.getPreviousLayer(this).learn(node, learningRate, false);
                };*/

            }
        }
    }
}
