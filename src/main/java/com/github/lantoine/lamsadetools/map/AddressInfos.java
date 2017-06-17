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

import com.google.maps.GeoApiContext;
import com.google.maps.GeocodingApi;
import com.google.maps.model.AddressComponent;
import com.google.maps.model.AddressComponentType;
import com.google.maps.model.GeocodingResult;
import com.google.maps.model.LatLng;

public class AddressInfos {

	GeoApiContext geoApiContext;
	private String rawAddress;
	private String formatted_address;
	private String longitude;
	private String latitude;
	private String city;

	private String apiKey = "AIzaSyCEfh_xRxP-9OvzTu2PvlQKG5fpwSt5rpU";

	public static void main(String[] args) throws Exception {
		AddressInfos ai = new AddressInfos("22 Boulevard de la Libération Chaville");
		ai.retrieveGeocodeResponse();
		ai.getCityFromLatLang();
	}

	/**
	 * Enter an address to set the rawAddress attribute. the latitude and the
	 * longitude of the address you've entered will be set to empty Please use
	 * retrieveGeocodeResponse() method to set those attributes.
	 *
	 * @param rawAdress
	 * @throws Exception
	 */
	public AddressInfos(String rawAdress) throws Exception {
		if (rawAdress == null) {
			throw new IllegalArgumentException("The rawAdress cannot be null");
		}
		rawAddress = rawAdress;
		formatted_address = "";
		longitude = "0";
		latitude = "0";
		geoApiContext = new GeoApiContext().setApiKey(apiKey);
		city = getCityFromLatLang();

	}

	/**
	 * Converts this object's latitude and langitude into a city name by using
	 * Google's Geocoding API
	 * 
	 * @return the city name
	 * @throws NumberFormatException
	 * @throws Exception
	 */
	public String getCityFromLatLang() throws NumberFormatException, Exception {
		GeocodingResult[] results = GeocodingApi.newRequest(geoApiContext)
				.latlng(new LatLng(Double.parseDouble(latitude), Double.parseDouble(longitude))).language("en").await();

		for (AddressComponent addressComponent : results[0].addressComponents) {
			for (AddressComponentType acType : addressComponent.types) {
				if (acType == AddressComponentType.LOCALITY) {
					return addressComponent.longName;
				}
			}
		}

		return "";
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

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}
}
