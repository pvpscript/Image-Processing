package main;

import java.util.ArrayList;
import java.util.List;

import org.opencv.core.Mat;
import org.opencv.highgui.HighGui;
import org.opencv.imgcodecs.Imgcodecs;

import dataStructures.Pair;
import imageUtils.Image;
import imageUtils.Pixel.Type;
import operations.Convolution;
import operations.Description;
import operations.Histogram;
import operations.Morphology;
import operations.Segmentation;
import operations.Zoom;

public class Test {
	// Diretório da imagem.
	private static final String IMAGES_PATH = "/home/p3t3r/UEL/comp_grafica/primeiro_semestre/images/";
	private static final String temp = "/home/pete/Desktop/cg_images";
		
	public static void runTests() throws Exception {
		double[] greenPixel = {0, 255, 0};
		double[] paintGreen = {11, 255, 0};
		double[] paintRed = {0, 14, 255};
		double[] redPixel = {0, 0, 255};
		double[] someColor = {191, 64, 191};
		double[] orange = {0, 140, 255};
		double[] black = {0, 0, 0};
		double[] white = {255, 255, 255};
		
		//teste(0, null, null, null, null, 0, 0, 0);
		
//		List<Integer> test = new ArrayList<Integer>();
//		test.add(1);
//		test.add(2);
//		test.add(3);
//		
//		test.add(1, 5);
//		test.add(3, 4);
//		
//		// Resultado esperado: 1, 5, 2, 4, 3
//		
//		for (int i : test) {
//			System.out.println("Val: " + i);
//		}
//		
//		System.out.println("\nTest 2:");
//		
//		List<Integer> test2 = new ArrayList<Integer>();
//		test2.add(1);
//		test2.add(2);
//		test2.add(3);
//		test2.add(4);
//		test2.add(5);
//		
//		for(int i = 1; i < 10; i++) {
//			test2.add(i, 10 + i);
//		}
//		
//		for (int i : test2) {
//			System.out.println("Val: " + i);
//		}
		
		Image image = openImage(IMAGES_PATH, "face.png");
		
		HighGui.imshow("Original image!", image.acquireMat());
		HighGui.waitKey(0);
		
		Image eqColor = Histogram.equalizeColor(image);
		Image eqGray = Histogram.equalizeGray(image);
		
		HighGui.imshow("Equalized gray!", eqGray.acquireMat());
		HighGui.waitKey(0);
		
		HighGui.imshow("Equalized color!", eqColor.acquireMat());
		HighGui.waitKey(0);
		
		System.exit(0);
		
	//	Image image = openImage(IMAGES_PATH, "beyond.png");
		
		HighGui.imshow("Original image!", image.acquireMat());
		HighGui.waitKey(0);
		
		Image sqrdIn = Zoom.squaredIn(image);
		
		HighGui.imshow("Original -> Squared In", sqrdIn.acquireMat());
		HighGui.waitKey(0);
		
		Image sqrdOut = Zoom.squaredOut(image);
		
		HighGui.imshow("Original -> Squared Out", sqrdOut.acquireMat());
		HighGui.waitKey(0);
		
		Image sqrdInOut = Zoom.squaredOut(sqrdIn);
		
		HighGui.imshow("Squared In -> Squared Out", sqrdInOut.acquireMat());
		HighGui.waitKey(0);
		
		Image sqrdOutIn = Zoom.squaredIn(sqrdOut);
		
		HighGui.imshow("Squared Out -> Squared In", sqrdOutIn.acquireMat());
		HighGui.waitKey(0);
		
		//----------------------------------------
		
		Image linIn = Zoom.linearIn(image);
		
		HighGui.imshow("Original -> Linear In", linIn.acquireMat());
		HighGui.waitKey(0);
		
		Image linOut = Zoom.linearOut(image);
		
		HighGui.imshow("Original -> Linear Out", linOut.acquireMat());
		HighGui.waitKey(0);
		
		Image linInOut = Zoom.linearOut(linIn);
		
		HighGui.imshow("Linear In -> Linear Out", linInOut.acquireMat());
		HighGui.waitKey(0);
		
		Image linOutIn = Zoom.linearIn(linOut);
		
		HighGui.imshow("Linear Out -> Linear In", linOutIn.acquireMat());
		HighGui.waitKey(0);
		
		System.exit(0);
		
		Image img = openImage(temp, "hough_teste.png");
		
		Image fourier = Convolution.bandPass(img, 0.1, 20).getKey();
		
//		System.out.println("Rows: " + img.getRows() + " Cols: " + img.getCols());
//		Image kmeans = Segmentation.kMeans(img, 10, null);
//		Image isodata = Segmentation.isodata(img, 5, 10);
		
//		saveImage(temp, "kmeans_test.png", kmeans);
//		saveImage(temp, "isodata_test.png", isodata);
		saveImage(temp, "fourier_test.png", fourier);
		
//		saveImage(temp, "simple_bug_result.png", Description.simpleBugFollower(img));
//		saveImage(temp, "backtrack_bug_result.png", Description.backtrackingBugFollower(img));
		
//		Image img = openImage(temp, "frapuccino.png");
//		saveImage(temp, "lixo_verde.png", Convolution.detectBorder(img, 200, Convolution.BorderDetectMethod.SOBEL));
//		System.out.println("qweqweqwe");
//		String chain = Description.chainCode(img);
//		System.out.println("lol");
//		System.out.println(chain);
//		Pair<Image, String> qwe = Description.mooreBoundaryTracking(img);
//		
//		System.out.println("Chain code: " + qwe.getValue());
//		Description.shapeNumber(qwe.getValue(), Description.Direction.CLOCKWISE);
//		
//		saveImage(temp, "lixorama.png", qwe.getKey());
		
		System.exit(0);
		
//		Image res = Morphology.floodFillPoint(img, greenPixel, 263, 463, 0);
		Image res = Morphology.floodFillPoint(img, white, 264, 463, 0);
		Image res2 = Morphology.floodFillCenter(img, black, 0);
		Image res3 = Morphology.floodFillColor(img, paintGreen, paintRed, 0);
		
//		Image res = Morphology.floodFillPoint(img, greenPixel, 185, 400, 0);
//		Image res2 = Morphology.floodFillCenter(img, someColor, 0);
//		Image res3 = Morphology.floodFillColor(img, paintGreen, orange, 0);

//		Image res = Morphology.floodFillPoint(img, greenPixel, 0, 0, 0);
//		Image res2 = Morphology.floodFillCenter(img, someColor, 20);
//		Image res3 = Morphology.floodFillColor(img, paintRed, paintGreen, 80);

		
		HighGui.imshow("Original image!", img.acquireMat());
		HighGui.waitKey(0);		
		
		HighGui.imshow("Fill", res.acquireMat());
		HighGui.waitKey(0);
		
		HighGui.imshow("Original image!", img.acquireMat());
		HighGui.waitKey(0);
		
		HighGui.imshow("Fill middle color", res2.acquireMat());
		HighGui.waitKey(0); 
		
		HighGui.imshow("Original image!", img.acquireMat());
		HighGui.waitKey(0);
		
		HighGui.imshow("Fill color: green -> orange", res3.acquireMat());
		HighGui.waitKey(0);
		
		Image img2 = openImage(IMAGES_PATH, "face.png");
		Image img3 = openImage(IMAGES_PATH, "forest.jpg");
		Image img4 = openImage(IMAGES_PATH, "lake.jpg");
		Image img5 = openImage(IMAGES_PATH, "lena.png");
		Image img6 = openImage(IMAGES_PATH, "taj_mahal.png");
		Image img7 = openImage(IMAGES_PATH + "/histogram/", "gray_equalized2.png");
		Image img8 = openImage(IMAGES_PATH + "/histogram/", "colored_equalized3.png");
		
		System.out.println("blur.png: " + Histogram.getConstrast(img));
		System.out.println("face.png: " + Histogram.getConstrast(img2));
		System.out.println("forest.jpg: " + Histogram.getConstrast(img3));
		System.out.println("lake.jpg: " + Histogram.getConstrast(img4));
		System.out.println("lena.png: " + Histogram.getConstrast(img5));
		System.out.println("taj_mahal.png: " + Histogram.getConstrast(img6));
		System.out.println("grey_equalized: " + Histogram.getConstrast(img7));
		System.out.println("color_equalized: " + Histogram.getConstrast(img8));


		//System.out.println("PROCESSING...");
		
//		HighGui.imshow("Original image!", img.getImage());
//		HighGui.waitKey(0);
//		
//		HighGui.imshow("Mean", Convolution.mean(img).getImage());
//		HighGui.waitKey(0);
//		
////		for(int i = 0; i < 30; i++)
////			img = Convolution.median(img);
//		
//		HighGui.imshow("Median", Convolution.median(img).getImage());
//		HighGui.waitKey(0);
//		
//		System.exit(0);
//		
//		HighGui.imshow("Bordered. Threshold = 20", Convolution.sobel(Gray.grayScale(img), 20).getImage());
//		HighGui.waitKey(0);
//		HighGui.imshow("Bordered. Threshold = 50", Convolution.sobel(Gray.grayScale(img), 50).getImage());
//		HighGui.waitKey(0);
//		HighGui.imshow("Bordered. Threshold = 100", Convolution.sobel(Gray.grayScale(img), 100).getImage());
//		HighGui.waitKey(0);
//		HighGui.imshow("Bordered. Threshold = 150", Convolution.sobel(Gray.grayScale(img), 150).getImage());
//		HighGui.waitKey(0);
//		
//		
//		System.exit(0);
//
//		double[] blackPixel = {0, 0, 0};
//		double[] whitePixel = {255, 255, 255};
//		
//		Mat modelMatrix = new Mat(3, 3, img.getImageType());
//		for(int i = 0; i < modelMatrix.rows(); i++) {
//			for(int j = 0; j < modelMatrix.cols(); j++) {
//				modelMatrix.put(i, j, whitePixel);
//			}
//		}
//		modelMatrix.put(0, 1, blackPixel);
//		modelMatrix.put(1, 0, blackPixel);
//		modelMatrix.put(1, 1, blackPixel);
//		modelMatrix.put(1, 2, blackPixel);
//		modelMatrix.put(2, 1, blackPixel);
//		Image model = new Image(modelMatrix, Type.BGR);
//		/*
//		 * Modelo:
//		 * 
//		 * 010
//		 * 111
//		 * 010
//		 */
//		
//		Image skeletonized = Morphology.skeletonize(Gray.binary(img), model);
//
//		HighGui.imshow("Original image!", img.getImage());
//		HighGui.waitKey(0);
//		HighGui.imshow("Binary image!", Gray.binary(img).getImage());
//		HighGui.waitKey(0);
//		HighGui.imshow("Skeletonized image!", skeletonized.getImage());
//		HighGui.waitKey(0);
//		
//		saveImage(IMAGES_PATH, "esqueletinho_teste.png", skeletonized);
//		
//		HighGui.destroyAllWindows();
//		
//		System.out.println("DONE");
//		
//		System.exit(0);
	}
	
	public static Image openImage(String path, String name) {
		String fullPath = path + "/" + name;
		Mat matrix = Imgcodecs.imread(fullPath);
	
		return !matrix.empty() ? new Image(matrix, Type.BGR) : null;
	}
	
	public static void saveImage(String path, String name, Image image) {
		String fullPath = path + "/" + name;
		
		Imgcodecs.imwrite(fullPath, image.acquireMat());
	}
}
