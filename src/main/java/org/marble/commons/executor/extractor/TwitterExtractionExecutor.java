package org.marble.commons.executor.extractor;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;

import org.marble.commons.dao.model.Execution;
import org.marble.commons.dao.model.OriginalStatus;
import org.marble.commons.dao.model.Topic;
import org.marble.commons.dao.model.TwitterApiKey;
import org.marble.commons.exception.InvalidExecutionException;
import org.marble.commons.model.ExecutionStatus;
import org.marble.commons.service.DatastoreService;
import org.marble.commons.service.ExecutionService;
import org.marble.commons.service.TopicService;
import org.marble.commons.service.TwitterApiKeyService;
import org.marble.commons.service.TwitterSearchService;
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

    @Autowired
    DatastoreService datastoreService;

    DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");

    Execution execution;

    @Autowired
    TwitterSearchService twitterSearchService;

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

            Integer id = execution.getId();

            msg = "Starting twitter extraction <" + id + ">.";
            log.info(msg);
            execution.appendLog(msg);

            // Changing execution state
            execution.setStatus(ExecutionStatus.Running);
            execution = executionService.save(execution);

            // Get the associated topic
            Topic topic = topicService.findOne(execution.getTopic().getId());

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
            String keyword = topic.getKeywords();

            twitterSearchService.configure(apiKeys.get(apiKeysIndex));

            msg = "Extraction will begin with Api Key <" + apiKeys.get(apiKeysIndex).getDescription() + ">";
            log.info(msg);
            execution.appendLog(msg);
            executionService.save(execution);

            long lastId = 0;
            if (topic.getUpperLimit() != null) {
                lastId = topic.getUpperLimit();
            }

            long maxStatuses = 200;
            if (topic.getStatusesPerFullExtraction() != null) {
                maxStatuses = topic.getStatusesPerFullExtraction();
            }

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
                    twitterSearchService.configure((apiKeys.get(apiKeysIndex)));
                    continue;
                }
                if (statusList != null && statusList.size() > 0) {

                    for (Status status : statusList) {
                        lastId = status.getId();
                        log.info("UpperLimit: " + lastId + ", count: " + count + ", maxStatuses: " + maxStatuses);
                        topic.setUpperLimit(lastId);
                        // save
                        OriginalStatus originalStatus = new OriginalStatus(status, topic.getId());
                        datastoreService.insertOriginalStatus(originalStatus);

                        count++;
                        if (count >= maxStatuses) {
                            break;
                        }

                    }

                }
                topicService.save(topic);

                msg = "Statuses extracted so far: <" + count + ">";
                log.info(msg);
                execution.appendLog(msg);
                executionService.save(execution);

            } while (count < maxStatuses);

            msg = "Extraction of this topic has finished.";
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