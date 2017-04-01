package com.github.lamsadetools.map;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashMap;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;

import com.sun.star.lang.IllegalArgumentException;

public class AddressInfos {
	
	
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
			throw new IOException("Error when trying to read Nextline", e2);
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
	
}
