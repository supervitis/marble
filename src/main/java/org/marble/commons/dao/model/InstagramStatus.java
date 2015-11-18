package org.marble.commons.dao.model;

import java.util.Date;

import javax.persistence.Id;

import org.marble.commons.dao.model.instagram.Caption;
import org.marble.commons.dao.model.instagram.Comment;
import org.marble.commons.dao.model.instagram.Image;
import org.marble.commons.dao.model.instagram.InstagramUser;
import org.marble.commons.dao.model.instagram.Location;
import org.marble.commons.dao.model.instagram.Video;

import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;


@Document(collection = "instagram_statuses")
public class InstagramStatus {

    @Indexed
    private Integer instagramTopicId;
    private Date createdTime;

    @Id
    private long id;
    private String[] tags;
    private String type;
    private Location location;
    private Comment[] comments;
    private String filter;
    private String link;
    private InstagramUser[] likes;
    private Image imageLowResolution;
    private Image imageThumbnail;
    private Image imageStandardResolution;
    private Video videoLowResolution;
    private Video videoStandardResolution;
    private InstagramUser[] usersInPhoto;
    private Caption caption;
    private InstagramUser user;
  
    
    public InstagramStatus() {

    }


	public Integer getInstagramTopicId() {
		return instagramTopicId;
	}


	public void setInstagramTopicId(Integer instagramTopicId) {
		this.instagramTopicId = instagramTopicId;
	}


	public Date getCreatedTime() {
		return createdTime;
	}


	public void setCreatedTime(Date createdTime) {
		this.createdTime = createdTime;
	}


	public long getId() {
		return id;
	}


	public void setId(long id) {
		this.id = id;
	}


	public String[] getTags() {
		return tags;
	}


	public void setTags(String[] tags) {
		this.tags = tags;
	}


	public String getType() {
		return type;
	}


	public void setType(String type) {
		this.type = type;
	}


	public Location getLocation() {
		return location;
	}


	public void setLocation(Location location) {
		this.location = location;
	}


	public Comment[] getComments() {
		return comments;
	}


	public void setComments(Comment[] comments) {
		this.comments = comments;
	}


	public String getFilter() {
		return filter;
	}


	public void setFilter(String filter) {
		this.filter = filter;
	}


	public String getLink() {
		return link;
	}


	public void setLink(String link) {
		this.link = link;
	}


	public InstagramUser[] getLikes() {
		return likes;
	}


	public void setLikes(InstagramUser[] likes) {
		this.likes = likes;
	}


	public Image getImageLowResolution() {
		return imageLowResolution;
	}


	public void setImageLowResolution(Image imageLowResolution) {
		this.imageLowResolution = imageLowResolution;
	}


	public Image getImageThumbnail() {
		return imageThumbnail;
	}


	public void setImageThumbnail(Image imageThumbnail) {
		this.imageThumbnail = imageThumbnail;
	}


	public Image getImageStandardResolution() {
		return imageStandardResolution;
	}


	public void setImageStandardResolution(Image imageStandardResolution) {
		this.imageStandardResolution = imageStandardResolution;
	}


	public Video getVideoLowResolution() {
		return videoLowResolution;
	}


	public void setVideoLowResolution(Video videoLowResolution) {
		this.videoLowResolution = videoLowResolution;
	}


	public Video getVideoStandardResolution() {
		return videoStandardResolution;
	}


	public void setVideoStandardResolution(Video videoStandardResolution) {
		this.videoStandardResolution = videoStandardResolution;
	}


	public InstagramUser[] getUsersInPhoto() {
		return usersInPhoto;
	}


	public void setUsersInPhoto(InstagramUser[] usersInPhoto) {
		this.usersInPhoto = usersInPhoto;
	}


	public Caption getCaption() {
		return caption;
	}


	public void setCaption(Caption caption) {
		this.caption = caption;
	}


	public InstagramUser getUser() {
		return user;
	}


	public void setUser(InstagramUser user) {
		this.user = user;
	}
 
    
    
    
}

    
    