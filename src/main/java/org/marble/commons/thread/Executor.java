package org.marble.commons.thread;

import org.marble.commons.dao.model.Execution;

public interface Executor extends Runnable {

    @Override
    public void run();

    void setExecution(Execution execution);
}
