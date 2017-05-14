package com.github.lamsadetools;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

import com.github.lamsadetools.conferences.Conference;
import com.github.lamsadetools.conferences.database.ConferenceDatabase;
import com.github.lamsadetools.conferences.database.ConferenceDatabasePrompter;

public class TestGround {

	public static void main(String[] args) throws Exception {

		// Conference.clearDataBase();
		String dateFormat = "dd/MM/yyyy";
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern(dateFormat);
		dtf.withLocale(Locale.FRANCE);

		Conference conf = new Conference("Antoine s conf", "url", LocalDate.parse("10/03/2017", dtf),
				LocalDate.parse("11/03/2017", dtf), 0, "city", "address");
		ConferenceDatabase.insertInDatabase(conf);

		int id = 10;
		ConferenceDatabase.removeConferenceFromDatabase(id);

		ConferenceDatabase.getAllConferencesFromDatabase();
		ConferenceDatabasePrompter.menu();

		// Conference.getConferenceFromDatabase(1);

		// PapierAEnTeteGenerator pm = new PapierAEnTeteGenerator();
		//
		// PDDocument doc = null;
		//
		// try {
		// doc = PDDocument.load(new File(pm.getFilename()));
		//
		// // Point to the first page of the document
		// final PDPage page = doc.getPage(0);
		//
		// final PDImageXObject pdImage =
		// PDImageXObject.createFromFile(pm.getDauphinelogoimage(), doc);
		// final PDPageContentStream contentStream = new
		// PDPageContentStream(doc, page, AppendMode.APPEND, true);
		//
		// // Add all images
		// double scale = 0.15;
		// ImageSize is = new ImageSize(pm.getDauphinelogoimage(), scale);
		//
		// pm.addImageToPdf(doc, contentStream, pdImage, "_" + pm.getFilename(),
		// 20.0,
		// PapierAEnTeteGenerator.pdfHeight - is.getHeight() - 20.0, scale);
		//
		// is = new ImageSize(pm.getCnrslogo(), scale);
		//
		// // addImageToPdf(pm.getFilename(), pm.getCnrslogo(), "_" +
		// // pm.getFilename(),
		// // PapierAEnTeteGenerator.pdfWidth - is.getWidth() - 20,
		// // PapierAEnTeteGenerator.pdfHeight - is.getHeight() - 20.0, scale);
		//
		// // Add all the images and text to the document before closing the
		// // stream
		// // Can a content stream be reopened ?
		//
		// /*
		// * buffer already closed
		// */
		//
		// contentStream.close();
		// doc.save("output.pdf");
		// } finally {
		// doc.close();
		// }

	}

}