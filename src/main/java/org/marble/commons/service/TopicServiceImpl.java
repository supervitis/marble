package org.marble.commons.service;

import org.marble.commons.dao.TopicDao;
import org.marble.commons.dao.UserDao;
import org.marble.commons.dao.model.Topic;
import org.marble.commons.dao.model.User;
import org.marble.commons.exception.InvalidTopicException;
import org.marble.commons.exception.InvalidUserException;
import org.marble.commons.model.SignupForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
public class TopicServiceImpl implements TopicService {

    @Autowired
    TopicDao topicDao;

	@Override
	public Topic updateTopic(Topic topic) throws InvalidTopicException {
		topic = topicDao.save(topic);
		return topic;
	}
	
	@Override
	public Topic getTopic(Integer id) throws InvalidTopicException {
		Topic topic = topicDao.findOne(id);
		return topic;
	}
}
