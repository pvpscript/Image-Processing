package imageUtils;

import org.opencv.core.CvType;

import imageUtils.Image.Fill;
import imageUtils.Pixel.Type;

public class StructuringElements {
	public static Image line() {
		return new Image(3, 1, CvType.CV_8UC3, Type.BGR, Fill.BLACK);
	}
	
	public static Image cross() {
		Image cross = new Image(3, 3, CvType.CV_8UC3, Type.BGR, Fill.BLACK);
		Pixel[][] pixels = cross.getPixels();
		int[] whitePixel = {255, 255, 255};
		
		pixels[0][0] = new Pixel(whitePixel, new Point(0, 0),
				cross.getPixelsType());
		pixels[0][2] = new Pixel(whitePixel, new Point(0, 2),
				cross.getPixelsType());
		pixels[2][0] = new Pixel(whitePixel, new Point(2, 0),
				cross.getPixelsType());
		pixels[2][2] = new Pixel(whitePixel, new Point(2, 2),
				cross.getPixelsType());
		
		return cross;
	}
	
	public static Image square() {
		return new Image(3, 3, CvType.CV_8UC3, Type.BGR, Fill.BLACK);
	}
}
