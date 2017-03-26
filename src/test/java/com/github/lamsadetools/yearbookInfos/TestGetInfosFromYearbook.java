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
		HashMap<String, String> hashTested = new HashMap<String, String>();
		hashTested = prof.getHashMap();
		HashMap<String, String> hashTest = new HashMap<String, String>();
		hashTest.put("Courriel","olivier.cailloux@lamsade.dauphine.fr");
		hashTest.put("Fonction","MAITRE DE CONFERENCES");
		hashTest.put("Téléphone","+33 1 44 05 46 53");
		hashTest.put("Groupes","MIDO - LAMSADE");
		hashTest.put("Fax","non renseigné");
		hashTest.put("Bureau","P405 ter");
		Assert.assertTrue(hashTested.equals(hashTest));
							
	}
	  
	
    
    
}
