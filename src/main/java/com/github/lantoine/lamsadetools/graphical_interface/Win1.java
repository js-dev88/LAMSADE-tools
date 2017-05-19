package com.github.lantoine.lamsadetools.graphical_interface;

import org.eclipse.swt.SWT;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

public class Win1 {

	public Win1(Display display) {

		initUI(display);
	}

	private void initUI(Display display) {

		// Display display = new Display();
		Shell shell = new Shell(display, SWT.SHELL_TRIM | SWT.CENTER);

		Color col = new Color(display, 100, 220, 150);
		shell.setBackground(col);
		col.dispose();

		GridLayout layout = new GridLayout(2, false);
		shell.setLayout(layout);

		Label lbl1 = new Label(shell, SWT.NONE);
		Composite composite = new Composite(shell, SWT.FILL);
		lbl1.setLayoutData(composite);

		Color col1 = new Color(display, 255, 155, 10);
		lbl1.setBackground(col1);
		col1.dispose();

		Text txtTest = new Text(composite, SWT.NONE);
		txtTest.setText("Testing");

		Text txtMoreTests = new Text(composite, SWT.NONE);
		txtMoreTests.setText("Another test");

		Button button = new Button(composite, SWT.PUSH);
		button.setText("Press Me");
		button.addListener(SWT.Selection, new Listener() {
			@Override
			public void handleEvent(Event e) {
				switch (e.type) {
				case SWT.Selection:
					System.out.println("Button pressed");
					break;
				default:
					break;
				}
			}
		});

		Label lbl3 = new Label(shell, SWT.NONE);
		GridData gd3 = new GridData(SWT.FILL, SWT.FILL, true, true);
		gd3.widthHint = 1000;
		gd3.heightHint = 500;
		gd3.horizontalSpan = 2;
		lbl3.setLayoutData(gd3);

		Color col3 = new Color(display, 100, 205, 200);
		lbl3.setBackground(col3);
		col3.dispose();

		shell.setText("Grid");
		shell.pack();
		shell.open();

		while (!shell.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
	}

	@SuppressWarnings("unused")
	public static void main(String[] args) {

		Display display = new Display();
		Win1 ex = new Win1(display);
		display.dispose();
	}

}
