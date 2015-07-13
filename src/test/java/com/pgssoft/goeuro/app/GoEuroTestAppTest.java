package com.pgssoft.goeuro.app;

import static java.util.Arrays.asList;
import static org.apache.commons.io.FileUtils.readLines;
import static org.fest.assertions.api.Assertions.assertThat;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;

import org.apache.commons.lang.RandomStringUtils;
import org.apache.commons.lang.math.RandomUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;

import com.pgssoft.goeuro.domain.GeoPosition;
import com.pgssoft.goeuro.domain.Location;
import com.pgssoft.goeuro.service.LocationClient;
import com.pgssoft.goeuro.service.LocationClientImpl;

@RunWith(MockitoJUnitRunner.class)
public class GoEuroTestAppTest {

	private GoEuroTestApp app = new GoEuroTestApp();

	@InjectMocks
	private LocationClient locationClient = new LocationClientImpl();

	@Mock
	private RestTemplate restTemplate;

	@Before
	public void setup() {
		app.setLocationClient(locationClient);
	}

	@Test
	public void shouldSaveLocationsToCSVFile() throws IOException {
		String suggestion = "Warsaw";
		Location[] locations = new Location[] { location(1), location(2),
				location(3) };
		when(
				restTemplate.getForObject(Mockito.anyString(),
						Mockito.eq(Location[].class), Mockito.eq(suggestion)))
				.thenReturn(locations);

		String fileName = app.testGoEuro(suggestion);

		File testFile = new File("" + fileName);
		Mockito.verify(restTemplate).getForObject(Mockito.anyString(),
				Mockito.eq(Location[].class), Mockito.eq(suggestion));
		assertTrue(testFile.exists());

		List<String> results = readLines(testFile);
		assertThat(results).hasSize(3);
		assertThat(results).isEqualTo(
				asList(toString(locations[0]), toString(locations[1]),
						toString(locations[2])));

		testFile.deleteOnExit();
	}

	@Test
	public void shouldCreateEmptyFileWhenNoResult() throws IOException {
		String suggestion = "no-existing-location";
		when(
				restTemplate.getForObject(Mockito.anyString(),
						Mockito.eq(Location[].class), Mockito.eq(suggestion)))
				.thenReturn(new Location[] {});

		String fileName = app.testGoEuro(suggestion);

		File testFile = new File(fileName);
		Mockito.verify(restTemplate).getForObject(Mockito.anyString(),
				Mockito.eq(Location[].class), Mockito.eq(suggestion));
		assertTrue(testFile.exists());

		assertThat(readLines(testFile)).isEmpty();

		testFile.deleteOnExit();
	}

	@Test(expected = IllegalArgumentException.class)
	public void shouldReturnExceptionWhenInvalidParameter() throws IOException {
		String invalidParam = "param-with-?";
		app.testGoEuro(invalidParam);
	}

	@SuppressWarnings("unchecked")
	@Test(expected = RuntimeException.class)
	public void shouldReturnExceptionWhenProblemWithAccessToAPI()
			throws IOException {
		String suggestion = "any-location";
		when(
				restTemplate.getForObject(Mockito.anyString(),
						Mockito.eq(Location[].class), Mockito.eq(suggestion)))
				.thenThrow(ResourceAccessException.class);

		app.testGoEuro(suggestion);
	}

	private Location location(long id) {
		Location loc = new Location();
		loc.set_id(id);
		loc.setName(RandomStringUtils.randomAlphanumeric(10));
		loc.setType(RandomStringUtils.randomAlphanumeric(10));
		GeoPosition geo = new GeoPosition();
		geo.setLatitude(new BigDecimal(RandomUtils.nextDouble()));
		geo.setLongitude(new BigDecimal(RandomUtils.nextDouble()));
		loc.setGeo_position(geo);
		return loc;
	}

	private String toString(Location location) {
		return "" + location.get_id() + ",\"" + location.getName() + "\",\""
				+ location.getType() + "\","
				+ location.getGeo_position().getLatitude() + ','
				+ location.getGeo_position().getLongitude();
	}

}
