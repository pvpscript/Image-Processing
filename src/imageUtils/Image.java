package imageUtils;

import org.opencv.core.CvType;
import org.opencv.core.Mat;

import imageUtils.Pixel.Type;

public class Image {
	public enum Channel {
		FIRST,
		SECOND,
		THIRD
	}
	public enum Fill {
		BLACK,
		WHITE
	}
	
	private Mat matrix;
	private int channels;
	private int rows;
	private int cols;
	private int imageType;
	private Type pixelsType;
	private Pixel[][] pixels;
	
	public Image(Mat matrix, Type pixelsType) {
		this.matrix = matrix;
		this.channels = matrix.channels();
		this.rows = matrix.rows();
		this.cols = matrix.cols();
		this.imageType = matrix.type();
		this.pixelsType = pixelsType;
		
		this.pixels = createImage(pixelsType);
	}
	
	public Image(Pixel[][] pixels, Mat matrix, Type pixelsType) {
		this.pixels = pixels;
		this.matrix = matrix;
		this.rows = matrix.rows();
		this.cols = matrix.cols();
		this.imageType = matrix.type();
		this.pixelsType = pixelsType;
	}
	
	public Image(VisitPixel[][] pixels, Mat matrix, Type pixelsType) {
		this.pixels = pixels;
		this.matrix = matrix;
		this.rows = matrix.rows();
		this.cols = matrix.cols();
		this.imageType = matrix.type();
		this.pixelsType = pixelsType;
	}
	
	public Image(int rows, int cols, int type, Type pixelsType, Fill fill) {
		double[] pixel = new double[3];
		Mat matrix = new Mat(rows, cols, type);
		
		switch(fill) {
		case BLACK:
			pixel[0] = 0;
			pixel[1] = 0;
			pixel[2] = 0;
			break;
		case WHITE:
			pixel[0] = 255;
			pixel[1] = 255;
			pixel[2] = 255;
			break;
		}
		
		for(int i = 0; i < rows; i++) {
			for(int j = 0; j < cols; j++) {
				matrix.put(i, j, pixel);
			}
		}
		
		this.matrix = matrix;
		this.rows = matrix.rows();
		this.cols = matrix.cols();
		this.imageType = matrix.type();
		this.pixelsType = pixelsType;
		
		this.pixels = createImage(pixelsType);
	}
	
	private Pixel[][] createImage(Type type) {
		Pixel[][] pixels = new Pixel[this.rows][this.cols];
		
		for(int i = 0; i < this.rows; i++) {
			for(int j = 0; j < this.cols; j++) {
				pixels[i][j] = new Pixel(this.matrix.get(i, j),
						new Point(i, j), type);
			}
		}
		
		return pixels;
	}
	
	public Pixel[][] actualPixelPlacement() {
		Pixel[][] pixels = new Pixel[this.rows][this.cols];
		Point point;
		
		for (int i = 0; i < this.rows; i++) {
			for (int j = 0; j < this.cols; j++) {
				point = this.pixels[i][j].getPoint();
				
				if (point.getX() < this.rows && point.getY() < this.cols &&
						point.getX() >= 0 && point.getY() >= 0) {
					pixels[i][j] = this.pixels[(int)point.getX()][(int)point.getY()];
				} else {
					pixels[i][j] = new Pixel(new double[] {0, 0, 0},
							new Point(i, j), this.pixelsType);
				}
			}
		}
		
		return pixels;
	}
	
	public Point getCenterOfMass() {
		Point first = this.pixels[0][0].getPoint();
		Point last = this.pixels[this.rows - 1][this.cols - 1].getPoint();
		double x = (first.getX() + last.getX()) / 2;
		double y = (first.getY() + last.getY()) / 2;
		
		return new Point(x, y);
	}
	
	public Image copyOriginal() {
		return new Image(this.matrix, this.pixelsType);
	}
	
	public Mat acquireMat() {
		Point point;
		double[] rawPixel;
		
		Mat matrix = Mat.zeros(this.rows, this.cols, this.imageType);
		
		for(int i = 0; i < this.rows; i++) {
			for(int j = 0; j < this.cols; j++) {
				point = this.pixels[i][j].getPoint();
				
					if (point.getX() < this.rows && point.getY() < this.cols &&
							point.getX() >= 0 && point.getY() >= 0) {
						rawPixel = this.pixels[(int)point.getX()][(int)point.getY()].raw();
				
						double[] pixel = {
								rawPixel[0],
								rawPixel[1],
								rawPixel[2]
						};
						
						matrix.put(i, j, pixel);
					}
			}
		}
		
		return matrix;
	}
	
	public Mat acquireMat(int channels) {
		Point point;
		double[] rawPixel;
		int type;
		
		switch(channels) {
		case 1: type = CvType.CV_8UC1; break;
		case 2: type = CvType.CV_8UC2; break;
		case 3: type = CvType.CV_8UC3; break;
		default: type = CvType.CV_8UC3; break;
		}
		
		double[] pixel = new double[channels];
		
		Mat matrix = Mat.zeros(this.rows, this.cols, type);
		
		for(int i = 0; i < this.rows; i++) {
			for(int j = 0; j < this.cols; j++) {
				point = this.pixels[i][j].getPoint();
				
					if (point.getX() < this.rows && point.getY() < this.cols &&
							point.getX() >= 0 && point.getY() >= 0) {
						rawPixel = this.pixels[(int)point.getX()][(int)point.getY()].raw();
						
						for(int k = 0; k < channels; k++)
							pixel[k] = rawPixel[k];
						
						matrix.put(i, j, pixel);
					}
			}
		}
		
		return matrix;
	}
	
	public Mat jooj(int type) {
		Point point;
		double[] rawPixel;
//		int type;
//		
//		switch(channels) {
//		case 1: type = CvType.CV_8UC1; break;
//		case 2: type = CvType.CV_8UC2; break;
//		case 3: type = CvType.CV_8UC3; break;
//		default: type = CvType.CV_8UC3; break;
//		}
//		
//		double[] pixel = new double[channels];
		
		Mat matrix = Mat.zeros(this.rows, this.cols, type);
		
		for(int i = 0; i < this.rows; i++) {
			for(int j = 0; j < this.cols; j++) {
				point = this.pixels[i][j].getPoint();
				
					if (point.getX() < this.rows && point.getY() < this.cols &&
							point.getX() >= 0 && point.getY() >= 0) {
						rawPixel = this.pixels[(int)point.getX()][(int)point.getY()].raw();
						
						double[] pixel = {
								rawPixel[0],
								rawPixel[1],
								rawPixel[2]
						};
						
						matrix.put(i, j, pixel);
					}
			}
		}
		
		return matrix;
	}
	
	public Mat acquireMat(Channel channel) {
		Point point;
		double[] rawPixel;
		
		Mat matrix = Mat.zeros(this.rows, this.cols, this.imageType);
		int channelVal;
		
		switch(channel) {
		case FIRST: channelVal = 0; break;
		case SECOND: channelVal = 1; break;
		case THIRD: channelVal = 2; break;
		default: channelVal = 0;
		}
		
		for(int i = 0; i < this.rows; i++) {
			for(int j = 0; j < this.cols; j++) {
				point = this.pixels[i][j].getPoint();
				
					if (point.getX() < this.rows && point.getY() < this.cols &&
							point.getX() >= 0 && point.getY() >= 0) {
						rawPixel = this.pixels[(int)point.getX()][(int)point.getY()].raw();
				
						double[] pixel = {
								rawPixel[channelVal],
								rawPixel[channelVal],
								rawPixel[channelVal]
						};
						
						matrix.put(i, j, pixel);
					}
			}
		}
		
		return matrix;
	}
	
	public Mat getMatrix() {
		return this.matrix;
	}
	
	public int getRows() {
		return this.rows;
	}
	
	public int getCols() {
		return this.cols;
	}
	
	public int getImageType() {
		return this.imageType;
	}
	
	public Type getPixelsType() {
		return this.pixelsType;
	}
	
	public Pixel[][] getPixels() {
		return this.pixels;
	}
	
	public Pixel[][] clonePixels() {
		Pixel[][] pixels = new Pixel[this.rows][this.cols];
		double[] rawPixel;
		Point point;
		Type type;
		
		for(int i = 0; i < this.rows; i++) {
			for(int j = 0; j < this.cols; j++) {
				rawPixel = this.pixels[i][j].raw();
				point = this.pixels[i][j].getPoint();
				type = this.pixels[i][j].getType();
				
				pixels[i][j] = new Pixel(rawPixel, point, type);
			}
		}
		
		return pixels;
	}
	
	@Override
	public Image clone() {
		Pixel[][] pixels = new Pixel[this.rows][this.cols];
		Mat matrix = new Mat(this.rows, this.cols, this.imageType);
		double[] rawPixel;
		Point point;
		Type type;
		
		for(int i = 0; i < this.rows; i++) {
			for(int j = 0; j < this.cols; j++) {
				rawPixel = this.pixels[i][j].raw();
				point = this.pixels[i][j].getPoint();
				type = this.pixels[i][j].getType();
				
				pixels[i][j] = new Pixel(rawPixel, point, type);
				matrix.put(i, j, rawPixel);
			}
		}
		
		return new Image(pixels, matrix, this.pixelsType);
	}
	
	public Image extend(int rows, int cols) {
		Pixel[][] pixels = new Pixel[rows][cols];
		double[] rawPixel;
		double[] whitePixel = {255, 255, 255};
		Point point;
		Type type = this.getPixelsType();
		Mat extendedMat = new Mat(rows, cols, this.imageType);
		
		for(int i = 0; i < rows; i++) {
			for(int j = 0; j < cols; j++) {
				if (i < this.rows && j < this.cols) {
					rawPixel = this.pixels[i][j].raw();
					point = this.pixels[i][j].getPoint();
					
					pixels[i][j] = new Pixel(rawPixel, point, type);
					extendedMat.put(i, j, rawPixel);
				} else {
					pixels[i][j] = new Pixel(whitePixel, new Point(i, j),
							type);
					extendedMat.put(i, j, whitePixel);
				}
					
			}
		}
		
		return new Image(pixels, extendedMat, this.pixelsType);
	}
	
//	public BufferedImage toBufferedImage() {
//		Mat image = getImage();
//		byte[] dataSrc = new byte[this.rows * this.cols * this.imageType];
//		BufferedImage buffImage = new BufferedImage(this.rows, this.cols,
//				BufferedImage.TYPE_3BYTE_BGR);
//		byte[] dataTarget;
//		
//		image.get(0, 0, dataSrc);
//		dataTarget = ((DataBufferByte) buffImage.getRaster().getDataBuffer()).getData();
////		buffImage.getRaster().setDataElements(0, 0, this.rows, this.cols, dataSrc);
//		System.arraycopy(dataSrc, 0, dataTarget, 0, dataSrc.length);
//		
//		return buffImage;
//	}
}
