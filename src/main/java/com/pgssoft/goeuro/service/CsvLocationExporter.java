package com.pgssoft.goeuro.service;

import static com.google.common.base.Joiner.on;
import static org.apache.commons.io.FileUtils.writeLines;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.Collection;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.google.common.base.Function;
import com.google.common.collect.Collections2;
import com.google.common.collect.Lists;
import com.pgssoft.goeuro.domain.Location;

public class CsvLocationExporter implements LocationExporter {

	private static final char ESCAPE_SIGN = '\"';
	private static final String FILE_EXTENSION = "csv";
	private static final String NEW_LINE = "\r\n";
	private static final char SEPARATOR = ',';
	
	@Override
	public String getFileExtension() {
		return FILE_EXTENSION;
	}
	
	@Override
	public void exportToFile(File exportFile, Collection<Location> locations) {
		try {
			writeInFile(locations, exportFile);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void writeInFile(Collection<Location> locations, File exportFile)
			throws IOException {
		writeLines(exportFile, Collections2.transform(locations, toFileRecord()), NEW_LINE);
	}
	
	private Function<Location, String> toFileRecord(){
		return new Function<Location, String>() {

			@Override
			public String apply(Location location) {
				return buildRecord(Lists.newArrayList(
						toRecordValue(location.get_id()),
						toRecordValue(location.getName()),
						toRecordValue(location.getType()),
						toRecordValue(location.getGeo_position().getLatitude()),
						toRecordValue(location.getGeo_position().getLongitude())));
			}
		};
	}
	
	private static String buildRecord(List<String> recordValues){
		return on(SEPARATOR).join(recordValues);
	}

	private static String toRecordValue(Long value) {
		return value == null ? StringUtils.EMPTY : value.toString();
	}
	
	private static String toRecordValue(String value) {
		return value == null ? StringUtils.EMPTY : escape(value);
	}
	
	private static String toRecordValue(BigDecimal value) {
		return value == null ? StringUtils.EMPTY : value.toPlainString();
	}
	
	private static String escape(String value) {
		return ESCAPE_SIGN + value.replaceAll("\"", "\"\"") +  ESCAPE_SIGN;
	}
}
