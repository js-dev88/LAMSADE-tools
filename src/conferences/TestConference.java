package conferences;

import static org.junit.Assert.fail;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import org.junit.Test;

public class TestConference {

	@Test
	public void equals() throws ParseException {
		String dateFormat = "dd/MM/yy";
		DateFormat format = new SimpleDateFormat(dateFormat);
		format.setLenient(false);
		Conference conf1 = new Conference("Antoine s conf", "url", format.parse("10/03/2017"),
				format.parse("11/03/2017"), 0);
		Conference conf2 = new Conference("Antoine s conf", "url", format.parse("10/03/2017"),
				format.parse("11/03/2017"), 0);
		if (!conf1.equals(conf2)) {
			fail("Equals not functionning");
		}

	}

}
