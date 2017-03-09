package conferences;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Date;
import java.util.Scanner;

import org.h2.jdbcx.JdbcConnectionPool;

public class Conference {
	/**
	 * Asks the user for several parameters and uses them to create a Conference
	 * object
	 *
	 * @return a conference object with the parameters passed by input
	 */
	public static Conference createConference() {
		String dateFormat = "dd/MM/yy";
		String tableauQuestion[] = { "url", "title", "start date" + " (" + dateFormat + ")",
				"end date" + " (" + dateFormat + ")", "entry fee" };
		Scanner sc = new Scanner(System.in);

		String url = "", title = "", entry_fee = "";
		Date start_date = null, end_date = null;

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
				start_date = Conference.readDate(dateFormat);
				break;
			case 3:
				end_date = Conference.readDate(dateFormat);
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
	 * Format a java date into a SQL date
	 *
	 * @param date
	 * @return
	 */
	public static String dateFormater(Date date) {
		Instant instant = date.toInstant();
		ZoneId zoneId = ZoneId.of("America/Montreal");
		ZonedDateTime zdt = ZonedDateTime.ofInstant(instant, zoneId);
		LocalDate localDate = zdt.toLocalDate();
		java.sql.Date sqlDate = java.sql.Date.valueOf(localDate);
		return sqlDate.toString();
	}

	/**
	 * display all the conferences present in the database ordered by start date
	 *
	 * @throws SQLException
	 */
	public static void getAllConferencesFromDatabase() throws SQLException {
		JdbcConnectionPool cp;
		Connection conn;
		cp = JdbcConnectionPool.create("jdbc:h2:~/conferences", "sa", "sa");
		conn = cp.getConnection();
		Statement state = conn.createStatement();
		ResultSet result = state.executeQuery("SELECT * FROM conference ORDER BY start_date");
		ResultSetMetaData resultMeta = result.getMetaData();

		System.out.println("\n**********************************");
		// We print the name of the columns
		for (int i = 1; i <= resultMeta.getColumnCount(); i++) {
			System.out.print("\t" + resultMeta.getColumnName(i).toUpperCase() + "\t *");
		}

		System.out.println("\n**********************************");

		while (result.next()) {
			for (int i = 1; i <= resultMeta.getColumnCount(); i++) {
				System.out.print("\t" + result.getObject(i).toString() + "\t |");
			}

			System.out.println("\n---------------------------------");

		}

		result.close();
		state.close();
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
		JdbcConnectionPool cp;
		Connection conn;
		cp = JdbcConnectionPool.create("jdbc:h2:~/conferences", "sa", "sa");
		conn = cp.getConnection();
		Statement state = conn.createStatement();
		ResultSet result = state.executeQuery("SELECT * FROM conference WHERE conferenceID = " + conferenceID);

		String dateFormat = "yyyy-MM-dd";
		DateFormat format = new SimpleDateFormat(dateFormat);
		format.setLenient(false);

		result.next();
		String url = result.getString(2);
		System.out.println(url);
		String title = result.getObject(3).toString();
		System.out.println(title);
		Date start_date = result.getDate(4);
		Date end_date = result.getDate(5);
		double entry_fee = result.getDouble(6);
		result.close();
		state.close();
		return new Conference(url, title, start_date, end_date, entry_fee);

	}

	/**
	 * Insert the given conference in the database
	 *
	 * @param conf
	 * @throws SQLException
	 */
	public static void insertInDatabase(Conference conf) throws SQLException {
		JdbcConnectionPool cp;
		Connection conn;

		cp = JdbcConnectionPool.create("jdbc:h2:~/conferences", "sa", "sa");
		conn = cp.getConnection();

		try {
			String create_tables = "CREATE TABLE conference (" + "conferenceID     SERIAL, "
					+ "Title            varchar(255) NOT NULL, " + "URL              varchar(255) NOT NULL, "
					+ "start_date       date NOT NULL, " + "end_date         date NOT NULL, "
					+ "entry_fee        double, " + "CONSTRAINT conferenceID PRIMARY KEY (conferenceID) ); ";
			conn.createStatement().execute(create_tables);

		} catch (Exception e) {
			System.out.println("Table not created, it probably already exists");
		}

		String insert_statement = "INSERT INTO conference (Title, URL, end_date, start_date, entry_fee)   VALUES ('"
				+ conf.getTitle() + "','" + conf.getUrl() + "','" + conf.getSQLStart_date() + "','"
				+ conf.getSQLEnd_date() + "','" + conf.getEntry_fee() + "' );";
		conn.createStatement().execute(insert_statement);
		cp.dispose();
		conn.close();

	}

	/**
	 * display a menu which enables you to create, search, edit and delete
	 * conferences
	 * 
	 * @throws SQLException
	 */
	public static void menu() throws SQLException {
		int option = -1;

		Scanner sc = new Scanner(System.in);

		while (option != 0) {
			System.out.println("Welcome to the conference creation | Created by Javier & Antoine");
			System.out.println("#################### ");
			System.out.println("Please choose an option:");
			System.out.println("1. Create a new conference.");
			System.out.println("2. Search a conference.");
			System.out.println("3. View all conferences.");
			System.out.println("4. Edit a conference."); // by URL or title
			System.out.println("5. Delete a conference.");
			System.out.println("0. Exit");
			String optionstr = sc.nextLine();
			// Verify input is an integer
			try {
				option = Integer.parseInt(optionstr);
			} catch (NumberFormatException ex) {
				System.out.println("Please choose a valid option");
				option = -1;
			}
		}

		switch (option) {
		case 1:
			Conference.createConference();
			break;
		case 2:

			break;
		case 3:
			Conference.getAllConferencesFromDatabase();
			break;
		case 4:

			break;

		default:
			break;
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

	private static Date readDate(String dateFormat) {

		Scanner sc = new Scanner(System.in);

		DateFormat format = new SimpleDateFormat(dateFormat);
		format.setLenient(false);
		Date date = null;

		while (date == null) {
			String line = sc.nextLine();
			try {
				date = format.parse(line);
			} catch (ParseException e) {
				System.out.println("Sorry, that's not valid. Please try again.");
			}
		}

		return date;
	}

	private Date end_date;

	private double entry_fee;

	private Date start_date;

	private String title;

	private String url;

	public Conference(String title, Date start_date, Date end_date) {
		this(null, title, start_date, end_date, 0);
	}

	public Conference(String title, Date start_date, Date end_date, double entry_fee) {
		this(null, title, start_date, end_date, entry_fee);
	}

	public Conference(String url, String title, Date start_date, Date end_date) {
		this(url, title, start_date, end_date, 0);
	}

	public Conference(String url, String title, Date start_date, Date end_date, double entry_fee) {
		this.url = url;
		this.title = title;
		this.start_date = start_date;
		this.end_date = end_date;
		this.entry_fee = entry_fee;
	}

	public Date getEnd_date() {
		return end_date;
	}

	public double getEntry_fee() {
		return entry_fee;
	}

	public String getSQLEnd_date() {
		return Conference.dateFormater(end_date);
	}

	public String getSQLStart_date() {
		return Conference.dateFormater(start_date);
	}

	public Date getStart_date() {
		return start_date;
	}

	public String getTitle() {
		return title;
	}

	public String getUrl() {
		return url;
	}

	public void setEnd_date(Date end_date) {
		this.end_date = end_date;
	}

	public void setEntry_fee(double entry_fee) {
		this.entry_fee = entry_fee;
	}

	public void setStart_date(Date start_date) {
		this.start_date = start_date;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public void setUrl(String url) {
		this.url = url;
	}

}
