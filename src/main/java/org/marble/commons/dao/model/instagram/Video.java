package org.marble.commons.dao.model.instagram;

import twitter4j.JSONException;
import twitter4j.JSONObject;

public class Video {

	private String url;
	private Integer width;
	private Integer height;
	
	public Video (){
		
	}
	
	public Video(String url, Integer width, Integer height) {
		super();
		this.url = url;
		this.width = width;
		this.height = height;
	}

	public Video(JSONObject vid) throws JSONException {
		this.url = vid.getString("url");
		this.width = vid.getInt("width");
		this.height = vid.getInt("height");	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public Integer getWidth() {
		return width;
	}

	public void setWidth(Integer width) {
		this.width = width;
	}

	public Integer getHeight() {
		return height;
	}

	public void setHeight(Integer height) {
		this.height = height;
	}

	@Override
	public String toString() {
		return "Video [url=" + url + ", width=" + width + ", height=" + height
				+ "]";
	}
	
	
}
