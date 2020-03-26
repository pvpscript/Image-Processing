package operations;

import imageUtils.Image;
import imageUtils.Pixel;
import imageUtils.Point;
import imageUtils.Pixel.Type;

public class Conversion {
	public static Image toBgr(Image image) {
		double[] bgrPixel;
		Point point;
		Pixel[][] pixels = image.clonePixels();
		
		for(int i = 0; i < image.getRows(); i++) {
			for(int j = 0; j < image.getCols(); j++) {
				bgrPixel = pixels[i][j].bgr();
				point = pixels[i][j].getPoint();
				
				pixels[i][j] = new Pixel(bgrPixel, point, Type.BGR);
			}
		}
		
		return new Image(pixels, image.getMatrix(), Type.BGR);
	}
	
	public static Image toQiy(Image image) {
		double[] qiyPixel;
		Point point;
		Pixel[][] pixels = image.clonePixels();
		
		for(int i = 0; i < image.getRows(); i++) {
			for(int j = 0; j < image.getCols(); j++) {
				qiyPixel = pixels[i][j].qiy();
				point = pixels[i][j].getPoint();
				
				pixels[i][j] = new Pixel(qiyPixel, point, Type.QIY);
			}
		}
		
		return new Image(pixels, image.getMatrix(), Type.QIY);
	}
	
	public static Image toVsh(Image image) {
		double[] vshPixel;
		Point point;
		Pixel[][] pixels = image.clonePixels();
		
		for(int i = 0; i < image.getRows(); i++) {
			for(int j = 0; j < image.getCols(); j++) {
				vshPixel = pixels[i][j].vsh();
				point = pixels[i][j].getPoint();
				
				pixels[i][j] = new Pixel(vshPixel, point, Type.VSH);
			}
		}
		
		return new Image(pixels, image.getMatrix(), Type.VSH);
	}
}
