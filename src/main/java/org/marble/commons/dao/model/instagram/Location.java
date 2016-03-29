package org.marble.commons.dao.model.instagram;

import twitter4j.JSONException;
import twitter4j.JSONObject;

public class Location {

	private Long locationId;
	private Double latitude;
	private Double longitude;
	private String name;
	
	
	public Location() {
		
	}
	
	
	public Location(Long locationId, Double latitude, Double longitude,
			String name) {
		this.locationId = locationId;
		this.latitude = latitude;
		this.longitude = longitude;
		this.name = name;
	}
	
	public Location(JSONObject loc) throws JSONException {
		
		 Long locid = loc.getLong("id");
		 String locname = loc.getString("name");
		 Double loclat = (Double)(loc.get("latitude"));
		 Double loclng = (Double)(loc.get("longitude"));
		 this.locationId = locid;
		 this.latitude = loclat;
		 this.longitude = loclng;
		 this.name = locname;
	}
	public Long getLocationId() {
		return locationId;
	}
	public void setLocationId(Long locationId) {
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

	@Override
	public String toString() {
		return "Location [locationId=" + locationId + ", latitude=" + latitude
				+ ", longitude=" + longitude + ", name=" + name + "]";
	}
	
	
}
