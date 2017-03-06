package com.github.odfdom;

import java.io.File;

import org.odftoolkit.simple.TextDocument;

public class ODTDocument {

	private TextDocument outputOdt;
	private String filename;

	public TextDocument getOutputOdt() {
		return outputOdt;
	}

	public void setOutputOdt(TextDocument outputOdt) {
		this.outputOdt = outputOdt;
	}

	public ODTDocument(String filename) {
		try {
			outputOdt = TextDocument.newTextDocument();

			// add image
			// outputOdt.newImage(new URI("odf-logo.png"));

			// add paragraph
			// outputOdt.addParagraph("Hello World, Hello Simple ODF!");

			// // add list
			// outputOdt.addParagraph("The following is a list.");
			// List list = outputOdt.addList();
			// String[] items = { "item1", "item2", "item3" };
			// list.addItems(items);

			// add table
			// Table table = outputOdt.addTable(2, 2);
			// Cell cell = table.getCellByPosition(0, 0);
			// cell.setStringValue("Hello World!");

			outputOdt.save(filename);
		} catch (Exception e) {
			System.err.println("ERROR: unable to create output file.");
		}
	}

	public ODTDocument() {
		this("doc.odt");
	}

	/**
	 * Takes the parameters of a researcher and puts them into a .odt file
	 *
	 * @param name
	 *            name of the researcher
	 * @param address
	 *            address of the researcher
	 * @param email
	 *            email of the researcher
	 */
	public void insertSearchParamsIntoHeader(String name, String phone, String email) {
		try {
			outputOdt = TextDocument.loadDocument(new File(filename));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		outputOdt.addParagraph(name);
		outputOdt.addParagraph(phone);
		outputOdt.addParagraph(email);

		outputOdt.close();
	}
}