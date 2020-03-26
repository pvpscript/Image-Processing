package dataStructures;

import imageUtils.Point;

public class Triangle {
	private Point p;
	private Point q;
	private Point r;
	
	public Triangle(Point p, Point q, Point r) {
		this.p = p;
		this.q = q;
		this.r = r;
	}
	
	public Point getP() {
		return this.p;
	}
	
	public void setP(Point p) {
		this.p = p;
	}
	
	public Point getQ() {
		return this.q;
	}
	
	public void setQ(Point q) {
		this.q = q;
	}
	
	public Point getR() {
		return this.r;
	}
	
	public void setR(Point r) {
		this.r = r;
	}
	
	public Point getTopmostPoint() {
		Point topmost;
		
		if (this.p.getY() > this.q.getY())
			if (this.p.getY() > this.r.getY())
				topmost = this.p;
			else
				topmost = this.r;
		else if (this.q.getY() > this.r.getY())
			topmost = this.q;
		else
			topmost = this.r;
		
		return topmost;
	}
	
	public Point getBottommostPoint() {
		Point bottommost;
		
		if (this.p.getY() < this.q.getY())
			if (this.p.getY() < this.r.getY())
				bottommost = this.p;
			else
				bottommost = this.r;
		else if (this.q.getY() < this.r.getY())
			bottommost = this.q;
		else
			bottommost = this.r;
		
		return bottommost;
	}
	
	public Point getLeftmostPoint() {
		Point leftmost;
		
		if (this.p.getX() > this.q.getX())
			if (this.p.getX() > this.r.getX())
				leftmost = this.p;
			else
				leftmost = this.r;
		else if (this.q.getX() > this.r.getX())
			leftmost = this.q;
		else
			leftmost = this.r;
		
		return leftmost;
	}
	
	public Point getRightmostPoint() {
		Point rightmost;
		
		if (this.p.getX() < this.q.getX())
			if (this.p.getX() < this.r.getX())
				rightmost = this.p;
			else
				rightmost = this.r;
		else if (this.q.getX() < this.r.getX())
			rightmost = this.q;
		else
			rightmost = this.r;
		
		return rightmost;
	}
	
	public boolean hasPoint(Point s) {
		Point a = this.p;
		Point b = this.q;
		Point c = this.r;
		
	    double asX = s.getX() - a.getX();
	    double asY = s.getY() - a.getY();

	    boolean sAB = (b.getX() - a.getX()) * asY -
	    		(b.getY() - a.getY()) * asX > 0;

	    if ((c.getX() - a.getX()) * asY -
	    		(c.getY() - a.getY()) * asX > 0 == sAB)
	    	return false;

	    if((c.getX() - b.getX()) * (s.getY() - b.getY()) -
	    		(c.getY() - b.getY()) * (s.getX() - b.getX()) > 0 != sAB)
	    	return false;

	    return true;
	}
}
