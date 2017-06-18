package com.github.lantoine.lamsadetools.yearbookInfos;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sun.star.lang.IllegalArgumentException;

/**
 * This class let the application connect to a professor in the Yearbook You can
 * access to the html page in a String format from another class
 *
 * @author Julien Saussier
 *
 */
public class ConnectionToYearbook {

	private static final Logger logger = LoggerFactory.getLogger(ConnectionToYearbook.class);

	// html page from Yearbook
	private File htmlPageFile;
	private InputStream htmlPage;
	private String firstname;
	private String surname;
	private String login;

	/**
	 * ConnectionToYearbook's Constructor
	 * 
	 * @param firstname
	 *            may not be null
	 * @param surname
	 *            may not be null
	 * @throws IllegalArgumentException
	 *             if null parameters
	 */
	public ConnectionToYearbook(String firstname, String surname) throws IllegalArgumentException {

		if (firstname == null || surname == null) {
			throw new IllegalArgumentException("Firstname or surname is null");
		}
		this.firstname = firstname;
		this.surname = surname;

	}
	
	public ConnectionToYearbook(String login) throws IllegalArgumentException {
		if(login ==null){
			throw new IllegalArgumentException("login is null");
		}
		this.login = login;
	}

	/**
	 * Make the connection to the yearbook from a firstname and a surname
	 * 
	 * @param firstname
	 *            may not be null
	 * @param surname
	 *            may not be null
	 * @throws IllegalArgumentException
	 *             if webtarget is null
	 * @throws NullPointerException
	 *             if no data found
	 * @throws FileNotFoundException
	 */
	public void buildConnection() throws IllegalArgumentException, NullPointerException, FileNotFoundException {
		// Build the URL parameter with the firstname's first letter and the
		// person's surname
		String param = firstname.toLowerCase().charAt(0) + surname.toLowerCase();
		String param0 = "fiche";
		// This part of the url is always the same
		String urlConstantPArt = "https://www.ent.dauphine.fr/Annuaire/index.php?";
		// First step of the JAX rs class => Client initialization
		Client client = ClientBuilder.newClient();
		// Build the targeted URL
		WebTarget t1 = client.target(urlConstantPArt).queryParam("param0", param0).queryParam("param1", param);
		// The entire HTML page is stocked in result
		htmlPageFiller(t1);
		client.close();
		logger.debug("Connection Established...");
	}

	/**
	 * @return the htmlPage in a string format
	 */

	public InputStream getHtmlPage() {
		logger.debug("HTML Page is send");
		return htmlPage;
	}

	/**
	 *
	 * @param webtrgt
	 *            is the webtarget (url with parameters)
	 * @return a filled html page File
	 * @throws IllegalArgumentException
	 *             if webtarget is null
	 * @throws NullPointerException
	 *             if no data found
	 * @throws FileNotFoundException
	 */
	private InputStream htmlPageFiller(WebTarget webtrgt)
			throws IllegalArgumentException, NullPointerException, FileNotFoundException {

		if (webtrgt == null) {
			throw new IllegalArgumentException("Web Target is null, error during the target construction");
		}

		htmlPageFile = webtrgt.request(MediaType.TEXT_HTML_TYPE).get(File.class);

		if (htmlPageFile == null) {
			throw new NullPointerException("htmlPage has no data, the yearbook site may be down");
		} 
		logger.debug("Yearbook successfully targeted");
		htmlPage = new FileInputStream(htmlPageFile);
		return htmlPage;
		

	}

}
