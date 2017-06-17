package com.github.lantoine.lamsadetools.graphical_interface;

import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.prefs.Preferences;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.DirectoryDialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PreferencesWindow {
	private static Label lbl_directory;
	private static final Logger LOGGER = LoggerFactory.getLogger(PreferencesWindow.class);
	private static Text text_save_dir;

	/**
	 * Check if a path exists
	 *
	 * @param str_path
	 *            path to check
	 * @return true if path exists
	 */
	protected static boolean checkPath(String str_path) {
		Path path = FileSystems.getDefault().getPath(str_path);
		if (Files.exists(path)) {
			LOGGER.debug("path " + str_path + " is correct");
			return true;
		}
		LOGGER.debug("path " + str_path + " is incorrect");
		return false;
	}

	/**
	 * @wbp.parser.entryPoint
	 */
	public static void main() {
		System.setProperty("SWT_GTK3", "0");
		Display display = new Display();
		open(display);
	}

	static void open(Display display) {
		// This will define a node in which the preferences can be stored
		Preferences prefs = Preferences.userRoot().node("graphical_prefs :");
		Shell shell = new Shell(display);
		RowLayout layout = new RowLayout();
		layout.wrap = false;
		shell.setLayout(layout);

		lbl_directory = new Label(shell, SWT.PUSH);
		lbl_directory.setText("default output directory");

		text_save_dir = new Text(shell, SWT.BORDER);
		text_save_dir.setText(Prefs.getSaveDir());
		text_save_dir.addModifyListener(new ModifyListener() {

			@Override
			public void modifyText(ModifyEvent e) {
				if (checkPath(text_save_dir.getText()))
					Prefs.setSaveDir(text_save_dir.getText());
			}

		});

		Button btnSaveOrdreMission = new Button(shell, SWT.NONE);
		btnSaveOrdreMission.setText("Browse");
		btnSaveOrdreMission.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				DirectoryDialog fileDialog = new DirectoryDialog(shell, SWT.OPEN);
				LOGGER.info("Opening the file dialog for user to choose where he wants to store his files");
				String str_save_dir = fileDialog.open();
				LOGGER.info("Location chosen: " + str_save_dir);

				if (str_save_dir == null) {
					LOGGER.info("User closed the directory dialog");
				} else {
					prefs.put("working_dir", str_save_dir);// store the
															// absolute
															// path
					text_save_dir.setText(str_save_dir);
					LOGGER.debug("directory set to" + str_save_dir);
				}
			}
		});
		shell.pack();
		shell.open();
	}
}
