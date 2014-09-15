package org.marble.commons.service;

import java.util.List;

import org.marble.commons.dao.model.Plot;
import org.marble.commons.exception.InvalidPlotException;

public interface PlotService {

    public Plot save(Plot plot) throws InvalidPlotException;

    public Plot findOne(Integer id) throws InvalidPlotException;
    
    public List<Plot> findByTopic(Integer topicId);

    public void delete(Integer id);

    Long count();

}
