package org.marble.commons.service;

import java.util.List;

import org.marble.commons.dao.model.StreamingStatus;
import org.marble.commons.dao.model.StreamingTopic;
import org.marble.commons.exception.InvalidStreamingTopicException;
import org.marble.commons.model.StreamingTopicInfo;

import com.mongodb.DBCursor;

public interface StreamingTopicService {

	public StreamingTopic save(StreamingTopic streamingTopic) throws InvalidStreamingTopicException;

	public StreamingTopic findOne(Integer id) throws InvalidStreamingTopicException;

	List<StreamingTopic> findAll();

	public void delete(Integer id);

    Long count();

    StreamingTopicInfo info(Integer id) throws InvalidStreamingTopicException;

	DBCursor findCursorByStreamingTopicId(Integer streamingTopicId);

}
