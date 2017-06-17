package com.github.lantoine.lamsadetools.missionOrder;

import java.io.InputStream;
import java.nio.file.FileSystems;

import org.odftoolkit.simple.SpreadsheetDocument;
import org.odftoolkit.simple.table.Cell;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.lantoine.lamsadetools.conferences.Conference;
import com.github.lantoine.lamsadetools.setCoordinates.UserDetails;
import com.github.lantoine.lamsadetools.yearbookInfos.GetInfosFromYearbook;
import com.sun.star.lang.IllegalArgumentException;

import ch.qos.logback.classic.Level;

public class generateMissionOrder {
	@SuppressWarnings("unused")
	private static final Logger LOGGER = LoggerFactory.getLogger(generateMissionOrder.class);

	private static String target = FileSystems.getDefault().getPath("").toAbsolutePath() + "/ordre_de_mission_test.ods";

	public String getTarget() {
		return target;
	}

	/**
	 * Mission Order Constructor
	 * 
	 * @param firstname
	 *            may not be null
	 * @param lastname
	 *            may not be null
	 * @throws IllegalArgumentException
	 */
	public generateMissionOrder() throws IllegalArgumentException {

	}

	
	/**
	 * @param userDetails 
	 * @param conference
	 * Use the userDetails and conference to fill the Spreadsheet
	 * @throws Exception
	 */
	public void generateSpreadsheetDocument(UserDetails userDetails, Conference conference) throws Exception {

		try (InputStream inputStream = generateMissionOrder.class.getResourceAsStream("ordre_de_mission.ods");
				SpreadsheetDocument spreadsheetDoc = SpreadsheetDocument.loadDocument(inputStream)) {
			// Name
			Cell nameCell = spreadsheetDoc.getSheetByName("Feuil1").getCellByPosition("F8");
			nameCell.setStringValue(userDetails.getName());

			// Firstname
			Cell firstNameCell = spreadsheetDoc.getSheetByName("Feuil1").getCellByPosition("Y8");
			firstNameCell.setStringValue(userDetails.getFirstName());

			// mail
			Cell mailCell = spreadsheetDoc.getSheetByName("Feuil1").getCellByPosition("F11");
			mailCell.setStringValue(userDetails.getEmail());

			// Departure Data - One Way trip
			Cell cityCountryDepartureCellOneWayTrip = spreadsheetDoc.getSheetByName("Feuil1").getCellByPosition("B31");
			cityCountryDepartureCellOneWayTrip.setStringValue(userDetails.getCity() + " ," + userDetails.getCountry());

			Cell cityCountryArrivalCellOneWayTrip = spreadsheetDoc.getSheetByName("Feuil1").getCellByPosition("T31");
			cityCountryArrivalCellOneWayTrip.setStringValue(conference.getCity() + " ," + conference.getCountry());

			// Arrival Data - Return trip
			Cell cityCountryDepartureCellReturnTrip = spreadsheetDoc.getSheetByName("Feuil1").getCellByPosition("B37");
			cityCountryDepartureCellReturnTrip.setStringValue(conference.getCity() + " ," + conference.getCountry());

			Cell cityCountryArrivalReturnTrip = spreadsheetDoc.getSheetByName("Feuil1").getCellByPosition("T37");
			cityCountryArrivalReturnTrip.setStringValue(userDetails.getCity() + " ," + userDetails.getCountry());
			
			// Date trip
			
			// if the departure date is equals of the arrival date then we must write the fields about the date trip 
			if (conference.getStart_date().toString().equals(conference.getEnd_date().toString())) {
				Cell dateDeparture = spreadsheetDoc.getSheetByName("Feuil1").getCellByPosition("M22");
				dateDeparture.setStringValue(conference.getStart_date().toString());

				Cell dateArrival = spreadsheetDoc.getSheetByName("Feuil1").getCellByPosition("Z22");
				dateArrival.setStringValue(conference.getEnd_date().toString());

			} else { // else the departure date is different of the arrival we must write the field mission about the date of mission

				Cell dateDeparture = spreadsheetDoc.getSheetByName("Feuil1").getCellByPosition("M25");
				dateDeparture.setStringValue(conference.getStart_date().toString());

				Cell dateArrival = spreadsheetDoc.getSheetByName("Feuil1").getCellByPosition("Z25");
				dateArrival.setStringValue(conference.getEnd_date().toString());

			}

			spreadsheetDoc.save("ordre_de_mission_test.ods");
			inputStream.close();
			//spreadsheetDoc.close();

		

		}
	}

}
