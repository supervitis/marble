package org.marble.commons.dao.model;

import java.util.Date;

import javax.persistence.Id;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import twitter4j.JSONObject;

@Document(collection = "uploaded_statuses")
public class UploadedStatus {

	 @Indexed
	 private Integer datasetId;

	 @Id
	 private ObjectId id;
	 private Object status;
	 
	 public UploadedStatus() {

    }

    public UploadedStatus(Integer datasetId, Object status) {
    	this.id = new ObjectId();
    	this.datasetId = datasetId;
    	this.status = status;
    }

	public Integer getDatasetId() {
		return datasetId;
	}

	public void setDatasetId(Integer datasetId) {
		this.datasetId = datasetId;
	}


	public Object getStatus() {
		return status;
	}

	public void setStatus(Object status) {
		this.status = status;
	}

	   
}
