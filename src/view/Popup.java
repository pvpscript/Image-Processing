package view;

import javax.swing.JFrame;

public abstract class Popup extends JFrame {
	private GUI parent;
	private String title;
	private int width;
	private int height;
	private int x;
	private int y;
	
	public Popup(GUI parent, String title, int width, int height) {		
		this.parent = parent;
		this.title = title;
		this.width = width;
		this.height = height;
		
		this.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
		this.setLayout(null);
		this.setResizable(false);
		this.setSize(width, height);
		//this.setAlwaysOnTop(true);
	}
	
	public Popup(GUI parent, String title, int width, int height,
			int x, int y) {		
		this.parent = parent;
		this.title = title;
		this.width = width;
		this.height = height;
		this.x = x;
		this.y = y;
		
		this.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
		this.setLayout(null);
		this.setResizable(false);
		this.setSize(width, height);
		this.setLocation(x, y);
		//this.setAlwaysOnTop(true);
	}
	
	public GUI getParentGUI() {
		return this.parent;
	}
	
	public String getTitle() {
		return this.title;
	}

	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}
	
	public void open() {
		this.setVisible(true);
	}
}
