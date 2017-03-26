package com.github.lamsadetools.yearbookInfos;

import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;

import org.apache.commons.io.FileUtils;
import org.junit.Test;

import com.sun.star.lang.IllegalArgumentException;

public class TestConnectionToYearbook {
	
	@Test
	public void testConnectionWithYearbook() throws IllegalArgumentException, IOException {
		ConnectionToYearbook test = new ConnectionToYearbook("Olivier", "Cailloux");
		String htmlTestDocument = test.getHtmlPage();
		
	    PrintWriter out = new PrintWriter("src/test/resources/com/github/lamsadetools/yearbookInfos/testedHtmlPage.txt");
	    out.print(htmlTestDocument);
		out.close();

		File file1 = new File("src/test/resources/com/github/lamsadetools/yearbookInfos/testedHtmlPage.txt");
		File file2 = new File("src/test/resources/com/github/lamsadetools/yearbookInfos/testHtmlPage.txt");
		assertTrue(FileUtils.contentEquals(file1, file2));
							
	}
}
