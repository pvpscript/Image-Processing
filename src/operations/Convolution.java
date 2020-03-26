package operations;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.imgproc.Imgproc;

import dataStructures.Pair;
import imageUtils.ConvolutionMethods;
import imageUtils.Image;
import imageUtils.Pixel;
import imageUtils.Point;
import imageUtils.Image.Fill;
import imageUtils.Pixel.Type;

public class Convolution {	
	public enum BorderDetectMethod {
		PREWITT,
		SOBEL,
		KIRSCH
	};
	
	private static int convolute(Image image, int[][] matrix,
			int offsetX, int offsetY) {
		Pixel[][] pixels = image.getPixels();
		double[] rawPixel;
		int sum = 0;
		
		for(int i = -1; i < 2; i++)
			for(int j = -1; j < 2; j++)
				if (i + offsetX >= 0 && j + offsetY >= 0 &&
						i + offsetX < image.getRows() &&
						j + offsetY < image.getCols()) {
					rawPixel = pixels[i + offsetX][j + offsetY].raw();
					sum += rawPixel[0] * matrix[i + 1][j + 1];
				}
		
		return sum;
	}
	
	private static double[] convoluteMedian(Image image, int offsetX, int offsetY) {
		Pixel[][] pixels = image.getPixels();
		double[] vshPixel;
		double[] pixel = new double[3];
		List<double[]> arr = new ArrayList<double[]>();
		Comparator<double[]> compareDouble =
				(double[] v1, double[] v2) -> {
					if (v1[0] > v2[0])
						return 1;
					else if (v1[0] < v2[0])
						return -1;
					else
						return 0;
				};
		int i = 0;
		
		for(int j = -1; j < 2; j++) {
			for(int k = -1; k < 2; k++) {
				if (j + offsetX >= 0 && k + offsetY >= 0 &&
						j + offsetX < image.getRows() &&
						k + offsetY < image.getCols()) {
					vshPixel = (pixels[j + offsetX][k + offsetY]).vsh();
					arr.add(vshPixel);
					i++;
				}
			}
		}
		
		arr.sort(compareDouble);
		if (i % 2 == 0) {
			double[] tempPixel1 = arr.get((i-1)/2);
			double[] tempPixel2 = arr.get((i-1)/2 + 1);
			
			pixel[0] = (tempPixel1[0] + tempPixel2[0]) / 2;
			pixel[1] = (tempPixel1[1] + tempPixel2[1]) / 2;
			pixel[2] = (tempPixel1[2] + tempPixel2[2]) / 2;
		} else
			pixel = arr.get((i-1)/2 + 1);
		
		return pixel;
	}
	
	private static int[] nucleusMean(Image image, int offsetX, int offsetY) {
		Pixel[][] pixels = image.getPixels();
		double[] rawPixel;
		int[] pixel = new int[3];
		int sum;
		int count;
		
		for(int i = 0; i < 3; i++) {
			sum = 0;
			count = 0;
			
			for(int j = -1; j < 2; j++) {
				for(int k = -1; k < 2; k++) {
					if (j + offsetX >= 0 && k + offsetY >= 0 &&
							j + offsetX < image.getRows() &&
							k + offsetY < image.getCols()) {
						rawPixel = pixels[j + offsetX][k + offsetY].raw();
						sum += rawPixel[i];
						count++;
					}
				}
			}
			
			pixel[i] = sum / count;
		}
		
		return pixel;
	}
	
	public static Image detectBorder(Image image, int threshold,
			BorderDetectMethod method) {
//		Pair<int[][], int[][]> convMats = ConvolutionMethods.sobel();
		List<int[][]> convMats;
		Image bordered = new Image(image.getRows(), image.getCols(),
				image.getImageType(), Type.BGR, Fill.BLACK);
		Pixel[][] borderedPixel = bordered.getPixels();
		double[] whitePixel = {255, 255, 255};
		double sum = 0;
		
		switch(method) {
		case PREWITT:
			convMats = ConvolutionMethods.prewitt(); break;
		case SOBEL:
			convMats = ConvolutionMethods.sobel(); break;
		case KIRSCH:
			convMats = ConvolutionMethods.kirsch(); break;
		default:
			convMats = ConvolutionMethods.sobel();
		}
		
		for(int i = 0; i < image.getRows(); i++) {
			for(int j = 0; j < image.getCols(); j++) {
				for(int[][] matrix : convMats) {
					sum += Math.pow(convolute(image, matrix, i, j), 2);
				}
				
				if (Math.sqrt(sum) > threshold)
					borderedPixel[i][j] = new Pixel(whitePixel,
							new Point(i, j), image.getPixelsType());
				
				sum = 0;
			}
		}
		
		return bordered;
	}
	
	public static Image mean(Image image) {
		Image meanConv = new Image(image.getRows(), image.getCols(),
				image.getImageType(), Type.BGR, Fill.WHITE);
		Pixel[][] pixels = meanConv.getPixels();
		int pixel[];
		
		for(int i = 0; i < image.getRows(); i++) {
			for(int j = 0; j < image.getCols(); j++) {
				pixel = nucleusMean(image, i, j);
				
				pixels[i][j] = new Pixel(pixel,
						new Point(i, j), image.getPixelsType());
			}
		}
		
		return meanConv;
	}
	
	public static Image median(Image image) {
		Image meanConv = new Image(image.getRows(), image.getCols(),
				image.getImageType(), Type.VSH, Fill.WHITE);
		Pixel[][] pixels = meanConv.getPixels();
		double[] pixel;
		
		for(int i = 0; i < image.getRows(); i++) {
			for(int j = 0; j < image.getCols(); j++) {
				pixel = convoluteMedian(image, i, j);
				
				pixels[i][j] = new Pixel(pixel,
						new Point(i, j), meanConv.getPixelsType());
			}
		}
		
		return Conversion.toBgr(meanConv);
	}
	
	private static Mat fourier(Image image) {
		Mat grayOriginal = new Mat();
		
		Imgproc.cvtColor(image.acquireMat(), grayOriginal,
				Imgproc.COLOR_BGR2GRAY);
		
		Mat normalized = new Mat();
		grayOriginal.convertTo(normalized, CvType.CV_32FC1, 1.0 / 255.0);
		
		List<Mat> complex = new ArrayList<Mat>();
		complex.add(normalized);
		complex.add(Mat.zeros(normalized.size(), CvType.CV_32F));
		
		Mat dftMerged = new Mat();
		Core.merge(complex, dftMerged);
		Mat dftMat = new Mat();
		Core.dft(dftMerged, dftMat, Core.DFT_COMPLEX_INPUT);
		
		return dftMat;
	}
	
	public static Pair<Image, Image> highPass(Image image, double keep) {
		Mat dftMat = fourier(image);
		Mat freqFilterImg = Mat.zeros(dftMat.rows(), dftMat.cols(), CvType.CV_8UC1);
		
		for(int i = 0; i < dftMat.rows(); i++) {
			for(int j = 0; j < dftMat.cols(); j++) {
				if (!(i > dftMat.rows() * keep && i < (dftMat.rows() * (1 - keep)) ||
						j > dftMat.cols() * keep && j < (dftMat.cols() * (1 - keep)))) {
					dftMat.put(i, j, new double[] {0, 0});
					
					freqFilterImg.put(i, j, new double[] {255});
				}
			}
		}
		
		Mat inv = new Mat();
		Core.dft(dftMat, inv, Core.DFT_INVERSE | Core.DFT_REAL_OUTPUT |
				Core.DFT_SCALE);	
		Core.normalize(inv, inv, 0, 255, Core.NORM_MINMAX, CvType.CV_8UC1);
		
		return new Pair<Image, Image>(new Image(inv, Type.BGR),
				new Image(freqFilterImg, Type.BGR));
	}
	
	public static Pair<Image, Image> lowPass(Image image, double keep) {
		Mat dftMat = fourier(image);
		Mat freqFilterImg = Mat.zeros(dftMat.rows(), dftMat.cols(), CvType.CV_8UC1);
		
		for(int i = 0; i < dftMat.rows(); i++) {
			for(int j = 0; j < dftMat.cols(); j++) {
				if (i > dftMat.rows() * keep && i < (dftMat.rows() * (1 - keep)) ||
						j > dftMat.cols() * keep && j < (dftMat.cols() * (1 - keep))) {
					dftMat.put(i, j, new double[] {0, 0});
					
					freqFilterImg.put(i, j, new double[] {255});
				}
			}
		}
		
		Mat inv = new Mat();
		Core.dft(dftMat, inv, Core.DFT_INVERSE | Core.DFT_REAL_OUTPUT |
				Core.DFT_SCALE);	
		Core.normalize(inv, inv, 0, 255, Core.NORM_MINMAX, CvType.CV_8UC1);
		
		return new Pair<Image, Image>(new Image(inv, Type.BGR),
				new Image(freqFilterImg, Type.BGR));
	}
	
	public static Pair<Image, Image> bandPass(Image image, double keep,
			int centerOffset) {
		Mat dftMat = fourier(image);
		Mat freqFilterImg = Mat.zeros(dftMat.rows(), dftMat.cols(), CvType.CV_8UC1);
		int centerX = dftMat.rows() / 2;
		int centerY = dftMat.cols() / 2;
		

		for(int i = 0; i < dftMat.rows(); i++) {
			for(int j = 0; j < dftMat.cols(); j++) {
				if (!((i > centerX - centerOffset && i < centerX + centerOffset) &&
						j > centerY - centerOffset && j < centerY + centerOffset) &&
						(i > dftMat.rows() * keep && i < (dftMat.rows() * (1 - keep)) ||
								j > dftMat.cols() * keep && j < (dftMat.cols() * (1 - keep)))) {
					dftMat.put(i, j, new double[] {0, 0});
					
					freqFilterImg.put(i, j, new double[] {255});
				}
			}
		}
		
		Mat inv = new Mat();
		Core.dft(dftMat, inv, Core.DFT_INVERSE | Core.DFT_REAL_OUTPUT |
				Core.DFT_SCALE);	
		Core.normalize(inv, inv, 0, 255, Core.NORM_MINMAX, CvType.CV_8UC1);
		
		return new Pair<Image, Image>(new Image(inv, Type.BGR),
				new Image(freqFilterImg, Type.BGR));
	}
	
//	private Mat lowPassFilter(Mat freqArray) {
//		Mat filter = Mat.ones(freqArray.rows(), freqArray.cols(), CvType.CV_8UC1);
//		
//		
//		return null;
//	}
	
//	private Mat invertDft(Mat src) {
//		Mat inv = new Mat();
//		
////		Core.idft(src, dst);
//	}
 }
