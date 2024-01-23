import java.util.*;
import java.lang.Math;

public class ClusteringMapGenerator implements ColorMapGenerator_Inter { //class for creating new colormap with k-means clustering

    DistanceMetric_Inter metric;

    public ClusteringMapGenerator(DistanceMetric_Inter metric) {
        this.metric = metric;
    }

    public Pixel[] generateColorPalette(Pixel[][] pixelArr, int numColors) throws IllegalArgumentException { 
        //uses a form of k++ to create original centroids, note for true k++ centroids should be choosen random with weights to make it farther
        //to confirm results of code are accurate, furtherest pixel is choosen as a centroid rather than the random function to compare to result file
        if (numColors < 1) {
            throw new IllegalArgumentException();
        }
        Pixel[] initialColorPalette = new Pixel[numColors];
        initialColorPalette[0] = pixelArr[0][0];
        for (int c = 1; c < initialColorPalette.length; c++) {
            double maxDistance = -Double.MAX_VALUE;
            Pixel maxPixel = null;
            for (int i = 1; i < pixelArr.length; i++) {
                for (int j = 1; j < pixelArr[i].length; j++) { 
                    double minCentroidD = Double.MAX_VALUE;
                    Pixel minCentroidPixel = null;
                    for (int n = 0; n < c; n++) {
                        double centroidDistance = metric.colorDistance(pixelArr[i][j], initialColorPalette[n]);
                        if (centroidDistance < minCentroidD) {
                            minCentroidD = centroidDistance;
                            minCentroidPixel = pixelArr[i][j];
                        }
                    }
                    if (minCentroidD > maxDistance) {
                        maxDistance = minCentroidD;
                        maxPixel = minCentroidPixel;
                    }
                    if (minCentroidD == maxDistance) {
                        int pixelVal = to24bit(minCentroidPixel);
                        if (pixelVal > to24bit(maxPixel)) {
                                maxPixel = minCentroidPixel;
                        }
                    }
                }
            }
            initialColorPalette[c] = maxPixel;
        }
        return initialColorPalette;
    }

    public Map<Pixel, Pixel> generateColorMap(Pixel[][] pixelArr, Pixel[] initialColorPalette) { //uses k-means to update centroids until convergence
        Map<Pixel, Pixel> oldMap = null;
        Map<Pixel, Pixel> colorMap = null;
        boolean converged = false;
        while (!converged) {
            colorMap = new HashMap<Pixel, Pixel>();
            for (int i = 0; i < pixelArr.length; i++) {
                for (int j = 0; j < pixelArr[i].length; j++) {
                    double minDistance = Double.MAX_VALUE;
                    Pixel minPixel = null;
                for (int n = 0; n < initialColorPalette.length; n++) {
                    double distance = metric.colorDistance(pixelArr[i][j], initialColorPalette[n]);
                    if (distance < minDistance) {
                        minPixel = initialColorPalette[n];
                        minDistance = distance;
                    }
                }
                colorMap.put(pixelArr[i][j], minPixel);
                }
            }
            Pixel[] newColorPalette = new Pixel[initialColorPalette.length];
            int[] totalRed = new int[initialColorPalette.length];
            int[] totalBlue = new int[initialColorPalette.length];
            int[] totalGreen = new int[initialColorPalette.length];
            int[] numPixels = new int[initialColorPalette.length];
            for (int i = 0; i < pixelArr.length; i++) {
                for (int j = 0; j < pixelArr[i].length; j++) {
                    Pixel bucket = colorMap.get(pixelArr[i][j]);
                    boolean found = false;
                    int loc = 0;
                    while (!found && loc < initialColorPalette.length) {
                        if (bucket.equals(initialColorPalette[loc])) {
                            found = true;
                        }
                        else {
                            loc = loc + 1;
                        }
                    }
                    if (found) {
                        totalRed[loc] = totalRed[loc] + pixelArr[i][j].getRed();
                        totalBlue[loc] = totalBlue[loc] + pixelArr[i][j].getBlue();
                        totalGreen[loc] = totalGreen[loc] + pixelArr[i][j].getGreen();
                        numPixels[loc] = numPixels[loc] + 1;
                    }
                }
            }
            for (int i = 0; i < newColorPalette.length; i++) {
                if (numPixels[i] != 0) {
                    int averageRed = totalRed[i]/numPixels[i];
                    int averageGreen = totalGreen[i]/numPixels[i];
                    int averageBlue = totalBlue[i]/numPixels[i];
                    Pixel addedPix = new Pixel(averageRed, averageGreen, averageBlue);
                    newColorPalette[i] = addedPix;
                }
                else {
                    newColorPalette[i] = initialColorPalette[i];
                }
            }
            if (oldMap == null || !oldMap.equals(colorMap)) {
                oldMap = colorMap;
                initialColorPalette = newColorPalette;
            }
            else {
                converged = true;
            }
        } 
        return colorMap;
    }

    private int to24bit(Pixel p) {
        String redBits = Integer.toBinaryString(p.getRed());
        while (redBits.length() < 8) {
            redBits = "0" + redBits;
        }
        String greenBits = Integer.toBinaryString(p.getGreen());
        while (greenBits.length() < 8) {
            greenBits = "0" + greenBits;
        }
        String blueBits = Integer.toBinaryString(p.getBlue());
        while (blueBits.length() < 8) {
            blueBits = "0" + blueBits;
        }
        String totalBit = redBits + greenBits + blueBits;
        return Integer.parseInt(totalBit, 2);
    }
}