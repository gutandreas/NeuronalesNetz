package edu.andreasgut.neuronalesnetzwerkfx;

import edu.andreasgut.neuronalesnetzwerkfx.core.*;

import edu.andreasgut.neuronalesnetzwerkfx.imagetools.SourceImage;
import edu.andreasgut.neuronalesnetzwerkfx.view.TimelineManager;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.canvas.Canvas;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.text.Text;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Duration;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DecimalFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.LinkedList;

public class NeuralNetworkController {
    @FXML
    private Label welcomeText;

    @FXML
    private VBox windowVBox;


    @FXML
    private Label fileNameLabel;

    @FXML
    private Label directoryLabelTraining;

    @FXML
    private Label directoryLabelTesting;

    @FXML
    private Label resultLabel;

    @FXML
    private GridPane trainingGridPane;

    @FXML
    private HBox mainHBox;

    @FXML
    private VBox inputLayerVBox;

    @FXML
    private HBox hiddenLayersHBox;

    @FXML
    private VBox outputLayerVBox;

    @FXML
    private GridPane drawGridPane;

    @FXML
    private AnchorPane layerAnchorPane;

    @FXML
    private AnchorPane imageAnchorPane;

    @FXML
    private GridPane newNetworkSettingGridPane;

    @FXML
    private ToggleGroup newNetworkGroup;

    @FXML
    private RadioButton newNetworkRadio;


    @FXML
    private Button einzelbildButton;

    @FXML
    private MenuButton correctOutputMenu;

    private int circleRadius = 10;

    private NeuralNetwork neuralNetwork;

    private File currentSelectedFile;

    private File selectedDirectoryForTraining;

    private File selectedDirectoryForTesting;

    private int selectedCorrectOutput;

    double[][] drawGridPaneAsArray;

    @FXML
    private TextField exportNameTextfield;

    @FXML
    private XYChart evolutionaryErrorChart;

    @FXML
    private XYChart evolutionaryErrorChartAll;

    @FXML
    private XYChart gradientErrorChart;

    @FXML
    private XYChart gradientErrorChartAll;

    @FXML
    private PieChart correctnessChart;


    @FXML
    private Slider widthSlider;

    @FXML
    private Slider layerSlider;

    @FXML
    private Slider neuronsSlider;

    @FXML
    private Slider outputSlider;

    @FXML
    private Slider repetitionsEvolutionarySlider;

    @FXML
    private Slider repetitionsGradientSlider;

    @FXML
    private Slider percentSlider;

    @FXML
    private Slider learningRateSlider;

    @FXML
    private Button randomlyChangeButton;

    @FXML
    private Button exportButton;

    @FXML
    private Button importButton;


    public void initialize() {

        percentSlider.valueProperty().addListener((observable, oldValue, newValue) -> {
            updateGUI();
            neuralNetwork.selectRandomEdges(percentSlider.getValue());
            highlightSelectedEdges();
        });

    }

    public void initializeGUI(NeuralNetwork neuralNetwork){
        layerAnchorPane.getChildren().clear();
        this.neuralNetwork = neuralNetwork;
        initializeLayerAnchorPane(neuralNetwork);
        initializeLines(neuralNetwork);
        initializeDrawGridPane();
        updateGUI();
    }

    public void initializeDrawGridPane(){

        int dimension = (int) Math.sqrt(neuralNetwork.getInputlayer().getNumberOfNodes());
        drawGridPane.getChildren().clear();
        drawGridPaneAsArray = new double[dimension][dimension];

        drawGridPane.setOnMouseDragged(event -> {
            if (event.isPrimaryButtonDown()) {
                int colIndex = (int) (event.getY() / (200./dimension));
                int rowIndex = (int) (event.getX() / (200./dimension));


                if (colIndex >= 0 && colIndex < dimension && rowIndex >= 0 && rowIndex < dimension && drawGridPaneAsArray[colIndex][rowIndex] == 0) {
                    Pane pane = (Pane) drawGridPane.getChildren().get(rowIndex * dimension + colIndex);
                    pane.setStyle("-fx-background-color: " + "white" + ";");
                    drawGridPaneAsArray[colIndex][rowIndex] = 1;
                    neuralNetwork.startCalculations(Arrays.stream(drawGridPaneAsArray)
                            .flatMapToDouble(Arrays::stream)
                            .toArray());

                    resultLabel.setText("" + neuralNetwork.getIndexOfHighestOutputNode());
                    updateGUI();
                }

            }
        });

        for (int i = 0; i < dimension ; i++){
            for (int j = 0; j < dimension; j++) {
                Pane square = createSquare(dimension);
                drawGridPane.add(square, i, j);
            }
        }
    }

    public void resetDrawGridPane(){
        initializeDrawGridPane();
        neuralNetwork.startCalculations(Arrays.stream(drawGridPaneAsArray)
                .flatMapToDouble(Arrays::stream)
                .toArray());
        updateGUI();
    }


    private Pane createSquare(int numberOfSquaresInDimension) {
        Pane square = new Pane();
        square.setStyle("-fx-background-color: " + "black" + ";");
        square.setMinSize(200./numberOfSquaresInDimension, 200./numberOfSquaresInDimension); // Größe des Quadrats anpassen
        return square;
    }

    public void highlightSelectedEdges(){

        Platform.runLater(()->{

            TimelineManager.stopAllTimelines();
            for (NetworkEdge edge : neuralNetwork.getSelectedEdges()) {

                edge.getLine().setStroke(Color.BLUE);
                Timeline timeline = new Timeline(
                        new KeyFrame(Duration.ZERO, e -> edge.getLine().setStroke(Color.WHITE)),
                        new KeyFrame(Duration.seconds(0.3), e -> edge.resetLineColor()),
                        new KeyFrame(Duration.seconds(0.6), e -> edge.getLine().setStroke(Color.WHITE))

                );
                timeline.setCycleCount(Timeline.INDEFINITE); // Wiederholen der Animation unendlich oft
                timeline.play();
                TimelineManager.addTimeline(timeline);

            }

        });
    }

    public void stopHighlightingSelectedEdges(){
        TimelineManager.stopAllTimelines();
        for (NetworkEdge edge : neuralNetwork.getSelectedEdges()) {
            edge.resetLineColor();
        }

    }



    public void updateGUI(){
        initializeLayerAnchorPane(neuralNetwork);
        neuralNetwork.updateLineGraphicOfAllEdges();


    }

    public void showImageInAnchorPane(SourceImage sourceImage){
        Canvas canvas = sourceImage.getImageAsCanvas();
        imageAnchorPane.getChildren().add(canvas);
        sourceImage.getImageAsCanvas().setOnMouseClicked(event -> {
            neuralNetwork.startCalculations(sourceImage.getImageAs1DArray());
            resultLabel.setText(neuralNetwork.getIndexOfHighestOutputNode() + "");
            updateGUI();
        });
    }

    public void highlightMaxOutput(){
        NetworkNode highestNode = neuralNetwork.getOutputlayer().getNodes().get(neuralNetwork.getIndexOfHighestOutputNode());
        for (Node node : highestNode.getGraphicGroup().getChildren()) {
            if (node instanceof Circle) {
                Circle circle = (Circle) node;
                circle.setFill(Color.WHITE);
            }
        }
    }

    public void stopHilghlitingMaxOutput(){
        for (NetworkNode networkNode : neuralNetwork.getOutputlayer().getNodes()){
            networkNode.getGraphicGroup();
            for (Node node : networkNode.getGraphicGroup().getChildren()) {
                if (node instanceof Circle) {
                    Circle circle = (Circle) node;
                    circle.setFill(Color.rgb(0, 255, 0, networkNode.getOutput()));
                }
            }
        }

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

        double circleRadius;

        if (layer.getNumberOfNodes() > 40){
            circleRadius = 3;
        } else if (layer.getNumberOfNodes() > 30) {
            circleRadius = 5;
        } else {
            circleRadius = 10;
        }

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
            circle.setStroke(Color.GRAY);
            circle.setStrokeWidth(2);

            Text textNode = new Text();
            textNode.getStyleClass().add("node-number");
            circle.setOnMouseEntered(event -> {
                textNode.setText("Neuron\nOutput: " + node.getOutput());
                layerAnchorPane.getChildren().add(textNode);
                circle.setStroke(Color.WHITE);
            });

            circle.setOnMouseExited(event -> {
                layerAnchorPane.getChildren().remove(textNode);
                circle.setStroke(Color.GRAY);
            });
            Text text = new Text(roundedValue);
            node.updateNodeGraphic(circle, text);
            layerAnchorPane.getChildren().add(node.getGraphicGroup());
            AnchorPane.setTopAnchor(node.getGraphicGroup(), y);
            AnchorPane.setLeftAnchor(node.getGraphicGroup(), x);

            y += deltaY;
        }
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
                    textWeight.getStyleClass().add("node-number");

                    if (line.getOnMouseEntered() == null) {
                        line.setOnMouseEntered(event -> {
                            line.setStrokeWidth(line.getStrokeWidth() * 3);
                            line.setStroke(Color.rgb(0, 255, 255));
                            textWeight.setText("Gewicht:" + edge.getWeight() + "\nOutput: " + edge.getFrom().getOutput());
                            layerAnchorPane.getChildren().add(textWeight);
                        });
                        line.setOnMouseExited(event -> {
                            edge.updateLineWeightGraphic();
                            edge.resetLineColor();
                            layerAnchorPane.getChildren().remove(textWeight);

                        });
                    }

                    layerAnchorPane.getChildren().add(line);
                }
            };
        }

    }



    public void selectFile(){

        FileChooser fileChooser = new FileChooser();

        File currentDirectory = new File("src/main/resources/images/numbers");
        fileChooser.setInitialDirectory(currentDirectory);

        currentSelectedFile = fileChooser.showOpenDialog(new Stage());
        System.out.println(currentSelectedFile.getAbsolutePath());
        //System.out.println(SourceImage.getPathFromResourceFolder(currentSelectedFile.getAbsolutePath()));
        fileNameLabel.setText(currentSelectedFile.getName());


    }

    public void loadImage(){

        if (currentSelectedFile != null){
            SourceImage sourceImage;
            int width = (int) Math.sqrt(neuralNetwork.getInputlayer().getNumberOfNodes());
            sourceImage  = new SourceImage(currentSelectedFile.toURI().toString(), width);
            showImageInAnchorPane(sourceImage);
            neuralNetwork.startCalculations(sourceImage.getImageAs1DArray());
            resultLabel.setText(neuralNetwork.getIndexOfHighestOutputNode() + "");
            updateGUI();
        }
        else {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Keine Datei ausgewählt!");
            alert.setHeaderText("Keine Datei ausgewählt!");
            alert.setContentText("Wählen Sie zuerst eine Datei aus und laden Sie das Bild erneut.");
            alert.showAndWait();

        }

    }

    public void loadNetwork(){

        int width = (int) widthSlider.getValue();
        int hiddenLayers = (int) layerSlider.getValue() - 2;
        int nodesInHiddenLayer = (int) neuronsSlider.getValue();
        int numberOfOutputs = (int) outputSlider.getValue();
        neuralNetwork = new NeuralNetwork(width*width, hiddenLayers, nodesInHiddenLayer, numberOfOutputs);
        SourceImage sourceImage = new SourceImage("/images/default/default1.png", width);
        showImageInAnchorPane(sourceImage);
        neuralNetwork.startCalculations(sourceImage.getImageAs1DArray());
        initializeGUI(neuralNetwork);
    }

    public void selectDirectoryForTraining(){
        DirectoryChooser directoryChooser = new DirectoryChooser();
        directoryChooser.setTitle("Select Folder");

        selectedDirectoryForTraining = directoryChooser.showDialog(new Stage());

        System.out.println(selectedDirectoryForTraining.toURI());
        directoryLabelTraining.setText(selectedDirectoryForTraining.getName());
    }

    public void selectDirectoryForTesting(){
        DirectoryChooser directoryChooser = new DirectoryChooser();
        directoryChooser.setTitle("Select Folder");

        selectedDirectoryForTesting = directoryChooser.showDialog(new Stage());

        System.out.println(selectedDirectoryForTesting.toURI());
        directoryLabelTesting.setText(selectedDirectoryForTesting.getName());
    }

    public void startEvolutionaryTraining(){
        int repetitions = (int) repetitionsEvolutionarySlider.getValue();
        trainingGridPane.getChildren().clear();
        for (int i = 0; i < repetitions; i++){
            neuralNetwork.trainEvolutionary(selectedDirectoryForTraining);

            selectRandomEdges();
        }
        addErrorToErrorChart(evolutionaryErrorChart, evolutionaryErrorChartAll, repetitions, true);
        SourceImage sourceImage = new SourceImage("/images/default/default1.png", (int) Math.sqrt(neuralNetwork.getInputlayer().getNumberOfNodes()));
        showImageInAnchorPane(sourceImage);
        neuralNetwork.startCalculations(sourceImage.getImageAs1DArray());
        updateGUI();

    }

    public void startGradientDescentTraining(){
        int repetitions = (int) repetitionsGradientSlider.getValue();
        double learningRate = learningRateSlider.getValue();
        for (int i = 0; i < repetitions; i++) {
            neuralNetwork.trainWithGradientDescent(selectedDirectoryForTraining, learningRate);
        }
        addErrorToErrorChart(gradientErrorChart, gradientErrorChartAll, repetitions, false);
        SourceImage sourceImage = new SourceImage("/images/default/default1.png", (int) Math.sqrt(neuralNetwork.getInputlayer().getNumberOfNodes()));
        showImageInAnchorPane(sourceImage);
        neuralNetwork.startCalculations(sourceImage.getImageAs1DArray());
        updateGUI();

    }

    private void addErrorToErrorChart(XYChart trainingChart, XYChart allChart, int repetitions, boolean smallestErrorToo){

        trainingChart.setTitle("Trainingsdurchgang von " + LocalDateTime.now().format(DateTimeFormatter.ISO_TIME));
        trainingChart.getData().clear();
        XYChart.Series<Number, Double> dataSeriesReal = new XYChart.Series<>();
        XYChart.Series<Number, Double> dataSeriesSmallest = new XYChart.Series<>();

        dataSeriesReal.setName("Aktueller Fehler");
        dataSeriesSmallest.setName("Kleinster bisheriger Fehler");

        for (int i = 0; i < repetitions; i++) {
            LinkedList<Double> realErrorList = neuralNetwork.getRealErrorHistoryList();
            LinkedList<Double> smallestErrorList = neuralNetwork.getSmallestErrorHistoryList();


            dataSeriesReal.getData().add(new XYChart.Data<>(repetitions-i, realErrorList.get(realErrorList.size()-i-1)));
            dataSeriesSmallest.getData().add(new XYChart.Data<>(repetitions - i, smallestErrorList.get(smallestErrorList.size() - i - 1)));
        }

        Platform.runLater(() -> trainingChart.getData().add(dataSeriesReal));

        if (smallestErrorToo){
            Platform.runLater(() -> trainingChart.getData().add(dataSeriesSmallest));
        }

        allChart.setTitle("Gesamtverlauf von " + LocalDateTime.now().format(DateTimeFormatter.ISO_TIME));
        allChart.getData().clear();
        XYChart.Series<Number, Double> dataSeriesRealAll = new XYChart.Series<>();
        XYChart.Series<Number, Double> dataSeriesSmallestAll = new XYChart.Series<>();

        dataSeriesRealAll.setName("Aktueller Fehler");
        dataSeriesSmallestAll.setName("Kleinster bisheriger Fehler");

        int numberOfAllErrors = Math.min(neuralNetwork.getRealErrorHistoryList().size(), neuralNetwork.getSmallestErrorHistoryList().size());
        for (int i = 0; i < numberOfAllErrors; i++) {
            LinkedList<Double> realErrorList = neuralNetwork.getRealErrorHistoryList();
            LinkedList<Double> smallestErrorList = neuralNetwork.getSmallestErrorHistoryList();


            dataSeriesRealAll.getData().add(new XYChart.Data<>(numberOfAllErrors-i, realErrorList.get(realErrorList.size()-i-1)));
            dataSeriesSmallestAll.getData().add(new XYChart.Data<>(numberOfAllErrors-i, smallestErrorList.get(smallestErrorList.size()-i-1)));
        }

        Platform.runLater(() -> allChart.getData().add(dataSeriesRealAll));
        if (smallestErrorToo) {
            Platform.runLater(() -> allChart.getData().add(dataSeriesSmallestAll));
        }

    }

    public void testNetworkWithSelectedDirectory(){

        double percentageOfCorrectAnswers = neuralNetwork.getPercentageOfCorrectGuesses(selectedDirectoryForTesting);
        correctnessChart.getData().clear();
        System.out.println("Anteil korrekter Antworten: " + percentageOfCorrectAnswers);
        PieChart.Data dataTrue = new PieChart.Data("Korrekt", percentageOfCorrectAnswers);
        PieChart.Data dataFalse = new PieChart.Data("Falsch", 1-percentageOfCorrectAnswers);
        correctnessChart.getData().addAll(dataTrue, dataFalse);

    }



    public void randomlyChange(){
        neuralNetwork.adjustWeightsOfSelectedEdgesRandomly();
        updateGUI();
    }

    public void selectRandomEdges(){
        neuralNetwork.selectRandomEdges(percentSlider.getValue());
        updateGUI();

    }


    public void showNewNetworkSettings(){
        newNetworkSettingGridPane.setVisible(true);
    }

    public void hideNewNetworkSettings(){
        newNetworkSettingGridPane.setVisible(false);
    }

    public void exportNetwork(){
        JSONObject jsonNeuralNetwork = neuralNetwork.getNetWorkAsJSONObject();

        // Schreibe das JSON-Objekt in eine Datei
        String fileName = exportNameTextfield.getText();
        try (FileWriter file = new FileWriter("templates/" + fileName + ".json")) {
            file.write(jsonNeuralNetwork.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void importNetwork(){



        FileChooser fileChooser = new FileChooser();

        File currentDirectory = new File("templates");
        fileChooser.setInitialDirectory(currentDirectory);

        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("JSON-Dateien (*.json)", "*.json");
        fileChooser.getExtensionFilters().add(extFilter);

        File selectedFile = fileChooser.showOpenDialog(new Stage());

        if (selectedFile != null && selectedFile.getName().endsWith(".json")) {
            System.out.println("Ausgewählte JSON-Datei: " + selectedFile.getAbsolutePath());
        } else {
            System.out.println("Keine JSON-Datei ausgewählt oder ungültiges Format.");
        }

        try (FileReader reader = new FileReader(selectedFile)) {
            JSONTokener tokener = new JSONTokener(reader);
            Object obj = tokener.nextValue();

           if (obj instanceof JSONObject) {
                JSONObject jsonObject = (JSONObject) obj;
                neuralNetwork = new NeuralNetwork(jsonObject);
                initializeGUI(neuralNetwork);

            } else {
                System.out.println("Ungültiges JSON-Format.");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        loadImage();


    }



}
