package view;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JButton;
import javax.swing.JLabel;

public class DeformPopup extends Popup {
	protected JButton btnDeform;
	protected JLabel lblTriangle1;
	protected JLabel lblTriangle2;
	protected JLabel lblP;
	protected JLabel lblPprime;
	protected JLabel lblQ;
	protected JLabel lblQprime;
	protected JLabel lblR;
	protected JLabel lblRprime;
	
	public DeformPopup(GUI parent, String title, int width, int height) {
		super(parent, title, width, height);
		
		this.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent arg0) {
				parent.deformToggle = false;
				
				parent.lblImageOne.clearCanvas();
				parent.lblImageTwo.clearCanvas();
				
				lblP.setText("P");
				lblQ.setText("Q");
				lblR.setText("R");
				lblPprime.setText("P'");
				lblQprime.setText("Q'");
				lblRprime.setText("R'");
				
//				parent.lblImageTwo.setDrawingEnabled(true);
				parent.lblImageOne.setDrawingEnabled(false);
				parent.lblImageTwo.setDrawingEnabled(false);
			}
		});
		
		btnDeform = new JButton("Reset Triangles");
		btnDeform.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				lblP.setText("P");
				lblQ.setText("Q");
				lblR.setText("R");
				lblPprime.setText("P'");
				lblQprime.setText("Q'");
				lblRprime.setText("R'");
				
				parent.lblImageOne.clearCanvas();
				parent.lblImageTwo.clearCanvas();
				
				parent.lblImageOne.clicks = 0;
				parent.lblImageTwo.clicks = 0;
				parent.lblImageOne.setDrawingEnabled(true);
			}
		});
		btnDeform.setBounds(12, 0, 290, 25);
		this.getContentPane().add(btnDeform);
		
		lblTriangle1 = new JLabel("Triangle 1:");
		lblTriangle1.setBounds(12, 26, 88, 15);
		this.getContentPane().add(lblTriangle1);
		
		lblTriangle2 = new JLabel("Triangle 2:");
		lblTriangle2.setBounds(163, 26, 75, 15);
		this.getContentPane().add(lblTriangle2);
		
		lblP = new JLabel("P");
		lblP.setBounds(12, 42, 139, 15);
		this.getContentPane().add(lblP);
		
		lblPprime = new JLabel("P'");
		lblPprime.setBounds(163, 42, 139, 15);
		this.getContentPane().add(lblPprime);
		
		lblQ = new JLabel("Q");
		lblQ.setBounds(12, 53, 139, 15);
		this.getContentPane().add(lblQ);
		
		lblQprime = new JLabel("Q'");
		lblQprime.setBounds(163, 53, 139, 15);
		this.getContentPane().add(lblQprime);
		
		lblR = new JLabel("R");
		lblR.setBounds(12, 69, 139, 15);
		this.getContentPane().add(lblR);
		
		lblRprime = new JLabel("R'");
		lblRprime.setBounds(163, 69, 139, 15);
		this.getContentPane().add(lblRprime);
	}

}
