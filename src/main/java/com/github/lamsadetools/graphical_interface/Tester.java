package com.github.lamsadetools.graphical_interface;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;

public class Tester {

	public static void main(String[] args) {
		Display display = new Display();
		Shell shell = new Shell(display);

		shell.setSize(300, 300);
		shell.setText("Example");
		// What are layouts ?
		shell.setLayout(new GridLayout());

		shell.setLocation(400, 200);

		// What's the second parameter for ?
		final Button button = new Button(shell, SWT.RIGHT);
		button.setText("Click me or die!");
		button.addListener(SWT.Selection, new Listener() {
			@Override
			public void handleEvent(Event e) {
				switch (e.type) {
				case SWT.Selection:
					System.out.println("Button pressed");
					break;
				default:
				}
			}
		});

		shell.open();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
		display.dispose();
	}

}
