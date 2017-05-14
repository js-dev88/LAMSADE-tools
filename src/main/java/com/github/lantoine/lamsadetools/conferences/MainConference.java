package com.github.lantoine.lamsadetools.conferences;

import java.sql.SQLException;

import com.github.lantoine.lamsadetools.conferences.database.ConferenceDatabase;
import com.github.lantoine.lamsadetools.conferences.database.ConferenceDatabasePrompter;

/**
 * This class is made to be executed by a user who would like to store
 * conferences
 *
 * @author lantoine
 *
 */
public class MainConference {

	public static void main(String[] args) throws SQLException {
		
		//ConferenceDatabase.clearDataBase();
		ConferenceDatabasePrompter.menu();

	}

}
