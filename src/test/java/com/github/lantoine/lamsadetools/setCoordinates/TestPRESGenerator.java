package com.github.lantoine.lamsadetools.setCoordinates;

import static org.junit.Assert.*;

import java.io.File;
import java.nio.file.FileSystems;
import java.nio.file.Path;

import org.junit.Assert;
import org.junit.Test;

import com.github.lantoine.lamsadetools.setCoordinates.PRESGenerator;
import com.github.lantoine.lamsadetools.setCoordinates.UserDetails;

public class TestPRESGenerator {

	@Test
	public void testfillPRES() throws Exception {

		UserDetails usertest = new UserDetails();

		usertest.setGroup("AAA");
		usertest.setName("Nametest");
		usertest.setFirstName("FNametest");
		usertest.setFunction("Test");

		PRESGenerator.fillPRES(usertest);

		Path path = FileSystems.getDefault().getPath("");

		File file = new File(path.toAbsolutePath() + "/Premier_test_template_Clone.fodt");
		assertTrue(file.exists());

		file.delete();
	}

}
