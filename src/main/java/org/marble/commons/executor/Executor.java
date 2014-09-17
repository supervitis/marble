package org.marble.commons.executor;

import org.marble.commons.dao.model.Execution;

public interface Executor extends Runnable {

    @Override
    public void run();

    void setExecution(Execution execution);
}
