package org.marble.commons.dao.model.instagram;

import twitter4j.JSONException;
import twitter4j.JSONObject;

public class Image {

	private String url;
	private Integer width;
	private Integer height;
	
	
	public Image(){
		
	}
	public Image(String url, Integer width, Integer height) {
		super();
		this.url = url;
		this.width = width;
		this.height = height;
	}

	public Image(JSONObject image) throws JSONException {
		this.url = image.getString("url");
		this.width = image.getInt("width");
		this.height = image.getInt("height");
	}

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
		return "Image [url=" + url + ", width=" + width + ", height=" + height
				+ "]";
	}
	
	
}
