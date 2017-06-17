package com.github.lantoine.lamsadetools.missionOrder;

import java.io.InputStream;
import java.net.URL;
import java.nio.file.FileSystems;
import java.sql.SQLException;

import org.odftoolkit.simple.SpreadsheetDocument;
import org.odftoolkit.simple.table.Cell;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.lantoine.lamsadetools.conferences.Conference;
import com.github.lantoine.lamsadetools.keyring.EmailPassword;
import com.github.lantoine.lamsadetools.keyring.EmailPasswordDatabase;
import com.github.lantoine.lamsadetools.setCoordinates.UserDetails;
import com.github.lantoine.lamsadetools.utils.Util;
import com.github.lantoine.lamsadetools.yearbookInfos.GetInfosFromYearbook;
import com.github.windpapi4j.InitializationFailedException;
import com.github.windpapi4j.WinAPICallFailedException;
import com.sun.star.lang.IllegalArgumentException;
import com.sun.star.uno.Exception;

import ch.qos.logback.classic.Level;

public class generateMissionOrder {
	@SuppressWarnings("unused")
	private static final Logger LOGGER = LoggerFactory.getLogger(generateMissionOrder.class);
    
	private String target;

	public String getTarget() {
		return target;
	}
	public void setTarget(String title) {
		URL path = generateMissionOrder.class.getResource("ordre_de_mission.ods");
		this.target = path.toString() + "/" + title;
	}

	public generateMissionOrder() {

	}

	/**
	 * @param userDetails
	 * @param conference
	 *            Use the userDetails and conference to fill the Spreadsheet
	 * @throws java.lang.Exception 
	 */
	public void generateSpreadsheetDocument(UserDetails userDetails, Conference conference) throws java.lang.Exception {

		try (InputStream inputStream = generateMissionOrder.class.getResourceAsStream("ordre_de_mission.ods");
				SpreadsheetDocument spreadsheetDoc = SpreadsheetDocument.loadDocument(inputStream)) {
			URL path = generateMissionOrder.class.getResource("ordre_de_mission.ods");
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

			// write the field mission about the date of mission

			Cell dateDeparture = spreadsheetDoc.getSheetByName("Feuil1").getCellByPosition("M25");
			dateDeparture.setStringValue(conference.getStart_date().toString());

			Cell dateArrival = spreadsheetDoc.getSheetByName("Feuil1").getCellByPosition("Z25");
			dateArrival.setStringValue(conference.getEnd_date().toString());

			spreadsheetDoc.save(path.toString() + "/" + generateTitle(conference));
		}
	}

	public String generateTitle(Conference conference) {
		String title;
		title = "OM_" + conference.getCity() + "_" + conference.getStart_date().toString();
		return title;

	}

}
