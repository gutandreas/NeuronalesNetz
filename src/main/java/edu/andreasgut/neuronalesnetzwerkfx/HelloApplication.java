package edu.andreasgut.neuronalesnetzwerkfx;

import edu.andreasgut.neuronalesnetzwerkfx.core.NeuralNetwork;
import edu.andreasgut.neuronalesnetzwerkfx.core.Tools;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class HelloApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("hello-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 600, 400);
        HelloController helloController = fxmlLoader.getController();
        stage.setTitle("Hello!");

        NeuralNetwork neuralNetwork = new NeuralNetwork(3, 5, 7, 2);
        neuralNetwork.startCalculations(Tools.getRandomValues(neuralNetwork.getInputlayer().getNumberOfNodes()));

        helloController.initializeGUI(neuralNetwork);

        //helloController.test();

        // Festlegen der Szene für die Bühne und Anzeigen der Bühne
        stage.setScene(scene);
        stage.setTitle("JavaFX Shapes Example");
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}