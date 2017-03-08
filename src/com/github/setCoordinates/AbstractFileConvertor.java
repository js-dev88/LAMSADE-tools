package com.github.setCoordinates;

import java.io.File;
import org.artofsolving.jodconverter.office.OfficeException;

public interface AbstractFileConvertor {

void convertToOdt(File source, File destination) throws OfficeException;

}

