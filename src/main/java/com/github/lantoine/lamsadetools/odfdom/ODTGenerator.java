package com.github.lantoine.lamsadetools.odfdom;

import org.odftoolkit.simple.TextDocument;
import org.odftoolkit.simple.table.Cell;
import org.odftoolkit.simple.table.Table;
import org.odftoolkit.simple.text.list.List;

public class ODTGenerator {

	TextDocument outputOdt;

	public ODTGenerator() {
		try {
			outputOdt = TextDocument.newTextDocument();

			// add image
			// outputOdt.newImage(new URI("odf-logo.png"));

			// add paragraph
			outputOdt.addParagraph("Hello World, Hello Simple ODF!");

			// add list
			outputOdt.addParagraph("The following is a list.");
			List list = outputOdt.addList();
			String[] items = { "item1", "item2", "item3" };
			list.addItems(items);

			// add table
			Table table = outputOdt.addTable(2, 2);
			Cell cell = table.getCellByPosition(0, 0);
			cell.setStringValue("Hello World!");

			outputOdt.save("HelloWorld.odt");
		} catch (Exception e) {
			System.err.println("ERROR: unable to create output file.");
		}
	}
}