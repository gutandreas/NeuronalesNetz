package edu.andreasgut.neuronalesnetzwerkfx;

import edu.andreasgut.neuronalesnetzwerkfx.core.Hiddenlayer;
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

    private final LinkedList<Node> inputNodes = new LinkedList<>();
    private final LinkedList<LinkedList<Node>> hiddenNodesLists = new LinkedList<>();
    private final LinkedList<Node> outputNodes = new LinkedList<>();

    public void initializeGui(NeuralNetwork neuralNetwork){
        initializeGridPane(neuralNetwork);
        initializeInputLayer(neuralNetwork);
        initializeHiddenLayers(neuralNetwork);
        initializeOutputLayer(neuralNetwork);
        initializeLines();

    }

    private void initializeGridPane(NeuralNetwork neuralNetwork){
        //layerGridPane.setGridLinesVisible(true);
        layerGridPane.setAlignment(Pos.CENTER);
        layerGridPane.setHgap(30);
        layerGridPane.setVgap(30);
        layerGridPane.getColumnConstraints().clear();
        layerGridPane.getRowConstraints().clear();
        int numberOfLayers = 1 + neuralNetwork.getHiddenlayers().size() + 1;

        for (int i = 0; i < numberOfLayers; i++){
            layerGridPane.getColumnConstraints().add(new ColumnConstraints());
        }

        int maxNodeNumber = Math.max(Math.max(neuralNetwork.getInputlayer().getNumberOfNodes(),
                        neuralNetwork.getHiddenlayers().get(0).getNumberOfNodes()),
                neuralNetwork.getOutputlayer().getNumberOfNodes() );

        for (int i = 0; i < maxNodeNumber; i++){
            layerGridPane.getRowConstraints().add(new RowConstraints());
        }

        System.out.println("Gridpane erstellt mit " + layerGridPane.getColumnConstraints().size() + " Spalten und "
                + layerGridPane.getRowConstraints().size() + " Zeilen");
    }


    public void initializeInputLayer(NeuralNetwork neuralNetwork) {

        inputLayerVBox.setSpacing(20);
        int row = 0;

        for (double d : neuralNetwork.getInputlayer().getOutputs()) {
            Label label = new Label(d + "");
            Rectangle rectangle = new Rectangle(20, 20, Color.rgb(0, 0, 255, d));
            label.setGraphic(rectangle);
            layerGridPane.add(label, 0, row);
            row++;
        }
    }

    public void initializeHiddenLayers(NeuralNetwork neuralNetwork){
        int column = 1;
        for (Hiddenlayer hiddenlayer : neuralNetwork.getHiddenlayers()){

            int row = 0;
            for (double d : hiddenlayer.getOutputs()){

                DecimalFormat df = new DecimalFormat("#.0" + "0".repeat(2 - 1));
                String roundedValue = df.format(d);
                Label label = new Label(roundedValue + "");

                Circle circle = new Circle(5, Color.rgb(255, 0, 0, d));
                label.setGraphic(circle);


                layerGridPane.add(label, column, row);
                row++;

            }
            column++;


        }
    }

    public void initializeOutputLayer(NeuralNetwork neuralNetwork){

        int row = 0;
        int column = neuralNetwork.getHiddenlayers().size() + 1;

        for (double d : neuralNetwork.getOutputlayer().getOutputs()) {
            Label label = new Label(d + "");
            Rectangle rectangle = new Rectangle(20, 20, Color.rgb(0, 255, 0, d));
            label.setGraphic(rectangle);
            layerGridPane.add(label, column, row);
            row++;
        }


    }

    public void initializeLines() {


    }



    @FXML
    protected void onHelloButtonClick() {
        welcomeText.setText("Welcome to JavaFX Application!");
    }
}
