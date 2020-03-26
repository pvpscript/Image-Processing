package gui;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;

import javax.swing.ImageIcon;
import javax.swing.JLabel;

import org.opencv.highgui.HighGui;

import operations.Morphology;

public class JLabelWithMouseHandler extends JLabel implements MouseListener {
	private GUI parentGui;
	
	private static final long serialVersionUID = 1L;
	
	public JLabelWithMouseHandler() {		
		addMouseListener(this);
	}
	
	public JLabelWithMouseHandler(GUI parent) {
		this.parentGui = parent;
		
		addMouseListener(this);
	}

	@Override
	public void mouseClicked(MouseEvent arg0) {
//		switch(this.operation) {
//		case FLOOD_FILL:
//			this.image = Morphology.floodFillPoint(this.image, this.color,
//					arg0.getY(), arg0.getX(), this.offset);
//			this.update();
//			break;
//		case DEFORM:
//			break;
//		}
		this.parentGui.setImage(Morphology.floodFillPoint(this.parentGui.getImage(),
				this.parentGui.getColor(), arg0.getY(), arg0.getX(),
				this.parentGui.getOffset()));
				
		this.update();
	}

	@Override
	public void mouseEntered(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseExited(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mousePressed(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseReleased(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}
	
//	public void setOperation(Operation operation) {
//		this.operation = operation;
//	}
//	
	public void update() {
		BufferedImage buffImage = (BufferedImage) HighGui.toBufferedImage(
				this.parentGui.getImage().acquireMat());
		
		this.setIcon(new ImageIcon(buffImage));
	}
	
	public void reset() {
		BufferedImage buffImage = (BufferedImage) HighGui.toBufferedImage(
				this.parentGui.getOriginalImage().acquireMat());
		this.parentGui.setImage(this.parentGui.getOriginalImage());
		
		this.setIcon(new ImageIcon(buffImage));
	}
//
//	public void update(Image image) {
//		BufferedImage buffImage = (BufferedImage) HighGui.toBufferedImage(
//				image.getImage());
//		
//		this.image = image;
//		this.setIcon(new ImageIcon(buffImage));
//	}
//	
//	public double[] getColor() {
//		return this.color;
//	}
//	
//	public void setColor(double[] color) {
//		this.color = color;
//	}
//	
//	public int getOffset() {
//		return this.offset;
//	}
//	
//	public void setOffset(int offset) {
//		this.offset = offset;
//	}
}
