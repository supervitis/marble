package org.marble.commons.model;

import java.util.Date;

public class TopicInfo {
    
    private Integer topicId;

    private Long totalStatusesExtracted;
    private Date oldestStatusDate;
    private Long oldestStatusId;
    private Date newestStatusDate;
    private Long newestStatusId;
    private Long totalStatusesProcessed;

    public Integer getTopicId() {
        return topicId;
    }

    public void setTopicId(Integer topicId) {
        this.topicId = topicId;
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

    public Long getOldestStatusId() {
        return oldestStatusId;
    }

    public void setOldestStatusId(Long oldestStatusId) {
        this.oldestStatusId = oldestStatusId;
    }

    public Date getNewestStatusDate() {
        return newestStatusDate;
    }

    public void setNewestStatusDate(Date newestStatusDate) {
        this.newestStatusDate = newestStatusDate;
    }

    public Long getNewestStatusId() {
        return newestStatusId;
    }

    public void setNewestStatusId(Long newestStatusId) {
        this.newestStatusId = newestStatusId;
    }

    public Long getTotalStatusesProcessed() {
        return totalStatusesProcessed;
    }

    public void setTotalStatusesProcessed(Long totalStatusesProcessed) {
        this.totalStatusesProcessed = totalStatusesProcessed;
    }

}
