package org.marble.commons.executor.processor;

import java.util.Map;

import org.marble.commons.executor.Executor;

public interface ProcessorExecutor extends Executor {

    public Map<String, String> getParameters();

    public void setParameters(Map<String, String> parameters);
}
