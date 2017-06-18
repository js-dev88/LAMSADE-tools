package com.github.lantoine.lamsadetools.utils;

import org.junit.Test;

public class TestUtil {
	
	@Test(expected=IllegalStateException.class)
	public void testSendEmailStringString() {
		
		Util.sendEmail("1234567890abcdefghijklmnopqrstuvwxy@mailinator.com","test");
		
	}

	// @Test
	// public void testSendEmailString() {
	// fail("Not yet implemented");
	// }

}
