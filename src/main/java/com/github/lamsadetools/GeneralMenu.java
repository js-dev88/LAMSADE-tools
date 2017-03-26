package com.github.lamsadetools;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Scanner;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;

import com.github.lamsadetools.conferences.Conference;
import com.github.lamsadetools.setCoordinates.SetCoordinates;
import com.github.lamsadetools.setCoordinates.UserDetails;
import com.github.lamsadetools.yearbookInfos.GetInfosFromYearbook;
import com.sun.star.lang.IllegalArgumentException;

public class GeneralMenu {
	/**
	 * @throws IOException
	 * @throws IllegalArgumentException
	 * @throws InvalidFormatException
	 *
	 */
	public static void fillAForm() throws IllegalArgumentException, IOException, InvalidFormatException {
		//UserDetails userDetails = GetInfosFromYearbook.getUserDetails();
		Scanner sc = new Scanner(System.in);
		System.out.println("source file:");
		String source = sc.nextLine();
		System.out.println("destination file:");
		String destination = sc.nextLine();
		//SetCoordinates.setDetails(source, destination, userDetails);
	}

	public static void main(String[] args)
			throws IllegalArgumentException, InvalidFormatException, SQLException, IOException {
		mainMenu();

	}

	/**
	 * ask the user what he wants to do
	 *
	 * @throws SQLException
	 * @throws IOException
	 * @throws InvalidFormatException
	 * @throws IllegalArgumentException
	 */
	public static void mainMenu() throws SQLException, IllegalArgumentException, InvalidFormatException, IOException {
		int option = 0;
		Scanner sc = new Scanner(System.in);
		System.out.println("1. Fill a form.");
		System.out.println("2. Access the conference database.");
		System.out.println("0. Exit");
		String optionstr = sc.nextLine();
		// Verify input is an integer
		try {
			option = Integer.parseInt(optionstr);
		} catch (NumberFormatException ex) {
			System.out.println("Please choose a valid option");
			option = -1;
		}

		switch (option) {
		case 1:
			GeneralMenu.fillAForm();
			break;
		case 2:
			Conference.menu();
			break;
		default:
			break;
		}
	}
}
