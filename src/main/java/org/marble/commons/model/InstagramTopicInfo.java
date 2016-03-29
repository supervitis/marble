package org.marble.commons.model;

import java.util.Date;

public class InstagramTopicInfo {
    
    private Integer instagramTopicId;
    private Boolean active;
    private Long totalStatusesExtracted;
    private Date oldestStatusDate;
    private String oldestStatusId;
    private Date newestStatusDate;
    private String newestStatusId;
    private Long totalStatusesProcessed;

    public Integer getInstagramTopicId() {
        return instagramTopicId;
    }

    public void setInstagramTopicId(Integer instagramTopicId) {
        this.instagramTopicId = instagramTopicId;
    }
    
    public Boolean getActive() {
		return active;
	}

	public void setActive(Boolean active) {
		this.active = active;
	}

	public Long getTotalStatusesExtracted() {
        return totalStatusesExtracted;
    }

    public void setTotalStatusesExtracted(Long totalStatusesExtracted) {
        this.totalStatusesExtracted = totalStatusesExtracted;
    }

    public Date getOldestStatusDate() {
        return oldestStatusDate;
    }

    public void setOldestStatusDate(Date oldestStatusDate) {
        this.oldestStatusDate = oldestStatusDate;
    }
    
    public void setOldestStatusDate(Long timestamp) {
        this.oldestStatusDate = new Date(timestamp*1000);
    }

    public String getOldestStatusId() {
        return oldestStatusId;
    }

    public void setOldestStatusId(String oldestStatusId) {
        this.oldestStatusId = oldestStatusId;
    }

    public Date getNewestStatusDate() {
        return newestStatusDate;
    }

    public void setNewestStatusDate(Date newestStatusDate) {
        this.newestStatusDate = newestStatusDate;
    }
    
    public void setNewestStatusDate(Long timestamp) {
        this.newestStatusDate = new Date(timestamp*1000);
    }

    public String getNewestStatusId() {
        return newestStatusId;
    }

    public void setNewestStatusId(String newestStatusId) {
        this.newestStatusId = newestStatusId;
    }

    public Long getTotalStatusesProcessed() {
        return totalStatusesProcessed;
    }

    public void setTotalStatusesProcessed(Long totalStatusesProcessed) {
        this.totalStatusesProcessed = totalStatusesProcessed;
    }

}
