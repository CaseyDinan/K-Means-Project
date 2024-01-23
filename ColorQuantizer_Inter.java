interface ColorQuantizer_Inter {
    
    public Pixel[][] quantizeTo2DArray(int numColors) throws IllegalArgumentException;
    //Performs color quantization. If numColors is less than 1, this method throws an IllegalArgumentException
    
    public void quantizeToBMP(String fileName, int numColors) throws IllegalArgumentException;
    //Performs color quantization that is saved to a bmp file
}