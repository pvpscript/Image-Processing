package dataStructures;

import java.util.ArrayList;
import java.util.List;

import imageUtils.Pixel;

public class Centroid {
	public double[] color;
	public List<Pixel> group;
	
	public Centroid(double[] color, ArrayList<Pixel> group) {
		this.color = color;
		this.group = group;
	}
}
