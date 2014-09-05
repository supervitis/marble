package org.marble.commons.service;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.springframework.data.mongodb.core.query.Query;
import org.springframework.web.multipart.MultipartFile;

import org.marble.commons.dao.model.OriginalStatus;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.mongodb.MongoException;

public interface DatastoreService {

    public void insertOriginalStatus(OriginalStatus originalStatus);

 /*
 * public void setCollection(String collection);
 * 
 * public void renameCollection(String newName);
 * 
 * public DBCursor getOriginalStatuses();
 * 
 * public DBCursor getCollectionData(String collection);
 * 
 * public void insertProcessedPolarity(String processedPolarity);
 * 
 * public Object getProcessedData();
 * 
 * public Map<String, String> getMongoStatus();
 * 
 * public Boolean getExistsCollection();
 * 
 * public Boolean isProcessed();
 * 
 * public String getOldestStatusDate();
 * 
 * public String getOldestStatusId();
 * 
 * public String getNewestStatusDate();
 * 
 * public String getNewestStatusId();
 * 
 * public void dropProcessedCollection();
 */

    <T> void save(T object);

    <T> void removeCollection(Class<T> entityClass);


    
    <T> void findAllAndRemove(Query query, Class<T> entityClass);

    <T> T findOneByQuery(Query query, Class<T> entityClass) throws MongoException;

    <T> List<T> findByTopicId(Integer topicId, Class<T> entityClass) throws MongoException;

    <T> List<T> findByQuery(Query query, Class<T> entityClass) throws MongoException;

    <T> T findOneByText(String text, Class<T> entityClass) throws MongoException;

    <T> void findAllAndRemoveByTopicId(Integer topicId, Class<T> entityClass) throws MongoException;

    <T> List<T> findAll(Class<T> entityClass);

    

    

}