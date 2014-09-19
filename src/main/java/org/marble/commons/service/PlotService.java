package org.marble.commons.service;

import java.util.List;
import java.util.Set;

import org.marble.commons.dao.model.Plot;
import org.marble.commons.exception.InvalidPlotException;
import org.marble.commons.model.ExecutionModuleDefinition;

public interface PlotService {

    public Plot save(Plot plot) throws InvalidPlotException;

    public Plot findOne(Integer id) throws InvalidPlotException;
    
    public List<Plot> findByTopic(Integer topicId);

    public void delete(Integer id);

    Long count();

}
