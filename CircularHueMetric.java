import java.lang.Math;

public class CircularHueMetric implements DistanceMetric_Inter {

    public double colorDistance(Pixel p1, Pixel p2) {
        double distance1 = Math.abs(p2.getHue() - p1.getHue());
        double distance2 = 360 - distance1;
        return Math.min(distance1, distance2);
    }
    
}