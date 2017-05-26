package com.github.lantoine.lamsadetools.yearbookInfos;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.StandardCopyOption;


import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
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
