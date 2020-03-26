package view;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JRadioButton;
import javax.swing.JTextPane;

import operations.Transform;

public class TranslatePopup extends Popup {
	protected JLabel lblX;
	protected JLabel lblY;
	protected JTextPane textPaneX;
	protected JTextPane textPaneY;
	protected JRadioButton rdbtnImageOne;
	protected JRadioButton rdbtnImageTwo;
	protected JButton btnTranslate;
	
	public TranslatePopup(GUI parent, String title, int width, int height) {
		super(parent, title, width, height);
		
		lblX = new JLabel("X: ");
		lblX.setBounds(22, 14, 18, 15);
		this.getContentPane().add(lblX);
		
		lblY = new JLabel("Y: ");
		lblY.setBounds(22, 38, 18, 15);
		this.getContentPane().add(lblY);
		
		textPaneX = new JTextPane();
		textPaneX.setBounds(44, 12, 50, 21);
		textPaneX.setText("0");
		this.getContentPane().add(textPaneX);
		
		textPaneY = new JTextPane();
		textPaneY.setBounds(44, 36, 50, 21);
		textPaneY.setText("0");
		this.getContentPane().add(textPaneY);
		
		rdbtnImageOne = new JRadioButton("Image 1");
		rdbtnImageOne.setSelected(true);
		rdbtnImageOne.setEnabled(false);
		rdbtnImageOne.setBounds(22, 61, 80, 23);
		this.getContentPane().add(rdbtnImageOne);
		
		rdbtnImageTwo = new JRadioButton("Image 2");
		rdbtnImageTwo.setBounds(22, 80, 80, 23);
		rdbtnImageTwo.setSelected(false);
		rdbtnImageTwo.setEnabled(false);
		this.getContentPane().add(rdbtnImageTwo);
		
		btnTranslate = new JButton("Translate");
		btnTranslate.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int x = Integer.parseInt(textPaneX.getText().trim());
				int y = Integer.parseInt(textPaneY.getText().trim());
				
				if (rdbtnImageOne.isSelected() && parent.imageOne != null) {
					parent.imageOne =
							Transform.translate(parent.imageOne, x, y);
					parent.updateOne();
				} else if (rdbtnImageTwo.isSelected() && parent.imageTwo != null) {
					parent.imageTwo =
							Transform.translate(parent.imageTwo, x, y);
					parent.updateTwo();
				}
			}
		});
		btnTranslate.setBounds(12, 111, 102, 25);
		this.getContentPane().add(btnTranslate);
	}

}
