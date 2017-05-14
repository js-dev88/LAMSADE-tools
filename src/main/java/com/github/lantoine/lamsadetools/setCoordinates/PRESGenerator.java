package com.github.lantoine.lamsadetools.setCoordinates;

import java.io.File;
import java.io.IOException;
import java.util.*;
import org.odftoolkit.odfdom.doc.OdfDocument;
import org.odftoolkit.odfdom.doc.OdfTextDocument;
import org.odftoolkit.odfdom.incubator.search.TextNavigation;
import org.odftoolkit.simple.TextDocument;
import org.odftoolkit.simple.common.navigation.TextSelection;
import org.w3c.dom.*;

public class PRESGenerator

{
	/**
	 * Create a PRES document
	 *
	 * @param source
	 * @param target
	 * @throws Exception
	 */
	public static void main(String[] args) throws Exception {
		OdfTextDocument textdoc = OdfTextDocument.loadDocument("com/github/lamsadetools/setCoordinates/Premier_test_template.odt");

		/* Creation of search for every word needed to be replaced */
		TextNavigation searchN;
		TextNavigation searchP;
		TextNavigation searchG;
		TextNavigation searchE;
		TextNavigation searchT;

		searchN = new TextNavigation("NOMR", textdoc);
		searchP = new TextNavigation("PRENOMR", textdoc);
		searchG = new TextNavigation("GRADER", textdoc);
		searchE = new TextNavigation("TEMPSPARTIELLER", textdoc);
		searchT = new TextNavigation("TEMPSPLEINR", textdoc);

		/* Verify all the word and replace them by the value */
		while (searchN.hasNext()) {

			TextSelection item = (TextSelection) searchN.nextSelection();

			item.replaceWith("Test");

		}
		while (searchP.hasNext()) {

			TextSelection item = (TextSelection) searchP.nextSelection();

			item.replaceWith("Test");

		}
		while (searchG.hasNext()) {

			TextSelection item = (TextSelection) searchG.nextSelection();

			item.replaceWith("Test");

		}
		while (searchE.hasNext()) {

			TextSelection item = (TextSelection) searchE.nextSelection();

			item.replaceWith("Test");

		}
		while (searchT.hasNext()) {

			TextSelection item = (TextSelection) searchT.nextSelection();

			item.replaceWith("Test");

		}

	}

}
