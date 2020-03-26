package view;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JTextField;

public class FillPopup extends Popup {
	protected JTextField fieldOffset;
	protected JTextField fieldR;
	protected JTextField fieldG;
	protected JTextField fieldB;
	
	public FillPopup(GUI parent, String title, int width, int height) {
		super(parent, title, width, height);
		
		JButton setFillColor = new JButton("Set Fill Color");
		setFillColor.setBounds(12, 68, 147, 25);
		setFillColor.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				parent.fillColor[0] = !fieldB.getText().isEmpty() ?
						Double.parseDouble(fieldB.getText()) : 0;
				parent.fillColor[1] = !fieldG.getText().isEmpty() ?
					Double.parseDouble(fieldG.getText()) : 0;
				parent.fillColor[2] = !fieldR.getText().isEmpty() ?
						Double.parseDouble(fieldR.getText()) : 0;
			
				parent.fillOffset = !fieldOffset.getText().isEmpty() ?
						Integer.parseInt(fieldOffset.getText()) : 0;
						
				parent.fillToggle = true;
			}
		});
		this.getContentPane().add(setFillColor);
		
		this.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent arg0) {
				parent.fillToggle = false;
			}
		});
		
		fieldOffset = new JTextField();
		fieldOffset.setColumns(10);
		fieldOffset.setBounds(63, 39, 96, 19);
		fieldOffset.setText("0");
		this.getContentPane().add(fieldOffset);
		
		JLabel lblOffset = new JLabel("Offset:");
		lblOffset.setBounds(12, 41, 70, 15);
		this.getContentPane().add(lblOffset);
		
		fieldR = new JTextField(10);
		fieldR.setBounds(27, 12, 30, 19);
		fieldR.setText("0");
		this.getContentPane().add(fieldR);
		
		JLabel lblR = new JLabel("R:");
		lblR.setBounds(12, 14, 33, 15);
		this.getContentPane().add(lblR);
		
		fieldG = new JTextField(10);
		fieldG.setBounds(78, 12, 30, 19);
		fieldG.setText("0");
		this.getContentPane().add(fieldG);
		
		JLabel lblG = new JLabel("G:");
		lblG.setBounds(63, 14, 33, 15);
		this.getContentPane().add(lblG);
		
		fieldB = new JTextField(10);
		fieldB.setBounds(129, 12, 30, 19);
		fieldB.setText("0");
		this.getContentPane().add(fieldB);
		
		JLabel lblB = new JLabel("B:");
		lblB.setBounds(114, 14, 33, 15);
		this.getContentPane().add(lblB);
	}
}
