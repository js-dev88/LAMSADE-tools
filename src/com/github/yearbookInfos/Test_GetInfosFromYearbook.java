package com.github.yearbookInfos;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import org.junit.Assert;
import org.junit.Test;

/**
 * 
 */

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
		GetInfosFromYearbook profJava = new GetInfosFromYearbook(prenom, nom);
		ArrayList<String> rawInfos = profJava.getInfos();
		String[][] orderedInfos = profJava.getOrderedInfos();
		for(int i =0; i<rawInfos.size();++i){
			System.out.println(rawInfos.get(i));
		}
		Assert.assertTrue("Fonction = MAITRE DE CONFERENCES", orderedInfos[0][0].equals("Fonction") && orderedInfos[0][1].equals("MAITRE DE CONFERENCES"));
		
		
		String toStringTest = profJava.toString();
		System.out.println(toStringTest);
		
	}
	@Test
	public void testGetInfosFromYearbookwithURL() {
		URL url =null;
		try {
			url = new URL("https://www.ent.dauphine.fr/Annuaire/index.php?param0=fiche&param1=mmanouvrier");
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		GetInfosFromYearbook profBDD = new GetInfosFromYearbook(url);
		ArrayList<String> rawInfos = profBDD.getInfos();
		String[][] orderedInfos = profBDD.getOrderedInfos();
		//System.out.println("TOTO" + orderedInfos[3][0]);
		//Assert.assertTrue("Le bureau de Mme Mannouvrier est P424", orderedInfos[3][0].equals("Bureau") && orderedInfos[3][1].equals("P424"));
		System.out.println("Affichage avec une boucle :\n");
		for (int i=0;i<orderedInfos.length;++i){
			System.out.println(orderedInfos[i][0] + " : " + orderedInfos[i][1]);
		}	
		
		String toStringTest = profBDD.toString();
		System.out.println("Affiche avec toString()" + toStringTest);
		
	}

}
