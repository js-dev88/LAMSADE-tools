package com.github.lantoine.lamsadetools.setCoordinates;

import java.io.File;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.artofsolving.jodconverter.OfficeDocumentConverter;
import org.artofsolving.jodconverter.document.DocumentFormat;
import org.artofsolving.jodconverter.office.DefaultOfficeManagerConfiguration;
import org.artofsolving.jodconverter.office.OfficeConnectionProtocol;
import org.artofsolving.jodconverter.office.OfficeException;
import org.artofsolving.jodconverter.office.OfficeManager;

public class Convert {

	private OfficeDocumentConverter converter = null;
	private OfficeManager officeManager = null;

	public void convertToOdt(final File source, final File destination) throws OfficeException {

		DocumentFormat outputFormat = converter.getFormatRegistry().getFormatByExtension("odt"); // "html"
																									// ou
																									// "pdf"

		converter.convert(source, destination, outputFormat);
	}

	@PostConstruct
	protected void initOfficeManager() {

		DefaultOfficeManagerConfiguration configuration = new DefaultOfficeManagerConfiguration();

		configuration.setPortNumber(8100);
		configuration.setConnectionProtocol(OfficeConnectionProtocol.SOCKET);

		configuration.setTemplateProfileDir(new File("D:\\openoffice\\3"));
		configuration.setOfficeHome(new File("C:\\Program Files (x86)\\LibreOffice 5"));

		configuration.setTaskExecutionTimeout(30000L);

		officeManager = configuration.buildOfficeManager();
		converter = new OfficeDocumentConverter(officeManager);

		officeManager.start();

	}

	@PreDestroy
	protected void preDrestroy() {

		officeManager.stop();

	}

}
