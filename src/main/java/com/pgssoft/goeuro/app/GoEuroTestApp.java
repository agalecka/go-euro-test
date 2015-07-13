package com.pgssoft.goeuro.app;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Component;

import com.pgssoft.goeuro.service.LocationClient;
import com.pgssoft.goeuro.service.LocationFileExporter;

@Component
public class GoEuroTestApp {

    private static final Logger LOGGER = LoggerFactory.getLogger(GoEuroTestApp.class);

	@Autowired
	private LocationClient locationClient;

	public static void main(String[] args) {
		validateParameters(args);
		LOGGER.info("Searching for {}", args[0]);
		ApplicationContext context = new ClassPathXmlApplicationContext(
				"config.xml");
		GoEuroTestApp tester = context.getBean(GoEuroTestApp.class);
		test(args[0], tester);
	}

	private static void test(String param, GoEuroTestApp tester) {
		try {
			String fileLocation = tester.testGoEuro(param);
			LOGGER.info("Exported locations saved in {} ", fileLocation);
		} catch (Exception e) {
			LOGGER.error(e.getMessage());
		}
	}

	public String testGoEuro(String suggestion) {
		validate(suggestion);
		return LocationFileExporter
				.from(locationClient.getLocations(suggestion))
				.withFilePrefix(suggestion).toCsvFile();
	}

	private static void validateParameters(String[] args) {
		if (args == null || args.length == 0) {
			throw new IllegalArgumentException("Missing string parameter");
		}
	}
	
	private void validate(String param) {
		if (containsIllegalsCharacters(param)) {
			throw new IllegalArgumentException("Parameter contains illegal characters");
		}
	}
	
	private boolean containsIllegalsCharacters(String param) {
		return param.matches("^.*[/?].*$");
	}

	public void setLocationClient(LocationClient locationClient) {
		this.locationClient = locationClient;
	}

}
