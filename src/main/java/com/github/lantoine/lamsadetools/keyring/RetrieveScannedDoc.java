package com.github.lantoine.lamsadetools.keyring;

/*import java.io.IOException;
import java.util.Properties;

import javax.mail.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.lantoine.lamsadetools.yearbookInfos.ConnectionToYearbook;
import com.sun.mail.imap.IMAPFolder;*/

/**
 * Only a test class with a trace of the problem It is not a functionnal class
 * and it's not used in the projec
 *
 */
public class RetrieveScannedDoc {

	/*
	 * private static final Logger logger =
	 * LoggerFactory.getLogger(ConnectionToYearbook.class);
	 * 
	 * public static void main(String[] args) throws MessagingException,
	 * IOException {
	 * 
	 * Properties props = System.getProperties();
	 * props.setProperty("mail.store.protocol", "imaps");
	 * 
	 * // Put below to false, if no https is needed
	 * props.put("mail.imaps.auth.plain.disable", "true");
	 * props.put("mail.imaps.tls.enable", "true"); props.put("mail.imaps.host",
	 * "outlook.office365.com"); Session session =
	 * Session.getDefaultInstance(props, null); session.setDebug(true); Store
	 * store = session.getStore("imaps"); Here is the problem
	 * store.connect("outlook.office365.com", 993, "XX", "XX");
	 * 
	 * IMAPFolder folder = (IMAPFolder) store.getFolder("inbox");
	 * 
	 * if (!folder.isOpen()) folder.open(Folder.READ_WRITE); Message[] messages
	 * = folder.getMessages(); System.out.println("No of Messages : " +
	 * folder.getMessageCount()); System.out.println("No of Unread Messages : "
	 * + folder.getUnreadMessageCount()); System.out.println(messages.length);
	 * for (int i = 0; i < messages.length; i++) {
	 * 
	 * System.out.println(
	 * "*****************************************************************************"
	 * ); System.out.println("MESSAGE " + (i + 1) + ":"); Message msg =
	 * messages[i];
	 * 
	 * logger.debug("Subject: " + msg.getSubject()); logger.debug("From: " +
	 * msg.getFrom()[0]); logger.debug("To: " + msg.getAllRecipients()[0]);
	 * logger.debug("Date: " + msg.getReceivedDate()); logger.debug("Size: " +
	 * msg.getSize()); logger.debug("Body: \n" + msg.getContent());
	 * logger.debug(msg.getContentType());
	 * 
	 * } }
	 */

}
