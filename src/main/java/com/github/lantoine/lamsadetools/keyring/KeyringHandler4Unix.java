package com.github.lantoine.lamsadetools.keyring;

import java.sql.SQLException;



import com.github.windpapi4j.InitializationFailedException;
import com.github.windpapi4j.WinAPICallFailedException;
import com.sun.star.uno.Exception;

import io.bunting.keyring.DefaultKeyring;
import io.bunting.keyring.Keyring;

public class KeyringHandler4Unix {
	private static Keyring keyring  = new DefaultKeyring("myApp");
	private static String user = "user";
	private static String service = "serv";


	 
	 public static void main(String[] args){
		 Keyring  keyring = new DefaultKeyring("myApp");
			
		 String service ="serv";
		 String user ="user";
		 String stringPassword= "Abdel";
		 
		 char[] password = stringPassword.toCharArray();
		 //keyring.setPassword(service,user,password);
		 
		 char[] password2 = keyring.getPassword(service, user);
		 System.out.println(password2);
		
		 
    }
	 
	 
	 /**
	  * This method will use the OS mechanism 
	 * @param password
	 */
	public void encryptPasswordUnix(String password){
		 
		 char[] charPassword = password.toCharArray();
		 keyring.setPassword(service,user,charPassword);
	 }
	 
	 public char[] decryptPasswordUnix(){
		 char[] password;
		 return password = keyring.getPassword(service, user);
	 }

	
}
