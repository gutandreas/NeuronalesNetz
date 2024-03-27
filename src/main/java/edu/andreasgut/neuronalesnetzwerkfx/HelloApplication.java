package edu.andreasgut.neuronalesnetzwerkfx;

import edu.andreasgut.neuronalesnetzwerkfx.core.*;
import edu.andreasgut.neuronalesnetzwerkfx.imagetools.SourceImage;
import edu.andreasgut.neuronalesnetzwerkfx.view.ViewManager;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class HelloApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {

        //HelloController helloController = ViewManager.addStageWithFXML("Neuronales Netz", "mainStage", "hello-view.fxml").getController();
        SettingsViewController settingsViewController = ViewManager.addStageWithFXML("Neuronales Netz", "mainStage", "settings-view.fxml").getController();



        SourceImage sourceImage = new SourceImage("/images/numbers/0/img_69.jpg", 20);

        NeuralNetwork neuralNetwork = new NeuralNetwork(sourceImage.getNumberOfPixelForNeuralNetwork(), 2, 4, 10);
        neuralNetwork.startCalculations(sourceImage.getImageAs1DArray());


        //helloController.initializeGUI(neuralNetwork);
        //helloController.loadNewImage(sourceImage);
        //train(neuralNetwork);
    }

    public static void main(String[] args) {
        launch();
    }

    public static void train(NeuralNetwork neuralNetwork){
        TrainingObject[] trainingObjects = new TrainingObject[10000];

        for (int i = 0; i < trainingObjects.length; i++){
            trainingObjects[i] = new TrainingObject(neuralNetwork.getInputlayer().getNumberOfNodes());
        }

        Outputlayer outputlayer = neuralNetwork.getOutputlayer();

        for (TrainingObject trainingObject : trainingObjects){
            int maxIndex = trainingObject.getMaxIndex();
            NetworkNode node = outputlayer.getNodes().get(maxIndex);
            outputlayer.learn(node, 0.01, true);
        }
    }
}