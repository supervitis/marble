package org.marble.commons.dao.model.instagram;

import java.util.Date;

public class Comment {

	private Integer commentId;
	private Date createdTime;
	private String text;
	private InstagramUser user;
	
	
	public Comment(Integer commentId, Date createdTime, String text,
			InstagramUser user) {
		super();
		this.commentId = commentId;
		this.createdTime = createdTime;
		this.text = text;
		this.user = user;
	}


	public Integer getCommentId() {
		return commentId;
	}


	public void setCommentId(Integer commentId) {
		this.commentId = commentId;
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
