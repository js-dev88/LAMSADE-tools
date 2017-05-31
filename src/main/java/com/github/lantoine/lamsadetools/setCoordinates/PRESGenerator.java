package com.github.lantoine.lamsadetools.setCoordinates;

import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.PrintStream;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.lantoine.lamsadetools.conferences.IO;

public class PRESGenerator

{

	private static final Logger logger = LoggerFactory.getLogger(PRESGenerator.class);

	/**
	 * Create a PRES document
	 *
	 * @param source
	 * @param target
	 * @throws Exception
	 */
	public static void main(String[] args) throws Exception {
		// OdfTextDocument textdoc = OdfTextDocument
		// .loadDocument("com/github/lamsadetools/setCoordinates/Premier_test_template.odt");
		//
		// /* Creation of search for every word needed to be replaced */
		// TextNavigation searchN;
		// TextNavigation searchP;
		// TextNavigation searchG;
		// TextNavigation searchE;
		// TextNavigation searchT;
		//
		// searchN = new TextNavigation("NOMR", textdoc);
		// searchP = new TextNavigation("PRENOMR", textdoc);
		// searchG = new TextNavigation("GRADER", textdoc);
		// searchE = new TextNavigation("TEMPSPARTIELLER", textdoc);
		// searchT = new TextNavigation("TEMPSPLEINR", textdoc);
		//
		// /* Verify all the word and replace them by the value */
		// while (searchN.hasNext()) {
		//
		// // TextSelection item = (TextSelection) searchN.nextSelection();
		//
		// // item.replaceWith("Test");
		//
		// }
		// while (searchP.hasNext()) {
		//
		// // TextSelection item = (TextSelection) searchP.nextSelection();
		//
		// // item.replaceWith("Test");
		//
		// }
		// while (searchG.hasNext()) {
		//
		// // TextSelection item = (TextSelection) searchG.nextSelection();
		//
		// // item.replaceWith("Test");
		//
		// }
		// while (searchE.hasNext()) {
		//
		// // TextSelection item = (TextSelection) searchE.nextSelection();
		//
		// // item.replaceWith("Test");
		//
		// }
		// while (searchT.hasNext()) {
		//
		// // TextSelection item = (TextSelection) searchT.nextSelection();
		//
		// // item.replaceWith("Test");
		//
		// }

		UserDetails user = promptUserDetails();
		fillPRES(user);
		logger.info("PRES generated");

	}

	public static void fillPRES(UserDetails user) throws Exception {
		Path path = FileSystems.getDefault().getPath("");
		System.out.println("The File will be saved in: " + path.toAbsolutePath());

		// define source and target
		ClassLoader classLoader = PRESGenerator.class.getClassLoader();
		String source = classLoader
				.getResource("com/github/lantoine/lamsadetools/PRESGenerator/Premier_test_template.fodt").getPath();
		String target = path.toAbsolutePath() + "/Premier_test_template_Clone.fodt";

		// open source and copy it in target only changing what needs to be
		// changed
		FileOutputStream fos = new FileOutputStream(target);
		try (PrintStream printStream = new PrintStream(fos)) {
			try (BufferedReader brSource = new BufferedReader(new FileReader(source))) {
				// copy lines until 2338 because we want to keep them
				for (int i = 0; i < 2323; i++) {
					String lineSource = brSource.readLine();
					printStream.println(lineSource);
				}

				// change the next lines with user's informations

				// Name line
				String lineSource = brSource.readLine();
				String regex = "NOMR";
				Pattern p = Pattern.compile(regex);
				Matcher m = p.matcher(lineSource);
				lineSource = m.replaceFirst(user.getName());
				printStream.println(lineSource);

				// Firstname line
				lineSource = brSource.readLine();
				regex = "PRENOMR";
				p = Pattern.compile(regex);
				m = p.matcher(lineSource);
				lineSource = m.replaceFirst(user.getFirstName());
				printStream.println(lineSource);

				// Grade line
				lineSource = brSource.readLine();
				regex = "GRADER";
				p = Pattern.compile(regex);
				m = p.matcher(lineSource);
				lineSource = m.replaceFirst(user.getGroup());
				printStream.println(lineSource);

				// Time line
				lineSource = brSource.readLine();
				regex = "TPSPLR";
				p = Pattern.compile(regex);
				m = p.matcher(lineSource);
				lineSource = m.replaceFirst(user.getFunction());
				printStream.println(lineSource);

				// copy lines until the end because we want to keep them
				while ((lineSource = brSource.readLine()) != null) {
					printStream.println(lineSource);
				}
			}
		}
	}

	public static UserDetails promptUserDetails() {
		String tableauQuestion[] = { "Your name ?", "Your first name?", "Your grade?", "Full time job ?" };
		IO io = new IO();
		io.scanner = new Scanner(System.in);
		UserDetails user = new UserDetails();
		for (int i = 0; i <= tableauQuestion.length - 1; i++) {

			System.out.println(tableauQuestion[i]);

			if (tableauQuestion[i].equals("Your grade?")) {

				user.setGroup(io.scanner.nextLine());

			} else if (tableauQuestion[i].equals("Your name ?")) {

				user.setName(io.scanner.nextLine());

			} else if (tableauQuestion[i].equals("Your first name?")) {

				user.setFirstName(io.scanner.nextLine());

			} else if (tableauQuestion[i].equals("Full time job ?")) {

				user.setFunction(io.scanner.nextLine());
			}
		}
		return user;
	}

}
