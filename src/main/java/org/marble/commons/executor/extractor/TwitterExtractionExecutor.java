package org.marble.commons.executor.extractor;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.marble.commons.dao.model.Execution;
import org.marble.commons.dao.model.Statuses;
import org.marble.commons.dao.model.SurveyInfo;
import org.marble.commons.dao.model.Topic;
import org.marble.commons.dao.model.TwitterApiKey;
import org.marble.commons.exception.InvalidExecutionException;
import org.marble.commons.exception.InvalidTopicException;
import org.marble.commons.model.ExecutionStatus;
import org.marble.commons.service.ExecutionService;
import org.marble.commons.service.ResetServiceImpl;
import org.marble.commons.service.TopicService;
import org.marble.commons.service.TwitterApiKeyService;
import org.marble.commons.service.TwitterSearchService;
import org.marble.commons.service.TwitterService;
import org.marble.commons.service.TwitterServiceImpl;
import org.marble.commons.util.MarbleUtil;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import twitter4j.Status;
import twitter4j.TwitterException;

@Component
@Scope("prototype")
public class TwitterExtractionExecutor implements ExtractorExecutor {

    private static final Logger log = LoggerFactory.getLogger(TwitterExtractionExecutor.class);

    @Autowired
    ExecutionService executionService;
    
    @Autowired
    TopicService topicService;
    
    @Autowired
    TwitterApiKeyService twitterApiKeyService;

    DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");

    Execution execution;
    
    @Autowired
    TwitterSearchService twitterSearchService;

    Integer id;
    
    @Override
    public String getName() {
        // TODO Auto-generated method stub
        return null;
    }

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
            
            SurveyInfo surveyInfo = execution.getSurveyInfo();
            surveyInfo.addQuestionAndAnswer("happy", "Not really!");
            execution.setSurveyInfo(surveyInfo);
            execution = executionService.updateExecution(execution);
            
            Integer id = execution.getId();

            msg = "Starting twitter extraction <"+id+">.";
            log.info(msg);
            execution.appendLog(msg);
            
            // Changing execution state
            execution.setStatus(ExecutionStatus.Running);
            execution = executionService.updateExecution(execution);
            
            
            // MFC START
            // Get the associated topic
            Topic topic = topicService.getTopic(execution.getTopic().getId());
            
            //topic.setDescription("Lo he cambiado!!!!");
            //topicService.updateTopic(topic);
        
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
                executionService.updateExecution(execution);
                return;
            }
            
            Integer apiKeysIndex = 0;
            String keyword = topic.getKeywords();
            twitterSearchService.configure(apiKeys.get(apiKeysIndex));
            
            msg = "Extraction will begin with Api Key <" + apiKeys.get(apiKeysIndex).getDescription() + ">";
            log.info(msg);
            execution.appendLog(msg);
            executionService.updateExecution(execution);
            
            long lastId = 0;
            if (topic.getUpperLimit() != null) {
                lastId = topic.getUpperLimit();
            }
            
            long maxStatuses = 200;
            if (topic.getStatusesPerFullExtraction() != null) {
                maxStatuses = topic.getStatusesPerFullExtraction();
            }
            
            int loop = 1;
            int count = 0;
            do {
                List<Status> statusList;
                
                try {
                    statusList = twitterSearchService.search(keyword, lastId);
                } catch (TwitterException e) {
                    // TODO Auto-generated catch block
                    
                    apiKeysIndex++;
                    if (apiKeysIndex >= apiKeysCount) {
                        msg = "API Rate exceeded for all keys. Waiting a minute.";
                        log.warn(msg, e);
                        execution.appendLog(msg);
                        executionService.updateExecution(execution);
                        
                        apiKeysIndex = 0;
                        try {
                            Thread.sleep(60000);
                        } catch (InterruptedException e1) {
                            // TODO Auto-generated catch block
                            log.error("Error while sleeping.", e);
                        }

                    } else {
                        
                    }
                    msg = "API Rate exceeded. Changing to API Key <" + apiKeys.get(apiKeysIndex).getDescription() + ">.";
                    log.warn(msg, e);
                    execution.appendLog(msg);
                    executionService.updateExecution(execution);
                    
                    // Changing to another API Key
                    twitterSearchService.configure((apiKeys.get(apiKeysIndex)));
                    continue;
                }
                if (statusList != null && statusList.size() > 0) {

                    for (Status status : statusList) {
                        lastId = status.getId();
                        log.info("UpperLimit: " + lastId + ", count: " + count + ", maxStatuses: " + maxStatuses);
                        //topic.setUpperLimit(lastId);
                        count++;
                        if (count > maxStatuses) {
                            break;
                        }

                        
                        /*topic.addStatus(status);
                        Statuses statuses = new Statuses();
                        statuses.addStatus(status);
                        statuses.setOjete("Moreno");
                        topic.setStatuses(statuses);*/
                    }
                    
                }
                //topicService.updateTopic(topic);
                
                msg = "Statuses extracted so far: <" + count + ">";
                log.info(msg);
                execution.appendLog(msg);
                executionService.updateExecution(execution);

                loop++;
            } while (count < maxStatuses);

/*


        String keyword = topic.getKeywords();
        String collection = topic.getName();
        mongoDatastoreOperations.setCollection(collection);

        int loop = 1;
        int count = 0;
        do {
            List<Status> statusList;
            String executionCommand = progressWall.pullExtractorMessage(topic.getName());
            if (executionCommand != null && executionCommand == ExecutionCommands.STOP) {
                topic.getTopicStatus().setExtractorStatus(
                        Utilities.getDatedMessage("Stopped by user."));
                break;
            }
            try {
                statusList = twitterGateway.searchTweets(keyword, lastId);
            } catch (TwitterException e) {
                // TODO Auto-generated catch block
                log.warn("API Rate exceeded", e);
                apiKeysIndex++;
                if (apiKeysIndex >= apiKeysCount) {
                    apiKeysIndex = 0;
                    progressWall.setExtractorUnitProgress(topic.getName(),
                            Utilities.getDatedMessage("API Rate Exceeded. Waiting a minute."));
                    try {
                        Thread.sleep(60000);
                    } catch (InterruptedException e1) {
                        // TODO Auto-generated catch block
                        log.error("Error while sleeping.", e);
                    }
                    progressWall.setExtractorUnitProgress(topic.getName(), Utilities
                            .getDatedMessage("Changing to API Key <" + apiKeysIndex + ">."));
                } else {
                    progressWall.setExtractorUnitProgress(
                            topic.getName(),
                            Utilities.getDatedMessage("API Rate Exceeded. Changing to API Key <"
                                    + apiKeysIndex + ">."));
                }
                // Changing to another API Key
                twitterGateway.configure((apiKeys.get(apiKeysIndex)));

                // Changing to another api key
                continue;
            }
            if (statusList != null && statusList.size() > 0) {

                for (Status status : statusList) {
                    lastId = status.getId();
                    topic.setUpperLimit(lastId);
                    log.debug("Element to add: " + TwitterObjectFactory.getRawJSON(status));
                    count++;
                    if (count > maxStatuses) {
                        break;
                    }
                    mongoDatastoreOperations.insertOriginalStatus(TwitterObjectFactory
                            .getRawJSON(status));
                }
            }
            log.trace("Loop #:" + loop);
            progressWall.setExtractorUnitProgress(
                    topic.getName(),
                    Utilities.getDatedMessage("Extraction in progress. Loop #" + loop
                            + "," + count + " Statuses"));
            loop++;
        } while (count < maxStatuses);

        topic.getTopicStatus().setDatabaseStatus(mongoDatastoreOperations.getMongoStatus());
        progressWall.removeExtractorUnitProgress(topic.getName());
        topic.getTopicStatus().setExtractorStatus(
                Utilities.getDatedMessage("Extraction of this topic has finished."));

        // MFC END

    }
    
*/
            msg = "Extraction of this topic has finished.";
            log.info(msg);
            execution.appendLog(msg);
            execution.setStatus(ExecutionStatus.Stopped);
            execution = executionService.updateExecution(execution);
        } catch (InvalidExecutionException | InvalidTopicException e) {
            msg = "An error ocurred while manipulating execution <" + id + ">. Execution aborted.";
            log.info(msg, e);
            return;
        }
    }
}