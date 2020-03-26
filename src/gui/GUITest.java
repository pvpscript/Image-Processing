package gui;

import java.awt.FlowLayout;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.WindowConstants;

public class GUITest {

	public static void init() {
		JFrame frame = createJFrame("Testezinho");
		
		frame.pack();
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);	
	}
	
	public static void test() throws IOException {
		BufferedImage img = ImageIO.read(new File("/home/p3t3r/lixorama.png"));
	    ImageIcon icon = new ImageIcon(img);
	    JFrame frame = new JFrame();
	    frame.setLayout(new FlowLayout());
	    frame.setSize(200, 300);
	    JLabel lbl = new JLabel();
	    lbl.setIcon(icon);
	    frame.add(lbl);
	    frame.setVisible(true);
	    frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
	}
	
	public static JFrame createJFrame(String windowName) {
		JFrame frame = new JFrame(windowName);
		frame.setLayout(new BoxLayout(frame.getContentPane(), BoxLayout.PAGE_AXIS));

		frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		return frame;
	}
}
