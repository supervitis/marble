package org.marble.commons.executor.plotter;

import java.util.Map;
import java.util.Set;

import org.marble.commons.executor.Executor;

public interface PlotterExecutor extends Executor {

    public String getOperation();

    public void setOperation(String operation);

    public Map<String, String> getParameters();

    public void setParameters(Map<String, Object> parameters);

}
