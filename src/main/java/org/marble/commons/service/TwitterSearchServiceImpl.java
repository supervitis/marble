package org.marble.commons.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.marble.commons.dao.model.TwitterApiKey;

import twitter4j.GeoLocation;
import twitter4j.Query;
import twitter4j.Query.Unit;
import twitter4j.QueryResult;
import twitter4j.StallWarning;
import twitter4j.Status;
import twitter4j.StatusDeletionNotice;
import twitter4j.StatusListener;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.TwitterStream;
import twitter4j.TwitterStreamFactory;
import twitter4j.conf.Configuration;
import twitter4j.conf.ConfigurationBuilder;

@Service
public class TwitterSearchServiceImpl implements TwitterSearchService {

    final public long DEFAULT_MAX_ID = 0;
    final public int DEFAULT_COUNT = 100;

    private TwitterApiKey apiKey;
    private Integer statusesPerCall = DEFAULT_COUNT;
    private Configuration configuration;

    Twitter twitter;
    TwitterStream twitterStream;

    public TwitterSearchServiceImpl() {

    }

    public TwitterSearchServiceImpl(TwitterApiKey apiKey) {
        this.configure(apiKey);
    }

    @Override
    public Integer getStatusesPerCall() {
        return statusesPerCall;
    }

    @Override
    public void setStatusesPerCall(Integer statusesPerCall) {
        this.statusesPerCall = statusesPerCall;
    }

    @Override
    public void configure(TwitterApiKey apiKey) {

        this.apiKey = apiKey;

        ConfigurationBuilder configurationBuilder = new ConfigurationBuilder();
        configurationBuilder.setJSONStoreEnabled(true);

        configurationBuilder.setOAuthConsumerKey(this.apiKey.getConsumerKey());
        configurationBuilder.setOAuthConsumerSecret(this.apiKey.getConsumerSecret());
        configurationBuilder.setOAuthAccessToken(this.apiKey.getAccessToken());
        configurationBuilder.setOAuthAccessTokenSecret(this.apiKey.getAccessTokenSecret());

        this.configuration = configurationBuilder.build();

        TwitterFactory factory = new TwitterFactory(configuration);
        this.twitter = factory.getInstance();
        


    }

    @Override
    public void unconfigure() {
        this.apiKey = null;
        this.configuration = null;
        this.twitter = null;
    }

    @Override
    public List<Status> search(String keyword) throws TwitterException {
        return this.search(keyword, this.DEFAULT_MAX_ID);
    }

    @Override
    public List<Status> search(String keyword, long maxId) throws TwitterException {
        // TODO Multiple keywords

        QueryResult result = null;

        Query query = new Query(keyword);
        if (maxId != this.DEFAULT_MAX_ID) {
            query.setMaxId(maxId - 1);
        }
        query.setCount(this.statusesPerCall);
        result = twitter.search(query);
        return result.getTweets();
    }
    
    @Override
    public List<Status> search(String keyword, long maxId, String since, String until, GeoLocation geolocation, Double radius,Unit unit) throws TwitterException {
        // TODO Multiple keywords

        QueryResult result = null;

        Query query = new Query(keyword);
        if (maxId != this.DEFAULT_MAX_ID) {
            query.setMaxId(maxId - 1);
        }
        query.setCount(this.statusesPerCall);

        if(since != null)
        	query.setSince(since);
        if(until != null)
        	query.setUntil(until);
        if(geolocation != null && radius != null && unit != null)
        	query.setGeoCode(geolocation,radius.doubleValue(),unit);

        result = twitter.search(query);
        return result.getTweets();
    }

}
