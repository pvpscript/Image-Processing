package operations;

import imageUtils.Image;
import imageUtils.Pixel;
import imageUtils.Point;

public class Histogram {
	public static Image equalizeGray(Image image) {
		double[] histogram = new double[256];
		int imageSize = image.getRows() * image.getCols();
		Pixel[][] pixels = image.clonePixels();
		
		for(int i = 0; i < 256; i++)
			histogram[i] = 0;
		
		for(int i = 0; i < image.getRows(); i++)
			for(int j = 0; j < image.getCols(); j++)
				histogram[(int)pixels[i][j].raw()[0]]++;

		for(int i = 0; i < 256; i++)
			histogram[i] /= imageSize;
		
		for(int i = 1; i < 256; i++)
			histogram[i] += histogram[i-1];
		
		for(int i = 0; i < 256; i++)
			histogram[i] *= 255;
		
		for(int i = 0; i < image.getRows(); i++) {
			for(int j = 0; j < image.getCols(); j++) {
				Point point = pixels[i][j].getPoint();
				double[] pixel = {
						histogram[(int)pixels[i][j].raw()[0]],
						histogram[(int)pixels[i][j].raw()[0]],
						histogram[(int)pixels[i][j].raw()[0]]
				};
				
				pixels[i][j] = new Pixel(pixel, point, image.getPixelsType());
			}
		}
		
		return new Image(pixels, image.getMatrix(), image.getPixelsType());
	}
	
	public static Image equalizeColor(Image image) {
		Image qiyImage = Conversion.toQiy(image);
		
		double[] histogram = new double[256];
		int imageSize = qiyImage.getRows() * qiyImage.getCols();
		Pixel[][] pixels = qiyImage.clonePixels();
		
		for(int i = 0; i < 256; i++)
			histogram[i] = 0;
		
		for(int i = 0; i < qiyImage.getRows(); i++)
			for(int j = 0; j < qiyImage.getCols(); j++)
				histogram[(int)pixels[i][j].raw()[2]]++;

		for(int i = 0; i < 256; i++)
			histogram[i] /= imageSize;
		
		for(int i = 1; i < 256; i++)
			histogram[i] += histogram[i-1];
		
		for(int i = 0; i < 256; i++)
			histogram[i] *= 255;
		
		for(int i = 0; i < qiyImage.getRows(); i++) {
			for(int j = 0; j < qiyImage.getCols(); j++) {
				Point point = pixels[i][j].getPoint();
				double[] pixel = {
						pixels[i][j].raw()[0],
						pixels[i][j].raw()[1],
						histogram[(int)pixels[i][j].raw()[2]]
				};
				
				pixels[i][j] = new Pixel(pixel, point,
						qiyImage.getPixelsType());
			}
		}
		
		return Conversion.toBgr(
				new Image(pixels, qiyImage.getMatrix(),
						qiyImage.getPixelsType()));
	}
	
	public static double getConstrast(Image image) {
		Image qiyImage = Conversion.toQiy(image); 
		Pixel[][] pixels = image.getPixels();
		int imageSize = qiyImage.getRows() * qiyImage.getCols();
		double averageContrast = 0;
		double sum = 0;
		
		for(int i = 0; i < image.getRows(); i++)
			for(int j = 0; j < image.getCols(); j++)
				averageContrast += pixels[i][j].raw()[2] / imageSize;
		
		for(int i = 0; i < image.getRows(); i++) {
			for (int j = 0; j < image.getCols(); j++) {
				sum += Math.pow(((pixels[i][j].raw()[2] / 256) -
						averageContrast), 2);
			}
		}
		
		return (1.0 / imageSize) * Math.sqrt(sum);
	}
}
