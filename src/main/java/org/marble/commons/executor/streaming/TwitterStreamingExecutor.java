package org.marble.commons.executor.streaming;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import org.marble.commons.dao.model.Execution;
import org.marble.commons.dao.model.OriginalStatus;
import org.marble.commons.dao.model.StreamingTopic;
import org.marble.commons.dao.model.Topic;
import org.marble.commons.dao.model.TwitterApiKey;
import org.marble.commons.exception.InvalidExecutionException;
import org.marble.commons.executor.extractor.ExtractorExecutor;
import org.marble.commons.model.ExecutionStatus;
import org.marble.commons.service.DatasetService;
import org.marble.commons.service.DatastoreService;
import org.marble.commons.service.ExecutionService;
import org.marble.commons.service.StreamingTopicService;
import org.marble.commons.service.TwitterApiKeyService;
import org.marble.commons.service.TwitterSearchService;
import org.marble.commons.service.TwitterStreamingService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import twitter4j.FilterQuery;
import twitter4j.GeoLocation;
import twitter4j.JSONObject;
import twitter4j.Status;
import twitter4j.StreamController;
import twitter4j.TwitterException;
import twitter4j.TwitterStream;
import twitter4j.TwitterStreamFactory;
import twitter4j.Query.Unit;

import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;
import com.mongodb.util.JSON;

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
	            
	            //TODO: IMPLEMENTAR LOS LISTENERS Y DEMAS
	            
	            msg = "Extraction will begin with Api Key <" + apiKeys.get(apiKeysIndex).getDescription() + ">";
	            log.info(msg);
	            execution.appendLog(msg);
	            executionService.save(execution);
	            
	            FilterQuery query = new FilterQuery();
	            TwitterStreamingListener listener = new TwitterStreamingListener();
	            twitterStream.shutdown();
	            twitterStream.addListener(listener);
	            listeners.add(listener);
	            String[] languages = {streamingTopic.getLanguage()};
	    		query = query.track(getKeywords()).language(languages);
	            twitterStream.filter(query);
	            /*
	            long lastId = 0;
	            if (streamingTopic.getUpperLimit() != null) {
	                lastId = streamingTopic.getUpperLimit();
	            }

	            long maxStatuses = 200;
	            if (streamingTopic.getStatusesPerFullExtraction() != null) {
	                maxStatuses = streamingTopic.getStatusesPerFullExtraction();
	            }
	            
	            String sinceDate = null;
	            if(streamingTopic.getSinceDate() != null)
	            	sinceDate = dateOnlyFormat.format(streamingTopic.getSinceDate());
	            
	            String untilDate = null;
	            if(streamingTopic.getUntilDate() != null)
	            	untilDate = dateOnlyFormat.format(streamingTopic.getUntilDate());
	            
	            Double longitude = streamingTopic.getGeoLongitude();
	            Double latitude = streamingTopic.getGeoLatitude();
	            Double radius = streamingTopic.getGeoRadius();
	            Unit unit = streamingTopic.getGeoUnit();
	            
	            GeoLocation geoLoc = null;
	            if (longitude != null && latitude != null){
	            	geoLoc = new GeoLocation(latitude.doubleValue(), longitude.doubleValue());
	            }
	            

	            int count = 0;
	            do {
	                List<Status> statusList;

	                try {
	                    statusList = twitterStreamingService.search(keyword, lastId, sinceDate, untilDate, geoLoc, radius, unit );
	                } catch (TwitterException e) {
	                    // TODO Auto-generated catch block

	                    apiKeysIndex++;
	                    if (apiKeysIndex >= apiKeysCount) {
	                        msg = "API Rate exceeded for all keys. Waiting a minute.";
	                        log.warn(msg, e);
	                        execution.appendLog(msg);
	                        executionService.save(execution);

	                        apiKeysIndex = 0;
	                        try {
	                            Thread.sleep(60000);
	                        } catch (InterruptedException e1) {
	                            // TODO Auto-generated catch block
	                            log.error("Error while sleeping.", e);
	                        }

	                    } else {

	                    }
	                    msg = "API Rate exceeded. Changing to API Key <" + apiKeys.get(apiKeysIndex).getDescription()
	                            + ">.";
	                    log.warn(msg, e);
	                    execution.appendLog(msg);
	                    executionService.save(execution);

	                    // Changing to another API Key
	                    twitterStreamingService.configure((apiKeys.get(apiKeysIndex)));
	                    continue;
	                }
	                if (statusList != null && statusList.size() > 0) {

	                    for (Status status : statusList) {
	                        lastId = status.getId();
	                        log.info("UpperLimit: " + lastId + ", count: " + count + ", maxStatuses: " + maxStatuses);
	                        streamingTopic.setUpperLimit(lastId);
	                        // save
	                        OriginalStatus originalStatus = new OriginalStatus(status, streamingTopic.getId());
	                        if(streamingTopic.getLowerLimit() != null && streamingTopic.getLowerLimit() >= originalStatus.getId()) {
	                            inRange = false;
	                            msg = "Reached the lower limit for this topic.";
	                            log.info(msg);
	                            execution.appendLog(msg);
	                            executionService.save(execution);
	                            break;
	                        }

	                        datastoreService.insertOriginalStatus(originalStatus);
	                        
	                        //Con esto llega? 
	                        count++;
	                        if (count >= maxStatuses) {
	                            break;
	                        }

	                    }

	                }
	                else {
	                    // No statuses extracted, it might be out of availability.
	                    msg = "No statuses available for extraction at this point.";
	                    log.info(msg);
	                    execution.appendLog(msg);
	                    executionService.save(execution);
	                    break;
	                }
	                
	                //Supongo que no pero este puede fallar 
	                streamingTopicService.save(streamingTopic);

	                msg = "Statuses extracted so far: <" + count + ">";
	                log.info(msg);
	                execution.appendLog(msg);
	                executionService.save(execution);

	            } while (count < maxStatuses && inRange);

	            msg = "Extraction of this streaming topic has finished.";
	            log.info(msg);
	            execution.appendLog(msg);
	            execution.setStatus(ExecutionStatus.Stopped);
	            execution = executionService.save(execution);
	         */
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
		String [] keywords = {};
		/*for(TwitterStreamingListener listener : listeners){
			//Get keywords
		}*/
		return keywords;
		
	}
}
