package org.marble.commons.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.convert.MongoConverter;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.query.BasicQuery;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import org.marble.commons.dao.model.OriginalStatus;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.MongoException;

@Service
public class DatastoreServiceImpl implements DatastoreService {

    @Autowired
    MongoOperations mongoOperations;
    
    @Autowired
    MongoTemplate mongoTemplate;

    @Autowired
    ObjectMapper objectMapper;

    private static final Logger log = LoggerFactory.getLogger(DatastoreServiceImpl.class);

    @Override
    public <T> long countAll(Class<T> entityClass) {
        Query query = new Query();
       return mongoOperations.count(query, entityClass);
    }
    
    @Override
    public <T> List<T> findAll(Class<T> entityClass) {
       return mongoOperations.findAll(entityClass);
    }

    @Override
    public <T> void findAllAndRemove(Query query, Class<T> entityClass) {
        mongoOperations.findAllAndRemove(query, entityClass);
    }

    @Override
    public <T> void findAllAndRemoveByTopicId(Integer topicId, Class<T> entityClass) throws MongoException {
        Query query = new BasicQuery("{'topicId': " + topicId + "}");
        this.findAllAndRemove(query, entityClass);
    }

    @Override
    public <T> List<T> findByQuery(Query query, Class<T> entityClass) throws MongoException {
        List<T> result = mongoOperations.find(query, entityClass);
        if (result == null) {
            throw new MongoException("Object <" + entityClass.getName() + "> with query <" + query + "> not found.");
        }
        return result;
    }
    
    @Override
    public <T> List<T> findByTopicId(Integer topicId, Class<T> entityClass) throws MongoException {
        Query query = new Query();
        query.addCriteria(Criteria.where("topicId").is(topicId));
        return this.findByQuery(query, entityClass);
    }
    
    @Override
    public <T> DBCursor findCursorByTopicId(Integer topicId, Class<T> entityClass) {
        Document document = entityClass.getAnnotation(Document.class);
        DBCollection collection = mongoOperations.getCollection(document.collection());
        
        BasicDBObject searchQuery = new BasicDBObject();
        searchQuery.put("topicId", topicId);

        DBCursor cursor = collection.find(searchQuery);
        return cursor;
    }

    @Override
    public <T> T findOneByQuery(Query query, Class<T> entityClass) throws MongoException {
        T result = mongoOperations.findOne(query, entityClass);
        if (result == null) {
            throw new MongoException("Object <" + entityClass.getName() + "> with query <" + query + "> not found.");
        }
        return result;
    }

    @Override
    public <T> T findOneByText(String text, Class<T> entityClass) throws MongoException {
        Query query = new Query();
        query.addCriteria(Criteria.where("text").is(text));
        return this.findOneByQuery(query, entityClass);
    }
    

    @Override
    public MongoConverter getConverter() {
        return mongoOperations.getConverter();
    }

    @Override
    public void insertOriginalStatus(OriginalStatus originalStatus) {
        mongoOperations.save(originalStatus);
    }

    @Override
    public <T> void removeCollection(Class<T> entityClass) {
        mongoOperations.dropCollection(entityClass);

    }

    @Override
    public <T> void save(T object) {
        mongoOperations.save(object);

    }

}