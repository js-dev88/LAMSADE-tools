package com.github.lantoine.lamsadetools.conferences.database;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;

import com.github.lantoine.lamsadetools.conferences.Conference;

public class ConferenceDatabase {
	private static ConnectionDataBase connectionDataBase;
	private static final String CREATETABLE = "CREATE TABLE IF NOT EXISTS conference (" + "conferenceID     SERIAL, "
			+ "Title            varchar(255) NOT NULL, " + "URL              varchar(255) NOT NULL, "
			+ "start_date       date NOT NULL, " + "end_date         date NOT NULL, " + "entry_fee        double, "
			+ "City            varchar(255) NOT NULL, "
			+"Address            varchar(255) NOT NULL, "
			+ "CONSTRAINT conferenceID PRIMARY KEY (conferenceID) ); ";

	private static final String SQL_DATE_FORMAT = "yyyy-MM-dd";

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
			set_statement = ConferenceDatabase.constructSetStatement(set_statement, "Title", conf.getTitle());
		}
		
		if (!conf.getAddress().isEmpty()) {
			set_statement = ConferenceDatabase.constructSetStatement(set_statement, "Title", conf.getTitle());
		}
		set_statement = "UPDATE conferences " + set_statement + where_statement + ";";

		/*
		 * JdbcConnectionPool cp; Connection conn;
		 *
		 * cp = JdbcConnectionPool.create("jdbc:h2:~/conferences", "sa", "sa");
		 * conn = cp.getConnection();
		 */

		ConferenceDatabase.getConnectionDataBase().getConnection();

		/*
		 * conn.createStatement().execute(CREATETABLE);
		 *
		 * conn.createStatement().execute(set_statement);
		 */

		ConferenceDatabase.getConnectionDataBase().sqlQuery(CREATETABLE);

		ConferenceDatabase.getConnectionDataBase().sqlQuery(set_statement);

		/*
		 * conn.close(); cp.dispose();
		 */
		ConferenceDatabase.getConnectionDataBase().closeAndDisposeConnection();

		return true;
	}

	/**
	 * display all the conferences present in the database ordered by start date
	 *
	 * @throws SQLException
	 */
	public static void getAllConferencesFromDatabase() throws SQLException {

		ConferenceDatabase.getAllConferencesFromDatabase("", "");

	}

	/**
	 *
	 */
	public static void getAllConferencesFromDatabase(String type, String value) throws SQLException {
		/*
		 * JdbcConnectionPool cp; Connection conn; cp =
		 * JdbcConnectionPool.create("jdbc:h2:~/conferences", "sa", "sa");
		 */
		Connection conn = ConferenceDatabase.getConnectionDataBase().getConnection();
		conn.createStatement().execute(CREATETABLE);

		try (Statement state = conn.createStatement()) {

			try (ResultSet result = type.isEmpty() && value.isEmpty() ? state.executeQuery("SELECT * FROM conference")
					: state.executeQuery(
							"SELECT * FROM conference WHERE " + type + " = '" + value + "' ORDER BY start_date;")) {

				DateFormat format = new SimpleDateFormat(Conference.DATE_FORMAT);
				format.setLenient(false);
				ArrayList<Conference> conferencesArray = new ArrayList<Conference>();

				while (result.next()) {

					int id = result.getInt(1);
					String url = result.getString(2);
					String title = result.getString(3);
					LocalDate start_date = LocalDate.parse(result.getString(4));
					LocalDate end_date = LocalDate.parse(result.getString(5));
					double entry_fee = result.getDouble(6);
					String city = result.getString(7);
					String address = result.getString(8);
					conferencesArray.add(new Conference(id, url, title, start_date, end_date, entry_fee, city, address));
				}

				for (Conference i : conferencesArray) {
					System.out.println("####################");
					System.out.println("Conference: " + i.getTitle() + " (" + i.getUrl() + ")");
					System.out.println("From the " + i.getStart_date() + " to the " + i.getEnd_date());
					System.out.println("in" + i.getCity() + " to this address " + i.getAddress());
					System.out.println("Fee: " + i.getEntry_fee());
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
		Connection conn = ConferenceDatabase.getConnectionDataBase().getConnection();
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
			String city = result.getString(7);
			String address = result.getString(8);
			result.close();
			state.close();
			return new Conference(id, title, url, start_date, end_date, entry_fee, city, address);
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

		ConferenceDatabase.getConnectionDataBase().getConnection();

		ConferenceDatabase.getConnectionDataBase().sqlQuery(CREATETABLE);

		String insert_statement = "INSERT INTO conference (Title, URL, end_date, start_date, entry_fee)   VALUES ('"
				+ conf.getTitle() + "','" + conf.getUrl() + "','" + conf.getStart_date() + "','" + conf.getEnd_date()
				+ "','" + conf.getEntry_fee() + "' );";

		ConferenceDatabase.getConnectionDataBase().sqlQuery(insert_statement);
		ConferenceDatabase.getConnectionDataBase().closeAndDisposeConnection();

		/*
		 * conn.createStatement().execute(insert_statement); conn.close();
		 * cp.dispose();
		 */

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

		ConferenceDatabase.getConnectionDataBase().getConnection();
		ConferenceDatabase.getConnectionDataBase().sqlQuery("Delete from conference where conferenceID =" + id + ";");
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
		conn.createStatement().execute(CREATETABLE);

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
					conferencesArray.add(new Conference(_id, _url, _title, _start_date, _end_date, _entry_fee, city, address));
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
