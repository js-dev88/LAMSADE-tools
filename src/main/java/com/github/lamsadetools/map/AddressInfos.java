package com.github.lamsadetools.map;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

import com.github.lamsadetools.setCoordinates.UserDetails;
import com.sun.star.lang.IllegalArgumentException;

public class AddressInfos {
	final static Logger LOGGER = Logger.getLogger(AddressInfos.class);
	static final String path = "src/test/resources/log4j.properties";
	
	private String rawAdress;
	private HashMap<String, String> information = new HashMap<String, String>();

	
	/**
	 * Enter an address and you'll be able to get a formatted address, 
	 * the latitude and the longitude of the address you've entered.
	 * 
	 * @param rawAdress
	 * @throws IllegalArgumentException
	 * @throws IOException
	 */
	public AddressInfos(String rawAdress) throws IllegalArgumentException, IOException {
		if(rawAdress == null ){
			throw new IllegalArgumentException("The rawAdress cannot be null");
		}		
		this.rawAdress = rawAdress;		
		//Build the URL parameter with the address needed
		String param1 = rawAdress;
		//This part of the url is always the same
		String urlConstantPart = "http://maps.googleapis.com/maps/api/geocode/xml?sensor=true";
		//First step of the JAX rs class => Client initialization
		Client client = ClientBuilder.newClient();
		//Build the targeted URL
		WebTarget t1 = client.target(urlConstantPart);
		WebTarget t2 = t1.queryParam("address", param1);
		
		//The entire HTML page is stocked in result
		String result = t2.request(MediaType.TEXT_PLAIN).get(String.class);
		retrieveGeocodeResponse(result);
		client.close();	
	}
	
	
	
	/**
	 * 
	 * retrieveGeocodeResponse read a string which contains a geocode response from maps.googleapis
	 * it fills a hashmap with the address, latitude and longitude and their corresponding values.
	 * 
	 * @param xmlText
	 * @throws IOException
	 * @throws IllegalArgumentException
	 * 
	 * 
	 */
	private void retrieveGeocodeResponse(String xmlText) throws IOException, IllegalArgumentException {

		String line = null;
		String nextLine = null;
		ArrayList<String> rawInfos = new ArrayList<String>();

		// read each line and add it into rawInfos
		try (BufferedReader br = new BufferedReader(new StringReader(xmlText))) {
			while ((line = br.readLine()) != null) {
				//check whether the status of the resquest is OK or not
				if (line.contains("<status>")) { 
					superfluousRemover(rawInfos);
					if (!line.contains("OK")){
						throw new IllegalArgumentException("the address typed doesn't exist: " + rawAdress);
					}
				}
				// Add the formatted address into rawInfos
				if (line.contains("<formatted_address>")){
					rawInfos.add("Address");
					rawInfos.add(line);
				}
				
				//Add the longitude and latitude into rawInfos
				if (line.contains("<location>") ){
					while (!(nextLine = br.readLine()).contains("</location>")){
						if (nextLine.contains("<lat>")){
							rawInfos.add("Latitude");
							rawInfos.add(nextLine);
						}
						
						if (nextLine.contains("<lng>")){
							rawInfos.add("Longitude");
							rawInfos.add(nextLine);
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
	 * Remove unnecessary html/xml tags and spaces (before the first character and after the last one).
	 * @param rawInfos
	 * 
	 */
	private void superfluousRemover(ArrayList<String> rawInfos) {
		for (int i = 0; i < rawInfos.size(); ++i) {
			rawInfos.set(i, rawInfos.get(i).replaceAll("<[^>]+>", "")); // delete
																	// every
																	// XML tags
			rawInfos.set(i, rawInfos.get(i).trim()); // delete every spaces
												// surrounding the string
		}
	}
	
	/**
	 * Construct the HasMap collections with proper label and values
	 *
	 * @param rawInfos
	 *            contains lines with labels and lines with corresponding data
	 * @throws IllegalArgumentException
	 *             if rawInfos is null, this indicates that the address is wrong, 
	 *             no data is found or the original HTML page is a 404.
	 * @throws IllegalArgumentException
	 *             if the rawInfos contains no data
	 * @return the HAshMap with all the person's informations
	 */
	private HashMap<String, String> hashMapConstructor(ArrayList<String> rawInfos) throws IllegalArgumentException {
		if (rawInfos.isEmpty() || rawInfos.size() == 0) {
			throw new IllegalArgumentException("Wrong parameters or site is unreachable");
		}
		int j = 0;
		while (j < rawInfos.size()) {
			information.put(rawInfos.get(j), rawInfos.get(j + 1));
			j += 2;
		}
		return information;
	}
	
	
	@Override
	public String toString() {
		String toString = "";
		for (String i : information.keySet()) {
			toString += i + ": " + information.get(i) + "\n";
		}
		return toString;
	}
	
	
	
	/**
	 * This method is temporary and won't be available in the next release
	 * It gives a link to a png tile corresponding to the latitude and longitude given 
	 * and zoomed at the level entered (zoom parameter)
	 * 
	 * @param lat
	 * @param lon
	 * @param zoom
	 * @return the link to a png tile 
	 */
	@Deprecated
	private String getTileNumber(double lat, double lon, int zoom) {
		int xtile = (int)Math.floor( (lon + 180) / 360 * (1<<zoom) ) ;
		int ytile = (int)Math.floor( (1 - Math.log(Math.tan(Math.toRadians(lat)) + 1 / Math.cos(Math.toRadians(lat))) / Math.PI) / 2 * (1<<zoom) ) ;
		if (xtile < 0)
			xtile=0;
		if (xtile >= (1<<zoom))
			xtile=((1<<zoom)-1);
		if (ytile < 0)
			ytile=0;
		if (ytile >= (1<<zoom))
			ytile=((1<<zoom)-1);
		return("http://tile.openstreetmap.org/" + zoom + "/" + xtile + "/" + ytile + ".png");
	}
	
	public String getRawAdress() {
		return rawAdress;
	}

	public HashMap<String, String> getInformation() {
		return information;
	}
	
	public String getAddress() {
		return information.get("Address");
	}
	
	public String getLatitude() {
		return information.get("Latitude");
	}
	
	public String getLongitude() {
		return information.get("Longitude");
	}
	
	
	public static void main(String[] args) {
		PropertyConfigurator.configure(path);
		
		String rawAdress = "Paris";
		HashMap<String, String> information;
		String linkTile;
		try {
			AddressInfos paris = new AddressInfos(rawAdress);
			LOGGER.info("\n" + paris.toString());
			information = paris.getInformation();
			linkTile = paris.getTileNumber(Double.parseDouble(information.get("Latitude")), Double.parseDouble(information.get("Longitude")), 10);
			LOGGER.info("\n" + linkTile);
		} catch (IllegalArgumentException | IOException e) {
			LOGGER.error("Error : ", e);
		}		
	}	
}
