package org.marble.commons.service;

import java.util.List;

import org.marble.commons.dao.model.Execution;
import org.marble.commons.exception.InvalidExecutionException;
import org.marble.commons.exception.InvalidInstagramTopicException;
import org.marble.commons.exception.InvalidModuleException;
import org.marble.commons.exception.InvalidStreamingTopicException;
import org.marble.commons.exception.InvalidTopicException;
import org.marble.commons.model.ExecutionModuleParameters;

public interface ExecutionService {

    public Execution save(Execution execution) throws InvalidExecutionException;

    public Execution findOne(Integer id) throws InvalidExecutionException;

    public void appendToLog(Integer id, String log) throws InvalidExecutionException;

    public List<Execution> getExecutionsPerTopic(Integer topicId);

    public Integer executeExtractor(Integer topicId) throws InvalidTopicException, InvalidExecutionException;

    public Integer executeProcessor(Integer topicId, ExecutionModuleParameters plotParameters)
            throws InvalidTopicException, InvalidExecutionException, InvalidModuleException;

    public Integer executePlotter(Integer topicId, ExecutionModuleParameters plotParameters)
            throws InvalidTopicException,
            InvalidExecutionException, InvalidModuleException;

    Long count();

    Integer executeProcessor(ExecutionModuleParameters plotParameters) throws InvalidTopicException,
            InvalidExecutionException, InvalidModuleException;
    
	Integer executeInstagramExtractor(Integer topicId)
			throws InvalidExecutionException, InvalidInstagramTopicException;

	Integer executeStreaming(Integer streamingTopicId)
			throws InvalidTopicException, InvalidExecutionException, InvalidStreamingTopicException;

	Integer stopStreaming(Integer streamingTopicId)
			throws InvalidStreamingTopicException, InvalidExecutionException;

	void useNextAPIKey();

	void sendMail(String string, String message, String string2);

	public List<Execution> getExecutionsPerInstagramTopic(Integer instagramTopicId);



}
