package com.github.lantoine.lamsadetools.graphical_interface;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;

import javax.xml.parsers.ParserConfigurationException;

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
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.SAXException;

import com.github.lantoine.lamsadetools.conferences.Conference;
import com.github.lantoine.lamsadetools.conferences.database.ConferenceDatabase;
import com.github.lantoine.lamsadetools.map.AddressInfos;
import com.github.lantoine.lamsadetools.map.GoogleItineraryMap;
import com.github.lantoine.lamsadetools.setCoordinates.SetCoordinates;
import com.github.lantoine.lamsadetools.setCoordinates.UserDetails;
import com.github.lantoine.lamsadetools.utils.Util;
import com.github.lantoine.lamsadetools.yearbookInfos.GetInfosFromYearbook;
import com.github.lantoine.lamsadetools.yearbookInfos.YearbookDataException;

import net.fortuna.ical4j.data.ParserException;
import net.fortuna.ical4j.model.ValidationException;

/**
 * The Tester class is the interface of the LAMSADE-Tools application It allows
 * the user : -to see all the existing conferences, -to add new ones -to see the
 * itinerary on openStreetMap
 */
public class Tester {
	private static final Logger LOGGER = LoggerFactory.getLogger(Tester.class);
	private static Shell shell;
	private static Text txt_city_ud;
	private static Text txt_country_ud;
	private static Text txt_email;
	private static Text txt_fax;
	private static Text txt_firstname;
	private static Text txt_function;
	private static Text txt_group;
	private static Text txt_lastname;
	private static Text txt_number;
	private static Text txt_office;

	/**
	 * Converts a LocalDate passed by parameter into a string
	 *
	 * @param localdate
	 *            the date to be converted
	 * @return
	 */
	private static String convertLocaldateToString(LocalDate localdate) {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/LLLL/yyyy");
		String formattedDate = localdate.format(formatter);
		return formattedDate;
	}

	/**
	 * fillConferenceTable takes a table in parameter that will be filled with
	 * all the conferences information
	 *
	 * @param table
	 * @throws SQLException
	 */
	private static void fillConferenceTable(Table table) throws SQLException {
		String[] titles = { "Title", "URL", "Start Date", "End Date", "Fee", "City", "Address" };
		ArrayList<Conference> confs = ConferenceDatabase.returnAllConferencesFromDatabase();

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
			item.setText(5, i.getCity());
			item.setText(6, i.getCountry());
		}

		for (int loopIndex = 0; loopIndex < titles.length; loopIndex++) {
			table.getColumn(loopIndex).pack();
		}

	}

	/**
	 *
	 * @return user informations, returns null if failed to get the informations
	 */
	public static UserDetails getUserDetails() {
		if (txt_firstname.getText().isEmpty() || txt_lastname.getText().isEmpty()) {
			// MessageBox mb = new MessageBox(shell, SWT.ICON_ERROR | SWT.OK);
			// mb.setText("Information missing");
			// mb.setMessage("Please fill name and first name");
			// mb.open();
			return null;
		}

		UserDetails user = null;
		if (txt_office.getText().isEmpty() || txt_number.getText().isEmpty() || txt_group.getText().isEmpty()
				|| txt_function.getText().isEmpty() || txt_fax.getText().isEmpty() || txt_email.getText().isEmpty()
				|| txt_country_ud.getText().isEmpty() || txt_city_ud.getText().isEmpty()) {
			try {
				LOGGER.debug("Launching GetInfosFromYearbook.getUserDetails with : " + txt_lastname.getText() + " and "
						+ txt_firstname.getText());
				user = GetInfosFromYearbook.getUserDetails(txt_lastname.getText(), txt_firstname.getText());
			} catch (com.sun.star.lang.IllegalArgumentException | IOException | YearbookDataException | SAXException
					| ParserConfigurationException e) {
				LOGGER.error("getUserDetails: an error occurend while getting the informations");
				MessageBox mb = new MessageBox(shell, SWT.ICON_ERROR | SWT.OK);
				mb.setText("Error");
				mb.setMessage(
						"Could not get the informations from the yearbook\nThere is probably an error in the name or firstname");
				mb.open();
			}

		} else {
			user = new UserDetails(txt_firstname.getText(), txt_lastname.getText(), txt_function.getText(),
					txt_number.getText(), txt_email.getText(), txt_group.getText(), txt_fax.getText(),
					txt_office.getText(), txt_city_ud.getText(), txt_country_ud.getText());
		}
		return user;
	}

	public static void main(String[] args) throws SQLException {

		System.setProperty("SWT_GTK3", "0");
		Display display = new Display();
		shell = new Shell(display);

		shell.setText("Conference List");

		GridLayout gridLayout = new GridLayout();
		gridLayout.numColumns = 1;

		shell.setLayout(gridLayout);

		shell.setLocation(400, 200);

		shell.layout(true, true);

		final Point newSize = shell.computeSize(SWT.DEFAULT, SWT.DEFAULT, true);

		shell.setSize(new Point(888, 661));

		/*
		 * Initialize Group conferencesInfos which will include : -The Grid data
		 * which will display conferences
		 *
		 * -The fields that will allow the user to add a new conference in the
		 * database
		 */

		Group grpUserDetails = new Group(shell, SWT.NONE);
		GridData gd_grpUserDetails = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_grpUserDetails.widthHint = 860;
		gd_grpUserDetails.heightHint = 214;
		grpUserDetails.setLayoutData(gd_grpUserDetails);
		grpUserDetails.setText("User Details");

		Label lblFirstname = new Label(grpUserDetails, SWT.NONE);
		lblFirstname.setBounds(10, 26, 70, 15);
		lblFirstname.setText("First Name");

		Label lblNewLabel_1 = new Label(grpUserDetails, SWT.NONE);
		lblNewLabel_1.setBounds(10, 53, 70, 15);
		lblNewLabel_1.setText("Last Name");

		txt_firstname = new Text(grpUserDetails, SWT.BORDER);
		txt_firstname.setBounds(86, 23, 98, 21);

		txt_lastname = new Text(grpUserDetails, SWT.BORDER);
		txt_lastname.setBounds(86, 50, 98, 21);

		/*
		 * Handle the User Info's Search Throws exception if firstname or
		 * lastname is wrong
		 */

		Button btn_searchInfo = new Button(grpUserDetails, SWT.NONE);
		btn_searchInfo.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				try {
					LOGGER.debug("Launching GetInfosFromYearbook.getUserDetails with : " + txt_lastname.getText()
							+ " and " + txt_firstname.getText());
					UserDetails user = GetInfosFromYearbook.getUserDetails(txt_lastname.getText(),
							txt_firstname.getText());
					txt_function.setText(user.getFunction());
					txt_number.setText(user.getNumber());
					txt_email.setText(user.getEmail());
					txt_group.setText(user.getGroup());
					txt_fax.setText(user.getFax());
					txt_office.setText(user.getOffice());
					txt_city_ud.setText(user.getCity());
					txt_country_ud.setText(user.getCountry());
				} catch (@SuppressWarnings("unused") Exception e) {
					MessageBox mb = new MessageBox(shell, SWT.ICON_ERROR | SWT.OK);
					mb.setText("Error");
					mb.setMessage("Please verify firstname and lastname");
					mb.open();
				}
			}
		});
		btn_searchInfo.setBounds(26, 87, 158, 25);
		btn_searchInfo.setText("Search My Info");

		Label lbl_function = new Label(grpUserDetails, SWT.NONE);
		lbl_function.setBounds(224, 26, 55, 15);
		lbl_function.setText("Function");

		Label lbl_number = new Label(grpUserDetails, SWT.NONE);
		lbl_number.setBounds(224, 56, 55, 15);
		lbl_number.setText("Phone");

		Label lbl_email = new Label(grpUserDetails, SWT.NONE);
		lbl_email.setBounds(224, 92, 55, 15);
		lbl_email.setText("E-mail");

		Label lbl_group = new Label(grpUserDetails, SWT.NONE);
		lbl_group.setBounds(224, 124, 55, 15);
		lbl_group.setText("Group");

		txt_function = new Text(grpUserDetails, SWT.BORDER);
		txt_function.setBounds(285, 20, 219, 21);

		txt_number = new Text(grpUserDetails, SWT.BORDER);
		txt_number.setBounds(285, 53, 219, 21);

		txt_email = new Text(grpUserDetails, SWT.BORDER);
		txt_email.setBounds(285, 87, 219, 21);

		txt_group = new Text(grpUserDetails, SWT.BORDER);
		txt_group.setBounds(285, 121, 219, 21);

		Label lbl_fax = new Label(grpUserDetails, SWT.NONE);
		lbl_fax.setText("Fax");
		lbl_fax.setBounds(535, 26, 55, 15);

		Label lbl_office = new Label(grpUserDetails, SWT.NONE);
		lbl_office.setText("Office");
		lbl_office.setBounds(535, 59, 55, 15);

		Label lbl_city = new Label(grpUserDetails, SWT.NONE);
		lbl_city.setText("City");
		lbl_city.setBounds(535, 92, 55, 15);

		Label lbl_country = new Label(grpUserDetails, SWT.NONE);
		lbl_country.setText("Country");
		lbl_country.setBounds(535, 124, 55, 15);

		txt_fax = new Text(grpUserDetails, SWT.BORDER);
		txt_fax.setBounds(596, 23, 219, 21);

		txt_office = new Text(grpUserDetails, SWT.BORDER);
		txt_office.setBounds(596, 56, 219, 21);

		txt_city_ud = new Text(grpUserDetails, SWT.BORDER);
		txt_city_ud.setBounds(596, 89, 219, 21);

		txt_country_ud = new Text(grpUserDetails, SWT.BORDER);
		txt_country_ud.setBounds(596, 121, 219, 21);

		Button btnGeneratePapierEn = new Button(grpUserDetails, SWT.NONE);
		btnGeneratePapierEn.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				LOGGER.debug("Button clicked : Paper with header");
				UserDetails user = getUserDetails();
				if (user != null) {
					try {
						MessageBox mb = new MessageBox(shell, SWT.ICON_INFORMATION | SWT.OK);
						mb.setText("Success");
						mb.setMessage("file saved in : " + SetCoordinates.fillPapierEnTete(user));
						LOGGER.debug("SetCoordinates.fillPapierEnTete completed");
						mb.open();
					} catch (Exception e2) {
						throw new IllegalStateException(e2);
					}

				} else {
					LOGGER.error("Could not run SetCoordinates.fillPapierEnTete");
					MessageBox mb = new MessageBox(shell, SWT.ICON_ERROR | SWT.OK);
					mb.setText("Information missing");
					mb.setMessage("Please fill name and first name");
					mb.open();
				}
			}
		});

		Label lblPlaceholder = new Label(grpUserDetails, SWT.NONE);
		lblPlaceholder.setBounds(21, 190, 829, 14);
		lblPlaceholder.setText("");
		new Label(shell, SWT.NONE);

		btnGeneratePapierEn.setBounds(25, 118, 159, 28);
		btnGeneratePapierEn.setText("Generate Papier");

		Button btnSaveOrdreMission = new Button(grpUserDetails, SWT.NONE);
		btnSaveOrdreMission.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				FileDialog dialog = new FileDialog(shell, SWT.OPEN);
				LOGGER.info("Opening the file dialog for user to choose a file to save to the app");
				String dialogResult = dialog.open();
				LOGGER.info("File chosen: " + dialogResult);

				if (dialogResult == null) {
					LOGGER.info("User closed the file save dialog");
				} else {
					// Check if there exists a file with the same name in our
					// missions directory
					Path path = FileSystems.getDefault().getPath("");
					String pathToTargetFile = path.toAbsolutePath() + "/missions/" + new File(dialogResult).getName();
					File pathToProject = new File(pathToTargetFile);

					boolean exists = pathToProject.exists();
					if (exists == true) {
						LOGGER.info("Duplicate Detected");
						MessageBox mBox = new MessageBox(shell, SWT.ICON_QUESTION | SWT.OK | SWT.CANCEL);
						mBox.setText("Duplicate detected");
						mBox.setMessage("A file with that name already exists. Would you like to replace it?");
						int returnCode = mBox.open();
						if (returnCode == 256) {
							LOGGER.info("User chose not to replace the existing file");
						} else {
							LOGGER.info("User chose to replace the existing file");
							Util.saveFile(dialogResult);
							lblPlaceholder.setText("The file has successfully been saved to " + pathToTargetFile);

						}
					} else {
						LOGGER.info("Calling saveFile(String) to save the file to disk");
						Util.saveFile(dialogResult);
						lblPlaceholder.setText("The file has successfully been saved to " + pathToTargetFile);

					}
				}
			}
		});
		btnSaveOrdreMission.setBounds(26, 152, 158, 28);
		btnSaveOrdreMission.setText("Save Ordre Mission");

		// Group Conferences informations
		Group grp_conferencesInfos = new Group(shell, SWT.NONE);
		GridData gd_conferencesInfos = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_conferencesInfos.heightHint = 237;
		gd_conferencesInfos.widthHint = 860;
		grp_conferencesInfos.setLayoutData(gd_conferencesInfos);
		grp_conferencesInfos.setText("Conferences");

		Table table = new Table(grp_conferencesInfos, SWT.V_SCROLL);
		table.setBounds(165, 16, 502, 134);
		table.setHeaderVisible(true);
		fillConferenceTable(table);

		Button btn_addNewConf = new Button(grp_conferencesInfos, SWT.NONE);
		btn_addNewConf.setBounds(165, 156, 149, 25);
		btn_addNewConf.setText("Create conference");

		Label lblTitle = new Label(grp_conferencesInfos, SWT.NONE);
		lblTitle.setAlignment(SWT.RIGHT);
		lblTitle.setBounds(25, 16, 50, 15);
		lblTitle.setText("Title");
		Text txt_title = new Text(grp_conferencesInfos, SWT.BORDER);
		txt_title.setBounds(81, 15, 78, 21);

		Label lblUrl = new Label(grp_conferencesInfos, SWT.NONE);
		lblUrl.setAlignment(SWT.RIGHT);
		lblUrl.setText("URL");
		lblUrl.setBounds(25, 45, 50, 15);
		Text txt_url = new Text(grp_conferencesInfos, SWT.BORDER);
		txt_url.setBounds(81, 42, 78, 21);

		Label lblStartDate = new Label(grp_conferencesInfos, SWT.NONE);
		lblStartDate.setAlignment(SWT.RIGHT);
		lblStartDate.setText("Start Date");
		lblStartDate.setBounds(10, 72, 65, 15);
		Text txt_startDate = new Text(grp_conferencesInfos, SWT.BORDER);
		txt_startDate.setBounds(81, 69, 78, 21);

		Label lblEndDate = new Label(grp_conferencesInfos, SWT.NONE);
		lblEndDate.setAlignment(SWT.RIGHT);
		lblEndDate.setText("End Date");
		lblEndDate.setBounds(10, 99, 65, 15);
		Text txt_endDate = new Text(grp_conferencesInfos, SWT.BORDER);
		txt_endDate.setBounds(81, 96, 78, 21);

		Label lblFee = new Label(grp_conferencesInfos, SWT.NONE);
		lblFee.setText("Fee");
		lblFee.setBounds(42, 126, 33, 15);
		Text txt_fee = new Text(grp_conferencesInfos, SWT.BORDER);
		txt_fee.setBounds(81, 123, 78, 21);

		// add news fields

		/*
		 * Label lblCity= new Label(grp_conferencesInfos, SWT.NONE);
		 * lblCity.setAlignment(SWT.RIGHT); lblCity.setBounds(25, 38, 50, 15);
		 * lblCity.setText("City"); Text txt_city = new
		 * Text(grp_conferencesInfos, SWT.BORDER); txt_city.setBounds(81, 35,
		 * 78, 21);
		 */

		Label lblCity = new Label(grp_conferencesInfos, SWT.NONE);
		lblCity.setAlignment(SWT.RIGHT);
		lblCity.setBounds(25, 153, 50, 15);
		lblCity.setText("City");
		Text txt_city = new Text(grp_conferencesInfos, SWT.BORDER);
		txt_city.setBounds(81, 150, 78, 21);

		Label lbladdress = new Label(grp_conferencesInfos, SWT.NONE);
		lbladdress.setAlignment(SWT.RIGHT);
		lbladdress.setBounds(25, 180, 50, 15);
		lbladdress.setText("Address");
		Text txt_address = new Text(grp_conferencesInfos, SWT.BORDER);
		txt_address.setBounds(81, 177, 78, 21);

		// Create new Conference object, assign it values from
		// selection, then pass it function to export to desktop
		// public Conference(int id, String title, String url, LocalDate
		// start_date, LocalDate end_date, double entry_fee)
		Button btnExportEvent = new Button(grp_conferencesInfos, SWT.NONE);
		btnExportEvent.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {

				DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/M/yyyy");

				TableItem[] ti = table.getSelection();

				if (ti.length == 0) {
					// TODO: Put somewhere that an event should be selected
				} else {
					Conference conf = new Conference(ti[0].getText(0), ti[0].getText(1),
							LocalDate.parse(ti[0].getText(2), formatter), LocalDate.parse(ti[0].getText(3), formatter),
							Double.parseDouble(ti[0].getText(4)), ti[0].getText(5), ti[0].getText(6));
					try {
						FileDialog dialog = new FileDialog(shell, SWT.SAVE);
						dialog.setFilterNames(new String[] { "Calendar Files", "All Files (*.*)" });
						dialog.setFilterExtensions(new String[] { "*.ics", "*.*" }); // Windows

						switch (Util.getOS()) {
						case WINDOWS:
							dialog.setFilterPath("c:\\");
							break;
						case LINUX:
							dialog.setFilterPath("/");
							break;
						case MAC:
							dialog.setFilterPath("/");
							break;
						case SOLARIS:
							dialog.setFilterPath("/");
							break;
						default:
							dialog.setFilterPath("/");
						}

						dialog.setFileName("conference.ics");
						conf.generateCalendarFile(dialog.open());
					} catch (IOException | ValidationException | ParserException e2) {
						e2.printStackTrace();
					}
				}

			}
		});
		btnExportEvent.setBounds(320, 154, 104, 28);
		btnExportEvent.setText("Export Event");

		/*
		 * Behavior of a click on the add new conference button
		 */
		btn_addNewConf.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				System.out.println("Appui sur le bouton");
				if (!txt_title.getText().isEmpty() && !txt_url.getText().isEmpty() && !txt_startDate.getText().isEmpty()
						&& !txt_startDate.getText().isEmpty() && !txt_endDate.getText().isEmpty()
						&& !txt_fee.getText().isEmpty()) {

					try {
						DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
						LocalDate startDate = LocalDate.parse(txt_startDate.getText(), formatter);
						LocalDate endDate = LocalDate.parse(txt_endDate.getText(), formatter);
						Conference conf = new Conference(txt_title.getText(), txt_url.getText(), startDate, endDate,
								Double.parseDouble(txt_fee.getText()), txt_city.getText(), txt_address.getText());
						ConferenceDatabase.insertInDatabase(conf);
						/*
						 * Reload the conference table
						 */
						table.removeAll();
						fillConferenceTable(table);

					} catch (DateTimeParseException e1) {
						MessageBox mb = new MessageBox(shell, SWT.ICON_ERROR | SWT.OK);
						mb.setText("Date format error");
						mb.setMessage("Please fill the dates in the following format : dd/MM/YYYY");
						mb.open();
					} catch (SQLException e2) {
						throw new IllegalStateException(e2);
					}
				} else {
					MessageBox mb = new MessageBox(shell, SWT.ICON_ERROR | SWT.OK);
					mb.setText("Information missing");
					mb.setMessage("Please fill in the information regarding the conference");
					mb.open();
				}

			}
		});

		Group grp_map = new Group(shell, SWT.NONE);
		grp_map.setText("Visualize your travel");
		GridData gd_map = new GridData(SWT.LEFT, SWT.CENTER, false, true, 1, 1);
		gd_map.heightHint = 85;
		gd_map.widthHint = 412;
		grp_map.setLayoutData(gd_map);
		Text departure = new Text(grp_map, SWT.BORDER);
		departure.setLocation(101, 22);
		departure.setSize(196, 21);
		Text arrival = new Text(grp_map, SWT.BORDER);
		arrival.setLocation(101, 51);
		arrival.setSize(196, 21);

		Button btnItinerary = new Button(grp_map, SWT.NONE);
		btnItinerary.setText("Search");
		btnItinerary.setBounds(303, 49, 95, 25);

		Label lblDeparture = new Label(grp_map, SWT.NONE);
		lblDeparture.setAlignment(SWT.RIGHT);
		lblDeparture.setBounds(10, 25, 73, 15);
		lblDeparture.setText("Departure");

		Label lblArrival = new Label(grp_map, SWT.NONE);
		lblArrival.setAlignment(SWT.RIGHT);
		lblArrival.setBounds(10, 54, 73, 15);
		lblArrival.setText("Arrival");

		/*
		 * Behavior of the btnItinerary : Takes the addresses entered in
		 * departure and arrival Texts and call the ItineraryMap class to open
		 * the itinerary into the browser
		 */
		btnItinerary.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent arg0) {
				System.out.println("Appui sur le bouton");
				if (!departure.getText().isEmpty() && !arrival.getText().isEmpty()) {
					try {
						AddressInfos dep = new AddressInfos(departure.getText());
						AddressInfos arr = new AddressInfos(arrival.getText());
						// ItineraryMap itinerary = new
						// ItineraryMap(dep.getLongitude(),
						// dep.getLatitude(),arr.getLongitude(),
						// arr.getLatitude());

						GoogleItineraryMap itinerary = new GoogleItineraryMap(departure.getText(), arrival.getText());
						String url = itinerary.setMapUrl();
						System.out.println(url);
						itinerary.openMapUrl(url);

					} catch (IllegalArgumentException e) {
						LOGGER.error("Error : ", e);
					}
				} else {
					MessageBox mb = new MessageBox(shell, SWT.ICON_ERROR | SWT.OK);
					mb.setText("Infromation missing");
					mb.setMessage("Please fill in the departure and arrival fields");
					mb.open();
				}

			}
		});
		arrival.addModifyListener(new ModifyListener() {
			@Override
			public void modifyText(ModifyEvent e) {
				System.out.println("nouvelle valeur = " + ((Text) e.widget).getText());
			}
		});
		departure.addModifyListener(new ModifyListener() {
			@Override
			public void modifyText(ModifyEvent e) {
				System.out.println("nouvelle valeur = " + ((Text) e.widget).getText());
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
