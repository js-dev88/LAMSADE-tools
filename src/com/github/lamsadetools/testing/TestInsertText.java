package com.github.lamsadetools.testing;

import org.junit.Test;

import com.github.odfdom.ODTDocument;

public class TestInsertText {

	@Test
	public void test() throws AssertionError {

		try {
			ODTDocument doc = new ODTDocument();

			doc.insertSearchParamsIntoHeader("Pedro", "0656566565", "edoreld@gmail.com");

			outputOdt.save("test.odt");

		} catch (Exception e) {
			System.err.println("ERROR: unable to create output file.");
		}
	}

}
