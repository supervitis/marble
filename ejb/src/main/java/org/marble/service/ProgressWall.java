package org.marble.service;

import java.util.HashMap;

import javax.ejb.LocalBean;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.inject.Named;

@Singleton
@LocalBean
@Startup
@Named
public class ProgressWall {
    private HashMap<String, String> progressMap  = new HashMap<String, String>();
    private HashMap<String, String> requestsMap  = new HashMap<String, String>();

    final private String            extractorTag = "|extractor";
    final private String            processorTag = "|processor";

    /* General Methods */
    public HashMap<String, String> getProgressMap() {
        return progressMap;
    }

    @SuppressWarnings("unused")
    private void setProgressMap(HashMap<String, String> progressMap) {
        this.progressMap = progressMap;
    }

    private void setUnitProgress(String unitName, String progressMessage) {
        this.progressMap.put(unitName, progressMessage);
    }

    private String getUnitProgress(String unitName) {
        return this.progressMap.get(unitName);
    }

    private void removeUnitProgress(String unitName) {
        this.progressMap.remove(unitName);
    }

    public HashMap<String, String> getRequestsMap() {
        return requestsMap;
    }

    @SuppressWarnings("unused")
    private void setRequestsMap(HashMap<String, String> requestsMap) {
        this.requestsMap = requestsMap;
    }

    public void pushMessage(String unitName, String message) {
        this.requestsMap.put(unitName, message);
    }

    public String pullMessage(String unitName) {
        String message = this.requestsMap.get(unitName);
        this.requestsMap.remove(unitName);
        return message;
    }

    /* Processor Methods */
    public void setProcessorUnitProgress(String unitName, String progressMessage) {
        unitName = unitName + processorTag;
        this.setUnitProgress(unitName, progressMessage);
    }

    public String getProcessorUnitProgress(String unitName) {
        unitName = unitName + processorTag;
        return this.getUnitProgress(unitName);
    }

    public void removeProcessorUnitProgress(String unitName) {
        unitName = unitName + processorTag;
        this.removeUnitProgress(unitName);
    }

    public void pushProcessorMessage(String unitName, String message) {
        unitName = unitName + processorTag;
        this.requestsMap.put(unitName, message);
    }

    public String pullProcessorMessage(String unitName) {
        unitName = unitName + processorTag;
        String message = this.requestsMap.get(unitName);
        this.requestsMap.remove(unitName);
        return message;
    }

    /* Extractor Methods */
    public void setExtractorUnitProgress(String unitName, String progressMessage) {
        unitName = unitName + extractorTag;
        this.setUnitProgress(unitName, progressMessage);
    }

    public String getExtractorUnitProgress(String unitName) {
        unitName = unitName + extractorTag;
        return this.getUnitProgress(unitName);
    }

    public void removeExtractorUnitProgress(String unitName) {
        unitName = unitName + extractorTag;
        this.removeUnitProgress(unitName);
    }

    public void pushExtractorMessage(String unitName, String message) {
        unitName = unitName + extractorTag;
        this.requestsMap.put(unitName, message);
    }

    public String pullExtractorMessage(String unitName) {
        unitName = unitName + extractorTag;
        String message = this.requestsMap.get(unitName);
        this.requestsMap.remove(unitName);
        return message;
    }
}
