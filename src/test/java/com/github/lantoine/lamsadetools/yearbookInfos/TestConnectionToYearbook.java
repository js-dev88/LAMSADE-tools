package com.github.lantoine.lamsadetools.yearbookInfos;

import java.io.IOException;
import java.io.InputStream;
import org.junit.Assert;
import org.junit.Test;

import com.github.lantoine.lamsadetools.yearbookInfos.ConnectionToYearbook;
import com.sun.star.lang.IllegalArgumentException;

public class TestConnectionToYearbook {

	@Test
	public void testIfHtmlPageisNull() throws IllegalArgumentException, IOException {
		
		
		ConnectionToYearbook test = new ConnectionToYearbook("Olivier", "Cailloux");
		test.buildConnection();
		try(InputStream HTMLPage = test.getHtmlPage()){
			Assert.assertNotNull(HTMLPage);
			HTMLPage.close();
		}
		
		
					
	}
	
	
}
