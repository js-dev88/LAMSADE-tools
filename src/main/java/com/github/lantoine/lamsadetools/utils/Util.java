package com.github.lantoine.lamsadetools.utils;

import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.SendFailedException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.lantoine.lamsadetools.missionOrder.History;

/**
 * Utility class with misc functions
 *
 * @author edoreld
 */
public class Util {
	public enum OS {
		WINDOWS, LINUX, MAC, SOLARIS
	}// Operating systems.

	private static OS os = null;

	private static final Logger logger = LoggerFactory.getLogger(Util.class);

	public static void main(String[] args) throws IllegalStateException {
		// String path = History.getFilePath("DJC_Lyon-France_2018-04-10.fodt",
		// true);
		// sendEmail("lamsadetoolsuser@gmail.com", path);
	}

	/**
	 * Detects the operating system of the user's computer and returns it
	 *
	 * @return the operating system of the user
	 */
	public static OS getOS() {
		if (os == null) {
			String operSys = System.getProperty("os.name").toLowerCase();
			if (operSys.contains("win")) {
				os = OS.WINDOWS;
			} else if (operSys.contains("nix") || operSys.contains("nux") || operSys.contains("aix")) {
				os = OS.LINUX;
			} else if (operSys.contains("mac")) {
				os = OS.MAC;
			} else if (operSys.contains("sunos")) {
				os = OS.SOLARIS;
			}
		}
		return os;
	}

	/**
	 * Sends an email to an address passed by parameter. If there is an error
	 * other than lack of an Internet connection, the program will exit with a
	 * stack trace.
	 *
	 * @param to_address
	 *            the address to send the email to
	 * @param filename
	 *            if it's not empty, it will send the file referenced to by the
	 *            filename as an attachment
	 * @throws IllegalStateException
	 * @returns 0 if it all went well, -1 if there was some error.
	 */
	public static int sendEmail(String to_address, String filename) throws IllegalStateException {

		int smtp_port = 465;
		String smtp_host = "smtp.gmail.com";
		String smtp_username = "lamsade.tools@gmail.com";
		String smtp_password = "z}}VC_-7{3bk^?*q^qZ4Z>G<5cgN&un&@>wU`gyza]kR(2v/&j!*6*s?H[^w=52e";

		Properties props = System.getProperties();
		props.put("mail.smtp.host", smtp_host);
		props.put("mail.smtps.auth", true);
		props.put("mail.smtps.starttls.enable", true);
		props.put("mail.smtps.debug", true);

		Session session = Session.getInstance(props, null);

		Message message = new MimeMessage(session);

		logger.info("Trying to send an email to " + to_address);

		try {
			try {
				message.addRecipient(Message.RecipientType.TO, new InternetAddress(to_address));
			} catch (AddressException e) {
				logger.error("There is a problem with the address");
				throw new IllegalStateException(e);
			}
			message.setFrom(new InternetAddress(smtp_username));

			message.setSubject("New Mission Order");
			message.setText("Email Test Body");

			Multipart multipart = new MimeMultipart();

			MimeBodyPart messageBodyPart = new MimeBodyPart();

			messageBodyPart.setContent("Une conference a été partagée avec vous", "text/html");

			MimeBodyPart attachPart = new MimeBodyPart();

			String attachFile = filename;
			attachPart.attachFile(attachFile);

			// adds parts to the multipart
			multipart.addBodyPart(messageBodyPart);
			multipart.addBodyPart(attachPart);

			// sets the multipart as message's content
			message.setContent(multipart);

			Transport transport = session.getTransport("smtps");
			try {
				transport.connect(smtp_host, smtp_port, smtp_username, smtp_password);
			} catch (MessagingException e) {
				logger.debug("There seems to be a problem with the connection. Try again later");
				return -1;
			}

			try {
				transport.sendMessage(message, message.getAllRecipients());
			} catch (SendFailedException e) {
				logger.error("Something went wrong trying to send the message");
				throw new IllegalStateException(e);
			}

			transport.close();
		} catch (MessagingException e) {
			logger.error("Something went wrong with building the message");
			throw new IllegalStateException(e);
		} catch (IOException e) {
			logger.error("Error in trying to add the attachment");
			throw new IllegalStateException(e);
		}
		logger.info("Email sent successfully");
		return 0;
	}

	/**
	 * Overloading sendEmail to be able to call it with a default empty filename
	 * for testing purposes **only**. This sends a **test** email to the address
	 * passed by parameter. Use for debugging.
	 *
	 * @param to_address
	 *            The address to send the email to
	 * @throws IllegalStateException
	 */
	static int sendEmail(String to_address) throws IllegalStateException {
		return sendEmail(to_address, "");
	}

	/**
	 *
	 * @param filename
	 *            the path of the file to save
	 * @return 0 if it worked
	 */
	public static int saveFile(String pathToFile) {

		// Get path to the missions folder in the Java project
		Path path = FileSystems.getDefault().getPath("");
		File pathToProject = new File(path.toAbsolutePath() + "/missions");

		logger.info("The File will be saved in: " + pathToProject.toString());

		// Get the path to the filename to save in the project
		File fileName = new File(pathToFile);

		logger.info("FileName" + fileName);

		// Copy the file to the missions directory
		try {
			FileUtils.copyFileToDirectory(fileName, pathToProject);
		} catch (IOException e) {
			logger.error("Something went wrong trying to copy the file to the project");
			throw new IllegalStateException(e);
		}

		return 0;
	}

	/**
	 * Method to open an url on the user's default browser if supported
	 * 
	 * @param url
	 *            the URL to open
	 */
	public static void openURL(String url) {
		if (Desktop.isDesktopSupported()) {
			try {
				Desktop.getDesktop().browse(new URI(url));
			} catch (IOException | URISyntaxException e) {
				throw new IllegalStateException(e);
			}
		}
	}
}