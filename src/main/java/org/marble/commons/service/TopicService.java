package org.marble.commons.service;

import java.util.List;

import org.marble.commons.dao.model.OriginalStatus;
import org.marble.commons.dao.model.Topic;
import org.marble.commons.exception.InvalidTopicException;
import org.marble.commons.model.TopicInfo;

public interface TopicService {

	public Topic save(Topic topic) throws InvalidTopicException;

	public Topic findOne(Integer id) throws InvalidTopicException;

	List<Topic> findAll();

	public void delete(Integer id);

    Long count();

    TopicInfo info(Integer id) throws InvalidTopicException;

	List<OriginalStatus> findAllStatusByTopicId(Integer topicId);

}
