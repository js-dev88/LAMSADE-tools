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

import java.io.File;
import java.io.IOException;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.PDPageContentStream.AppendMode;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;

/**
 * Class that is able to create blank pdfs and add images to a pdf
 */
public final class PDFManipulator
{
	/**
	 * Creates a blank pdf.
	 * @param fileName <pdf-file-name> without ".pdf"
	 */
	
	public PDFManipulator() {
		
	}
	
	/**
	 * Creates a blank PDF file with its name passed by parameter
	 * @param fileName what to call the file
	 * @throws IOException
	 */
	
	public void createBlankPDF(String fileName) throws IOException
	{      
		if (!fileName.endsWith(".pdf")) {
			fileName += ".pdf";
		}

		PDDocument doc = new PDDocument();
		try
		{
			// a valid PDF document requires at least one page
			PDPage blankPage = new PDPage();
			doc.addPage(blankPage);
			doc.save(fileName);
		}
		finally
		{
			doc.close();
		}
	}
	
	public void addImageToPdf( String inputFile, String imagePath, String outputFile )
            throws IOException
    {
        // the document
        PDDocument doc = null;
        try
        {
            doc = PDDocument.load( new File(inputFile) );

            //we will add the image to the first page.
            PDPage page = doc.getPage(0);

            // createFromFile is the easiest way with an image file
            // if you already have the image in a BufferedImage, 
            // call LosslessFactory.createFromImage() instead
            PDImageXObject pdImage = PDImageXObject.createFromFile(imagePath, doc);
            PDPageContentStream contentStream = new PDPageContentStream(doc, page, AppendMode.APPEND, true);

            // contentStream.drawImage(ximage, 20, 20 );
            // better method inspired by http://stackoverflow.com/a/22318681/535646
            // reduce this value if the image is too large
            float scale = 1f;
            contentStream.drawImage(pdImage, 20, 20, pdImage.getWidth()*scale, pdImage.getHeight()*scale);

            contentStream.close();
            doc.save( outputFile );
        }
        finally
        {
            if( doc != null )
            {
                doc.close();
            }
        }
    }
}