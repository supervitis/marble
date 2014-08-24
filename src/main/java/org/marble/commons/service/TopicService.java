package org.marble.commons.service;

import java.util.List;

import org.marble.commons.dao.model.Topic;
import org.marble.commons.exception.InvalidTopicException;

public interface TopicService {

	public Topic updateTopic(Topic topic) throws InvalidTopicException;

	public Topic getTopic(Integer id) throws InvalidTopicException;

	List<Topic> getTopics();

	public void deleteTopic(Integer id);

	public Topic createTopic(Topic topic) throws InvalidTopicException;

}
