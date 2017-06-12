package com.github.lantoine.lamsadetools.keyring;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Base64;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.windpapi4j.InitializationFailedException;
import com.github.windpapi4j.WinAPICallFailedException;
import com.github.windpapi4j.WinDPAPI;
import com.github.windpapi4j.WinDPAPI.CryptProtectFlag;
import com.sun.star.uno.Exception;

import static java.nio.charset.StandardCharsets.UTF_8;

/**
 * Keyrin mecanism for windows using the windpapi4j API
 * 
 * @author js-dev88, AbdelKader Zerouali
 *
 */
public class KeyringHandler4Windows {
	@SuppressWarnings("unused")
	private static final Logger LOGGER = LoggerFactory.getLogger(KeyringHandler4Windows.class);

	private static WinDPAPI winDPAPI;

	/**
	 * encrypt the string with Windows mecanism
	 * 
	 * @param plaintext
	 * @return
	 * @throws WinAPICallFailedException
	 *             if encryption failed
	 */
	public static String encrypt(String plaintext) throws WinAPICallFailedException {
		byte[] encryptedBytes = winDPAPI.protectData(plaintext.getBytes(UTF_8));
		return Base64.getEncoder().encodeToString(encryptedBytes);
	}

	/**
	 * Decrypt the string with Windows mecanism
	 * 
	 * @param plaintext
	 * @return
	 * @throws WinAPICallFailedException
	 *             if decryption failed
	 */
	public static String decrypt(String encryptedString) throws WinAPICallFailedException {
		byte[] encryptedBytes = Base64.getDecoder().decode(encryptedString);
		return new String(winDPAPI.unprotectData(encryptedBytes), UTF_8);
	}

	/**
	 * factorization of the process to call the API directly from other classes
	 * @param pswdToEncrypt
	 *            may not be null
	 * @throws Exception
	 * @throws InitializationFailedException
	 * @throws WinAPICallFailedException
	 */
	public static String getEncryptedString(String pswdToEncrypt)
			throws Exception, InitializationFailedException, WinAPICallFailedException {
		if (pswdToEncrypt == null) {
			throw new NullPointerException("No String detected");
		}
		String pswdEncrypted;
		if (!WinDPAPI.isPlatformSupported()) {
			throw new Exception("The Windows Data Protection API (DPAPI) is not available on "
					+ System.getProperty("os.name") + ".");
		}
		winDPAPI = WinDPAPI.newInstance(CryptProtectFlag.CRYPTPROTECT_UI_FORBIDDEN);
		pswdEncrypted = encrypt(pswdToEncrypt);
		return pswdEncrypted;
	}

	/**
	 * factorization of the process to call the API directly from other classes
	 * @param pswdEncrypted
	 *            may not be null
	 * @return the decrypted password in String format
	 * @throws Exception
	 * @throws InitializationFailedException
	 * @throws WinAPICallFailedException
	 */
	public static String getDecryptedString(String pswdEncrypted)
			throws Exception, InitializationFailedException, WinAPICallFailedException {
		if (pswdEncrypted == null) {
			throw new NullPointerException("No String detected");
		}
		String pswdDecrypted;
		if (!WinDPAPI.isPlatformSupported()) {
			throw new Exception("The Windows Data Protection API (DPAPI) is not available on "
					+ System.getProperty("os.name") + ".");
		}
		winDPAPI = WinDPAPI.newInstance(CryptProtectFlag.CRYPTPROTECT_UI_FORBIDDEN);
		pswdDecrypted = decrypt(pswdEncrypted);
		return pswdDecrypted;
	}

}
