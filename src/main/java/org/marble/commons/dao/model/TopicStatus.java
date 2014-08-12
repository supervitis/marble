package org.marble.commons.dao.model;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class TopicStatus implements Serializable {
	private static final long serialVersionUID = 8605664034870038164L;
	private String processStatus = "-";
	private String extractorStatus = "-";
	private Map<String, String> databaseStatus = new HashMap<String, String>();

	public String getProcessStatus() {
		return processStatus;
	}

	public void setProcessStatus(String processStatus) {
		this.processStatus = processStatus;
	}

	public String getExtractorStatus() {
		return extractorStatus;
	}

	public void setExtractorStatus(String extractorStatus) {
		this.extractorStatus = extractorStatus;
	}

	public Map<String, String> getDatabaseStatus() {
		return databaseStatus;
	}

	public void setDatabaseStatus(Map<String, String> map) {
		this.databaseStatus = map;
	}
}
