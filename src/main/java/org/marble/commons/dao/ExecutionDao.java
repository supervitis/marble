package org.marble.commons.dao;

import java.util.List;

import org.marble.commons.dao.model.Execution;
import org.springframework.data.repository.CrudRepository;

public interface ExecutionDao extends CrudRepository<Execution,Integer> {
    List<Execution> findByTopic_id(Integer id);

	List<Execution> findByInstagramTopic_id(Integer instagramTopicId);
}
