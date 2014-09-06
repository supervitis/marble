package org.marble.commons.service;

import java.util.List;

import org.marble.commons.dao.model.Topic;
import org.marble.commons.exception.InvalidTopicException;

public interface TopicService {

	public Topic save(Topic topic) throws InvalidTopicException;

	public Topic findOne(Integer id) throws InvalidTopicException;

	List<Topic> findAll();

	public void delete(Integer id);

	public Topic create(Topic topic) throws InvalidTopicException;

    Long count();

}
