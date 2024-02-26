package edu.andreasgut.neuronalesnetzwerkfx;

import edu.andreasgut.neuronalesnetzwerkfx.core.NeuralNetwork;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;

public class HelloController {
    @FXML
    private Label welcomeText;

    @FXML
    private VBox inputLayerVBox = new VBox();


    public void generateInputLayer(NeuralNetwork neuralNetwork){
        for (int i = 0; i < neuralNetwork.getInputlayer().getOutputs().length; i++) {
            System.out.print("Test");
            inputLayerVBox.getChildren().add(new Rectangle(20, 20));

        }
    }

    @FXML
    protected void onHelloButtonClick() {
        welcomeText.setText("Welcome to JavaFX Application!");
    }
}