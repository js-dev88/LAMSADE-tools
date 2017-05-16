package com.github.lantoine.lamsadetools.conferences;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URISyntaxException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;

import net.fortuna.ical4j.data.CalendarOutputter;
import net.fortuna.ical4j.data.ParserException;
import net.fortuna.ical4j.model.Dur;
import net.fortuna.ical4j.model.Property;
import net.fortuna.ical4j.model.ValidationException;
import net.fortuna.ical4j.model.component.VEvent;
import net.fortuna.ical4j.model.property.CalScale;
import net.fortuna.ical4j.model.property.Description;
import net.fortuna.ical4j.model.property.ProdId;
import net.fortuna.ical4j.model.property.Version;

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

	public static final String DATE_FORMAT = "dd/MM/yyyy";

	private LocalDate end_date;

	private double entry_fee;

	private int id;

	private LocalDate start_date;

	private String title;

	private String url;
	
	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	private String city;
	
	private String country;

	public Conference(int id, String title, String url, LocalDate start_date, LocalDate end_date, double entry_fee, String city, String country) {
		this.id = id;
		this.url = url;
		this.title = title;
		this.start_date = start_date;
		this.end_date = end_date;
		this.entry_fee = entry_fee;
		this.city = city;
		this.country = country;
		
	}

	public Conference(String title, String url, LocalDate start_date, LocalDate end_date, double entry_fee,String city, String country) {
		this(0, title, url, start_date, end_date, entry_fee, city, country);
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

	public LocalDate getEnd_date() {
		return end_date;
	}

	public double getEntry_fee() {
		return entry_fee;
	}

	public int getId() {
		return id;
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

	/**
	 * Overriding a required method
	 */
	@Override
	public int hashCode() {
		return super.hashCode();
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
				+ end_date + ", entry_fee=" + entry_fee +",city=" + city +", country=" + country+"]";
	}

	/**
	 * Generates a vcal file with the conference details with this object's data
	 *
	 * @param filename:
	 *            the path including the name of the file to be generated
	 * @throws IOException
	 * @throws ValidationException
	 * @throws ParserException
	 *
	 * @author Javier Martínez
	 */
	public void generateCalendarFile(String filename) throws IOException, ValidationException, ParserException {
		String calFile = filename;

		// start time
		java.util.Calendar startCal = java.util.Calendar.getInstance();

		startCal.setTime(java.sql.Date.valueOf(getStart_date()));
		// end time
		java.util.Calendar endCal = java.util.Calendar.getInstance();
		endCal.setTime(java.sql.Date.valueOf(getEnd_date()));

		String subject = "Conference";
		String description = "A conference with a fee of " + getEntry_fee();
		String hostEmail = "";

		// Creating a new calendar
		net.fortuna.ical4j.model.Calendar calendar = new net.fortuna.ical4j.model.Calendar();
		calendar.getProperties().add(new ProdId("-//Ben Fortuna//iCal4j 1.0//EN"));
		calendar.getProperties().add(Version.VERSION_2_0);
		calendar.getProperties().add(CalScale.GREGORIAN);

		SimpleDateFormat sdFormat = new SimpleDateFormat("yyyyMMdd'T'hhmmss'Z'");
		String strDate = sdFormat.format(startCal.getTime());

		net.fortuna.ical4j.model.Date startDt = null;
		try {
			startDt = new net.fortuna.ical4j.model.Date(strDate);
		} catch (ParseException e) {
			e.printStackTrace();
		}

		long diff = endCal.getTimeInMillis() - startCal.getTimeInMillis();
		int min = (int) (diff / (1000 * 60));

		Dur dur = new Dur(0, 0, min, 0);

		// Creating a meeting event
		VEvent meeting = new VEvent(startDt, dur, subject);

		// This is where you would add a location if there was one
		// meeting.getProperties().add(new Location(location));
		meeting.getProperties().add(new Description());

		try {
			meeting.getProperties().getProperty(Property.DESCRIPTION).setValue(description);
		} catch (IOException e) {
			e.printStackTrace();
		} catch (URISyntaxException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			e.printStackTrace();
		}

		// try {
		// meeting.getProperties().add(new Organizer(null, "MAILTO:" +
		// hostEmail));
		// } catch (URISyntaxException e) {
		// e.printStackTrace();
		// }

		calendar.getComponents().add(meeting);

		FileOutputStream fout = null;

		try {
			fout = new FileOutputStream(calFile);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

		CalendarOutputter outputter = new CalendarOutputter();
		outputter.setValidating(false);

		try {
			outputter.output(calendar, fout);
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ValidationException e) {
			e.printStackTrace();
		}

		System.out.println(meeting);

	}

}
