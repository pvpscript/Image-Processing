package operations;

import org.opencv.core.Mat;

import imageUtils.Image;
import imageUtils.Pixel;
import imageUtils.Point;

public class Logical {
	protected static boolean isBinary(Image image) {
		double[] rawPixel;
		Pixel[][] pixels = image.getPixels();
		
		for(int i = 0; i < image.getRows(); i++) {
			for(int j = 0; j < image.getCols(); j++) {
				rawPixel = pixels[i][j].raw();
				
				if ((rawPixel[0] != 255 && rawPixel[0] != 0) ||
						(rawPixel[1] != 255 && rawPixel[1] != 0) ||
						(rawPixel[2] != 255 && rawPixel[2] != 0))
					return false;
			}
		}
		
		return true;
	}
	
	// Logical NOT!
	
	public static Image logicalAnd(Image image1, Image image2) {
		double[] rawPixel1, rawPixel2;
		Pixel[][] pixels1 = isBinary(image1) ? image1.clonePixels() :
			Gray.binary(image1).clonePixels();
		Pixel[][] pixels2 = isBinary(image2) ? image2.clonePixels() :
			Gray.binary(image2).clonePixels();
		
		int rows = image1.getRows() < image2.getRows() ? image1.getRows() :
			image2.getRows();
		int cols = image1.getCols() < image2.getCols() ? image1.getCols() :
			image2.getCols();
		
		Image imgAnd = new Image(new Mat(rows, cols, image1.getImageType()),
				image1.getPixelsType());
		Pixel[][] pixelsAnd = imgAnd.getPixels();
		
		for(int i = 0; i < rows; i++) {
			for(int j = 0; j < cols; j++) {
				rawPixel1 = pixels1[i][j].raw();
				rawPixel2 = pixels2[i][j].raw();
				
				double channel = (rawPixel1[0] == 0.0 ? true : false) &&
						(rawPixel2[0] == 0.0 ? true : false) ? 0.0 : 255.0;
				double[] pixel = {channel, channel, channel};
				
				pixelsAnd[i][j] = new Pixel(pixel, new Point(i, j),
						imgAnd.getPixelsType());
			}
		}
		
		return imgAnd;
	}
	
	public static Image logicalOr(Image image1, Image image2) {
		double[] rawPixel1, rawPixel2;
		Pixel[][] pixels1 = isBinary(image1) ? image1.clonePixels() :
			Gray.binary(image1).clonePixels();
		Pixel[][] pixels2 = isBinary(image2) ? image2.clonePixels() :
			Gray.binary(image2).clonePixels();
		
		int rows = image1.getRows() < image2.getRows() ? image1.getRows() :
			image2.getRows();
		int cols = image1.getCols() < image2.getCols() ? image1.getCols() :
			image2.getCols();
		
		Image imgOr = new Image(new Mat(rows, cols, image1.getImageType()),
				image1.getPixelsType());
		Pixel[][] pixelsOr = imgOr.getPixels();
		
		for(int i = 0; i < rows; i++) {
			for(int j = 0; j < cols; j++) {
				rawPixel1 = pixels1[i][j].raw();
				rawPixel2 = pixels2[i][j].raw();
				
				double channel = (i < image1.getRows() && j < image1.getCols() ?
						(rawPixel1[0] == 0.0 ? true : false) :
							false) ||
						(i < image2.getRows() && j < image2.getCols() ?
								(rawPixel2[0] == 0.0 ? true : false) :
									false) ? 0.0 : 255.0;
				double[] pixel = {channel, channel, channel};
				
				pixelsOr[i][j] = new Pixel(pixel, new Point(i, j),
						imgOr.getPixelsType());
			}
		}
		
		return imgOr;
	}
	
	// Logical XOR!
}
