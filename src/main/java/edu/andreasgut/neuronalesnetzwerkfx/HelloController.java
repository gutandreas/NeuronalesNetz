package edu.andreasgut.neuronalesnetzwerkfx;

import edu.andreasgut.neuronalesnetzwerkfx.core.Hiddenlayer;
import edu.andreasgut.neuronalesnetzwerkfx.core.Layer;
import edu.andreasgut.neuronalesnetzwerkfx.core.NeuralNetwork;
import javafx.fxml.FXML;
import javafx.geometry.Bounds;
import javafx.geometry.Insets;
import javafx.geometry.Point2D;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import org.controlsfx.control.spreadsheet.Grid;

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

    private final LinkedList<Node> inputNodes = new LinkedList<>();
    private final LinkedList<LinkedList<Node>> hiddenNodesLists = new LinkedList<>();
    private final LinkedList<Node> outputNodes = new LinkedList<>();
    Node nodes[][] = new Node[100][100];

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


        for (edu.andreasgut.neuronalesnetzwerkfx.core.Node node : layer.getNodes()){
            DecimalFormat df = new DecimalFormat("#.0" + "0".repeat(2 - 1));
            String roundedValue = df.format(node.getOutput());
            Label label = new Label(roundedValue + "");
            Circle circle = new Circle(5, Color.rgb(red, green, blue, node.getOutput()));
            label.setGraphic(circle);
            node.setGraphicElement(circle, label);
            layerAnchorPane.getChildren().add(label);

            AnchorPane.setTopAnchor(label, y);
            AnchorPane.setLeftAnchor(label, x);

            y += deltaY;
        }
    }

    private void initializeLines(NeuralNetwork neuralNetwork){
        for (edu.andreasgut.neuronalesnetzwerkfx.core.Node startNode : neuralNetwork.getInputlayer().getNodes()){
            System.out.println("X: " + AnchorPane.getTopAnchor(startNode.getLabel()));
            for (edu.andreasgut.neuronalesnetzwerkfx.core.Node endNode : neuralNetwork.getHiddenlayers().get(0).getNodes()){
                Line line = new Line(AnchorPane.getLeftAnchor(startNode.getLabel()),
                        AnchorPane.getTopAnchor(startNode.getLabel()),
                        AnchorPane.getLeftAnchor(endNode.getLabel()),
                        AnchorPane.getTopAnchor(endNode.getLabel()));
                layerAnchorPane.getChildren().add(line);
            }
        };
    }

    private void addToArray(int row, int column, Node node){
        nodes[row][column] = node;
    }





    @FXML
    protected void onHelloButtonClick() {
        welcomeText.setText("Welcome to JavaFX Application!");
    }
}
