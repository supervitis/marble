package org.marble.commons.executor.extractor;

import java.io.IOException;
import java.net.MalformedURLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import org.marble.commons.dao.model.Execution;
import org.marble.commons.dao.model.InstagramStatus;
import org.marble.commons.dao.model.InstagramToken;
import org.marble.commons.dao.model.OriginalStatus;
import org.marble.commons.dao.model.InstagramTopic;
import org.marble.commons.dao.model.TwitterApiKey;
import org.marble.commons.exception.EmptyInstagramResponseException;
import org.marble.commons.exception.InvalidExecutionException;
import org.marble.commons.exception.InvalidInstagramTopicException;
import org.marble.commons.model.ExecutionStatus;
import org.marble.commons.service.DatastoreService;
import org.marble.commons.service.ExecutionService;
import org.marble.commons.service.InstagramSearchService;
import org.marble.commons.service.InstagramTokenService;
import org.marble.commons.service.InstagramTopicService;
import org.marble.commons.service.TwitterApiKeyService;
import org.marble.commons.service.TwitterSearchService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import twitter4j.GeoLocation;
import twitter4j.Query.Unit;
import twitter4j.JSONException;
import twitter4j.Status;
import twitter4j.TwitterException;

@Component
@Scope("prototype")
public class InstagramExtractionExecutor implements ExtractorExecutor {

    private static final Logger log = LoggerFactory.getLogger(InstagramExtractionExecutor.class);

    @Autowired
    ExecutionService executionService;

    @Autowired
    InstagramTopicService instagramTopicService;
    
    @Autowired
    InstagramTokenService instagramTokenService;

    @Autowired
    DatastoreService datastoreService;

    DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
    DateFormat dateOnlyFormat = new SimpleDateFormat("yyyy-MM-dd");

    Execution execution;

    @Autowired
    InstagramSearchService instagramSearchService;

    @Override
    public void setExecution(Execution execution) {
        this.execution = execution;
    }

    @Override
    public void run() {
        String msg = "";
        try {
            log.info("Initializing execution...");
            Thread.sleep(1000);
        } catch (InterruptedException e) {
        }

        try {

            Boolean inRange = true;

            Integer id = execution.getId();

            msg = "Starting twitter extraction <" + id + ">.";
            log.info(msg);
            execution.appendLog(msg);

            // Changing execution state
            execution.setStatus(ExecutionStatus.Running);
            execution = executionService.save(execution);

            // Get the associated instagramTopic
            InstagramTopic instagramTopic = instagramTopicService.findOne(execution.getInstagramTopic().getId());
            
            //Here we will have a list with all available tokens
            List<InstagramToken> instagramAccessTokens = instagramTokenService.getInstagramTokens();
    		
            for (InstagramToken token : instagramAccessTokens) {
                log.info("Key available: " + token.getAccessToken());
            }

            
            Integer tokenCount = instagramAccessTokens.size();
            if (tokenCount == 0) {
                msg = "There are no tokens available. Aborting execution.";
                log.info(msg);
                execution.appendLog(msg);
                execution.setStatus(ExecutionStatus.Aborted);
                executionService.save(execution);
                instagramTopic = instagramTopicService.findOne(execution.getInstagramTopic().getId());
                instagramTopicService.save(instagramTopic);
                return;
            }

            Integer tokenIndex = 0;
            String keyword = instagramTopic.getKeywords();

            instagramSearchService.configure(instagramAccessTokens.get(tokenIndex).getAccessToken());

            msg = "Extraction will begin with Api Key <" + instagramAccessTokens.get(tokenIndex) + ">";
            log.info(msg);
            execution.appendLog(msg);
            executionService.save(execution);

            long maxStatuses = 0;
            if (instagramTopic.getStatusesPerFullExtraction() != null) {
                maxStatuses = instagramTopic.getStatusesPerFullExtraction();
            }
            
            Long untilDate = null;
            if(instagramTopic.getUntilDate() != null)
            	untilDate = instagramTopic.getUntilDate().getTime()/1000;
            
            Double longitude = instagramTopic.getGeoLongitude();
            Double latitude = instagramTopic.getGeoLatitude();
            Double radius = instagramTopic.getGeoRadius();
            Unit unit = instagramTopic.getGeoUnit();
            
            Long maxDate = untilDate;
            
            int count = 0;
            do {
                List<InstagramStatus> statusList = new ArrayList<InstagramStatus>();
                try{
                statusList = instagramSearchService.search(instagramTopic.getId(),latitude,longitude, radius,unit, maxDate);
                }catch(EmptyInstagramResponseException e){
                	maxDate = maxDate - 60;
                    instagramTopic.setUpperLimit(maxDate);
                    instagramTopic = instagramTopicService.save(instagramTopic);
                    log.error("The query returned no statuses",e);
                }catch(JSONException e){
                	log.error("Error in the returned JSON",e);
                }catch(MalformedURLException e){
                	log.error("The URL was not correct",e);
                }catch(IOException e){
                	log.error("There was an IO error",e);
                	String errorMsg = e.getLocalizedMessage();
                	if(errorMsg.contains("response code: 420")){
                    tokenIndex++;
                    if (tokenIndex >= tokenCount) {
                        msg = "API Rate exceeded for all keys. Waiting a minute.";
                        log.warn(msg, e);
                        execution.appendLog(msg);
                        executionService.save(execution);

                        tokenIndex = 0;
                        try {
                            Thread.sleep(60000);
                        } catch (InterruptedException e1) {
                            log.error("Error while sleeping.", e);
                        }
                    }
                    msg = "API Rate exceeded. Changing to API Key <" + instagramAccessTokens.get(tokenIndex) + ">.";
                    log.warn(msg, e);
                    execution.appendLog(msg);
                    executionService.save(execution);

                    // Changing to another API Key
                    instagramSearchService.configure((instagramAccessTokens.get(tokenIndex).getAccessToken()));
                    continue;
                	}
                }

                if (statusList != null && statusList.size() > 0) {
                    System.out.println("LIST OF STATUS SIZE = " + statusList.size());
                    for (InstagramStatus status : statusList) {
                        maxDate = status.getCreatedTime();
                        log.info("UpperLimit: " + maxDate + ", count: " + count + ", maxStatuses: " + maxStatuses + ", sinceDate: " + instagramTopic.getSinceDate().getTime()/1000 );
                        instagramTopic.setUpperLimit(maxDate);
                        instagramTopic = instagramTopicService.save(instagramTopic);                       
                        if(instagramTopic.getSinceDate() != null && (instagramTopic.getSinceDate().getTime()/1000) >= maxDate) {
                            
                        	inRange = false;
                            msg = "Reached the minimum date for this Instagram Topic.";
                            log.info(msg);
                            execution.appendLog(msg);
                            executionService.save(execution);
                            break;
                        }

                        datastoreService.insertInstagramStatus(status);
                        count++;
                        if (count >= maxStatuses) {
                            break;
                        }

                    }

                }
                
                
                instagramTopicService.save(instagramTopic);

                msg = "Statuses extracted so far: <" + count + ">";
                log.info(msg);
                execution.appendLog(msg);
                executionService.save(execution);
            } while (count < maxStatuses && inRange);

            msg = "Extraction of this instagramTopic has finished.";
            log.info(msg);
            execution.appendLog(msg);
            execution.setStatus(ExecutionStatus.Stopped);
            execution = executionService.save(execution);
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
}
