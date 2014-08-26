/*package org.marble.commons.thread;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.marble.commons.dao.model.Execution;
import org.marble.commons.dao.model.ExecutionStatus;
import org.marble.commons.dao.model.Topic;
import org.marble.commons.dao.model.TwitterApiKey;
import org.marble.commons.exception.InvalidExecutionException;
import org.marble.commons.service.ExecutionService;
import org.marble.commons.service.ResetServiceImpl;
import org.marble.commons.util.MarbleUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope("prototype")
public class TwitterExtractionExecutor implements Executor {

    private static final Logger log = LoggerFactory.getLogger(TwitterExtractionExecutor.class);

    @Autowired
    ExecutionService executionService;

    DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");

    Execution execution;

    Integer id;

    public TwitterExtractionExecutor() {

    }

    @Override
    public void setExecution(Execution execution) {
        this.execution = execution;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    @Override
    public void run() {
        try {
            log.info("On your marks...");
            Thread.sleep(1000);
        } catch (InterruptedException e) {
        }

        try {
            log.info("Executor starting execution <" + id + ">");
            execution = executionService.getExecution(id);
            log.info("Execution of type <" + execution.getType() + ">");
        } catch (InvalidExecutionException e) {
            log.info("Execution <" + id + "> not found. Execution aborted.", e);
            return;
        }

        // Changing execution state
        execution.setStatus(ExecutionStatus.RUNNING);

        // MFC START
        // Get the managed topic from the persistence store to update status
        topic = em.merge(topic);

        // Change status of execution
        topic.getTopicStatus().setExtractorStatus(
                MarbleUtil.getDatedMessage("Extraction of this topic started."));
        progressWall.setExtractorUnitProgress(topic.getName(),
                MarbleUtil.getDatedMessage("Extraction of this topic started."));

        List<TwitterApiKey> apiKeys = twitterApiKeyRepository.getAllOrderedByAccessToken();
        for (TwitterApiKey key : apiKeys) {
            log.debug("Key available: " + key);
        }

        Integer apiKeysCount = apiKeys.size();
        Integer apiKeysIndex = 0;
        TwitterGateway twitterGateway = new TwitterGateway(apiKeys.get(apiKeysIndex));

        String keyword = topic.getKeywords();
        String collection = topic.getName();
        mongoDatastoreOperations.setCollection(collection);

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
}
*/