package com.github.lamsadetools.conferences;

import java.time.LocalDate;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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

	@SuppressWarnings("unused")
	private static final Logger logger = LoggerFactory.getLogger(Conference.class);

	private LocalDate end_date;

	private double entry_fee;

	private int id;

	private LocalDate start_date;

	private String title;

	private String url;

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
				+ end_date + ", entry_fee=" + entry_fee + "]";
	}

}
