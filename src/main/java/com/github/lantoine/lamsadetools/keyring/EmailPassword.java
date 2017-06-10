package com.github.lantoine.lamsadetools.keyring;

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
 * send Mission Orders
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

	@Override
	public String toString() {
		return "EmailPassword [email=" + email + ", encryptedPassword=" + encryptedPassword + ", nonEncryptedPassword="
				+ nonEncryptedPassword + "]";
	}

	/**
	 * Use an adapted keyring hadler to the user OS to encrypt user email
	 * password
	 * 
	 * @param an
	 *            Email Password object
	 * @throws Exception
	 * @throws WinAPICallFailedException
	 * @throws InitializationFailedException
	 * @return
	 */
	public EmailPassword encryptPassword()
			throws Exception, InitializationFailedException, WinAPICallFailedException {

		String pswdToEncrypt = this.getNonEncryptedPassword();
		String pswdEncrypted = "";

		switch (Util.getOS()) {
		case WINDOWS:
			pswdEncrypted = KeyringHandler4Windows.getEncryptedString(pswdToEncrypt);
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
		this.setNonEncryptedPassword(null);
		this.setEncryptedPassword(pswdEncrypted);
		return this;
	}

}
