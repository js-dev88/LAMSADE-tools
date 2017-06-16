package com.github.lantoine.lamsadetools.graphical_interface;

import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

public class PreferencesWindow {
	static void open(Display display) {
		Shell shellPreference = new Shell(display);
		shellPreference.open();
	}
}
