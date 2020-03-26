package view;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.Map;

import javax.swing.DefaultListCellRenderer;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;

import imageUtils.Image;
import imageUtils.StructuringElements;
import operations.Morphology;

public class MorphOperationsPopup extends Popup {
	protected JLabel lblOperation;
	protected JComboBox comboBoxOperations;
	protected JLabel lblStructuringElement;
	protected DefaultListCellRenderer listRenderer;
	protected JComboBox comboBoxStructuringElements;
	protected JCheckBox chckbxImageOne;
	protected JCheckBox chckbxImageTwo;
	
	interface MorphOperation {
		Image op(Image a, Image b);
	}
	
	public MorphOperationsPopup(GUI parent, String title, int width, int height) {
		super(parent, title, width, height);
		
		lblOperation = new JLabel("Operation: ");
		lblOperation.setBounds(12, 12, 87, 15);
		this.getContentPane().add(lblOperation);

//		MorphOperation[] operations = {
//			Morphology::opening,
//			Morphology::closing,
//			Morphology::dilation,
//			Morphology::erosion
//		};
		Map<String, MorphOperation> operations = new HashMap<String, MorphOperation>() {
			{
				put("Opening", Morphology::opening);
				put("Closing", Morphology::closing);
				put("Dilation", Morphology::dilation);
				put("Erosion", Morphology::erosion);
				put("Skeletonize", Morphology::skeletonize);
			}
		};
		
//		String[] opNames = {"Opening", "Closing", "Dilation", "Erosion"};
		comboBoxOperations = new JComboBox(operations.keySet().toArray());
		comboBoxOperations.setBounds(12, 37, 87, 24);
		this.getContentPane().add(comboBoxOperations);
		
		lblStructuringElement = new JLabel("Structuring element:");
		lblStructuringElement.setBounds(117, 12, 160, 15);
		this.getContentPane().add(lblStructuringElement);
	
		ImageIcon[] images = {
				new ImageIcon(parent.resources + "/struct_elems/cross.png"),
				new ImageIcon(parent.resources + "/struct_elems/line.png"),
				new ImageIcon(parent.resources + "/struct_elems/square.png")
		};
		
		Image[] structElems = {
				StructuringElements.cross(),
				StructuringElements.line(),
				StructuringElements.square()
		};

		DefaultListCellRenderer listRenderer = new DefaultListCellRenderer();
		listRenderer.setHorizontalAlignment(DefaultListCellRenderer.CENTER);
		
		comboBoxStructuringElements = new JComboBox(images);
		comboBoxStructuringElements.setBounds(117, 37, 160, 67);
		comboBoxStructuringElements.setRenderer(listRenderer);
		this.getContentPane().add(comboBoxStructuringElements);
		
		JButton btnNewButton = new JButton("Apply operation");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String opItem = (String) comboBoxOperations.getSelectedItem();
				int structElemIndex = comboBoxStructuringElements.getSelectedIndex();
				
				if (chckbxImageOne.isSelected() && parent.imageOne != null) {
					parent.imageOne = operations.get(opItem).op(
							parent.imageOne, structElems[structElemIndex]);
					parent.updateOne();
				}
				if (chckbxImageTwo.isSelected() && parent.imageTwo != null) {
					parent.imageTwo = operations.get(opItem).op(
							parent.imageTwo, structElems[structElemIndex]);
					parent.updateTwo();
				}
			}
		});
		btnNewButton.setBounds(12, 116, 265, 25);
		this.getContentPane().add(btnNewButton);
		
		chckbxImageOne = new JCheckBox("Image 1");
		chckbxImageOne.setSelected(true);
		chckbxImageOne.setEnabled(false);
		chckbxImageOne.setBounds(12, 69, 87, 15);
		this.getContentPane().add(chckbxImageOne);
		
		chckbxImageTwo = new JCheckBox("Image 2");
		chckbxImageTwo.setEnabled(false);
		chckbxImageTwo.setBounds(12, 90, 87, 15);
		this.getContentPane().add(chckbxImageTwo);
	}

}
