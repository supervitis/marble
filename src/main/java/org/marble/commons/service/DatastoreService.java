package org.marble.commons.service;

import org.marble.commons.dao.model.OriginalStatus;

public interface DatastoreService {

    public void insertOriginalStatus(OriginalStatus originalStatus);

/*
    public void setCollection(String collection);

    public void renameCollection(String newName);

    public DBCursor getOriginalStatuses();

    public DBCursor getCollectionData(String collection);

    public void insertProcessedPolarity(String processedPolarity);

    public Object getProcessedData();

    public Map<String, String> getMongoStatus();

    public Boolean getExistsCollection();

    public Boolean isProcessed();

    public String getOldestStatusDate();

    public String getOldestStatusId();

    public String getNewestStatusDate();

    public String getNewestStatusId();

    public void dropProcessedCollection();

    public void insertDataFromFile(Topic topic, File file) throws Exception;
    */
}