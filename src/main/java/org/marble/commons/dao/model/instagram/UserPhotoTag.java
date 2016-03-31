package org.marble.commons.dao.model.instagram;

import twitter4j.JSONException;
import twitter4j.JSONObject;

public class UserPhotoTag {

	InstagramUser user;
	Double positionX;
	Double positionY;
	
	public UserPhotoTag(){
		
		
	}
	public UserPhotoTag(InstagramUser user, Double positionX, Double positionY) {
		super();
		this.user = user;
		this.positionX = positionX;
		this.positionY = positionY;
	}

	public UserPhotoTag(JSONObject tag) throws JSONException {
		this.positionX = (Double)(tag.getJSONObject("position").get("x"));
		this.positionY = (Double)(tag.getJSONObject("position").get("y"));
		this.user = new InstagramUser(tag.getJSONObject("user"));
	}

	public InstagramUser getUser() {
		return user;
	}

	public void setUser(InstagramUser user) {
		this.user = user;
	}

	public Double getPositionX() {
		return positionX;
	}

	public void setPositionX(Double positionX) {
		this.positionX = positionX;
	}

	public Double getPositionY() {
		return positionY;
	}

	public void setPositionY(Double positionY) {
		this.positionY = positionY;
	}

	@Override
	public String toString() {
		return "UserPhotoTag [user=" + user + ", positionX=" + positionX
				+ ", positionY=" + positionY + "]";
	}
	
	
}
