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
import javax.validation.constraints.Pattern;

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;
import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.format.annotation.DateTimeFormat;

import twitter4j.Query.Unit;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;

@Entity
@Table(name = "mrbl_instagram_topics")
@JsonIgnoreProperties({ "changeSet", "executions" })
public class InstagramTopic implements Serializable {
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
    @Column(name = "keywords")
    private String keywords;
   
	@Digits(fraction = 0, integer = 32)
    @Column(name = "upper_limit")
    private Long upperLimit = 0L;
	
    //Dates and geolocation
    @DateTimeFormat(pattern = "yyyy-MM-dd hh:mm")
    @Column(name = "sinceDate")
    private Date sinceDate;
    
    @DateTimeFormat(pattern = "yyyy-MM-dd hh:mm")
    @Column(name = "untilDate")
    private Date untilDate;
    
    @Min(-90)
    @Max(90)
    @NotNull
    @Column(name = "geoLatitude")
    private Double geoLatitude;
    
    @Min(-180)
    @Max(180)
    @NotNull
    @Column(name = "geoLongitude")
    private Double geoLongitude;
    
    @NotNull
    @Column(name = "geoRadius")
    private Double geoRadius;
    
    @Column(name = "geoUnit")
    private Unit geoUnit;

    @NotNull
    @Digits(fraction = 0, integer = 5)
    @Column(name = "statuses_per_full_extraction")
    private Integer statusesPerFullExtraction = 10000;


    @OneToMany(mappedBy = "instagramTopic", fetch = FetchType.EAGER)
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
