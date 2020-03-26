package view;

import java.awt.Component;
import java.awt.Container;
import java.awt.GridBagConstraints;

import javax.swing.JButton;
import javax.swing.JLabel;

public class Example extends Popup {
	protected JLabel label;
	protected JButton button;
	
	public Example(GUI gui, String title, int width, int height) {
		super(gui, title, width, height);
		GUI parent = super.getParentGUI();
		
		GridBagConstraints gbc = new GridBagConstraints();
		
		this.label = new JLabel("Fuck this shit m8.");
		gbc.gridx = 0;
		gbc.gridy = 0;
		this.add(this.label, gbc);
		
		this.button = new JButton("Doesn't do shit!");
		gbc.gridx = 0;
		gbc.gridy = 1;
		this.add(this.button, gbc);
	}
}
