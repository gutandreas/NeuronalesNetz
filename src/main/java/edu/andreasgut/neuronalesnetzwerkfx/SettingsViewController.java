package edu.andreasgut.neuronalesnetzwerkfx;

import edu.andreasgut.neuronalesnetzwerkfx.core.NeuralNetwork;
import edu.andreasgut.neuronalesnetzwerkfx.imagetools.SourceImage;
import edu.andreasgut.neuronalesnetzwerkfx.view.ViewManager;
import javafx.fxml.FXML;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;

public class SettingsViewController {


    @FXML
    ToggleGroup resolution;

    @FXML
    ToggleGroup images;

    public void start(){
        String resolutionText = ((RadioButton) resolution.getSelectedToggle()).getText();
        String imagesText = ((RadioButton) images.getSelectedToggle()).getText();

        int targetWidth = 0;

        switch (resolutionText) {
            case "Hoch":
                targetWidth = 20;
            case "Mittel":
                targetWidth = 10;
            case "Tief":
                targetWidth = 5;
        }

        HelloController helloController = ViewManager.changeSceneInCurrentStageWithFXML("hello-view.fxml").getController();
        SourceImage sourceImage = new SourceImage("/images/numbers/0/img_69.jpg", targetWidth);
        helloController.loadNewImage(sourceImage);
        NeuralNetwork neuralNetwork = new NeuralNetwork(sourceImage.getNumberOfPixelForNeuralNetwork(), 4, 6, 10);
        neuralNetwork.startCalculations(sourceImage.getImageAs1DArray());
        helloController.initializeGUI(neuralNetwork);

    }

}
