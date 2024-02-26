package edu.andreasgut.neuronalesnetzwerkfx;

import edu.andreasgut.neuronalesnetzwerkfx.core.Hiddenlayer;
import edu.andreasgut.neuronalesnetzwerkfx.core.NeuralNetwork;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;

import java.text.DecimalFormat;

public class HelloController {
    @FXML
    private Label welcomeText;

    @FXML
    private VBox inputLayerVBox;

    @FXML
    private HBox hiddenLayersHBox;

    @FXML
    private VBox outputLayerVBox;

    public void initializeInputLayer(NeuralNetwork neuralNetwork) {

        inputLayerVBox.setSpacing(20);

        for (double d : neuralNetwork.getInputlayer().getOutputs()) {
            Label label = new Label(d + "");
            Rectangle rectangle = new Rectangle(20, 20, Color.rgb(0, 0, 255, d));
            label.setGraphic(rectangle);
            inputLayerVBox.getChildren().add(label);
            inputLayerVBox.setAlignment(Pos.CENTER);
        }
    }

    public void initializeHiddenLayers(NeuralNetwork neuralNetwork){
        for (Hiddenlayer hiddenlayer : neuralNetwork.getHiddenlayers()){
            VBox vBox = new VBox();
            for (double d : hiddenlayer.getOutputs()){
                DecimalFormat df = new DecimalFormat("#.0" + "0".repeat(2 - 1));
                String roundedValue = df.format(d);
                Label label = new Label(roundedValue + "");
                Circle circle = new Circle(5, Color.rgb(255, 0, 0, d));
                label.setGraphic(circle);
                vBox.getChildren().add(label);
                vBox.setStyle("-fx-padding: 20px");
                vBox.setAlignment(Pos.CENTER);
            }
            hiddenLayersHBox.getChildren().add(vBox);
            hiddenLayersHBox.setAlignment(Pos.CENTER);


        }
    }

    public void initializeOutputLayer(NeuralNetwork neuralNetwork){

        outputLayerVBox.setSpacing(20);

        for (double d : neuralNetwork.getOutputlayer().getOutputs()) {

            Label label = new Label(d + "");
            Rectangle rectangle = new Rectangle(20, 20, Color.rgb(0, 255, 0, d));
            label.setGraphic(rectangle);
            outputLayerVBox.getChildren().add(label);
            outputLayerVBox.setAlignment(Pos.CENTER);
        }
    }

    @FXML
    protected void onHelloButtonClick() {
        welcomeText.setText("Welcome to JavaFX Application!");
    }
}
