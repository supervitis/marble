package org.marble.commons.dao.model.instagram;

import java.util.Date;

import twitter4j.JSONException;
import twitter4j.JSONObject;

public class Comment {

	private Long commentId;
	private Long createdTime;
	private String text;
	private InstagramUser user;
	
	public Comment(){
		
	}
	
	public Comment(Long commentId, Long createdTime, String text,
			InstagramUser user) {
		super();
		this.commentId = commentId;
		this.createdTime = createdTime;
		this.text = text;
		this.user = user;
	}

	public Comment(JSONObject comment) throws JSONException{
		 commentId = comment.getLong("id");
		 text = comment.getString("text");
		 createdTime = comment.getLong("created_time");
		 user = new InstagramUser(comment.getJSONObject("from"));
	}
	public Long getCommentId() {
		return commentId;
	}


	public void setCommentId(Long commentId) {
		this.commentId = commentId;
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
		return "Comment [commentId=" + commentId + ", createdTime="
				+ createdTime + ", text=" + text + ", user=" + user + "]";
	}
	
	
	
}
