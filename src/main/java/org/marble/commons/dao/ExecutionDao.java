package org.marble.commons.dao;

import org.marble.commons.dao.model.Execution;
import org.springframework.data.repository.CrudRepository;

public interface ExecutionDao extends CrudRepository<Execution,Integer> {
}
