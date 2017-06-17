package com.github.lantoine.lamsadetools.graphical_interface;

import java.nio.file.FileSystems;
import java.util.prefs.Preferences;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Prefs {
	private static final Preferences graphical_prefs = Preferences.userRoot().node("graphical_prefs :");
	private static final Logger LOGGER = LoggerFactory.getLogger(Prefs.class);

	/**
	 * @return the login
	 */
	public static String getLogin() {
		return graphical_prefs.get("login", "");
	}

	/**
	 * @return the name
	 */
	public static String getName() {
		return graphical_prefs.get("name", "");
	}

	/**
	 * @return a String containing the str_save_dir
	 */
	public static String getSaveDir() {
		return graphical_prefs.get("save_dir", FileSystems.getDefault().getPath("").toAbsolutePath().toString());
	}

	/**
	 * @return the surname
	 */
	public static String getSurname() {
		return graphical_prefs.get("surname", "");
	}

	/**
	 * @param login
	 *            the login to set
	 */
	public static void setLogin(String login) {
		graphical_prefs.put("login", login);
	}

	/**
	 * @param name
	 *            the name to set
	 */
	public static void setName(String name) {
		graphical_prefs.put("name", name);
	}

	/**
	 * @param str_save_dir
	 *            the str_save_dir to save in the preferences
	 */
	public static void setSaveDir(String str_save_dir) {
		graphical_prefs.put("save_dir", str_save_dir);
		LOGGER.debug("pref for save directory set to : " + str_save_dir);
	}

	/**
	 * @param surname
	 *            the surname to set
	 */
	public static void setSurname(String surname) {
		graphical_prefs.put("surname", surname);
	}

}
