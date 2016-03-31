package org.marble.commons.service;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.apache.commons.io.IOUtils;
import org.hibernate.jpa.criteria.predicate.IsEmptyPredicate;
import org.marble.commons.dao.model.InstagramStatus;
import org.marble.commons.dao.model.instagram.Caption;
import org.marble.commons.dao.model.instagram.Comment;
import org.marble.commons.dao.model.instagram.Image;
import org.marble.commons.dao.model.instagram.InstagramUser;
import org.marble.commons.dao.model.instagram.Location;
import org.marble.commons.dao.model.instagram.UserPhotoTag;
import org.marble.commons.dao.model.instagram.Video;
import org.marble.commons.exception.EmptyInstagramResponseException;
import org.marble.commons.executor.extractor.InstagramExtractionExecutor;

import twitter4j.JSONArray;
import twitter4j.JSONException;
import twitter4j.JSONObject;
import twitter4j.Query.Unit;

@Service
public class InstagramSearchServiceImpl implements InstagramSearchService {

	private String instagramAccessToken;
	  private static final Logger log = LoggerFactory.getLogger(InstagramSearchServiceImpl.class);

	public InstagramSearchServiceImpl() {

	}

	public InstagramSearchServiceImpl(String accessToken) {
		this.instagramAccessToken = accessToken;
	}

	@Override
	public void configure(String accessToken) {
		this.instagramAccessToken = accessToken;
	}

	@Override
	public void unconfigure() {
		this.instagramAccessToken = null;

	}

	@Override
	public List<InstagramStatus> search(Integer instagramTopicId, Double latitude, Double longitude,
			Double radius, Unit unit, Long maxDate) throws EmptyInstagramResponseException, JSONException, MalformedURLException, IOException {

		ArrayList<InstagramStatus> statuses = new ArrayList<InstagramStatus>();
		double dist = radius*1000;
		if (unit.equals(Unit.mi)) {
			// Convert to KM
			dist = dist * 0.621371192;
		}


		String url = "https://api.instagram.com/v1/media/search?lat="
				+ latitude + "&lng=" + longitude + "&distance=" + dist
				+ "&max_timestamp=" + maxDate + "&access_token="
				+ instagramAccessToken;
		JSONObject json = null;
		json = new JSONObject(IOUtils.toString(new URL(url),Charset.forName("UTF-8")));
		System.out.println(url);
		int responseCode = 0;
		try {
			responseCode = json.getJSONObject("meta").getInt("code");
		} catch (JSONException e) {
			System.out.println("Error: " + e.getLocalizedMessage());
		}
		if (responseCode == 200) {
			JSONArray dataArray = json.getJSONArray("data");
			if(dataArray.length() == 0){
				throw new EmptyInstagramResponseException();
			}
			for (int i = 0; i < dataArray.length(); i++) {

				JSONObject data = dataArray.getJSONObject(i);

				String id = null;
				try {
					id = data.getString("id");
				} catch (JSONException e2) {
					System.out.println("Error: " + e2.getLocalizedMessage());
				}

				Long createdTime = 0L;
				try {
					createdTime = data.getLong("created_time");
				} catch (JSONException e2) {					
					System.out.println("Error: " + e2.getLocalizedMessage());
				}
				JSONArray tagsArray = null;
				try {
					tagsArray = data.getJSONArray("tags");
				} catch (JSONException e2) {
					System.out.println("Error: " + e2.getLocalizedMessage());
				}
				ArrayList<String> tagList = new ArrayList<String>();
				for (int x = 0; x < tagsArray.length(); x++) {
					try {
						tagList.add(tagsArray.getString(x));
					} catch (JSONException e) {	
						System.out.println("Error: " + e.getLocalizedMessage());

					}
				}
				String[] tags = {};
				tags = tagList.toArray(tags);

				String type = null;
				try {
					type = data.getString("type");
				} catch (JSONException e2) {
					System.out.println("Error: " + e2.getLocalizedMessage());
				}

				JSONObject loc = null;
				Location location = null;
				try {
					loc = data.getJSONObject("location");
				
				if (loc != null) {
					location = new Location(loc);
				}
				} catch (JSONException e2) {
					System.out.println("Error: " + e2.getLocalizedMessage());
				}
				
				JSONObject commentsObject;
				ArrayList<Comment> commentList = new ArrayList<Comment>();
				Comment[] comments = {};
				JSONArray commentsArray;
				try {
					commentsObject = data.getJSONObject("comments");
					if (commentsObject.getInt("count") >= 0) {
						commentsArray = commentsObject.getJSONArray("data");
						for (int j = 0; j < commentsArray.length(); j++) {
							JSONObject comment = commentsArray.getJSONObject(j);
							commentList.add(new Comment(comment));
						}
						comments = commentList.toArray(comments);
					}
				} catch (JSONException e2) {
					System.out.println("Error: " + e2.getLocalizedMessage());
				}

				String filter = null;
				try {
					filter = data.getString("filter");
				} catch (JSONException e2) {
					System.out.println("Error: " + e2.getLocalizedMessage());
				}

				String link = null;
				try {
					link = data.getString("link");
				} catch (JSONException e2) {
					System.out.println("Error: " + e2.getLocalizedMessage());
				}

				
				JSONObject likesObject = null;
				ArrayList<InstagramUser> likesList = new ArrayList<InstagramUser>();
				InstagramUser[] likes = {};
				JSONArray likesArray;
				try {
					likesObject = data.getJSONObject("likes");
				if (likesObject.getInt("count") >= 0) {
					likesArray = likesObject.getJSONArray("data");
					for (int j = 0; j < likesArray.length(); j++) {
						JSONObject like = likesArray.getJSONObject(j);
						if (like != null)
							likesList.add(new InstagramUser(like));
					}
					likes = likesList.toArray(likes);
				}
				} catch (JSONException e2) {
					System.out.println("Error: " + e2.getLocalizedMessage());
				}
				
				Image imageLowResolution = null;
				Image imageThumbnail = null;
				Image imageStandardResolution = null;
				JSONObject imagesObject;
				try {
					imagesObject = data.getJSONObject("images");
				

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
				}catch(Exception e){
					System.out.println("Error: " + e.getLocalizedMessage());
				}
				
				
				Video videoLowResolution = null;
				Video videoStandardResolution = null;
				JSONObject videosObject = null;
				try {
					videosObject = data.getJSONObject("videos");
				

				if (videosObject != null) {
					JSONObject lowVidObject = null;
					lowVidObject = videosObject	.getJSONObject("low_resolution");
					JSONObject stVidObject = null;
						stVidObject = videosObject.getJSONObject("standard_resolution");
						videoLowResolution = new Video(lowVidObject);
						videoStandardResolution = new Video(stVidObject);
				}
				} catch (Exception e2) {
					System.out.println("Error: " + e2.getLocalizedMessage());
				}
				
				JSONArray userTagsArray = null;
				ArrayList<UserPhotoTag> userTagsList = new ArrayList<UserPhotoTag>();
				UserPhotoTag[] usersInPhoto = {};
				try {
					userTagsArray = data.getJSONArray("users_in_photo");
				} catch (JSONException e1) {
					System.out.println("Error: " + e1.getLocalizedMessage());
				}
				if (userTagsArray != null) {

					for (int x = 0; x < userTagsArray.length(); x++) {
						try {
							userTagsList.add(new UserPhotoTag(userTagsArray
									.getJSONObject(x)));
						} catch (JSONException e) {
						}
					}
					usersInPhoto = userTagsList.toArray(usersInPhoto);
				}

				Caption caption = null;
				try {
					caption = new Caption(data.getJSONObject("caption"));
				} catch (Exception e) {
					System.out.println("Error: " + e.getLocalizedMessage());
				}

				JSONObject jsonUser = null;
				try {
					jsonUser = data.getJSONObject("user");
				} catch (JSONException e) {
					System.out.println("Error: " + e.getLocalizedMessage());
				}
				InstagramUser user = null;
				if (jsonUser != null) {
					try {
						user = new InstagramUser(jsonUser);
					} catch (JSONException e) {
						System.out.println("Error: " + e.getLocalizedMessage());
					}
				}
				InstagramStatus status = new InstagramStatus(instagramTopicId, createdTime,
						id, tags, type, location, comments, filter, link,
						likes, imageLowResolution, imageThumbnail,
						imageStandardResolution, videoLowResolution,
						videoStandardResolution, usersInPhoto, caption, user);
				statuses.add(status);
			}
		}
		return statuses;
	}
}
