package org.marble.commons.executor.streaming;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import org.marble.commons.dao.model.Execution;
import org.marble.commons.dao.model.StreamingTopic;
import org.marble.commons.dao.model.TwitterApiKey;
import org.marble.commons.exception.InvalidExecutionException;
import org.marble.commons.executor.extractor.ExtractorExecutor;
import org.marble.commons.model.ExecutionStatus;
import org.marble.commons.service.DatasetService;
import org.marble.commons.service.DatastoreService;
import org.marble.commons.service.ExecutionService;
import org.marble.commons.service.StreamingTopicService;
import org.marble.commons.service.TwitterApiKeyService;
import org.marble.commons.service.TwitterStreamingService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import twitter4j.FilterQuery;
import twitter4j.StreamController;
import twitter4j.TwitterStream;
import twitter4j.TwitterStreamFactory;
import twitter4j.conf.Configuration;
import twitter4j.conf.ConfigurationBuilder;

@Component
@Scope("singleton")
public class TwitterStreamingExecutor implements StreamingExecutor{

    private static final Logger log = LoggerFactory.getLogger(TwitterStreamingExecutor.class);

    
    @Autowired
    ExecutionService executionService;

    @Autowired
    StreamingTopicService streamingTopicService;
    
    @Autowired
    DatasetService datasetService;

    @Autowired
    TwitterApiKeyService twitterApiKeyService;

    @Autowired
    DatastoreService datastoreService;
    
    Execution execution;
    
    @Autowired
    TwitterStreamingService twitterStreamingService;
    
    ArrayList<TwitterStreamingListener> listeners;
    TwitterStream twitterStream = null;
    
    
    
    
    StreamController streamController;
    DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
    DateFormat dateOnlyFormat = new SimpleDateFormat("yyyy-MM-dd");

	public void executeStreaming(Execution execution) {
		
		 String msg = "";
	        try {
	            log.info("Initializing execution...");
	            Thread.sleep(1000);
	        } catch (InterruptedException e) {
	        }

	        try {
	            Integer id = execution.getId();
	            msg = "Starting twitter streaming extraction <" + id + ">.";
	            log.info(msg);
	            execution.appendLog(msg);

	            // Changing execution state
	            execution.setStatus(ExecutionStatus.Running);
	            execution = executionService.save(execution);

	            StreamingTopic streamingTopic = streamingTopicService.findOne(execution.getStreamingTopic().getId());

	            
	            // Get twitter keys
	            List<TwitterApiKey> apiKeys = twitterApiKeyService.getEnabledTwitterApiKeys();
	            for (TwitterApiKey key : apiKeys) {
	                log.info("Key available: " + key);
	            }

	            Integer apiKeysCount = apiKeys.size();
	            if (apiKeysCount == 0) {
	                msg = "There are no Api Keys available. Aborting execution.";
	                log.info(msg);
	                execution.appendLog(msg);
	                execution.setStatus(ExecutionStatus.Aborted);
	                executionService.save(execution);
	                return;
	            }

	            Integer apiKeysIndex = 0;

	            //Solo se crea el streaming si no existe ya
	            if(twitterStream == null){
	     
	        		twitterStream = twitterStreamingService.configure(apiKeys.get(apiKeysIndex));
	    		}
	            
	            if(listeners == null){
	            	listeners = new ArrayList<TwitterStreamingListener>();
	            }
	            
	            msg = "Extraction will begin with Api Key <" + apiKeys.get(apiKeysIndex).getDescription() + ">";
	            log.info(msg);
	            execution.appendLog(msg);
	            executionService.save(execution);
	            
	            FilterQuery query = new FilterQuery();
	            TwitterStreamingListener listener = new TwitterStreamingListener(streamingTopic,execution,datastoreService,executionService);
	            twitterStream.shutdown();
	            twitterStream.addListener(listener);
	            listeners.add(listener);
	            String[] languages = {streamingTopic.getLanguage()};
	            String[] keywords = getKeywords();
	            log.info("Active Streaming topics: " + keywords.length);
	    		query = query.track(keywords).language(languages);
	    		streamingTopic.setActive(true);
	    		streamingTopicService.save(streamingTopic);
	            twitterStream.filter(query);
	            log.info("Thread" + keywords + "finished");
	        } catch (Exception e) {
	            msg = "An error ocurred while manipulating execution <" + execution.getId() + ">. Execution aborted.";
	            log.error(msg, e);
	            execution.appendLog(msg);
	            execution.setStatus(ExecutionStatus.Aborted);
	            try {  
	                execution = executionService.save(execution);
	            } catch (InvalidExecutionException e1) {
	                log.error("Status couldn't be refreshed on the execution object.");
	            }
	            return;
	        }
	}


	public void stopStreaming(Execution execution){

		 String msg = "";
	        try {
	            log.info("Initializing execution...");
	            Thread.sleep(1000);
	        } catch (InterruptedException e) {
	        }

	        try {
	            Integer id = execution.getId();
	            msg = "Stopping twitter streaming extraction <" + id + ">.";
	            log.info(msg);
	            execution.appendLog(msg);

	            // Changing execution state
	            execution.setStatus(ExecutionStatus.Stopped);
	            execution = executionService.save(execution);
	            StreamingTopic streamingTopic = streamingTopicService.findOne(execution.getStreamingTopic().getId());
	           	            
	            FilterQuery query = new FilterQuery();
	            TwitterStreamingListener listener = new TwitterStreamingListener(streamingTopic,execution,datastoreService,executionService);
	            twitterStream.shutdown();
	            twitterStream.removeListener(listener);
	            listeners.remove(listener);
	            if(!listeners.isEmpty()){
		            String[] languages = {streamingTopic.getLanguage()};
		            String[] keywords = getKeywords();
		            log.info("Active Streaming topics: " + keywords.length);
		    		query = query.track(keywords).language(languages);
		    		streamingTopic.setActive(false);
		    		streamingTopicService.save(streamingTopic);
		            twitterStream.filter(query);
	            }
	            log.info("Thread finished");
	        } catch (Exception e) {
	            msg = "An error ocurred while manipulating execution <" + execution.getId() + ">. Execution aborted.";
	            log.error(msg, e);
	            execution.appendLog(msg);
	            execution.setStatus(ExecutionStatus.Aborted);
	            try {  
	                execution = executionService.save(execution);
	            } catch (InvalidExecutionException e1) {
	                log.error("Status couldn't be refreshed on the execution object.");
	            }
	            return;
	        }
	}
	public void setExecution(Execution execution) {
        this.execution = execution;		
	}
	
	public String[] getKeywords(){
		ArrayList<String> keywords = new ArrayList<String>();
		for(TwitterStreamingListener listener : listeners){
			keywords.add(listener.getKeywords());
			
		}
		String[] result = {};
		return keywords.toArray(result);
		
	}
}
