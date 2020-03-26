/*
 * Copyright (c) 1995, 2008, Oracle and/or its affiliates. All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 *   - Redistributions of source code must retain the above copyright
 *     notice, this list of conditions and the following disclaimer.
 *
 *   - Redistributions in binary form must reproduce the above copyright
 *     notice, this list of conditions and the following disclaimer in the
 *     documentation and/or other materials provided with the distribution.
 *
 *   - Neither the name of Oracle or the names of its
 *     contributors may be used to endorse or promote products derived
 *     from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS
 * IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO,
 * THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR
 * PURPOSE ARE DISCLAIMED.  IN NO EVENT SHALL THE COPYRIGHT OWNER OR
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
 * EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR
 * PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
 * LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */ 

package view;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.io.File;
import java.net.URL;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import dataStructures.Pair;

/* Utils.java is used by FileChooserDemo2.java. */
public class Utils {
    public static final String jpeg = "jpeg";
    public static final String jpg = "jpg";
    public static final String jpe = "jpe";
    public static final String jp2 = "jp2";
    public static final String bmp = "bmp";
    public static final String pbm = "pbm";
    public static final String pgm = "pgm";
    public static final String ppm = "ppm";
    public static final String sr = "sr";
    public static final String ras = "ras";
    public static final String tiff = "tiff";
    public static final String tif = "tif";
    public static final String png = "png";

    /*
     * Get the extension of a file.
     */
    public static String getExtension(File f) {
        String ext = null;
        String s = f.getName();
        int i = s.lastIndexOf('.');

        if (i > 0 &&  i < s.length() - 1) {
            ext = s.substring(i+1).toLowerCase();
        }
        return ext;
    }
    
    public static Pair<Integer, Integer> twoFieldsOptionPane() {
		JTextField widthField = new JTextField(5);
	    JTextField heightField = new JTextField(5);
	    GridBagConstraints gbc = new GridBagConstraints();
	
	    JPanel panel = new JPanel();
	    panel.setLayout(new GridBagLayout());
	    
	    gbc.gridx = 0;
	    gbc.gridy = 0;
	    panel.add(new JLabel("Width: "), gbc);
	    
	    gbc.gridx = 1;
	    gbc.gridy = 0;
	    panel.add(widthField, gbc);
	    
	    gbc.gridx = 0;
	    gbc.gridy = 1;
	    panel.add(new JLabel("Height: "), gbc);
	    
	    gbc.gridx = 1;
	    gbc.gridy = 1;
	    panel.add(heightField, gbc);
	
	    int result = JOptionPane.showConfirmDialog(null, panel, 
	             "Image dimensions", JOptionPane.OK_CANCEL_OPTION);
	    
	    if (result == JOptionPane.OK_OPTION) {
	    	String width = widthField.getText();
	    	String height = heightField.getText();
	    	
	    	return new Pair<Integer, Integer>(
	    			!width.isEmpty() ? Integer.parseInt(width) : 1,
	    			!height.isEmpty() ? Integer.parseInt(height) : 1);
	    }
	    
	    return null;
	}
}