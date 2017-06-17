package com.github.lantoine.lamsadetools.map;

import static org.junit.Assert.*;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import org.junit.Assert;
import org.junit.Test;

import com.github.lantoine.lamsadetools.yearbookInfos.ConnectionToYearbook;

public class TestConnectionToGoogleMapsApi {

	@Test
	public void testIfHtmlPageIsNull() throws IllegalArgumentException, NullPointerException, IOException {
		ConnectionToGoogleMapsApi test = new ConnectionToGoogleMapsApi("Paris");
		test.buildConnection();
		try (InputStream HTMLPage = test.getHtmlPage()){
			Assert.assertNotNull(HTMLPage);	
		}
			
	}

}
