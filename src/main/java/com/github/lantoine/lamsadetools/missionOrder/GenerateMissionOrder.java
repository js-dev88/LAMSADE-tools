package com.github.lantoine.lamsadetools.missionOrder;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.nio.file.FileSystems;

import org.apache.commons.io.FileUtils;
import org.odftoolkit.simple.SpreadsheetDocument;
import org.odftoolkit.simple.table.Cell;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.lantoine.lamsadetools.conferences.Conference;
import com.github.lantoine.lamsadetools.setCoordinates.UserDetails;
import com.github.lantoine.lamsadetools.yearbookInfos.GetInfosFromYearbook;
import com.sun.star.lang.IllegalArgumentException;

import ch.qos.logback.classic.Level;

/**
 * This class fills a searcher Mission Order
 * It can only be accessed in a static way
 *
 */
public class GenerateMissionOrder {
	@SuppressWarnings("unused")
	private static final Logger LOGGER = LoggerFactory.getLogger(GenerateMissionOrder.class);

	/**
	 * "target" attribute is the default saving path
	 * 
	 */
	private static String target = FileSystems.getDefault().getPath("").toAbsolutePath() + "/ordre_de_mission_test.ods";

	public static String getTarget() {
		return target;
	}

	/**
	 * @param userDetails
	 * @param conference
	 *            Use the userDetails and conference to fill the Spreadsheet
	 * @throws Exception
	 */
	public static void generateSpreadsheetDocument(UserDetails userDetails, Conference conference, String fileDestination) throws Exception {
		if (!fileDestination.isEmpty())
			target = fileDestination;
		try (InputStream inputStream = GenerateMissionOrder.class.getResourceAsStream("ordre_de_mission.ods");
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

			// write the field mission about the date of mission

			Cell dateDeparture = spreadsheetDoc.getSheetByName("Feuil1").getCellByPosition("M25");
			dateDeparture.setStringValue(conference.getStart_date().toString());

			Cell dateArrival = spreadsheetDoc.getSheetByName("Feuil1").getCellByPosition("Z25");
			dateArrival.setStringValue(conference.getEnd_date().toString());

			spreadsheetDoc.save(target);

			saveOrderMissionToHistory(target, conference.getCity(), 
					conference.getCountry(), conference.getStart_date().toString());
			File filesource = new File(target);	
			String filename = new String("historique_OM/OM_"+ conference.getCity() + "-" + conference.getCountry() + 
					"_" + conference.getStart_date().toString() +".ods");
			File targetfile = new File(filename);
			FileUtils.copyFile(filesource, targetfile);

		}
	}

	private static void saveOrderMissionToHistory(String fileToCopy, String city, String country, String startDate)
			throws IOException {
		File filesource = new File(fileToCopy);
		String filename = new String("historique_OM/OM_"+ city + "-" + country + 
				"_" + startDate +".ods");
		File targetfile = new File(filename);
		FileUtils.copyFile(filesource, targetfile);
	}

}
