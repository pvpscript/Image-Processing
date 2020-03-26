package operations;

import org.opencv.core.Mat;

import dataStructures.Misc;
import dataStructures.Triangle;
import imageUtils.Image;
import imageUtils.Pixel;
import imageUtils.Point;
import imageUtils.Image.Fill;

public class Transform {
	public static Image deform(Image existingImage, Image image,
			Triangle tV, Triangle tW) {
		int rows = (int)tW.getLeftmostPoint().getX();
		int cols = (int)tW.getTopmostPoint().getY();
		Image result;
		
		if (existingImage != null) {
			rows = existingImage.getRows() > rows
					? existingImage.getRows()
					: rows;
			cols = existingImage.getCols() > cols
					? existingImage.getCols()
					: cols;
				
			result = existingImage.extend(rows, cols);
		} else
			result = new Image(rows, cols,
					image.getImageType(), image.getPixelsType(), Fill.WHITE);
//		Pixel[][] pixels = image.getPixels();
		Pixel[][] pixels = image.actualPixelPlacement();
		Pixel[][] resPixels = result.getPixels();
		Pixel resPixel;
		Point point;
		Point warped;
		Mat affineMatrix = Misc.getAffineMatrix(tV, tW);
		
		for(int i = 0; i < rows; i++) {
			for(int j = 0; j < cols; j++) {
				point = new Point(i, j);
				
				if (tW.hasPoint(point)) {
					warped = point.warpAffineBackwards(affineMatrix);

					resPixel = pixels[(int)warped.getX()][(int)warped.getY()];
					resPixels[i][j] = new Pixel(resPixel.raw(), point,
							image.getPixelsType());
				}
			}
		}
		
		return result;
	}

	
	public  static Image rotate(Image image, double angle) {
		Point point;
		Pixel pixel;
		Pixel[][] pixels = image.clonePixels();
		double angleRad = ((angle % 360) * Math.PI) / 180;
		
		Point center = image.getCenterOfMass();
		
		for(int i = 0; i < image.getRows(); i++) {
			for(int j = 0; j < image.getCols(); j++) {
				pixel = pixels[i][j];
				point = pixel.getPoint();
				
				pixels[i][j] =
						new Pixel(pixel.raw(),
								point.rotate(center, angleRad),
								image.getPixelsType());
			}
		}
		
		return new Image(pixels, image.getMatrix(), image.getPixelsType());
	}
	
	public static Image translate(Image image, double x, double y) {
		Point point;
		Pixel pixel;
		Pixel[][] pixels = image.clonePixels();
		Point reference = new Point(y, -x);
		
		for(int i = 0; i < image.getRows(); i++) {
			for(int j = 0; j < image.getCols(); j++) {
				pixel = pixels[i][j];
				point = pixel.getPoint();
				
				pixels[i][j] = 
						new Pixel(pixel.raw(),
								point.translate(reference),
								image.getPixelsType());
			}
		}
		
		return new Image(pixels, image.getMatrix(), image.getPixelsType());
	}
}
