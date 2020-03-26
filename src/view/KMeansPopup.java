package view;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JTextField;

import imageUtils.Point;
import operations.Segmentation;


public class KMeansPopup extends Popup {
	protected JTextField fieldClusters;

	public KMeansPopup(GUI parent, String title, int width, int height) {
		super(parent, title, width, height);
		
		JLabel lblSeeds = new JLabel("Seeds: ");
		lblSeeds.setBounds(12, 17, 53, 15);
		this.getContentPane().add(lblSeeds);
		
		fieldClusters = new JTextField();
		fieldClusters.setText("10");
		fieldClusters.setBounds(68, 15, 114, 19);
		fieldClusters.setColumns(10);
		this.getContentPane().add(fieldClusters);
		this.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent arg0) {
				parent.kMeansClickToggle = false;
			}
		});
		
		String[] options = {"Seed number", "Click"};
		JComboBox<String> comboBoxOptions = new JComboBox<>(options);
		comboBoxOptions.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				switch(comboBoxOptions.getSelectedIndex()) {
				case 0:
					fieldClusters.setEnabled(true);
					fieldClusters.setText("10");
					
					parent.kMeansClickToggle = false;
					parent.lblImageOne.setDrawingEnabled(false);
					break;
				case 1:
					fieldClusters.setEnabled(false);
					fieldClusters.setText("0");
					
					parent.kMeansClickToggle = true;
					parent.lblImageOne.setDrawingEnabled(true);
					break;
				}
			}
		});
		comboBoxOptions.setSelectedIndex(0);
		comboBoxOptions.setBounds(194, 12, 122, 24);
		this.getContentPane().add(comboBoxOptions);
		
		JButton btnRun = new JButton("Run K-Means");
		btnRun.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				switch(comboBoxOptions.getSelectedIndex()) {
				case 0:
					String cText = fieldClusters.getText();
					int clusters = cText.isEmpty() ? 0 : Integer.parseInt(cText);
					
					parent.imageOne =
							Segmentation.kMeans(parent.imageOne, clusters, null);
					
					parent.updateOne();
					break;
				case 1:
					parent.imageOne =
							Segmentation.kMeans(parent.imageOne,
									parent.centroidLocations.size(),
									parent.centroidLocations);
					
					parent.lblImageOne.clearCanvas();
					parent.centroidLocations = new ArrayList<Point>();
					parent.centroids = 0;
					parent.updateOne();
					break;
				}
			}
		});
		btnRun.setBounds(12, 46, 304, 25);
		this.getContentPane().add(btnRun);
	}

}
