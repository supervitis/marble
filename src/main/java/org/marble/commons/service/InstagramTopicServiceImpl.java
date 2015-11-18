package org.marble.commons.service;

import java.util.List;

import org.marble.commons.dao.InstagramTopicDao;
import org.marble.commons.dao.model.InstagramStatus;
import org.marble.commons.dao.model.InstagramTopic;
import org.marble.commons.exception.InvalidInstagramTopicException;
import org.marble.commons.model.InstagramTopicInfo;

import com.mongodb.DBCursor;
import com.mongodb.MongoException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
public class InstagramTopicServiceImpl implements InstagramTopicService {

    private static final Logger log = LoggerFactory.getLogger(InstagramTopicServiceImpl.class);
    @Autowired
    InstagramTopicDao instagramTopicDao;

    @Autowired
    DatastoreService datastoreService;

    @Override
    public InstagramTopic save(InstagramTopic instagramTopic) throws InvalidInstagramTopicException {
        // TODO Modify this in order to update only certain fields (and do not
        // overwrite the status)
        instagramTopic = instagramTopicDao.save(instagramTopic);
        if (instagramTopic == null) {
            throw new InvalidInstagramTopicException();
        }
        return instagramTopic;
    }

    @Override
    public InstagramTopic findOne(Integer id) throws InvalidInstagramTopicException {
        InstagramTopic instagramTopic = instagramTopicDao.findOne(id);
        if (instagramTopic == null) {
            throw new InvalidInstagramTopicException();
        }
        return instagramTopic;
    }

    @Override
    public List<InstagramTopic> findAll() {
        List<InstagramTopic> instagramTopics = instagramTopicDao.findAll();
        return instagramTopics;
    }

    @Override
    public void delete(Integer id) {
        instagramTopicDao.delete(id);
        // Remove all the related tweets from the database
        datastoreService.findAllAndRemoveByInstagramTopicId(id, InstagramStatus.class);
        return;
    }

    @Override
    public InstagramTopicInfo info(Integer id) throws InvalidInstagramTopicException {
        // This is only to check if exists
        InstagramTopic instagramTopic = instagramTopicDao.findOne(id);
        if (instagramTopic == null) {
            throw new InvalidInstagramTopicException();
        }

        InstagramTopicInfo instagramTopicInfo = new InstagramTopicInfo();
        instagramTopicInfo.setTopicId(id);
        instagramTopicInfo.setActive(instagramTopic.getActive());
        try {
            instagramTopicInfo.setTotalStatusesExtracted(datastoreService.countByInstagramTopicId(id, InstagramStatus.class));

            if (instagramTopicInfo.getTotalStatusesExtracted() > 0) {
                InstagramStatus status = datastoreService.findOneByInstagramTopicIdSortBy(id, "createdAt", Sort.Direction.ASC,
                        InstagramStatus.class);
                instagramTopicInfo.setOldestStatusDate(status.getCreatedTime());
                instagramTopicInfo.setOldestStatusId(status.getId());

                status = datastoreService
                        .findOneByInstagramTopicIdSortBy(id, "createdTime", Sort.Direction.DESC, InstagramStatus.class);
                instagramTopicInfo.setNewestStatusDate(status.getCreatedTime());
                instagramTopicInfo.setNewestStatusId(status.getId());
            }
        } catch (MongoException e) {
            log.warn(
                    "Exception caught while extracting the instagramTopic info.",
                    e);
        }
        return instagramTopicInfo;
    }

    @Override
    public Long count() {
        return instagramTopicDao.count();
    }
	
	@Override
	public DBCursor findCursorByInstagramTopicId(Integer instagramTopicId){
		return datastoreService.findCursorByInstagramTopicId(instagramTopicId, InstagramStatus.class);
	}

}
