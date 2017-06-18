package com.github.lantoine.lamsadetools.setCoordinates;

import static org.junit.Assert.*;

import java.io.File;
import java.nio.file.FileSystems;
import java.nio.file.Path;

import org.junit.Assert;
import org.junit.Test;

import com.github.lantoine.lamsadetools.setCoordinates.SetCoordinates;
import com.github.lantoine.lamsadetools.setCoordinates.UserDetails;

public class TestSetCoordinates {

	@Test
	public void testfillPapierEnTete() throws Exception {

		UserDetails usertest = new UserDetails();
		usertest.setNumber("06000000");
		usertest.setName("Nametest");
		usertest.setFirstName("FNametest");
		usertest.setFunction("Test");
		usertest.setEmail("Iamatest@testland.fr");

		SetCoordinates.fillPapierEnTete(usertest);

		Path path = FileSystems.getDefault().getPath("");

		File file = new File(path.toAbsolutePath() + "/papier_a_en_tete_Clone.fodt");
		assertTrue(file.exists());

		file.delete();

	}

}
