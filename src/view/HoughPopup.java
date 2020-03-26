package view;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import operations.Convolution;
import operations.Description;

public class HoughPopup extends Popup {
	private JTextField fieldDetectBordersThreshold;
	private JTextField fieldHoughThreshold;
	private JLabel lblMinlinlength;
	private JTextField fieldMinLinLength;
	private JLabel lblMaxlinegap;
	private JTextField fieldMaxLineGap;
	private JLabel lblDetectBordersMethod;
	private JTextField fieldRho;
	private JTextField fieldTheta;
	private JLabel lblBorderDetection;
	private JButton btnDetectLines;
	private Convolution.BorderDetectMethod selectedMethod;

	public HoughPopup(GUI parent, String title, int width, int height) {
		super(parent, title, width, height);
		
		JLabel lblDetectBordersThreshold = new JLabel("Threshold: ");
		lblDetectBordersThreshold.setBounds(428, 41, 80, 15);
		this.getContentPane().add(lblDetectBordersThreshold);
		
		String[] methods = {"Prewitt", "Sobel", "Kirsch"};
//		Convolution.BorderDetectMethod>
		JComboBox<String> comboBoxBordersMethod = new JComboBox<>(methods);
		comboBoxBordersMethod.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				for(Convolution.BorderDetectMethod m : Convolution.BorderDetectMethod.values())
					if (((String)comboBoxBordersMethod.getSelectedItem())
							.toLowerCase().compareTo(m.toString()
									.toLowerCase()) == 0)
						selectedMethod = m;
			}
		});
		comboBoxBordersMethod.setSelectedIndex(1);
		comboBoxBordersMethod.setBounds(297, 65, 111, 24);
		this.getContentPane().add(comboBoxBordersMethod);
		
		fieldDetectBordersThreshold = new JTextField();
		fieldDetectBordersThreshold.setText("150");
		fieldDetectBordersThreshold.setBounds(438, 68, 46, 19);
		this.getContentPane().add(fieldDetectBordersThreshold);
		fieldDetectBordersThreshold.setColumns(10);
		
		JLabel lblHoughThreshold = new JLabel("Hough threshold: ");
		lblHoughThreshold.setBounds(12, 68, 136, 15);
		this.getContentPane().add(lblHoughThreshold);
		
		fieldHoughThreshold = new JTextField();
		fieldHoughThreshold.setText("50");
		fieldHoughThreshold.setBounds(205, 66, 46, 19);
		this.getContentPane().add(fieldHoughThreshold);
		fieldHoughThreshold.setColumns(10);
		
		lblMinlinlength = new JLabel("minLinLength:");
		lblMinlinlength.setBounds(12, 95, 101, 15);
		this.getContentPane().add(lblMinlinlength);
		
		fieldMinLinLength = new JTextField();
		fieldMinLinLength.setText("50");
		fieldMinLinLength.setBounds(205, 93, 46, 19);
		this.getContentPane().add(fieldMinLinLength);
		fieldMinLinLength.setColumns(10);
		
		lblMaxlinegap = new JLabel("maxLineGap:");
		lblMaxlinegap.setBounds(12, 124, 101, 15);
		this.getContentPane().add(lblMaxlinegap);
		
		fieldMaxLineGap = new JTextField();
		fieldMaxLineGap.setText("30");
		fieldMaxLineGap.setBounds(205, 122, 46, 19);
		this.getContentPane().add(fieldMaxLineGap);
		fieldMaxLineGap.setColumns(10);
		
		lblDetectBordersMethod = new JLabel("Method:");
		lblDetectBordersMethod.setBounds(297, 41, 70, 15);
		this.getContentPane().add(lblDetectBordersMethod);
		
		JLabel lblTheta = new JLabel("Theta:");
		lblTheta.setBounds(12, 41, 70, 15);
		this.getContentPane().add(lblTheta);
		
		JLabel lblRho = new JLabel("Rho:");
		lblRho.setBounds(12, 14, 70, 15);
		this.getContentPane().add(lblRho);
		
		fieldRho = new JTextField();
		fieldRho.setText("1");
		fieldRho.setBounds(205, 12, 46, 19);
		this.getContentPane().add(fieldRho);
		fieldRho.setColumns(10);
		
		fieldTheta = new JTextField();
		fieldTheta.setText("1");
		fieldTheta.setBounds(205, 39, 46, 19);
		this.getContentPane().add(fieldTheta);
		fieldTheta.setColumns(10);
		
		JLabel lblPx = new JLabel("px");
		lblPx.setBounds(252, 14, 70, 15);
		this.getContentPane().add(lblPx);
		
		JLabel label = new JLabel("°");
		label.setBounds(252, 41, 70, 15);
		this.getContentPane().add(label);
		
		lblBorderDetection = new JLabel("Border detection");
		lblBorderDetection.setHorizontalAlignment(SwingConstants.CENTER);
		lblBorderDetection.setBounds(297, 14, 211, 15);
		this.getContentPane().add(lblBorderDetection);
		
		btnDetectLines = new JButton("Detect lines");
		btnDetectLines.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int threshold = Integer.parseInt(fieldDetectBordersThreshold.getText());
				
				int rho = Integer.parseInt(fieldRho.getText());
				double theta = (Integer.parseInt(fieldTheta.getText()) * Math.PI) / 180;
				int houghThreshold = Integer.parseInt(fieldHoughThreshold.getText());
				int minLinLength = Integer.parseInt(fieldMinLinLength.getText());
				int maxLineGap = Integer.parseInt(fieldMaxLineGap.getText());
				parent.imageOne = Description.detectLines(parent.imageOne,
						selectedMethod, rho, theta, threshold, houghThreshold,
						minLinLength, maxLineGap);
				
				parent.updateOne();
			}
		});
		btnDetectLines.setBounds(291, 119, 215, 25);
		this.getContentPane().add(btnDetectLines);
	}

}
