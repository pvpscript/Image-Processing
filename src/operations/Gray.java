package operations;

import imageUtils.Image;
import imageUtils.Pixel;

public class Gray {
	public static Image grayScale(Image image) {
		Pixel[][] pixels = image.clonePixels();
		double[] pixel;
		double mean;
		
		for(int i = 0; i < image.getRows(); i++) {
			for(int j = 0; j < image.getCols(); j++) {
				pixel = pixels[i][j].raw();
				mean = (pixel[0] + pixel[1] + pixel[2]) / 3;
				double[] grayPixel = {mean, mean, mean};
				
				pixels[i][j].setPixelColor(grayPixel);
			}
		}
		
		return new Image(pixels, image.getMatrix(), image.getPixelsType());
	}
	
	public static Image binary(Image image) {
		Pixel[][] pixels = image.clonePixels();
		double[] pixel;
		double mean;
		
		for(int i = 0; i < image.getRows(); i++) {
			for(int j = 0; j < image.getCols(); j++) {
				pixel = pixels[i][j].raw();
				mean = (pixel[0] + pixel[1] + pixel[2]) / 3;
				mean = (mean > 100) ? 255 : 0;
				double[] grayPixel = {mean, mean, mean};
				
				pixels[i][j].setPixelColor(grayPixel);
			}
		}
		
		return new Image(pixels, image.getMatrix(), image.getPixelsType());
	}
}
