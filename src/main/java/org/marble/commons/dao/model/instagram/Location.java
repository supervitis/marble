package org.marble.commons.dao.model.instagram;

public class Location {

	private Integer locationId;
	private Double latitude;
	private Double longitude;
	private String name;
	
	
	
	public Location(Integer locationId, Double latitude, Double longitude,
			String name) {
		this.locationId = locationId;
		this.latitude = latitude;
		this.longitude = longitude;
		this.name = name;
	}
	public Integer getLocationId() {
		return locationId;
	}
	public void setLocationId(Integer locationId) {
		this.locationId = locationId;
	}
	public Double getLatitude() {
		return latitude;
	}
	public void setLatitude(Double latitude) {
		this.latitude = latitude;
	}
	public Double getLongitude() {
		return longitude;
	}
	public void setLongitude(Double longitude) {
		this.longitude = longitude;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	
}
