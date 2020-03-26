package main;

import java.awt.EventQueue;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.imgcodecs.Imgcodecs;

import imageUtils.Image;
import imageUtils.Pixel.Type;
import view.GUI;

public class Main {
	public static void main(String[] args) throws Exception {
		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
		
		//Test.runTests();
		
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					GUI window = new GUI();
					window.getFrame().setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
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
