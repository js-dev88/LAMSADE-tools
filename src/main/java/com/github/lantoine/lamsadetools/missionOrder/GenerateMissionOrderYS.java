package com.github.lantoine.lamsadetools.missionOrder;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.nio.channels.FileChannel;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalUnit;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.apache.commons.io.FileUtils;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.util.Units;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.jboss.resteasy.plugins.delegates.LocaleDelegate;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

import com.github.lantoine.lamsadetools.conferences.Conference;
import com.github.lantoine.lamsadetools.map.ConnectionToGoogleMapsApi;
import com.github.lantoine.lamsadetools.setCoordinates.SetCoordinates;
import com.github.lantoine.lamsadetools.setCoordinates.UserDetails;
import com.github.lantoine.lamsadetools.yearbookInfos.ConnectionToYearbook;
import com.github.lantoine.lamsadetools.yearbookInfos.YearbookDataException;

/**
 * This class fills a young searcher Mission Order
 * It can only be accessed in a static way
 * 
 *
 */
public class GenerateMissionOrderYS {
	
	/**
	 * "target" attribut is the default saving path
	 * 
	 */
	private static String target = FileSystems.getDefault().getPath("").toAbsolutePath()
			+ "/demande_de_mission_jeune_chercheur.fodt";

	public static String getTarget() {
		return target;
	}

	/**
	 * 
	 * illYSOrderMission fills the "demande_de_mission_jeune_chercheur.fodt"
	 * document using a UserDetails and a Conference to get the information
	 * 
	 * @param user
	 * @param conf
	 * @throws IOException
	 * @throws SAXException
	 * @throws ParserConfigurationException
	 * @throws IllegalArgumentException
	 */
	public static void fillYSOrderMission(UserDetails user, Conference conf, String fileDestination)
			throws IOException, SAXException, ParserConfigurationException, IllegalArgumentException {
		if (!fileDestination.isEmpty())
			target = fileDestination;
		// Path path = FileSystems.getDefault().getPath("");
		// System.out.println("The File will be saved in: " +
		// path.toAbsolutePath());

		// define source and target
		ClassLoader classLoader = SetCoordinates.class.getClassLoader();
		String source = classLoader
				.getResource("com/github/lantoine/lamsadetools/missionOrder/demande_de_mission_jeune_chercheur.fodt")
				.getPath();
		// String target = path.toAbsolutePath() +
		// "/demande_de_mission_jeune_chercheur_Clone.fodt";

		File demand = new File(source);

		DocumentBuilderFactory factory = null;
		DocumentBuilder builder = null;
		Document doc = null;

		try (InputStream xmlText = new FileInputStream(demand)) {
			// InputStream transformed in DOM Document
			factory = DocumentBuilderFactory.newInstance();
			builder = factory.newDocumentBuilder();
			doc = builder.parse(new InputSource(xmlText));
		}

		NodeList span = doc.getElementsByTagName("text:span");

		// Search every information that have to be completed and complete them
		// using the UserDetails and the Conference information
		for (int i = 0; i < span.getLength(); i++) {
			if (span.item(i).getTextContent().contains("NOM") && !span.item(i).getTextContent().contains("PRENOM")) {
				span.item(i).setTextContent(user.getName());
			}

			if (span.item(i).getTextContent().contains("PRENOM")) {
				span.item(i).setTextContent(user.getFirstName());
			}

			if (span.item(i).getTextContent().contains("POLE")) {
				span.item(i).setTextContent(user.getGroup());
			}

			if (span.item(i).getTextContent().contains("INTITULEMISSION")) {
				span.item(i).setTextContent(conf.getTitle());
			}

			if (span.item(i).getTextContent().contains("LIEN")) {
				span.item(i).setTextContent(conf.getUrl());
			}

			if (span.item(i).getTextContent().contains("VILLE - PAYS")) {
				span.item(i).setTextContent(conf.getCity() + " - " + conf.getCountry());
			}

			if (span.item(i).getTextContent().contains("DATE")) {
				span.item(i).setTextContent(conf.getStart_date().toString());
			}

			if (span.item(i).getTextContent().contains("DUREE")) {
				//Period period = Period.between(conf.getStart_date(), conf.getEnd_date());
				Long period = ChronoUnit.DAYS.between(conf.getStart_date(), conf.getEnd_date());
				span.item(i).setTextContent(String.valueOf(period + " jour(s)"));
			}

			saveYSOrderMission(doc);
			saveYSOrderMissionToHistory(target, conf.getCity(), conf.getCountry(), conf.getStart_date().toString());
		}
	}

	/**
	 * This method saves a Document as an XML file
	 * 
	 * @param doc
	 * @param target
	 *            = destination of the saved file
	 * @throws IOException
	 */
	private static void saveYSOrderMission(Document doc) throws IOException {
		DOMSource source = new DOMSource(doc);
		TransformerFactory transformerFactory = TransformerFactory.newInstance();
		Transformer transformer;
		try (FileWriter writer = new FileWriter(new File(target))) {
			StreamResult result = new StreamResult(writer);
			transformer = transformerFactory.newTransformer();
			transformer.transform(source, result);
		} catch (TransformerException e1) {
			throw new IllegalStateException(e1);
		}
	}

	private static void saveYSOrderMissionToHistory(String fileToCopy, String city, String country, String startDate)
			throws IOException {
		File filesource = new File(fileToCopy);
		String filename = new String("historique_DJC/DJC_" + city + "-" + country + "_" + startDate + ".fodt");
		File targetfile = new File(filename);
		FileUtils.copyFile(filesource, targetfile);
	}

	public static void main(String[] args) {
		UserDetails user = new UserDetails();
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/M/yyyy");
		Conference conf = new Conference("titre", "url", LocalDate.parse("10/06/2016", formatter),
				LocalDate.parse("11/06/2016", formatter), 3.05, "city", "country");
		try {
			fillYSOrderMission(user, conf, target);
		} catch (IllegalArgumentException e) {
			throw new IllegalStateException(e);
		} catch (IOException e) {
			throw new IllegalStateException(e);
		} catch (SAXException e) {
			throw new IllegalStateException(e);
		} catch (ParserConfigurationException e) {
			throw new IllegalStateException(e);
		}

	}

}
