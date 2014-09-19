package org.marble.commons.dao.model;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.PrePersist;
import javax.persistence.Table;
import javax.validation.constraints.Pattern;

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;
import org.hibernate.validator.constraints.NotEmpty;

import org.marble.commons.util.StringDateSerializer;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonRawValue;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

@Entity
@Table(name = "mrbl_plots")
@JsonIgnoreProperties({ "topic", "mainOptions", "overviewOptions", "data", "execution" })
public class Plot implements Serializable {

    private static final long serialVersionUID = 6936532299491147949L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(unique = true, name = "id")
    private Integer id;

    @Column(name = "name")
    @NotEmpty
    @Pattern(regexp = "[a-zA-Z_ 0-9-]+")
    private String name;

    @Column(name = "description")
    private String description;

    @OneToOne()
    // @Cascade({ CascadeType.DELETE })
    private Topic topic;

    @OneToOne()
    @Cascade({ CascadeType.PERSIST, CascadeType.REMOVE })
    private Execution execution;

    @Column(length = 2000, name = "main_options")
    // @JsonRawValue
    private String mainOptions;

    @Column(length = 2000, name = "overview_options")
    // @JsonRawValue
    private String overviewOptions;

    @Column(length = 200000, name = "plot_data")
    // @JsonRawValue
    private String data;

    @JsonSerialize(using = StringDateSerializer.class)
    @Column(name = "created_at")
    public Date createdAt;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Topic getTopic() {
        return topic;
    }

    public void setTopic(Topic topic) {
        this.topic = topic;
    }

    public String getMainOptions() {
        return mainOptions;
    }

    public void setMainOptions(String mainOptions) {
        this.mainOptions = mainOptions;
    }

    public void setMainOptions(Map<String, Object> mainOptions) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            this.mainOptions = mapper.writeValueAsString(mainOptions);
        } catch (JsonProcessingException e) {
            this.mainOptions = null;
        }
    }

    public String getOverviewOptions() {
        return overviewOptions;
    }

    public void setOverviewOptions(String overviewOptions) {
        this.overviewOptions = overviewOptions;
    }

    public void setOverviewOptions(Map<String, Object> overviewOptions) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            this.overviewOptions = mapper.writeValueAsString(overviewOptions);
        } catch (JsonProcessingException e) {
            this.overviewOptions = null;
        }
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public void setData(List<Map<String, Object>> data) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            this.data = mapper.writeValueAsString(data);
        } catch (JsonProcessingException e) {
            this.data = null;
        }
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Execution getExecution() {
        return execution;
    }

    public void setExecution(Execution execution) {
        this.execution = execution;
    }

    @PrePersist
    public void prePersist() {
        Date now = new Date();
        this.createdAt = now;
    }

}
