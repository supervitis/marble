package org.marble.commons.dao.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.PrePersist;
import javax.persistence.Table;

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;
import org.springframework.data.mongodb.crossstore.RelatedDocument;

import org.marble.commons.model.ExecutionCommand;
import org.marble.commons.model.ExecutionStatus;
import org.marble.commons.util.MarbleUtil;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Entity
@Table(name = "mrbl_executions")
@JsonIgnoreProperties({ "changeSet" })
public class Execution implements Serializable {
    private static final long serialVersionUID = -1285068636097871799L;

    @Id
    @Column(unique = true, name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Enumerated(EnumType.STRING)
    @Column(name = "type")
    private ExecutionType type;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private ExecutionStatus status = ExecutionStatus.Initialized;

    @Enumerated(EnumType.STRING)
    @Column(name = "command")
    private ExecutionCommand command;

    @Column(length = 100000, name = "log")
    private String log;

    @ManyToOne(optional = false)
    @JoinColumn(name = "topic_id")
    @Cascade({ CascadeType.DELETE })
    @JsonBackReference
    private Topic topic;

    @Column(name = "created_at")
    public Date createdAt;

    @Column(name = "updated_at")
    public Date updatedAt;

    @RelatedDocument
    @JsonIgnore
    private SurveyInfo surveyInfo;
    
    public SurveyInfo getSurveyInfo() {
        return surveyInfo;
    }

    public void setSurveyInfo(SurveyInfo surveyInfo) {
        this.surveyInfo = surveyInfo;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public ExecutionType getType() {
        return type;
    }

    public void setType(ExecutionType type) {
        this.type = type;
    }

    public ExecutionStatus getStatus() {
        return status;
    }

    public void setStatus(ExecutionStatus status) {
        this.status = status;
    }

    public ExecutionCommand getCommand() {
        return command;
    }

    public void setCommand(ExecutionCommand command) {
        this.command = command;
    }

    public String getLog() {
        return log;
    }

    public void setLog(String log) {
        this.log = log;
    }

    public void appendLog(String log) {
        this.log = MarbleUtil.getDatedMessage(log) + "\n" + this.log;
    }

    public Topic getTopic() {
        return topic;
    }

    public void setTopic(Topic topic) {
        this.topic = topic;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }

    @PrePersist
    public void prePersist() {
        Date now = new Date();
        this.createdAt = now;
        this.updatedAt = now;
    }

    // MFC Spring Data Bug DATAMONGO-519
    // @PreUpdate
    public void preUpdate() {
        this.updatedAt = new Date();
    }

}
