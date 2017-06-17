package com.github.lantoine.lamsadetools.missionOrder;

import java.io.File;
import java.io.FilenameFilter;
import java.nio.file.FileSystems;

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
	public static File[] getYSOMHistory(String path) {
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
	 * Gives the Searcher order missions saved in the path
	 * 
	 * @param path
	 * @return
	 */
	public static File[] getOMHistory(String path) {
		File folder = new File(path);
		File[] history = folder.listFiles(new FilenameFilter() {
			@Override
			public boolean accept(File dir, String name) {
				return name.endsWith(".ods");
			}
		});
		return history;
	}

	public static void main(String[] args) {
		String path = FileSystems.getDefault().getPath("").toAbsolutePath() + "/historique_DJC";
		File[] ysHistory = History.getYSOMHistory(path);

		if (!(ysHistory.length == 0)) {
			for (int i = 0; i < ysHistory.length; ++i) {
				System.out.println(ysHistory[i].getName() + "\n");
			}
		}

		String path2 = FileSystems.getDefault().getPath("").toAbsolutePath() + "/historique_OM";
		File[] history = History.getOMHistory(path2);

		if (!(ysHistory.length == 0)) {
			for (int i = 0; i < history.length; ++i) {
				System.out.println(history[i].getName() + "\n");
			}
		}
	}
}
