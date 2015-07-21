package org.marble.commons.service;

import java.util.List;

import org.marble.commons.dao.TopicDao;
import org.marble.commons.dao.model.OriginalStatus;
import org.marble.commons.dao.model.ProcessedStatus;
import org.marble.commons.dao.model.Topic;
import org.marble.commons.exception.InvalidTopicException;
import org.marble.commons.model.TopicInfo;

import com.mongodb.MongoException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
public class TopicServiceImpl implements TopicService {

    private static final Logger log = LoggerFactory.getLogger(TopicServiceImpl.class);
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
    public List<OriginalStatus> findAllStatusByTopicId(Integer topicId){
    	return datastoreService.findByTopicId(topicId, OriginalStatus.class);
    }

    @Override
    public TopicInfo info(Integer id) throws InvalidTopicException {
        // This is only to check if exists
        Topic topic = topicDao.findOne(id);
        if (topic == null) {
            throw new InvalidTopicException();
        }

        TopicInfo topicInfo = new TopicInfo();
        topicInfo.setTopicId(id);
        try {
            topicInfo.setTotalStatusesExtracted(datastoreService.countByTopicId(id, OriginalStatus.class));
            topicInfo.setTotalStatusesProcessed(datastoreService.countByTopicId(id, ProcessedStatus.class));

            if (topicInfo.getTotalStatusesExtracted() > 0) {
                OriginalStatus status = datastoreService.findOneByTopicIdSortBy(id, "createdAt", Sort.Direction.ASC,
                        OriginalStatus.class);
                topicInfo.setOldestStatusDate(status.getCreatedAt());
                topicInfo.setOldestStatusId(status.getId());

                status = datastoreService
                        .findOneByTopicIdSortBy(id, "createdAt", Sort.Direction.DESC, OriginalStatus.class);
                topicInfo.setNewestStatusDate(status.getCreatedAt());
                topicInfo.setNewestStatusId(status.getId());
            }
        } catch (MongoException e) {
            log.warn(
                    "Exception caught while extracting the topic info.",
                    e);
        }
        return topicInfo;
    }

    @Override
    public Long count() {
        return topicDao.count();
    }

}
