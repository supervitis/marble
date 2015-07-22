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
public class TwitterStreamingExecutor implements ExtractorExecutor {

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
    
	@Override
	public void run() {
		
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
	            String keyword = streamingTopic.getKeywords();

	            //Solo se crea el streaming si no existe ya
	            if(twitterStream == null){
	     
	        		twitterStreamingService.configure(apiKeys.get(apiKeysIndex));
	    		}
	            
	            if(listeners == null){
	            	listeners = new ArrayList<TwitterStreamingListener>();
	            }
	            
	            //TODO: IMPLEMENTAR LOS LISTENERS Y DEMAS
	            
	            msg = "Extraction will begin with Api Key <" + apiKeys.get(apiKeysIndex).getDescription() + ">";
	            log.info(msg);
	            execution.appendLog(msg);
	            executionService.save(execution);
	            
	            FilterQuery query = new FilterQuery();
	            TwitterStreamingListener listener = new TwitterStreamingListener(streamingTopic,execution);
	            twitterStream.shutdown();
	            twitterStream.addListener(listener);
	            listeners.add(listener);
	            String[] languages = {streamingTopic.getLanguage()};
	    		query = query.track(getKeywords()).language(languages);
	            twitterStream.filter(query);
	            
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

	@Override
	public void setExecution(Execution execution) {
        this.execution = execution;		
	}
	
	public String[] getKeywords(){
		ArrayList<String> keywords = new ArrayList<String>();
		for(TwitterStreamingListener listener : listeners){
			keywords.add(listener.getKeyword());
			
		}
		String[] result = {};
		return keywords.toArray(result);
		
	}
}
