package operations;

import java.util.ArrayList;
import java.util.List;

import org.opencv.core.Mat;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;

import org.opencv.core.Point;

import imageUtils.Image;
import parser.Lexical;
import parser.Syntactical;
import parser.Token;

public class Graphic {
	private static void drawXAxis(Mat img, double vertLow, double vertHigh) {
	    int i;
	    int row;
	    double offset;
	 
	    if ((vertLow <= 0.0 && vertHigh <= 0.0) ||
	    		(vertLow >= 0.0 && vertHigh >= 0.0))
	        return;
	 
	    offset = Math.abs(vertLow) + Math.abs(vertHigh);
	    row = (int) Math.round(Math.abs((vertHigh * img.rows()) / offset));
	 
	    if (row < img.rows() && row >= 0)
	        for(i = 0; i< img.cols(); i++)
	            img.put(row, i, new double[] {0, 0, 0});
	}
	 
	private static void drawYAxis(Mat img, double horLow, double horHigh) {
	    int i;
	    int col;
	    double offset;
	 
	    if ((horLow <= 0.0 && horHigh <= 0.0) ||
	    		(horLow >= 0.0 && horHigh >= 0.0))
	        return;
	 
	    offset = Math.abs(horLow) + Math.abs(horHigh);
	    col = (int) Math.round(Math.abs((horLow * img.cols()) / offset));
	 
	    if (col < img.cols() && col >= 0)
	        for(i = 0; i < img.rows(); i++)
	            img.put(i, col, new double[] {0, 0, 0});
	}
	
	public static Image drawFunction(Image image, double vertLow, double vertHigh,
			double horLow, double horHigh, String function, double[] color) {
		Mat img = image.getMatrix();
//		double vertLow = -5;
//		double vertHigh = 10;
//		double horLow = -10;
//		double horHigh = 10;
		double dI;
		int i;
		int val;
		
		List<Point> points = new ArrayList<Point>();
		
	    double step = (Math.abs(horLow) + horHigh) / image.getCols();
	    double offset = (Math.abs(vertLow) + vertHigh);
	    double scale = offset / image.getRows();
	    int row = (int) Math.round((vertHigh * image.getRows() / offset));
	 
	    drawXAxis(img, vertLow, vertHigh);
	    drawYAxis(img, horLow, horHigh);
	    
	    List<Token> tokens = Lexical.analyse(function);
	    
	    for(dI=horLow, i=0; dI < horHigh && i < img.cols(); dI+=step, i++) {
	        val = (int) (Math.round(-Syntactical.eval(tokens, dI) / scale) + row);
	 
        	points.add(new Point(i, val));

	    }
	    
	    Point p1 = points.get(0);
		for (i = 1; i < points.size(); i++) {
			Point p2 = points.get(i);
			if (p1 != null && p2 != null)
				Imgproc.line(img, p1, p2,
						new Scalar(color[0], color[1], color[2]));
			p1 = p2;
		}
	    

	    return new Image(img, image.getPixelsType());
	}
}
