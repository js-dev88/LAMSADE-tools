package com.github.lamsadetools.graphical_interface;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;

import com.github.lamsadetools.conferences.Conference;
import com.github.lamsadetools.map.AddressInfos;
import com.github.lamsadetools.map.ItineraryMap;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Group;

public class Tester {
	private static Text txtDeparture;
	private static Text txtArrival;
	public static void main(String[] args) throws SQLException {

		Display display = new Display();
		Shell shell = new Shell(display);

		// shell.setSize(300, 300);
		shell.setText("Conference List");

		GridLayout gridLayout = new GridLayout();
		gridLayout.numColumns = 2;

		shell.setLayout(gridLayout);

		shell.setLocation(400, 200);
		
				Table table = new Table(shell, SWT.V_SCROLL);
				table.setHeaderVisible(true);
				
						GridData gridDataTable = new GridData();
						gridDataTable.widthHint = 673;
						gridDataTable.horizontalAlignment = GridData.FILL;
						gridDataTable.verticalAlignment = GridData.FILL;
						gridDataTable.grabExcessHorizontalSpace = true;
						
								table.setLayoutData(gridDataTable);

		String[] titles = { "Title", "URL", "Start Date", "End Date", "Fee" };

		ArrayList<Conference> confs = Conference.returnAllConferencesFromDatabase();

		for (String title : titles) {
			System.out.println(title);
			TableColumn column = new TableColumn(table, SWT.NULL);
			column.setText(title);
		}

		for (Conference i : confs) {
			TableItem item = new TableItem(table, SWT.NULL);
			item.setText(0, i.getTitle());
			System.out.println(item.getText(0));
			item.setText(1, i.getUrl());
			item.setText(2, convertLocaldateToString(i.getStart_date()));
			item.setText(3, convertLocaldateToString(i.getEnd_date()));
			item.setText(4, Double.toString(i.getEntry_fee()));
		}
		

		for (int loopIndex = 0; loopIndex < titles.length; loopIndex++) {
			table.getColumn(loopIndex).pack();
		}
		shell.layout(true, true);

		final Point newSize = shell.computeSize(SWT.DEFAULT, SWT.DEFAULT, true);

		shell.setSize(new Point(1005, 381));
		new Label(shell, SWT.NONE);
		
		Group group = new Group(shell, SWT.NONE);
		group.setText ("Visualize your travel");
		GridData gd_group = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_group.heightHint = 85;
		gd_group.widthHint = 668;
		group.setLayoutData(gd_group);
		
		txtDeparture = new Text(group, SWT.BORDER);
		txtDeparture.setBounds(20, 22, 64, 21);
		txtDeparture.setText("Departure");
		Text departure = new Text(group, SWT.BORDER);
		departure.setLocation(450, 22);
		departure.setSize(196,21);
		
		txtArrival = new Text(group, SWT.BORDER);
		txtArrival.setBounds(365, 22, 64, 21);
		txtArrival.setText("Arrival");
		Text arrival = new Text(group, SWT.BORDER);
		arrival.setLocation(99, 22);
		arrival.setSize(196,21);
		
		Button button = new Button(group, SWT.NONE);
		button.setText("Validate");
		button.setBounds(597, 49, 48, 25);
		button.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				System.out.println("Appui sur le bouton");
				if (!departure.getText().isEmpty() && !arrival.getText().isEmpty()){
					try {
						AddressInfos dep = new AddressInfos(departure.getText());
						AddressInfos arr = new AddressInfos(arrival.getText());
						ItineraryMap itinerary = new ItineraryMap(dep.getLongitude(), dep.getLatitude(), 
								arr.getLongitude(), arr.getLatitude());
						//LOGGER.info("\n" + paris.toString());
						//LOGGER.info("\n" + cologne.toString());
						String url = itinerary.setMapUrl();
						System.out.println(url);
						itinerary.openMapUrl(url);
									
					} catch (IllegalArgumentException | IOException  e) {
						System.out.println("Error : "+ e);
					}
				}
				else {
					MessageBox mb = new MessageBox(shell, SWT.ICON_ERROR | SWT.OK);
					mb.setText("Infromation missing");
					mb.setMessage("Please fill in the departure and arrival fields");				
				}
					
			}
		});
		arrival.addModifyListener(new ModifyListener() {
			@Override
			public void modifyText(ModifyEvent e) {
				System.out.println("nouvelle valeur = " + ((Text)e.widget).getText());
			}
		});
		departure.addModifyListener(new ModifyListener() {
			@Override
			public void modifyText(ModifyEvent e) {
			System.out.println("nouvelle valeur = " + ((Text)e.widget).getText());
			}
		});
		
				// What's the second parameter for ?
			/*	final Button button1 = new Button(shell, SWT.RIGHT);
				
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
						});*/

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
