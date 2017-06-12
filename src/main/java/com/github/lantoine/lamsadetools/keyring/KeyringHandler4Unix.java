package com.github.lantoine.lamsadetools.keyring;

import java.sql.SQLException;



import com.github.windpapi4j.InitializationFailedException;
import com.github.windpapi4j.WinAPICallFailedException;
import com.sun.star.uno.Exception;

import io.bunting.keyring.DefaultKeyring;
import io.bunting.keyring.Keyring;

public class KeyringHandler4Unix {
	

	 
	 public static void main(String[] args){
		 Keyring  keyring = new DefaultKeyring("myApp");
			
		 String service ="serv";
		 String user ="user";
		 String stringPassword= "test";
		 
		 char[] password = stringPassword.toCharArray();
		 keyring.setPassword(service,user,password);
		 
		 char[] password2 = keyring.getPassword(service, user);
		 
		
		 
    }


	
}
