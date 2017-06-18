package com.github.lantoine.lamsadetools.yearbookInfos;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Scanner;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

import com.github.lantoine.lamsadetools.setCoordinates.UserDetails;
import com.sun.star.lang.IllegalArgumentException;

/**
 * GetInfosFromYearbook will get information from Dauphine's yearbook It needs
 * the name and the surname of the person you want to get information. To
 * retrive the information
 * 
 * @author Abdelkader ZEROUALI, Antony Entremont, Julien Saussier
 *
 */
public class GetInfosFromYearbook {

	private static final Logger logger = LoggerFactory.getLogger(GetInfosFromYearbook.class);

	private String firstname;

	public String getFirstname() {
		return firstname;
	}

	public String getSurname() {
		return surname;
	}

	public String getLogin() {
		return login;
	}

	private String surname;
	private String login;

	// HashMap collection register informations with a key and an associated
	// value
	private HashMap<String, String> informations = new HashMap<>();

	/**
	 * Constructor using a person's login
	 *
	 * @param login
	 *            of the person not be null
	 * @throws Throwable
	 */
	public GetInfosFromYearbook(String login) throws IllegalArgumentException {
		if (login == null) {
			throw new IllegalArgumentException("login is null");
		}
		this.login = login;

	}

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

	/**
	 * Make connection to the yearbook and retrieve an html page Put all the
	 * person's info in a Hashmap with labels for keys and data for values
	 * 
	 * @param htmlText
	 *            is the yearBook's page in HTML format
	 * @throws IOException
	 *             if Nextline function fails
	 * @throws IllegalArgumentException
	 *             from hashMapConstructor
	 * @throws YearbookDataException
	 *             from ConnectionToYearbook
	 * @throws SAXException
	 * @throws ParserConfigurationException
	 */
	public void retrieveYearbookData() throws IOException, IllegalArgumentException, YearbookDataException,
			SAXException, ParserConfigurationException {
		DocumentBuilderFactory factory = null;
		DocumentBuilder builder = null;
		Document htmlDoc = null;

		// Yearbook connection
		ConnectionToYearbook connection;
		if (login == null)
			connection = new ConnectionToYearbook(firstname, surname);
		else
			connection = new ConnectionToYearbook(login);
		connection.buildConnection();
		factory = DocumentBuilderFactory.newInstance();
		builder = factory.newDocumentBuilder();

		try (InputStream htmlText = connection.getHtmlPage()) {
			htmlDoc = builder.parse(new InputSource(htmlText));
		} catch (SAXParseException e) {
			logger.debug(e.getMessage());
			throw new YearbookDataException("Error 404 you should verify parameters");
		}

		NodeList h3 = htmlDoc.getElementsByTagName("h3");

		// Verify parameters
		if (h3.getLength() != 0) {
			for (int i = 0; i < h3.getLength(); i++) {
				if (h3.item(i).getNodeValue() == "Erreur 404") {
					throw new YearbookDataException("404 Error - Wrong name or surname");
				}
			}

		}
		// If parameters are valid create a Hashmap with Professor's information

		String category = "";
		String info = "";

		// This is useful only if we don't have the name and firstname
		if (surname == null && firstname == null) {
			Node h4 = htmlDoc.getElementsByTagName("h4").item(0);
			String nameSurnameRaw = h4.getTextContent();
			String[] nameSurname = nameSurnameRaw.split(" ");
			surname = "";
			firstname = "";
			for (int i = 0; i < nameSurname.length; ++i) {
				if (nameSurname[i] == nameSurname[i].toUpperCase()) {
					surname += nameSurname[i] + " ";
				} else {
					firstname += nameSurname[i] + " ";
				}
			}
		}

		NodeList ulList = htmlDoc.getElementsByTagName("ul");

		for (int i = 0; i < ulList.getLength(); i++) {
			category = "";
			info = "";
			NodeList listOfLi = ulList.item(i).getChildNodes();
			for (int j = 0; j < listOfLi.getLength(); j++) {
				if (listOfLi.item(j).getNodeName() == "li") {
					Element li = (Element) listOfLi.item(j);
					if (li.getAttribute("class").equals("label")) {
						logger.debug("cat: " + li.getTextContent().trim());
						category = li.getTextContent().trim();
					} else if (li.getAttribute("class").equals("value")) {
						info = li.getTextContent().trim();
					}
				}
				informations.put(category, info);
			}

		}
	}

	/**
	 * @return value if exists, empty otherwise
	 */
	public String getBureau() {
		if (informations.get("Bureau") == null)
			return "";
		else
			return informations.get("Bureau");
	}

	/**
	 * @return value if exists, empty otherwise
	 */
	public String getCourrier() {
		if (informations.get("Courriel") == null)
			return "";
		else
			return informations.get("Courriel");
	}

	/**
	 * @return value if exists, empty otherwise
	 */
	public String getFax() {
		if (informations.get("Fax") == null)
			return "";
		else
			return informations.get("Fax");
	}

	/**
	 * @return value if exists, empty otherwise
	 */
	public String getFonction() {
		if (informations.get("Fonction") == null)
			return "";
		else
			return informations.get("Fonction");
	}

	/**
	 * @return value if exists, empty otherwise
	 */
	public String getGroupes() {
		if (informations.get("Groupes") == null)
			return "";
		else
			return informations.get("Groupes");
	}

	/**
	 * @return value if exists, empty otherwise
	 */
	public String getTelephone() {
		if (informations.get("Téléphone") == null)
			return "";
		else
			return informations.get("Téléphone");
	}

	/**
	 * Ask the user for his name and find his informations
	 * 
	 * @return return a UserDetails filled with the informations from Dauphine's
	 *         yearbook
	 * @throws YearbookDataException
	 * @throws IOException
	 * @throws ParserConfigurationException
	 * @throws SAXException
	 * @throws Throwable
	 */
	/*
	 * No Upperletter for a function only for constructor, please report this
	 * code in the UserDetails class or any class from setcoordinates
	 */
	public static UserDetails getUserDetails() throws IllegalArgumentException, IOException, YearbookDataException,
			SAXException, ParserConfigurationException {
		try (Scanner sc = new Scanner(System.in)) {
			System.out.println("Name?:");
			String name = sc.nextLine();
			System.out.println("FirstName?:");
			String first_name = sc.nextLine();
			return getUserDetails(name, first_name);
		}

	}

	/**
	 * find informations of the person in parameter
	 * 
	 * @param name
	 * @param firstname
	 * @return return a UserDetails filled with the informations from Dauphine's
	 *         yearbook
	 * @throws IOException
	 * @throws YearbookDataException
	 * @throws ParserConfigurationException
	 * @throws SAXException
	 * @throws Throwable
	 */
	/*
	 * No Upperletter for a function only for constructor Userdetails
	 * instanciation must be in the setcoordinates package
	 */
	public static UserDetails getUserDetails(String name, String firstname) throws IllegalArgumentException,
			IOException, YearbookDataException, SAXException, ParserConfigurationException {
		GetInfosFromYearbook prof = new GetInfosFromYearbook(firstname, name);
		prof.retrieveYearbookData();
		UserDetails user = new UserDetails(name, firstname, prof.getFonction(), prof.getTelephone(), prof.getCourrier(),
				prof.getGroupes(), prof.getFax(), prof.getBureau(), "Paris", "France");
		return user;

	}

	/**
	 * find informations of the person in parameter
	 * 
	 * @param login
	 * @return return a UserDetails filled with the informations from Dauphine's
	 *         yearbook
	 * @throws IOException
	 * @throws YearbookDataException
	 * @throws ParserConfigurationException
	 * @throws SAXException
	 * @throws Throwable
	 */
	/*
	 * No Upperletter for a function only for constructor Userdetails
	 * instanciation must be in the setcoordinates package
	 */
	public static UserDetails getUserDetails(String login) throws IllegalArgumentException, IOException,
			YearbookDataException, SAXException, ParserConfigurationException {
		GetInfosFromYearbook prof = new GetInfosFromYearbook(login);
		prof.retrieveYearbookData();
		UserDetails user = new UserDetails(prof.surname, prof.firstname, prof.getFonction(), prof.getTelephone(),
				prof.getCourrier(), prof.getGroupes(), prof.getFax(), prof.getBureau(), "Paris", "France");
		return user;

	}

	public static void main(String[] args) throws IllegalArgumentException, IOException, YearbookDataException,
			SAXException, ParserConfigurationException {
		String prenom = "Jerome";
		String nom = "Lang";
		UserDetails user = GetInfosFromYearbook.getUserDetails(nom, prenom);
		System.out.println(user.getName());
		System.out.println(user.getFirstName());
		logger.info("Informations sur l'objet GIFYB :\n" + user.toString());

		String login = "jlang";
		UserDetails user2 = GetInfosFromYearbook.getUserDetails(login);
		System.out.println("user2 " + user2.getName());
		System.out.println("user2 " + user2.getFirstName());
		// GetInfosFromYearbook profJava = new GetInfosFromYearbook(prenom,
		// nom);
		// profJava.retrieveYearbookData();
		// logger.info("info profjava:" + profJava.getBureau());
		logger.info("Informations sur l'objet GIFYB :\n" + user2.toString());

		GetInfosFromYearbook prof1 = new GetInfosFromYearbook(prenom, nom);
		prof1.retrieveYearbookData();
		System.out.println(prof1.firstname + " " + prof1.surname + " " + prof1.login);
		GetInfosFromYearbook prof2 = new GetInfosFromYearbook(login);
		prof2.retrieveYearbookData();
		System.out.println(prof2.firstname + " " + prof2.surname + " " + prof2.login);

	}
}
