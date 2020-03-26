package imageUtils;

public class Pixel {
	public enum Type {
		BGR,
		QIY,
		VSH
	}
	
	private double a;
	private double b;
	private double c;
	private Point point;
	private Type type;
	
	public Pixel(double[] pixel, Point point, Type type) {
//		System.out.println("Coisinha: " + pixel.length);
		if (pixel.length == 1) {
			this.a = pixel[0];
			this.b = pixel[0];
			this.c = pixel[0];
//		} else if (pixel.length == 2) {
//			this.a = pixel[0];
//			this.b = pixel[1];
		} else {
			this.a = pixel[2];
			this.b = pixel[1];
			this.c = pixel[0];
		}
		
		this.point = point;
		this.type = type;
	}
	
	public Pixel(int[] pixel, Point point, Type type) {
		if (pixel.length == 1) {
			this.a = pixel[0];
			this.b = pixel[0];
			this.c = pixel[0];
		} else {
			this.a = pixel[2];
			this.b = pixel[1];
			this.c = pixel[0];
		}
		
		this.point = point;
		this.type = type;
	}
	
	public Type getType() {
		return this.type;
	}
	
	public double[] raw() {
		double[] pixel = {this.c, this.b, this.a};

		return pixel;
	}
	
	public double[] bgr() {
		double[] pixel = new double[3];
		
		switch(this.type) {
		case BGR:
			pixel[0] = this.c; // B
			pixel[1] = this.b; // G
			pixel[2] = this.a; // R
			
			break;
		case QIY:
			pixel[0] = (this.a + 
			-1.106 * this.b +
			1.703 * this.c); // B
			
			pixel[1] = (this.a +
			-0.272 * this.b +
			-0.647 * this.c); // G
			
			pixel[2] = (this.a + 
			0.956 * this.b +
			0.619 * this.c); // R
			
			pixel[0] = Math.round((pixel[0] < 0) ? 0 : (pixel[0] > 255) ? 255 : pixel[0]);
			pixel[1] = Math.round((pixel[1] < 0) ? 0 : (pixel[1] > 255) ? 255 : pixel[1]);
			pixel[2] = Math.round((pixel[2] < 0) ? 0 : (pixel[2] > 255) ? 255 : pixel[2]);
			
			break;
		case VSH:
			double r, rPrime;
			double g, gPrime;
			double b, bPrime;
			
			double val = this.c;
			double sat = this.b;
			double hue = this.a;
			
			double c = val * sat;
			double x = c * (1 - Math.abs(((hue / 60) % 2) - 1));
			double m = val - c;
			
			if (hue >= 0 && hue < 60) {
				rPrime = c;
				gPrime = x;
				bPrime = 0;
			} else if (hue >= 60 && hue < 120) {
				rPrime = x;
				gPrime = c;
				bPrime = 0;
			} else if (hue >= 120 && hue < 180) {
				rPrime = 0;
				gPrime = c;
				bPrime = x;
			} else if (hue >= 180 && hue < 240) {
				rPrime = 0;
				gPrime = x;
				bPrime = c;
			} else if (hue >= 240 && hue < 300) {
				rPrime = x;
				gPrime = 0;
				bPrime = c;
			} else {
				rPrime = c;
				gPrime = 0;
				bPrime = x;
			}
			
			r = (rPrime + m) * 255;
			g = (gPrime + m) * 255;
			b = (bPrime + m) * 255;
			
			pixel[0] = Math.round(b);
			pixel[1] = Math.round(g);
			pixel[2] = Math.round(r);
			
			break;
		}

		return pixel;
	}
	
	public double[] qiy() {
		double[] pixel = new double[3];
		
		switch(this.type) {
		case BGR:
			pixel[0] = (0.211 * this.a +
					-0.523 * this.b +
					0.312 * this.c); // Q
			pixel[1] = (0.596 * this.a +
					-0.274 * this.b +
					-0.322 * this.c); // I
			pixel[2] = (0.299 * this.a +
					0.587 * this.b +
					0.114 * this.c); // Y
			break;
		case QIY:
			pixel[0] = this.c; // Q
			pixel[1] = this.b; // I
			pixel[2] = this.a; // Y
			
			break;
		case VSH:
			double a, b, c;
			
			pixel = bgr();
			a = pixel[2];
			b = pixel[1];
			c = pixel[0];
			
			pixel[0] = (0.211 * a +
					-0.523 * b +
					0.312 * c); // Q
			pixel[1] = (0.596 * a +
					-0.274 * b +
					-0.322 * c); // I
			pixel[2] = (0.299 * a +
					0.587 * b +
					0.114 * c); // Y
		}
				
		return pixel;
	}
	
	public double[] vsh() {
		double[] pixel = null;
		double r, g, b;
		
		switch(this.type) {
		case QIY:
			pixel = bgr();
			break;
		case BGR:
			b = this.c; // B
			g = this.b; // G
			r = this.a; // R
			
			pixel = vshPixel(b, g, r);
			
			break;
		case VSH:
			pixel = new double[3];
			
			pixel[0] = this.a; // V
			pixel[1] = this.b; // S
			pixel[2] = this.c; // H
			break;
		}
		
		return pixel;
	}
	
	private double[] vshPixel(double b2, double g, double r) {
		double hue;
		double sat;
		double val;
		double[] pixel = new double[3];
		
		double bPrime = b2/255;
		double gPrime = g/255;
		double rPrime = r/255;
		
		double cMax = Math.max(rPrime, Math.max(gPrime, bPrime));
		double cMin = Math.min(rPrime, Math.min(gPrime, bPrime));
		
		double delta = cMax - cMin;
		
		if (delta == 0)
			hue = 0;
		else if (cMax == rPrime)
			hue = Math.round(60 * (((gPrime - bPrime)/delta) % 6));
		else if (cMax == gPrime)
			hue = Math.round(60 * (((bPrime - rPrime)/delta) + 2));
		else
			hue = Math.round(60 * (((rPrime - gPrime)/delta) + 4));
		
		sat = cMax == 0 ? 0 : (delta / cMax);
		
		val = cMax;
		
		pixel[0] = val;
		pixel[1] = sat;
		pixel[2] = hue;
		
		return pixel;
	}
	
	public void setPixelColor(double[] pixel) {
		this.a = pixel[2];
		this.b = pixel[1];
		this.c = pixel[0];
	}
	
	public void setPixelColor(int[] pixel) {
		this.a = pixel[2];
		this.b = pixel[1];
		this.c = pixel[0];
	}
	
	public Point getPoint() {
		return this.point;
	}
}
