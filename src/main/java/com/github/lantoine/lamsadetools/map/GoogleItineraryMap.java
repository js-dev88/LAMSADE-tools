package com.github.lantoine.lamsadetools.map;

import java.awt.Desktop;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import javax.xml.parsers.ParserConfigurationException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.SAXException;

/**
 * ItineraryMap allows you to open an itinerary into your browser on google's
 * page.
 *
 */
public class GoogleItineraryMap {
	private static final Logger LOGGER = LoggerFactory.getLogger(GoogleItineraryMap.class);

	private String origin;
	private String destination;

	/**
	 * GoogleItineraryMap needs the origin and the destination addresses
	 *
	 * @param origin
	 * @param destination
	 */
	public GoogleItineraryMap(String origin, String destination) {
		this.origin = origin;
		this.destination = destination;
	}

	/**
	 * ItineraryMap needs two AddressInfos.
	 *
	 *
	 * @param longitudeA
	 * @param latitudeA
	 * @param longitudeB
	 * @param latitudeB
	 */
	public GoogleItineraryMap(AddressInfos origin, AddressInfos destination) {
		this.origin = origin.getRawAddress();
		this.destination = destination.getRawAddress();
	}

	/**
	 * This method sets the google url according to the origin and destination
	 * addresses
	 *
	 * @return url
	 */
	public String setMapUrl() {
		String formattedOrigin = formatAddress(origin);
		String formattedDestination = formatAddress(destination);
		String url = "https://www.google.com/maps?q=" + formattedOrigin + "+to+" + formattedDestination;

		return url;
	}

	public String formatAddress(String address) {
		return address.trim().replaceAll(" ", "+");
	}

	/**
	 * Open an URL into the default browser
	 *
	 * @param url
	 *            (must be a String)
	 */
	public void openMapUrl(String url) {
		if (Desktop.isDesktopSupported()) {
			try {
				Desktop.getDesktop().browse(new URI(url));
			} catch (IOException | URISyntaxException e) {
				throw new IllegalStateException(e);
			}
		}
	}

	public void setOrigin(String origin) {
		this.origin = origin;
	}

	public void setDestination(String destination) {
		this.destination = destination;
	}

	public String getOrigin() {
		return origin;
	}

	public String getDestination() {
		return destination;
	}

	public static void main(String[] args) throws Exception {

		String rawAdressA = "Paris";
		String rawAdressB = "Cologne";
		AddressInfos paris = new AddressInfos(rawAdressA);
		try {
			paris.retrieveGeocodeResponse();
		} catch (java.lang.IllegalArgumentException | IOException | SAXException | ParserConfigurationException e) {
			throw new IllegalStateException(e);
		}
		AddressInfos cologne = new AddressInfos(rawAdressB);
		try {
			cologne.retrieveGeocodeResponse();
		} catch (java.lang.IllegalArgumentException | IOException | SAXException | ParserConfigurationException e) {
			throw new IllegalStateException(e);
		}
		GoogleItineraryMap parisCologne = new GoogleItineraryMap(paris.getRawAddress(), cologne.getRawAddress());
		LOGGER.info("\n" + paris.toString());
		LOGGER.info("\n" + cologne.toString());
		String url = parisCologne.setMapUrl();
		System.out.println(url);
		parisCologne.openMapUrl(url);

	}
}
