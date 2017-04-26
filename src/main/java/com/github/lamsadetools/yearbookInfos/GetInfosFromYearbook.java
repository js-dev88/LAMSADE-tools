package com.github.lamsadetools.yearbookInfos;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.jdom2.JDOMException;

import com.github.lamsadetools.setCoordinates.UserDetails;
import com.sun.star.lang.IllegalArgumentException;

/**
 * GetInfosFromYearbook will get information from Dauphine's yearbook It needs
 * the name and the surname of the person you want to get information.
 *
 * @author Abdelkader ZEROUALI, Antony Entremont, Julien Saussier
 *
 */
public class GetInfosFromYearbook {

	final static Logger LOGGER = Logger.getLogger(GetInfosFromYearbook.class);
	// Set the properties file's path
	static final String path = "src/main/resources/com/github/lamsadetools/log4j.properties";

	/**
	 * Ask the user for his name and find his informations
	 *
	 * @return return a UserDetails filled with the informations from Dauphine's
	 *         yearbook
	 * @throws Throwable
	 */
	public static UserDetails getUserDetails() throws Throwable {
		Scanner sc = new Scanner(System.in);
		System.out.println("Name?:");
		String name = sc.nextLine();
		System.out.println("FirstName?:");
		String first_name = sc.nextLine();
		return getUserDetails(name, first_name);
	}

	/**
	 * find informations of the person in parameter
	 *
	 * @param name
	 * @param firstname
	 * @return return a UserDetails filled with the informations from Dauphine's
	 *         yearbook
	 * @throws Throwable
	 */
	public static UserDetails getUserDetails(String name, String firstname) throws Throwable {
		GetInfosFromYearbook prof = new GetInfosFromYearbook(firstname, name);
		UserDetails user = new UserDetails(name, firstname, prof.getFonction(), prof.getTelephone(),
				prof.getCourrier());
		return user;

	}

	public static void main(String[] args) throws Throwable {
		// path configuration for logger
		PropertyConfigurator.configure(path);

		try {
			String prenom = "Olivier";
			String nom = "CAILLOUX";
			GetInfosFromYearbook profJava = new GetInfosFromYearbook(prenom, nom);
			System.out.println("info profjava:" + profJava.getBureau());
			LOGGER.info("\n" + profJava.toString());
		} catch (Exception e) {
			// the message of the original exception is displayed
			LOGGER.error("Error : ", e);
			throw e;
		}
	}

	// HashMap collection register informations with a key and an associated
	// value
	private HashMap<String, String> informations = new HashMap<String, String>();

	/**
	 * Constructor using a person's firstname and surname
	 *
	 * @param firstname
	 *            of the person may not be null
	 * @param surname
	 *            of the person not be null
	 * @throws Throwable
	 */
	public GetInfosFromYearbook(String firstname, String surname) throws Throwable {
		if (firstname == null || surname == null) {
			throw new IllegalArgumentException("Firstname or surname is null");
		}
		String htmlPage;
		try {
			ConnectionToYearbook newConnection = new ConnectionToYearbook(firstname, surname);
			htmlPage = newConnection.getHtmlPage();
			retrieveYearbookData(htmlPage);
		} catch (Throwable t) {
			LOGGER.error(t);
			throw t;
		}

	}

	public String getBureau() {
		return informations.get("Bureau");
	}

	public String getCourrier() {
		return informations.get("Courriel");
	}

	public String getFax() {
		return informations.get("Fax");
	}

	public String getFonction() {
		return informations.get("Fonction");
	}

	public String getGroupes() {
		return informations.get("Groupes");
	}

	public String getTelephone() {
		return informations.get("Téléphone");
	}

	/**
	 * Construct the HasMap collections with proper label and values
	 *
	 * @param rawInfos
	 *            contains lines with labels and lines with corresponding data
	 * @throws IllegalArgumentException
	 *             if rawInfo is null, this indicates that the firstname or the
	 *             surname is wrong, no data is found or the original HTML page
	 *             is a 404.
	 * @throws Exception
	 *             if the Hashmap contains no data
	 * @return the HAshMap with all the person's informations
	 */
	private HashMap<String, String> hashMapConstructor(ArrayList<String> rawInfos) throws IllegalArgumentException {
		if (rawInfos.isEmpty() || rawInfos.size() == 0) {
			throw new IllegalArgumentException("Wrong parameters or site is unreachable");
		}
		int j = 0;
		while (j < rawInfos.size()) {
			informations.put(rawInfos.get(j), rawInfos.get(j + 1));
			j += 2;
		}
		return informations;
	}

	/**
	 * Put all the person's info in a Hashmap with labels for keys and data for
	 * values
	 *
	 * @param htmlText
	 *            is the yearBook's page in HTML format
	 * @throws IOException
	 *             if Nextline function fails
	 * @throws IllegalArgumentException
	 *             from hashMapConstructor
	 */
	public void retrieveYearbookData(String htmlText) throws IOException, IllegalArgumentException, JDOMException {
		/*
		 * SAXBuilder sb = new SAXBuilder(); try { Document doc =
		 * sb.build(htmlText); } catch (JDOMException e) { throw new
		 * JDOMException("XML from String Error"); }
		 */
		String line = null;
		String nextLine = null;
		ArrayList<String> rawInfos = new ArrayList<String>();

		// read each line and add it into rawInfos
		try (BufferedReader br = new BufferedReader(new StringReader(htmlText))) {
			while ((line = br.readLine()) != null) {
				if (line.contains("class=\"label\"")) { // class ="label"
														// contains the name of
														// the needed info
					rawInfos.add(line);
					boolean found = false;
					while ((nextLine = br.readLine()) != null && found != true) {
						if (nextLine.contains("</li>")) {
							// LOGGER.info("TEST NEXTLINE : " +nextLine);
							rawInfos.add(nextLine);
							found = true;
						}
					}
				}
			}
		} catch (IOException e2) {
			throw new IOException("Error when trying to read Nextline");
		}
		superfluousRemover(rawInfos);
		hashMapConstructor(rawInfos);

	}

	/**
	 * superfluousRemover remove HTML tags and useless spaces (beginning and
	 * end) in an ArrayList of Strings
	 *
	 * @param infos
	 *            is filled with raw HTML lines
	 */
	private void superfluousRemover(ArrayList<String> infos) {
		for (int i = 0; i < infos.size(); ++i) {
			infos.set(i, infos.get(i).replaceAll("<[^>]+>", "")); // delete
																	// every
																	// HTML tags
			infos.set(i, infos.get(i).trim()); // delete every spaces
												// surrounding the string
		}
	}

	@Override
	public String toString() {
		String toString = "";
		for (String i : informations.keySet()) {
			toString += i + ": " + informations.get(i) + "\n";
		}
		return toString;
	}
}
