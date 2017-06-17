package com.github.lantoine.lamsadetools.map;

import org.junit.Assert;
import org.junit.Test;

public class TestAddressInfos {

	@Test
	public void testAddressInfosResults() throws Exception {
		String rawAddress = "Paris";
		AddressInfos paris = new AddressInfos(rawAddress);
		paris.retrieveGeocodeResponse();
		Assert.assertEquals("Paris, France", paris.getFormatted_address());
		Assert.assertEquals("48.8566140", paris.getLatitude());
		Assert.assertEquals("2.3522219", paris.getLongitude());
		Assert.assertEquals("Paris", paris.getRawAddress());
	}

}
