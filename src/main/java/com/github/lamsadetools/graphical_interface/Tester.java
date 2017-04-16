package com.github.lamsadetools.graphical_interface;

import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;

import com.github.lamsadetools.conferences.Conference;

public class Tester {
	public static void main(String[] args) throws SQLException {

		Display display = new Display();
		Shell shell = new Shell(display);

		// shell.setSize(300, 300);
		shell.setText("Conference List");

		GridLayout gridLayout = new GridLayout();
		gridLayout.numColumns = 4;

		shell.setLayout(gridLayout);

		shell.setLocation(400, 200);

		// What's the second parameter for ?
		final Button button1 = new Button(shell, SWT.RIGHT);

		Table table = new Table(shell, SWT.V_SCROLL);

		String[] titles = { "Title", "URL", "Start Date", "End Date", "Fee" };

		ArrayList<Conference> confs = Conference.returnAllConferencesFromDatabase();

		for (String title : titles) {
			System.out.println(title);
			TableColumn column = new TableColumn(table, SWT.NULL);
			column.setText(title);
		}
		table.setHeaderVisible(true);

		for (Conference i : confs) {
			TableItem item = new TableItem(table, SWT.NULL);
			item.setText(0, i.getTitle());
			item.setText(1, i.getUrl());
			item.setText(2, convertLocaldateToString(i.getStart_date()));
			item.setText(3, convertLocaldateToString(i.getEnd_date()));
			item.setText(4, Double.toString(i.getEntry_fee()));
		}

		for (int loopIndex = 0; loopIndex < titles.length; loopIndex++) {
			table.getColumn(loopIndex).pack();
		}

		GridData gridDataTable = new GridData();
		gridDataTable.horizontalAlignment = GridData.FILL;
		gridDataTable.verticalAlignment = GridData.FILL;
		gridDataTable.grabExcessHorizontalSpace = true;

		table.setLayoutData(gridDataTable);

		GridData gridData = new GridData();
		gridData.verticalAlignment = GridData.BEGINNING; // Options: BEGINNING,
															// CENTER, END, FILL
		button1.setLayoutData(gridData);

		button1.setText("Press me");

		button1.addListener(SWT.Selection, new Listener() {
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

		shell.layout(true, true);

		final Point newSize = shell.computeSize(SWT.DEFAULT, SWT.DEFAULT, true);

		shell.setSize(newSize);

		shell.open();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
		display.dispose();
	}

	private static String convertLocaldateToString(LocalDate localdate) {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd LLLL yyyy");
		String formattedDate = localdate.format(formatter);
		return formattedDate;
	}

}
