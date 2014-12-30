package org.marble.commons.dao.model;

import java.util.Date;

import javax.persistence.Id;

import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "processed_statuses")
public class ProcessedStatus {

    @Indexed
    private Integer topicId;
    private Float polarity;

    private Date createdAt;

    @Id
    private long id;
    private String text;

    @Indexed
    private boolean isRetweeted;
    private String screenName;
    private String timeZone;
    private Date originalCreatedAt;

    public ProcessedStatus() {

    }

    public ProcessedStatus(ProcessedStatus status) {
        this.topicId = status.getTopicId();
        this.polarity = status.getPolarity();
        this.createdAt = status.getCreatedAt();
        this.id = status.getId();
        this.text = status.getText();
        this.isRetweeted = status.isRetweeted();
        this.screenName = status.getScreenName();
        this.timeZone = status.getTimeZone();
        this.originalCreatedAt = status.getOriginalCreatedAt();
    }

    public ProcessedStatus(OriginalStatus status) {
        this.topicId = status.getTopicId();
        this.polarity = null;
        this.createdAt = status.getCreatedAt();
        this.id = status.getId();
        this.text = status.getText();
        if (status.getUser() != null) {
            this.screenName = status.getUser().getScreenName();
            this.timeZone = status.getUser().getTimeZone();
        }
        if (status.getRetweetedStatus() != null) {
            this.originalCreatedAt = status.getRetweetedStatus().getCreatedAt();
            this.isRetweeted = true;
        }
    }

    public Integer getTopicId() {
        return topicId;
    }

    public void setTopicId(Integer topicId) {
        this.topicId = topicId;
    }

    public Float getPolarity() {
        return polarity;
    }

    public void setPolarity(Float polarity) {
        this.polarity = polarity;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public boolean isRetweeted() {
        return isRetweeted;
    }

    public void setRetweeted(boolean isRetweeted) {
        this.isRetweeted = isRetweeted;
    }

    public String getScreenName() {
        return screenName;
    }

    public void setScreenName(String screenName) {
        this.screenName = screenName;
    }

    public String getTimeZone() {
        return timeZone;
    }

    public void setTimeZone(String timeZone) {
        this.timeZone = timeZone;
    }

    public Date getOriginalCreatedAt() {
        return originalCreatedAt;
    }

    public void setOriginalCreatedAt(Date originalCreatedAt) {
        this.originalCreatedAt = originalCreatedAt;
    }

}