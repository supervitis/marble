package org.marble.commons.dao.model.instagram;


import twitter4j.JSONException;
import twitter4j.JSONObject;

public class Caption {

	private String captionId;
	private Long createdTime;
	private String text;
	private InstagramUser user;
	
	public Caption(){
		
	}
	public Caption(String captionId, Long createdTime, String text,
			InstagramUser user) {
		super();
		this.captionId = captionId;
		this.createdTime = createdTime;
		this.text = text;
		this.user = user;
	}


	public Caption(JSONObject cap) throws JSONException {
		this.captionId = cap.getString("id");
		this.createdTime = new Long(cap.getLong("created_time"));
		this.text = cap.getString("text");
		this.user = new InstagramUser(cap.getJSONObject("from"));
		
	}


	public String getCaptionId() {
		return captionId;
	}


	public void setCaptionId(String captionId) {
		this.captionId = captionId;
	}


	public Long getCreatedTime() {
		return createdTime;
	}


	public void setCreatedTime(Long createdTime) {
		this.createdTime = createdTime;
	}


	public String getText() {
		return text;
	}


	public void setText(String text) {
		this.text = text;
	}


	public InstagramUser getUser() {
		return user;
	}


	public void setUser(InstagramUser user) {
		this.user = user;
	}


	@Override
	public String toString() {
		return "Caption [captionId=" + captionId + ", createdTime="
				+ createdTime + ", text=" + text + ", user=" + user + "]";
	}
	
}
