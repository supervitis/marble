package org.marble.service.extractors;

import java.util.List;

import javax.ejb.Asynchronous;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;

import org.apache.log4j.Logger;
import org.marble.service.ProgressWall;
import org.marble.service.extractors.TwitterGateway;
import org.marble.util.Utilities;
import org.marble.data.TwitterApiKeyRepository;
import org.marble.data.datastore.MongoDatastoreOperations;
import org.marble.model.ExecutionCommands;
import org.marble.model.Topic;
import org.marble.model.TwitterApiKey;

import twitter4j.Status;
import twitter4j.TwitterException;
import twitter4j.TwitterObjectFactory;

@Stateless
@Named
public class TwitterExtractor extends Extractor {

    @Inject
    private Logger                   log;

    @Inject
    private EntityManager            em;

    @Inject
    private TwitterApiKeyRepository  twitterApiKeyRepository;

    @Inject
    private MongoDatastoreOperations mongoDatastoreOperations;

    @Inject
    private ProgressWall             progressWall;

    @Asynchronous
    public void searchAndLoadStatuses(Topic topic) {

        // Get the managed topic from the persistence store to update status
        topic = em.merge(topic);

        // Change status of execution
        topic.getTopicStatus().setExtractorStatus(
                Utilities.getDatedMessage("Extraction of this topic started."));
        progressWall.setExtractorUnitProgress(topic.getName(),
                Utilities.getDatedMessage("Extraction of this topic started."));

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
    }

}
