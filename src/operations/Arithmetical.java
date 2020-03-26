package operations;

import org.opencv.core.Mat;

import imageUtils.Image;
import imageUtils.Pixel;
import imageUtils.Point;

public class Arithmetical extends Logical {
	public static Image complement(Image image) {
		double[] rawPixel;
		Point point;
		Pixel[][] pixels = isBinary(image) ? image.clonePixels() :
			Gray.binary(image).clonePixels();
		
		for(int i = 0; i < image.getRows(); i++) {
			for(int j = 0; j < image.getCols(); j++) {
				rawPixel = pixels[i][j].raw();
				point = pixels[i][j].getPoint();
				
				double[] pixel = {
						rawPixel[0] == 255.0 ? 0.0 : 255.0,
						rawPixel[1] == 255.0 ? 0.0 : 255.0,
						rawPixel[2] == 255.0 ? 0.0 : 255.0,
				};
				
				pixels[i][j] = new Pixel(pixel, point, image.getPixelsType());
			}
		}
		
		return new Image(pixels, image.getMatrix(), image.getPixelsType());
	}
	
	public static Image difference(Image image1, Image image2) {
		double[] rawPixel1, rawPixel2;
		double[] whitePixel = {255, 255, 255};
		double[] blackPixel = {0, 0, 0};
		Pixel[][] pixels1 = isBinary(image1)
				? image1.clonePixels()
				: Gray.binary(image1).clonePixels();
		Pixel[][] pixels2 = isBinary(image2)
				? image2.clonePixels()
				: Gray.binary(image2).clonePixels();
		
		int rows = image1.getRows() < image2.getRows()
				? image1.getRows()
				: image2.getRows();
		int cols = image1.getCols() < image2.getCols()
				? image1.getCols()
				: image2.getCols();
		
		Image imgDif = new Image(new Mat(rows, cols, image1.getImageType()),
				image1.getPixelsType());
		Pixel[][] pixelsDif = imgDif.getPixels();
		
		for(int i = 0; i < rows; i++) {
			for(int j = 0; j < cols; j++) {
				rawPixel1 = pixels1[i][j].raw();
				rawPixel2 = pixels2[i][j].raw();
				
				if ((rawPixel1[0] == 0 && rawPixel2[0] == 0) ||
						(rawPixel1[0] == 255 && rawPixel2[0] == 255))
					pixelsDif[i][j] = new Pixel(whitePixel, new Point(i, j),
							imgDif.getPixelsType());
				else
					pixelsDif[i][j] = new Pixel(blackPixel, new Point(i, j),
							imgDif.getPixelsType());
			}
		}
		
		return imgDif;
	}
}
