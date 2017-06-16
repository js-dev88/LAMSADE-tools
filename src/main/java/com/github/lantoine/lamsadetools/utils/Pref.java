package com.github.lantoine.lamsadetools.utils;

import java.util.prefs.Preferences;

/**
 * This class will store the Name and Surname of the user
 * 
 * @author zeroab16
 *
 */
public class Pref {
	private Preferences prefs;
	public Preferences getPrefs() {
		return prefs;
	}

	public Pref() {
		// TODO Auto-generated constructor stub
	}

	  public void setPreference(String name, String surname, String login) {
	    // This will define a node in which the preferences can be stored
	    prefs = Preferences.userRoot().node(this.getClass().getName());
	    String k_name = "Nom";
	    String k_surname = "Prenom";
	    String k_login= "Login";
	   
	    // First we will get the values
	    // Define a boolean value
	    /*System.out.println(prefs.getBoolean(ID1, true));
	    // Define a string with default "Hello World
	    System.out.println(prefs.get(ID2, "Hello World"));
	    // Define a integer with default 50
	    System.out.println(prefs.getInt(ID3, 50));
	    */

	    prefs.put(k_name, name );
	    prefs.put(k_surname, surname);
	    prefs.put(k_login, login);
	    // now set the values
	  }
	
	  public static void main(String[] args) {
		String name = "Toto";
		String surname = "TEST";
		String login = "ttest";
		Pref pref= new Pref();	
		pref.setPreference(name,surname,login);
		System.out.println(pref.getPrefs().get("Nom", "blabla"));
		System.out.println(pref.getPrefs().get("Prenom", "blabla"));
		System.out.println(pref.getPrefs().get("Login", "blabla"));
		
		
	}
	
}
