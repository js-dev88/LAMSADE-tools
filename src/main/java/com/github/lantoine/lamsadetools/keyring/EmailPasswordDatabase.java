package com.github.lantoine.lamsadetools.keyring;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.lantoine.lamsadetools.conferences.database.ConnectionDataBase;
import com.sun.star.lang.NullPointerException;

public class EmailPasswordDatabase {

	private static final Logger LOGGER = LoggerFactory.getLogger(EmailPasswordDatabase.class);

	private static final String DROPSTATEMENT = "DROP table EMAILPASSWORD;";

	private static final String CREATETABLE = "CREATE TABLE IF NOT EXISTS EMAILPASSWORD ("
			+ "emailPasswordID	SERIAL, " + "email varchar(255) NOT NULL, "
			+ "encryptedPassword varchar(500) NOT NULL, "
			+ "CONSTRAINT emailPasswordID PRIMARY KEY (emailPasswordID)); ";

	private static final String INSERTQUERY = "INSERT INTO EMAILPASSWORD (email, encryptedPassword) VALUES (?, ?);";

	private static final String DELETEQUERY = "Delete from EMAILPASSWORD where email = ? ;";

	private static final String SELECTQUERY = "SELECT * FROM EMAILPASSWORD WHERE email = ?;";


	/**
	 * Create table EmailPassword
	 *
	 * @throws SQLException
	 */
	static void createTable() throws SQLException {
		ConnectionDataBase db = new ConnectionDataBase();
		try (Connection conn = db.getConnection()) {
			conn.createStatement().execute(CREATETABLE);
		}
	}

	/**
	 * Drop table EmailPassword
	 *
	 * @throws SQLException
	 */
	public static void clearDataBase() throws SQLException {
		ConnectionDataBase db = new ConnectionDataBase();
		db.getConnection();
		db.sqlQuery(CREATETABLE);
		db.sqlQuery(DROPSTATEMENT);
		db.closeAndDisposeConnection();
	}

	/**
	 * Insert Email and Encrypted Password in Database
	 *
	 * @param EmailPassword
	 *            Object
	 * @throws SQLException
	 */
	public static void insertInDatabase(EmailPassword email) throws SQLException {
		ConnectionDataBase db = new ConnectionDataBase();
		try (Connection conn = db.getConnection();) {

			createTable();

			try (PreparedStatement preparedStatement = conn.prepareStatement(INSERTQUERY);) {

				preparedStatement.setString(1, email.getEmail());
				preparedStatement.setString(2, email.getEncryptedPassword());

				try {
					preparedStatement.executeUpdate();
				} catch (SQLException e) {
					LOGGER.debug(e.getMessage()
							+ "The database could not be created or upgraded, there must be an old version on your computer");
				}

				db.closeAndDisposeConnection();
			}
		}
	}

	/**
	 * Delete an EmailPassword object from database
	 *
	 * @param EmailPassword
	 *            Object
	 * @throws SQLException
	 */

	public static void removePasswordFromDatabase(String email) throws SQLException {
		ConnectionDataBase db = new ConnectionDataBase();
		try (Connection conn = db.getConnection();) {

			try (PreparedStatement preparedStatement = conn.prepareStatement(DELETEQUERY);) {
				preparedStatement.setString(1, email);
				preparedStatement.executeUpdate();
				db.closeAndDisposeConnection();
			}

		}
	}

	/**
	 * Select the only EmailPassword nuplet
	 *
	 * @param email
	 *            may not be null
	 * @throws NullPointerException
	 *             if no data is retrieved
	 */
	public static EmailPassword getEmailPassword(String email) throws SQLException, NullPointerException {
		ConnectionDataBase db = new ConnectionDataBase();
		try (Connection conn = db.getConnection();) {
			createTable();

			try (Statement state = conn.createStatement()) {
				try (PreparedStatement preparedStatement = conn.prepareStatement(SELECTQUERY);) {
					if (email == null) {
						throw new IllegalArgumentException();
					}
					preparedStatement.setString(1, email);
					try (ResultSet result = preparedStatement.executeQuery();) {
						if (!result.next()) {
							throw new NullPointerException("Can't find email in the Database");
						}
						String selectEmail = result.getString(2);
						String selectEncryptedPassword = result.getString(3);
						EmailPassword retrievedEmail = new EmailPassword(selectEmail, null, selectEncryptedPassword);
						return retrievedEmail;

					}

				}

			}
		}
	}

}
