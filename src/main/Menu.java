package main;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

import org.opencv.core.Mat;
import org.opencv.highgui.HighGui;
import org.opencv.imgcodecs.Imgcodecs;

import dataStructures.Pair;
import imageUtils.Image;
import imageUtils.StructuringElements;
import imageUtils.Pixel.Type;
import operations.Conversion;
import operations.Convolution;
import operations.Gray;
import operations.Histogram;
import operations.Inverter;
import operations.Logical;
import operations.Arithmetical;
import operations.Morphology;
import operations.Transform;
import operations.Convolution.BorderDetectMethod;

@Deprecated
public class Menu {
	private static Scanner in;
	private static Map<String, Image> images;
	private static StructuringElements strElem;
	
	public static void showMenu() {
		in = new Scanner(System.in);
		strElem = new StructuringElements();
		images = null;
		
		System.out.println("=============== AWESOME MENU!1 ===============");

		fetchImages();
		mainMenu();
		
		System.out.println("\nDONE");
	}
	
	private static void fetchImages() {
		boolean sameFolder;
		images = (images == null) ? new HashMap<String, Image>() : images;
		
		System.out.print("\nThe images you're gonna work with are all located in the same folder? (Y/n) ");
		
		switch(in.nextLine()) {
		case "Y": case "y":
			sameFolder = true; break;
		case "N": case "n":
			sameFolder = false; break;
		default:
			sameFolder = false;
		}
		System.out.println();
		
		if (sameFolder) {
			System.out.print("Type in the images path: ");
			String imagesPath = in.nextLine();
			
			System.out.println("Type in the name of each image that is going to be used.");
			System.out.println("Press enter (blank line) when finished!\n");
			while(true) {
				System.out.print("> ");
				String imageName = in.hasNextLine() ? in.nextLine() : null;
				if (imageName == null || imageName.equals(""))
					break;
			
				Image image = openImage(imagesPath, imageName);
	
				if (image != null)
					if (!images.containsKey(imageName))
						images.put(imageName, image);
					else
						System.out.println("[*] Image \"" + imageName + "\" already exists");
				else
					System.out.println("[*] Image \"" + imageName + "\" not found in \"" + imagesPath + "\".");
			}
		} else {
			System.out.println("Type in the name of each image that is going to be used.");
			System.out.println("Note that the names MUST be followed by the absolute path where the image is located.");
			System.out.println("Press enter (blank line) when finished!\n");
			while(true) {
				System.out.print("> ");
				String imageName = in.hasNextLine() ? in.nextLine() : null;
				if (imageName == null || imageName.equals(""))
					break;
				
				String imagesPath = ".";
				int nameIndex = imageName.lastIndexOf("/");
				if (nameIndex > -1) {
					imagesPath = imageName.substring(0, nameIndex);
					imageName = imageName.substring(nameIndex + 1);
				}
			
				Image image = openImage(imagesPath, imageName);
	
				if (image != null)
					if (!images.containsKey(imageName))
						images.put(imageName, image);
					else
						System.out.println("[*] Image \"" + imageName + "\" already exists");
				else
					System.out.println("[*] Image \"" + imageName + "\" not found in \"" + imagesPath + "\".");
			}
		}
		
		System.out.println();
	}
	
	private static void mainMenu() {
		do {
			System.out.println("1 - List images.");
			System.out.println("2 - Show image.");
			System.out.println("3 - Perform an operation.");
			System.out.println("4 - Fetch more images.");
			System.out.println("5 - Clone image.");
			System.out.println("6 - Rename image.");
			System.out.println("7 - Remove image.");
			System.out.println("8 - Save image.");
			System.out.println("\n0 - Exit.");
			
			System.out.print("\nOption: ");
		} while(chooseMain());
	}
	
	private static boolean chooseMain() {
		byte op;
		
		if (in.hasNextLine()) {
			op = in.nextByte();
			in.nextLine();
		} else
			return false;
		
		switch(op) {
		case 0:
			return false;
		case 1: listImages(); break;
		case 2:	showImage(); break;
		case 3:	operations(); break;
		case 4: fetchImages(); break;
		case 5:	cloneImage(); break;
		case 6: renameImage(); break;
		case 7: removeImage(); break;
		case 8:	prepareSave(); break;
		}
		
		return true;
	}
	
	private static void listImages() {
		if (images.size() > 0) {
			System.out.println("\nImages in the database:");
			for(String imageName : images.keySet()) {
				System.out.println("> " + imageName);
			}
			System.out.println();
		} else
			System.out.println("\n[*] The image database is empty\n");
	}
	
	private static void renameImage() {
		Pair<String, Image> image = selectSingle();
		String newName;

		while(true) {
			System.out.print("New name for the image: ");
			newName = in.nextLine();
			
			if (!images.containsKey(newName)) {
				images.remove(image.getKey());
				images.put(newName, image.getValue());
				break;
			} else
				System.out.println("[*] Image \"" + newName + "\" already exists\n");
		}
	}
	
	private static void removeImage() {
		Pair<String, Image> image = selectSingle();
		String name = image.getKey();

		if (images.remove(name, image.getValue()))
			System.out.println("[*] Image \"" + name + "\" succesfully removed from database\n");
		else
			System.out.println("[*] Error while trying to remove \"" + name + "\" from database\n");
	}
	
	private static void cloneImage() {
		Pair<String, Image> image = selectSingle();
		String clonedImage;
		
		while(true) {
			System.out.print("Name the cloned image: ");
			clonedImage = in.nextLine();
			
			if (!images.containsKey(clonedImage)) {
				images.put(clonedImage, image.getValue());
				break;
			} else
				System.out.println("[*] Image \"" + clonedImage + "\" already exists\n");
		}
	}
	
	public static void operations() {
		do {
			System.out.println("---------- Choose an operation ----------\n");
			
			System.out.println("> Conversion:");
			System.out.println("\t1 - Convert an image to BGR.");
			System.out.println("\t2 - Convert an image to QIY.\n");
			
			System.out.println("> Gray:");
			System.out.println("\t3 - Convert an image to grayscale.");
			System.out.println("\t4 - Binarize an image.\n");
			
			System.out.println("> Histogram:");
			System.out.println("\t5 - Equalize a colored image.");
			System.out.println("\t6 - Equalize a grayscale image.");
			System.out.println("\t66 - Show contrast value.\n");
			
			System.out.println("> Inverter:");
			System.out.println("\t7 - Invert image colors.\n");

			System.out.println("> Logical:");
			System.out.println("\t8 - Perform a logical AND between two images.");
			System.out.println("\t9 - Perform a logical OR between two images.\n");
			
			System.out.println("> Mathematical:");
			System.out.println("\t10 - Get the complement of an image.");
			System.out.println("\t11 - Get the difference between two images.\n");
			
			System.out.println("> Morphology:");
			System.out.println("\t12 - Perform a closing in an image.");
			System.out.println("\t13 - Perform a dilation in an image.");
			System.out.println("\t14 - Perform a erosion in an image.");
			System.out.println("\t15 - Perform a opening in an image.");
			System.out.println("\t16 - Skeletonize an image.");
			System.out.println("\t17 - Flood Fill.\n");
			
			System.out.println("> Transform:");
			System.out.println("\t18 - Rotate an image.");
			System.out.println("\t19 - Translate an image.\n");
			
			System.out.println("> Convolution:");
			System.out.println("\t20 - Border detection.");
			System.out.println("\t21 - Mean.");
			System.out.println("\t22 - Median.\n");
			
			System.out.println("0 - Return to main menu.\n");
			
			System.out.print("\nOption: ");
		} while(chooseOperation());
	}
	
	private static boolean chooseOperation() {
		byte op;
		
		if (in.hasNextLine()) {
			op = in.nextByte();
			in.nextLine();
		} else
			return false;
		
		Image model = null;
		Pair<String, Image> singleImage;
		Pair<Pair<String, Image>, Pair<String, Image>> twoImages;
		Pair<String, Image> image1;
		Pair<String, Image> image2;
		
		switch(op) {
		case 0:
			return false;
		case 1:
			singleImage = selectSingle();
			
			images.put(singleImage.getKey(),
					Conversion.toBgr(singleImage.getValue()));
			break;
		case 2:
			singleImage = selectSingle();
			
			images.put(singleImage.getKey(),
					Conversion.toQiy(singleImage.getValue()));
			break;
		case 3:
			singleImage = selectSingle();
			
			images.put(singleImage.getKey(),
					Gray.grayScale(singleImage.getValue()));
			break;
		case 4:
			singleImage = selectSingle();
			
			images.put(singleImage.getKey(),
					Gray.binary(singleImage.getValue()));
			break;
		case 5:
			singleImage = selectSingle();
			
			images.put(singleImage.getKey(),
					Histogram.equalizeColor(singleImage.getValue()));
			break;
		case 6:
			singleImage = selectSingle();
			
			images.put(singleImage.getKey(),
					Histogram.equalizeGray(singleImage.getValue()));
			break;
		case 66:
			singleImage = selectSingle();
			
			System.out.println("Contrast value of " + singleImage.getKey() +
					" is: " + Histogram.getConstrast(singleImage.getValue()) +
					"\n");
			break;
		case 7:
			singleImage = selectSingle();
			
			images.put(singleImage.getKey(),
					Inverter.invertColors(singleImage.getValue()));
			break;
		case 8:
			twoImages = selectTwo();
			image1 = twoImages.getKey();
			image2 = twoImages.getValue();
			
			saveImageToDatabase(Logical.logicalAnd(image1.getValue(),
					image2.getValue()));
			break;
		case 9:
			twoImages = selectTwo();
			image1 = twoImages.getKey();
			image2 = twoImages.getValue();
			
			saveImageToDatabase(Logical.logicalOr(image1.getValue(),
					image2.getValue()));
			break;
		case 10:
			singleImage = selectSingle();
			
			images.put(singleImage.getKey(),
					Arithmetical.complement(singleImage.getValue()));
			break;
		case 11:
			twoImages = selectTwo();
			image1 = twoImages.getKey();
			image2 = twoImages.getValue();
			
			saveImageToDatabase(Arithmetical.difference(image1.getValue(),
					image2.getValue()));
			break;
		case 12:
			singleImage = selectSingle();
			model = selectModel();
			
			images.put(singleImage.getKey(),
					Morphology.closing(singleImage.getValue(), model));
			break;
		case 13:
			singleImage = selectSingle();
			model = selectModel();
			
			images.put(singleImage.getKey(),
					Morphology.dilation(singleImage.getValue(), model));
			break;
		case 14:
			singleImage = selectSingle();
			model = selectModel();
			
			images.put(singleImage.getKey(),
					Morphology.erosion(singleImage.getValue(), model));
			break;
		case 15:
			singleImage = selectSingle();
			model = selectModel();
			
			images.put(singleImage.getKey(),
					Morphology.opening(singleImage.getValue(), model));
			break;
		case 16:
			singleImage = selectSingle();
			model = selectModel();
			
			images.put(singleImage.getKey(),
					Morphology.skeletonize(singleImage.getValue(), model));
			break;
		case 17:
			saveImageToDatabase(selectFloodFillOption());
			break;
		case 18:
			singleImage = selectSingle();
			
			System.out.print("Choose an angle to rotate the image: ");
			double angle = in.nextDouble();
			in.nextLine();
			
			images.put(singleImage.getKey(),
					Transform.rotate(singleImage.getValue(), angle));
			break;
			
		case 19:
			singleImage = selectSingle();
			
			System.out.print("Choose an \"X\" value to translate the image: ");
			double x = in.nextDouble();
			in.nextLine();
			System.out.print("Choose a \"Y\" value to translate the image: ");
			double y = in.nextDouble();
			in.nextLine();
			
			images.put(singleImage.getKey(),
					Transform.translate(singleImage.getValue(), x, y));
			break;
		case 20:
			singleImage = selectSingle();
			BorderDetectMethod method = selectBorderDetectMethod();
			
			System.out.print("Choose a threshold for the method: ");
			int threshold = in.nextInt();
			in.nextLine();
			
			saveImageToDatabase(Convolution.detectBorder(
					singleImage.getValue(), threshold, method));
			break;
		case 21:
			singleImage = selectSingle();
			
			saveImageToDatabase(Convolution.mean(singleImage.getValue()));
			break;
		case 22:
			singleImage = selectSingle();
			
			saveImageToDatabase(Convolution.median(singleImage.getValue()));
			break;
		}
		
		return true;
	}
	
	private static BorderDetectMethod selectBorderDetectMethod() {
		byte method;
		
		while(true) {
			System.out.println("\nSelect a border detection method: ");
			System.out.println("1 - Prewitt.");
			System.out.println("2 - Sobel.");
			System.out.println("3 - Kirsch.");
			
			System.out.print("\nOption: ");
			method = in.nextByte();
			switch(method) {
			case 1:
				return BorderDetectMethod.PREWITT;
			case 2:
				return BorderDetectMethod.SOBEL;
			case 3:
				return BorderDetectMethod.KIRSCH;
			default:
				System.out.println("Invalid method.");
				break;
			}
		}
	}
	
	private static Image selectFloodFillOption() {
		byte op;
		Pair<String, Image> singleImage;
		
		while(true) {
			System.out.println("\nSelect a flood fill option: ");
			System.out.println("1 - Fill by location.");
			System.out.println("2 - Fill from center.");
			System.out.println("3 - Fill by color.");
			
			System.out.print("\nOption: ");
			op = in.nextByte();
			in.nextLine();
			switch(op) {
			case 1:
				singleImage = selectSingle();

				return floodFillPointCaller(singleImage.getValue());
			case 2:
				singleImage = selectSingle();

				return floodFillCenterCaller(singleImage.getValue());
			case 3:
				singleImage = selectSingle();

				return floodFillColorCaller(singleImage.getValue());
			default:
				System.out.println("Invalid option.");
				break;
			}
		}
	}
	
	private static void showImage() {
		Image image;
		String imageName;
		
		do {
			System.out.print("Select an image: ");
			imageName = in.hasNextLine() ? in.nextLine() : null;
			if (imageName == null || imageName.equals(""))
				return;
			
			image = images.get(imageName);
			if (image == null)
				System.out.println("[*] Image \"" + imageName + "\" not found in the database.");
		} while(image == null || imageName.equals(""));
		
		HighGui.imshow("Image: " + imageName, image.acquireMat());
		HighGui.waitKey(0);
	}
	
	private static Pair<String, Image> selectSingle() {
		Image image = null;
		String imageName = null;
		
		do {
			System.out.print("Select an image: ");
			imageName = in.hasNextLine() ? in.nextLine() : null;
			if (imageName == null || imageName.equals(""))
				return null;
			
			image = images.get(imageName);
			if (image == null)
				System.out.println("[*] Image \"" + imageName + "\" not found in the database.");
		} while(image == null || imageName.equals(""));
		
		return new Pair<String, Image>(imageName, image);
	}
	
	private static Pair<Pair<String, Image>, Pair<String, Image>>selectTwo() {		
		Image image1 = null;
		String imageName1 = null;
		Image image2 = null;
		String imageName2 = null;
		
		do {
			System.out.print("Select an image: ");
			imageName1 = in.hasNextLine() ? in.nextLine() : null;
			if (imageName1 == null || imageName1.equals(""))
				return null;
			
			image1 = images.get(imageName1);
			if (image1 == null)
				System.out.println("[*] Image \"" + imageName1 + "\" not found in the database.");
		} while(image1 == null || imageName1.equals(""));
		
		do {
			System.out.print("Select an image: ");
			imageName2 = in.hasNextLine() ? in.nextLine() : null;
			if (imageName2 == null || imageName2.equals(""))
				return null;
			
			image2 = images.get(imageName2);
			if (image2 == null)
				System.out.println("[*] Image \"" + imageName2 + "\" not found in the database.");
		} while(image2 == null || imageName2.equals(""));
		
		return new Pair<Pair<String, Image>, Pair<String, Image>>(
				new Pair<String, Image>(imageName1, image1), 
				new Pair<String, Image>(imageName2, image2));
	}
	
	private static Image selectModel() {
//		byte op;
//		
//		System.out.println("Select a structuring element:\n");
//		
//		System.out.println("1)");
//		strElem.showStructuringElement(ElementType.);
//		System.out.println("2)");
//		strElem.showStructuringElement(ElementType.CROSS);
//		System.out.println("3)");
//		strElem.showStructuringElement(ElementType.SQUARE);
//		
//		System.out.print("Option: ");
//		op = in.nextByte();
//		in.nextLine();
//		switch(op) {
//		case 1: return strElem.getLine();
//		case 2: return strElem.getCross();
//		case 3: return strElem.getSquare();
//		default: return strElem.getCross();
//		}
		
		return null;
	}
	
	private static Image floodFillPointCaller(Image image) {
		double[] repColor = new double[3];
		int x;
		int y;
		
		System.out.println("\nPick a color value in RGB to be the replacement color.");
		System.out.print("R: ");
		repColor[2] = in.nextInt();
		System.out.print("G: ");
		repColor[1] = in.nextInt();
		System.out.print("B: ");
		repColor[0] = in.nextInt();
		
		while(true) {
			System.out.println("\nPick a point (x, y) in the image to fill up.");
			System.out.print("X: ");
			x = in.nextInt();
			System.out.print("Y: ");
			y = in.nextInt();
		
			if (x >= image.getRows() || x < 0 ||
					y >= image.getCols() || y < 0)
				System.out.println("Point (" + x + ", " + y + ")"
						+ "out of image bounds.");
			else
				break;
		}
		
		System.out.print("\nPick an offset value (integer) for the matched color: ");
		int offset = in.nextInt();
		in.nextLine();
		
		return Morphology.floodFillPoint(image, repColor, x, y, offset);
	}
	
	private static Image floodFillCenterCaller(Image image) {
		double[] repColor = new double[3];
		
		System.out.println("\nPick a color value in RGB to be the replacement color.");
		System.out.print("R: ");
		repColor[2] = in.nextInt();
		System.out.print("G: ");
		repColor[1] = in.nextInt();
		System.out.print("B: ");
		repColor[0] = in.nextInt();
		
		System.out.print("\nPick an offset value (integer) for the matched color: ");
		int offset = in.nextInt();
		in.nextLine();
		
		return Morphology.floodFillCenter(image, repColor, offset);
	}
	
	private static Image floodFillColorCaller(Image image) {
		double[] targetColor = new double[3];
		double[] repColor = new double[3];
		
		System.out.println("\nPick a color value in RGB to be the target color.");
		System.out.print("R: ");
		targetColor[2] = in.nextInt();
		System.out.print("G: ");
		targetColor[1] = in.nextInt();
		System.out.print("B: ");
		targetColor[0] = in.nextInt();
		
		System.out.println("\nPick a color value in RGB to be the replacement color.");
		System.out.print("R: ");
		repColor[2] = in.nextInt();
		System.out.print("G: ");
		repColor[1] = in.nextInt();
		System.out.print("B: ");
		repColor[0] = in.nextInt();
		
		System.out.print("\nPick an offset value (integer) for the matched color: ");
		int offset = in.nextInt();
		in.nextLine();
		
		return Morphology.floodFillColor(image, targetColor, repColor, offset);
	}
	
	private static void saveImageToDatabase(Image image) {
		boolean found = true;
		
		System.out.println("\nA new image was created!");
		do {
			System.out.print("Choose a name for it: ");
			
			String name = in.nextLine();
			if (!name.equals("")) {
				if (!images.containsKey(name)) {
					images.put(name, image);
					found = false;
				} else {
					System.out.println("The database already contains an image called " + name);
					System.out.println("please, choose another name.\n");
				}
			} else
				System.out.println("Invalid name.\n");
		} while(found);
	}
	
	private static void prepareSave() {
		Pair<String, Image> image = selectSingle();
		String path;
		File pathCheck;
		String name;
		String extension;
		
		do {
			System.out.print("\nType the path where to save: ");
			path = in.nextLine();
			pathCheck = new File(path);
			
			if (!pathCheck.exists())
				System.out.println("The path does not exists.\n");
			else if (!pathCheck.isDirectory())
				System.out.println("The path is not a directory.\n");
		} while(!pathCheck.exists() || !pathCheck.isDirectory());
		
		System.out.print("Save image as \"" + image.getKey() + "\"? (Y/n) ");
		switch(in.nextLine()) {
		case "Y": case "y":
			System.out.print("Type an extension: ");
			extension = in.nextLine();
			name = image.getKey() + "." + extension;
			break;
		case "N": case "n":
			System.out.print("Save as: ");
			name = in.nextLine();
			break;
		default:
			name = image.getKey();
		}

		saveImage(path, name, image.getValue());
		System.out.println("Image saved!\n");
	}
	
	private static Image openImage(String path, String name) {
		String fullPath = path + "/" + name;
		Mat matrix = Imgcodecs.imread(fullPath);
	
		return !matrix.empty() ? new Image(matrix, Type.BGR) : null;
	}
	
	public static void saveImage(String path, String name, Image image) {
		String fullPath = path + "/" + name;
		
		Imgcodecs.imwrite(fullPath, image.acquireMat());
	}
}
