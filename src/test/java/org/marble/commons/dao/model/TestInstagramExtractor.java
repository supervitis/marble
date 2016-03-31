package org.marble.commons.dao.model;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;

import org.apache.commons.io.IOUtils;
import org.marble.commons.dao.model.instagram.Caption;
import org.marble.commons.dao.model.instagram.Comment;
import org.marble.commons.dao.model.instagram.Image;
import org.marble.commons.dao.model.instagram.InstagramUser;
import org.marble.commons.dao.model.instagram.Location;
import org.marble.commons.dao.model.instagram.UserPhotoTag;
import org.marble.commons.dao.model.instagram.Video;

import twitter4j.JSONArray;
import twitter4j.JSONException;
import twitter4j.JSONObject;
import twitter4j.Query.Unit;

public class TestInstagramExtractor {

	public static void main(String[] args) throws JSONException {
		Double latitude = 40.756667;
		Double longitude = -73.986389;
		double dist = 5000;
		double maxDate = 1448364598;
		String instagramAccessToken = "2205827063.a33b3a4.5ceb14860bc8429886c9551b8e42c048";
		String url = "https://api.instagram.com/v1/media/search?lat="
				+ latitude + "&lng=" + longitude + "&distance=" + dist
				+ "&max_timestamp=Inf" + "&access_token="
				+ instagramAccessToken;
		System.out.println(url);
		JSONObject json = null;
		try {
			json = new JSONObject(IOUtils.toString(new URL(url),Charset.forName("UTF-8")));
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			System.out.println(e.getLocalizedMessage());
		}
		
		int responseCode = 0;
		try {
			responseCode = json.getJSONObject("meta").getInt("code");
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if (responseCode == 200) {
			JSONArray dataArray = json.getJSONArray("data");
			for (int i = 0; i < dataArray.length(); i++) {

				JSONObject data = dataArray.getJSONObject(i);

				String id = data.getString("id");

				Long createdTime = data.getLong("created_time");
				JSONArray tagsArray = data.getJSONArray("tags");
				ArrayList<String> tagList = new ArrayList<String>();
				for (int x = 0; x < tagsArray.length(); x++) {
					tagList.add(tagsArray.getString(x));
				}
				String[] tags = {};
				tags = tagList.toArray(tags);

				String type = data.getString("type");

				JSONObject loc = data.getJSONObject("location");
				Location location = null;
				if (loc != null) {
					location = new Location(loc);
				}

				JSONObject commentsObject = data.getJSONObject("comments");
				ArrayList<Comment> commentList = new ArrayList<Comment>();
				Comment[] comments = {};
				JSONArray commentsArray;
				if (commentsObject.getInt("count") >= 0) {
					commentsArray = commentsObject.getJSONArray("data");
					for (int j = 0; j < commentsArray.length(); j++) {
						JSONObject comment = commentsArray.getJSONObject(j);
						commentList.add(new Comment(comment));
					}
					comments = commentList.toArray(comments);
				}

				String filter = data.getString("filter");

				String link = data.getString("link");

				JSONObject likesObject = data.getJSONObject("likes");
				ArrayList<InstagramUser> likesList = new ArrayList<InstagramUser>();
				InstagramUser[] likes = {};
				JSONArray likesArray;
				if (likesObject.getInt("count") >= 0) {
					likesArray = likesObject.getJSONArray("data");
					for (int j = 0; j < likesArray.length(); j++) {
						JSONObject like = likesArray.getJSONObject(j);
						if(like!=null)
							likesList.add(new InstagramUser(like));
					}
					likes = likesList.toArray(likes);
				}

				Image imageLowResolution = null;
				Image imageThumbnail = null;
				Image imageStandardResolution = null;
				JSONObject imagesObject = data.getJSONObject("images");

				if (imagesObject != null) {
					JSONObject lowImageObject = imagesObject
							.getJSONObject("low_resolution");
					JSONObject thumbImageObject = imagesObject
							.getJSONObject("thumbnail");
					JSONObject stImageObject = imagesObject
							.getJSONObject("standard_resolution");
					imageLowResolution = new Image(lowImageObject);
					imageThumbnail = new Image(thumbImageObject);
					imageStandardResolution = new Image(stImageObject);
				}

				Video videoLowResolution = null;
				Video videoStandardResolution = null;
				JSONObject videosObject = null;
				try{
					videosObject = data.getJSONObject("videos");
				}catch(Exception e){}
				
				if (videosObject != null) {
					JSONObject lowVidObject = videosObject
							.getJSONObject("low_resolution");
					JSONObject stVidObject = videosObject
							.getJSONObject("standard_resolution");
					videoLowResolution = new Video(lowVidObject);
					videoStandardResolution = new Video(stVidObject);
				}
				
				JSONArray userTagsArray = data.getJSONArray("users_in_photo");
				ArrayList<UserPhotoTag> userTagsList = new ArrayList<UserPhotoTag>();
				for (int x = 0; x < userTagsArray.length(); x++) {
					userTagsList.add(new UserPhotoTag(userTagsArray.getJSONObject(x)));
				}
				
				UserPhotoTag[] usersInPhoto = {};
				usersInPhoto = userTagsList.toArray(usersInPhoto);

				Caption caption =null;
				try{
				caption = new Caption(data.getJSONObject("caption"));
				}catch(Exception e){}
				
				JSONObject jsonUser = data.getJSONObject("user");
				InstagramUser user = null;
				if (jsonUser != null) {
					user = new InstagramUser(jsonUser);
				}
				InstagramStatus status = new InstagramStatus(4,  createdTime, id,
						tags, type, location, comments,filter, link, likes,
						imageLowResolution, imageThumbnail,
						imageStandardResolution, videoLowResolution,
						videoStandardResolution, usersInPhoto,
						caption, user);
				System.out.println(status);
			}
			
		}

		
	}

}
