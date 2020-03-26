package imageUtils;

public class VisitPixel extends Pixel {
	private boolean visited;
	
	public VisitPixel(int[] pixel, Point point, Type type) {
		super(pixel, point, type);
		this.visited = false;
		// TODO Auto-generated constructor stub
	}
	
	public VisitPixel(double[] pixel, Point point, Type type) {
		super(pixel, point, type);
		this.visited = false;
		// TODO Auto-generated constructor stub
	}
	
	public VisitPixel(Pixel pixel, boolean visited) {
		super(pixel.raw(), pixel.getPoint(), pixel.getType());
		this.visited = visited;
	}
	
	public static VisitPixel[][] createVisitPixels(Pixel[][] pixels,
			int rows, int cols) {
		VisitPixel[][] visitPixels = new VisitPixel[rows][cols];
		
		for(int i = 0; i < rows; i++)
			for(int j = 0; j < cols; j++)
				visitPixels[i][j] = new VisitPixel(pixels[i][j].bgr(),
						pixels[i][j].getPoint(), pixels[i][j].getType());
		
		return visitPixels;
	}
	
	public void setVisited(boolean visited) {
		this.visited = visited;
	}
	
	public boolean getVisited() {
		return this.visited;
	}
}
