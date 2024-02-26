package edu.andreasgut.neuronalesnetzwerkfx;

import edu.andreasgut.neuronalesnetzwerkfx.core.Hiddenlayer;
import edu.andreasgut.neuronalesnetzwerkfx.core.NeuralNetwork;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

import java.io.IOException;

public class HelloApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("hello-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 600, 400);
        HelloController helloController = fxmlLoader.getController();
        stage.setTitle("Hello!");

        NeuralNetwork neuralNetwork = new NeuralNetwork(5, 3, 5, 2);
        neuralNetwork.activate(new double[]{0.3, 0.5, 0.2, 0.4, 0.9});

        helloController.initializeInputLayer(neuralNetwork);
        helloController.initializeHiddenLayers(neuralNetwork);
        helloController.initializeOutputLayer(neuralNetwork);

        // Festlegen der Szene für die Bühne und Anzeigen der Bühne
        stage.setScene(scene);
        stage.setTitle("JavaFX Shapes Example");
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}