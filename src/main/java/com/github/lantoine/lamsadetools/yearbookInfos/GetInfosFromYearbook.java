package com.github.lantoine.lamsadetools.yearbookInfos;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Scanner;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.lantoine.lamsadetools.setCoordinates.UserDetails;
import com.sun.star.lang.IllegalArgumentException;

/**
 * GetInfosFromYearbook will get information from Dauphine's yearbook It needs
 * the name and the surname of the person you want to get information.
 * To retrive the information
 * @author Abdelkader ZEROUALI, Antony Entremont, Julien Saussier
 *
 */
public class GetInfosFromYearbook {

	@SuppressWarnings("unused")
	private static final Logger logger = LoggerFactory.getLogger(GetInfosFromYearbook.class);

	private String firstname;
	private String surname;

	// HashMap collection register informations with a key and an associated value
	private HashMap<String, String> informations = new HashMap<String, String>();

	/**
	 * Constructor using a person's firstname and surname
	 *
	 * @param firstname of the person may not be null
	 * @param surname of the person not be null
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
	 * Make connection to the yearbook and retrieve an html page
	 * Put all the person's info in a Hashmap with labels for keys and data for values
	 * @param htmlText is the yearBook's page in HTML format
	 * @throws IOException if Nextline function fails
	 * @throws IllegalArgumentException from hashMapConstructor
	 * @throws YearbookDataException 
	 */
	public void retrieveYearbookData() throws IOException, IllegalArgumentException, YearbookDataException {
		
		//Yearbook connection
		ConnectionToYearbook connection = new ConnectionToYearbook(firstname, surname);
		connection.buildConnection();
		File htmlText = connection.getHtmlPage();
		
		//Instanciate a jsoup document
		org.jsoup.nodes.Document jsoupDoc = Jsoup.parse(htmlText, StandardCharsets.UTF_8.name());
        //Verify parameters
		if(!jsoupDoc.getElementsByTag("h3").isEmpty()){
			if(jsoupDoc.getElementsByTag("h3").text() == "Erreur 404"){
				throw new YearbookDataException("404 Error - Wrong name or surname");
			}
		}
		//If parameters are valid create a Hashmap with Professor's information
		Elements ulList = jsoupDoc.getElementsByTag("ul");

		for(Element ul : ulList){
			String category ="";
			String info ="";
			Elements listOfli = ul.children();
			for(Element li : listOfli){
				if(li.hasClass("label")){
					category = li.text().trim();
				}else if(li.hasClass("value")){
					info = li.text().trim();
				}
				informations.put(category, info);
				
			}
			
			
		}

	    if(informations.size() > 7){
	    	throw new YearbookDataException("Too many rows in Hashmap");
	    }else if(informations.size() < 7){
	    	throw new YearbookDataException("Some yearbook's informations are missing");
	    }
		logger.debug("Professor's Informations are ready to be exploited...");
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
	 * Ask the user for his name and find his informations
	 * @return return a UserDetails filled with the informations from Dauphine's yearbook
	 * @throws YearbookDataException 
	 * @throws IOException 
	 * @throws Throwable
	 */
	/*No Upperletter for a function only for constructor, 
	 * please report this code in the UserDetails class or any class from setcoordinates
	 */
	public static UserDetails getUserDetails() throws IllegalArgumentException, IOException, YearbookDataException {
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
	 * @throws IOException 
	 * @throws YearbookDataException 
	 * @throws Throwable
	 */
	/*No Upperletter for a function only for constructor
	  Userdetails instanciation must be in the setcoordinates package*/
	public static UserDetails getUserDetails(String name, String firstname) throws IllegalArgumentException, IOException, YearbookDataException {
		GetInfosFromYearbook prof = new GetInfosFromYearbook(firstname, name);
		prof.retrieveYearbookData();
		UserDetails user = new UserDetails(name, firstname, prof.getFonction(), prof.getTelephone(),
				prof.getCourrier());
		return user;

	}

	public static void main(String[] args) throws IllegalArgumentException, IOException, YearbookDataException {
			String prenom = "Olivier";
			String nom = "CAILLOUX";
		
			GetInfosFromYearbook profJava = new GetInfosFromYearbook(prenom, nom);
			profJava.retrieveYearbookData();
			logger.info("info profjava:" + profJava.getBureau());
			logger.info("Informations sur l'objet GIFYB :\n" + profJava.toString());
	}
}
