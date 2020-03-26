package operations;

import java.util.Stack;

import dataStructures.Misc;
import imageUtils.Image;
import imageUtils.Pixel;
import imageUtils.Pixel.Type;
import imageUtils.Point;
import imageUtils.Image.Fill;

public class Morphology {
	public static Image erosion(Image image, Image model) {
		Image eroded = new Image(image.getRows(), image.getCols(),
				image.getImageType(), Type.BGR, Fill.WHITE);
		
		for(int i = 0; i < image.getRows(); i++) {
			for(int j = 0; j < image.getCols(); j++) {
				if (contains(image, model, i, j)) {
					putMiddle(eroded, model, i, j);
				}
			}
		}
		
		return eroded;
	}
	
	public static Image dilation(Image image, Image model) {
		Image dilated = new Image(image.clonePixels(), image.acquireMat(),
				image.getPixelsType());
		
		for(int i = 0; i < image.getRows(); i++) {
			for(int j = 0; j < image.getCols(); j++) {
				if (belongs(image, model, i, j)) {
					putWhole(dilated, model, i, j);
				}
			}
		}
		
		return dilated;
	}
	
	public static Image opening(Image image, Image model) {
		return dilation(erosion(image, model), model);
	}
	
	public static Image closing(Image image, Image model) {
		return erosion(dilation(image, model), model);
	}
	
	public static Image skeletonize(Image image, Image model) {
		Image skeleton = new Image(image.getRows(), image.getCols(),
				image.getImageType(), Type.BGR, Fill.WHITE);
		
		Image eroded = image;
		Image opened = opening(eroded, model);
		Image diff = Arithmetical.difference(eroded, opened);
		skeleton = Logical.logicalOr(skeleton, diff);
		
		int i = 0;
		System.out.println("\nIteration: " + i++);
		
		while(!fullyEroded(opened)) {
			eroded = erosion(eroded, model);
			opened = opening(eroded, model);
			diff = Arithmetical.difference(eroded, opened);
			skeleton = Logical.logicalOr(skeleton, diff);

			System.out.println("Iteration: " + i++);
		}
		
		return skeleton;
	}
	
	public static Image floodFillPoint(Image image, double[] resColor,
			int x, int y, int offset) {
		Image fillPoint = image.clone();
		Pixel[][] pixels = fillPoint.getPixels();
		double[] targetColor;
		
		if (x < fillPoint.getRows() && x >= 0 &&
				y < fillPoint.getCols() && y >= 0)
			targetColor = pixels[x][y].bgr();
		else
			return null;
		
		forestFire(fillPoint, targetColor, resColor, x, y, offset);
		
		return fillPoint;
	}

	public static Image floodFillCenter(Image image, double[] resColor,
			int offset) {
		Image fillCenter = image.clone();
		Pixel[][] pixels = fillCenter.getPixels();
		Point point = fillCenter.getCenterOfMass();
		double[] targetColor = pixels[(int)point.getX()][(int)point.getY()].bgr();
		
		forestFire(fillCenter, targetColor, resColor,
				(int)point.getX(), (int)point.getY(), offset);
		
		return fillCenter;
	}
	
	public static Image floodFillColor(Image image, double[] targetColor,
			double[] repColor, int offset) {
		Image filled = image;
		Pixel[][] pixels = filled.getPixels();
		
		if (Misc.equals(targetColor, repColor, offset))
			return image;
		
		for(int i = 0; i < image.getRows(); i++) {
			for(int j = 0; j < image.getCols(); j++) {
				if (Misc.equals(pixels[i][j].bgr(), targetColor, offset)) {
					forestFire(filled, targetColor, repColor,
							i, j, offset);
					pixels = filled.getPixels();
				}
			}
		}
		
		return filled;
	}
	
	private static boolean belongs(Image image, Image subImage, int x, int y) {
		int rows = subImage.getRows() + x;
		int cols = subImage.getCols() + y;
		
		if (rows > image.getRows() || cols > image.getCols())
			return false;
		
		Pixel[][] pixels = image.getPixels();
		int midX = Math.round((subImage.getRows() / 2) + x);
		int midY = Math.round((subImage.getCols() / 2) + y);
		double[] rawPixel = pixels[midX][midY].raw();
		
		if (rawPixel[0] == 0)
			return true;
		else
			return false;
	}
	
	private static boolean contains(Image image, Image subImage, int x, int y) {
		int rows = subImage.getRows() + x;
		int cols = subImage.getCols() + y;
		
		if (rows > image.getRows() || cols > image.getCols())
			return false;
		
		Pixel[][] pixels = image.getPixels();
		Pixel[][] subPixels = subImage.getPixels();
		double[] rawPixel;
		double[] subRawPixel;
		
		for(int i = 0; i < subImage.getRows(); i++) {
			for(int j = 0; j < subImage.getCols(); j++) {
				rawPixel = pixels[x + i][y + j].raw();
				subRawPixel = subPixels[i][j].raw();
			
				if (subRawPixel[0] == 0 && subRawPixel[0] != rawPixel[0])
					return false;
			}
		}
		
		return true;
	}
	
	private static void putMiddle(Image image, Image model, int x, int y) {
		int midX = model.getRows() / 2;
		int midY = model.getCols() / 2;
		Pixel[][] pixels = image.getPixels();
		double[] blackPixel = {0, 0, 0};
		
		pixels[midX + x][midY + y] = new Pixel(blackPixel,
				new Point(midX + x, midY + y), image.getPixelsType());
	}
	
	private static void putWhole(Image image, Image model, int x, int y) {
		Pixel[][] pixels = image.getPixels();
		Pixel[][] subPixels = model.getPixels();
		double[] blackPixel = {0, 0, 0};
		
		for(int i = 0; i < model.getRows(); i++)
			for(int j = 0; j < model.getCols(); j++)
				if (subPixels[i][j].raw()[0] == 0)
					pixels[i + x][j + y] = new Pixel(blackPixel,
							new Point(i + x, j + y), image.getPixelsType());
	}
	
	private static boolean fullyEroded(Image image) {
		Pixel[][] pixels = image.getPixels();
		double[] rawPixel;
		
		for(int i = 0; i < image.getRows(); i++) {
			for(int j = 0; j < image.getCols(); j++) {
				rawPixel = pixels[i][j].raw();
				
				if (rawPixel[0] != 255 || rawPixel[1] != 255 ||
						rawPixel[2] != 255)
					return false;
			}
		}
		
		return true;
	}
	
	private static void forestFire(Image image, double[] targetColor,
			double[] repColor, int x, int y, int offset) {
		Pixel[][] pixels = image.getPixels();
		Stack<Point> stack;
		Point point, tempPoint;
		Pixel pixel;
		
		if (Misc.equals(targetColor, repColor, offset))
			return;
		if (!Misc.equals(pixels[x][y].bgr(), targetColor, offset))
			return;
		
		pixels[x][y].setPixelColor(repColor);
		stack = new Stack<Point>();
		stack.push(new Point(x, y));
		
		while(!stack.empty()) {
			point = stack.pop();
			
			tempPoint = new Point(point.getX() - 1, point.getY());
			if (tempPoint.getX() < image.getRows() &&
					tempPoint.getY() < image.getCols() &&
					tempPoint.getX() >= 0 && tempPoint.getY() >= 0) {
				pixel = pixels[(int)tempPoint.getX()][(int)tempPoint.getY()];
				if (Misc.equals(pixel.bgr(), targetColor, offset)) {
					pixel.setPixelColor(repColor);
					stack.push(tempPoint);
				}
			}
			
			tempPoint = new Point(point.getX(), point.getY() - 1);
			if (tempPoint.getX() < image.getRows() &&
					tempPoint.getY() < image.getCols() &&
					tempPoint.getX() >= 0 && tempPoint.getY() >= 0) {
				pixel = pixels[(int)tempPoint.getX()][(int)tempPoint.getY()];
				if (Misc.equals(pixel.bgr(), targetColor, offset)) {
					pixel.setPixelColor(repColor);
					stack.push(tempPoint);
				}
			}
			
			tempPoint = new Point(point.getX(), point.getY() + 1);
			if (tempPoint.getX() < image.getRows() &&
					tempPoint.getY() < image.getCols() &&
					tempPoint.getX() >= 0 && tempPoint.getY() >= 0) {
				pixel = pixels[(int)tempPoint.getX()][(int)tempPoint.getY()];
				if (Misc.equals(pixel.bgr(), targetColor, offset)) {
					pixel.setPixelColor(repColor);
					stack.push(tempPoint);
				}
			}
			
			tempPoint = new Point(point.getX() + 1, point.getY());
			if (tempPoint.getX() < image.getRows() &&
					tempPoint.getY() < image.getCols() &&
					tempPoint.getX() >= 0 && tempPoint.getY() >= 0) {
				pixel = pixels[(int)tempPoint.getX()][(int)tempPoint.getY()];
				if (Misc.equals(pixel.bgr(), targetColor, offset)) {
					pixel.setPixelColor(repColor);
					stack.push(tempPoint);
				}
			}	
		}
	}
}
