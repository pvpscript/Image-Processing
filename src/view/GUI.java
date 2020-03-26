package view;

import java.awt.Component;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JButton;
import javax.swing.ImageIcon;

import javax.swing.SwingConstants;


import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.highgui.HighGui;
import org.opencv.imgcodecs.Imgcodecs;

import dataStructures.Pair;
import dataStructures.Triangle;
import imageUtils.Image;
import imageUtils.Pixel.Type;
import imageUtils.Point;
import imageUtils.StructuringElements;
import imageUtils.Image.Channel;
import imageUtils.Image.Fill;
import operations.Arithmetical;
import operations.Description;
import operations.Gray;
import operations.Histogram;
import operations.Inverter;
import operations.Logical;
import operations.Morphology;
import operations.Transform;
import operations.Zoom;

import javax.swing.JMenu;
import javax.swing.JMenuBar;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;

import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JFileChooser;

import java.awt.event.ActionEvent;

import java.awt.event.ActionListener;

import java.awt.Font;

import java.awt.event.MouseMotionAdapter;
import java.awt.image.BufferedImage;

import javax.swing.LayoutStyle.ComponentPlacement;

import javax.swing.border.LineBorder;
import java.awt.Color;

public class GUI {
	protected String resources = System.getProperty("user.dir") +
			"/resources";
	
	protected ImageIcon addCanvasIcon = new ImageIcon(resources +
			"/icons/add-canvas.png");
	protected ImageIcon removeCanvasIcon = new ImageIcon(resources +
			"/icons/remove-canvas.png");
	
	protected Image imageOne = null;
	protected Image imageTwo = null;
	protected Image originalImageOne = null;
	protected Image originalImageTwo = null;
	
	protected Triangle v = null;
	protected Triangle w = null;
	protected boolean deformToggle = false;
	
	protected Canvas lblImageOne = new Canvas();
	protected Canvas lblImageTwo = new Canvas();
	protected boolean canvasTwoEnabled = false;
	
	protected JButton btnResetImageOne;
	protected JButton btnResetImageTwo = new JButton("Reset");
	protected JButton btnSwapImages = new JButton("");
	
	protected JFrame frame;
//	protected final Action actionPickImageOne = new ActionPickImageOne();
//	protected final Action actionPickImageTwo = new ActionPickImageTwo();
//	protected final Action actionDeform = new ActionDeform();
//	protected final Action actionFill = new ActionFill();
	
	protected Popup fillPopup =
			new FillPopup(this, "Fill options", 180, 130);
	protected Popup deformPopup =
			new DeformPopup(this, "Deform triangles", 320, 125);
	protected Popup graphicPopup =
			new GraphicPopup(this, "Graphic options", 460, 190);
	protected Popup houghPopup =
			new HoughPopup(this, "Hough transform options", 520, 180);
	protected Popup chainPopup =
			new ChainPopup(this, "Chain code", 540, 195);
	protected Popup kMeansPopup =
			new KMeansPopup(this, "K-Means", 330, 110);
	protected Popup isodataPopup =
			new IsodataPopup(this, "ISODATA", 290, 130);
	protected Popup fourierPopup =
			new FourierPopup(this, "Fourier filter", 320, 155);
	protected Popup morphOperationsPopup =
			new MorphOperationsPopup(this,
					"Morphological operations", 295, 175);
	protected Popup rotatePopup =
			new RotatePopup(this, "Rotation", 150, 150);
	protected Popup translatePopup =
			new TranslatePopup(this,"Translation", 125, 170);
	
	protected boolean fillToggle = false;
	protected double[] fillColor = {0, 0, 0};
	protected int fillOffset = 0;
	
	protected boolean kMeansClickToggle = false;
	protected int centroids;
	protected ArrayList<Point> centroidLocations;

	/**
	 * Create the application.
	 */
	public GUI() {
		initialize();
	}
	
	public JFrame getFrame() {
		return this.frame;
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame("Image Processing");
		frame.setBounds(100, 100, 1000, 700);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		JLabel lblMousePositionOne = new JLabel(" ");
		JLabel lblMousePositionTwo = new JLabel(" ");
		
		JScrollPane scrollPaneOne = new JScrollPane();
		scrollPaneOne.setBorder(new LineBorder(new Color(0, 0, 0)));
		scrollPaneOne.setAlignmentY(Component.CENTER_ALIGNMENT);
		
		
//		Canvas lblImageOne = new Canvas();
		lblImageOne.addMouseListener(new MouseAdapter() {
			Point p = null;
			Point q = null;
			Point r = null;
			
			@Override
			public void mouseClicked(MouseEvent arg0) {
				if (deformToggle && lblImageOne.getDrawingEnabled()) {
					lblImageOne.clicks++;

					if (lblImageOne.clicks == 1) {
						p = new Point(arg0.getY(), arg0.getX());
						((DeformPopup)deformPopup).lblP.setText(
								"P (" + arg0.getX() + ", " + arg0.getY() + ");");
						lblImageOne.setFirstPoint(arg0.getX(), arg0.getY());
					} else if (lblImageOne.clicks == 2) {
						q = new Point(arg0.getY(), arg0.getX());
						((DeformPopup)deformPopup).lblQ.setText(
								"Q (" + arg0.getX() + ", " + arg0.getY() + ");");
						lblImageOne.setSecondPoint(arg0.getX(), arg0.getY());
					} else if (lblImageOne.clicks == 3) {
						r = new Point(arg0.getY(), arg0.getX());
						v = new Triangle(p, q, r);

						lblImageOne.setDrawingEnabled(false);
						lblImageTwo.setDrawingEnabled(true);
						
						((DeformPopup)deformPopup).lblR.setText(
								"R (" + arg0.getX() + ", " + arg0.getY() + ");");
						lblImageOne.setThirdPoint(arg0.getX(), arg0.getY());
						lblImageOne.drawFigure(Canvas.drawTypes.TRIANGLE);
					}
				} else if (fillToggle) {
					Image fillResult;
					
					if (imageOne != null) {
						fillResult = Morphology.floodFillPoint(imageOne,
								fillColor, arg0.getY(), arg0.getX(),
								fillOffset);
						if (fillResult != null)
							updateOne(fillResult);
					}
				} else if (kMeansClickToggle && lblImageOne.getDrawingEnabled()) {
					if (arg0.getY() < imageOne.getRows() &&
							arg0.getX() < imageOne.getCols()) {
						centroids++;
						centroidLocations.add(new Point(arg0.getY(), arg0.getX()));
						lblImageOne.addCircle(arg0.getX(), arg0.getY());
						
						lblImageOne.drawFigure(Canvas.drawTypes.CIRCLE);
						((KMeansPopup)kMeansPopup).fieldClusters.setText(
								String.valueOf(centroids));
					}
				}
			}
			
			@Override
			public void mouseExited(MouseEvent e) {
				lblMousePositionOne.setText(" ");
			}
		});
		lblImageOne.setVerticalAlignment(SwingConstants.TOP);
		lblImageOne.setHorizontalAlignment(SwingConstants.LEFT);
		lblImageOne.addMouseMotionListener(new MouseMotionAdapter() {
			@Override
			public void mouseMoved(MouseEvent arg0) {
				lblMousePositionOne.setText("X: " + arg0.getX() + " Y: " + arg0.getY());
			}
		});
		scrollPaneOne.setViewportView(lblImageOne);
		
		scrollPaneOne.setColumnHeaderView(lblMousePositionOne);
		
		JButton btnAddImageOne = new JButton();
		btnAddImageOne.setText("Open");
		btnAddImageOne.setFont(new Font("Dialog", Font.BOLD, 12));
		btnAddImageOne.setHorizontalTextPosition(JButton.CENTER);
		btnAddImageOne.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				JFileChooser chooser = new JFileChooser();
			    chooser.setFileFilter(new ImageFileFilter());
			    
			    int returnVal = chooser.showOpenDialog(btnAddImageOne);
			    if (returnVal == JFileChooser.APPROVE_OPTION) {
		    		File imageFile = chooser.getSelectedFile();
		    		
		    		originalImageOne = openImage(imageFile.getPath());
		    		imageOne = originalImageOne.clone();
		    		
		    		updateOne();
		    		
		    		btnResetImageOne.setEnabled(true);
			    }
			    
			    updateSwapAspects();
			}
		});
//		btnAddImageOne.setAction(actionPickImageOne);
		
		JScrollPane scrollPaneTwo = new JScrollPane();
		scrollPaneTwo.setVisible(false);
		scrollPaneTwo.setBorder(new LineBorder(new Color(0, 0, 0)));
		
		scrollPaneTwo.setColumnHeaderView(lblMousePositionTwo);
		
//		JLabel lblImageTwo = new JLabel("");
		lblImageTwo.addMouseListener(new MouseAdapter() {
			Point pPrime = null;
			Point qPrime = null;
			Point rPrime = null;
			
			@Override
			public void mouseClicked(MouseEvent arg0) {
				if (deformToggle && lblImageTwo.getDrawingEnabled()) {
					lblImageTwo.clicks++;

					if (lblImageTwo.clicks == 1) {
						pPrime = new Point(arg0.getY(), arg0.getX());
						((DeformPopup)deformPopup).lblPprime.setText(
								"P' (" + arg0.getX() + ", " + arg0.getY() + ");");
						lblImageTwo.setFirstPoint(arg0.getX(), arg0.getY());
					} else if (lblImageTwo.clicks == 2) {
						qPrime = new Point(arg0.getY(), arg0.getX());
						((DeformPopup)deformPopup).lblQprime.setText(
								"Q' (" + arg0.getX() + ", " + arg0.getY() + ");");
						lblImageTwo.setSecondPoint(arg0.getX(), arg0.getY());
					} else if (lblImageTwo.clicks == 3) {
						lblImageTwo.setDrawingEnabled(false);
						
						rPrime = new Point(arg0.getY(), arg0.getX());
						w = new Triangle(pPrime, qPrime, rPrime);
						
						((DeformPopup)deformPopup).lblRprime.setText(
								"R' (" + arg0.getX() + ", " + arg0.getY() + ");");
						lblImageTwo.setThirdPoint(arg0.getX(), arg0.getY());
						lblImageTwo.drawFigure(Canvas.drawTypes.TRIANGLE);
						
						updateTwo(Transform.deform(imageTwo, imageOne, v, w));
					}
				}// else if (fillToggle) {
//					double[] color = {0, 0, 0};
//					int offset;
//					Image fillResult;
//					
//					color[0] = Double.parseDouble(fieldB.getText());
//					color[1] = Double.parseDouble(fieldG.getText());
//					color[2] = Double.parseDouble(fieldR.getText());
//					
//					offset = Integer.parseInt(fieldOffset.getText());
//					
//					if (imageTwo != null) {
//						fillResult = Morphology.floodFillPoint(imageTwo, color,
//								arg0.getY(), arg0.getX(), offset);
//						if (fillResult != null)
//							updateTwo(fillResult);
//					}
//					
//					//System.out.println("");
//				}
			}
			
			@Override
			public void mouseExited(MouseEvent e) {
				lblMousePositionTwo.setText(" ");
			}
		});
		lblImageTwo.addMouseMotionListener(new MouseMotionAdapter() {
			@Override
			public void mouseMoved(MouseEvent arg0) {
				lblMousePositionTwo.setText("X: " + arg0.getX() + " Y: " + arg0.getY());
			}
		});
		lblImageTwo.setVerticalAlignment(SwingConstants.TOP);
		scrollPaneTwo.setViewportView(lblImageTwo);
		
		JButton btnAddImageTwo = new JButton();
		btnAddImageTwo.setText("Open");
		btnAddImageTwo.setVisible(false);
		btnAddImageTwo.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				JFileChooser chooser = new JFileChooser();
			    chooser.setFileFilter(new ImageFileFilter());

			    int returnVal = chooser.showOpenDialog(btnAddImageOne);
			    if (returnVal == JFileChooser.APPROVE_OPTION) {
			    	File imageFile = chooser.getSelectedFile();
			    	originalImageTwo = openImage(imageFile.getPath());
			    	imageTwo = originalImageTwo.clone();
			    	
		    		updateTwo();
		    		
		    		btnResetImageTwo.setEnabled(true);
			    }
			    
			    updateSwapAspects();
			}
		});
//		btnAddImageTwo.setAction(actionPickImageTwo);
		
		JButton btnNewImageOne = new JButton("New");
		btnNewImageOne.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Pair<Integer, Integer> dim =
						Utils.twoFieldsOptionPane();
				if (dim != null) {
					originalImageOne = new Image(dim.getKey(), dim.getValue(),
							CvType.CV_8UC3, Type.BGR, Fill.WHITE);
					imageOne = originalImageOne.clone();
					updateOne();
					
					btnResetImageOne.setEnabled(true);
				}
				
				updateSwapAspects();
			}
		});
		
		JButton btnSaveImageOne = new JButton("Save");
		btnSaveImageOne.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.out.println("Save image one.");
				
				JFileChooser chooser = new JFileChooser();

			    int returnVal = chooser.showSaveDialog(btnAddImageOne);
			    if (returnVal == JFileChooser.APPROVE_OPTION) {
		    		File imageFile = chooser.getSelectedFile();
		    		saveImage(imageFile.getAbsolutePath(), imageOne);
			    }
			}
		});
		
		JButton btnNewImageTwo = new JButton("New");
		btnNewImageTwo.setVisible(false);
		btnNewImageTwo.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.out.println("New image for canvas two.");
				
				Pair<Integer, Integer> dim =
						Utils.twoFieldsOptionPane();
				if (dim != null) {
					imageTwo = new Image(dim.getKey(), dim.getValue(),
							CvType.CV_8UC3, Type.BGR, Fill.WHITE);
					updateTwo();
					
					btnResetImageTwo.setEnabled(true);
				}
				
				updateSwapAspects();
			}
		});
		
		JButton btnSaveImageTwo = new JButton("Save");
		btnSaveImageTwo.setVisible(false);
		btnSaveImageTwo.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.out.println("Save image two.");
				
				JFileChooser chooser = new JFileChooser();

			    int returnVal = chooser.showSaveDialog(btnAddImageOne);
			    if (returnVal == JFileChooser.APPROVE_OPTION) {
		    		File imageFile = chooser.getSelectedFile();
		    		saveImage(imageFile.getAbsolutePath(), imageTwo);
			    }
			}
		});
		
		JButton btnSwitchCanvas = new JButton("");
		btnSwitchCanvas.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (canvasTwoEnabled) {
					btnSwitchCanvas.setIcon(addCanvasIcon);
					scrollPaneTwo.setVisible(false);
					scrollPaneTwo.setVisible(false);
					btnNewImageTwo.setVisible(false);
					btnSaveImageTwo.setVisible(false);
					btnAddImageTwo.setVisible(false);
					btnResetImageTwo.setVisible(false);
					btnSwapImages.setVisible(false);
					
					((GraphicPopup)graphicPopup).rdbtnImageOne.setEnabled(false);
					((GraphicPopup)graphicPopup).rdbtnImageOne.setSelected(true);
					((GraphicPopup)graphicPopup).rdbtnImageTwo.setEnabled(false);
					((GraphicPopup)graphicPopup).chckbxShowAxisTwo.setEnabled(false);
					((MorphOperationsPopup)morphOperationsPopup).chckbxImageOne.setEnabled(false);
					((MorphOperationsPopup)morphOperationsPopup).chckbxImageOne.setSelected(true);
					((MorphOperationsPopup)morphOperationsPopup).chckbxImageTwo.setEnabled(false);
					((MorphOperationsPopup)morphOperationsPopup).chckbxImageTwo.setSelected(false);
					((RotatePopup)rotatePopup).rdbtnImageOne.setEnabled(false);
					((RotatePopup)rotatePopup).rdbtnImageOne.setSelected(true);
					((RotatePopup)rotatePopup).rdbtnImageTwo.setEnabled(false);
					((TranslatePopup)translatePopup).rdbtnImageOne.setEnabled(false);
					((TranslatePopup)translatePopup).rdbtnImageOne.setSelected(true);
					((TranslatePopup)translatePopup).rdbtnImageTwo.setEnabled(false);
				} else {
					btnSwitchCanvas.setIcon(removeCanvasIcon);
					scrollPaneTwo.setVisible(true);
					scrollPaneTwo.setVisible(true);
					btnNewImageTwo.setVisible(true);
					btnSaveImageTwo.setVisible(true);
					btnAddImageTwo.setVisible(true);
					btnResetImageTwo.setVisible(true);
					btnSwapImages.setVisible(true);	
					
					((GraphicPopup)graphicPopup).rdbtnImageOne.setEnabled(true);
					((GraphicPopup)graphicPopup).rdbtnImageTwo.setEnabled(true);
					((GraphicPopup)graphicPopup).chckbxShowAxisTwo.setEnabled(true);
					((MorphOperationsPopup)morphOperationsPopup).chckbxImageOne.setEnabled(true);
					((MorphOperationsPopup)morphOperationsPopup).chckbxImageTwo.setEnabled(true);
					((RotatePopup)rotatePopup).rdbtnImageOne.setEnabled(true);
					((RotatePopup)rotatePopup).rdbtnImageTwo.setEnabled(true);
					((TranslatePopup)translatePopup).rdbtnImageOne.setEnabled(true);
					((TranslatePopup)translatePopup).rdbtnImageTwo.setEnabled(true);
				}
				
				updateSwapAspects();
				
				canvasTwoEnabled = !canvasTwoEnabled;
			}
		});
		btnSwitchCanvas.setIcon(addCanvasIcon);
		btnSwapImages.setEnabled(false);
		
		btnSwapImages.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {				
				if (imageOne != null && imageTwo == null) {
					imageTwo = imageOne;
					originalImageTwo = originalImageOne;
					imageOne = null;
				} else if (imageOne == null && imageTwo != null) {
					imageOne = imageTwo;
					originalImageOne = originalImageTwo;
					imageTwo = null;
				} else if (imageOne != null && imageTwo != null) {
					Image temp = imageOne;
					imageOne = imageTwo;
					imageTwo = temp;
					temp = originalImageOne;
					originalImageOne = originalImageTwo;
					originalImageTwo = temp;
				}
				
				updateOne();
				updateTwo();
				updateSwapAspects();
			}
		});
		btnSwapImages.setVisible(false);
		btnSwapImages.setIcon(new ImageIcon(resources + "/icons/not-swap.png"));
		
		btnResetImageOne = new JButton("Reset");
		btnResetImageOne.setEnabled(false);
		btnResetImageOne.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				imageOne = originalImageOne.clone();
				updateOne();
			}
		});
		
		GroupLayout groupLayout = new GroupLayout(frame.getContentPane());
		groupLayout.setHorizontalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup()
					.addGap(12)
					.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
						.addComponent(scrollPaneOne, GroupLayout.DEFAULT_SIZE, 465, Short.MAX_VALUE)
						.addGroup(groupLayout.createSequentialGroup()
							.addComponent(btnNewImageOne)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(btnAddImageOne)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(btnSaveImageOne, GroupLayout.PREFERRED_SIZE, 72, GroupLayout.PREFERRED_SIZE)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(btnResetImageOne)))
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(groupLayout.createParallelGroup(Alignment.TRAILING)
						.addComponent(btnSwitchCanvas, GroupLayout.PREFERRED_SIZE, 30, GroupLayout.PREFERRED_SIZE)
						.addComponent(btnSwapImages, GroupLayout.PREFERRED_SIZE, 30, GroupLayout.PREFERRED_SIZE))
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(groupLayout.createParallelGroup(Alignment.TRAILING)
						.addComponent(scrollPaneTwo, GroupLayout.DEFAULT_SIZE, 465, Short.MAX_VALUE)
						.addGroup(groupLayout.createSequentialGroup()
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(btnNewImageTwo)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(btnAddImageTwo)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(btnSaveImageTwo, GroupLayout.PREFERRED_SIZE, 72, GroupLayout.PREFERRED_SIZE)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(btnResetImageTwo)))
					.addGap(12))
		);
		groupLayout.setVerticalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup()
					.addGap(12)
					.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
						.addGroup(groupLayout.createSequentialGroup()
							.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
								.addComponent(scrollPaneOne, GroupLayout.DEFAULT_SIZE, 449, Short.MAX_VALUE)
								.addComponent(scrollPaneTwo, GroupLayout.DEFAULT_SIZE, 449, Short.MAX_VALUE))
							.addGap(4)
							.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
								.addComponent(btnNewImageOne)
								.addGroup(groupLayout.createParallelGroup(Alignment.BASELINE, false)
									.addComponent(btnSaveImageOne)
									.addComponent(btnNewImageTwo)
									.addComponent(btnResetImageOne)
									.addComponent(btnResetImageTwo))
								.addGroup(groupLayout.createParallelGroup(Alignment.BASELINE)
									.addComponent(btnSaveImageTwo)
									.addComponent(btnAddImageTwo))
								.addComponent(btnAddImageOne)))
						.addGroup(groupLayout.createSequentialGroup()
							.addComponent(btnSwitchCanvas)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(btnSwapImages)
							.addPreferredGap(ComponentPlacement.RELATED)))
					.addGap(12))
		);
		btnResetImageTwo.setEnabled(false);
		btnResetImageTwo.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				imageTwo = originalImageTwo.clone();
				updateTwo();
			}
		});
		btnResetImageTwo.setVisible(false);
		groupLayout.setHonorsVisibility(true);
		frame.getContentPane().setLayout(groupLayout);

		JMenuBar menuBar = new JMenuBar();
		frame.setJMenuBar(menuBar);
		
		JMenu mnGraphic = new JMenu("Graphic");
		menuBar.add(mnGraphic);
		
		JMenuItem mntmGraphic = new JMenuItem("Plot graphic");
		mnGraphic.add(mntmGraphic);
		mntmGraphic.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				graphicPopup.open();
			}
		});
		
		JMenu mnActions = new JMenu("Actions");
		menuBar.add(mnActions);
		
		JMenu mnZoom = new JMenu("Zoom");
		mnActions.add(mnZoom);
		
		JMenu mnSquared = new JMenu("Squared");
		mnZoom.add(mnSquared);
		
		JMenuItem mntmSquaredIn = new JMenuItem("In");
		mntmSquaredIn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (imageOne != null) {
					imageOne = Zoom.squaredIn(imageOne);
					
					updateOne();
				}
			}
		});
		mnSquared.add(mntmSquaredIn);
		
		JMenuItem mntmSquaredOut = new JMenuItem("Out");
		mntmSquaredOut.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (imageOne != null) {
					imageOne = Zoom.squaredOut(imageOne);
					
					updateOne();
				}
			}
		});
		mnSquared.add(mntmSquaredOut);
		
		JMenu mnLinear = new JMenu("Linear");
		mnZoom.add(mnLinear);
		
		JMenuItem mntmLinearIn = new JMenuItem("In");
		mntmLinearIn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (imageOne != null) {
					imageOne = Zoom.linearIn(imageOne);
					
					updateOne();
				}
			}
		});
		mnLinear.add(mntmLinearIn);
		
		JMenuItem mntmLinearOut = new JMenuItem("Out");
		mntmLinearOut.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (imageOne != null) {
					imageOne = Zoom.linearOut(imageOne);
					
					updateOne();
				}
			}
		});
		mnLinear.add(mntmLinearOut);
		
//		JMenu mnConversion = new JMenu("Conversion");
//		mnActions.add(mnConversion);
//		
//		JMenuItem mntmRgb = new JMenuItem("RGB");
//		mnConversion.add(mntmRgb);
//		
//		JMenuItem mntmYiq = new JMenuItem("YIQ");
//		mnConversion.add(mntmYiq);
//		
//		JMenuItem mntmHsv = new JMenuItem("HSV");
//		mnConversion.add(mntmHsv);
		
		JMenu mnConvolution = new JMenu("Convolution");
		mnActions.add(mnConvolution);
		
		JMenuItem mntmDetectBorder = new JMenuItem("Detect Border");
		mnConvolution.add(mntmDetectBorder);
		
		JMenu mnFilters = new JMenu("Filters");
		mnConvolution.add(mnFilters);
		
		JMenuItem mntmMean = new JMenuItem("Mean");
		mnFilters.add(mntmMean);
		
		JMenuItem mntmMedian = new JMenuItem("Median");
		mnFilters.add(mntmMedian);
		
		JMenuItem mntmFourier = new JMenuItem("Fourier");
		mntmFourier.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				fourierPopup.open();
			}
		});
		mnFilters.add(mntmFourier);
		
		JMenu mnDescription = new JMenu("Description");
		mnActions.add(mnDescription);
		
		JMenuItem mntmDetectLines = new JMenuItem("Detect Lines (Hough)");
		mntmDetectLines.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				houghPopup.open();
			}
		});
		mnDescription.add(mntmDetectLines);
		
		JMenuItem mntmChainCode = new JMenuItem("Chain code");
		mntmChainCode.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				chainPopup.open();
			}
		});
		mnDescription.add(mntmChainCode);
		
		JMenu mnBugFollower = new JMenu("Bug Follower");
		mnDescription.add(mnBugFollower);
		
		JMenuItem mntmSimple = new JMenuItem("Simple");
		mntmSimple.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if (imageOne != null) {
					imageOne = Description.simpleBugFollower(imageOne);
					updateOne();
				}
			}
		});
		mnBugFollower.add(mntmSimple);
		
		JMenuItem mntmBacktracking = new JMenuItem("Backtracking");
		mntmBacktracking.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if (imageOne != null) {
					imageOne = Description.backtrackingBugFollower(imageOne);
					updateOne();
				}
			}
		});
		mnBugFollower.add(mntmBacktracking);
		
		JMenu mnGrayscale = new JMenu("Grayscale");
		mnActions.add(mnGrayscale);
		
		JMenu mnChannel = new JMenu("Channel");
		mnGrayscale.add(mnChannel);
		
		JMenuItem mntmR = new JMenuItem("R");
		mntmR.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (imageOne != null) {
					imageOne = new Image(imageOne.acquireMat(Channel.THIRD),
							Type.BGR);
					
					updateOne();
				}
			}
		});
		mnChannel.add(mntmR);
		
		JMenuItem mntmG = new JMenuItem("G");
		mntmG.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (imageOne != null) {
					imageOne = new Image(imageOne.acquireMat(Channel.SECOND),
							Type.BGR);
					
					updateOne();
				}
			}
		});
		mnChannel.add(mntmG);
		
		JMenuItem mntmB = new JMenuItem("B");
		mntmB.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (imageOne != null) {
					imageOne = new Image(imageOne.acquireMat(Channel.FIRST),
							Type.BGR);
					
					updateOne();
				}
			}
		});
		mnChannel.add(mntmB);
		
		JMenuItem mntmGrayMean = new JMenuItem("Mean");
		mntmGrayMean.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (imageOne != null) {
					imageOne = Gray.grayScale(imageOne);
				
					updateOne();
				}
			}
		});
		mnGrayscale.add(mntmGrayMean);
		
		JMenuItem mntmBinary = new JMenuItem("Binary");
		mntmBinary.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (imageOne != null) {
					imageOne = Gray.binary(imageOne);
					
					updateOne();
				}
			}
		});
		mnGrayscale.add(mntmBinary);
		
		JMenuItem mntmEqualize = new JMenuItem("Equalize");
		mntmEqualize.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (imageOne != null) {
					imageOne = Histogram.equalizeColor(imageOne);
					
					updateOne();
				}
			}
		});
		mnActions.add(mntmEqualize);
		
		JMenuItem mntmInvertColors = new JMenuItem("Invert Colors");
		mntmInvertColors.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (imageOne != null) {
					imageOne = Inverter.invertColors(imageOne);
					
					updateOne();
				}
			}
		});
		mnActions.add(mntmInvertColors);
		
		JMenu mnLogical = new JMenu("Logical");
		mnActions.add(mnLogical);
		
		JMenuItem mntmAnd = new JMenuItem("AND");
		mntmAnd.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (imageOne != null && imageTwo != null) {
					imageOne = Logical.logicalAnd(imageOne, imageTwo);
					
					updateOne();
				}
			}
		});
		mnLogical.add(mntmAnd);
		
		JMenuItem mntmOr = new JMenuItem("OR");
		mntmOr.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (imageOne != null && imageTwo != null) {
					imageOne = Logical.logicalOr(imageOne, imageTwo);
					
					updateOne();
				}
			}
		});
		mnLogical.add(mntmOr);
		
		JMenu mnArithmetical = new JMenu("Arithmetical");
		mnActions.add(mnArithmetical);
		
		JMenuItem mntmComplement = new JMenuItem("Complement");
		mntmComplement.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (imageOne != null) {
					imageOne = Arithmetical.complement(imageOne);
					
					updateOne();
				}
			}
		});
		mnArithmetical.add(mntmComplement);
		
		JMenuItem mntmDifference = new JMenuItem("Difference");
		mntmDifference.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (imageOne != null && imageTwo != null) {
					imageOne = Arithmetical.difference(imageOne, imageTwo);
					
					updateOne();
				}
			}
		});
		mnArithmetical.add(mntmDifference);
		
		JMenu mnMorphology = new JMenu("Morphology");
		mnActions.add(mnMorphology);
		
		JMenuItem mntmFloodFill = new JMenuItem("Flood Fill");
		mntmFloodFill.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (deformToggle) {
					deformPopup.dispose();
					deformToggle = false;
				}
				if (kMeansClickToggle) {
					kMeansPopup.dispose();
					kMeansClickToggle = false;
				}
				
				fillToggle = true;
				fillPopup.open();
			}
		});
		mnMorphology.add(mntmFloodFill);
		
		JMenuItem mntmOperations = new JMenuItem("Operations");
		mntmOperations.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				morphOperationsPopup.open();
			}
		});
		mnMorphology.add(mntmOperations);
		
		JMenu mnSegmentation = new JMenu("Segmentation");
		mnActions.add(mnSegmentation);
		
		JMenuItem mntmKmeans = new JMenuItem("K-Means");
		mntmKmeans.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (deformToggle) {
					deformPopup.dispose();
					deformToggle = false;
				}
				if (fillToggle) {
					fillPopup.dispose();
					fillToggle = false;
				}
				
				centroids = 0;
				centroidLocations = new ArrayList<Point>();
				lblImageOne.clearCanvas();
				
				kMeansPopup.open();
			}
		});
		mnSegmentation.add(mntmKmeans);
		
		JMenuItem mntmIsodata = new JMenuItem("ISODATA");
		mntmIsodata.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				isodataPopup.open();
			}
		});
		mnSegmentation.add(mntmIsodata);
		
		JMenu mnTransform = new JMenu("Transform");
		mnActions.add(mnTransform);
		
		JMenuItem mntmRotate = new JMenuItem("Rotate");
		mntmRotate.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (imageOne != null) {
					rotatePopup.open();
				}
			}
		});
		mnTransform.add(mntmRotate);
		
		JMenuItem mntmTranslate = new JMenuItem("Translate");
		mntmTranslate.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (imageOne != null) {
					translatePopup.open();
				}
			}
		});
		mnTransform.add(mntmTranslate);
		
		JMenuItem mntmDeform = new JMenuItem("Deform");
		mntmDeform.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if (canvasTwoEnabled) {
					if (fillToggle) {
						fillPopup.dispose();
						fillToggle = false;
					}
					if (kMeansClickToggle) {
						kMeansPopup.dispose();
						kMeansClickToggle = false;
					}
					
					deformToggle = true;
					lblImageOne.setDrawingEnabled(true);
					lblImageTwo.setDrawingEnabled(false);
					deformPopup.open();
				} else {
					JOptionPane.showMessageDialog(null,
							"Canvas two must be enabled.");
				}
			}
		});
		mnTransform.add(mntmDeform);
	}
	
//	private class ActionPickImageOne extends AbstractAction {
//		public ActionPickImageOne() {
//			putValue(NAME, "Open");
//			putValue(SHORT_DESCRIPTION, "Select an image for the first image field");
//		}
//		public void actionPerformed(ActionEvent e) {
//		}
//	}
//	
//	private class ActionPickImageTwo extends AbstractAction {
//		public ActionPickImageTwo() {
//			putValue(NAME, "Open");
//			putValue(SHORT_DESCRIPTION, "Select an image for the second image field");
//		}
//		public void actionPerformed(ActionEvent e) {
//		}
//	}
//	
//	private class ActionDeform extends AbstractAction {
//		public ActionDeform() {
//			putValue(NAME, "Deform");
//			putValue(SHORT_DESCRIPTION, "Perform a deformation");
//		}
//		public void actionPerformed(ActionEvent e) {
//			deformToggle = (deformToggle == false) ? true : false;
//			
//			if (btnFill.isEnabled()) {
//				lblImageOne.setDrawingEnabled(true);
//				lblImageTwo.setDrawingEnabled(false);
//				btnFill.setEnabled(false);
//			} else {
//				lblImageOne.clearCanvas();
//				lblImageTwo.clearCanvas();
//				
//				lblP.setText("P");
//				lblQ.setText("Q");
//				lblR_1.setText("R");
//				lblPprime.setText("P'");
//				lblQprime.setText("Q'");
//				lblRprime.setText("R'");
//				
//				lblImageTwo.setDrawingEnabled(true);
//				btnFill.setEnabled(true);
//			}
//		}
//	}
//	
//	private class ActionFill extends AbstractAction {
//		public ActionFill() {
//			putValue(NAME, "Fill");
//			putValue(SHORT_DESCRIPTION, "Flood fill clicked region.");
//		}
//		public void actionPerformed(ActionEvent e) {
//			fillToggle = (fillToggle == false) ? true : false; 
//			
//			if (btnDeform.isEnabled()) {
//				btnDeform.setEnabled(false);
//			} else {
//				btnDeform.setEnabled(true);
//			}
//		}
//	}
	
	protected void updateOne(Image image) {
		BufferedImage buffImage = (BufferedImage) HighGui.toBufferedImage(
				image.acquireMat());
		this.imageOne = image;
		
		lblImageOne.setIcon(new ImageIcon(buffImage));
	}
	
	protected void updateOne() {
		ImageIcon icon = null;
		
		if (imageOne != null) {
			BufferedImage buffImage = (BufferedImage) HighGui.toBufferedImage(
					this.imageOne.acquireMat());
			
			icon = new ImageIcon(buffImage);
		}
			
		lblImageOne.setIcon(icon);
	}
	
	protected void updateTwo(Image image) {
		BufferedImage buffImage = (BufferedImage) HighGui.toBufferedImage(
				image.acquireMat());
		this.imageTwo = image;
		
		lblImageTwo.setIcon(new ImageIcon(buffImage));
	}
	
	protected void updateTwo() {
		ImageIcon icon = null;
		
		if (imageTwo != null) {
			BufferedImage buffImage = (BufferedImage) HighGui.toBufferedImage(
					this.imageTwo.acquireMat());
			
			icon = new ImageIcon(buffImage);
		}
			
		lblImageTwo.setIcon(icon);
	}
	
	protected Image openImage(String fullPath) {
//		String fullPath = path + "/" + name;
		Mat matrix = Imgcodecs.imread(fullPath);
	
		return !matrix.empty() ? new Image(matrix, Type.BGR) : null;
	}
	
	protected void saveImage(String fullPath, Image image) {
		if (image != null)
			Imgcodecs.imwrite(fullPath, image.acquireMat());
	}
	
	protected void updateSwapAspects() {
		if (imageOne == null && imageTwo == null) {
			btnSwapImages.setIcon(new ImageIcon(resources + "/icons/not-swap.png"));
			btnSwapImages.setEnabled(false);
			btnResetImageOne.setEnabled(false);
			btnResetImageTwo.setEnabled(false);
		} else if (imageOne != null && imageTwo == null) {
			btnSwapImages.setIcon(new ImageIcon(resources + "/icons/left-to-right.png"));
			btnSwapImages.setEnabled(true);
			btnResetImageOne.setEnabled(true);
			btnResetImageTwo.setEnabled(false);
		} else if (imageOne == null && imageTwo != null) { 
			btnSwapImages.setIcon(new ImageIcon(resources + "/icons/right-to-left.png"));
			btnSwapImages.setEnabled(true);
			btnResetImageOne.setEnabled(false);
			btnResetImageTwo.setEnabled(true);
		} else {
			btnSwapImages.setIcon(new ImageIcon(resources + "/icons/swap.png"));
			btnSwapImages.setEnabled(true);
			btnResetImageOne.setEnabled(true);
			btnResetImageTwo.setEnabled(true);
		}
	}
}
