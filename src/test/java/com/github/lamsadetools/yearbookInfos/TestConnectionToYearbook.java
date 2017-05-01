package com.github.lamsadetools.yearbookInfos;

import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.StandardCopyOption;


import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.junit.Assert;
import org.junit.Test;

import com.sun.star.lang.IllegalArgumentException;

public class TestConnectionToYearbook {

	@Test
	public void testIfHtmlPageisNull() throws IllegalArgumentException, IOException {
		
		
		ConnectionToYearbook test = new ConnectionToYearbook("Olivier", "Cailloux");
		test.buildConnection();
		File HTMLPage = test.getHtmlPage();
		Assert.assertNotNull(HTMLPage);
		
		
					
	}
	
	@Test
	public void testContentOfHtmlAndAvoid404() throws IOException, URISyntaxException, IllegalArgumentException{
		
		ConnectionToYearbook test = new ConnectionToYearbook("Olivier", "Cailloux");
		test.buildConnection();
		File HTMLPage = test.getHtmlPage();
		//Used to create a reference file 
		/*FileInputStream fis = new FileInputStream(HTMLPage);
	    File targetFile = new File("src/test/resources/com/github/lamsadetools/yearbookInfos/testHtmlPage.html");
	    java.nio.file.Files.copy(
	    		  fis, 
			      targetFile.toPath(), 
			      StandardCopyOption.REPLACE_EXISTING);
	    IOUtils.closeQuietly(fis);*/
		File file = new File(TestConnectionToYearbook.class.getResource("testHtmlPage.html").getFile());
		Assert.assertEquals(FileUtils.readLines(file), FileUtils.readLines(HTMLPage));
	}
}
