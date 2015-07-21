package org.marble.commons.service;

import org.marble.commons.dao.model.TwitterApiKey;

import twitter4j.FilterQuery;
import twitter4j.StatusListener;
import twitter4j.TwitterFactory;
import twitter4j.TwitterStream;
import twitter4j.TwitterStreamFactory;
import twitter4j.conf.Configuration;
import twitter4j.conf.ConfigurationBuilder;

public class TwitterStreamingServiceImpl implements TwitterStreamingService {
   
    private TwitterApiKey apiKey;
    private Configuration configuration;
    
    TwitterStream twitterStream;

    public TwitterStreamingServiceImpl(){
    	
    }
    
    public TwitterStreamingServiceImpl(TwitterApiKey apiKey) {
        this.configure(apiKey);
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
        this.twitterStream = new TwitterStreamFactory(configuration).getInstance();
    }
    
    @Override
    public void unconfigure() {
        this.apiKey = null;
        this.configuration = null;
        this.twitterStream = null;
    }
    
    @Override
    public TwitterStream addListener(StatusListener listener){
    	twitterStream.shutdown();
    	twitterStream.addListener(listener);
    	//TODO: Cambiar por filter con las palabras de los topics
    	twitterStream.sample();
    	return this.twitterStream;
    }
    
    @Override
    public TwitterStream removeListener(StatusListener listener){
    	twitterStream.shutdown();
    	twitterStream.removeListener(listener);
    	//TODO: Cambiar por filter con las palabras de los topics
    	twitterStream.sample();
    	return this.twitterStream;
    }
}
