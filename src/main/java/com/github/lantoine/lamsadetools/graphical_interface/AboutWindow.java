package com.github.lantoine.lamsadetools.graphical_interface;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

public class AboutWindow {
	private static StyledText styledText1;
	private static StyledText styledText2;
	private static StyledText styledText3;
	private static StyledText styledText4;
	private static StyledText styledText5;

	static void open(Display display) {
		Shell shell = new Shell(display);
		GridLayout layout = new GridLayout();

		shell.setLayout(layout);
		styledText1 = new StyledText(shell, SWT.NONE);
		styledText1.setText("LAMSADE-tools");
		styledText2 = new StyledText(shell, SWT.NONE);
		styledText2.setText("1.0.0");
		styledText3 = new StyledText(shell, SWT.NONE);
		styledText3.setText("Do all the boring things for you");
		styledText4 = new StyledText(shell, SWT.NONE);
		styledText4.setText("Copyright LAMASADE-tools authors");
		styledText5 = new StyledText(shell, SWT.NONE);
		styledText5.setText("https://github.com/LAntoine/LAMSADE-tools");

		shell.open();
	}
}
