package com.github.lantoine.lamsadetools.keyring;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.lantoine.lamsadetools.missionOrder.generateMissionOrder;
import com.github.lantoine.lamsadetools.utils.Util;
import com.sun.star.uno.Exception;
/** Create an object with email and 
 * password used for storage in database and send Mission Orders
 * @author js-dev88
 *
 */
public class EmailPassword {
	@SuppressWarnings("unused")
	private static final Logger LOGGER = LoggerFactory.getLogger(EmailPassword.class);
	private String email;
	private String encryptedPassword;
	public String getNonEncryptedPassword() {
		return nonEncryptedPassword;
	}
	public void setNonEncryptedPassword(String nonEncryptedPassword) {
		this.nonEncryptedPassword = nonEncryptedPassword;
	}
	private String nonEncryptedPassword;
	
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getEncryptedPassword() {
		return encryptedPassword;
	}
	public void setEncryptedPassword(String encryptedPassword) {
		this.encryptedPassword = encryptedPassword;
	}
	
	public EmailPassword(String email, String nonEncryptedPassword){
		this.email = email;
		this.nonEncryptedPassword = nonEncryptedPassword;
	}
	@Override
	public String toString() {
		return "EmailPassword [email=" + email + ", encryptedPassword=" + encryptedPassword + ", nonEncryptedPassword="
				+ nonEncryptedPassword + "]";
	}
	
	
	
	/**
	 * Use an adapted keyring hadler to the user OS to encrypt user email password
	 * @param an Email Password object
	 * @throws Exception 
	 * 
	 */
	/*public EmailPassword encryptPassword(EmailPassword emailToEncrypt) throws Exception{
		    EmailPassword emailEncrypted;
			switch (Util.getOS()) {
			case WINDOWS:
				
				break;
			case LINUX:
				
				break;
			case MAC:
				
				break;
			case SOLARIS:
				
				break;
			default:
				throw new Exception("Could not retrieve the OS");
			}
			return emailEncrypted;
	}*/
	
	
	
}
