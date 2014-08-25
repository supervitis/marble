package org.marble.commons.dao.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name="mrbl_executions")
public class Execution implements Serializable {
    private static final long serialVersionUID = -1285068636097871799L;

    @Id
    @Column(unique = true)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
 
    @Enumerated(EnumType.STRING)
    private ExecutionType type;
    
    @Enumerated(EnumType.STRING)
    private ExecutionStatus status;
    
    @Enumerated(EnumType.STRING)
    private ExecutionCommand command;
    
    @Lob
    @Column( length = 100000 )
    private String log;
    
    @ManyToOne(optional=false)
    @JoinColumn(name="topic_id")
    @Cascade({CascadeType.DELETE})
    @JsonBackReference
    private Topic topic;

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

    public String getLog() {
        return log;
    }

    public void setLog(String log) {
        this.log = log;
    }
    
    public void appendLog(String log) {
        this.log = log + this.log;
    }

    public Topic getTopic() {
        return topic;
    }

    public void setTopic(Topic topic) {
        this.topic = topic;
    }
    
}
