package view;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JRadioButton;
import javax.swing.JTextPane;

import operations.Transform;

public class RotatePopup extends Popup {
	protected JRadioButton rdbtnImageOne;
	protected JRadioButton rdbtnImageTwo;
	protected JLabel lblAngle;
	protected JTextPane textPaneAngle;
	protected JLabel labelDegree;
	protected JButton btnRotate;

	public RotatePopup(GUI parent, String title, int width, int height) {
		super(parent, title, width, height);
		
		rdbtnImageOne = new JRadioButton("Image 1");
		rdbtnImageOne.setBounds(22, 37, 89, 23);
		rdbtnImageOne.setSelected(true);
		rdbtnImageOne.setEnabled(false);
		this.getContentPane().add(rdbtnImageOne);
		
		rdbtnImageTwo = new JRadioButton("Image 2");
		rdbtnImageTwo.setBounds(22, 59, 89, 23);
		rdbtnImageTwo.setSelected(false);
		rdbtnImageTwo.setEnabled(false);
		this.getContentPane().add(rdbtnImageTwo);
		
		lblAngle = new JLabel("Angle: ");
		lblAngle.setBounds(12, 14, 55, 15);
		this.getContentPane().add(lblAngle);
		
		textPaneAngle = new JTextPane();
		textPaneAngle.setBounds(68, 12, 48, 21);
		textPaneAngle.setText("0");
		this.getContentPane().add(textPaneAngle);
		
		labelDegree = new JLabel("°");
		labelDegree.setBounds(118, 14, 20, 15);
		this.getContentPane().add(labelDegree);
		
		btnRotate = new JButton("Rotate");
		btnRotate.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int angle = Integer.parseInt(textPaneAngle.getText().trim());
				
				if (rdbtnImageOne.isSelected() && parent.imageOne != null) {
					parent.imageOne =
							Transform.rotate(parent.imageOne, angle);
					parent.updateOne();
					System.out.println("Image one; angle: " + angle);
				} else if (rdbtnImageTwo.isSelected() && parent.imageTwo != null) {
					parent.imageTwo =
							Transform.rotate(parent.imageTwo, angle);
					parent.updateTwo();
					System.out.println("Image two; angle: " + angle);
				}
			}
		});
		btnRotate.setBounds(12, 90, 126, 25);
		this.getContentPane().add(btnRotate);
	}

}
