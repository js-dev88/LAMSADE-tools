package com.github.lantoine.lamsadetools.missionOrder;

import java.io.File;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.io.FileUtils;
import org.odftoolkit.simple.SpreadsheetDocument;
import org.odftoolkit.simple.table.Cell;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.lantoine.lamsadetools.setCoordinates.UserDetails;
import com.github.lantoine.lamsadetools.yearbookInfos.GetInfosFromYearbook;
import com.sun.star.lang.IllegalArgumentException;

import ch.qos.logback.classic.Level;

public class generateMissionOrder {
	@SuppressWarnings("unused")
	private static final Logger LOGGER = LoggerFactory.getLogger(generateMissionOrder.class);
	
	private String firstname;
	private String lastname;


	public static void main(String[] args) throws Exception {

		new generateMissionOrder("olivier","Cailloux").generateSpreadsheetDocument();
	}
	/**
	 * Mission Order Constructor
	 * @param firstname may not be null
	 * @param lastname may not be null
	 * @throws IllegalArgumentException 
	 * */
	public generateMissionOrder(String firstname, String lastname) throws IllegalArgumentException{
		if (firstname == null || lastname == null) {
			throw new IllegalArgumentException("Firstname or lastname is null");
		}
		this.firstname = firstname;
		this.lastname = lastname;

	}
	
	/**
	 * User Instantiation and fill the Spreadsheet
	 * @throws Exception when something went wrong with the generation
	 * */
	public void generateSpreadsheetDocument() throws Exception {
		UserDetails user = GetInfosFromYearbook.getUserDetails(lastname,firstname);
		
		try (InputStream inputStream = generateMissionOrder.class.getResourceAsStream("ordre_de_mission.ods");
				SpreadsheetDocument spreadsheetDoc = SpreadsheetDocument.loadDocument(inputStream)) {
            //Name
			Cell nameCell = spreadsheetDoc.getSheetByName("Feuil1").getCellByPosition("F8");
			nameCell.setStringValue(user.getName());
			
			//Firstname
			Cell firstNameCell = spreadsheetDoc.getSheetByName("Feuil1").getCellByPosition("Y8");
			firstNameCell.setStringValue(user.getFirstName());
			
			//mail
			Cell mailCell = spreadsheetDoc.getSheetByName("Feuil1").getCellByPosition("F11");
			mailCell.setStringValue(user.getEmail());
			
			//Departure Data - One Way trip
			Cell cityCountryDepartureCell = spreadsheetDoc.getSheetByName("Feuil1").getCellByPosition("B31");
			cityCountryDepartureCell.setStringValue(user.getCity()+" ,"+user.getCountry());
			
			//Arrival Data - Return trip
			Cell cityCountryArrivalCell = spreadsheetDoc.getSheetByName("Feuil1").getCellByPosition("T37");
			cityCountryArrivalCell.setStringValue(user.getCity()+" ,"+user.getCountry());
			
			spreadsheetDoc.save("ordre_de_mission_test.ods");
			inputStream.close();
			
			File filesource = new File("ordre_de_mission_test.ods");
			
			String filename = new SimpleDateFormat("'historique_OM/OM' yyyyMMddHHmm'.ods'").format(new Date());
			File targetfile = new File(filename);
			
			FileUtils.copyFile(filesource,targetfile);	
			// spreadsheetDoc.close();

		}

	}

}
