package org.marble.commons.dao.model.instagram;

import java.util.Date;

public class Caption {

	private Integer captionId;
	private Date createdTime;
	private String text;
	private InstagramUser user;
	
	
	public Caption(Integer captionId, Date createdTime, String text,
			InstagramUser user) {
		super();
		this.captionId = captionId;
		this.createdTime = createdTime;
		this.text = text;
		this.user = user;
	}


	public Integer getCaptionId() {
		return captionId;
	}


	public void setCaptionId(Integer captionId) {
		this.captionId = captionId;
	}


	public Date getCreatedTime() {
		return createdTime;
	}


	public void setCreatedTime(Date createdTime) {
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
	
	
	
	
	
}
