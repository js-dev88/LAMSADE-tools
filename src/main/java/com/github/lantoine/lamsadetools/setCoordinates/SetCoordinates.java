package com.github.lantoine.lamsadetools.setCoordinates;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.channels.FileChannel;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.xwpf.model.XWPFHeaderFooterPolicy;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;

import com.github.lantoine.lamsadetools.conferences.IO;

public class SetCoordinates {

	/**
	 * Duplicate a file
	 *
	 * @param source
	 * @param target
	 * @throws IOException
	 */
	public static void copy(File source, File target) throws IOException {
		// try with resources
		try (FileInputStream FIS = new FileInputStream(source); FileOutputStream FOS = new FileOutputStream(target);) {
			try (FileChannel sourceChannel = FIS.getChannel(); FileChannel targetChannel = FOS.getChannel();) {
				targetChannel.transferFrom(sourceChannel, 0, sourceChannel.size());
			}
		}
	}

	/**
	 * fill the paper with header
	 *
	 * @param user
	 * @return path where the file is saved
	 * @throws Exception
	 */
	public static String fillPapierEnTete(UserDetails user, Path path) throws Exception {
		System.out.println("The File will be saved in: " + path.toAbsolutePath());

		// define source and target
		ClassLoader classLoader = SetCoordinates.class.getClassLoader();
		String source = classLoader.getResource("com/github/lantoine/lamsadetools/setCoordinates/papier_a_en_tete.fodt")
				.getPath();
		String target = path.toAbsolutePath() + "/papier_a_en_tete_Clone.fodt";

		// open source and copy it in target only changing what needs to be
		// changed
		FileOutputStream fos = new FileOutputStream(target);
		try (PrintStream printStream = new PrintStream(fos)) {
			try (BufferedReader brSource = new BufferedReader(new FileReader(source))) {
				// copy lines until 36763 because we want to keep them
				for (int i = 0; i < 36762; i++) {
					String lineSource = brSource.readLine();
					printStream.println(lineSource);
				}

				// change the next 2 lines with user's informations
				// 1st line
				String lineSource = brSource.readLine();
				String regex = "Nom";
				Pattern p = Pattern.compile(regex);
				Matcher m = p.matcher(lineSource);
				lineSource = m.replaceFirst(user.getName());
				regex = "Prenom";
				p = Pattern.compile(regex);
				m = p.matcher(lineSource);
				lineSource = m.replaceFirst(user.getFirstName());
				regex = "Tel";
				p = Pattern.compile(regex);
				m = p.matcher(lineSource);
				lineSource = m.replaceFirst(user.getNumber());
				printStream.println(lineSource);

				// 2nd line
				lineSource = brSource.readLine();
				regex = "Fonction";
				p = Pattern.compile(regex);
				m = p.matcher(lineSource);
				lineSource = m.replaceFirst(user.getFunction());
				regex = "E-mail";
				p = Pattern.compile(regex);
				m = p.matcher(lineSource);
				lineSource = m.replaceFirst(user.getEmail());
				printStream.println(lineSource);

				// copy lines until the end because we want to keep them
				while ((lineSource = brSource.readLine()) != null) {
					printStream.println(lineSource);
				}
			}
		}
		System.out.println("PapierEnTete generated");
		return path.toAbsolutePath().toString();
	}

	public static void main(String[] args) throws Exception {
		UserDetails user = promptUserDetails();
		fillPapierEnTete(user);

	}

	/**
	 * fill the paper with header
	 *
	 * @param user
	 * @return path where the file is saved
	 * @throws Exception
	 */
	public static String fillPapierEnTete(UserDetails user) throws Exception {
		Path path = FileSystems.getDefault().getPath("");
		return fillPapierEnTete(user, path);
	}

	/**
	 * prompt the user for its names, phone, ...
	 *
	 * @return return a user with all the asked informations
	 */
	public static UserDetails promptUserDetails() {
		String tableauQuestion[] = { "Your name ?", "Your first name?", "Your telephone number?", "Your mail?",
				"Your function?" };
		IO io = new IO();
		io.scanner = new Scanner(System.in);
		UserDetails user = new UserDetails();
		for (int i = 0; i <= tableauQuestion.length - 1; i++) {

			System.out.println(tableauQuestion[i]);

			if (tableauQuestion[i].equals("Your telephone number?")) {

				user.setNumber(io.scanner.nextLine());

			} else if (tableauQuestion[i].equals("Your name ?")) {

				user.setName(io.scanner.nextLine());

			} else if (tableauQuestion[i].equals("Your first name?")) {

				user.setFirstName(io.scanner.nextLine());

			} else if (tableauQuestion[i].equals("Your function?")) {

				user.setFunction(io.scanner.nextLine());
			} else if (tableauQuestion[i].equals("Your mail?")) {

				user.setEmail(io.scanner.nextLine());

			}
		}
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
	public static void setDetails(UserDetails user) throws IOException, InvalidFormatException {
		String source = "com/github/lamsadetools/setCoordinates/papier_en_tete.docx";
		String destination = "com/github/lamsadetools/setCoordinates/papier_en_tete_CloneTest.docx";

		ClassLoader classLoader = SetCoordinates.class.getClassLoader();

		File sourceFile = new File(classLoader.getResource(source).getFile());
		File destinationFile = new File(classLoader.getResource(destination).getFile());
		copy(sourceFile, destinationFile);

		try (XWPFDocument doc = new XWPFDocument(OPCPackage.open(sourceFile))) {
			XWPFHeaderFooterPolicy policy = doc.getHeaderFooterPolicy();

			for (XWPFParagraph p : policy.getFirstPageHeader().getParagraphs()) {
				List<XWPFRun> runs = p.getRuns();
				if (runs != null) {
					for (XWPFRun r : runs) {
						String text = r.getText(0);

						if (text != null && text.contains("Prenom")) {
							System.out.println("contains prenom");
							text = text.replace("Prenom", user.getFirstName());

							r.setText(text, 0);

						}

						else if (text != null && text.contains("Nom")) {
							text = text.replace("Nom", user.getName());

							r.setText(text, 0);

						}

						else if (text != null && text.contains("e-mail")) {
							text = text.replace("e-mail", user.getEmail());

							r.setText(text, 0);

						}

						else if (text != null && text.contains("tel.")) {
							text = text.replace("tel.", user.getNumber());

							r.setText(text, 0);

						}

						else if (text != null && text.contains("Fonction")) {
							text = text.replace("Fonction", user.getFunction());

							r.setText(text, 0);

						}

					}
				}
			}
			try (FileOutputStream fos = new FileOutputStream(destinationFile)) {
				doc.write(fos);
			}
		}

		long start = System.currentTimeMillis();

		System.err.println("Generate papier_en_tete.html with " + (System.currentTimeMillis() - start) + "ms");
	}

}