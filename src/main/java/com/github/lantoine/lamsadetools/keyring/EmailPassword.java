package com.github.lantoine.lamsadetools.keyring;

import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.lantoine.lamsadetools.missionOrder.generateMissionOrder;
import com.github.lantoine.lamsadetools.utils.Util;
import com.github.windpapi4j.InitializationFailedException;
import com.github.windpapi4j.WinAPICallFailedException;
import com.github.windpapi4j.WinDPAPI;
import com.github.windpapi4j.WinDPAPI.CryptProtectFlag;
import com.sun.star.uno.Exception;

/**
 * Create an object with email and password used for storage in database and
 * send Mission Orders. Please use this class when you need a Keyring.
 * 
 * @author js-dev88, AbdelKader Zerouali
 *
 */
public class EmailPassword {
	@SuppressWarnings("unused")
	private static final Logger LOGGER = LoggerFactory.getLogger(EmailPassword.class);
	private String email;
	private String encryptedPassword;
	private String nonEncryptedPassword;

	public String getNonEncryptedPassword() {
		return nonEncryptedPassword;
	}

	public void setNonEncryptedPassword(String nonEncryptedPassword) {
		this.nonEncryptedPassword = nonEncryptedPassword;
	}

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

	public EmailPassword(String email, String nonEncryptedPassword, String encryptedPassword) {
		this.email = email;
		this.nonEncryptedPassword = nonEncryptedPassword;
		this.encryptedPassword = encryptedPassword;
	}

	/**
	 * Unix and Mac Email Password Constructor
	 * 
	 * @param email
	 * @param nonEncryptedPassword
	 * @param encryptedPassword
	 */
	public EmailPassword(String unixPassword) {
		this.nonEncryptedPassword = unixPassword;
	}

	@Override
	public String toString() {
		return "EmailPassword [email=" + email + ", encryptedPassword=" + encryptedPassword + ", nonEncryptedPassword="
				+ nonEncryptedPassword + "]";
	}

	/**
	 * Use an adapted keyring handler to encrypt and store the email's password
	 * password
	 * 
	 * @param an
	 *            Email Password object
	 * @throws Exception
	 * @throws WinAPICallFailedException
	 * @throws InitializationFailedException
	 * @return
	 */
	public void encryptPassword() throws Exception, InitializationFailedException, WinAPICallFailedException {

		switch (Util.getOS()) {
		case WINDOWS:
			String pswdToEncrypt = this.getNonEncryptedPassword();
			String pswdEncrypted;
			pswdEncrypted = KeyringHandler4Windows.getEncryptedString(pswdToEncrypt);
			this.setNonEncryptedPassword(null);
			this.setEncryptedPassword(pswdEncrypted);
			break;
		case LINUX:
			String pswdToEncryptUnix = this.getNonEncryptedPassword();
			KeyringHandler4Unix.encryptPasswordUnix(pswdToEncryptUnix);
			break;
		case MAC:
			String pswdToEncryptMac = this.getNonEncryptedPassword();
			KeyringHandler4Unix.encryptPasswordUnix(pswdToEncryptMac);
			break;
		case SOLARIS:
			String pswdToEncryptSolaris = this.getNonEncryptedPassword();
			KeyringHandler4Unix.encryptPasswordUnix(pswdToEncryptSolaris);
			break;
		default:
			throw new Exception("Could not retrieve the OS");
		}

	}

	/**
	 * Use an adapted keyring handler to decrypt password
	 * 
	 * @param an
	 *            Email Password object
	 * @throws Exception
	 * @throws WinAPICallFailedException
	 * @throws InitializationFailedException
	 * @return
	 */
	public String decryptPassword() throws Exception, InitializationFailedException, WinAPICallFailedException {
		String decryptedPassword;
		switch (Util.getOS()) {
		case WINDOWS:
			String pswdToDecrypt = this.getEncryptedPassword();
			decryptedPassword = KeyringHandler4Windows.getDecryptedString(pswdToDecrypt);
			break;
		case LINUX:
			decryptedPassword = KeyringHandler4Unix.decryptPasswordUnix();
			break;
		case MAC:
			decryptedPassword = KeyringHandler4Unix.decryptPasswordUnix();
			break;
		case SOLARIS:
			decryptedPassword = KeyringHandler4Unix.decryptPasswordUnix();
			break;
		default:
			throw new Exception("Could not retrieve the OS");
		}

		return decryptedPassword;
	}

	public static void main(String[] args) throws NullPointerException, Exception, InitializationFailedException,
			WinAPICallFailedException, SQLException {
		
		switch (Util.getOS()) {
		case WINDOWS:
			EmailPassword toStore = new EmailPassword("adresse@test", "testMdpEnClair", "");
			toStore.encryptPassword();
			LOGGER.debug(toStore.toString());
			
			EmailPasswordDatabase.createTable();
			EmailPasswordDatabase.insertInDatabase(toStore);
			EmailPassword tested = EmailPasswordDatabase.getEmailPassword("adresse@test");
			
			LOGGER.debug(tested.toString());
			
			EmailPasswordDatabase.removePasswordFromDatabase("adresse@test");
			EmailPasswordDatabase.clearDataBase();
			break;
		case LINUX:
			EmailPassword toStoreUnix = new EmailPassword("testMdpEnClair");
			toStoreUnix.encryptPassword();
			LOGGER.debug(toStoreUnix.decryptPassword());
			break;
		case MAC:
			EmailPassword toStoreMac = new EmailPassword("testMdpEnClair");
			toStoreMac.encryptPassword();
			LOGGER.debug(toStoreMac.decryptPassword());
			break;
		case SOLARIS:
			EmailPassword toStoreSolaris = new EmailPassword("testMdpEnClair");
			toStoreSolaris.encryptPassword();
			LOGGER.debug(toStoreSolaris.decryptPassword());
			break;
		default:
			throw new Exception("Could not retrieve the OS");
		}
	}

}
