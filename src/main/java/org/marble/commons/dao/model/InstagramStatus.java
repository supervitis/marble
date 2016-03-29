package org.marble.commons.dao.model;

import java.util.Arrays;

import javax.persistence.Id;

import org.marble.commons.dao.model.instagram.Caption;
import org.marble.commons.dao.model.instagram.Comment;
import org.marble.commons.dao.model.instagram.Image;
import org.marble.commons.dao.model.instagram.InstagramUser;
import org.marble.commons.dao.model.instagram.Location;
import org.marble.commons.dao.model.instagram.UserPhotoTag;
import org.marble.commons.dao.model.instagram.Video;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;


@Document(collection = "instagram_statuses")
public class InstagramStatus {

    @Indexed
    private Integer instagramTopicId;
    private Long createdTime;

    @Id
    private String id;
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
    private UserPhotoTag[] usersInPhoto;
    private Caption caption;
    private InstagramUser user;
  
    
    public InstagramStatus() {

    }

    

	public InstagramStatus(Integer instagramTopicId, Long createdTime, String id,
			String[] tags, String type, Location location, Comment[] comments,
			String filter, String link, InstagramUser[] likes,
			Image imageLowResolution, Image imageThumbnail,
			Image imageStandardResolution, Video videoLowResolution,
			Video videoStandardResolution, UserPhotoTag[] usersInPhoto,
			Caption caption, InstagramUser user) {
		super();
		this.instagramTopicId = instagramTopicId;
		this.createdTime = createdTime;
		this.id = id;
		this.tags = tags;
		this.type = type;
		this.location = location;
		this.comments = comments;
		this.filter = filter;
		this.link = link;
		this.likes = likes;
		this.imageLowResolution = imageLowResolution;
		this.imageThumbnail = imageThumbnail;
		this.imageStandardResolution = imageStandardResolution;
		this.videoLowResolution = videoLowResolution;
		this.videoStandardResolution = videoStandardResolution;
		this.usersInPhoto = usersInPhoto;
		this.caption = caption;
		this.user = user;
	}



	public Integer getInstagramTopicId() {
		return instagramTopicId;
	}


	public void setInstagramTopicId(Integer instagramTopicId) {
		this.instagramTopicId = instagramTopicId;
	}


	public Long getCreatedTime() {
		return createdTime;
	}


	public void setCreatedTime(Long createdTime) {
		this.createdTime = createdTime;
	}


	public String getId() {
		return id;
	}


	public void setId(String id) {
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


	public UserPhotoTag[] getUsersInPhoto() {
		return usersInPhoto;
	}


	public void setUsersInPhoto(UserPhotoTag[] usersInPhoto) {
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



	@Override
	public String toString() {
		return "InstagramStatus [instagramTopicId=" + instagramTopicId
				+ ", createdTime=" + createdTime + ", id=" + id + ", tags="
				+ Arrays.toString(tags) + ", type=" + type + ", location="
				+ location + ", comments=" + Arrays.toString(comments)
				+ ", filter=" + filter + ", link=" + link + ", likes="
				+ Arrays.toString(likes) + ", imageLowResolution="
				+ imageLowResolution + ", imageThumbnail=" + imageThumbnail
				+ ", imageStandardResolution=" + imageStandardResolution
				+ ", videoLowResolution=" + videoLowResolution
				+ ", videoStandardResolution=" + videoStandardResolution
				+ ", usersInPhoto=" + Arrays.toString(usersInPhoto)
				+ ", caption=" + caption + ", user=" + user + "]";
	}
 
    
    
    
}

    
    