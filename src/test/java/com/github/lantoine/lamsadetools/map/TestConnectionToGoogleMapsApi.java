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
<<<<<<< HEAD
=======
		try (InputStream HTMLPage = test.getHtmlPage()) {
			Assert.assertNotNull(HTMLPage);
			HTMLPage.close();
		}
>>>>>>> 2a7fed96b7a3947a407d565a72b460f6b8975b01
	}

}
