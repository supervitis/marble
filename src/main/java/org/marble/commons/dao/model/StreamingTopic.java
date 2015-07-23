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
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;
import javax.validation.constraints.Pattern;

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;
import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat.ISO;

import twitter4j.Query.Unit;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;

@Entity
@Table(name = "mrbl_streaming_topics")
@JsonIgnoreProperties({ "changeSet", "executions" })
public class StreamingTopic implements Serializable {
    private static final long serialVersionUID = -4417618450499483945L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(unique = true, name = "id")
    private Integer id;

    @Column(unique = true, name = "name")
    @NotEmpty
    @org.hibernate.validator.constraints.Length(max = 20)
    @Pattern(regexp = "[a-zA-Z_0-9-]+")
    private String name;
    @Column(name = "description")
    private String description;

    @NotNull
    @NotEmpty
    @Column(name = "keywords")
    private String keywords;

    @NotNull
    @NotEmpty
    @Column(name = "active")
    private Boolean active = false;
   

	@Digits(fraction = 0, integer = 24)
    @Column(name = "upper_limit")
    private Long upperLimit;

    @Digits(fraction = 0, integer = 24)
    @Column(name = "lower_limit")
    private Long lowerLimit;
    
    //Dates and geolocation
    @DateTimeFormat(pattern = "yyyy-MM-dd hh:mm")
    @Column(name = "sinceDate")
    private Date sinceDate;
    
    @DateTimeFormat(pattern = "yyyy-MM-dd hh:mm")
    @Column(name = "untilDate")
    private Date untilDate;
    
    @Min(-90)
    @Max(90)
    @Column(name = "geoLatitude")
    private Double geoLatitude;
    
    @Min(-180)
    @Max(180)
    @Column(name = "geoLongitude")
    private Double geoLongitude;
    
    @Column(name = "geoRadius")
    private Double geoRadius;
    
    @Column(name = "geoUnit")
    private Unit geoUnit;

    @Pattern(regexp = "[a-zA-Z]{2}|")
    @Column(name = "language")
    private String language = "en";

    @Min(1)
    @Max(100)
    @Column(name = "statuses_per_call")
    private Integer statusesPerCall;

    @NotNull
    @Digits(fraction = 0, integer = 5)
    @Column(name = "statuses_per_full_extraction")
    private Integer statusesPerFullExtraction = 1000;


    @OneToMany(mappedBy = "streamingTopic", fetch = FetchType.EAGER)
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
    
    public Boolean getActive() {
 		return active;
 	}

 	public void setActive(Boolean active) {
 		this.active = active;
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

    public Date getSinceDate() {
		return sinceDate;
	}

	public void setSinceDate(Date sinceDate) {
		this.sinceDate = sinceDate;
	}

	public Date getUntilDate() {
		return untilDate;
	}

	public void setUntilDate(Date untilDate) {
		this.untilDate = untilDate;
	}

	public Double getGeoLatitude() {
		return geoLatitude;
	}

	public void setGeoLatitude(Double geoLatitude) {
		this.geoLatitude = geoLatitude;
	}

	public Double getGeoLongitude() {
		return geoLongitude;
	}

	public void setGeoLongitude(Double geoLongitude) {
		this.geoLongitude = geoLongitude;
	}

	public Double getGeoRadius() {
		return geoRadius;
	}

	public void setGeoRadius(Double geoRadius) {
		this.geoRadius = geoRadius;
	}

	public Unit getGeoUnit() {
		return geoUnit;
	}

	public void setGeoUnit(Unit geoUnit) {
		this.geoUnit = geoUnit;
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
