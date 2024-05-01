package edu.andreasgut.neuronalesnetzwerkfx.core;

import edu.andreasgut.neuronalesnetzwerkfx.imagetools.SourceImage;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.util.*;

public class NeuralNetwork {

    Inputlayer inputlayer;
    LinkedList<Hiddenlayer> hiddenlayers = new LinkedList<>();
    Outputlayer outputlayer;
    LinkedList<NetworkEdge> selectedEdges = new LinkedList<>();
    LinkedList<Double> realErrorHistoryList = new LinkedList<>();
    LinkedList<Double> smallestErrorHistoryList = new LinkedList<>();

    public NeuralNetwork(int numberOfInputNodes, int numberOfHiddenLayers, int numberOfHiddenLayerNodes, int numberOfOutputNodes) {
        this.inputlayer = new Inputlayer(numberOfInputNodes, this);
        Layer previousLayer = inputlayer;
        for (int i = 0; i < numberOfHiddenLayers; i++){
            Hiddenlayer tempHiddenLayer = new Hiddenlayer(numberOfHiddenLayerNodes, previousLayer, this);
            hiddenlayers.add(tempHiddenLayer);
            previousLayer = tempHiddenLayer;
        }
        this.outputlayer = new Outputlayer(numberOfOutputNodes, previousLayer, this);
    }

    public NeuralNetwork(JSONObject jsonNetwork){

        handleJSONWeights((JSONArray) jsonNetwork.get("weights"));
        handleJSONErrors((JSONArray) jsonNetwork.get("realErrors"));




    }

    private void handleJSONWeights(JSONArray weightsArray){
        System.out.println("Anzahl Layers: " + weightsArray.length());
        int numberOfInputNodes = ((JSONArray) weightsArray.get(0)).length();
        int numberOfHiddenLayers = weightsArray.length() - 2;
        int numberOfHiddenLayerNodes = ((JSONArray) weightsArray.get(1)).length();
        int numberOfOutputNodes = ((JSONArray) weightsArray.get(weightsArray.length()-1)).length();;

        this.inputlayer = new Inputlayer(numberOfInputNodes, this);
        Layer previousLayer = inputlayer;
        for (int i = 0; i < numberOfHiddenLayers; i++){
            Hiddenlayer tempHiddenLayer = new Hiddenlayer(numberOfHiddenLayerNodes, previousLayer, this);
            hiddenlayers.add(tempHiddenLayer);
            previousLayer = tempHiddenLayer;
        }
        this.outputlayer = new Outputlayer(numberOfOutputNodes, previousLayer, this);

        for (Layer layer : getAllLayers()){
            for (NetworkNode node : layer.getNodes()){
                for (NetworkEdge edge : node.getOutputEdges()){
                    JSONArray layerArray = (JSONArray) weightsArray.get(getAllLayers().indexOf(layer));
                    JSONArray nodeArray = (JSONArray) layerArray.get(layer.getNodes().indexOf(node));
                    double weight = Double.parseDouble(nodeArray.get(node.getOutputEdges().indexOf(edge)).toString());
                    edge.setWeight(weight);
                }
            }
        }
    }

    private void handleJSONErrors(JSONArray realErrors){
        for (Object error : realErrors){
            addErrorToErrorHistory(Double.parseDouble(error.toString()));
        }
    }


    public void startCalculations(double[] inputs){
        int inputLength = inputs.length;
        int inputNodes = inputlayer.getNumberOfNodes();
        if (inputLength != inputNodes){
            System.out.println("Fehler. Die Anzahl der Inputwerte Stimmen nicht mit der Anzahl Knoten des Inputlayers überein.");
            System.out.println("Die Inputlänge beträgt " + inputLength + " und die Anzahl Knoten im Inputlayer beträgt " + inputNodes + ".");
        } else {
            inputlayer.feedDataToInputNodes(inputs);
        }
    }

    public Inputlayer getInputlayer() {
        return inputlayer;
    }

    public LinkedList<Hiddenlayer> getHiddenlayers() {
        return hiddenlayers;
    }

    public Outputlayer getOutputlayer() {
        return outputlayer;
    }

    public int getNumberOfLayers(){
        return hiddenlayers.size() + 2;
    }

    public Layer getPreviousLayer(Layer currentLayer){
        for (Layer layer : getHiddenlayers()){
            if (layer == currentLayer){
                return layer;
            }
        }
        return inputlayer;
    }

    public int getMaxNumberOfNodePerLayer(){
        return Math.max(Math.max(inputlayer.getNumberOfNodes(), hiddenlayers.get(0).getNumberOfNodes()), outputlayer.getNumberOfNodes());
    }



    public LinkedList<NetworkEdge> getSelectedEdges() {
        return selectedEdges;
    }

    public void updateLineGraphicOfAllEdges(){
        for (NetworkEdge edge : getAllEdges()){
            edge.resetLineColor();
            edge.updateLineWeightGraphic();
        }
    }

    public LinkedList<NetworkEdge> getAllEdges(){
        LinkedList<NetworkEdge> listOfEdges = new LinkedList<>();
        for (Layer layer : getAllLayers()){
            for (NetworkNode node : layer.getNodes()){
                for (NetworkEdge edge : node.getOutputEdges()){
                    listOfEdges.add(edge);
                }
            }
        }
        return listOfEdges;
    }


    public void adjustWeightsOfSelectedEdgesRandomly(){
        System.out.println("Anzahl angepasster Gewichte: " + selectedEdges.size());
        Random random = new Random();
        for (NetworkEdge edge : selectedEdges){
            edge.setWeight(random.nextDouble() * 2 - 1);
        }
    }

    public void selectRandomEdges(double percentage){
        selectedEdges.clear();
        int numberOfEdgesToSelect = (int) Math.round(getAllEdges().size() * percentage / 100);
        System.out.println("Anzahl ausgewählter Kanten: " + numberOfEdgesToSelect);
        Random random = new Random();
        while (selectedEdges.size() < numberOfEdgesToSelect){
            NetworkEdge randomEdge = getAllEdges().get(random.nextInt(getAllEdges().size()));
            if (!selectedEdges.contains(randomEdge)){
                selectedEdges.add(randomEdge);
            }
        }
    }

    public LinkedList<Layer> getAllLayers(){
        LinkedList<Layer> layers = new LinkedList<>();
        layers.add(inputlayer);
        for (Layer layer : hiddenlayers){
            layers.add(layer);
        }
        layers.add(outputlayer);

        return layers;
    }

    public void calculateError(File directory){
        List<File> files = new ArrayList<>();
        File[] fileArray = directory.listFiles();

        if (fileArray != null) {
            Arrays.sort(fileArray, Comparator.comparing(File::getName));
            for (File file : fileArray) {
                if (file.isFile()) {
                    files.add(file);
                }
            }
            System.out.println(files.size());
            //Arrays.sort(fileArray, Comparator.comparing(File::getName));
        }

        double error = 0;

        for (File file : files) {
            char firstSymbol = file.getName().charAt(0);
            if (Character.isDigit(firstSymbol)){
                int indexOfCorrectOutput = firstSymbol - '0';
                System.out.println(indexOfCorrectOutput + " bei Datei " + file.getName());
                SourceImage sourceImage = new SourceImage(file.toURI().toString(), (int) Math.sqrt(getInputlayer().getNumberOfNodes()));
                startCalculations(sourceImage.getImageAs1DArray());
                for (int i = 0; i < getOutputlayer().getNumberOfNodes(); i++){
                    double target;
                    if (i == indexOfCorrectOutput){
                        target = 1;
                    }
                    else {
                        target = 0;
                    }
                    double output = getOutputlayer().getNodes().get(i).getOutput();


                    double errorForThisOutput = Math.abs(output - target);
                    System.out.println("Error für diesen Output: " + errorForThisOutput);
                    double squaredErrorForThisOutput = Math.pow(errorForThisOutput, 2);
                    error += squaredErrorForThisOutput;

                }
            }
        }

        error /= files.size();
        System.out.println("Fehler: " + error);
        System.out.println("Letztes File:" + files.get(files.size()-1));

        addErrorToErrorHistory(error);

    }


    private void addErrorToErrorHistory(double error){
        realErrorHistoryList.add(error);
        int limit = 5000;
        if (realErrorHistoryList.size() > limit){
            for (int i = limit; i < realErrorHistoryList.size(); i++){
                realErrorHistoryList.remove(i);
            }
        }

        if (smallestErrorHistoryList.size() == 0){
            smallestErrorHistoryList.add(error);}
        else {
            double lastError = smallestErrorHistoryList.get(smallestErrorHistoryList.size() - 1);
            if (lastError > error) {
                smallestErrorHistoryList.add(error);
            } else {
                smallestErrorHistoryList.add(lastError);
            }
        }

        if (smallestErrorHistoryList.size() > limit){
            for (int i = limit; i < smallestErrorHistoryList.size(); i++){
                smallestErrorHistoryList.remove(i);
            }
        }
    }


    public void train(File directory){
        if (realErrorHistoryList.size() == 0){
            calculateError(directory);
        }
        double minError = smallestErrorHistoryList.get(smallestErrorHistoryList.size()-1);
        adjustWeightsOfSelectedEdgesRandomly();
        calculateError(directory);
        double currentError = realErrorHistoryList.get(realErrorHistoryList.size()-1);

        if (currentError > minError) {
            for (NetworkEdge edge : selectedEdges) {
                edge.backUpWeight();
            }
            System.out.println("Netz wurde nicht verbessert und wurde zurückgesetzt. Aktueller Fehler: " + minError);
        }
        else {
            System.out.println("Netz wurde verbessert. Aktueller Fehler: " + currentError);
        }

    }

    public double getPercentageOfCorrectGuesses(File directory){
        List<File> files = new ArrayList<>();
        File[] fileArray = directory.listFiles();

        if (fileArray != null) {
            Arrays.sort(fileArray, Comparator.comparing(File::getName));
            for (File file : fileArray) {
                if (file.isFile()) {
                    files.add(file);
                }
            }
            System.out.println(files.size());
            Arrays.sort(fileArray, Comparator.comparing(File::getName));
        }

        int correctAnswers = 0;

        for (File file : files) {
            char firstSymbol = file.getName().charAt(0);
            if (Character.isDigit(firstSymbol)){
                int indexOfCorrectOutput = firstSymbol - '0';
                System.out.println(indexOfCorrectOutput + " bei Datei " + file.getName());
                SourceImage sourceImage = new SourceImage(file.toURI().toString(), (int) Math.sqrt(getInputlayer().getNumberOfNodes()));
                startCalculations(sourceImage.getImageAs1DArray());
                int indexOfGuess = getIndexOfHighestOutputNode();
                System.out.println("Getippter Output: " + indexOfGuess);

                if (indexOfCorrectOutput == indexOfGuess){
                    correctAnswers++;
                }
            }
        }

        return correctAnswers / (double) files.size();
    }

    public int getIndexOfHighestOutputNode(){

        int maxIndex = 0;
        double maxOutput = 0;

        for (int i = 0; i < getOutputlayer().getNumberOfNodes(); i++){
            if (getOutputlayer().getNodes().get(i).getOutput() > maxOutput){
                maxOutput = getOutputlayer().getNodes().get(i).getOutput();
                maxIndex = i;
            }
        }

        return maxIndex;
    }

    public LinkedList<Double> getRealErrorHistoryList() {
        return realErrorHistoryList;
    }

    public LinkedList<Double> getSmallestErrorHistoryList() {
        return smallestErrorHistoryList;
    }

    public JSONObject getNetWorkAsJSONObject() {
        JSONObject jsonNetwork = new JSONObject();
        JSONArray jsonNetworkWeights = new JSONArray();

        for (int layer = 0; layer < getAllLayers().size(); layer++){

            JSONArray jsonLayer = new JSONArray();

            for (int node = 0; node < getAllLayers().get(layer).getNumberOfNodes(); node++){
                JSONArray jsonNode = new JSONArray();


                for (NetworkEdge edge : getAllLayers().get(layer).getNodes().get(node).getOutputEdges()) {
                    jsonNode.put(edge.getWeight());
                }

                jsonLayer.put(jsonNode);

            }

            jsonNetworkWeights.put(jsonLayer);
        }

        jsonNetwork.put("weights", jsonNetworkWeights);

        JSONArray realErrors = new JSONArray();
        for (Double error : realErrorHistoryList){
            realErrors.put(error);
        }
        jsonNetwork.put("realErrors", realErrors);

        /*JSONArray smallestErrors = new JSONArray();
        for (Double error : smallestErrorHistoryList){
            smallestErrors.put(error);
        }
        jsonNetwork.put("smallestErrors", smallestErrors);*/


        return jsonNetwork;


    }

}


