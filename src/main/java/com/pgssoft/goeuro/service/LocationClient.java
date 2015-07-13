package com.pgssoft.goeuro.service;

import java.util.List;

import com.pgssoft.goeuro.domain.Location;

public interface LocationClient {

	public List<Location> getLocations(String suggestion);

}
