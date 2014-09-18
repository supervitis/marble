package org.marble.commons.service;

import java.util.List;

import org.marble.commons.dao.model.Execution;
import org.marble.commons.exception.InvalidExecutionException;
import org.marble.commons.exception.InvalidModuleException;
import org.marble.commons.exception.InvalidPlotParametersException;
import org.marble.commons.exception.InvalidTopicException;
import org.marble.commons.model.ExecutionCreationParameters;

public interface ExecutionService {

    public Execution save(Execution execution) throws InvalidExecutionException;

    public Execution findOne(Integer id) throws InvalidExecutionException;

    public void appendToLog(Integer id, String log) throws InvalidExecutionException;

    public List<Execution> getExecutionsPerTopic(Integer topicId);

    public Integer executeExtractor(Integer topicId) throws InvalidTopicException, InvalidExecutionException;

    public Integer executeProcessor(Integer topicId) throws InvalidTopicException, InvalidExecutionException;

    public Integer executePlotter(Integer topicId, ExecutionCreationParameters plotParameters) throws InvalidTopicException,
            InvalidExecutionException, InvalidPlotParametersException, InvalidModuleException;

    Long count();
}
