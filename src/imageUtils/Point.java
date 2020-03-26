package imageUtils;

import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;

public class Point {
	private double x;
	private double y;
	
	public Point(double x, double y) {
		this.x = x;
		this.y = y;
	}
	
	public Point() {
	}

	public double getX() {
		return this.x;
	}

	public void setX(double x) {
		this.x = x;
	}

	public double getY() {
		return this.y;
	}

	public void setY(double y) {
		this.y = y;
	}
	
	public Point add(Point b) {
		return new Point(this.x + b.getX(),this.y + b.getY());
	}
	
	public Point sub(Point b) {
		return new Point(this.x - b.getX(), this.y - b.getY());
	}
	
	public Point multiplyScalar(double scalar) {
		return new Point(this.x * scalar, this.y * scalar);
	}
	
	@Override
	public String toString() {
		return "X: " + this.x + " Y: " + this.y;
	}
	
	public boolean equals(Point b) {
		return (this.x == b.getX() && this.y == b.getY());
	}
	
	public Point rotate(Point reference, double angle) {		
		double x = (this.x - reference.getX()) * Math.cos(angle) -
				(this.y - reference.getY()) * Math.sin(angle) +
				reference.getX();
		double y = (this.x - reference.getX()) * Math.sin(angle) +
				(this.y - reference.getY()) * Math.cos(angle) +
				reference.getY();
		
		
		return new Point(x, y);
	}
	
	public Point translate(Point reference) {
		double x = this.x + reference.getX();
		double y = this.y + reference.getY();
		
		return new Point(x, y);
	}
	
	public Point warpAffine(Mat transform) {
		double[] vector = new double[3];
		Mat vectorMat = new Mat(1, 3, CvType.CV_32FC1);
		Mat result = new Mat(1, 3, CvType.CV_32FC1);

		vector[0] = this.x;
		vector[1] = this.y;
		vector[2] = 1;
		
		vectorMat.put(0, 0, vector);
		Core.gemm(vectorMat, transform, 1, new Mat(), 0, result, 0);
		
		double x = Math.round(result.get(0, 0)[0]);
		double y = Math.round(result.get(0, 1)[0]);
		
		return new Point(x, y);
	}
	
	public Point warpAffineBackwards(Mat transform) {
		double[] vector = new double[3];
		Mat invTransform = new Mat(transform.rows(), transform.cols(),
				transform.channels());
		Mat vectorMat = new Mat(1, 3, CvType.CV_32FC1);
		Mat result = new Mat(1, 3, CvType.CV_32FC1);

		vector[0] = this.x;
		vector[1] = this.y;
		vector[2] = 1;
		
		vectorMat.put(0, 0, vector);
		Core.invert(transform, invTransform);
		Core.gemm(vectorMat, invTransform, 1, new Mat(), 0, result, 0);
		
		double x = Math.round(result.get(0, 0)[0]);
		double y = Math.round(result.get(0, 1)[0]);
		
		return new Point(x, y);
	}
	
	public Point transform(double[] warp) {
		Mat vector = new Mat(1, 3, CvType.CV_32FC1);
		Mat warpMatrix = new Mat(3, 3, CvType.CV_32FC1);
		Mat result = new Mat();  
		double x;
		double y;
		
//		double[] klasp = {0, 1, 0, -1, 0, 0, 6, -4, 1};
//		double[] klasp = {0.66667, -0.66667, 0, 0, 0.5, 0, -2, 4, 1};
	
//		vector.put(0, 0, new float[] {(float) this.x, (float) this.y});
//		warpMatrix.put(0, 0, warp);
		
		vector.put(0, 0, new double[] {this.x, this.y, 1});
		warpMatrix.put(0, 0, warp);
		
		Core.gemm(vector, warpMatrix, 1, new Mat(), 0, result, 0);
		
		x = result.get(0, 0)[0];
		y = result.get(0, 1)[0];

		System.out.println(result.dump());
		System.out.println("X: " + x + " Y: " + y);
		System.exit(0);
		
		return new Point(x, y);
	}
}
