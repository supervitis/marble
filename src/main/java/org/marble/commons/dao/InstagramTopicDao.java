package org.marble.commons.dao;

import java.util.List;

import org.marble.commons.dao.model.InstagramTopic;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface InstagramTopicDao extends PagingAndSortingRepository<InstagramTopic, Integer>{
	List<InstagramTopic> findAll();
}
