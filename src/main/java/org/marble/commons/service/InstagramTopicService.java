package org.marble.commons.service;

import java.util.List;

import org.marble.commons.dao.model.InstagramStatus;
import org.marble.commons.dao.model.InstagramTopic;
import org.marble.commons.exception.InvalidInstagramTopicException;
import org.marble.commons.model.InstagramTopicInfo;

import com.mongodb.DBCursor;

public interface InstagramTopicService {

	public InstagramTopic save(InstagramTopic instagramTopic) throws InvalidInstagramTopicException;

	public InstagramTopic findOne(Integer id) throws InvalidInstagramTopicException;

	List<InstagramTopic> findAll();

	public void delete(Integer id);

    Long count();

    InstagramTopicInfo info(Integer id) throws InvalidInstagramTopicException;

	DBCursor findCursorByInstagramTopicId(Integer instagramTopicId);

}
