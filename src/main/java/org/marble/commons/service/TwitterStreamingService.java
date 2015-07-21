package org.marble.commons.service;

import org.marble.commons.dao.model.TwitterApiKey;

import twitter4j.FilterQuery;
import twitter4j.StatusListener;
import twitter4j.TwitterStream;

public interface TwitterStreamingService {

	void configure(TwitterApiKey apiKey);

	void unconfigure();

	TwitterStream addListener(StatusListener listener);

	TwitterStream removeListener(StatusListener listener);


}
