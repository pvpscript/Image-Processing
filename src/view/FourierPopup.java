package view;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JTextField;

import dataStructures.Pair;
import imageUtils.Image;
import operations.Convolution;

public class FourierPopup extends Popup {
	private JTextField fieldKeep;
	private JLabel lblCenterOffset;
	private JTextField fieldCenterOffset;
	private JCheckBox chckbxShowSpectrum;
	
	public FourierPopup(GUI parent, String title, int width, int height) {
		super(parent, title, width, height);
		
		JLabel lblFilter = new JLabel("Filter: ");
		lblFilter.setBounds(12, 12, 46, 15);
		this.getContentPane().add(lblFilter);
		
		String[] filters = {"High pass", "Low pass", "Band pass"};
		JComboBox<String> comboBoxFilter = new JComboBox<>(filters);
		comboBoxFilter.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (comboBoxFilter.getSelectedIndex() == 2) {
					lblCenterOffset.setEnabled(true);
					fieldCenterOffset.setEnabled(true);
				} else {
					lblCenterOffset.setEnabled(false);
					fieldCenterOffset.setEnabled(false);
				}
			}
		});
		comboBoxFilter.setBounds(12, 32, 108, 24);
		this.getContentPane().add(comboBoxFilter);
		
		JLabel lblKeep = new JLabel("Keep:");
		lblKeep.setBounds(195, 14, 46, 15);
		this.getContentPane().add(lblKeep);
		
		fieldKeep = new JTextField();
		fieldKeep.setText("0.1");
		fieldKeep.setBounds(242, 12, 63, 19);
		this.getContentPane().add(fieldKeep);
		fieldKeep.setColumns(10);
		
		lblCenterOffset = new JLabel("Center offset:");
		lblCenterOffset.setEnabled(false);
		lblCenterOffset.setBounds(138, 37, 109, 15);
		this.getContentPane().add(lblCenterOffset);
		
		fieldCenterOffset = new JTextField();
		fieldCenterOffset.setText("20");
		fieldCenterOffset.setEnabled(false);
		fieldCenterOffset.setBounds(242, 35, 63, 19);
		this.getContentPane().add(fieldCenterOffset);
		fieldCenterOffset.setColumns(10);
		
		JButton btnApplyFilter = new JButton("Apply Filter");
		btnApplyFilter.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Pair<Image, Image> result = null;
				String keep = fieldKeep.getText();
				String centerOffset = fieldCenterOffset.getText();
				
				switch(comboBoxFilter.getSelectedIndex()) {
				case 0:
					result = Convolution.highPass(parent.imageOne,
							keep.isEmpty() ? 0.1 : Double.parseDouble(keep));
					break;
				case 1:
					result = Convolution.lowPass(parent.imageOne,
							keep.isEmpty() ? 0.1 : Double.parseDouble(keep));
					break;
				case 2:
					result = Convolution.bandPass(parent.imageOne,
							keep.isEmpty() ? 0.1 : Double.parseDouble(keep),
							centerOffset.isEmpty() ? 20 : Integer.parseInt(centerOffset));
					break;
				}
				
				parent.imageOne = result.getKey();
				parent.updateOne();
				
				if (chckbxShowSpectrum.isSelected()) {
					parent.imageTwo = result.getValue();
					parent.updateTwo();
				}	
			}
		});
		btnApplyFilter.setBounds(12, 91, 293, 25);
		this.getContentPane().add(btnApplyFilter);
		
		chckbxShowSpectrum = new JCheckBox("Show filter spectrum");
		chckbxShowSpectrum.setBounds(12, 64, 182, 23);
		chckbxShowSpectrum.setSelected(true);
		this.getContentPane().add(chckbxShowSpectrum);
	}
	
	
}
