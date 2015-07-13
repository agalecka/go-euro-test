package com.pgssoft.goeuro.service;

import java.io.File;
import java.util.List;

import com.pgssoft.goeuro.domain.Location;

public class LocationFileExporter {

	private List<Location> locations;
	private LocationExporter exporter;
	private String prefix;
	
	private String dirPath;

	public static LocationFileExporter from(List<Location> locations) {
		LocationFileExporter exp = new LocationFileExporter();
		exp.locations = locations;
		return exp;
	}
	
	public LocationFileExporter withFilePrefix(String prefix) {
		this.prefix = prefix;
		return this;
	}

	public String toCsvFile() {
		this.exporter = new CsvLocationExporter();
		File exportFile = createFile();
		exporter.exportToFile(exportFile, locations);
		return exportFile.getAbsolutePath();
	}
	
	private File createFile() {
		return new File(dirPath, generateFileName());
	}

	private String generateFileName() {
		return replaceIllegalFileCharacters(prefix) + '_' + System.currentTimeMillis() + '.' + exporter.getFileExtension();
	}
	
	private String replaceIllegalFileCharacters(String fileName) {
		return fileName.replaceAll("[^a-zA-Z0-9_\\-\\.]", "_");
		
	}

}
