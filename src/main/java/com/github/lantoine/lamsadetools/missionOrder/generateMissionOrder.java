package com.github.lantoine.lamsadetools.missionOrder;

import java.io.InputStream;

import org.odftoolkit.simple.SpreadsheetDocument;
import org.odftoolkit.simple.table.Cell;

public class generateMissionOrder {

	public static void main(String[] args) throws Exception {
		new generateMissionOrder().generateSpreadsheetDocument();
	}

	public void generateSpreadsheetDocument() throws Exception {
		try (InputStream inputStream = generateMissionOrder.class.getResourceAsStream("ordre_de_mission.ods");
				SpreadsheetDocument spreadsheetDoc = SpreadsheetDocument.loadDocument(inputStream)) {
			Cell positionCell = spreadsheetDoc.getTableByName("B").getCellByPosition("E1");
			System.out.println(positionCell.getDisplayText());
			positionCell.setStringValue("ploum");
			spreadsheetDoc.save("ordre_de_mission.ods");
		}
	}

}
