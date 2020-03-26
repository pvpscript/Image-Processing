package view;

import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;

import javax.swing.JLabel;

import imageUtils.Point;

public class Canvas extends JLabel {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public enum drawTypes {
		NONE,
		CIRCLE,
		TRIANGLE
	};
	
	private int xa;
	private int xb;
	private int xc;
	private int ya;
	private int yb;
	private int yc;
	private boolean drawingEnabled = true;
	
	protected ArrayList<Point> circles = new ArrayList<Point>();
	protected int clicks = 0;
	protected drawTypes type = drawTypes.NONE;
	
	public Canvas() {
		super();
	}
	
	@Override
	protected void paintComponent(Graphics g) {
	    super.paintComponent(g); //ALWAYS call this method first!

	    switch(type) {
	    case CIRCLE:
	    	if (circles == null || circles.size() == 0) {
		    	int r = 6;
		    	int x = xa - (r / 2);
		    	int y = ya - (r / 2);
		    	
		    	g.setColor(Color.DARK_GRAY);
		    	g.fillOval(x, y, r, r);
	    	} else {
	    		for (Point p : circles) {
		    		int r = 6;
			    	int x = (int) (p.getX() - (r / 2));
			    	int y = (int) (p.getY() - (r / 2));
			    	
			    	g.setColor(Color.DARK_GRAY);
			    	g.fillOval(x, y, r, r);
	    		}
	    	}
	    	break;
	    case TRIANGLE:
	    	if (this.clicks == 3) {
	    		g.setColor(Color.BLACK);
		    	g.drawPolygon(new int[] {xa, xb, xc},
		    			new int[] {ya, yb, yc}, 3); //Draws triangle
	//	    	this.clicks = 0;
		    }
	    	break;
    	default: break;
	    }
//	    if (this.clicks == 3) {
//	    	g.drawPolygon(new int[] {xa, xb, xc},
//	    			new int[] {ya, yb, yc}, 3); //Draws triangle
////	    	this.clicks = 0;
//	    }
	}
	
	public void setFirstPoint(int x, int y) {
		this.xa = x;
		this.ya = y;
	}
	
	public void setSecondPoint(int x, int y) {
		this.xb = x;
		this.yb = y;
	}
	
	public void setThirdPoint(int x, int y) {
		this.xc = x;
		this.yc = y;
	}
	
	public void addCircle(int x, int y) {
		this.circles.add(new Point(x, y));
	}
	
	public void drawFigures(drawTypes type) {
		this.type = type;
		repaint();
	}
	
	public void drawFigure(drawTypes type) {
		this.type = type;
		repaint();
	}
	
	public void clearCanvas() {
		this.clicks = 0;
		this.type = drawTypes.NONE;
		this.circles = new ArrayList<Point>();
		repaint();
	}
	
	public boolean getDrawingEnabled() {
		return this.drawingEnabled;
	}
	
	public void setDrawingEnabled(boolean drawingEnabled) {
		this.drawingEnabled = drawingEnabled;
	}
}
