package view;

import java.io.File;

import javax.swing.filechooser.FileFilter;

public class ImageFileFilter extends FileFilter {

	@Override
	public boolean accept(File f) {
	    if (f.isDirectory()) {
	        return true;
	    }
	    
	    String extension = Utils.getExtension(f);
	    if (extension != null) {
	        if (extension.equals(Utils.jpeg) ||
	            extension.equals(Utils.jpg) ||
	            extension.equals(Utils.jpe) ||
	            extension.equals(Utils.jp2) ||
	            extension.equals(Utils.bmp) ||
	            extension.equals(Utils.pbm) ||
	            extension.equals(Utils.pgm) ||
	            extension.equals(Utils.ppm) ||
	            extension.equals(Utils.sr) ||
	            extension.equals(Utils.ras) ||
	            extension.equals(Utils.tiff) ||
	            extension.equals(Utils.tif) ||
	            extension.equals(Utils.png)) {
	                return true;
	        } else {
	            return false;
	        }
	    }

	    return false;
	}

	@Override
	public String getDescription() {
		// TODO Auto-generated method stub
		return "Image types supported by OpenCv";
	}

}
