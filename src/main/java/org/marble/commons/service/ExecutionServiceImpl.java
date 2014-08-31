package org.marble.commons.service;

import java.util.List;

import javax.transaction.Transactional;

import org.marble.commons.dao.ExecutionDao;
import org.marble.commons.dao.model.Execution;
import org.marble.commons.dao.model.ExecutionType;
import org.marble.commons.dao.model.Topic;
import org.marble.commons.exception.InvalidExecutionException;
import org.marble.commons.exception.InvalidTopicException;
import org.marble.commons.executor.Executor;
import org.marble.commons.model.ExecutionStatus;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.core.task.TaskExecutor;
import org.springframework.stereotype.Service;

@Service
public class ExecutionServiceImpl implements ExecutionService {

    private static final Logger log = LoggerFactory.getLogger(ExecutionServiceImpl.class);
    
    @Autowired
    ExecutionDao executionDao;
    
    @Autowired
    TopicService topicService;
    
    @Autowired
    private TaskExecutor taskExecutor;
    @Autowired
    private ApplicationContext context;
	
    @Override
	public Execution findOne(Integer id) throws InvalidExecutionException {
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
    public Execution save(Execution execution) throws InvalidExecutionException {
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

    @Override
    @Transactional
    public Integer executeExtractor(Integer topicId) throws InvalidTopicException, InvalidExecutionException {
        // Special function to perform special operations ;)
        log.info("Executing the extractor for topic <"+topicId +">.");

        Execution execution = new Execution();

        Topic topic = topicService.getTopic(topicId);
        
        execution.setStatus(ExecutionStatus.Initialized);
        execution.setType(ExecutionType.Extractor);
        topic.getExecutions().add(execution);
        execution.setTopic(topic);
        topic = topicService.updateTopic(topic);
        
        execution = this.save(execution);

        log.info("Starting execution <" + execution.getId() + ">... now!");
        Executor executor = (Executor) context.getBean("twitterExtractionExecutor");
        executor.setExecution(execution);
        taskExecutor.execute(executor);

        log.info("That's it. Have fun!");

        return execution.getId();
    }
}
