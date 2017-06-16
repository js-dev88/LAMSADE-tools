package com.github.lantoine.lamsadetools.map;

import java.io.IOException;
import java.io.InputStream;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

public class AddressInfos {

	private String rawAddress;
	private String formatted_address;
	private String longitude;
	private String latitude;

	/**
	 * Enter an address to set the rawAddress attribute. the latitude and the
	 * longitude of the address you've entered will be set to empty Please use
	 * retrieveGeocodeResponse() method to set those attributes.
	 *
	 * @param rawAdress
	 * @throws IllegalArgumentException
	 */
	public AddressInfos(String rawAdress) throws IllegalArgumentException {
		if (rawAdress == null) {
			throw new IllegalArgumentException("The rawAdress cannot be null");
		}
		rawAddress = rawAdress;
		formatted_address = "";
		longitude = "0";
		latitude = "0";
	}

	/**
	 *
	 * retrieveGeocodeResponse connects to maps.googleapis enters the rawAddress
	 * and gets a geocode response It takes from the geocode response : the
	 * formatted_address, latitude and longitude and sets the corresponding
	 * class attributes
	 *
	 * @throws IOException
	 *
	 *
	 */
	public void retrieveGeocodeResponse()
			throws IOException, SAXException, ParserConfigurationException, IllegalArgumentException {
		DocumentBuilderFactory factory = null;
		DocumentBuilder builder = null;
		Document htmlDoc = null;

		// Connection to the google maps api
		ConnectionToGoogleMapsApi connection = new ConnectionToGoogleMapsApi(rawAddress);
		connection.buildConnection();
		try (InputStream htmlText = connection.getHtmlPage()) {

			// InputStream transformed in DOM Document
			factory = DocumentBuilderFactory.newInstance();
			builder = factory.newDocumentBuilder();
			htmlDoc = builder.parse(new InputSource(htmlText));
			// close the stream
			htmlText.close();
			NodeList status = htmlDoc.getElementsByTagName("status");
			// Checks if the request has a positive result
			if (status.getLength() != 0) {
				for (int i = 0; i < status.getLength(); i++) {
					if (!status.item(i).getTextContent().contains("OK")) {
						throw new IllegalArgumentException("the address typed doesn't exist: " + rawAddress);
					}
				}
			}
			@SuppressWarnings("hiding")
			NodeList formatted_address = htmlDoc.getElementsByTagName("formatted_address");
			if (formatted_address.getLength() != 0) {
				for (int i = 0; i < formatted_address.getLength(); i++) {
					this.formatted_address = formatted_address.item(i).getTextContent();
				}
			}
			NodeList location = htmlDoc.getElementsByTagName("location");
			for (int i = 0; i < location.getLength(); i++) {
				NodeList lat = htmlDoc.getElementsByTagName("lat");
				latitude = lat.item(0).getTextContent();
				NodeList lng = htmlDoc.getElementsByTagName("lng");
				longitude = lng.item(0).getTextContent();
			}
		}
	}

	@Override
	public String toString() {
		String toString = "";
		toString += "rawAddress = " + rawAddress + "\n" + "formatted_address = " + formatted_address + "\n"
				+ "latitude = " + latitude + "\n" + "longitude = " + longitude;
		return toString;
	}

	public String getRawAddress() {
		return rawAddress;
	}

	public String getFormatted_address() {
		return formatted_address;
	}

	public String getLatitude() {
		return latitude;
	}

	public String getLongitude() {
		return longitude;
	}
}
