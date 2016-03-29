package org.marble.commons.service;

import java.beans.Introspector;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.transaction.Transactional;

import org.marble.commons.dao.ExecutionDao;
import org.marble.commons.dao.model.Execution;
import org.marble.commons.dao.model.InstagramTopic;
import org.marble.commons.dao.model.StreamingTopic;
import org.marble.commons.dao.model.Topic;
import org.marble.commons.exception.InvalidExecutionException;
import org.marble.commons.exception.InvalidInstagramTopicException;
import org.marble.commons.exception.InvalidModuleException;
import org.marble.commons.exception.InvalidStreamingTopicException;
import org.marble.commons.exception.InvalidTopicException;
import org.marble.commons.executor.extractor.ExtractorExecutor;
import org.marble.commons.executor.plotter.PlotterExecutor;
import org.marble.commons.executor.processor.ProcessorExecutor;
import org.marble.commons.executor.streaming.StreamingExecutor;
import org.marble.commons.executor.streaming.TwitterStreamingExecutor;
import org.marble.commons.model.ExecutionStatus;
import org.marble.commons.model.ExecutionType;
import org.marble.commons.model.ExecutionModuleParameters;
import org.marble.commons.model.ExecutionModuleDefinition;
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
    StreamingTopicService streamingTopicService;
    
    @Autowired
    InstagramTopicService instagramTopicService;

    @Autowired
    PlotService plotService;

    @Autowired
    ModuleService moduleService;

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
        log.info("Executing the extractor for topic <" + topicId + ">.");

        Execution execution = new Execution();

        Topic topic = topicService.findOne(topicId);

        execution.setStatus(ExecutionStatus.Initialized);
        execution.setType(ExecutionType.Extractor);
        topic.getExecutions().add(execution);
        execution.setTopic(topic);
        topic = topicService.save(topic);

        execution = this.save(execution);

        log.info("Starting execution <" + execution.getId() + ">... now!");
        ExtractorExecutor executor = (ExtractorExecutor) context.getBean("twitterExtractionExecutor");
        executor.setExecution(execution);
        taskExecutor.execute(executor);

        log.info("Executor launched.");

        return execution.getId();
    }
    
    @Override
    @Transactional
    public Integer executeInstagramExtractor(Integer topicId) throws InvalidInstagramTopicException, InvalidExecutionException {
        log.info("Executing the extractor for Instagram topic <" + topicId + ">.");

        Execution execution = new Execution();

        InstagramTopic instagramtopic = instagramTopicService.findOne(topicId);

        execution.setStatus(ExecutionStatus.Initialized);
        execution.setType(ExecutionType.InstagramExtractor);
        instagramtopic.getExecutions().add(execution);
        execution.setInstagramTopic(instagramtopic);
        instagramtopic = instagramTopicService.save(instagramtopic);

        execution = this.save(execution);

        log.info("Starting execution <" + execution.getId() + ">... now!");
        ExtractorExecutor executor = (ExtractorExecutor) context.getBean("instagramExtractionExecutor");
        executor.setExecution(execution);
        taskExecutor.execute(executor);

        log.info("Executor launched.");

        return execution.getId();
    }
    
    
    @Override
    @Transactional
    public Integer executeStreaming(Integer streamingTopicId) throws InvalidStreamingTopicException, InvalidExecutionException {
        log.info("Executing the extractor for topic <" + streamingTopicId + ">.");

        

        StreamingTopic streamingTopic = streamingTopicService.findOne(streamingTopicId);
        Execution execution = new Execution();
        execution.setStatus(ExecutionStatus.Initialized);
        execution.setType(ExecutionType.Streamer);
        streamingTopic.getExecutions().add(execution);
        execution.setStreamingTopic(streamingTopic);
        streamingTopic = streamingTopicService.save(streamingTopic);

        execution = this.save(execution);

        log.info("Starting execution <" + execution.getId() + ">... now!");
        TwitterStreamingExecutor executor = (TwitterStreamingExecutor) context.getBean("twitterStreamingExecutor");
        executor.executeStreaming(execution);

        log.info("Executor launched.");

        return execution.getId();
    }
    
    @Override
    @Transactional
    public Integer stopStreaming(Integer streamingTopicId) throws InvalidStreamingTopicException, InvalidExecutionException {
        log.info("Stopping the extractor for topic <" + streamingTopicId + ">.");

        

        StreamingTopic streamingTopic = streamingTopicService.findOne(streamingTopicId);
        Iterator<Execution> it = streamingTopic.getExecutions().iterator();
        Execution execution = null;
        log.info("Searching executions");
        while(it.hasNext()){
        	Execution auxExecution = it.next();
        	log.info("Checking an execution with state " + auxExecution.getStatus());
        	if(auxExecution.getStatus() == ExecutionStatus.Running){
        		execution = auxExecution;
        		break;
        	}
        }
       
        if(execution != null){
        	log.info("Execution found");
        streamingTopic.getExecutions().remove(execution);
        execution.setStatus(ExecutionStatus.Stopped);
        streamingTopic.getExecutions().add(execution);
        streamingTopic.setActive(false);
        streamingTopic = streamingTopicService.save(streamingTopic);

        execution = this.save(execution);

        log.info("Stoping execution <" + execution.getId() + ">... now!");
        TwitterStreamingExecutor executor = (TwitterStreamingExecutor) context.getBean("twitterStreamingExecutor");
        executor.stopStreaming(execution);

        log.info("Executor launched.");
        return execution.getId();
        }
        else{
        	streamingTopic.setActive(false);
        	streamingTopic = streamingTopicService.save(streamingTopic);
        	return -1;
        }
    }
    
    @Override
    public void useNextAPIKey(){
    	log.info("Changing API key");
        TwitterStreamingExecutor executor = (TwitterStreamingExecutor) context.getBean("twitterStreamingExecutor");
        executor.useNextAPIKey();
    }

    @Override
    @Transactional
    public Integer executeProcessor(Integer topicId, ExecutionModuleParameters moduleParameters) throws InvalidTopicException,
            InvalidExecutionException, InvalidModuleException {
        
        if (topicId != null) {
            log.info("Executing the processor for topic <" + topicId + ">.");
        } else {
            log.info("Executing the validation of this processor.");
        }
        
        // First, a check is made in order to prevent Injection Attacks
        ExecutionModuleDefinition module = moduleService.getProcessorModule(moduleParameters.getModule());
        if (module == null) {
            log.error("The module <" + moduleParameters.getModule() + "> is invalid.");
            throw new InvalidModuleException();
        }

        // Second, check the operation is valid
        if (moduleParameters.getOperation() == null || !module.getOperations().containsKey(moduleParameters.getOperation())) {
            log.error("The operation <" + moduleParameters.getOperation() + "> for module <" + moduleParameters.getModule()
                    + "> is invalid.");
            throw new InvalidModuleException();
        }
        
        // TODO CHECK Third, get the parameters


        // Fourth, prepare the execution

        Execution execution = new Execution();

        execution.setStatus(ExecutionStatus.Initialized);
        execution.setType(ExecutionType.Processor);
        execution.setModuleParameters(moduleParameters);

        if (topicId != null) {
            Topic topic = topicService.findOne(topicId);
            topic.getExecutions().add(execution);
            execution.setTopic(topic);
            topic = topicService.save(topic);
        }

        execution = this.save(execution);

        log.info("Starting execution <" + execution.getId() + ">... now!");
        ProcessorExecutor executor = (ProcessorExecutor) context.getBean(Introspector.decapitalize(module.getSimpleName()));
        executor.setExecution(execution);
        taskExecutor.execute(executor);

        log.info("Executor launched.");

        return execution.getId();
    }

    @Override
    @Transactional
    public Integer executeProcessor(ExecutionModuleParameters processorParameters) throws InvalidTopicException,
            InvalidExecutionException, InvalidModuleException {
        return this.executeProcessor(null, processorParameters);
    }

    @Override
    @Transactional
    public Integer executePlotter(Integer topicId, ExecutionModuleParameters moduleParameters) throws InvalidTopicException,
            InvalidExecutionException, InvalidModuleException {
        log.info("Executing the processor for topic <" + topicId + ">.");

        // First, a check is made in order to prevent Injection Attacks
        ExecutionModuleDefinition module = moduleService.getPlotterModule(moduleParameters.getModule());
        if (module == null) {
            log.error("The module <" + moduleParameters.getModule() + "> is invalid.");
            throw new InvalidModuleException();
        }

        // Second, check the operation is valid
        if (moduleParameters.getOperation() == null || !module.getOperations().containsKey(moduleParameters.getOperation())) {
            log.error("The operation <" + moduleParameters.getOperation() + "> for module <" + moduleParameters.getModule()
                    + "> is invalid.");
            throw new InvalidModuleException();
        }

        // MFC TODO Add parameters

        // Fourth, prepare the execution
        Execution execution = new Execution();

        Topic topic = topicService.findOne(topicId);

        execution.setStatus(ExecutionStatus.Initialized);
        execution.setType(ExecutionType.Plotter);
        topic.getExecutions().add(execution);
        execution.setTopic(topic);
        execution.setModuleParameters(moduleParameters);
        topic = topicService.save(topic);

        execution = this.save(execution);

        log.info("Starting execution <" + Introspector.decapitalize(module.getSimpleName()) + "|" + execution.getId() + ">... now!");
        PlotterExecutor executor = (PlotterExecutor) context.getBean(Introspector.decapitalize(module.getSimpleName()));
        // executor.setOperation(moduleParameters.getOperation());
        executor.setExecution(execution);
        taskExecutor.execute(executor);

        log.info("Executor launched.");
        return execution.getId();

    }

    @Override
    public Long count() {
        return executionDao.count();
    }

	@Override
	public void sendMail(String subject, String message, String to) {
		log.info("Changing API key");
        TwitterStreamingExecutor executor = (TwitterStreamingExecutor) context.getBean("twitterStreamingExecutor");
        executor.sendMail(subject,message,to);
	}

	@Override
	public List<Execution> getExecutionsPerInstagramTopic(Integer instagramTopicId) {
		  List<Execution> executions = executionDao.findByInstagramTopic_id(instagramTopicId);
	        return executions;
	}
}
