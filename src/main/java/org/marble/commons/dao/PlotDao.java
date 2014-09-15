package org.marble.commons.dao;

import java.util.List;

import org.marble.commons.dao.model.Plot;

import org.springframework.data.repository.CrudRepository;

public interface PlotDao extends CrudRepository<Plot,Integer> {
    List<Plot> findByTopic_id(Integer id);
}
