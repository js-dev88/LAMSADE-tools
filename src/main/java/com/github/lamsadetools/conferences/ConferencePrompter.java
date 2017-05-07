package com.github.lamsadetools.conferences;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.Scanner;

public class ConferencePrompter {
	/**
	 * Asks the user for several parameters and uses them to create a Conference
	 * object
	 *
	 * @return a conference object with the parameters passed by input
	 */
	public static Conference promptConference() {
		String tableauQuestion[] = { "url", "title", "start date" + " (" + Conference.DATE_FORMAT + ")",
				"end date" + " (" + Conference.DATE_FORMAT + ")", "entry fee" };
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
				start_date = ConferencePrompter.readDate(Conference.DATE_FORMAT);
				break;
			case 3:
				end_date = ConferencePrompter.readDate(Conference.DATE_FORMAT);
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
}
