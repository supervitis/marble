package org.marble.commons.service;

import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.mongodb.core.convert.MongoConverter;
import org.springframework.data.mongodb.core.query.Query;
import org.marble.commons.dao.model.OriginalStatus;
import org.marble.commons.dao.model.StreamingStatus;
import org.marble.commons.dao.model.UploadedStatus;

import com.mongodb.DBCursor;
import com.mongodb.MongoException;

public interface DatastoreService {

    public void insertOriginalStatus(OriginalStatus originalStatus);

    <T> void save(T object);

    <T> void removeCollection(Class<T> entityClass);
    
    <T> void findAllAndRemove(Query query, Class<T> entityClass);

    <T> T findOneByQuery(Query query, Class<T> entityClass) throws MongoException;

    <T> List<T> findByTopicId(Integer topicId, Class<T> entityClass) throws MongoException;

    <T> List<T> findByQuery(Query query, Class<T> entityClass) throws MongoException;
	
    <T> List<T> findByStreamingTopicId(Integer streamingTopicId,Class<T> entityClass)throws MongoException;

    <T> T findOneByText(String text, Class<T> entityClass) throws MongoException;

    <T> void findAllAndRemoveByTopicId(Integer topicId, Class<T> entityClass) throws MongoException;

    <T> List<T> findAll(Class<T> entityClass);

    <T> long countAll(Class<T> entityClass);
    
    <T> long countByTopicId(Integer topicId, Class<T> entityClass);

    <T> DBCursor findCursorByTopicId(Integer topicId, Class<T> entityClass);

    MongoConverter getConverter();

    <T> T findOneByTopicIdSortBy(Integer topicId, String field, Direction direction, Class<T> entityClass);

    <T> DBCursor findCursorByQuery(Map<String, Object> queryParameters, Class<T> entityClass);

    <T> DBCursor findCursorForAll(Class<T> entityClass);

	<T> void findAllAndRemoveByDatasetId(Integer datasetId, Class<T> entityClass)
			throws MongoException;

	<T> List<T> findByDatasetId(Integer datasetId, Class<T> entityClass)
			throws MongoException;

	<T> DBCursor findCursorByDatasetId(Integer datasetId, Class<T> entityClass);

	<T> T findOneByDatasetIdSortBy(Integer datasetId, String field,
			Direction direction, Class<T> entityClass);

	void insertStreamingStatus(StreamingStatus streamingStatus);

	void insertUploadedStatus(UploadedStatus uploadedStatus);



}