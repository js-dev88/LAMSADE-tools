package com.github.lantoine.lamsadetools.graphical_interface;

import java.nio.file.FileSystems;
import java.util.prefs.Preferences;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Prefs {
	private static final Preferences graphical_prefs = Preferences.userRoot().node("graphical_prefs :");
	private static final Logger LOGGER = LoggerFactory.getLogger(Prefs.class);

	/**
	 * @return a String containing the str_save_dir
	 */
	public static String getSaveDir() {
		return graphical_prefs.get("save_dir", FileSystems.getDefault().getPath("").toAbsolutePath().toString());
	}

	/**
	 * @param str_save_dir
	 *            the str_save_dir to save in the preferences
	 */
	public static void setSaveDir(String str_save_dir) {
		graphical_prefs.put("save_dir", str_save_dir);
		LOGGER.debug("pref for save directory set to : " + str_save_dir);
	}

}
