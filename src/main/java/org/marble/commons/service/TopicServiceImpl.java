package org.marble.commons.service;

import java.util.List;

import org.marble.commons.dao.TopicDao;
import org.marble.commons.dao.UserDao;
import org.marble.commons.dao.model.Topic;
import org.marble.commons.dao.model.User;
import org.marble.commons.exception.InvalidTopicException;
import org.marble.commons.exception.InvalidUserException;
import org.marble.commons.model.SignupForm;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
public class TopicServiceImpl implements TopicService {

    @Autowired
    TopicDao topicDao;

	@Override
	public Topic updateTopic(Topic topic) throws InvalidTopicException {
		// TODO Modify this in order to update only certain fields (and do not overwrite the status)
		topic = topicDao.save(topic);
		if (topic == null) {
			throw new InvalidTopicException();
		}
		return topic;
	}
	
	@Override
	public Topic getTopic(Integer id) throws InvalidTopicException {
		Topic topic = topicDao.findOne(id);
		if (topic == null) {
			throw new InvalidTopicException();
		}
		return topic;
	}
	
	@Override
	public List<Topic> getTopics(){
		List<Topic> topics = topicDao.findAll();
		return topics;
	}
}
