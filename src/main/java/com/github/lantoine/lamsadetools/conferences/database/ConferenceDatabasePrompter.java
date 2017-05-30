package com.github.lantoine.lamsadetools.conferences.database;

import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.Scanner;

import com.github.lantoine.lamsadetools.conferences.Conference;
import com.github.lantoine.lamsadetools.conferences.ConferencePrompter;
import com.github.lantoine.lamsadetools.conferences.IO;

public class ConferenceDatabasePrompter extends ConferencePrompter {

	/**
	 * A menu which enables the user to choose what conference he wants to
	 * delete
	 *
	 * @throws SQLException
	 */
	public static void deleteMenu() throws SQLException {

		System.out.println("Conference to delete :");
		System.out.println("ID :");

		Scanner sc = new Scanner(System.in);

		int id = sc.nextInt();

		ConferenceDatabase.removeConferenceFromDatabase(id);
		ConferenceDatabasePrompter.menu();
	}

	/**
	 * A menu which enables the user to edit a conference
	 *
	 * @throws SQLException
	 */
	public static void editConference() throws SQLException {
		Scanner sc = new Scanner(System.in);
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern(Conference.DATE_FORMAT);
		dtf.withLocale(Locale.FRANCE);
		System.out.println("conférenceID : ");
		int id = sc.nextInt();
		System.out.println("New title (optional): ");
		String title = sc.nextLine();
		System.out.println("New URL (optional): ");
		String url = sc.nextLine();
		System.out.println("New start date (" + Conference.DATE_FORMAT + ") (optional): ");
		LocalDate start_date = LocalDate.parse(sc.nextLine(), dtf);
		System.out.println("New end date (" + Conference.DATE_FORMAT + ") (optional): ");
		LocalDate end_date = LocalDate.parse(sc.nextLine(), dtf);
		System.out.println("New title (optional): ");
		double entry_fee = sc.nextDouble();
		System.out.println("New city: ");
		String city = sc.nextLine();
		System.out.println("New country: ");
		String country = sc.nextLine();
		Conference conf = new Conference(id, title, url, start_date, end_date, entry_fee, city, country);
		if (ConferenceDatabasePrompter.editConferenceInDatabase(conf)) {
			System.out.println("Edit Successful");
		} else {
			System.out.println("Edit failed");
		}
	}

	/**
	 * Asks the user what they want to search for and makes sure the input is 3
	 * characters long or more
	 *
	 * @return
	 */
	private static String getValidSearchQuery() {

		System.out.print("What do you want to search for?");
		IO io = new IO();
		io.scanner = new Scanner(System.in);
		String in = io.scanner.nextLine();

		while (in.isEmpty() || in.length() < 3) {
			System.out.print("Please type in at least 3 characters");
			in = io.scanner.nextLine();
		}

		return in;

	}

	/**
	 * display a menu which enables you to create, search, edit and delete
	 * conferences
	 *
	 * @throws SQLException
	 */
	public static void menu() throws SQLException {
		int option = -1;

		while (option != 0) {
			IO io = new IO();
			io.scanner = new Scanner(System.in);

			System.out.println("Welcome to the conference creation tool");
			System.out.println("#################### ");
			System.out.println("Please choose an option:");
			System.out.println("1. Create a new conference.");
			System.out.println("2. Search a conference.");
			System.out.println("3. View all conferences.");
			System.out.println("4. Edit a conference."); // by URL or title
			System.out.println("5. Delete a conference.");
			System.out.println("6. Delete the database.");
			System.out.println("0. Exit");

			String optionstr = io.scanner.nextLine();
			// Verify input is an integer
			try {
				option = Integer.parseInt(optionstr);
			} catch (NumberFormatException ex) {
				System.out.println("Please choose a valid option");
				option = -1;
			}

			switch (option) {
			case 1:
				ConferenceDatabase.insertInDatabase(ConferencePrompter.promptConference());
				break;
			case 2:
				ConferenceDatabasePrompter.searchMenu();
				break;
			case 3:
				ConferenceDatabase.getAllConferencesFromDatabase();
				break;
			case 4:
				ConferenceDatabasePrompter.editConference();
				break;
			case 5:
				ConferenceDatabasePrompter.deleteMenu();
				break;
			case 6:
				ConferenceDatabase.clearDataBase();
				break;
			default:
				break;
			}
		}
	}

	/**
	 * Let the user choose which
	 *
	 * @throws SQLException
	 */
	public static void searchMenu() throws SQLException {
		int option = -1;

		while (option != 0) {
			IO io = new IO();
			io.scanner = new Scanner(System.in);
			System.out.println("Please choose an option:");
			System.out.println("1. Search by name");
			System.out.println("2. Search by URL.");
			System.out.println("3. Search by start date");
			System.out.println("4. Search by city");
			System.out.println("5. Search by country");
			System.out.println("0. Exit");
			String optionstr = io.scanner.nextLine();
			// Verify input is an integer
			try {
				option = Integer.parseInt(optionstr);
			} catch (NumberFormatException ex) {
				System.out.println("Please choose a valid option");
				option = -1;
			}

			switch (option) {
			case 1:
				ConferenceDatabase.getConferencesFromDatabase("title", getValidSearchQuery());
				break;
			case 2:

				ConferenceDatabase.getConferencesFromDatabase("url", getValidSearchQuery());
				break;
			case 3:
				ConferenceDatabase.getConferencesFromDatabase("startdate", getValidSearchQuery());
				break;
			case 4:
				ConferenceDatabase.getConferencesFromDatabase("city", getValidSearchQuery());
				break;
			case 5:
				ConferenceDatabase.getConferencesFromDatabase("country", getValidSearchQuery());
				break;
			default:
			}
		}
	}

	/**
	 * Take a conference in parameter, and change the conference from the
	 * database which has the same ID to match the non null variables of the
	 * conference from parameters
	 *
	 * @return true if the edit was successful
	 * @throws SQLException
	 */
	public static boolean editConferenceInDatabase(Conference conf) throws SQLException {

		String where_statement = "WHERE conferenceID = \"" + conf.getId() + "\"";
		String set_statement = "";
		if (!conf.getTitle().isEmpty()) {
			set_statement = ConferenceDatabase.constructSetStatement(set_statement, "Title", conf.getTitle());
		}
		if (!conf.getUrl().isEmpty()) {
			set_statement = ConferenceDatabase.constructSetStatement(set_statement, "URL", conf.getUrl());
		}
		if (!conf.getStart_date().isEqual(LocalDate.parse("1970-01-01"))) {
			set_statement = ConferenceDatabase.constructSetStatement(set_statement, "start_date",
					conf.getStart_date().toString());
		}
		if (!conf.getEnd_date().isEqual(LocalDate.parse("1970-01-01"))) {
			set_statement = ConferenceDatabase.constructSetStatement(set_statement, "end_date",
					conf.getEnd_date().toString());
		}
		if (!(conf.getEntry_fee() == -1)) {
			set_statement = ConferenceDatabase.constructSetStatement(set_statement, "entry_fee",
					Double.toString(conf.getEntry_fee()));
		}
		if (!conf.getCity().isEmpty()) {
			set_statement = ConferenceDatabase.constructSetStatement(set_statement, "City", conf.getTitle());
		}

		if (!conf.getAddress().isEmpty()) {
			set_statement = ConferenceDatabase.constructSetStatement(set_statement, "Country", conf.getTitle());
		}
		set_statement = "UPDATE conferences " + set_statement + where_statement + ";";

		ConferenceDatabase.getConnectionDataBase().getConnection();

		ConferenceDatabase.createTable();

		ConferenceDatabase.getConnectionDataBase().sqlQuery(set_statement);

		ConferenceDatabase.getConnectionDataBase().closeAndDisposeConnection();

		return true;
	}

}
