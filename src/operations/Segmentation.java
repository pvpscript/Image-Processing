package operations;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

//import dataStructures.BinaryTree;
//import dataStructures.Centroid;
import dataStructures.Misc;
import dataStructures.Pair;
import imageUtils.Image;
import imageUtils.Pixel;
import imageUtils.Point;
import imageUtils.Image.Fill;

public class Segmentation {
	private static class Centroid {
		public double[] color;
		public List<Pixel> group;
		
		public Centroid(double[] color, ArrayList<Pixel> group) {
			this.color = color;
			this.group = group;
		}
	}
	
	public static Image kMeans(Image image, int clusters,
			ArrayList<Point> centroidPositions) {
		Pixel[][] pixels = image.getPixels();
		Image result = new Image(image.getRows(), image.getCols(),
				image.getImageType(), image.getPixelsType(), Fill.WHITE);
		Pixel[][] resPixels = result.getPixels();
		double[] color;
		Centroid[] centroids;
		int flag;
		
		if (centroidPositions == null) {
			centroids = new Centroid[clusters];
			
			for(int i = 0; i < clusters; i++) {
				int x = (int) Math.round(Math.random() * (image.getRows() - 1));
				int y = (int) Math.round(Math.random() * (image.getCols() - 1));
				
				centroids[i] = new Centroid(pixels[x][y].bgr(),
						new ArrayList<Pixel>());
			}
		} else {
			centroids = new Centroid[centroidPositions.size()];
			
			for(int i = 0; i < centroidPositions.size(); i++) {
				int x = (int) centroidPositions.get(i).getX();
				int y = (int) centroidPositions.get(i).getY();
				
				centroids[i] = new Centroid(pixels[x][y].bgr(),
						new ArrayList<Pixel>());
			}
		}
		
		do {
			flag = 0;
			
			for(int i = 0; i < image.getRows(); i++) {
				for(int j = 0; j < image.getCols(); j++) {
					color = pixels[i][j].bgr();
					
					double distance = 0;
					double minorDistance = -1;
					int index = 0;
					
					for(int k = 0; k < centroids.length; k++) {
						distance = Math.sqrt(
								Math.pow(color[0] - centroids[k].color[0], 2) +
								Math.pow(color[1] - centroids[k].color[1], 2) +
								Math.pow(color[2] - centroids[k].color[2], 2));
						if (distance < minorDistance || minorDistance < 0) {
							minorDistance = distance;
							index = k;
						}
					}
					
					centroids[index].group.add(pixels[i][j]);
				}
			}
			
			for(int i = 0; i < centroids.length; i++) {
				double[] newCentroidColor = {0, 0, 0};
				
				for(int j = 0; j < centroids[i].group.size(); j++) {
					newCentroidColor[0] += (centroids[i].group.get(j).bgr()[0] /
							centroids[i].group.size());
					newCentroidColor[1] += (centroids[i].group.get(j).bgr()[1] /
							centroids[i].group.size());
					newCentroidColor[2] += (centroids[i].group.get(j).bgr()[2] /
							centroids[i].group.size());
				}

				Misc.roundArray(newCentroidColor);
//				System.out.println("B: " + newCentroidColor[0] + "G: " + newCentroidColor[1] + " R: " + newCentroidColor[2]);
				
				if (!Misc.equals(centroids[i].color, newCentroidColor, 0)) {
					centroids[i].color = new double[] {
							newCentroidColor[0],
							newCentroidColor[1],
							newCentroidColor[2]
					};
					centroids[i].group = new ArrayList<Pixel>();
				} else {
					flag++;
				}
			}
		} while(flag != centroids.length);
		
		for(Centroid c : centroids) {
			for(Pixel px : c.group) {
				px.setPixelColor(c.color);
				Point p = px.getPoint();

				resPixels[(int)p.getX()][(int)p.getY()] = 
						new Pixel(c.color, p, px.getType());
			}
		}
		
		
		return result;
	}
	
	private static class Node {
		public double[] meanColor;
		public List<Pixel> group;
		
		public Node(double[] meanColor, List<Pixel> group) {
			this.meanColor = meanColor;
			this.group = group;
		}
	}
	
	public static Image isodata(Image image, int stdDevThreshold,
			int colorThreshold) {
		Image res = new Image(image.getRows(), image.getCols(),
				image.getImageType(), image.getPixelsType(), Fill.WHITE);
		Pixel[][] resPixels = res.getPixels();
		Pixel[][] pixels = image.clonePixels();
		double[] color;
		int size = image.getRows() * image.getCols();
		
		List<Pixel> group = new ArrayList<Pixel>();
		double[] meanColor = {0, 0, 0};
		
		for(int i = 0; i < image.getRows(); i++) {
			for(int j = 0; j < image.getCols(); j++) {
				color = pixels[i][j].bgr();
				
				group.add(pixels[i][j]);
				
				meanColor[0] += (color[0] / size);
				meanColor[1] += (color[1] / size);
				meanColor[2] += (color[2] / size);
			}
		}
		
		Node node = new Node(meanColor, group);
		List<Node> leaves = new ArrayList<Node>();
		
//		do {
			leaves.addAll(splitImage(node, stdDevThreshold));
//			node = mergeLeaves(leaves, colorThreshold);
			
//		} while (node != null && leaves.size() > 1);
		
		for(Node n : leaves) {
			Misc.roundArray(n.meanColor);
			
			for (Pixel px : n.group) {
				Point p = px.getPoint();

				resPixels[(int)p.getX()][(int)p.getY()] =
						new Pixel(n.meanColor, p, px.getType());
			}
		}

		return res;
	}
	
	private static List<Node> splitImage(Node node, int threshold) {
		Stack<Node> stack = new Stack<Node>();
		
		List<Node> leaves = new ArrayList<Node>();

		stack.push(node);
		while(!stack.empty()) {
			Node content = stack.pop();
			
			double[] stdDev = standardDeviation(content.group, mean(content.group));
			
			if (Misc.meanArray(stdDev) > threshold) {
				Pair<List<Pixel>, List<Pixel>> divided =
						divideGroup(content.group, content.meanColor);
				
				List<Pixel> above = divided.getKey();
				List<Pixel> below = divided.getValue();
				
				if (above.size() > 0 && below.size() > 0) {
					stack.push(new Node(mean(above), above));
					stack.push(new Node(mean(below), below));
				} else {
					leaves.add(content);
				}
			} else {
				leaves.add(content);
			}
		}
		
		return leaves;
	}
	
	private static Node mergeLeaves(List<Node> leaves, int threshold) {
		Node currNode;
		Node slidingNode;
		double distance;
		
		for(int i = 0; i < leaves.size(); i++) {
			currNode = leaves.get(i);
			
			for(int j = i + 1; j < leaves.size(); j++) {
				slidingNode = leaves.get(j);
				
				distance = Math.sqrt(
						Math.pow(currNode.meanColor[0] - slidingNode.meanColor[0], 2) +
						Math.pow(currNode.meanColor[1] - slidingNode.meanColor[1], 2) +
						Math.pow(currNode.meanColor[2] - slidingNode.meanColor[2], 2));
				
				if (distance < threshold) {
					List<Pixel> mergedGroups = new ArrayList<Pixel>(currNode.group);
					mergedGroups.addAll(slidingNode.group);
					
					double[] mergedMean = mean(mergedGroups);

					leaves.remove(currNode);
					leaves.remove(slidingNode);
					
					return new Node(mergedMean, mergedGroups);
				}
			}
		}
		
		return null;
	}
	
	private static double[] mean(List<Pixel> group) {
		double[] color;
		double[] res = {0, 0, 0};
		
		for(int i = 0; i < group.size(); i++) {
			color = group.get(i).bgr();
			
			res[0] += (color[0] / group.size());
			res[1] += (color[1] / group.size());
			res[2] += (color[2] / group.size());
		}
		
		return res;
	}
	
	private static double[] mean_aa(List<Pixel> group) {
		double[] color;
		double[] res = {0, 0, 0};
		
		for(int i = 0; i < group.size(); i++) {
			color = group.get(i).vsh();
			
			res[0] += (color[0] / group.size());
			res[1] += (color[1] / group.size());
			res[2] += (color[2] / group.size());
		}
		
//		Misc.roundArray(res);
		
		return res;
	}
	
	private static double[] standardDeviation(List<Pixel> group,
			double[] mean) {
		double[] color;
		double[] res = {0, 0, 0};
		
		for(int i = 0; i < group.size(); i++) {
			color = group.get(i).bgr();
			
			res[0] += (Math.pow(color[0] - mean[0], 2) / group.size());
			res[1] += (Math.pow(color[1] - mean[1], 2) / group.size());
			res[2] += (Math.pow(color[2] - mean[2], 2) / group.size());
		}
		
		res[0] = Math.sqrt(res[0]);
		res[1] = Math.sqrt(res[1]);
		res[2] = Math.sqrt(res[2]);
		
		return res;
	}
	
	private static double[] standardDeviation_aa(List<Pixel> group,
			double[] mean) {
		double[] color;
		double[] res = {0, 0, 0};
		
		for(int i = 0; i < group.size(); i++) {
			color = group.get(i).vsh();
			
			res[0] += (Math.pow(color[0] - mean[0], 2) / group.size());
			res[1] += (Math.pow(color[1] - mean[1], 2) / group.size());
			res[2] += (Math.pow(color[2] - mean[2], 2) / group.size());
		}
		
		res[0] = Math.sqrt(res[0]);
		res[1] = Math.sqrt(res[1]);
		res[2] = Math.sqrt(res[2]);
		
//		Misc.roundArray(res);
		
		return res;
	}
	
	private static boolean strayPixel(List<Pixel> group, double[] stdDev,
			int threshold) {
		double[] color;
		double colorVal;
		double stdDevVal = Misc.meanArray(stdDev);
		
		for(int i = 0; i < group.size(); i++) {
			color = group.get(i).bgr();
			
			colorVal = Misc.meanArray(color);
			
			System.out.println("\nColor value: " + colorVal);
			System.out.println("stdDevVal: " + stdDevVal + "\n");
			
			if (colorVal > stdDevVal + threshold)
				return true;
			
		}
		
		return false;
	}
	
	private static Pair<List<Pixel>, List<Pixel>> divideGroup(List<Pixel> group,
			double[] meanColor) {
		double meanVal = Misc.meanArray(meanColor);
		double[] color;
		List<Pixel> above = new ArrayList<Pixel>();
		List<Pixel> below = new ArrayList<Pixel>();
		
		for(int i = 0; i < group.size(); i++) {
			color = group.get(i).bgr();
			
			double colorVal = Misc.meanArray(color);
			
			if (colorVal > meanVal)
				above.add(group.get(i));
			else
				below.add(group.get(i));
		}
		
		return new Pair<List<Pixel>, List<Pixel>>(above, below);
	}
	
	private static Pair<List<Pixel>, List<Pixel>> divideGroup_aa(List<Pixel> group,
			double[] meanColor) {
//		double meanVal = Misc.meanArray(meanColor);
		double meanVal = meanColor[2];
		double[] color;
		List<Pixel> above = new ArrayList<Pixel>();
		List<Pixel> below = new ArrayList<Pixel>();
		
		for(int i = 0; i < group.size(); i++) {
			color = group.get(i).vsh();
			
//			double colorVal = Misc.meanArray(color);
			double colorVal = color[2];
			
//			System.out.println("\n\n Color Val: " + colorVal + " Mean Val: " + meanVal + "\n\n");
//			System.out.println("Color -> B: " + color[0] + " G: " + color[1] + " R: " + color[2]);
//			System.out.println("Mean Color -> B: " + meanColor[0] + " G: " + meanColor[1] + " R: " + meanColor[2] + "\n");
			
			if (colorVal > meanVal)
				above.add(group.get(i));
			else
				below.add(group.get(i));
		}
		
//		System.out.println("Tamaninho: " + group.size());
		
		
		return new Pair<List<Pixel>, List<Pixel>>(above, below);
	}
}
