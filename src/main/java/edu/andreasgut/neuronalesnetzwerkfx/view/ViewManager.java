package edu.andreasgut.neuronalesnetzwerkfx.view;

import edu.andreasgut.neuronalesnetzwerkfx.NeuralNetworkApplication;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class ViewManager {

    static Map<String, Stage> stageMap = new HashMap<>();
    static Map<String, Scene> sceneMap = new HashMap<>();
    static Stage currentStage;
    static Scene currentScene;
    static Map<Scene, FXMLLoader> sceneFXMLLoaderMap = new HashMap<>();


    public static FXMLLoader getFXMLLoaderByScene(Scene scene){
        return sceneFXMLLoaderMap.get(scene);
    }

    public static void addDefaultStage(String stageName){
        Stage defaultStage = new Stage();
        defaultStage.show();
        stageMap.put(stageName, defaultStage);
    }

    public static FXMLLoader addStageWithFXML(String stageTitle, String stageName, String fxmlFileName){
        FXMLLoader fxmlLoader = new FXMLLoader(NeuralNetworkApplication.class.getResource(fxmlFileName));
        Stage stage = new Stage();
        stage.setTitle(stageTitle);
        stageMap.put(stageName, stage);
        try {
            Scene scene = new Scene(fxmlLoader.load(), 1200, 800);
            scene.getStylesheets().add(ViewManager.class.getResource("/styles/style.css").toExternalForm());
            stage.setScene(scene);
            sceneFXMLLoaderMap.put(scene, fxmlLoader);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        if (currentStage == null){
            currentStage = stage;
            currentScene = stage.getScene();
        }
        stage.show();

        return fxmlLoader;
    }

    public static FXMLLoader changeSceneInCurrentStageWithFXML(String fxmlFileName){
        FXMLLoader fxmlLoader = new FXMLLoader(NeuralNetworkApplication.class.getResource(fxmlFileName));
        try {
            Scene scene = new Scene(fxmlLoader.load(), 1000, 800);
            scene.getStylesheets().add(ViewManager.class.getResource("/styles/style.css").toExternalForm());
            currentStage.setScene(scene);
            sceneFXMLLoaderMap.put(scene, fxmlLoader);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return fxmlLoader;

    }


}
