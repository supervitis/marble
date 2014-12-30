package org.marble.commons.service;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.marble.commons.executor.plotter.PlotterExecutor;
import org.marble.commons.executor.processor.ProcessorExecutor;
import org.marble.commons.model.ExecutionModuleDefinition;

import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class ModulesServiceImpl implements ModuleService {

    private final String plotterPackage = "org.marble.commons.executor.plotter";
    private final String processorPackage = "org.marble.commons.executor.processor";
    private static final Logger log = LoggerFactory.getLogger(ModulesServiceImpl.class);

    @SuppressWarnings("unchecked")
    @Override
    public <T> List<ExecutionModuleDefinition> getModules(String packageString, Class<T> superType) {
        List<ExecutionModuleDefinition> modules = new ArrayList<>();

        Reflections reflections = new Reflections(packageString);
        Set<Class<? extends T>> implementors = reflections.getSubTypesOf(superType);

        for (Class<? extends T> implementor : implementors) {
            // Get Name
            ExecutionModuleDefinition module = new ExecutionModuleDefinition();
            module.setName(implementor.getName());
            module.setSimpleName(implementor.getSimpleName());
            // Get Operations
            try {
                Field operations = implementor.getField("availableOperations");
                module.setOperations((Map<String, String>) operations.get(null));
            } catch (NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException e) {
                log.error("An error ocurred while extracting operations for class " + implementor.getName());
                continue;
            }
            // Get Parameters
            try {
                Field parameters = implementor.getField("availableParameters");
                module.setParameters((Map<String, String>) parameters.get(null));
            } catch (NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException e) {
                log.error("An error ocurred while extracting parameters for class " + implementor.getName());
                continue;
            }

            modules.add(module);
        }
        return modules;
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> ExecutionModuleDefinition getModule(String moduleName, String packageString, Class<T> superType) {
        Reflections reflections = new Reflections(packageString);
        Set<Class<? extends T>> implementors = reflections.getSubTypesOf(superType);
        ExecutionModuleDefinition module = null;
        for (Class<? extends T> implementor : implementors) {
            if (implementor.getName().equals(moduleName)) {
                module = new ExecutionModuleDefinition();
                module.setName(implementor.getName());
                module.setSimpleName(implementor.getSimpleName());
                // Get Operations
                try {
                    Field operations = implementor.getField("availableOperations");
                    module.setOperations((Map<String, String>) operations.get(null));
                } catch (NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException e) {
                    log.error("An error ocurred while extracting operations for class " + implementor.getName());
                    continue;
                }
                // Get Parameters
                try {
                    Field parameters = implementor.getField("availableParameters");
                    module.setParameters((Map<String, String>) parameters.get(null));
                } catch (NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException e) {
                    log.error("An error ocurred while extracting parameters for class " + implementor.getName());
                    continue;
                }
                break;
            }
        }
        return module;

    }

    @Override
    public ExecutionModuleDefinition getPlotterModule(String moduleName) {
        return getModule(moduleName, this.plotterPackage, PlotterExecutor.class);
    }
    
    @Override
    public ExecutionModuleDefinition getProcessorModule(String moduleName) {
        return getModule(moduleName, this.processorPackage, ProcessorExecutor.class);
    }

    @Override
    public List<ExecutionModuleDefinition> getPlotterModules() {
        return getModules(this.plotterPackage, PlotterExecutor.class);
    }
    
    @Override
    public List<ExecutionModuleDefinition> getProcessorModules() {
        return getModules(this.processorPackage, ProcessorExecutor.class);
    }

}
