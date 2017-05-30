package com.github.lantoine.lamsadetools.conferences;

import static org.junit.Assert.fail;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

import org.junit.Test;

import net.fortuna.ical4j.data.ParserException;
import net.fortuna.ical4j.model.ValidationException;

public class TestConference {

	@Test
	public void equals() {
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

	@Test
	public void testHashCode() {
		/*
		 * Whenever it is invoked on the same object more than once during an
		 * execution of a Java application, the hashCode method must
		 * consistently return the same integer, provided no information used in
		 * equals comparisons on the object is modified. This integer need not
		 * remain consistent from one execution of an application to another
		 * execution of the same application.
		 */
		String dateFormat = "dd/MM/yyyy";
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern(dateFormat);
		dtf.withLocale(Locale.FRANCE);
		Conference conf1 = new Conference("Antoine s conf", "url", LocalDate.parse("10/03/2017", dtf),
				LocalDate.parse("11/03/2017", dtf), 0, "madrid", "47 rue victor hugo");
		int hash1 = conf1.hashCode();
		int hash2 = conf1.hashCode();
		if (hash1 != hash2) {
			fail("Hascode not functionning : integer returned not consistent over 2 executions");
		}
		/*
		 * If two objects are equal according to the equals(Object) method, then
		 * calling the hashCode method on each of the two objects must produce
		 * the same integer result.
		 */
		Conference conf2 = new Conference("Antoine s conf", "url", LocalDate.parse("10/03/2017", dtf),
				LocalDate.parse("11/03/2017", dtf), 0, "madrid", "47 rue victor hugo");
		assert conf1.equals(conf2);

		if (hash1 != conf2.hashCode()) {
			fail("Hascode not functionning : equals objects do not have the same hashcode");
		}
	}

	@Test
	public void generateCalendarFile() throws IOException, ValidationException, ParserException {
		String dateFormat = "dd/MM/yyyy";
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern(dateFormat);
		dtf.withLocale(Locale.FRANCE);
		Conference conf1 = new Conference("Antoine s conf", "url", LocalDate.parse("10/03/2017", dtf),
				LocalDate.parse("11/03/2017", dtf), 0, "madrid", "47 rue victor hugo");
		conf1.generateCalendarFile("toto");
	}
}
