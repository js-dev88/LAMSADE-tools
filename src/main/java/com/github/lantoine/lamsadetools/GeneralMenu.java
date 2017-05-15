package com.github.lantoine.lamsadetools;

import java.util.Scanner;

import com.github.lantoine.lamsadetools.conferences.database.ConferenceDatabasePrompter;
import com.github.lantoine.lamsadetools.setCoordinates.SetCoordinates;
import com.github.lantoine.lamsadetools.setCoordinates.UserDetails;
import com.github.lantoine.lamsadetools.yearbookInfos.GetInfosFromYearbook;

public class GeneralMenu {
	/**
	 * @throws Throwable
	 *
	 */
	public static void fillAForm() throws Throwable {
		UserDetails userDetails = GetInfosFromYearbook.getUserDetails();
		if (userDetails.isFilled())
			SetCoordinates.fillPapierEnTete(userDetails);
		mainMenu();
	}

	public static void main(String[] args) throws Throwable {
		mainMenu();

	}

	/**
	 * ask the user what he wants to do
	 *
	 * @throws Throwable
	 */
	public static void mainMenu() throws Throwable {
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
			ConferenceDatabasePrompter.menu();
			break;
		default:
			break;
		}
		
	}
}
