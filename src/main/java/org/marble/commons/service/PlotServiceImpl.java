package org.marble.commons.service;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.marble.commons.dao.PlotDao;
import org.marble.commons.dao.model.Plot;
import org.marble.commons.exception.InvalidPlotException;
import org.marble.commons.executor.plotter.PlotterExecutor;
import org.marble.commons.model.PlotModule;

import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PlotServiceImpl implements PlotService {

    private static final Logger log = LoggerFactory.getLogger(PlotServiceImpl.class);

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

    @SuppressWarnings("unchecked")
    @Override
    public List<PlotModule> getModules() {
        List<PlotModule> modules = new ArrayList<>();

        Reflections reflections = new Reflections("org.marble.commons.executor.plotter");
        Set<Class<? extends PlotterExecutor>> implementors = reflections.getSubTypesOf(PlotterExecutor.class);

        for (Class<? extends PlotterExecutor> implementor : implementors) {
            // Get Name
            PlotModule module = new PlotModule();
            module.setName(implementor.getName());
            module.setSimpleName(implementor.getSimpleName());
            // Get Operations
            try {
                Field operations = implementor.getField("availableOperations");
                module.setOperations((Map<String, String>) operations.get(null));
            } catch (NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException e) {
                log.error("An error ocurred while extracting operations for class "+ implementor.getName());
                continue;
            }
            // Get Parameters
            try {
                Field parameters = implementor.getField("availableParameters");
                module.setParameters((Map<String, String>) parameters.get(null));
            } catch (NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException e) {
                log.error("An error ocurred while extracting parameters for class "+ implementor.getName());
                continue;
            }
            
            modules.add(module);
        }
        return modules;
    }

}
