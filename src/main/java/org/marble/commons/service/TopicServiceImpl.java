package org.marble.commons.service;

import java.util.List;

import org.marble.commons.dao.TopicDao;
import org.marble.commons.dao.model.OriginalStatus;
import org.marble.commons.dao.model.ProcessedStatus;
import org.marble.commons.dao.model.Topic;
import org.marble.commons.exception.InvalidTopicException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TopicServiceImpl implements TopicService {

    @Autowired
    TopicDao topicDao;

    @Autowired
    DatastoreService datastoreService;

    @Override
    public Topic save(Topic topic) throws InvalidTopicException {
        // TODO Modify this in order to update only certain fields (and do not
        // overwrite the status)
        topic = topicDao.save(topic);
        if (topic == null) {
            throw new InvalidTopicException();
        }
        return topic;
    }

    @Override
    public Topic findOne(Integer id) throws InvalidTopicException {
        Topic topic = topicDao.findOne(id);
        if (topic == null) {
            throw new InvalidTopicException();
        }
        return topic;
    }

    @Override
    public List<Topic> findAll() {
        List<Topic> topics = topicDao.findAll();
        return topics;
    }

    @Override
    public void delete(Integer id) {
        topicDao.delete(id);
        // Remove all the related tweets from the database
        datastoreService.findAllAndRemoveByTopicId(id, OriginalStatus.class);
        datastoreService.findAllAndRemoveByTopicId(id, ProcessedStatus.class);
        return;
    }

    @Override
    public Topic create(Topic topic) throws InvalidTopicException {
        topic = topicDao.save(topic);
        if (topic == null) {
            throw new InvalidTopicException();
        }
        return topic;
    }

    @Override
    public Long count() {
        return topicDao.count();
    }

}
