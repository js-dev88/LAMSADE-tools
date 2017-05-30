package com.github.lantoine.lamsadetools.utils;

import java.io.IOException;
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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
		sendEmail("edoreld@gmail.com", "/Users/edoreld/Desktop/presentation.pdf");
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
	 * Sends an email to an address passed by parameter.
	 *
	 * @param to_address
	 *            the address to send the email to
	 * @param filename
	 *            if it's not empty, it will send the file referenced to by the
	 *            filename as an attachment
	 * @throws IllegalStateException
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

		MimeMessage msg = new MimeMessage(session);

		Message message = new MimeMessage(session);

		logger.info("Trying to send an email to " + to_address);

		try {
			try {
				msg.addRecipient(Message.RecipientType.TO, new InternetAddress(to_address));
			} catch (AddressException e) {
				logger.error("There is a problem with the address");
				throw new IllegalStateException(e);
			}
			msg.setFrom(new InternetAddress(smtp_username));
			msg.setSubject("Une conference a été partagée avec vous");

			if (filename == "") {
				msg.setText("Email Test");
			} else {
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
			}

			Transport transport = session.getTransport("smtps");
			try {
				transport.connect(smtp_host, smtp_port, smtp_username, smtp_password);
			} catch (MessagingException e) {
				logger.debug("There seems to be a problem with the connection. Try again later");
			}

			try {
				transport.sendMessage(message, msg.getAllRecipients());
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
	 * for testing purposes **only**
	 *
	 * @param to_address
	 *            The address to send the email to
	 * @throws IllegalStateException
	 */
	static int sendEmail(String to_address) throws IllegalStateException {
		return sendEmail(to_address, "");
	}
}