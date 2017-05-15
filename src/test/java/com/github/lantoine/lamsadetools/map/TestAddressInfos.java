package com.github.lantoine.lamsadetools.map;

import static org.junit.Assert.*;

import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;

import org.junit.Assert;
import org.junit.Test;
import org.xml.sax.SAXException;

public class TestAddressInfos {

	@Test
	public void testAddressInfosResults() throws IllegalArgumentException, IOException, SAXException, ParserConfigurationException {
		String rawAddress = "Paris";
		AddressInfos paris = new AddressInfos(rawAddress);
		paris.retrieveGeocodeResponse();
		Assert.assertEquals("Paris, France", paris.getFormatted_address());
		Assert.assertEquals("48.8566140", paris.getLatitude());
		Assert.assertEquals("2.3522219", paris.getLongitude());
		Assert.assertEquals("Paris",paris.getRawAddress());						
	}
	

}
