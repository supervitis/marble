package org.marble.commons.service;

import java.util.List;

import org.marble.commons.dao.PlotDao;
import org.marble.commons.dao.model.Plot;
import org.marble.commons.exception.InvalidPlotException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PlotServiceImpl implements PlotService {

    //private static final Logger log = LoggerFactory.getLogger(PlotServiceImpl.class);

    @Autowired
    PlotDao plotDao;
    
    @Override
    public Plot findOne(Integer id) throws InvalidPlotException {
        Plot plot = plotDao.findOne(id);
        if (plot == null) {
            throw new InvalidPlotException();
        }
        return plot;
    }

    @Override
    public Plot save(Plot plot) throws InvalidPlotException {
        plot = plotDao.save(plot);
        if (plot == null) {
            throw new InvalidPlotException();
        }
        return plot;
    }

    @Override
    public List<Plot> findByTopic(Integer topicId) {
        List<Plot> plots = plotDao.findByTopic_id(topicId);
        return plots;
    }

    @Override
    public Long count() {
        return plotDao.count();
    }

    @Override
    public void delete(Integer id) {
        plotDao.delete(id);
        return;
    }

}
