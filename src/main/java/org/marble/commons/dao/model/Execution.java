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
import javax.persistence.PreUpdate;
import javax.persistence.Table;

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;
import org.marble.commons.model.ExecutionCommand;
import org.marble.commons.model.ExecutionStatus;
import org.marble.commons.model.ExecutionType;
import org.marble.commons.util.MarbleUtil;
import org.marble.commons.util.StringDateSerializer;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

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

    @Column(length = 50000, name = "log")
    private String log = "";

    @ManyToOne(optional = false)
    @JoinColumn(name = "topic_id")
    @Cascade({ CascadeType.DELETE })
    @JsonBackReference
    private Topic topic;

    @JsonSerialize(using = StringDateSerializer.class)
    @Column(name = "created_at")
    public Date createdAt;

    @JsonSerialize(using = StringDateSerializer.class)
    @Column(name = "updated_at")
    public Date updatedAt;

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
        if (this.log.length() > 50000) {
            this.log = this.log.substring(0, 50000);
            this.log = this.log.substring(0, this.log.lastIndexOf("\n"));

        }
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

    @PreUpdate
    public void preUpdate() {
        this.updatedAt = new Date();
    }

}
