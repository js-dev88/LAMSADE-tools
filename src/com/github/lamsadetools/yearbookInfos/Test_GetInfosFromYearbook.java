package com.github.lamsadetools.yearbookInfos;

import java.net.MalformedURLException;
import java.net.URL;

import org.junit.Assert;
import org.junit.Test;


/**
 * This class tests the use of GetInfosFromYearbook constructors
 * @author 
 *
 */
public class Test_GetInfosFromYearbook {

	@Test
	public void testGetInfosFromYearbookwithName() {
		String prenom = "Olivier";
		String nom = "CAILLOUX";
		GetInfosFromYearbook prof = new GetInfosFromYearbook(prenom, nom);
		Assert.assertTrue(prof.getCourrier().equals("olivier.cailloux@lamsade.dauphine.fr")
				&& prof.getFonction().equals("MAITRE DE CONFERENCES")
				&& prof.getTelephone().equals("+33 1 44 05 46 53")
				&& prof.getGroupes().equals("MIDO - LAMSADE")
				&& prof.getFax().equals("non renseigné")
				&& prof.getBureau().equals("P405 ter"));
		
		String toStringTest = prof.toString();
		System.out.println(toStringTest);
		
	}
	
	
	
	@Test
	public void testGetInfosFromYearbookwithURL() {
		URL url =null;
		try {
			url = new URL("https://www.ent.dauphine.fr/Annuaire/index.php?param0=fiche&param1=mmanouvrier");
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
		GetInfosFromYearbook prof = new GetInfosFromYearbook(url);
		Assert.assertTrue(prof.getBureau().equals("P424"));
				
	}

}