package edu.andreasgut.neuronalesnetzwerkfx.imagetools;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.*;
import javafx.scene.paint.Color;

import java.util.Arrays;

public class SourceImage {

    Image original;
    double zoom;
    Canvas imageAsCanvas;
    double[][] imageAsArray;
    double ratio;
    int targetWidth;
    int targetHeight;

    public SourceImage(String path, int targetWidth) {
        original = new Image(path, 200, 200, true, false, false);
        ratio = original.getHeight() / original.getWidth();
        this.targetWidth = targetWidth;
        targetHeight = (int) (targetWidth * ratio);

        zoom = original.getWidth() / targetWidth;


        // Neues Bild mit der gewünschten Auflösung erstellen
        WritableImage resizedImage = new WritableImage(targetWidth, targetHeight);
        PixelWriter pixelWriter = resizedImage.getPixelWriter();
        PixelReader pixelReader = original.getPixelReader();
        imageAsArray = new double[targetWidth][targetHeight];
        imageAsCanvas = new Canvas();
        imageAsCanvas.setHeight(original.getHeight());
        imageAsCanvas.setWidth(original.getWidth());


        for (int y = 0; y < targetHeight; y++) {
            for (int x = 0; x < targetWidth; x++) {
                // Bereich im ursprünglichen Bild
                int startX = (int) (x * (original.getWidth() / targetWidth));
                int startY = (int) (y * (original.getHeight() / targetHeight));
                int endX = (int) ((x + 1) * (original.getWidth() / targetWidth));
                int endY = (int) ((y + 1) * (original.getHeight() / targetHeight));

                // Durchschnittsfarbe für den Bereich berechnen
                double redSum = 0, greenSum = 0, blueSum = 0;
                for (int i = startX; i < endX; i++) {
                    for (int j = startY; j < endY; j++) {
                        Color color = pixelReader.getColor(i, j);
                        redSum += color.getRed();
                        greenSum += color.getGreen();
                        blueSum += color.getBlue();
                    }
                }
                int pixelCount = (endX - startX) * (endY - startY);
                Color avgColor = Color.color(redSum / pixelCount, greenSum / pixelCount, blueSum / pixelCount);
                Color grayColor = avgColor.grayscale();
                imageAsArray[x][y] = grayColor.getBrightness();
                GraphicsContext gc = imageAsCanvas.getGraphicsContext2D();
                gc.setFill(grayColor);
                gc.fillRect(x * zoom, y * zoom, 1 * zoom, 1 * zoom);

                pixelWriter.setColor(x, y, grayColor);

            }
        }

    }

    public Image getOriginal() {
        return original;
    }

    public double getZoom() {
        return zoom;
    }

    public Canvas getImageAsCanvas() {
        return imageAsCanvas;
    }

    public double[][] getImageAs2DArray() {
        return imageAsArray;
    }

    public double[] getImageAs1DArray() {
        double[] array1D = new double[imageAsArray.length * imageAsArray[0].length];
        int counter = 0;
        for (int row = 0; row < imageAsArray.length; row++){
            for (int column = 0; column < imageAsArray[0].length; column++){
                array1D[counter] = imageAsArray[row][column];
                counter++;
            }
        }

        return array1D;
    }

    public double getRatio() {
        return ratio;
    }

    public int getTargetWidth() {
        return targetWidth;
    }

    public int getTargetHeight() {
        return targetHeight;
    }

    public int getNumberOfPixelForNeuralNetwork() {
        return targetHeight * targetWidth;
    }


}


