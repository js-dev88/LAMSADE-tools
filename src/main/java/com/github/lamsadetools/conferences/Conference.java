package com.github.lamsadetools.conferences;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Locale;
import java.util.Scanner;

import org.apache.log4j.Logger;

/**
 * This class enable us to store some of the informations that a teacher may
 * want to store about his future conferences. it contains functions to store
 * informations in a database and a user interface to create, search, view, edit
 * and delete the conferences.
 *
 * @author lantoine
 *
 */
public class Conference {
	private static final String CREATETABLE = "CREATE TABLE IF NOT EXISTS conference (" + "conferenceID     SERIAL, "
			+ "Title            varchar(255) NOT NULL, " + "URL              varchar(255) NOT NULL, "
			+ "start_date       date NOT NULL, " + "end_date         date NOT NULL, " + "entry_fee        double, "
			+ "CONSTRAINT conferenceID PRIMARY KEY (conferenceID) ); ";
	private static final String DATE_FORMAT = "dd/MM/yyyy";

	final static Logger logger = Logger.getLogger(Conference.class);

	static final String path = "src/main/resources/com/github/lamsadetools/log4j.properties";

	private static final String SQL_DATE_FORMAT = "yyyy-MM-dd";

	public static void main(String[] args) throws Exception {
		menu();
	}

	/**
	 * Drop the table conference in order to erase all the conferences stored in
	 * the database
	 *
	 * @throws SQLException
	 */
	public static void clearDataBase() throws SQLException {

		/*
		 * JdbcConnectionPool cp; Connection conn;
		 *
		 * cp = JdbcConnectionPool.create("jdbc:h2:~/conferences", "sa", "sa");
		 * conn = cp.getConnection();
		 *
		 * conn.createStatement().execute(CREATETABLE);
		 * conn.createStatement().execute("DROP table Conference;");
		 * conn.close(); cp.dispose();
		 */

		Conference.getConnectionDataBase().getConnection();
		Conference.getConnectionDataBase().sqlQuery(CREATETABLE);
		Conference.getConnectionDataBase().sqlQuery("DROP table Conference;");
		Conference.getConnectionDataBase().closeAndDisposeConnection();
	}

	/**
	 * Construct a SQL where statement in order to facilitate the implementation
	 * of the methods which need to find some conferences in the database
	 *
	 * @param actual_statement
	 * @param field
	 * @param content
	 * @return
	 */
	private static String constructSetStatement(String actual_statement, String field, String content) {
		String new_statement = "";
		if (actual_statement.isEmpty()) {
			new_statement = "SET " + field + " = \"" + content + "\" ";
		} else {
			new_statement = actual_statement + "AND " + field + " = \"" + content + "\" ";
		}
		return new_statement;
	}

	/**
	 * Asks the user for several parameters and uses them to create a Conference
	 * object
	 *
	 * @return a conference object with the parameters passed by input
	 */
	public static Conference createConference() {
		String tableauQuestion[] = { "url", "title", "start date" + " (" + DATE_FORMAT + ")",
				"end date" + " (" + DATE_FORMAT + ")", "entry fee" };
		Scanner sc = new Scanner(System.in);

		String url = "", title = "", entry_fee = "";
		LocalDate start_date = null, end_date = null;

		for (int i = 0; i <= (tableauQuestion.length - 1); i++) {

			System.out.println("Please enter the " + tableauQuestion[i] + " of the conference");

			switch (i) {
			case 0:
				url = sc.nextLine();
				break;
			case 1:
				title = sc.nextLine();
				break;
			case 2:
				start_date = Conference.readDate(DATE_FORMAT);
				break;
			case 3:
				end_date = Conference.readDate(DATE_FORMAT);
				break;
			case 4:
				entry_fee = sc.nextLine();
				break;
			default:
				break;
			}
		}
		return new Conference(url, title, start_date, end_date, Double.parseDouble(entry_fee));
	}

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

		Conference.removeConferenceFromDatabase(id);
		Conference.menu();
	}

	/**
	 * A menu which enables the user to edit a conference
	 *
	 * @throws SQLException
	 */
	public static void editConference() throws SQLException {
		Scanner sc = new Scanner(System.in);
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern(DATE_FORMAT);
		dtf.withLocale(Locale.FRANCE);
		System.out.println("conférenceID : ");
		int id = sc.nextInt();
		System.out.println("New title (optional): ");
		String title = sc.nextLine();
		System.out.println("New URL (optional): ");
		String url = sc.nextLine();
		System.out.println("New start date (" + DATE_FORMAT + ") (optional): ");
		LocalDate start_date = LocalDate.parse(sc.nextLine(), dtf);
		System.out.println("New end date (" + DATE_FORMAT + ") (optional): ");
		LocalDate end_date = LocalDate.parse(sc.nextLine(), dtf);
		System.out.println("New title (optional): ");
		double entry_fee = sc.nextDouble();
		Conference conf = new Conference(id, title, url, start_date, end_date, entry_fee);
		if (editConferenceInDatabase(conf)) {
			System.out.println("Edit Successful");
		} else {
			System.out.println("Edit failed");
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
			set_statement = constructSetStatement(set_statement, "Title", conf.getTitle());
		}
		if (!conf.getUrl().isEmpty()) {
			set_statement = constructSetStatement(set_statement, "URL", conf.getUrl());
		}
		if (!conf.getStart_date().isEqual(LocalDate.parse("1970-01-01"))) {
			set_statement = constructSetStatement(set_statement, "start_date", conf.getSQLStart_date());
		}
		if (!conf.getEnd_date().isEqual(LocalDate.parse("1970-01-01"))) {
			set_statement = constructSetStatement(set_statement, "end_date", conf.getSQLEnd_date());
		}
		if (!(conf.getEntry_fee() == -1)) {
			set_statement = constructSetStatement(set_statement, "entry_fee", Double.toString(conf.getEntry_fee()));
		}
		set_statement = "UPDATE conferences " + set_statement + where_statement + ";";

		/*
		 * JdbcConnectionPool cp; Connection conn;
		 *
		 * cp = JdbcConnectionPool.create("jdbc:h2:~/conferences", "sa", "sa");
		 * conn = cp.getConnection();
		 */

		Conference.getConnectionDataBase().getConnection();

		/*
		 * conn.createStatement().execute(CREATETABLE);
		 *
		 * conn.createStatement().execute(set_statement);
		 */

		Conference.getConnectionDataBase().sqlQuery(CREATETABLE);

		Conference.getConnectionDataBase().sqlQuery(set_statement);

		/*
		 * conn.close(); cp.dispose();
		 */
		Conference.getConnectionDataBase().closeAndDisposeConnection();

		return true;
	}

	/**
	 * display all the conferences present in the database ordered by start date
	 *
	 * @throws SQLException
	 */
	public static void getAllConferencesFromDatabase() throws SQLException {

		Conference.getAllConferencesFromDatabase("", "");

	}

	/**
	 *
	 */
	public static void getAllConferencesFromDatabase(String type, String value) throws SQLException {
		/*
		 * JdbcConnectionPool cp; Connection conn; cp =
		 * JdbcConnectionPool.create("jdbc:h2:~/conferences", "sa", "sa");
		 */
		Connection conn = Conference.getConnectionDataBase().getConnection();
		conn.createStatement().execute(CREATETABLE);
		try (Statement state = conn.createStatement()) {

			try (ResultSet result = type.isEmpty() && value.isEmpty() ? state.executeQuery("SELECT * FROM conference")
					: state.executeQuery(
							"SELECT * FROM conference WHERE " + type + " = '" + value + "' ORDER BY start_date;")) {

				DateFormat format = new SimpleDateFormat(DATE_FORMAT);
				format.setLenient(false);
				ArrayList<Conference> conferencesArray = new ArrayList<Conference>();

				while (result.next()) {

					int id = result.getInt(1);
					String url = result.getString(2);
					String title = result.getString(3);
					LocalDate start_date = LocalDate.parse(result.getString(4));
					LocalDate end_date = LocalDate.parse(result.getString(5));
					double entry_fee = result.getDouble(6);
					conferencesArray.add(new Conference(id, url, title, start_date, end_date, entry_fee));
				}

				for (Conference i : conferencesArray) {
					System.out.println("####################");
					System.out.println("Conference: " + i.title + " (" + i.url + ")");
					System.out.println("From the " + i.start_date + " to the " + i.end_date);
					System.out.println("Fee: " + i.entry_fee);
				}

				result.close();
			}

			state.close();
		}
		return;
	}

	/**
	 * get the conference from the database whose conferenceID is the same than
	 * the one in parameter
	 *
	 * @param conferenceID
	 * @return
	 * @throws SQLException
	 * @throws ParseException
	 */
	public static Conference getConferenceFromDatabase(int conferenceID) throws SQLException, ParseException {
		/*
		 * JdbcConnectionPool cp; Connection conn; cp =
		 * JdbcConnectionPool.create("jdbc:h2:~/conferences", "sa", "sa"); conn
		 * = cp.getConnection();
		 */
		Connection conn = Conference.getConnectionDataBase().getConnection();
		Statement state = conn.createStatement();
		try (ResultSet result = state.executeQuery("SELECT * FROM conference WHERE conferenceID = " + conferenceID)) {

			DateFormat format = new SimpleDateFormat(SQL_DATE_FORMAT);
			format.setLenient(false);

			result.next();
			int id = result.getInt(1);
			String url = result.getString(2);
			String title = result.getObject(3).toString();
			LocalDate start_date = LocalDate.parse(result.getString(4));
			LocalDate end_date = LocalDate.parse(result.getString(5));
			double entry_fee = result.getDouble(6);
			result.close();
			state.close();
			return new Conference(id, title, url, start_date, end_date, entry_fee);
		}
	}

	/**
	 * Insert the given conference in the database
	 *
	 * @param conf
	 * @throws SQLException
	 */
	public static void insertInDatabase(Conference conf) throws SQLException {
		/*
		 * JdbcConnectionPool cp; Connection conn;
		 *
		 * cp = JdbcConnectionPool.create("jdbc:h2:~/conferences", "sa", "sa");
		 * conn = cp.getConnection();
		 *
		 *
		 *
		 * conn.createStatement().execute(CREATETABLE);
		 */

		Conference.getConnectionDataBase().getConnection();

		Conference.getConnectionDataBase().sqlQuery(CREATETABLE);

		String insert_statement = "INSERT INTO conference (Title, URL, end_date, start_date, entry_fee)   VALUES ('"
				+ conf.getTitle() + "','" + conf.getUrl() + "','" + conf.getSQLStart_date() + "','"
				+ conf.getSQLEnd_date() + "','" + conf.getEntry_fee() + "' );";

		Conference.getConnectionDataBase().sqlQuery(insert_statement);
		Conference.getConnectionDataBase().closeAndDisposeConnection();

		/*
		 * conn.createStatement().execute(insert_statement); conn.close();
		 * cp.dispose();
		 */

	}

	public static ConnectionDataBase getConnectionDataBase() {
		return connectionDataBase;
	}

	public void setConnectionDataBase(ConnectionDataBase connectionDataBase) {
		this.connectionDataBase = connectionDataBase;
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

			System.out.println("Welcome to the conference creation | Created by Javier & Antoine");
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
				Conference.createConference();
				break;
			case 2:
				Conference.searchMenu();
				break;
			case 3:
				Conference.getAllConferencesFromDatabase();
				break;
			case 4:
				Conference.editConference();
				break;
			case 5:
				Conference.deleteMenu();
				break;
			default:
				break;
			}
		}
	}

	/**
	 * Keeps asking the user for a date until the date verifies a specific
	 * format and then returns the date
	 *
	 * @param dateFormat
	 *            the format that the date needs to verify
	 * @return the date passed by input in the Date format
	 */

	private static LocalDate readDate(String dateFormat) {

		Scanner sc = new Scanner(System.in);

		DateTimeFormatter dtf = DateTimeFormatter.ofPattern(dateFormat);
		dtf.withLocale(Locale.FRANCE);
		// .setLenient(false);
		LocalDate date = null;

		while (date == null) {
			String line = sc.nextLine();
			try {
				date = LocalDate.parse(line, dtf);
			} catch (Exception e) {
				System.out.println("Sorry, that's not valid. Please try again.");
			}
		}

		return date;
	}

	/**
	 *
	 * @param id
	 * @throws SQLException
	 */
	public static void removeConferenceFromDatabase(int id) throws SQLException {

		/*
		 * JdbcConnectionPool cp; Connection conn;
		 *
		 * cp = JdbcConnectionPool.create("jdbc:h2:~/conferences", "sa", "sa");
		 *
		 * conn = cp.getConnection();
		 *
		 * conn.createStatement().
		 * execute("Delete from conference where conferenceID =" + id + ";");
		 * conn.close(); cp.dispose();
		 */

		Conference.getConnectionDataBase().getConnection();
		Conference.getConnectionDataBase().sqlQuery("Delete from conference where conferenceID =" + id + ";");
		Conference.getConnectionDataBase().closeAndDisposeConnection();
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
				Conference.getAllConferencesFromDatabase(getValidSearchQuery(), "title");
				break;
			case 2:

				Conference.getAllConferencesFromDatabase(getValidSearchQuery(), "url");
				break;
			case 3:
				Conference.getAllConferencesFromDatabase(getValidSearchQuery(), "startdate");

				break;
			default:
			}
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
	 * returns a whereStament ready to be used in getAllFromDatabase
	 *
	 * @param querry
	 *            a query formated like in the searchMenu
	 * @return
	 */

	private LocalDate end_date;

	private double entry_fee;

	private int id;

	private LocalDate start_date;

	private String title;

	private String url;

	private static ConnectionDataBase connectionDataBase;

	public Conference(int id, String title, String url, LocalDate start_date, LocalDate end_date, double entry_fee) {
		this.id = id;
		this.url = url;
		this.title = title;
		this.start_date = start_date;
		this.end_date = end_date;
		this.entry_fee = entry_fee;
	}

	public Conference(String title, String url, LocalDate start_date, LocalDate end_date, double entry_fee) {
		this(0, title, url, start_date, end_date, entry_fee);
	}

	@Override
	/**
	 * Compare the conference to obj by comparing all attributes except id
	 */
	public boolean equals(Object obj) {
		if (obj instanceof Conference) {
			Conference conference2 = (Conference) obj;
			System.out.println(toString());
			System.out.println(conference2.toString());
			if (title.equals(conference2.title) && url.equals(conference2.url)
					&& start_date.equals(conference2.start_date) && end_date.equals(conference2.end_date)
					&& (entry_fee == conference2.entry_fee)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Overriding a required method
	 */
	@Override
	public int hashCode() {
		return super.hashCode();
	}

	public LocalDate getEnd_date() {
		return end_date;
	}

	public double getEntry_fee() {
		return entry_fee;
	}

	public int getId() {
		return id;
	}

	public String getSQLEnd_date() {
		return getEnd_date().toString();
	}

	public String getSQLStart_date() {
		return getStart_date().toString();
	}

	public LocalDate getStart_date() {
		return start_date;
	}

	public String getTitle() {
		return title;
	}

	public String getUrl() {
		return url;
	}

	public void setEnd_date(LocalDate end_date) {
		this.end_date = end_date;
	}

	public void setEntry_fee(double entry_fee) {
		this.entry_fee = entry_fee;
	}

	public void setStart_date(LocalDate start_date) {
		this.start_date = start_date;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	@Override
	public String toString() {
		return "Conference [id=" + id + "title=" + title + ", url=" + url + ", start_date=" + start_date + ",end_date="
				+ end_date + ", entry_fee=" + entry_fee + "]";
	}

}
