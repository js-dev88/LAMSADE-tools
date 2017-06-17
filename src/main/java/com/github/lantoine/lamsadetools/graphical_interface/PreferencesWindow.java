package com.github.lantoine.lamsadetools.graphical_interface;

import java.nio.file.FileSystems;
import java.util.prefs.Preferences;

import org.eclipse.swt.SWT;
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
	private static String str_working_dir;
	private static Text text_working_dir;

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

		str_working_dir = prefs.get("working_dir", FileSystems.getDefault().getPath("").toAbsolutePath().toString());

		lbl_directory = new Label(shell, SWT.PUSH);
		lbl_directory.setText("default output directory");

		text_working_dir = new Text(shell, SWT.PUSH);
		text_working_dir.setText(str_working_dir);

		Button btnSaveOrdreMission = new Button(shell, SWT.NONE);
		btnSaveOrdreMission.setText("Browse");
		btnSaveOrdreMission.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				DirectoryDialog fileDialog = new DirectoryDialog(shell, SWT.OPEN);
				LOGGER.info("Opening the file dialog for user to choose where he wants to store his files");
				String str_working_dir = fileDialog.open();
				LOGGER.info("Location chosen: " + str_working_dir);

				if (str_working_dir == null) {
					LOGGER.info("User closed the directory dialog");
				} else {
					prefs.put("working_dir", str_working_dir);// store the
																// absolute
																// path
					text_working_dir.setText(str_working_dir);
					shell.pack(true);
					LOGGER.debug("directory set to" + str_working_dir);
				}
			}
		});
		shell.pack();
		shell.open();
	}
}
