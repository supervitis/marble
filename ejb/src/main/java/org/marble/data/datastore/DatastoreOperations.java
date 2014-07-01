package org.marble.data.datastore;


public abstract class DatastoreOperations {
    
    public abstract void setCollection(String collection);
    
    public abstract void insertOriginalStatus(String status);
    
    public abstract void insertProcessedPolarity(String processedPolarity);
    
    public abstract Object getOriginalStatuses();
    
    public abstract Object getProcessedData();
    
    public abstract Object getCollectionData(String collection);
}
