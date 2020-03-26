package operations;

import org.opencv.core.Mat;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;

import dataStructures.Misc;
import dataStructures.Pair;
import imageUtils.Image;
import imageUtils.Pixel;
import imageUtils.Point;
import imageUtils.Image.Fill;

public class Description {
	public enum ChainDirection {
		CLOCKWISE,
		COUNTERCLOCKWISE
	};
	
	public static Image detectLines(Image image,
			Convolution.BorderDetectMethod bdm, int rho, double theta,
			int borderThreshold, int houghThreshold, int minLinLength,
			int maxLineGap) {
		Image bordered = Convolution.detectBorder(image, borderThreshold, bdm);
		Mat lines = new Mat();
		Mat bordMat = bordered.acquireMat();
		Mat res = new Mat();
		
		Imgproc.cvtColor(bordMat, res, Imgproc.COLOR_BGR2GRAY);
		Imgproc.HoughLinesP(res, lines, rho,
				theta, houghThreshold, minLinLength, maxLineGap);
	
		Imgproc.cvtColor(res, res, Imgproc.COLOR_GRAY2BGR);

		for(int i = 0; i < lines.rows(); i++) {
			double[] line = lines.get(i, 0);
			org.opencv.core.Point p1 =
					new org.opencv.core.Point(line[0], line[1]);
			org.opencv.core.Point p2 =
					new org.opencv.core.Point(line[2], line[3]);
			Imgproc.line(res, p1, p2, new Scalar(0, 0, 255));
		}
			
		return new Image(res, image.getPixelsType());
	}
	
	public static Pair<Image, String> mooreBoundaryTracking(Image image) {
		Pixel[][] pixels = image.getPixels();
		double[] blackPixel = {0, 0, 0};
		double[] color;
		
		for(int i = 0; i < image.getRows(); i++) {
			for(int j = 0; j < image.getCols(); j++) {
				color = pixels[i][j].bgr();
				if (Misc.equals(color, blackPixel, 0))
					return runMoore(image, new Point(i, j));
			}
		}
		
		return null;
	}
	
	private static Pair<Image, String> runMoore(Image image, Point starting) {
		int[][] directions = {
				{0, 1},
				{1, 1},
				{1, 0},
				{1, -1},
				{0, -1},
				{-1, -1},
				{-1, 0},
				{-1, 1}
		};
		double[] blackPixel = {0, 0, 0};
		Pixel[][] pixels = image.getPixels();
		Point b;
		Point b1 = new Point(-1, -1);
		Point last = new Point();
		Point c;
		String chainCode = "";
	
		Image result = new Image(image.getRows(), image.getCols(),
				image.getImageType(), image.getPixelsType(), Fill.WHITE);
		Pixel[][] resultPixels = result.getPixels();
		
		b = starting;
		c = new Point(b.getX(), b.getY() - 1);
		
		int d;
		int i;
		int x;
		int y;
		int cX = 0;
		int cY = 0;
		int lastCX;
		int lastCY;
		int nextX;
		int nextY;
		
		boolean ranOnce = false;
		
		while(true) {
			x = (int) b.getX();
			y = (int) b.getY();
			last.setX(x);
			last.setY(y);
			
			resultPixels[x][y] = new Pixel(blackPixel, last,
					image.getPixelsType());
			
			for(d = 0; d < 8; d++) {
				cX = x + directions[d][0];
				cY = y + directions[d][1];
				
				if (cX == c.getX() && cY == c.getY())
					break;
			}
			
			lastCX = x + directions[d][0];
			lastCY = y + directions[d][1];
			
			for(i = d + 1; i != d; i = (i + 1) % 8) {
				nextX = x + directions[i][0];
				nextY = y + directions[i][1];
				
				if (nextX < image.getRows() && nextY < image.getCols() &&
						nextX >= 0 && nextY >= 0) {
					if (Misc.equals(pixels[nextX][nextY].bgr(), blackPixel, 0)) {
						b = pixels[nextX][nextY].getPoint();
						c.setX(lastCX);
						c.setY(lastCY);
													
						if (b1.getX() == -1 && b1.getY() == -1) {
							b1.setX(nextX);
							b1.setY(nextY);
						}
						
						break;
					}
				}
				
				lastCX = nextX;
				lastCY = nextY;
			}
			
			if (last.getX() == starting.getX() &&
					last.getY() == starting.getY() &&
					b.getX() == b1.getX() && b.getY() == b1.getY() &&
					ranOnce)
				break;
			
			chainCode += i;
			
			ranOnce = true;
		}
		
		return new Pair<Image, String>(result, chainCode);
	}
	
	public static Pair<String, String> shapeNumber(String chainCode,
			ChainDirection dir) {
		String difference = "";
		String circular;
		String minDiff;
		int ccLen = chainCode.length();
		
		for(int i = 0; i < ccLen; i++) {
			difference += normalize(
					chainCode.charAt((ccLen - 1 + i) % ccLen) - '0',
					chainCode.charAt(i) - '0', dir);
		}
		minDiff = difference;
		
		for(int i = 0; i < ccLen; i++) {
			circular = "";
			for(int j = i; j < ccLen + i; j++) {
				circular += difference.charAt((ccLen - 1 + j) % ccLen);
			}
			
			if (circular.compareTo(difference) <= 0 &&
					circular.compareTo(minDiff) <= 0)
				minDiff = circular;
		}
		
		return new Pair<String, String>(difference, minDiff);
	}
	
	private static int normalize(int curr, int next, ChainDirection dir) {
		switch(dir) {
		case CLOCKWISE:
			return (next - curr + 8) % 8;
		case COUNTERCLOCKWISE:
			return (curr - next + 8) % 8;
		default:
			return -1;
		}
	}
	
	private enum BugDirection {
		DOWN(0),
		UP(1),
		RIGHT(2),
		LEFT(3);
		
		private int val;
		
		private BugDirection(int val) {
			this.val = val;
		}
		
		public int getVal() {
			return this.val;
		}
	}
	
	public static Image simpleBugFollower(Image image) {
		int[][][] directions = {
				// Down
				{
					{0, -1, BugDirection.LEFT.getVal()}, // Not region			
					{0, 1, BugDirection.RIGHT.getVal()} // Region
				},
				
				// Up
				{
					{0, 1, BugDirection.RIGHT.getVal()},
					{0, -1, BugDirection.LEFT.getVal()}
				},
				
				// Right
				{
					{1, 0, BugDirection.DOWN.getVal()},
					{-1, 0, BugDirection.UP.getVal()}
				},
				
				// Left
				{
					{-1, 0, BugDirection.UP.getVal()},
					{1, 0, BugDirection.DOWN.getVal()}
				}
		};
		Image result = new Image(image.getRows(), image.getCols(),
				image.getImageType(), image.getPixelsType(), Fill.WHITE);
		Pixel[][] pixels = image.getPixels();
		Pixel[][] resultPixels = result.getPixels();
		Pixel pixel;
		double[] blackPixel = {0, 0, 0};
		double[] color;
		int bX;
		int bY;
		int d;
		Point starting = null;
		boolean run = true;
		
		for(int i = 0; i < image.getRows(); i++) {
			if (run) {
				for(int j = 0; j < image.getCols(); j++) {
					color = pixels[i][j].bgr();
					if (Misc.equals(color, blackPixel, 0)) {
						run = false;
						starting = new Point(i, j);
						break;
					}
				}
			} else {
				break;
			}
		}
		
		if (starting != null) {
			bX = (int)starting.getX();
			bY = (int)starting.getY();
			d = BugDirection.DOWN.getVal();
			
			do {
				if (bX >= 0 && bY >= 0 &&
						bX < image.getRows() && bY < image.getCols()) {
					pixel = pixels[bX][bY];
					if (Misc.equals(pixel.bgr(), blackPixel, 0)) {
						resultPixels[bX][bY] = new Pixel(blackPixel,
								pixel.getPoint(), image.getPixelsType());
						bX += directions[d][1][0];
						bY += directions[d][1][1];
						d = directions[d][1][2];
					} else {
						bX += directions[d][0][0];
						bY += directions[d][0][1];
						d = directions[d][0][2];
					}
				} else {
					bX += directions[d][0][0];
					bY += directions[d][0][1];
					d = directions[d][0][2];
				}
			} while(bX != starting.getX() || bY != starting.getY());
		}
		
		return result;
	}
	
	public static Image backtrackingBugFollower(Image image) {
		int[][][] directions = {
				// Down
				{
					{0, -1, BugDirection.LEFT.getVal()}, // Turn right	
					{-1, 0, BugDirection.UP.getVal()} // Backtrack
				},
				
				// Up
				{
					{0, 1, BugDirection.RIGHT.getVal()},
					{1, 0, BugDirection.DOWN.getVal()}
				},
				
				// Right
				{
					{1, 0, BugDirection.DOWN.getVal()},
					{0, -1, BugDirection.LEFT.getVal()}
				},
				
				// Left
				{
					{-1, 0, BugDirection.UP.getVal()},
					{0, 1, BugDirection.RIGHT.getVal()}
				}
		};
		Image result = new Image(image.getRows(), image.getCols(),
				image.getImageType(), image.getPixelsType(), Fill.WHITE);
		Pixel[][] pixels = image.getPixels();
		Pixel[][] resultPixels = result.getPixels();
		Pixel pixel;
		double[] blackPixel = {0, 0, 0};
		double[] color;
		Point starting = null;
		int bX;
		int bY;
		int d;
		boolean run = true;
		
		for(int i = 0; i < image.getRows(); i++) {
			if (run) {
				for(int j = 0; j < image.getCols(); j++) {
					color = pixels[i][j].bgr();
					if (Misc.equals(color, blackPixel, 0)) {
						run = false;
						starting = new Point(i - 1, j);
						break;
					}
				}
			} else {
				break;
			}
		}
		
		if (starting != null) {
			bX = (int)starting.getX() + 1;
			bY = (int)starting.getY();
			d = BugDirection.DOWN.getVal();

			do {
				if (bX >= 0 && bY >= 0 &&
						bX < image.getRows() && bY < image.getCols()) {
					pixel = pixels[bX][bY];
					if (Misc.equals(pixel.bgr(), blackPixel, 0)) {
						resultPixels[bX][bY] = new Pixel(blackPixel,
								pixel.getPoint(), image.getPixelsType());
						bX += directions[d][1][0];
						bY += directions[d][1][1];
						d = directions[d][1][2];
					}
				}
					
				bX += directions[d][0][0];
				bY += directions[d][0][1];
				d = directions[d][0][2];
			} while(bX != starting.getX() || bY != starting.getY());
		}
		
		return result;
	}
}
