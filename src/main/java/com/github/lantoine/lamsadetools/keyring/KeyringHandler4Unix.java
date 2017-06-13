package com.github.lantoine.lamsadetools.keyring;

import java.sql.SQLException;

import com.github.windpapi4j.InitializationFailedException;
import com.github.windpapi4j.WinAPICallFailedException;
import com.sun.star.uno.Exception;

import io.bunting.keyring.DefaultKeyring;
import io.bunting.keyring.Keyring;

/**
 * This class is used to simplify the access to the keyring Api
 * 
 * @author js-dev88 Abdelkader Zerouali
 *
 */
public class KeyringHandler4Unix {

	private static Keyring keyring = new DefaultKeyring("lamsade-tools");
	private static String user = "user";
	private static String service = "serv";

	/**
	 * This method will use the OS mechanism
	 * 
	 * @param password
	 */
	public static void encryptPasswordUnix(String password) {
		char[] charPassword = password.toCharArray();
		keyring.setPassword(service, user, charPassword);
	}

	public static String decryptPasswordUnix() {
		char[] password;
		password = keyring.getPassword(service, user);
		return password.toString();
	}

}
