package org.marble.commons.service;

import java.util.List;
import java.util.Set;

import org.marble.commons.dao.model.Plot;
import org.marble.commons.exception.InvalidPlotException;
import org.marble.commons.model.PlotModule;

public interface ModuleService {

    List<PlotModule> getPlotterModules();

    PlotModule getPlotterModule(String module);

    <T> List<PlotModule> getModules(String packageString, Class<T> superType);

    <T> PlotModule getModule(String moduleName, String packageString, Class<T> superType);

}
