package com.github.lamsadetools.yearbookInfos;

import java.io.IOException;
import java.util.HashMap;

import org.junit.Assert;
import org.junit.Test;
import com.sun.star.lang.IllegalArgumentException;
public class TestGetInfosFromYearbook {

	
	@Test
	public void testGetInfosFromYearbookWorks() throws IllegalArgumentException, IOException{
		String firstname = "Olivier";
		String surname = "CAILLOUX";
		GetInfosFromYearbook prof = new GetInfosFromYearbook(firstname, surname);
		prof.retrieveYearbookData();
		Assert.assertEquals("olivier.cailloux@lamsade.dauphine.fr", prof.getCourrier());
		Assert.assertEquals("MAITRE DE CONFERENCES", prof.getFonction());
		Assert.assertEquals("+33 1 44 05 46 53", prof.getTelephone());
		Assert.assertEquals("MIDO - LAMSADE", prof.getGroupes());
		Assert.assertEquals("non renseigné", prof.getFax());
		Assert.assertEquals("P405 ter", prof.getBureau());							
	}
	  
	
    
    
}
