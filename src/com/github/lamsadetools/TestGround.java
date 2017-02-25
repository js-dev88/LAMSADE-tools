package com.github.lamsadetools;

import java.io.IOException;
import org.apache.pdfbox.examples.pdmodel.PDFManipulator;

public class TestGround {

	public static void main(String[] args) throws IOException {
		
		PDFManipulator pm = new PDFManipulator();
		pm.createBlankPDF("test");
		pm.addImageToPdf("test.pdf", "img/logo-dauphine.jpg", "test2.pdf");
	
        
	}

}
