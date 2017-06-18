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

public class SetCoordinates {

	private static final Logger logger = LoggerFactory.getLogger(SetCoordinates.class);
	/**
	 * fill the paper with header
	 *
	 * @param user
	 * @return path where the file is saved
	 * @throws Exception
	 */
	public static String fillPapierEnTete(UserDetails user, Path path) throws Exception {
		logger.info("The File will be saved in: " + path.toAbsolutePath());

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
		logger.info("PapierEnTete generated");
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
	
}