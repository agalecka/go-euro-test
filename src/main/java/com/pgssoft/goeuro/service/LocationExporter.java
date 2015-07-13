package com.pgssoft.goeuro.service;

import java.io.File;
import java.util.Collection;

import com.pgssoft.goeuro.domain.Location;

public interface LocationExporter {

	void exportToFile(File exportFile, Collection<Location> locations);

	String getFileExtension();
}
