package com.github.lantoine.lamsadetools.conferences.database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.lantoine.lamsadetools.conferences.Conference;

public class ConferenceDatabase {

	private static final Logger logger = LoggerFactory.getLogger(ConferenceDatabase.class);
	private static ConnectionDataBase connectionDataBase;
	private static final String CREATETABLE = "CREATE TABLE IF NOT EXISTS conference (" + "conferenceID     SERIAL, "
			+ "Title            varchar(255) NOT NULL, " + "URL              varchar(255) NOT NULL, "
			+ "start_date       date NOT NULL, " + "end_date         date NOT NULL, " + "entry_fee        double, "
			+ "City            varchar(255) NOT NULL, " + "Country            varchar(255) NOT NULL, "
			+ "CONSTRAINT conferenceID PRIMARY KEY (conferenceID) ); ";

	private static final String SQL_DATE_FORMAT = "yyyy-MM-dd";

	/**
	 * Create the table conference and return a message asking to execute delete
	 * the database if there was a problem
	 */
	static void createTable() {
		Connection conn;
		try {
			conn = ConferenceDatabase.getConnectionDataBase().getConnection();
			conn.createStatement().execute(CREATETABLE);
		} catch (SQLException e) {
			System.out.println(
					"The database could not be created or upgraded, there must be an old version on your computer, please execute the function Detete database");
		}

	}

	/**
	 * Drop the table conference in order to erase all the conferences stored in
	 * the database
	 *
	 * @throws SQLException
	 */
	public static void clearDataBase() throws SQLException {

		ConferenceDatabase.getConnectionDataBase().getConnection();
		ConferenceDatabase.getConnectionDataBase().sqlQuery(CREATETABLE);
		ConferenceDatabase.getConnectionDataBase().sqlQuery("DROP table Conference;");
		ConferenceDatabase.getConnectionDataBase().closeAndDisposeConnection();
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
	static String constructSetStatement(String actual_statement, String field, String content) {
		String new_statement = "";
		if (actual_statement.isEmpty()) {
			new_statement = "SET " + field + " = \"" + content + "\" ";
		} else {
			new_statement = actual_statement + "AND " + field + " = \"" + content + "\" ";
		}
		return new_statement;

	}

	/**
	 * display all the conferences present in the database ordered by start date
	 *
	 * @throws SQLException
	 */
	public static void getAllConferencesFromDatabase() throws SQLException {

		ConferenceDatabase.getConferencesFromDatabase("", "");

	}

	/**
	 *
	 */
	public static void getConferencesFromDatabase(String type, String value) throws SQLException {

		Connection conn = ConferenceDatabase.getConnectionDataBase().getConnection();
		createTable();

		// Prepared statement to avoid SQL injection
		PreparedStatement preparedStatement = null;
		PreparedStatement preparedStatement1 = null;

		String fieldName = type;
		String selectAllSQL = "SELECT * from conference;";

		String selectSQL = "SELECT * FROM conference WHERE " + fieldName + " = ? ORDER BY start_date;";

		System.out.println(type);
		DateFormat format = new SimpleDateFormat(Conference.DATE_FORMAT);
		format.setLenient(false);
		try (Statement state = conn.createStatement()) {
			ResultSet result;
			if (fieldName == "") {

				preparedStatement = conn.prepareStatement(selectAllSQL);
				result = preparedStatement.executeQuery();
			} else {

				preparedStatement1 = conn.prepareStatement(selectSQL);
				preparedStatement1.setString(1, value);
				result = preparedStatement1.executeQuery();
			}
			ArrayList<Conference> conferencesArray = new ArrayList<Conference>();

			while (result.next()) {

				int id = result.getInt(1);
				String url = result.getString(2);
				String title = result.getString(3);
				LocalDate start_date = LocalDate.parse(result.getString(4));
				LocalDate end_date = LocalDate.parse(result.getString(5));
				double entry_fee = result.getDouble(6);
				String city = result.getString(7);
				String country = result.getString(8);
				conferencesArray.add(new Conference(id, url, title, start_date, end_date, entry_fee, city, country));
			}

			for (Conference i : conferencesArray) {
				System.out.println("####################");
				System.out.println("Conference: " + i.getTitle() + " (" + i.getUrl() + ")");
				System.out.println("From the " + i.getStart_date() + " to the " + i.getEnd_date());
				System.out.println("in " + i.getCity() + " to this country " + i.getCountry());
				System.out.println("Fee: " + i.getEntry_fee());
			}

			result.close();
			state.close();
		}
	}

	public static ConnectionDataBase getConnectionDataBase() {
		return connectionDataBase;
	}

	/**
	 * Insert the given conference in the database
	 *
	 * @param conf
	 * @throws SQLException
	 */
	public static void insertInDatabase(Conference conf) throws SQLException {

		String insertQuery = "INSERT INTO conference (Title, URL, end_date, start_date, entry_fee, City, Country)   VALUES (?, ?, ?, ?, ?, ?, ?);";
		PreparedStatement preparedStatement = null;

		Connection con = ConferenceDatabase.getConnectionDataBase().getConnection();

		createTable();

		preparedStatement = con.prepareStatement(insertQuery);

		preparedStatement.setString(1, conf.getTitle());
		preparedStatement.setString(2, conf.getUrl());
		preparedStatement.setDate(3, java.sql.Date.valueOf(conf.getStart_date()));
		preparedStatement.setDate(4, java.sql.Date.valueOf(conf.getEnd_date()));
		preparedStatement.setDouble(5, conf.getEntry_fee());
		preparedStatement.setString(6, conf.getCity());
		preparedStatement.setString(7, conf.getCountry());

		try {
			preparedStatement.executeUpdate();
		} catch (SQLException e) {
			System.out.println(
					"The database could not be created or upgraded, there must be an old version on your computer, please execute the function Detete database");
			logger.debug(
					"The database could not be created or upgraded, there must be an old version on your computer");

		}

		ConferenceDatabase.getConnectionDataBase().closeAndDisposeConnection();
		con.close();
	}

	/**
	 *
	 * @param id
	 * @throws SQLException
	 */
	public static void removeConferenceFromDatabase(int id) throws SQLException {

		Connection conn = ConferenceDatabase.getConnectionDataBase().getConnection();
		PreparedStatement preparedStatement = null;
		String deleteQuery = "Delete from conference where conferenceID = ? ;";

		preparedStatement = conn.prepareStatement(deleteQuery);
		preparedStatement.setInt(1, id);

		preparedStatement.executeUpdate();
		ConferenceDatabase.getConnectionDataBase().closeAndDisposeConnection();
	}

	/**
	 * Function to return an ArrayList of conferences
	 *
	 * @return an ArrayList of all the conferences
	 * @throws SQLException
	 */
	public static ArrayList<Conference> returnAllConferencesFromDatabase() throws SQLException {

		ArrayList<Conference> conferencesArray = new ArrayList<>();
		Connection conn = ConferenceDatabase.getConnectionDataBase().getConnection();
		createTable();

		try (Statement state = conn.createStatement()) {

			try (ResultSet result = state.executeQuery("SELECT * FROM conference")) {
				DateFormat format = new SimpleDateFormat(Conference.DATE_FORMAT);
				format.setLenient(false);

				while (result.next()) {

					int _id = result.getInt(1);
					String _url = result.getString(2);
					String _title = result.getString(3);
					LocalDate _start_date = LocalDate.parse(result.getString(4));
					LocalDate _end_date = LocalDate.parse(result.getString(5));
					double _entry_fee = result.getDouble(6);
					String city = result.getString(7);
					String address = result.getString(8);
					conferencesArray
							.add(new Conference(_id, _url, _title, _start_date, _end_date, _entry_fee, city, address));
				}

				result.close();
			}
			state.close();
			return conferencesArray;
		}
	}

	public void setConnectionDataBase(ConnectionDataBase connectionDataBase) {
		this.connectionDataBase = connectionDataBase;
	}

}