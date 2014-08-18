package org.marble.commons.dao;

import java.util.List;

import org.marble.commons.dao.model.Topic;
import org.springframework.data.repository.CrudRepository;

public interface TopicDao extends CrudRepository<Topic, Integer>{
	List<Topic> findAll();
}
