package org.marble.commons.dao.model.instagram;

import twitter4j.JSONException;
import twitter4j.JSONObject;

public class InstagramUser {

	private String userId;
	private String username;
	private String fullName;
	private String profilePictureUrl;
	
	public InstagramUser(){
		
	}
	
	public InstagramUser(String userId, String username, String fullName,
			String profilePictureUrl) {
		super();
		this.userId = userId;
		this.username = username;
		this.fullName = fullName;
		this.profilePictureUrl = profilePictureUrl;
	}

	public InstagramUser(JSONObject user) throws JSONException {
		this.userId = user.getString("id");
		this.username = user.getString("username");
		this.fullName = user.getString("full_name");
		this.profilePictureUrl = user.getString("profile_picture");
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getFullName() {
		return fullName;
	}

	public void setFullName(String fullName) {
		this.fullName = fullName;
	}

	public String getProfilePictureUrl() {
		return profilePictureUrl;
	}

	public void setProfilePictureUrl(String profilePictureUrl) {
		this.profilePictureUrl = profilePictureUrl;
	}

	@Override
	public String toString() {
		return "InstagramUser [userId=" + userId + ", username=" + username
				+ ", fullName=" + fullName + ", profilePictureUrl="
				+ profilePictureUrl + "]";
	}
	
	
	
}
