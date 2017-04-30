package com.github.lamsadetools.yearbookInfos;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


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

	@SuppressWarnings("unused")
	private static final Logger logger = LoggerFactory.getLogger(GetInfosFromYearbook.class);

	private String firstname;
	private String surname;

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
	public GetInfosFromYearbook(String firstname, String surname) throws IllegalArgumentException {
		if (firstname == null || surname == null) {
			throw new IllegalArgumentException("Firstname or surname is null");
		}
		this.firstname = firstname;
		this.surname = surname;

	}
	
	
	
	@Override
	public String toString() {
		String toString = "";
		for (String i : informations.keySet()) {
			toString += i + ": " + informations.get(i) + "\n";
		}
		return toString;
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
	 * @param rawInfos contains lines with labels and lines with corresponding data
	 * @throws Exception if the Hashmap contains no data
	 * @return the HAshMap with all the person's informations
	 */
	private HashMap<String, String> hashMapConstructor(ArrayList<String> rawInfos) {
		int j = 0;
		while (j < rawInfos.size()) {
			informations.put(rawInfos.get(j), rawInfos.get(j + 1));
			j += 2;
		}
		return informations;
	}

	/**
	 * Make connection to the yearbook and retrieve an html page
	 * Put all the person's info in a Hashmap with labels for keys and data for values
	 * @param htmlText is the yearBook's page in HTML format
	 * @throws IOException if Nextline function fails
	 * @throws IllegalArgumentException from hashMapConstructor
	 */
	public void retrieveYearbookData() throws IOException, IllegalArgumentException {
		
		//Yearbook connection
		ConnectionToYearbook connection = new ConnectionToYearbook(firstname, surname);
		connection.buildConnection();
		String htmlText = connection.getHtmlPage();
		
		
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
		if (rawInfos.isEmpty() || rawInfos.size() == 0) {
			if (htmlText.contains("<h4>Annuaire - page non trouvée</h4"))
				System.out.println("No page matching this name has been found");
			else
				throw new IllegalArgumentException("Wrong parameters or site is unreachable");
		}
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

	
	
	
	/**
	 * Ask the user for his name and find his informations
	 * @return return a UserDetails filled with the informations from Dauphine's yearbook
	 * @throws Throwable
	 */
	public static UserDetails getUserDetails() throws IllegalArgumentException {
		Scanner sc = new Scanner(System.in);
		System.out.println("Name?:");
		String name = sc.nextLine();
		System.out.println("FirstName?:");
		String first_name = sc.nextLine();
		return getUserDetails(name, first_name);
	}

	/**
	 * find informations of the person in parameter
	 * @param name
	 * @param firstname
	 * @return return a UserDetails filled with the informations from Dauphine's yearbook
	 * @throws Throwable
	 */
	public static UserDetails getUserDetails(String name, String firstname) throws IllegalArgumentException {
		GetInfosFromYearbook prof = new GetInfosFromYearbook(firstname, name);
		UserDetails user = new UserDetails(name, firstname, prof.getFonction(), prof.getTelephone(),
				prof.getCourrier());
		return user;

	}

	public static void main(String[] args) throws IllegalArgumentException {
			String prenom = "Olivier";
			String nom = "CAILLOUX";
			GetInfosFromYearbook profJava = new GetInfosFromYearbook(prenom, nom);
			profJava.retrieveYearbookData();
			logger.info("info profjava:" + profJava.getBureau());
			logger.info("Informations sur l'objet GIFYB :\n" + profJava.toString());
	}
}
