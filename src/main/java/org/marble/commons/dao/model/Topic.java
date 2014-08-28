package org.marble.commons.dao.model;

import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.Digits;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;
import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat.ISO;

import com.fasterxml.jackson.annotation.JsonManagedReference;

@Entity
@Table(name = "mrbl_topics")
public class Topic implements Serializable {
    private static final long serialVersionUID = -4417618450499483945L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(unique = true, name = "id")
    private Integer id;
    @NotNull
    @NotEmpty
    @Column(unique = true, name = "name")
    private String name;
    @Column(name = "description")
    private String description;

    @NotNull
    @NotEmpty
    @Column(name = "keywords")
    private String keywords;

    @Digits(fraction = 0, integer = 16)
    @Column(name = "upper_limit")
    private Long upperLimit;
    
    @Digits(fraction = 0, integer = 16)
    @Column(name = "lower_limit")
    private Long lowerLimit;
    
    @Pattern(regexp = "[a-zA-Z]{2}|")
    @Column(name = "language")
    private String language;

    @Digits(fraction = 0, integer = 3)
    @Column(name = "statuses_per_call")
    private Integer statusesPerCall;
    
    @Digits(fraction = 0, integer = 8)
    @Column(name = "statuses_per_full_extraction")
    private Integer statusesPerFullExtraction;
    
    @Column(name = "processor_positive_boundary")
    private Double processorPositiveBoundary;
    
    @Column(name = "processor_negative_boundary")
    private Double processorNegativeBoundary;

    @DateTimeFormat(iso = ISO.DATE_TIME)
    @Column(name = "plotter_left_date_boundary")
    private Date plotterLeftDateBoundary;
    
    @DateTimeFormat(iso = ISO.DATE_TIME)
    @Column(name = "plotter_right_date_boundary")
    private Date plotterRightDateBoundary;

    @Digits(fraction = 0, integer = 8)
    @Column(name = "plotter_step_size")
    private Integer plotterStepSize;

    @Column(name = "topic_status")
    private TopicStatus topicStatus = new TopicStatus();

    @OneToMany(mappedBy = "topic", fetch = FetchType.EAGER)
    @Cascade({ CascadeType.DELETE })
    @Column(name = "executions")
    @JsonManagedReference
    private Set<Execution> executions = new HashSet<Execution>();

    // TODO For future versions:
    // @ElementCollection
    // private List<String> keywords = new ArrayList<String>();
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

    public String getKeywords() {
        return keywords;
    }

    public void setKeywords(String keywords) {
        this.keywords = keywords;
    }

    public Long getUpperLimit() {
        return upperLimit;
    }

    public void setUpperLimit(Long upperLimit) {
        this.upperLimit = upperLimit;
    }

    public Long getLowerLimit() {
        return lowerLimit;
    }

    public void setLowerLimit(Long lowerLimit) {
        this.lowerLimit = lowerLimit;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public Integer getStatusesPerCall() {
        return statusesPerCall;
    }

    public void setStatusesPerCall(Integer statusesPerCall) {
        this.statusesPerCall = statusesPerCall;
    }

    public Integer getStatusesPerFullExtraction() {
        return statusesPerFullExtraction;
    }

    public void setStatusesPerFullExtraction(Integer statusesPerFullExtraction) {
        this.statusesPerFullExtraction = statusesPerFullExtraction;
    }

    public Double getProcessorPositiveBoundary() {
        return processorPositiveBoundary;
    }

    public void setProcessorPositiveBoundary(Double processorPositiveBoundary) {
        this.processorPositiveBoundary = processorPositiveBoundary;
    }

    public Double getProcessorNegativeBoundary() {
        return processorNegativeBoundary;
    }

    public void setProcessorNegativeBoundary(Double processorNegativeBoundary) {
        this.processorNegativeBoundary = processorNegativeBoundary;
    }

    public Date getPlotterLeftDateBoundary() {
        return plotterLeftDateBoundary;
    }

    public void setPlotterLeftDateBoundary(Date plotterLeftDateBoundary) {
        this.plotterLeftDateBoundary = plotterLeftDateBoundary;
    }

    public Date getPlotterRightDateBoundary() {
        return plotterRightDateBoundary;
    }

    public void setPlotterRightDateBoundary(Date plotterRightDateBoundary) {
        this.plotterRightDateBoundary = plotterRightDateBoundary;
    }

    public Integer getPlotterStepSize() {
        return plotterStepSize;
    }

    public void setPlotterStepSize(Integer plotterStepSize) {
        this.plotterStepSize = plotterStepSize;
    }

    public TopicStatus getTopicStatus() {
        // Just to verify that it was initialized
        if (topicStatus == null) {
            topicStatus = new TopicStatus();
        }
        return topicStatus;
    }

    public void setTopicStatus(TopicStatus topicStatus) {
        this.topicStatus = topicStatus;
    }

    public Set<Execution> getExecutions() {
        return executions;
    }

    public void setExecutions(Set<Execution> executions) {
        this.executions = executions;
    }

    public void addExecution(Execution execution) {
        this.executions.add(execution);
        return;
    }

    public void removeExecution(Execution execution) {
        this.executions.remove(execution);
        return;
    }
}
