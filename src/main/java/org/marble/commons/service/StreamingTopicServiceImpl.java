package org.marble.commons.service;

import java.util.List;

import org.marble.commons.dao.StreamingTopicDao;
import org.marble.commons.dao.model.StreamingStatus;
import org.marble.commons.dao.model.StreamingTopic;
import org.marble.commons.exception.InvalidStreamingTopicException;
import org.marble.commons.model.StreamingTopicInfo;

import com.mongodb.DBCursor;
import com.mongodb.MongoException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
public class StreamingTopicServiceImpl implements StreamingTopicService {

    private static final Logger log = LoggerFactory.getLogger(StreamingTopicServiceImpl.class);
    @Autowired
    StreamingTopicDao streamingTopicDao;

    @Autowired
    DatastoreService datastoreService;

    @Override
    public StreamingTopic save(StreamingTopic streamingTopic) throws InvalidStreamingTopicException {
        // TODO Modify this in order to update only certain fields (and do not
        // overwrite the status)
        streamingTopic = streamingTopicDao.save(streamingTopic);
        if (streamingTopic == null) {
            throw new InvalidStreamingTopicException();
        }
        return streamingTopic;
    }

    @Override
    public StreamingTopic findOne(Integer id) throws InvalidStreamingTopicException {
        StreamingTopic streamingTopic = streamingTopicDao.findOne(id);
        if (streamingTopic == null) {
            throw new InvalidStreamingTopicException();
        }
        return streamingTopic;
    }

    @Override
    public List<StreamingTopic> findAll() {
        List<StreamingTopic> streamingTopics = streamingTopicDao.findAll();
        return streamingTopics;
    }

    @Override
    public void delete(Integer id) {
        streamingTopicDao.delete(id);
        // Remove all the related tweets from the database
        datastoreService.findAllAndRemoveByStreamingTopicId(id, StreamingStatus.class);
        return;
    }

    @Override
    public StreamingTopicInfo info(Integer id) throws InvalidStreamingTopicException {
        // This is only to check if exists
        StreamingTopic streamingTopic = streamingTopicDao.findOne(id);
        if (streamingTopic == null) {
            throw new InvalidStreamingTopicException();
        }

        StreamingTopicInfo streamingTopicInfo = new StreamingTopicInfo();
        streamingTopicInfo.setTopicId(id);
        try {
            streamingTopicInfo.setTotalStatusesExtracted(datastoreService.countByStreamingTopicId(id, StreamingStatus.class));

            if (streamingTopicInfo.getTotalStatusesExtracted() > 0) {
                StreamingStatus status = datastoreService.findOneByStreamingTopicIdSortBy(id, "createdAt", Sort.Direction.ASC,
                        StreamingStatus.class);
                streamingTopicInfo.setOldestStatusDate(status.getCreatedAt());
                streamingTopicInfo.setOldestStatusId(status.getId());

                status = datastoreService
                        .findOneByStreamingTopicIdSortBy(id, "createdAt", Sort.Direction.DESC, StreamingStatus.class);
                streamingTopicInfo.setNewestStatusDate(status.getCreatedAt());
                streamingTopicInfo.setNewestStatusId(status.getId());
            }
        } catch (MongoException e) {
            log.warn(
                    "Exception caught while extracting the streamingTopic info.",
                    e);
        }
        return streamingTopicInfo;
    }

    @Override
    public Long count() {
        return streamingTopicDao.count();
    }

	@Override
	public List<StreamingStatus> findAllStatusByStreamingTopicId(Integer id) {
    	return datastoreService.findByStreamingTopicId(id, StreamingStatus.class);
	}
	
	@Override
	public DBCursor findCursorByStreamingTopicId(Integer streamingTopicId){
		return datastoreService.findCursorByStreamingTopicId(streamingTopicId, StreamingStatus.class);
	}

}
