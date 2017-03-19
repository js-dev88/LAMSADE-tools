package com.github.lamsadetools.yearbookInfos;

import static org.junit.Assert.*;

import java.io.IOException;

import org.junit.Assert;
import org.junit.Test;

import com.sun.star.lang.IllegalArgumentException;
public class TestGetInfosFromYearbook {
	
	
	
	@Test
	public void testGetInfosFromYearbookWorks() throws IllegalArgumentException, IOException{
		String firstname = "Olivier";
		String surname = "CAILLOUX";
		GetInfosFromYearbook prof = new GetInfosFromYearbook(firstname, surname);
		Assert.assertTrue(prof.getCourrier().equals("olivier.cailloux@lamsade.dauphine.fr")
				&& prof.getFonction().equals("MAITRE DE CONFERENCES")
				&& prof.getTelephone().equals("+33 1 44 05 46 53")
				&& prof.getGroupes().equals("MIDO - LAMSADE")
				&& prof.getFax().equals("non renseigné")
				&& prof.getBureau().equals("P405 ter"));				
	}
	
	@SuppressWarnings("unused")
    @Test
	public void testGetInfosFromYearbookWithNullFirstnameThrowsIAException() throws IOException, IllegalArgumentException {
		String firstname = null;
		String surname = "CAILLOUX";
		try {
			GetInfosFromYearbook prof = new GetInfosFromYearbook(firstname, surname);
		    fail("Should throw IllegalArgumentException when null parameters are set");
		  }catch(IllegalArgumentException e){
			  assert(e.getMessage().contains("Firstname or surname is null"));
		  }
		
	}
    
    @SuppressWarnings("unused")
    @Test
   	public void testGetInfosFromYearbookWithNullSurnameThrowsIAException() throws IOException, IllegalArgumentException {
   		String firstname = "Elsa";
   		String surname = null;
   		try {
   			GetInfosFromYearbook prof = new GetInfosFromYearbook(firstname, surname);
   		    fail("Should throw IllegalArgumentException when null parameters are set");
   		  }catch(IllegalArgumentException e){
   			  assert(e.getMessage().contains("Firstname or surname is null"));
   		  }
   		
   	}
    
    @SuppressWarnings("unused")
    @Test
    public void testGetInfosFromYearbookWrongFirstnameOrSurnamethrowsException() throws IOException, IllegalArgumentException {
		String firstname = "Elsa";
		String surname = "Marru";
		try {
			GetInfosFromYearbook prof = new GetInfosFromYearbook(firstname, surname);
		    fail("Should throw Exception when person is not found in the YearBook");
		  }catch(IllegalArgumentException e){
			  assert(e.getMessage().contains("Wrong parameters : Please verify Firstname And Surname"));
		  }
		
	}
}
