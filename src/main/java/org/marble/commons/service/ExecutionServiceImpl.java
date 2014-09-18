package org.marble.commons.service;

import java.beans.Introspector;
import java.util.List;

import javax.transaction.Transactional;

import org.marble.commons.dao.ExecutionDao;
import org.marble.commons.dao.model.Execution;
import org.marble.commons.dao.model.Topic;
import org.marble.commons.exception.InvalidExecutionException;
import org.marble.commons.exception.InvalidModuleException;
import org.marble.commons.exception.InvalidPlotParametersException;
import org.marble.commons.exception.InvalidTopicException;
import org.marble.commons.executor.extractor.ExtractorExecutor;
import org.marble.commons.executor.plotter.PlotterExecutor;
import org.marble.commons.executor.processor.ProcessorExecutor;
import org.marble.commons.model.ExecutionStatus;
import org.marble.commons.model.ExecutionType;
import org.marble.commons.model.ExecutionCreationParameters;
import org.marble.commons.model.PlotModule;

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
    public Integer executeProcessor(Integer topicId) throws InvalidTopicException, InvalidExecutionException {
        log.info("Executing the processor for topic <" + topicId + ">.");

        Execution execution = new Execution();

        Topic topic = topicService.findOne(topicId);

        execution.setStatus(ExecutionStatus.Initialized);
        execution.setType(ExecutionType.Processor);
        topic.getExecutions().add(execution);
        execution.setTopic(topic);
        topic = topicService.save(topic);

        execution = this.save(execution);

        log.info("Starting execution <" + execution.getId() + ">... now!");
        ProcessorExecutor executor = (ProcessorExecutor) context.getBean("bagOfWordsSenticProcessorExecutor");
        executor.setExecution(execution);
        taskExecutor.execute(executor);

        log.info("Executor launched.");

        return execution.getId();
    }

    @Override
    @Transactional
    public Integer executePlotter(Integer topicId, ExecutionCreationParameters plotParameters) throws InvalidTopicException,
            InvalidExecutionException, InvalidPlotParametersException, InvalidModuleException {
        log.info("Executing the processor for topic <" + topicId + ">.");

        // First, a check is made in order to prevent Injection Attacks
        PlotModule module = moduleService.getPlotterModule(plotParameters.getModule());
        if (module == null) {
            log.error("The module <" + plotParameters.getModule() + "> is invalid.");
            throw new InvalidModuleException();
        }

        // Second, check the operation is valid
        if (plotParameters.getOperation() == null || !module.getOperations().containsKey(plotParameters.getOperation())) {
            log.error("The operation <" + plotParameters.getOperation() + "> for module <" + plotParameters.getModule()
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
        topic = topicService.save(topic);

        execution = this.save(execution);

        log.info("Starting execution <" + Introspector.decapitalize(module.getSimpleName())+"|"+ execution.getId() + ">... now!");
        PlotterExecutor executor = (PlotterExecutor) context.getBean(Introspector.decapitalize(module.getSimpleName()));
        executor.setOperation(plotParameters.getOperation());
        executor.setExecution(execution);
        taskExecutor.execute(executor);

        log.info("Executor launched.");
        return execution.getId();

    }

    @Override
    public Long count() {
        return executionDao.count();
    }
}
