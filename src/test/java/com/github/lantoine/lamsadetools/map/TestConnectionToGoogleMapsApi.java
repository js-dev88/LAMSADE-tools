package com.github.lantoine.lamsadetools.map;

import java.io.IOException;
import java.io.InputStream;

import org.junit.Assert;
import org.junit.Test;

public class TestConnectionToGoogleMapsApi {

	@Test
	public void testIfHtmlPageIsNull() throws IllegalArgumentException, NullPointerException, IOException {
		ConnectionToGoogleMapsApi test = new ConnectionToGoogleMapsApi("Paris");
		test.buildConnection();
		try (InputStream HTMLPage = test.getHtmlPage()) {
			Assert.assertNotNull(HTMLPage);
			HTMLPage.close();
		}
	}

}
