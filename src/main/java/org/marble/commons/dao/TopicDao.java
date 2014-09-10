package org.marble.commons.dao;

import java.util.List;

import org.marble.commons.dao.model.Topic;

import org.springframework.data.repository.PagingAndSortingRepository;

public interface TopicDao extends PagingAndSortingRepository<Topic, Integer>{
	List<Topic> findAll();
}
