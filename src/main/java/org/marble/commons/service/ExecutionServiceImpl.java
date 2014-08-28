package org.marble.commons.service;

import java.util.List;

import org.marble.commons.dao.ExecutionDao;
import org.marble.commons.dao.model.Execution;
import org.marble.commons.dao.model.Topic;
import org.marble.commons.exception.InvalidExecutionException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ExecutionServiceImpl implements ExecutionService {

    @Autowired
    ExecutionDao executionDao;
	
    @Override
	public Execution getExecution(Integer id) throws InvalidExecutionException {
        Execution execution = executionDao.findOne(id);
        if (execution == null) {
            throw new InvalidExecutionException();
        }
        return execution;
    }

    @Override
    public void appendToLog(Integer id, String log) throws InvalidExecutionException {
        Execution execution = executionDao.findOne(id);
        if (execution == null) {
            throw new InvalidExecutionException();
        }
        execution.appendLog(log);
        executionDao.save(execution);    
        return;
    }

    @Override
    public Execution updateExecution(Execution execution) throws InvalidExecutionException {
        execution = executionDao.save(execution);
        if (execution == null) {
            throw new InvalidExecutionException();
        }
        return execution;
    }

    @Override
    public List<Execution> getExecutionsPerTopic(Integer topicId) {
        List<Execution> executions = executionDao.findByTopic_id(topicId);
        return executions;
    }
}
