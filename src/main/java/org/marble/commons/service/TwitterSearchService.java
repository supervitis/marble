package org.marble.commons.service;

import java.util.List;

import org.marble.commons.dao.model.TwitterApiKey;

import twitter4j.GeoLocation;
import twitter4j.Query.Unit;
import twitter4j.Status;
import twitter4j.TwitterException;

public interface TwitterSearchService {

    final public long DEFAULT_MAX_ID = 0;
    final public int DEFAULT_COUNT = 100;

    public Integer getStatusesPerCall();

    public void setStatusesPerCall(Integer statusesPerCall);

    public void configure(TwitterApiKey apiKey);

    public void unconfigure();

    public List<Status> search(String keyword) throws TwitterException;

    public List<Status> search(String keyword, long maxId) throws TwitterException;
    
    public List<Status> search(String keyword, long maxId, String since, String until, GeoLocation geolocation, Double radius, Unit unit) throws TwitterException;

	

}
