import java.util.Map;

interface ColorMapGenerator_Inter {
    /*
    Produces an initial palette. This initial palette will be
    the centers of the buckets and the starting centroids of clusters.
    If numColors is greater than the number of unique colors in pixelArr,
    this method should return a Pixel array containing all unique colors
    in pixelArr. If numColors is less than 1, this method throws an
    IllegalArgumentException.
     */
    public Pixel[] generateColorPalette(Pixel[][] pixelArr, int numColors) throws IllegalArgumentException;

   /*
    Computes the reduced color map. For bucketing, this will map each color
    to the center of its bucket, for clustering, this maps examples to final
    centroids.
    */
   public Map<Pixel, Pixel> generateColorMap(Pixel[][] pixelArr, Pixel[] initialColorPalette);
}