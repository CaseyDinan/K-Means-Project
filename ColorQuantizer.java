import java.awt.image.BufferedImage;
import java.io.*;
import javax.imageio.ImageIO;
import java.util.*;

public class ColorQuantizer implements ColorQuantizer_Inter {

    ColorMapGenerator_Inter generator;
    Pixel[][] pixelArray;

    public ColorQuantizer(Pixel[][] pixelArray, ColorMapGenerator_Inter gen) {
        this.generator = gen;
        this.pixelArray = pixelArray;
    }

    public ColorQuantizer(String bmpFile, ColorMapGenerator_Inter gen) { //reads from file and creates pixelArray
        try {
            BufferedImage image = ImageIO.read(new File(bmpFile));
        Pixel[][] pixelArr = new Pixel[image.getWidth()][image.getHeight()];
        for (int x = 0; x < image.getWidth(); x++) {
            for (int y = 0; y < image.getHeight(); y++) {
                int rgb = image.getRGB(x, y);
                int red = (rgb >> 16) & 0xFF;
                int green = (rgb >> 8) & 0xFF;
                int blue = rgb & 0xFF;
                pixelArr[x][y] = new Pixel(red, green, blue);
            }
        } 
        this.generator = gen;
        this.pixelArray = pixelArr;
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Pixel[][] quantizeTo2DArray(int numColors) throws IllegalArgumentException { //uses given metric to make new pixel array with the color quantization
        if (numColors < 1) {
            throw new IllegalArgumentException();
        }
        Pixel[] colorPallete = generator.generateColorPalette(pixelArray, numColors);
        Map<Pixel, Pixel> colorMap = generator.generateColorMap(pixelArray, colorPallete);
        Pixel[][] quantizedArr = new Pixel[pixelArray.length][pixelArray[0].length];
        for (int i = 0; i < pixelArray.length; i++) {
            for (int j = 0; j < pixelArray[i].length; j++) {
                quantizedArr[i][j] = colorMap.get(pixelArray[i][j]);
            }
        }
        return quantizedArr;
    }

    public void quantizeToBMP(String bmpFileName, int numColors) { //uses given metric to create quantization and write it to file
        if (numColors < 1) {
            throw new IllegalArgumentException();
        }
        Pixel[] colorPallete = generator.generateColorPalette(pixelArray, numColors);
        Map<Pixel, Pixel> colorMap = generator.generateColorMap(pixelArray, colorPallete);
        Pixel[][] quantizedArr = new Pixel[pixelArray.length][pixelArray[0].length];
        for (int i = 0; i < pixelArray.length; i++) {
            for (int j = 0; j < pixelArray[i].length; j++) {
                quantizedArr[i][j] = colorMap.get(pixelArray[i][j]);
            }
        }
        try {
            BufferedWriter bmpWriter = new BufferedWriter(new FileWriter(bmpFileName));
            for (int i = 0; i < pixelArray.length; i++) {
                for (int j = 0; j < pixelArray[i].length; j++) {
                    bmpWriter.write(pixelArray[i][j] + String.valueOf('\t'));
                }
                bmpWriter.newLine();
            }
            bmpWriter.close();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
}