package org.marble.commons.dao.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.Digits;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat.ISO;

@Entity
@Table
public class Topic implements Serializable {
	private static final long serialVersionUID = -4417618450499483945L;

	@Id
	@Column(unique = true)
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	@NotNull
	@NotEmpty
	@Column(unique = true)
	private String name;
	private String description;
	
	@NotNull
	@NotEmpty
	private String keywords;
	
	@Digits(fraction = 0, integer = 16)
	private Long upperLimit;
	@Digits(fraction = 0, integer = 16)
	private Long lowerLimit;
	@Pattern(regexp = "[a-zA-Z]{2}|")
	private String language;

	@Digits(fraction = 0, integer = 3)
	private Integer statusesPerCall;
	@Digits(fraction = 0, integer = 8)
	private Integer statusesPerFullExtraction;
	private Double processorPositiveBoundary;
	private Double processorNegativeBoundary;
	
	@DateTimeFormat(iso = ISO.DATE_TIME)
	private Date plotterLeftDateBoundary;
	@DateTimeFormat(iso = ISO.DATE_TIME)
	private Date plotterRightDateBoundary;
	
	@Digits(fraction = 0, integer = 8)
	private Integer plotterStepSize;
	
	private TopicStatus topicStatus = new TopicStatus();

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

	public void update(Topic topic) {
		if (topic.name != null) {
			this.name = topic.name;
		}
		if (topic.description != null) {
			this.description = topic.description;
		}
		if (topic.keywords != null) {
			this.keywords = topic.keywords;
		}
		if (topic.upperLimit != null) {
			this.upperLimit = topic.upperLimit;
		}
		if (topic.lowerLimit != null) {
			this.lowerLimit = topic.lowerLimit;
		}
		if (topic.language != null) {
			this.language = topic.language;
		}
		if (topic.statusesPerCall != null) {
			this.statusesPerCall = topic.statusesPerCall;
		}
		if (topic.statusesPerFullExtraction != null) {
			this.statusesPerFullExtraction = topic.statusesPerFullExtraction;
		}
		if (topic.processorPositiveBoundary != null) {
			this.processorPositiveBoundary = topic.processorPositiveBoundary;
		}
		if (topic.processorNegativeBoundary != null) {
			this.processorNegativeBoundary = topic.processorNegativeBoundary;
		}
		if (topic.plotterLeftDateBoundary != null) {
			this.plotterLeftDateBoundary = topic.plotterLeftDateBoundary;
		}
		if (topic.plotterRightDateBoundary != null) {
			this.plotterRightDateBoundary = topic.plotterRightDateBoundary;
		}
		if (topic.plotterStepSize != null) {
			this.plotterStepSize = topic.plotterStepSize;
		}
		if (topic.topicStatus != null) {
			this.topicStatus = topic.topicStatus;
		}
	}
}
