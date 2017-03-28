package com.github.lamsadetools.graphical_interface;

import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

public class Tester {
	public static void main(String[] args) {
		Display display = new Display();
		Shell shell = new Shell(display);
		// … populate shell …
		shell.open();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
		display.dispose();
	}

}
