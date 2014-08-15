package org.marble.commons.service;

import org.marble.commons.dao.model.Topic;
import org.marble.commons.exception.InvalidTopicException;
import org.marble.commons.exception.InvalidUserException;
import org.marble.commons.model.SignupForm;

public interface TopicService {

	public Topic updateTopic(Topic topic) throws InvalidTopicException;

	public Topic getTopic(Integer id) throws InvalidTopicException;

}
