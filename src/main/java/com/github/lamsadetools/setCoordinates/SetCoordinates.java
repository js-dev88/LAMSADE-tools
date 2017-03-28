package com.github.lamsadetools.setCoordinates;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.util.List;
import java.util.Scanner;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.xwpf.model.XWPFHeaderFooterPolicy;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;
import org.artofsolving.jodconverter.office.OfficeException;

public class SetCoordinates implements AbstractFileConvertor {

	/**
	 * Duplicate a file
	 *
	 * @param source
	 * @param target
	 * @throws IOException
	 */
	public static void copy(File source, File target) throws IOException {
		FileChannel sourceChannel = null;
		FileChannel targetChannel = null;
		try {
			sourceChannel = new FileInputStream(source).getChannel();
			targetChannel = new FileOutputStream(target).getChannel();
			targetChannel.transferFrom(sourceChannel, 0, sourceChannel.size());
		} finally {
			if (targetChannel != null)
				targetChannel.close();
			if (sourceChannel != null)
				sourceChannel.close();
		}
	}

	public static void main(String[] args) throws Exception {
		String source = "papier_en_tete.docx";
		String destination = "papier_en_tete_CloneTest.docx";

		UserDetails user = promptUserDetails();
		setDetails(source, destination, user);
		System.out.println("Done");

	}

	/**
	 * prompt the user for its names, phone, ...
	 *
	 * @return return a user with all the asked informations
	 */
	public static UserDetails promptUserDetails() {
		String tableauQuestion[] = { "Your name ?", "Your first name?", "Your telephone number?", "Your mail?",
				"Your function?" };
		Scanner sc = new Scanner(System.in);
		UserDetails user = new UserDetails();
		for (int i = 0; i <= (tableauQuestion.length - 1); i++) {

			System.out.println(tableauQuestion[i]);

			if (tableauQuestion[i].equals("Your telephone number?")) {

				user.setNumber(sc.nextLine());

			} else if (tableauQuestion[i].equals("Your name ?")) {

				user.setName(sc.nextLine());

			} else if (tableauQuestion[i].equals("Your first name?")) {

				user.setFirstName(sc.nextLine());

			} else if (tableauQuestion[i].equals("Your function?")) {

				user.setFunction(sc.nextLine());
			} else if (tableauQuestion[i].equals("Your mail?")) {

				user.setEmail(sc.nextLine());

			}
		}

		sc.close();
		return user;
	}

	/**
	 * Add the user details to the source file and return the result in the
	 * destination file
	 *
	 * @param source
	 * @param destinationFile
	 * @param user
	 * @throws IOException
	 * @throws InvalidFormatException
	 */
	public static void setDetails(String source, String destination, UserDetails user)
			throws IOException, InvalidFormatException {

		XWPFDocument doc = new XWPFDocument(OPCPackage.open(source));
		XWPFHeaderFooterPolicy policy = doc.getHeaderFooterPolicy();
		File sourceFile = new File(source);
		File destinationFile = new File(destination);
		copy(sourceFile, destinationFile);

		for (XWPFParagraph p : policy.getFirstPageHeader().getParagraphs()) {
			List<XWPFRun> runs = p.getRuns();
			if (runs != null) {
				for (XWPFRun r : runs) {
					String text = r.getText(0);

					if ((text != null) && text.contains("Prenom")) {
						text = text.replace("Prenom", user.getFirstName());

						r.setText(text, 0);

					}

					else if ((text != null) && text.contains("Nom")) {
						text = text.replace("Nom", user.getName());

						r.setText(text, 0);

					}

					else if ((text != null) && text.contains("e-mail")) {
						text = text.replace("e-mail", user.getEmail());

						r.setText(text, 0);

					}

					else if ((text != null) && text.contains("tel.")) {
						text = text.replace("tel.", user.getNumber());

						r.setText(text, 0);

					}

					else if ((text != null) && text.contains("Fonction")) {
						text = text.replace("Fonction", user.getFunction());

						r.setText(text, 0);

					}

				}
			}
		}
		doc.write(new FileOutputStream(destination));
		doc.close();

		long start = System.currentTimeMillis();

		System.err.println("Generate papier_en_tete.html with " + (System.currentTimeMillis() - start) + "ms");
	}

	@Override
	public void convertToOdt(File source, File destination) throws OfficeException {
		// TODO Auto-generated method stub

	}

}