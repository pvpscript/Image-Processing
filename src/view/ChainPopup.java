package view;

import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JTextField;

import dataStructures.Pair;
import imageUtils.Image;
import operations.Description;

public class ChainPopup extends Popup {
	private JTextField fieldChainCode;
	private JTextField fieldFirstDifference;
	private JTextField fieldShapeNumber;
	private Description.ChainDirection selectedDirection;

	public ChainPopup(GUI parent, String title, int width, int height) {
		super(parent, title, width, height);
		
		JLabel lblChainCode = new JLabel("Chain code: ");
		lblChainCode.setBounds(12, 50, 87, 15);
		this.getContentPane().add(lblChainCode);
		
		JLabel lblFirstDifference = new JLabel("First difference:");
		lblFirstDifference.setBounds(12, 77, 113, 15);
		this.getContentPane().add(lblFirstDifference);
		
		JLabel lblShapeNumber = new JLabel("Shape number: ");
		lblShapeNumber.setBounds(12, 104, 113, 15);
		this.getContentPane().add(lblShapeNumber);
		
		fieldChainCode = new JTextField();
		fieldChainCode.setBounds(129, 48, 305, 19);
		this.getContentPane().add(fieldChainCode);
		fieldChainCode.setColumns(10);
		
		fieldFirstDifference = new JTextField();
		fieldFirstDifference.setBounds(129, 75, 305, 19);
		this.getContentPane().add(fieldFirstDifference);
		fieldFirstDifference.setColumns(10);
		
		fieldShapeNumber = new JTextField();
		fieldShapeNumber.setBounds(129, 102, 305, 19);
		this.getContentPane().add(fieldShapeNumber);
		fieldShapeNumber.setColumns(10);
		
		JLabel lblDirection = new JLabel("Direction:");
		lblDirection.setBounds(12, 17, 70, 15);
		this.getContentPane().add(lblDirection);
		
		String[] directions = {"Clockwise", "Counterclockwise"};
		JComboBox<String> comboBoxDirection = new JComboBox<>(directions);
		comboBoxDirection.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				for(Description.ChainDirection cd : Description.ChainDirection.values())
					if (((String)comboBoxDirection.getSelectedItem())
							.toLowerCase().compareTo(cd.toString()
									.toLowerCase()) == 0)
						selectedDirection = cd;
			}
		});
		comboBoxDirection.setSelectedIndex(0);
		comboBoxDirection.setBounds(129, 12, 161, 24);
		this.getContentPane().add(comboBoxDirection);
		
		JButton btnCalculateChain = new JButton("Calculate chain");
		btnCalculateChain.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Pair<Image, String> boundary =
						Description.mooreBoundaryTracking(parent.imageOne);
				String chainCode = boundary.getValue();
				
				Pair<String, String> normShape =
						Description.shapeNumber(chainCode, selectedDirection);
				
				fieldChainCode.setText(chainCode);
				fieldFirstDifference.setText(normShape.getKey());
				fieldShapeNumber.setText(normShape.getValue());
			}
		});
		btnCalculateChain.setBounds(8, 131, 426, 25);
		this.getContentPane().add(btnCalculateChain);
		
		JButton btnCopyChainCode = new JButton("copy");
		btnCopyChainCode.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String str = fieldChainCode.getText().isEmpty() ? "" : fieldChainCode.getText();
				StringSelection stringSelection = new StringSelection(str);
				Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
				clipboard.setContents(stringSelection, null);
			}
		});
		btnCopyChainCode.setBounds(446, 50, 70, 15);
		this.getContentPane().add(btnCopyChainCode);
		
		JButton btnCopyFirstDifference = new JButton("copy");
		btnCopyFirstDifference.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String str = fieldFirstDifference.getText().isEmpty() ? "" : fieldFirstDifference.getText();
				StringSelection stringSelection = new StringSelection(str);
				Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
				clipboard.setContents(stringSelection, null);
			}
		});
		btnCopyFirstDifference.setBounds(446, 77, 70, 15);
		this.getContentPane().add(btnCopyFirstDifference);
		
		JButton btnCopyShapeNumber = new JButton("copy");
		btnCopyShapeNumber.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String str = fieldShapeNumber.getText().isEmpty() ? "" : fieldShapeNumber.getText();
				StringSelection stringSelection = new StringSelection(str);
				Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
				clipboard.setContents(stringSelection, null);
			}
		});
		btnCopyShapeNumber.setBounds(446, 104, 70, 15);
		this.getContentPane().add(btnCopyShapeNumber);
	}

}
