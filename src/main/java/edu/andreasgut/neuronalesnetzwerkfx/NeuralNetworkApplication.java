package edu.andreasgut.neuronalesnetzwerkfx;

import edu.andreasgut.neuronalesnetzwerkfx.core.*;
import edu.andreasgut.neuronalesnetzwerkfx.imagetools.SourceImage;
import edu.andreasgut.neuronalesnetzwerkfx.view.ViewManager;
import javafx.application.Application;
import javafx.stage.Stage;

import java.io.IOException;

public class NeuralNetworkApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {


        //NeuralNetworkController helloController = ViewManager.addStageWithFXML("Neuronales Netz", "mainStage", "view.fxml").getController();
        NeuralNetworkController controller = ViewManager.addStageWithFXML("Neuronales Netz", "mainStage", "view.fxml").getController();

        SourceImage sourceImage = new SourceImage("/images/default/default1.png", 6);
        controller.showImageInAnchorPane(sourceImage);
        NeuralNetwork neuralNetwork = new NeuralNetwork(sourceImage.getNumberOfPixelForNeuralNetwork(), 2, 15, 10);
        neuralNetwork.startCalculations(sourceImage.getImageAs1DArray());
        controller.initializeGUI(neuralNetwork);


        //helloController.initializeGUI(neuralNetwork);
        //helloController.loadNewImage(sourceImage);
        //train(neuralNetwork);
    }

    public static void main(String[] args) {
        launch();
    }


}