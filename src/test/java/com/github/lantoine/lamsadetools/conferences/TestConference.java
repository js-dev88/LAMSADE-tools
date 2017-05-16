package com.github.lantoine.lamsadetools.conferences;

import static org.junit.Assert.fail;

import java.text.ParseException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

import org.junit.Test;

import com.github.lantoine.lamsadetools.conferences.Conference;

public class TestConference {

	@Test
	public void equals() throws ParseException {
		String dateFormat = "dd/MM/yyyy";
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern(dateFormat);
		dtf.withLocale(Locale.FRANCE);
		Conference conf1 = new Conference("Antoine s conf", "url", LocalDate.parse("10/03/2017", dtf),
				LocalDate.parse("11/03/2017", dtf), 0, "madrid", "47 rue victor hugo");
		Conference conf2 = new Conference("Antoine s conf", "url", LocalDate.parse("10/03/2017", dtf),
				LocalDate.parse("11/03/2017", dtf), 0, "madrid", "47 rue victor hugo");
		if (!conf1.equals(conf2)) {
			fail("Equals not functionning");
		}

	}

}
