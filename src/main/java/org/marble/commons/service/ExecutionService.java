package org.marble.commons.service;

import org.marble.commons.dao.model.Execution;
import org.marble.commons.exception.InvalidExecutionException;

public interface ExecutionService {

	public Execution updateExecution(Execution execution) throws InvalidExecutionException;

	public Execution getExecution(Integer id) throws InvalidExecutionException;
	
	public void appendToLog(Integer id, String log) throws InvalidExecutionException;

	//List<Topic> getTopics();

	//public void deleteTopic(Integer id);

	//public Topic createTopic(Topic topic) throws InvalidTopicException;

}
