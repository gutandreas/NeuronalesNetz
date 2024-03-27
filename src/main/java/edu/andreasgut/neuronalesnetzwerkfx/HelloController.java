package edu.andreasgut.neuronalesnetzwerkfx;

import edu.andreasgut.neuronalesnetzwerkfx.core.*;

import edu.andreasgut.neuronalesnetzwerkfx.imagetools.SourceImage;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import java.io.File;
import java.net.URISyntaxException;
import java.nio.file.Paths;
import java.text.DecimalFormat;
import java.util.Arrays;
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

    @FXML
    private AnchorPane imageAnchorPane;

    @FXML
    private Button einzelbildButton;

    private int circleRadius = 10;

    private NeuralNetwork neuralNetwork;



    public void initializeGUI(NeuralNetwork neuralNetwork){
        this.neuralNetwork = neuralNetwork;
        initializeLayerAnchorPane(neuralNetwork);
        initializeLines(neuralNetwork);
        updateGUI();
    }

    public void updateGUI(){
        initializeLayerAnchorPane(neuralNetwork);
        neuralNetwork.updateLineColorOfAllEdges();
    }

    public void loadNewImage(SourceImage sourceImage){
        Canvas canvas = sourceImage.getImageAsCanvas();
        imageAnchorPane.getChildren().add(canvas);
    }


    private void initializeLayerAnchorPane(NeuralNetwork neuralNetwork){

        int layerNumber = 0;


        initializeLayerInAnchorPane(neuralNetwork, neuralNetwork.getInputlayer(), layerNumber,0, 0, 255, false);
        layerNumber++;

        for (Hiddenlayer hiddenlayer : neuralNetwork.getHiddenlayers()){

            initializeLayerInAnchorPane(neuralNetwork, hiddenlayer, layerNumber, 255, 0, 0, false);
            layerNumber++;

        }

        initializeLayerInAnchorPane(neuralNetwork, neuralNetwork.getOutputlayer(), layerNumber, 0, 255, 0, true);

    }

    private void initializeLayerInAnchorPane(NeuralNetwork neuralNetwork, Layer layer, int layerNumber, int red, int green, int blue, boolean outputLayer){

        double deltaY = layerAnchorPane.getPrefHeight() / (layer.getNumberOfNodes()+1);
        double deltaX = layerAnchorPane.getPrefWidth() / neuralNetwork.getNumberOfLayers();
        double x = deltaX * layerNumber + circleRadius;
        double y = deltaY + circleRadius;


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
                activateLearningClick(node, 0.01);
            }

            y += deltaY;
        }
    }

    private void activateLearningClick(NetworkNode node, double learningRate){
        node.getGraphicGroup().setOnMouseClicked(event -> {

            neuralNetwork.getOutputlayer().learn(node, learningRate, true);
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
                    edge.updateLineWeightGraphic();

                    Text textWeight = new Text();

                    if (line.getOnMouseEntered() == null) {
                        line.setOnMouseEntered(event -> {
                            line.setStrokeWidth(line.getStrokeWidth() * 3);
                            line.setStroke(Color.rgb(0, 255, 255));
                            textWeight.setText(edge.getWeight() + "");
                            layerAnchorPane.getChildren().add(textWeight);
                        });
                        line.setOnMouseExited(event -> {
                            edge.updateLineWeightGraphic();
                            edge.updateLineColor();
                            layerAnchorPane.getChildren().remove(textWeight);

                        });
                    }

                    layerAnchorPane.getChildren().add(line);
                }
            };
        }

    }



    public void loadImage(){

        FileChooser fileChooser = new FileChooser();

        File selectedFile = fileChooser.showOpenDialog(new Stage());
        System.out.println(getPathFromResourceFolder(selectedFile.getAbsolutePath()));
        SourceImage sourceImage = new SourceImage(getPathFromResourceFolder(selectedFile.getAbsolutePath()), 20);
        loadNewImage(sourceImage);
        neuralNetwork.startCalculations(sourceImage.getImageAs1DArray());
        initializeLayerAnchorPane(neuralNetwork);

        System.out.println(selectedFile);
    }

    private String getPathFromResourceFolder(String path){
        String[] ebenen = path.split("/");
        String[] letzteVierEbenen = Arrays.copyOfRange(ebenen, ebenen.length - 4, ebenen.length);
        String relativePath = "";
        for (String s: letzteVierEbenen) {

            relativePath += ("/" + s);


        }
        return relativePath;
    }

}
