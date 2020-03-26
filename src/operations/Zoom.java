package operations;

import imageUtils.Image;
import imageUtils.Image.Fill;
import imageUtils.Pixel;
import imageUtils.Pixel.Type;

public class Zoom {
	public static Image squaredIn(Image image) {
		int zoomedRows = image.getRows() * 2;
		int zoomedCols = image.getCols() * 2;
		
		Image zoomed = new Image(zoomedRows, zoomedCols,
				image.getImageType(), image.getPixelsType(), Fill.WHITE);
		Pixel[][] pixelsOrig = image.getPixels();
		Pixel[][] pixelZoomed = zoomed.getPixels();
		
		double[] bgr;
		
		for (int i = 0; i < zoomedRows; i+=2) {
			for(int j = 0; j < zoomedCols; j+=2) {
				bgr = pixelsOrig[i / 2][j / 2].bgr();
				
				pixelZoomed[i][j].setPixelColor(bgr);
				pixelZoomed[i][j+1].setPixelColor(bgr);
				pixelZoomed[i+1][j].setPixelColor(bgr);
				pixelZoomed[i+1][j+1].setPixelColor(bgr);
			}
		}
		
		return zoomed;
	}
	
	public static Image squaredOut(Image image) {
		int zoomedRows = (image.getRows() % 2 == 0)
				? image.getRows() / 2 
				: (image.getRows() + 1) / 2;
		int zoomedCols = (image.getCols() % 2 == 0) 
				? image.getCols() / 2 
				: (image.getCols() + 1) / 2;
		
		Image zoomed = new Image(zoomedRows, zoomedCols,
				image.getImageType(), Type.BGR, Fill.WHITE);
		Pixel[][] pixelsOrig = image.getPixels();
		Pixel[][] pixelsZoomed = zoomed.getPixels();
		
		double[] bgr;
		
		for (int i = 0, k = 0; i < image.getRows(); i+=2, k++) {
			for(int j = 0, l = 0; j < image.getCols(); j+=2, l++) {
				bgr = pixelsOrig[i][j].bgr();
				
				pixelsZoomed[k][l].setPixelColor(bgr);
			}
		}
		
		return zoomed;
	}
	
	public static Image linearIn(Image image) {
		int zoomedRows = image.getRows() * 2 - 1;
		int zoomedCols = image.getCols() * 2 - 1;
		
		Image zoomed = new Image(zoomedRows, zoomedCols,
				image.getImageType(), Type.BGR, Fill.WHITE);
		Pixel[][] pixelsOrig = image.getPixels();
		Pixel[][] pixelsZoomed = zoomed.getPixels();
		
		for(int i = 0; i < image.getRows(); i++) {
			for (int j = 0; j < image.getCols(); j++) {
				if (j + 1 < image.getCols()) {
					double[] pixelMean = {
							(pixelsOrig[i][j].bgr()[0] + pixelsOrig[i][j+1].bgr()[0]) / 2,
							(pixelsOrig[i][j].bgr()[1] + pixelsOrig[i][j+1].bgr()[1]) / 2,
							(pixelsOrig[i][j].bgr()[2] + pixelsOrig[i][j+1].bgr()[2]) / 2
					};
					
					if (j * 2 + 1 < zoomedCols)
						pixelsZoomed[i*2][j*2+1].setPixelColor(pixelMean);
				}
				pixelsZoomed[i*2][j*2].setPixelColor(pixelsOrig[i][j].bgr());
			}
		}
		
		for (int i = 1; i < zoomedRows; i+=2) {
			for (int j = 0; j < zoomedCols; j++) {
				double[] pixelMean = {
						(pixelsZoomed[i-1][j].bgr()[0] + pixelsZoomed[i+1][j].bgr()[0]) / 2,
						(pixelsZoomed[i-1][j].bgr()[1] + pixelsZoomed[i+1][j].bgr()[1]) / 2,
						(pixelsZoomed[i-1][j].bgr()[2] + pixelsZoomed[i+1][j].bgr()[2]) / 2
				};
				pixelsZoomed[i][j].setPixelColor(pixelMean);
			}
		}
		
		return zoomed;
	}
	
	public static Image linearOut(Image image) {
		int zoomedRows = (image.getRows() % 2 == 0)
				? image.getCols() / 2
				: (image.getRows() + 1) / 2;
		int zoomedCols = (image.getCols() % 2 == 0)
				? image.getCols() / 2
				: (image.getCols() + 1) / 2;
				
		Image zoomed = new Image(zoomedRows, zoomedCols,
				image.getImageType(), Type.BGR, Fill.WHITE);
		Pixel[][] pixelsOrig = image.getPixels();
		Pixel[][] pixelsZoomed = zoomed.getPixels();
		
		double r, g, b;
		int qtd = 1;
		
		
		for (int i = 0, k = 0; i < image.getRows(); i += 2, k++) {
			for (int j = 0, l = 0; j < image.getCols(); j += 2, l++) {
				r = pixelsOrig[i][j].bgr()[0];
				g = pixelsOrig[i][j].bgr()[1];
				b = pixelsOrig[i][j].bgr()[2];
				if (j + 1 < image.getCols()) {
					r += pixelsOrig[i][j+1].bgr()[0];
					g += pixelsOrig[i][j+1].bgr()[1];
					b += pixelsOrig[i][j+1].bgr()[2];
					
					qtd++;
				}
				if (i + 1 < image.getRows()) {
					r += pixelsOrig[i+1][j].bgr()[0];
					g += pixelsOrig[i+1][j].bgr()[1];
					b += pixelsOrig[i+1][j].bgr()[2];
					
					qtd++;
					
					if (j + 1 < image.getCols()) {
						r += pixelsOrig[i+1][j+1].bgr()[0];
						g += pixelsOrig[i+1][j+1].bgr()[1];
						b += pixelsOrig[i+1][j+1].bgr()[2];
						
						qtd++;
					}
				}
				
				double[] pixel = {
					r / qtd,
					g / qtd,
					b / qtd
				};
				qtd = 1;
				
				pixelsZoomed[k][l].setPixelColor(pixel);
			}
		}
		
		return zoomed;
	}
}
