package com.github.lamsadetools.yearbookInfos;

import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;

import org.apache.commons.io.FileUtils;
import org.junit.Assert;
import org.junit.Test;

import com.sun.star.lang.IllegalArgumentException;

public class TestConnectionToYearbook {
	
	@Test
	public void testConnectionWithYearbook() throws IllegalArgumentException, IOException {
		
		
		ConnectionToYearbook test = new ConnectionToYearbook("Olivier", "Cailloux");
		test.buildConnection();
		String HTMLPage = test.getHtmlPage();
		Assert.assertNotNull(HTMLPage);
		
		
	    
							
	}
}
