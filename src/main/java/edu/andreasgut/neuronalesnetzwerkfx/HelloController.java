package edu.andreasgut.neuronalesnetzwerkfx;

import edu.andreasgut.neuronalesnetzwerkfx.core.*;
import javafx.fxml.FXML;
import javafx.scene.Group;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.text.Text;

import java.text.DecimalFormat;
import java.util.LinkedList;

public class HelloController {
    @FXML
    private Label welcomeText;

    @FXML
    private VBox windowVBox;


    @FXML
    private HBox mainHBox;

    @FXML
    private VBox inputLayerVBox;

    @FXML
    private HBox hiddenLayersHBox;

    @FXML
    private VBox outputLayerVBox;

    @FXML
    private GridPane layerGridPane;

    @FXML
    private AnchorPane layerAnchorPane;

    private int circleRadius = 10;

    private NeuralNetwork neuralNetwork;
    private double[] inputs = new double[]{0.3, 0.5, 0.2, 0.4, 0.9};


    public void updateGui(NeuralNetwork neuralNetwork){
        this.neuralNetwork = neuralNetwork;
        initializeAnchorPane(neuralNetwork);
        initializeLines(neuralNetwork);
    }

    private void initializeAnchorPane(NeuralNetwork neuralNetwork){

        int layerNumber = 0;


        initializeLayerinAnchorPane(neuralNetwork, neuralNetwork.getInputlayer(), layerNumber,0, 0, 255, false);
        layerNumber++;

        for (Hiddenlayer hiddenlayer : neuralNetwork.getHiddenlayers()){

            initializeLayerinAnchorPane(neuralNetwork, hiddenlayer, layerNumber, 255, 0, 0, false);
            layerNumber++;

        }

        initializeLayerinAnchorPane(neuralNetwork, neuralNetwork.getOutputlayer(), layerNumber, 0, 255, 0, true);

    }

    private void initializeLayerinAnchorPane(NeuralNetwork neuralNetwork, Layer layer,  int layerNumber, int red, int green, int blue, boolean outputLayer){


        double deltaY = layerAnchorPane.getPrefHeight() / layer.getNumberOfNodes() + 1;
        double deltaX = layerAnchorPane.getPrefWidth() / neuralNetwork.getHiddenlayers().size() + 2;
        double x = deltaX * layerNumber;
        double y = deltaY;


        for (NetworkNode node : layer.getNodes()){
            DecimalFormat df = new DecimalFormat("#.0" + "0".repeat(2 - 1));
            String roundedValue = df.format(node.getOutput());
            if (node.getGraphicGroup() != null){
                layerAnchorPane.getChildren().remove(node.getGraphicGroup());
            }
            Circle circle = new Circle(circleRadius, Color.rgb(red, green, blue, node.getOutput()));
            Text text = new Text(roundedValue);
            node.updateNodeGraphic(circle, text);
            layerAnchorPane.getChildren().add(node.getGraphicGroup());
            AnchorPane.setTopAnchor(node.getGraphicGroup(), y);
            AnchorPane.setLeftAnchor(node.getGraphicGroup(), x);
            if (outputLayer){
                activateLearningClick(layer, node, 0.1);
            }

            y += deltaY;
        }
    }

    private void activateLearningClick(Layer layer, NetworkNode node, double learningRate){
        node.getGraphicGroup().setOnMouseClicked(event -> {
            for (NetworkNode tempNode : layer.getNodes()){
                if (node == tempNode){
                    node.learn(1, learningRate);
                }
                else {
                    node.learn(0, learningRate);
                }
            }


        });

    }

    private void initializeLines(NeuralNetwork neuralNetwork){

        LinkedList<LinkedList<NetworkNode>> completeList = new LinkedList<>();

        completeList.add(neuralNetwork.getInputlayer().getNodes());
        for (int i = 0; i < neuralNetwork.getHiddenlayers().size(); i++){
            completeList.add(neuralNetwork.getHiddenlayers().get(i).getNodes());
        }
        completeList.add(neuralNetwork.getOutputlayer().getNodes());

        for (int i = 0; i < completeList.size()-1; i++){
            for (NetworkNode startNode : completeList.get(i)){
                for (NetworkNode endNode : completeList.get(i+1)){
                    double startX = AnchorPane.getLeftAnchor(startNode.getGraphicGroup()) + startNode.getGraphicGroup().getTranslateX() + circleRadius;
                    double startY = AnchorPane.getTopAnchor(startNode.getGraphicGroup()) + startNode.getGraphicGroup().getTranslateY() + circleRadius;
                    double endX = AnchorPane.getLeftAnchor(endNode.getGraphicGroup()) + endNode.getGraphicGroup().getTranslateX() + circleRadius;
                    double endY = AnchorPane.getTopAnchor(endNode.getGraphicGroup()) + endNode.getGraphicGroup().getTranslateY() + circleRadius;

                    Line line = new Line(startX, startY, endX, endY);

                    int indexOfEdge = completeList.get(i+1).indexOf(endNode);
                    NetworkEdge edge = startNode.getOutputEdges().get(indexOfEdge);
                    edge.setLine(line);
                    edge.updateLineGraphic();

                    Text textWeight = new Text();

                    line.setOnMouseEntered(event -> {
                        line.setStrokeWidth(line.getStrokeWidth()*3);
                        line.setStroke(Color.rgb(0, 255, 255));
                        textWeight.setText(edge.getWeight() + "");
                        layerAnchorPane.getChildren().add(textWeight);
                    });
                    line.setOnMouseExited(event -> {
                        edge.updateLineGraphic();
                        layerAnchorPane.getChildren().remove(textWeight);

                    });

                    layerAnchorPane.getChildren().add(line);
                }
            };
        }



    }






    @FXML
    protected void onHelloButtonClick() {
        neuralNetwork.startCalculations(inputs);
        updateGui(neuralNetwork);
    }
}
