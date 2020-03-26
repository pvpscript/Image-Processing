package operations;

import imageUtils.Image;
import imageUtils.Pixel;
import imageUtils.Point;

public class Inverter {
	// Intensity Transformations
	// -> Image Negative
	public static Image invertColors(Image image) {
		double[] rawPixel;
		Point point;
		Pixel[][] pixels = image.clonePixels();
		
		
		for(int i = 0; i < image.getRows(); i++) {
			for(int j = 0; j < image.getCols(); j++) {
				rawPixel = pixels[i][j].raw();
				point = pixels[i][j].getPoint();
				
				double[] invPixel = {
						Math.abs(255 - rawPixel[0]),
						Math.abs(255 - rawPixel[1]),
						Math.abs(255 - rawPixel[2])
				};
				
				pixels[i][j] = new Pixel(invPixel, point, image.getPixelsType());
			}
		}
		
		return new Image(pixels, image.getMatrix(), image.getPixelsType());
	}
}
