package org.marble.commons.executor;

import org.marble.commons.dao.model.Execution;

public interface Executor extends Runnable {

    @Override
    public void run();
    
    public String getName();

    void setExecution(Execution execution);
}
