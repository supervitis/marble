package org.marble.commons.service;

import java.util.List;
import org.marble.commons.model.ExecutionModuleDefinition;

public interface ModuleService {

    List<ExecutionModuleDefinition> getPlotterModules();

    ExecutionModuleDefinition getPlotterModule(String module);

    <T> List<ExecutionModuleDefinition> getModules(String packageString, Class<T> superType);

    <T> ExecutionModuleDefinition getModule(String moduleName, String packageString, Class<T> superType);

}
