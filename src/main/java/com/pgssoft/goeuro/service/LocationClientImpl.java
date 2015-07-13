package com.pgssoft.goeuro.service;

import static java.util.Arrays.asList;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import com.pgssoft.goeuro.domain.Location;

@Service
public class LocationClientImpl implements LocationClient {

    private static final Logger LOGGER = LoggerFactory.getLogger(LocationClientImpl.class);
    
	@Value("${api.locations}")
	private String locationsUrl;

	@Autowired
	private RestTemplate restTemplate;

	@Override
	public List<Location> getLocations(String suggestion) {
		return asList(getForLocations(suggestion));
	}

	private Location[] getForLocations(String suggestion) {
		try {
			return restTemplate.getForObject(locationsUrl, Location[].class, suggestion);
		} catch (RestClientException ex) {
			LOGGER.error("Request to GoEuro ended with exception: {}", ex.getMessage());
			throw new RuntimeException("Sorry, request to GoEuro ended with error");
		}
	}
}
