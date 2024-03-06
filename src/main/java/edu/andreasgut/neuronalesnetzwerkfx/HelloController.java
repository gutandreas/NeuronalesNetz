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


    public void initializeGui(NeuralNetwork neuralNetwork){
        initializeAnchorPane(neuralNetwork);
        initializeLines(neuralNetwork);
    }

    private void initializeAnchorPane(NeuralNetwork neuralNetwork){

        int layerNumber = 0;


        initializeLayerinAnchorPane(neuralNetwork, neuralNetwork.getInputlayer(), layerNumber,0, 0, 255);
        layerNumber++;

        for (Hiddenlayer hiddenlayer : neuralNetwork.getHiddenlayers()){

            initializeLayerinAnchorPane(neuralNetwork, hiddenlayer, layerNumber, 255, 0, 0);
            layerNumber++;

        }

        initializeLayerinAnchorPane(neuralNetwork, neuralNetwork.getOutputlayer(), layerNumber, 0, 255, 0);

    }

    private void initializeLayerinAnchorPane(NeuralNetwork neuralNetwork, Layer layer,  int layerNumber, int red, int green, int blue){


        double deltaY = layerAnchorPane.getPrefHeight() / layer.getNumberOfNodes() + 1;
        double deltaX = layerAnchorPane.getPrefWidth() / neuralNetwork.getHiddenlayers().size() + 2;
        double x = deltaX * layerNumber;
        double y = deltaY;


        for (NetworkNode node : layer.getNodes()){
            DecimalFormat df = new DecimalFormat("#.0" + "0".repeat(2 - 1));
            String roundedValue = df.format(node.getOutput());
            Circle circle = new Circle(circleRadius, Color.rgb(red, green, blue, node.getOutput()));
            Text text = new Text(roundedValue);
            Group group = new Group();
            group.getChildren().add(circle);
            group.getChildren().add(text);
            layerAnchorPane.getChildren().add(group);
            node.setGraphicGroup(group);
            AnchorPane.setTopAnchor(group, y);
            AnchorPane.setLeftAnchor(group, x);

            y += deltaY;
        }
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
                    double weight = edge.getWeight();
                    line.setStrokeWidth(weight*3);
                    line.setStroke(Color.rgb((int) (weight*255), (int) (weight*255), (int) (weight*100)));

                    Text textWeight = new Text();

                    line.setOnMouseEntered(event -> {
                        textWeight.setText(edge.getWeight() + "");
                        layerAnchorPane.getChildren().add(textWeight);
                    });
                    line.setOnMouseExited(event -> {
                        layerAnchorPane.getChildren().remove(textWeight);
                    });

                    layerAnchorPane.getChildren().add(line);
                }
            };
        }



    }






    @FXML
    protected void onHelloButtonClick() {
        welcomeText.setText("Welcome to JavaFX Application!");
    }
}
