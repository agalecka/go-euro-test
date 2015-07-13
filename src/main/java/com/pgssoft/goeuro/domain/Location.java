package com.pgssoft.goeuro.domain;

public class Location {
	private Long _id;
	private String name;
	private String type;
	private GeoPosition geo_position;
	
	public Long get_id() {
		return _id;
	}
	public void set_id(Long _id) {
		this._id = _id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public GeoPosition getGeo_position() {
		return geo_position;
	}
	public void setGeo_position(GeoPosition geo_position) {
		this.geo_position = geo_position;
	}
}
