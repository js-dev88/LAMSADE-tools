package com.github.lantoine.lamsadetools.map;

import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.SAXException;

import com.github.lantoine.lamsadetools.utils.Util;

/**
 * ItineraryMap allows you to open an itinerary into your browser on
 * openstreetmap's page.
 *
 */
public class ItineraryMap {
	private static final Logger LOGGER = LoggerFactory.getLogger(ItineraryMap.class);

	private String longitudeA;
	private String latitudeA;
	private String longitudeB;
	private String latitudeB;

	/**
	 * ItineraryMap needs the longitude and the latitude of the departure and
	 * arrival points.
	 *
	 *
	 * @param longitudeA
	 * @param latitudeA
	 * @param longitudeB
	 * @param latitudeB
	 */
	public ItineraryMap(String longitudeA, String latitudeA, String longitudeB, String latitudeB) {
		this.latitudeA = latitudeA;
		this.longitudeA = longitudeA;
		this.latitudeB = latitudeB;
		this.longitudeB = longitudeB;
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
	public ItineraryMap(AddressInfos addressA, AddressInfos addressB) {
		latitudeA = addressA.getLatitude();
		longitudeA = addressA.getLongitude();
		latitudeB = addressB.getLatitude();
		longitudeB = addressB.getLongitude();
	}

	/**
	 * This method sets the openstreetmap url according to the longitudes and
	 * latitudes entered in the class attributes
	 *
	 * @return url (a String)
	 */
	public String setMapUrl() {
		String url = "http://www.openstreetmap.org/directions?engine=osrm_car&route=" + latitudeA + "%2C" + longitudeA
				+ "%3B" + latitudeB + "%2C" + longitudeB;
		return url;
	}

	/**
	 * Open an URL into the default browser
	 *
	 * @param url
	 *            (must be a String)
	 */
	public void openMapUrl(String url) {
		Util.openMapUrl(url);
	}

	public String getLongitudeA() {
		return longitudeA;
	}

	public void setLongitudeA(String longitudeA) {
		this.longitudeA = longitudeA;
	}

	public String getLatitudeA() {
		return latitudeA;
	}

	public void setLatitudeA(String latitudeA) {
		this.latitudeA = latitudeA;
	}

	public String getLongitudeB() {
		return longitudeB;
	}

	public void setLongitudeB(String longitudeB) {
		this.longitudeB = longitudeB;
	}

	public String getLatitudeB() {
		return latitudeB;
	}

	public void setLatitudeB(String latitudeB) {
		this.latitudeB = latitudeB;
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
		ItineraryMap parisCologne = new ItineraryMap(paris.getLongitude(), paris.getLatitude(), cologne.getLongitude(),
				cologne.getLatitude());
		LOGGER.info("\n" + paris.toString());
		LOGGER.info("\n" + cologne.toString());
		String url = parisCologne.setMapUrl();
		System.out.println(url);
		parisCologne.openMapUrl(url);

	}
}
