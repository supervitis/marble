package org.marble.service.extractors;

import java.util.List;

import org.marble.model.TwitterApiKey;

import twitter4j.Query;
import twitter4j.QueryResult;
import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.conf.Configuration;
import twitter4j.conf.ConfigurationBuilder;

public class TwitterGateway {

    final public long     DEFAULT_MAX_ID = 0;
    final public int      DEFAULT_COUNT  = 100;

    private TwitterApiKey apiKey;
    private Integer statusesPerCall = DEFAULT_COUNT;
    private Configuration configuration;

    Twitter               twitter;

    public TwitterGateway() {

    }

    public TwitterGateway(TwitterApiKey apiKey) {
        this.configure(apiKey);
    }

    public Integer getStatusesPerCall() {
        return statusesPerCall;
    }

    public void setStatusesPerCall(Integer statusesPerCall) {
        this.statusesPerCall = statusesPerCall;
    }

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

    public void unconfigure() {
        this.apiKey = null;
        this.configuration = null;
        this.twitter = null;
    }

    public List<Status> searchTweets(String keyword) throws TwitterException {
        return searchTweets(keyword, this.DEFAULT_MAX_ID);
    }

    public List<Status> searchTweets(String keyword, long maxId) throws TwitterException {
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

}
