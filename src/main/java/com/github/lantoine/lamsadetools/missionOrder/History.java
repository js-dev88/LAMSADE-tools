package com.github.lantoine.lamsadetools.missionOrder;

import java.awt.Desktop;
import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;

/**
 * This class will get the Order missions History It's used in a static way
 *
 */
public class History {

	/**
	 * Gives the Young Searcher order missions saved in the path
	 * 
	 * @param path
	 * @return
	 */
	public static File[] getYSOMHistory() {
		String path = FileSystems.getDefault().getPath("").toAbsolutePath() + "/historique_DJC";
		File folder = new File(path);
		File[] ysHistory = folder.listFiles(new FilenameFilter() {
			@Override
			public boolean accept(File dir, String name) {
				return name.endsWith(".fodt");
			}
		});

		return ysHistory;
	}

	/**
	 * Open a file with Desktop API
	 * 
	 * @param name
	 * @param isYc
	 *            is Young Searcher
	 * @throws IOException
	 */
	public static void openFile(String name, Boolean isYc) throws IOException {
		String folder;
		if (isYc) {
			folder = "/historique_DJC/";
		} else {
			folder = "/historique_OM/";
		}
		String path = FileSystems.getDefault().getPath("").toAbsolutePath() + folder + name;
		File file = new File(path.replace("\\", "/"));
		Desktop desktop = Desktop.getDesktop();
		desktop.open(file);
	}

	/**
	 * Delete a file from the folder
	 * 
	 * @param name
	 *            of the file
	 */
	public static void deleteFile(String name, Boolean isYc) {
		String folder;
		if (isYc) {
			folder = "/historique_DJC/";
		} else {
			folder = "/historique_OM/";
		}
		String path = FileSystems.getDefault().getPath("").toAbsolutePath() + folder + name;
		File fileToDelete = new File(path.replace("\\", "/"));
		System.out.println(path.replace("\\", "/"));
		fileToDelete.delete();
	}

	/**
	 * Gives the Searcher order missions saved in the path
	 * 
	 * @param path
	 * @return
	 */
	public static File[] getOMHistory() {
		String path2 = FileSystems.getDefault().getPath("").toAbsolutePath() + "/historique_OM";
		File folder = new File(path2);
		File[] history = folder.listFiles(new FilenameFilter() {
			@Override
			public boolean accept(File dir, String name) {
				return name.endsWith(".ods");
			}
		});
		return history;
	}

	public static String getFilePath(String nameOfFile, Boolean isYc) {
		String pathOfFile;
		String folder;
		if (isYc) {
			folder = "/historique_DJC/";
		} else {
			folder = "/historique_OM/";
		}
		pathOfFile = FileSystems.getDefault().getPath("").toAbsolutePath() + folder + nameOfFile;

		return pathOfFile.replace("\\", "/");
	}

}
