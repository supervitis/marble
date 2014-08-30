package org.marble.commons.service;

import java.util.List;

import org.marble.commons.dao.TopicDao;
import org.marble.commons.dao.model.Topic;
import org.marble.commons.exception.InvalidTopicException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
	public List<Topic> getTopics() {
		List<Topic> topics = topicDao.findAll();
		return topics;
	}

	@Override
	public void deleteTopic(Integer id) {
		topicDao.delete(id);
		return;
	}

	@Override
	public Topic createTopic(Topic topic) throws InvalidTopicException {
		topic = topicDao.save(topic);
		if (topic == null) {
			throw new InvalidTopicException();
		}
		return topic;
	}
}
