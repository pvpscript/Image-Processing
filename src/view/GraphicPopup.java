package view;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JRadioButton;
import javax.swing.JTextField;

import imageUtils.Image;
import operations.Graphic;

public class GraphicPopup extends Popup {
	JTextField fieldEquation;
	JLabel lblEquation;
	JRadioButton rdbtnImageOne;
	JRadioButton rdbtnImageTwo;
	JTextField fieldHorLow;
	JTextField fieldHorHigh;
	JTextField fieldVertLow;
	JTextField fieldVertHigh;
	JLabel lblIntervalX;
	JLabel lblIntervalY;
	JButton btnDrawFunction;
	JButton btnGraphicHelp;
	JLabel lblR;
	JLabel lblG;
	JLabel lblB;
	JTextField fieldR;
	JTextField fieldG;
	JTextField fieldB;
	JCheckBox chckbxShowAxisOne;
	JCheckBox chckbxShowAxisTwo;

	public GraphicPopup(GUI parent, String title, int width, int height) {
		super(parent, title, width, height);
	
		fieldEquation = new JTextField();
		fieldEquation.setColumns(10);
		fieldEquation.setBounds(89, 12, 345, 19);
		this.getContentPane().add(fieldEquation);
		
		lblEquation = new JLabel("Equation:");
		lblEquation.setBounds(12, 14, 70, 15);
		this.getContentPane().add(lblEquation);
		
		rdbtnImageOne = new JRadioButton("Image 1");
		rdbtnImageOne.setEnabled(false);
		rdbtnImageOne.setSelected(true);
		rdbtnImageOne.setBounds(22, 39, 110, 23);
		this.getContentPane().add(rdbtnImageOne);
		
		rdbtnImageTwo = new JRadioButton("Image 2");
		rdbtnImageTwo.setEnabled(false);
		rdbtnImageTwo.setBounds(148, 39, 110, 23);
		this.getContentPane().add(rdbtnImageTwo);
		
		fieldHorLow = new JTextField();
		fieldHorLow.setColumns(10);
		fieldHorLow.setBounds(277, 43, 54, 19);
		fieldHorLow.setText("-6.5");
		this.getContentPane().add(fieldHorLow);
		
		lblIntervalX = new JLabel("< x <");
		lblIntervalX.setBounds(336, 45, 70, 15);
		this.getContentPane().add(lblIntervalX);
		
		fieldHorHigh = new JTextField();
		fieldHorHigh.setColumns(10);
		fieldHorHigh.setBounds(380, 43, 54, 19);
		fieldHorHigh.setText("6.5");
		this.getContentPane().add(fieldHorHigh);
		
		fieldVertHigh = new JTextField();
		fieldVertHigh.setColumns(10);
		fieldVertHigh.setBounds(380, 72, 54, 19);
		fieldVertHigh.setText("4");
		this.getContentPane().add(fieldVertHigh);
		
		lblIntervalY = new JLabel("< y <");
		lblIntervalY.setBounds(336, 74, 70, 15);
		this.getContentPane().add(lblIntervalY);
		
		fieldVertLow = new JTextField();
		fieldVertLow.setColumns(10);
		fieldVertLow.setBounds(277, 72, 54, 19);
		fieldVertLow.setText("-4");
		this.getContentPane().add(fieldVertLow);
		
		btnDrawFunction = new JButton("Draw function");
		btnDrawFunction.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Image graphic;
				String vertLowStr = fieldVertLow.getText();
				String vertHighStr = fieldVertHigh.getText();
				String horLowStr = fieldHorLow.getText();
				String horHighStr = fieldHorHigh.getText();
				
				double vertLow = vertLowStr.isEmpty() ? -4 : Double.parseDouble(vertLowStr);
				double vertHigh = vertHighStr.isEmpty() ? 4 : Double.parseDouble(vertHighStr);
				double horLow = horLowStr.isEmpty() ? -6.5 : Double.parseDouble(horLowStr);
				double horHigh = horHighStr.isEmpty() ? 6.5 : Double.parseDouble(horHighStr);
				
				double[] funcColor = {
						fieldB.getText().isEmpty() ?
						0 : Double.parseDouble(fieldB.getText()),
						fieldG.getText().isEmpty() ?
						0 : Double.parseDouble(fieldG.getText()),
						fieldR.getText().isEmpty() ?
						0 : Double.parseDouble(fieldR.getText())};
				
				if (parent.canvasTwoEnabled) {
					if (rdbtnImageOne.isSelected()) {
						graphic = Graphic.drawFunction(parent.imageOne, vertLow, vertHigh,
								horLow, horHigh, fieldEquation.getText(), funcColor);
						parent.updateOne(graphic);
					} else if (rdbtnImageTwo.isSelected()){
						graphic = Graphic.drawFunction(parent.imageTwo, vertLow, vertHigh,
								horLow, horHigh, fieldEquation.getText(), funcColor);
						parent.updateTwo(graphic);
					}
				} else {
					graphic = Graphic.drawFunction(parent.imageOne, vertLow, vertHigh,
							horLow, horHigh, fieldEquation.getText(), funcColor);
					parent.updateOne(graphic);
				}
			}
		});
		btnDrawFunction.setBounds(12, 97, 246, 25);
		this.getContentPane().add(btnDrawFunction);
		
		btnGraphicHelp = new JButton("Help");
		btnGraphicHelp.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JOptionPane.showMessageDialog(null,
						"To plot a graph, just type in a polynomial function.\n\n\n"
						+ "Variables, operators, functions and constants that are accepted by the parser are the following:\n\n"
						+ "Variables: \"x\" is the only accepted variable, otherwise it'll raise a lexical error.\n"
						+ "Operators: \"+\", \"-\", \"*\", \"/\", \"^\", \"%\";\n"
						+ "Functions: \"sin()\", \"cos()\", \"tan()\", \"exp()\", \"ln()\";"
						+ "Constants: \"PI\"");
			}
		});
		btnGraphicHelp.setBounds(12, 128, 246, 25);
		this.getContentPane().add(btnGraphicHelp);
		
		lblR = new JLabel("R:");
		lblR.setBounds(270, 136, 14, 15);
		this.getContentPane().add(lblR);
		
		lblG = new JLabel("G:");
		lblG.setBounds(329, 136, 15, 15);
		this.getContentPane().add(lblG);
		
		fieldR = new JTextField();
		fieldR.setColumns(10);
		fieldR.setBounds(285, 134, 31, 19);
		fieldR.setText("0");
		this.getContentPane().add(fieldR);
		
		fieldG = new JTextField();
		fieldG.setColumns(10);
		fieldG.setBounds(344, 134, 31, 19);
		fieldG.setText("0");
		this.getContentPane().add(fieldG);
		
		lblB = new JLabel("B:");
		lblB.setBounds(388, 136, 15, 15);
		this.getContentPane().add(lblB);
		
		fieldB = new JTextField();
		fieldB.setColumns(10);
		fieldB.setBounds(403, 134, 31, 19);
		fieldB.setText("0");
		this.getContentPane().add(fieldB);
		
		chckbxShowAxisOne = new JCheckBox("Show Axis");
		chckbxShowAxisOne.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.out.println("Switch axis for image one.");
			}
		});
		chckbxShowAxisOne.setBounds(21, 66, 96, 23);
		this.getContentPane().add(chckbxShowAxisOne);
		
		chckbxShowAxisTwo = new JCheckBox("Show Axis");
		chckbxShowAxisTwo.setEnabled(false);
		chckbxShowAxisTwo.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.out.println("Switch axis for image two.");
			}
		});
		chckbxShowAxisTwo.setBounds(148, 66, 96, 23);
		this.getContentPane().add(chckbxShowAxisTwo);
		
		
//		fieldEquation = new JTextField();
//		fieldEquation.setColumns(10);
//		fieldEquation.setBounds(89, 12, 345, 19);
//		this.getContentPane().add(fieldEquation);
//		
//		lblEquation = new JLabel("Equation:");
//		lblEquation.setBounds(12, 14, 70, 15);
//		this.getContentPane().add(lblEquation);
//		
//		rdbtnImageOne = new JRadioButton("Image 1");
//		rdbtnImageOne.setEnabled(false);
//		rdbtnImageOne.setSelected(true);
//		rdbtnImageOne.setBounds(22, 39, 110, 23);
//		this.getContentPane().add(rdbtnImageOne);
//		
//		rdbtnImageTwo = new JRadioButton("Image 2");
//		rdbtnImageTwo.setEnabled(false);
//		rdbtnImageTwo.setBounds(148, 39, 110, 23);
//		this.getContentPane().add(rdbtnImageTwo);
//		
//		fieldHorLow = new JTextField();
//		fieldHorLow.setColumns(10);
//		fieldHorLow.setBounds(277, 43, 54, 19);
//		fieldHorLow.setText("-6.5");
//		this.getContentPane().add(fieldHorLow);
//		
//		JLabel lblIntervalX = new JLabel("< x <");
//		lblIntervalX.setBounds(336, 45, 70, 15);
//		this.getContentPane().add(lblIntervalX);
//		
//		fieldHorHigh = new JTextField();
//		fieldHorHigh.setColumns(10);
//		fieldHorHigh.setBounds(380, 43, 54, 19);
//		fieldHorHigh.setText("6.5");
//		this.getContentPane().add(fieldHorHigh);
//		
//		fieldVertHigh = new JTextField();
//		fieldVertHigh.setColumns(10);
//		fieldVertHigh.setBounds(380, 72, 54, 19);
//		fieldVertHigh.setText("4");
//		this.getContentPane().add(fieldVertHigh);
//		
//		lblIntervalY = new JLabel("< y <");
//		lblIntervalY.setBounds(336, 74, 70, 15);
//		this.getContentPane().add(lblIntervalY);
//		
//		fieldVertLow = new JTextField();
//		fieldVertLow.setColumns(10);
//		fieldVertLow.setBounds(277, 72, 54, 19);
//		fieldVertLow.setText("-4");
//		this.getContentPane().add(fieldVertLow);
//		
//		btnDrawFunction = new JButton("Draw function");
//		btnDrawFunction.setBounds(12, 68, 246, 25);
//		btnDrawFunction.addActionListener(new ActionListener() {
//			public void actionPerformed(ActionEvent e) {
//				Image graphic;
//				String vertLowStr = fieldVertLow.getText();
//				String vertHighStr = fieldVertHigh.getText();
//				String horLowStr = fieldHorLow.getText();
//				String horHighStr = fieldHorHigh.getText();
//				
//				double vertLow = vertLowStr.isEmpty() ? -4 : Double.parseDouble(vertLowStr);
//				double vertHigh = vertHighStr.isEmpty() ? 4 : Double.parseDouble(vertHighStr);
//				double horLow = horLowStr.isEmpty() ? -6.5 : Double.parseDouble(horLowStr);
//				double horHigh = horHighStr.isEmpty() ? 6.5 : Double.parseDouble(horHighStr);
//				
//				double[] funcColor = {
//						fieldB.getText().isEmpty() ?
//						0 : Double.parseDouble(fieldB.getText()),
//						fieldG.getText().isEmpty() ?
//						0 : Double.parseDouble(fieldG.getText()),
//						fieldR.getText().isEmpty() ?
//						0 : Double.parseDouble(fieldR.getText())};
//				
//				if (parent.canvasTwoEnabled) {
//					if (rdbtnImageOne.isSelected()) {
//						graphic = Graphic.drawFunction(parent.imageOne, vertLow, vertHigh,
//								horLow, horHigh, fieldEquation.getText(), funcColor);
//						parent.updateOne(graphic);
//					} else if (rdbtnImageTwo.isSelected()){
//						graphic = Graphic.drawFunction(parent.imageTwo, vertLow, vertHigh,
//								horLow, horHigh, fieldEquation.getText(), funcColor);
//						parent.updateTwo(graphic);
//					}
//				} else {
//					graphic = Graphic.drawFunction(parent.imageOne, vertLow, vertHigh,
//							horLow, horHigh, fieldEquation.getText(), funcColor);
//					parent.updateOne(graphic);
//				}
//			}
//		});
//		this.getContentPane().add(btnDrawFunction);
//		
//		btnGraphicHelp = new JButton("Help");
//		btnGraphicHelp.setBounds(12, 109, 246, 25);
//		this.getContentPane().add(btnGraphicHelp);
//		
//		lblR = new JLabel("R:");
//		lblR.setBounds(270, 117, 14, 15);
//		this.getContentPane().add(lblR);
//		
//		lblG = new JLabel("G:");
//		lblG.setBounds(329, 117, 15, 15);
//		this.getContentPane().add(lblG);
//		
//		lblB = new JLabel("B:");
//		lblB.setBounds(388, 117, 15, 15);
//		this.getContentPane().add(lblB);
//		
//		fieldR = new JTextField();
//		fieldR.setColumns(10);
//		fieldR.setBounds(285, 115, 31, 19);
//		this.getContentPane().add(fieldR);
//		
//		fieldG = new JTextField();
//		fieldG.setColumns(10);
//		fieldG.setBounds(344, 115, 31, 19);
//		this.getContentPane().add(fieldG);
//		
//		fieldB = new JTextField();
//		fieldB.setColumns(10);
//		fieldB.setBounds(403, 115, 31, 19);
//		this.getContentPane().add(fieldB);
	}
}
