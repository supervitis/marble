package org.marble.commons.dao.model.instagram;

public class InstagramUser {

	private Integer userId;
	private String username;
	private String fullName;
	private String profilePictureUrl;
	
	public InstagramUser(Integer userId, String username, String fullName,
			String profilePictureUrl) {
		super();
		this.userId = userId;
		this.username = username;
		this.fullName = fullName;
		this.profilePictureUrl = profilePictureUrl;
	}

	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
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
	
	
}
