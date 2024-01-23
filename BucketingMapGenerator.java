import java.util.*;
import java.lang.Math;

public class BucketingMapGenerator implements ColorMapGenerator_Inter { //class for splitting colors into generic color buckets

     public Pixel[] generateColorPalette(Pixel[][] pixelArr, int numColors) throws IllegalArgumentException { //creates the buckets for the map
        if (numColors < 1) {
            throw new IllegalArgumentException();
        }
        double total = Math.pow(2,24);
        double bucketsize = total/numColors;
        double medBucket = bucketsize/2;
        Pixel[] initialColorPalette = new Pixel[numColors];
        for (int i = 0; i < initialColorPalette.length; i++) {
            int bucketNum = (int) (bucketsize * i + medBucket);
            String bucketBits = Integer.toBinaryString(bucketNum);
            while (bucketBits.length() < 24) {
                bucketBits = "0" + bucketBits;
            }
            String redBits = bucketBits.substring(0,8);
            String greenBits = bucketBits.substring(8,16);
            String blueBits = bucketBits.substring(16);
            Pixel addedPix = new Pixel(Integer.parseInt(redBits, 2), Integer.parseInt(greenBits, 2), Integer.parseInt(blueBits, 2));
            initialColorPalette[i] = addedPix;
        }
        return initialColorPalette;
     }

    public Map<Pixel, Pixel> generateColorMap(Pixel[][] pixelArr, Pixel[] initialColorPalette) { //maps every pixel to the nearest bucket
        Map<Pixel, Pixel> colorMap = new HashMap<Pixel, Pixel>();
        for (int i = 0; i < pixelArr.length; i++) {
            for (int j = 0; j < pixelArr[i].length; j++) {
                int bitVal = to24bit(pixelArr[i][j]);
                int minDistance = Integer.MAX_VALUE;
                int minLoc = Integer.MAX_VALUE;
                for (int n = 0; n < initialColorPalette.length; n++) {
                    int bucket = to24bit(initialColorPalette[n]);
                    if (Math.abs(bitVal - bucket) < minDistance) {
                        minDistance = bucket;
                        minLoc = n;
                    }
                }
                if (minLoc != Integer.MAX_VALUE) {
                    colorMap.put(pixelArr[i][j], initialColorPalette[minLoc]);
                }
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