package org.marble.service.processors;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.ejb.Asynchronous;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;

import org.apache.log4j.Logger;
import org.marble.model.Topic;
import org.marble.service.ProgressWall;
import org.marble.util.Constants;
import org.marble.data.GlobalConfigurationRepository;
import org.marble.data.datastore.MongoDatastoreOperations;

import twitter4j.JSONException;
import twitter4j.JSONObject;

import com.mongodb.BasicDBObject;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;

@Stateless
@Named
public class Controller {

    @Inject
    private BasicProcessor processor;

    @Inject
    private Logger log;

    @Inject
    private EntityManager em;

    @Inject
    private GlobalConfigurationRepository globalConfigurationRepository;

    @Inject
    private ProgressWall progressWall;

    @Inject
    private MongoDatastoreOperations mongoDatastoreOperations;

    @Asynchronous
    public void process(Topic topic) {

        // Get the managed topic from the persistence store to update status
        topic = em.merge(topic);

        // Change status of execution
        DateFormat dateFormat = new SimpleDateFormat(Constants.LONG_DATE_FORMATTER);
        Date date = new Date();
        topic.getTopicStatus().setProcessStatus("Processing of this topic started at " + dateFormat.format(date) + ".");

        progressWall.setProcessorUnitProgress(topic.getName(), Constants.STATUS_RUNNING);

        String collection = topic.getName();

        log.info("Starting the BasicProcessorprocess operation for topic <" + topic.getName() + ">");
        mongoDatastoreOperations.setCollection(collection);
        // Drop current documents
        mongoDatastoreOperations.dropProcessedCollection();
        DBCursor statuses = (DBCursor) mongoDatastoreOperations.getOriginalStatuses();

        processor.setIgnoreNeutralSentences(Boolean.parseBoolean(globalConfigurationRepository
                .getValueByName(Constants.IGNORE_NEUTRAL_SENTENCES)));

        while (statuses.hasNext()) {

            DBObject status = statuses.next();
            if (status.get("created_at") == null) {
                continue;
            }
            String text = (String) status.get("text");
            if (text == null) {
                log.debug("Status text is null. Skipping...");
                continue;
            }
            log.debug("Analysing text: " + text.replaceAll("\n", ""));
            Float polarity = processor.processStatus(text);

            JSONObject processedPolarity = new JSONObject();

            String statusId = "";
            try {
                statusId = (String) status.get("id_str");
                processedPolarity.put("created_at", status.get("created_at"));
                processedPolarity.put("polarity", polarity);
                processedPolarity.put("id_str", statusId);
                processedPolarity.put("text", text);
                if (status.get("user") != null) { 
                    BasicDBObject user  =(BasicDBObject) status.get("user");
                    if (user.get("screen_name") != null) {
                        processedPolarity.put("user", user.get("screen_name"));
                    }
                    if (user.get("time_zone") != null) {
                        processedPolarity.put("time_zone", user.get("time_zone"));
                    }
                }
                if (status.get("retweeted_status") != null) {
                    processedPolarity.put("is_retweet", "true");
                    BasicDBObject retweetedStatus = (BasicDBObject) status.get("retweeted_status");
                    processedPolarity.put("original_created_at", retweetedStatus.get("created_at"));
                }
                log.debug("Total Result: " + processedPolarity.toString().replaceAll("\n", ""));
                mongoDatastoreOperations.insertProcessedPolarity(processedPolarity.toString());
            } catch (JSONException e) {
                log.warn("An JSON exception was caught while processing this status: < " + statusId + ">", e);
            } catch (Exception e) {
                log.warn("An exception was caught while processing this status: < " + statusId + ">", e);
            }
            
        }

        log.info("The BasicProcessor operation for topic <" + topic.getName() + "> has finished.");

        // Change status of execution
        date = new Date();
        topic.getTopicStatus().setProcessStatus(
                "Processing of this topic completed at " + dateFormat.format(date) + ".");
        progressWall.removeProcessorUnitProgress(topic.getName());
    }
}
