package dataStructures;

import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;

import imageUtils.Point;

public class Misc {
	public static boolean equals(double[] a, double[] a2, int offset) {
		if (a[0] >= a2[0] - offset && a[0] <= a2[0] + offset &&
				a[1] >= a2[1] - offset && a[1] <= a2[1] + offset &&
				a[2] >= a2[2] - offset && a[2] <= a2[2] + offset)
			return true;
		
		return false;
	}
	
	public static boolean equals(int[] a, int[] a2, int offset) {
		if (a[0] >= a2[0] - offset && a[0] <= a2[0] + offset &&
				a[1] >= a2[1] - offset && a[1] <= a2[1] + offset &&
				a[2] >= a2[2] - offset && a[2] <= a2[2] + offset)
			return true;
		
		return false;
	}
	
	public static void roundArray(double[] arr) {
		for(int i = 0; i < arr.length; i++)
			arr[i] = Math.round(arr[i]);
	}
	
	public static void floorArray(double[] arr) {
		for(int i = 0; i < arr.length; i++)
			arr[i] = Math.floor(arr[i]);
	}
	
	public static void ceilArray(double[] arr) {
		for(int i = 0; i < arr.length; i++)
			arr[i] = Math.ceil(arr[i]);
	}
	
	public static double meanArray(double[] arr) {
		double res = 0;
		
		for(int i = 0; i < arr.length; i++)
			res += (arr[i] / arr.length);
		
		
		return res;
	}
	
	public static Mat getAffineMatrix(Triangle t1, Triangle t2) {
		double[] A1 = new double[9];
		double[] A2 = new double[9];
		Mat invSrc = new Mat(3, 3, CvType.CV_32FC1);
		Mat invDst = new Mat(3, 3, CvType.CV_32FC1);
		Mat A2Mat = new Mat(3, 3, CvType.CV_32FC1);
		Mat result = new Mat(3, 3, CvType.CV_32FC1);
		
		Point p = t1.getP();
		Point q = t1.getQ();
		Point r = t1.getR();
		
		Point pPrime = t2.getP();
		Point qPrime = t2.getQ();
		Point rPrime = t2.getR();
		
		Point diff = p.sub(r);
		A1[0] = diff.getX();
		A1[1] = diff.getY();
		A1[2] = 0;
		diff = q.sub(r);
		A1[3] = diff.getX();
		A1[4] = diff.getY();
		A1[5] = 0;
		A1[6] = r.getX();
		A1[7] = r.getY();
		A1[8] = 1;
		
		diff = pPrime.sub(rPrime);
		
		A2[0] = diff.getX();
		A2[1] = diff.getY();
		A2[2] = 0;
		diff = qPrime.sub(rPrime);
		A2[3] = diff.getX();
		A2[4] = diff.getY();
		A2[5] = 0;
		A2[6] = rPrime.getX();
		A2[7] = rPrime.getY();
		A2[8] = 1;
		A2Mat.put(0, 0, A2);
		
		invSrc.put(0, 0, A1);
		
		Core.invert(invSrc, invDst);
		Core.gemm(invDst, A2Mat, 1, new Mat(), 0, result, 0);
		
		return result;
	}
}
