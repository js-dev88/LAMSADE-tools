/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.apache.pdfbox.examples.pdmodel;

import java.io.IOException;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;

/**
 * Class that is able to create blank pdfs and add images to a pdf
 */
public final class PapierAEnTeteGenerator {
	private String filename;
	private static final String dauphineLogoImage = "img/logo-dauphine.jpg";
	private static final String cnrsLogo = "img/logo-cnrs.jpg";
	private static final String lamsadeLogo = "img/logo-lamsade.jpg";
	public static final int pdfWidth = 612;
	public static final int pdfHeight = 792;

	public String getDauphinelogoimage() {
		return dauphineLogoImage;
	}

	public String getCnrslogo() {
		return cnrsLogo;
	}

	public String getLamsadelogo() {
		return lamsadeLogo;
	}

	public String getFilename() {
		return filename;
	}

	private void setFilename(String filename) {
		this.filename = filename;
	}

	/**
	 * Creates a blank pdf
	 *
	 * @param fileName
	 *            <pdf-file-name> without ".pdf"
	 * @throws IOException
	 */

	public PapierAEnTeteGenerator(String filename) throws IOException {
		setFilename(filename);
		createBlankPDF(this.filename);

		/*
		 * Insert logos Dauphine logo first
		 */

		// float scale = (float) 0.15;
		// ImageSize is = new ImageSize(dauphineLogoImage, scale);

		// addImageToPdf(getFilename(), dauphineLogoImage, "_" + getFilename(),
		// 20.0, pdfHeight - is.getHeight() - 20.0,
		// scale);

		// is = new ImageSize(cnrsLogo, scale);

		// addImageToPdf(getFilename(), cnrsLogo, "_" + getFilename(), pdfWidth
		// - is.getWidth() - 20,
		// pdfHeight - is.getHeight() - 20.0, scale);

	}

	/**
	 * Creates a blank pdf.
	 *
	 * @throws IOException
	 */

	public PapierAEnTeteGenerator() throws IOException {
		this("papier.pdf");
	}

	/**
	 * Creates a blank PDF file with its name passed by parameter
	 *
	 * @param fileName
	 *            what to call the file
	 * @throws IOException
	 */

	public void createBlankPDF(String filename) throws IOException {
		if (!filename.endsWith(".pdf")) {
			filename += ".pdf";
		}

		System.out.print(this.filename);

		final PDDocument doc = new PDDocument();
		try {
			// a valid PDF document requires at least one page
			final PDPage blankPage = new PDPage();
			doc.addPage(blankPage);
			doc.save(filename);
		} finally {
			doc.close();
		}
	}

	/**
	 * Inserts an image passed as parameter to the pdf documents in the desired
	 * coordinates. Size of PDF document = (612, 792) (px)
	 *
	 * @param inputFile
	 *            the pdf file
	 * @param imagePath
	 *            the path of the image to be inserted
	 * @param outputFile
	 *            the pdf file to be generated with the new image (can be the
	 *            same as inputFile)
	 * @param xCoord
	 *            the x coordinate of the image in the document
	 * @param yCoord
	 *            the y coordinate of the image in the document
	 * @param scale
	 *            the scale by which we will transform the image (the smallest,
	 *            the smaller the image)
	 * @throws IOException
	 */
	public void addImageToPdf(PDDocument doc, PDPageContentStream contentStream, PDImageXObject pdImage,
			String outputFile, double xCoord, double yCoord, double scale) throws IOException {
		// the document

		try {

			// contentStream.drawImage(ximage, 20, 20 );
			// better method inspired by
			// http://stackoverflow.com/a/22318681/535646
			// reduce this value if the image is too large

			// contentStream.drawImage(pdImage, pdfWidth -
			// pdImage.getWidth()*scale - margin , pdfHeight -
			// pdImage.getHeight()*scale - margin, pdImage.getWidth()*scale,
			// pdImage.getHeight()*scale);
			contentStream.drawImage(pdImage, (float) xCoord, (float) yCoord, (float) (pdImage.getWidth() * scale),
					(float) (pdImage.getHeight() * scale));
			contentStream.close();
			doc.save(outputFile);
		} finally {
			if (doc != null) {
				// doc.close();
			}
		}
	}
}