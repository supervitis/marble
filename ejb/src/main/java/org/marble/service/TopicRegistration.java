package org.marble.service;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.log4j.Logger;
import org.marble.data.datastore.MongoDatastoreOperations;
import org.marble.model.Topic;
import org.marble.util.Constants;

import javax.ejb.Stateless;
import javax.enterprise.event.Event;
import javax.inject.Inject;
import javax.persistence.EntityManager;

// The @Stateless annotation eliminates the need for manual transaction demarcation
@Stateless
public class TopicRegistration {

    @Inject
    private Logger                     log;

    @Inject
    private EntityManager              em;

    @Inject
    private Event<Topic> topicEventSrc;
    
    @Inject
    private MongoDatastoreOperations mongoDatastoreOperations;

    public void register(Topic topic) throws Exception {
        log.info("Updating/Inserting " + topic.getName());
        topic = em.merge(topic);
        topicEventSrc.fire(topic);
    }
    
    public void update(Topic topic) throws Exception {
        log.info("Updating " + topic.getName());
        Topic oldTopic = em.find(Topic.class, topic.getName());
        if (oldTopic != null) {
            oldTopic.update(topic);
        }
        else {
            oldTopic = topic;
        }
        topic = em.merge(oldTopic);
        topicEventSrc.fire(topic);
    }

    public void delete(Topic topic) throws Exception {
        log.info("Deleting " + topic.getName());
        topic = em.find(Topic.class, topic.getName());

        em.remove(topic);
        topicEventSrc.fire(topic);
    }

    public void deleteStatuses(Topic topic) {
        log.info("Deleting statuses for " + topic.getName());
        // Actually, no removal is performed, only the collection is renamed
        DateFormat dateFormat = new SimpleDateFormat(Constants.DROP_COLLECTION_SUFFIX);
        Date date = new Date();
        String dateSuffix = dateFormat.format(date);
        String newName = topic.getName() + "_" + dateSuffix;
        mongoDatastoreOperations.setCollection(topic.getName());
        mongoDatastoreOperations.renameCollection(newName);
        mongoDatastoreOperations.setCollection(topic.getName() + Constants.MONGO_PROCESSED_SUFFIX);
        newName = topic.getName() + Constants.MONGO_PROCESSED_SUFFIX + "_" + dateSuffix;
        mongoDatastoreOperations.renameCollection(newName);
        
    }
    
    public void refreshMongoStatus(Topic topic) {
        log.info("Refreshing Mongo status for "+ topic.getName());
        mongoDatastoreOperations.setCollection(topic.getName());
        topic.getTopicStatus().setDatabaseStatus(mongoDatastoreOperations.getMongoStatus());
        em.merge(topic);
        topicEventSrc.fire(topic);
    }
}
