package org.marble.commons.dao;

import java.util.List;

import org.marble.commons.dao.model.Dataset;

import org.springframework.data.repository.CrudRepository;

public interface DatasetDao extends CrudRepository<Dataset,Integer> {
	List<Dataset> findAll();
}
