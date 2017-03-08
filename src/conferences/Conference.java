package conferences;

import java.sql.Connection;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Scanner;

import org.h2.jdbcx.JdbcConnectionPool;

public class Conference {
	private String url;

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public Date getStart_date() {
		return start_date;
	}

	public void setStart_date(Date start_date) {
		this.start_date = start_date;
	}

	public Date getEnd_date() {
		return end_date;
	}

	public void setEnd_date(Date end_date) {
		this.end_date = end_date;
	}

	public double getEntry_fee() {
		return entry_fee;
	}

	public void setEntry_fee(double entry_fee) {
		this.entry_fee = entry_fee;
	}

	private String title;
	private Date start_date;
	private Date end_date;
	private double entry_fee;

	public Conference(String url, String title, Date start_date, Date end_date, double entry_fee) {
		this.url = url;
		this.title = title;
		this.start_date = start_date;
		this.end_date = end_date;
		this.entry_fee = entry_fee;
	}

	public Conference(String url, String title, Date start_date, Date end_date) {
		this(url, title, start_date, end_date, 0);
	}

	public Conference(String title, Date start_date, Date end_date, double entry_fee) {
		this(null, title, start_date, end_date, entry_fee);
	}

	public Conference(String title, Date start_date, Date end_date) {
		this(null, title, start_date, end_date, 0);
	}

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

		for (int i = 0; i <= tableauQuestion.length - 1; i++) {

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

	public static void menu() {
		int option = -1;

		Scanner sc = new Scanner(System.in);

		while (option != 0) {
			System.out.println("Welcome to the conference creation | Created by Javier & Antoine");
			System.out.println("#################### ");
			System.out.println("Please choose an option:");
			System.out.println("1. Create a new conference.");
			System.out.println("2. Search a conference.");
			System.out.println("3. Edit a conference."); // by URL or title
			System.out.println("4. Delete a conference.");
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

			break;
		case 4:

			break;

		default:
			break;
		}

	}

	public static void insertInDatabase(Conference conf) throws SQLException {
		JdbcConnectionPool cp;
		Connection conn;

		//try {
			cp = JdbcConnectionPool.create("jdbc:h2:~/conferences", "sa", "sa");
			conn = cp.getConnection();
			
			conn.createStatement().execute("DROP TABLE conference");
			
			try {
				String create_tables = "CREATE TABLE conference ("+ 
						 "conferenceID     SERIAL, "+
						 "Title            varchar(255) NOT NULL, "+
						 "URL              varchar(255) NOT NULL, "+
						 "start_date       date NOT NULL, "+
						 "end_date         date NOT NULL, "+
						 "entry_fee        double, "+
						 "CONSTRAINT conferenceID PRIMARY KEY (conferenceID) ); ";
				conn.createStatement().execute(create_tables);
				
			} catch (Exception e){
				System.out.println("Table not created, it probably already exists");
			}
			
			//String insert_statement = "INSERT INTO conference VALUES ('1',"+conf.getTitle()+"','"+conf.getUrl()+"','"+conf.getStart_date()+"','"+conf.getEnd_date()+"','"+conf.getEntry_fee()+"' );";
			String insert_statement = "INSERT INTO conference VALUES ('1','"+conf.getTitle()+"','"+conf.getUrl()+"','"+"2017-03-10"+"','"+"2017-03-20"+"','"+conf.getEntry_fee()+"' );";
			conn.createStatement().execute(insert_statement);
			cp.dispose();
			conn.close();
		// catch (SQLException ex) {
		//	System.out.println("Impossible d'insérer la conférence dans la base de données");
		//	return;
		//}

	}
	
	
}
