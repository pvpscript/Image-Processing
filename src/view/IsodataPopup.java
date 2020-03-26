package view;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JTextField;

import operations.Segmentation;

public class IsodataPopup extends Popup {
	private JTextField fieldStdDevThreshold;
	private JTextField fieldColorDistThreshold;

	public IsodataPopup(GUI parent, String title, int width, int height) {
		super(parent, title, width, height);
		
		JLabel lblStandardDeviationThreshold = new JLabel("Standard deviation threshold: ");
		lblStandardDeviationThreshold.setBounds(12, 12, 218, 15);
		this.getContentPane().add(lblStandardDeviationThreshold);
		
		fieldStdDevThreshold = new JTextField();
		fieldStdDevThreshold.setText("20");
		fieldStdDevThreshold.setBounds(230, 10, 38, 19);
		this.getContentPane().add(fieldStdDevThreshold);
		fieldStdDevThreshold.setColumns(10);
		
		JLabel lblColorDistanceThreshold = new JLabel("Color distance threshold:");
		lblColorDistanceThreshold.setBounds(46, 39, 180, 15);
		this.getContentPane().add(lblColorDistanceThreshold);
		
		fieldColorDistThreshold = new JTextField();
		fieldColorDistThreshold.setText("10");
		fieldColorDistThreshold.setBounds(230, 37, 38, 19);
		this.getContentPane().add(fieldColorDistThreshold);
		fieldColorDistThreshold.setColumns(10);
		
		JButton btnRun = new JButton("Run ISODATA");
		btnRun.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				String stdDevThreshold = fieldStdDevThreshold.getText();
				stdDevThreshold = stdDevThreshold.isEmpty() ? "20" : stdDevThreshold;
				String colorDistThreshold = fieldColorDistThreshold.getText();
				colorDistThreshold = colorDistThreshold.isEmpty() ? "10" : colorDistThreshold;
				
				parent.imageOne = Segmentation.isodata(parent.imageOne,
						Integer.parseInt(stdDevThreshold),
						Integer.parseInt(colorDistThreshold));
				
				parent.updateOne();
			}
		});
		btnRun.setBounds(12, 66, 256, 25);
		this.getContentPane().add(btnRun);
	}

}
