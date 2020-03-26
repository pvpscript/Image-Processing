package gui;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.WindowConstants;

import imageUtils.Image;

public class GUI {
	private String windowName;
	private Image image;
	private Image originalImage;
	private JLabel imageView;
	
	private double[] color = {0, 0, 0};
	private int offset;
	
	public GUI(String windowName, Image image) {
		this.windowName = windowName;
		this.image = image;
		this.originalImage = image.clone();
	}
	
	public void init() {
		//JPanel panel = new JPanel();
		//panel.setLayout(new BoxLayout(panel, BoxLayout.PAGE_AXIS));
		
		frameInit();
		
	}
	
	private void frameInit() {
		JFrame frame = createJFrame(this.windowName);
		
		frame.setLayout(new FlowLayout());
		frame.pack();
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
	}
	
	private JFrame createJFrame(String windowName) {
		JFrame frame = new JFrame(windowName);
		Box box = Box.createVerticalBox();
		
		frame.setLayout(new BoxLayout(frame.getContentPane(), BoxLayout.LINE_AXIS));

		setupImage(box);
		JPanel panel = new JPanel();
		panel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.insets = new Insets(2, 2, 2, 2);

        panel.add(new JLabel("R"), gbc);
        gbc.gridx++;
        panel.add(new JLabel("G"), gbc);
        gbc.gridx++;
        panel.add(new JLabel("B"), gbc);

        gbc.gridx = 0;
        gbc.gridy++;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        JTextField r = new JTextField(3);
        JTextField g = new JTextField(3);
        JTextField b = new JTextField(3);
        
        panel.add(r, gbc);
        gbc.gridx++;
        panel.add(g, gbc);
        gbc.gridx++;
        panel.add(b, gbc);

        JButton resetButton = new JButton("Reset");
        resetButton.addActionListener(new ActionListener() { 
        	  public void actionPerformed(ActionEvent e) { 
        		  ((JLabelWithMouseHandler)imageView).reset();
        	  } 
        });
        
        JButton setColorButton = new JButton("Set color");
        setColorButton.addActionListener(new ActionListener() { 
        	  public void actionPerformed(ActionEvent e) { 
        		  color[0] = Double.parseDouble(b.getText());
        		  color[1] = Double.parseDouble(g.getText());
        		  color[2] = Double.parseDouble(r.getText());
        	  } 
        });
        
        gbc.gridx = 0;
        gbc.gridy++;
        gbc.fill = GridBagConstraints.NONE;
        gbc.gridwidth = 3;
        panel.add(setColorButton, gbc);
        gbc.gridy++;
        panel.add(resetButton, gbc);
		
		box.add(panel);		
		
		frame.add(box);
		
		frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		
		return frame;
	}
	
	private void setupImage(Box box) {
		this.imageView = new JLabelWithMouseHandler(this);
		int rows = (this.image.getRows() > 700) ? 700 : this.image.getRows();
		int cols = (this.image.getCols() > 1580) ? 1580 : this.image.getCols();
				
		JScrollPane imageScrollPane = new JScrollPane(this.imageView);
	    imageScrollPane.getViewport().setPreferredSize(
	    		new Dimension(cols, rows));
	    
		box.add(imageScrollPane);
		((JLabelWithMouseHandler)this.imageView).update();
	}
	
	public Image getImage() {
		return this.image;
	}
	
	public void setImage(Image image) {
		this.image = image;
	}
	
	public Image getOriginalImage() {
		return this.originalImage;
	}
	
	public void setOriginalImage(Image originalImage) {
		this.originalImage = originalImage;
	}
	
	public double[] getColor() {
		return this.color;
	}
	
	public void setColor(double[] color) {
		this.color = color;
	}
	
	public int getOffset() {
		return this.offset;
	}
	
	public void setOffset(int offset) {
		this.offset = offset;
	}
}
