package conferences;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.Scanner;

import org.h2.jdbcx.JdbcConnectionPool;

public class Conference {
	private static final String CREATETABLE = "CREATE TABLE IF NOT EXISTS conference (" + "conferenceID     SERIAL, "
			+ "Title            varchar(255) NOT NULL, " + "URL              varchar(255) NOT NULL, "
			+ "start_date       date NOT NULL, " + "end_date         date NOT NULL, " + "entry_fee        double, "
			+ "CONSTRAINT conferenceID PRIMARY KEY (conferenceID) ); ";

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

		String dateFormat = "yyyy-MM-dd";
		DateFormat format = new SimpleDateFormat(dateFormat);
		format.setLenient(false);
		ArrayList<Conference> conferencesArray = new ArrayList<Conference>();

		while (result.next()) {
			int id = result.getInt(1);
			String url = result.getString(2);
			String title = result.getString(3);
			Date start_date = result.getDate(4);
			Date end_date = result.getDate(5);
			double entry_fee = result.getDouble(6);
			conferencesArray.add(new Conference(id, url, title, start_date, end_date, entry_fee));
		}

		for (Conference i : conferencesArray) {
			System.out.println(i);
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
		int id = result.getInt(1);
		String url = result.getString(2);
		String title = result.getObject(3).toString();
		Date start_date = result.getDate(4);
		Date end_date = result.getDate(5);
		double entry_fee = result.getDouble(6);
		result.close();
		state.close();
		return new Conference(id, title, url, start_date, end_date, entry_fee);

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

		conn.createStatement().execute(CREATETABLE);

		String insert_statement = "INSERT INTO conference (Title, URL, end_date, start_date, entry_fee)   VALUES ('"
				+ conf.getTitle() + "','" + conf.getUrl() + "','" + conf.getSQLStart_date() + "','"
				+ conf.getSQLEnd_date() + "','" + conf.getEntry_fee() + "' );";
		conn.createStatement().execute(insert_statement);
		conn.close();
		cp.dispose();

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

	private int id;

	private Date start_date;

	private String title;

	private String url;

	public Conference(int id, String title, String url, Date start_date, Date end_date, double entry_fee) {
		this.url = url;
		this.title = title;
		this.start_date = start_date;
		this.end_date = end_date;
		this.entry_fee = entry_fee;
	}

	public Conference(String title, String url, Date start_date, Date end_date, double entry_fee) {
		this(0, title, url, start_date, end_date, entry_fee);
	}

	@Override
	/**
	 * Compare the conference to obj by comparing all attributes except id
	 */
	public boolean equals(Object obj) {
		if (obj instanceof Conference) {
			System.out.println("intance of conf");
			Conference conference2 = (Conference) obj;
			if (this.title.equals(conference2.title) && this.url.equals(conference2.url)
					&& this.start_date.equals(conference2.start_date) && this.end_date.equals(conference2.end_date)
					&& this.entry_fee == conference2.entry_fee) {
				return true;
			}
		}
		return false;
	}

	public Date getEnd_date() {
		return end_date;
	};

	public double getEntry_fee() {
		return entry_fee;
	}

	public int getId() {
		return id;
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

	@Override
	public String toString() {
		return "Conference [id=" + id + "title=" + title + ", url=" + url + ", start_date=" + start_date + ",end_date="
				+ end_date + ", entry_fee=" + entry_fee + "]";
	}

}
