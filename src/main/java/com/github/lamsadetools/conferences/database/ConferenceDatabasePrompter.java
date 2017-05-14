package com.github.lamsadetools.conferences.database;

import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.Scanner;

import com.github.lamsadetools.conferences.Conference;
import com.github.lamsadetools.conferences.ConferencePrompter;
import com.github.lamsadetools.conferences.IO;

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
		System.out.println("New address: ");
		String address = sc.nextLine();
		Conference conf = new Conference(id, title, url, start_date, end_date, entry_fee, city, address);
		if (ConferenceDatabase.editConferenceInDatabase(conf)) {
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

		while (in.isEmpty() || (in.length() < 3)) {
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
			System.out.println("5. Search by address");
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
				ConferenceDatabase.getAllConferencesFromDatabase(getValidSearchQuery(), "title");
				break;
			case 2:

				ConferenceDatabase.getAllConferencesFromDatabase(getValidSearchQuery(), "url");
				break;
			case 3:
				ConferenceDatabase.getAllConferencesFromDatabase(getValidSearchQuery(), "startdate");
				break;
			case 4:
				ConferenceDatabase.getAllConferencesFromDatabase(getValidSearchQuery(), "city");
				break;
			case 5:
				ConferenceDatabase.getAllConferencesFromDatabase(getValidSearchQuery(), "address");
				break;
			default:
			}
		}
	}
}
