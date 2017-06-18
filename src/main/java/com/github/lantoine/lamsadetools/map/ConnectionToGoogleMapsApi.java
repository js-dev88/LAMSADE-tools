package com.github.lantoine.lamsadetools.map;

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

/**
 * This class let the application connect to maps.googleapis By entering an
 * address you'll be able query maps.googleapis and the hmtl page of your query.
 *
 * @author Julien Saussier
 *
 */
public class ConnectionToGoogleMapsApi {
	@SuppressWarnings("unused")
	private static final Logger logger = LoggerFactory.getLogger(ConnectionToGoogleMapsApi.class);

	// html page from maps.googleapis
	private File htmlPageFile;
	private InputStream htmlPage;
	private String rawAddress;

	/**
	 * ConnectionToGoogleMapsApi's Constructor
	 * 
	 * @param rawAddress
	 *            may not be null
	 * @throws IllegalArgumentException
	 *             if null parameter
	 */
	public ConnectionToGoogleMapsApi(String rawAddress) throws IllegalArgumentException {

		if (rawAddress == null) {
			throw new IllegalArgumentException("rawAddress is null");
		}
		this.rawAddress = rawAddress;
	}

	/**
	 * Make the connection to google maps api from an address
	 * 
	 * @throws IllegalArgumentException
	 *             if webtarget is null
	 * @throws NullPointerException
	 *             if no data found
	 * @throws FileNotFoundException
	 */
	public void buildConnection() throws IllegalArgumentException, NullPointerException, FileNotFoundException {
		// We define the parameter of the query
		String param = rawAddress;
		// This part of the url is always the same
		String urlConstantPart = "http://maps.googleapis.com/maps/api/geocode/xml?sensor=true";
		// First step of the JAX rs class => Client initialization
		Client client = ClientBuilder.newClient();
		// Build the targeted URL
		WebTarget t1 = client.target(urlConstantPart).queryParam("address", param);
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
			throw new NullPointerException("htmlPage has no data, google maps api may be down");
		}
		logger.debug("Google maps api successfully targeted");
		htmlPage = new FileInputStream(htmlPageFile);
		return htmlPage;

	}

}
