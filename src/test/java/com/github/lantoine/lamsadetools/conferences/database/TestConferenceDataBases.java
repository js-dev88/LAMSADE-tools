package com.github.lantoine.lamsadetools.conferences.database;

import static org.junit.Assert.*;

import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

import org.junit.Test;

import com.github.lantoine.lamsadetools.conferences.Conference;

public class TestConferenceDataBases {

	@Test
	public void createTable() {
		
		ConferenceDatabase.createTable();
		
	}
	@Test
	public void clearDataBase() throws SQLException {
		
		ConferenceDatabase.clearDataBase();
		
	}
	
	@Test
	public void insertInDatabase() throws SQLException {
		
		String dateFormat = "dd/MM/yyyy";
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern(dateFormat);
		dtf.withLocale(Locale.FRANCE);

		Conference conf = new Conference("Antoine s conf", "url", LocalDate.parse("10/03/2017", dtf),
				LocalDate.parse("11/03/2017", dtf), 0, "city", "address");
		ConferenceDatabase.insertInDatabase(conf);
		
	}
	
	@Test
	public void getAllConferencesFromDatabase() throws SQLException {
	
		
		ConferenceDatabase.getAllConferencesFromDatabase();
	}
	
	
	@Test
	public void getConferencesFromDatabase() throws SQLException {
	
		
		ConferenceDatabase.getConferencesFromDatabase(, );
	
}
