package gui;

import java.awt.FlowLayout;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;

public class JFrameWithMouseHandler extends JFrame implements MouseListener {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public JFrameWithMouseHandler() {
		setTitle("Titulozinho");
		addMouseListener(this);
	}
	
	public static void showImageGUI() throws IOException {
		BufferedImage img = ImageIO.read(new File("/home/p3t3r/lixorama.png"));
	    ImageIcon icon = new ImageIcon(img);
	    JFrame frame = new JFrameWithMouseHandler();
	    frame.setLayout(new FlowLayout());
	    frame.setSize(200, 300);
	    JLabel lbl = new JLabel();
	    lbl.setIcon(icon);
	    frame.add(lbl);
	    frame.setVisible(true);
	    frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
	}
	
	public static void imageGUI() {
		JFrame frame = new JFrameWithMouseHandler();
		
		frame.setVisible(true);
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
	}

	@Override
	public void mouseClicked(MouseEvent arg0) {
		System.out.println("Clicou em: " + arg0.getX() + " " + arg0.getY());
		
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
}
