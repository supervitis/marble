package org.marble.commons.dao;

import java.util.List;

import org.marble.commons.dao.model.StreamingTopic;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface StreamingTopicDao extends PagingAndSortingRepository<StreamingTopic, Integer>{
	List<StreamingTopic> findAll();
}
