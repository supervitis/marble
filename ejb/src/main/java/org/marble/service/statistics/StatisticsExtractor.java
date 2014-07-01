package org.marble.service.statistics;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.inject.Named;

import org.apache.log4j.Logger;
import org.marble.data.TopicRepository;
import org.marble.data.datastore.MongoDatastoreOperations;
import org.marble.model.Topic;
import org.marble.service.statistics.model.IndividualDetail;
import org.marble.service.statistics.model.TopicDetailedAnatomy;
import org.marble.service.statistics.model.TopicGlobalAnatomy;
import org.marble.util.Constants;
import org.marble.util.Utilities;

import com.mongodb.DBCursor;
import com.mongodb.DBObject;

@Stateless
@Named
public class StatisticsExtractor {

    private static DateFormat morrisDateFormatter = new SimpleDateFormat(Constants.MORRIS_FULL_DATE_FORMAT);
    private static SimpleDateFormat tweetsDateformatter = new SimpleDateFormat(Constants.TWEETS_DATE_FORMAT);

    @Inject
    private MongoDatastoreOperations mongoDatastoreOperations;

    @Inject
    private TopicRepository topicRepository;

    @Inject
    private Logger log;

    public String getGlobalTopicStatistics(String topicName) {
        // Get topic from repository
        log.info("Entering getGlobalTopicStatistics function, with arguments <" + topicName + ">");
        Topic topic = topicRepository.getByName(topicName);

        // Get the Data
        TopicGlobalAnatomy topicGlobalAnatomy = this.getGlobalTopicData(topic);

        topicGlobalAnatomy.sortAndTotalDetails();
        topicGlobalAnatomy.trimUsers(30);

        log.info("Exiting getGlobalTopicStatistics function, with arguments <" + topicName + ">");

        return topicGlobalAnatomy.toString();
    }

    public String getDetailedTopicStatistics(String topicName) {
        // Get topic from repository
        log.info("Entering getDetailedTopicStatistics function, with arguments <" + topicName + ">");
        Topic topic = topicRepository.getByName(topicName);

        // Get the Data
        TopicDetailedAnatomy topicDetailedAnatomy = this.getDetailedTopicData(topic);

        log.info("Exiting getDetailedTopicStatistics function, with arguments <" + topicName + ">");

        return topicDetailedAnatomy.toString();
    }

    public TopicGlobalAnatomy getGlobalTopicData(Topic topic) {

        // Get horizontal boundaries
        long leftDateBoundary = 0;
        long rightDateBoundary = 0;

        TopicGlobalAnatomy topicGlobalAnatomy = new TopicGlobalAnatomy();

        Integer stepSize = topic.getPlotterStepSize();

        try {
            leftDateBoundary = getBoundary(topic.getPlotterLeftDateBoundary(), stepSize);
            rightDateBoundary = getBoundary(topic.getPlotterRightDateBoundary(), stepSize);

        } catch (Exception e) {
            // Ignoring
            log.debug("No valid date boundaries were found for this topic.", e);
        }

        mongoDatastoreOperations.setCollection(topic.getName());
        DBCursor cursor = (DBCursor) mongoDatastoreOperations.getProcessedData();

        while (cursor.hasNext()) {
            // Loop Variables
            Long timeStampSlot;
            Date date;

            DBObject item = cursor.next();
            String dateInString = (String) item.get("created_at");

            try {
                // Get timeslot for the current tweet
                date = tweetsDateformatter.parse(dateInString);
                timeStampSlot = (long) Math.floor(date.getTime() / stepSize);
            } catch (ParseException e) {
                continue;
            }

            // Check if date is within boundaries
            if ((leftDateBoundary == 0 || timeStampSlot > leftDateBoundary)
                    && (rightDateBoundary == 0 || timeStampSlot < rightDateBoundary)) {

                // Get tweets origin
                Boolean isRetweet = Boolean.parseBoolean((String) item.get("is_retweet"));

                // Fill up the data
                topicGlobalAnatomy.incrementStatuses();
                if (isRetweet) {
                    topicGlobalAnatomy.incrementRetweets();
                }
                else {
                    topicGlobalAnatomy.incrementOriginals();
                }

                String user = (String) item.get("user");
                if (user != null) {
                    topicGlobalAnatomy.addUser(user);
                }

                String timeZone = (String) item.get("time_zone");
                if (timeZone != null) {
                    topicGlobalAnatomy.addTimeZone(timeZone);
                }

            }
        }
        return topicGlobalAnatomy;

    }

    public TopicDetailedAnatomy getDetailedTopicData(Topic topic) {

        // Get horizontal boundaries
        long leftDateBoundary = 0;
        long rightDateBoundary = 0;

        TopicDetailedAnatomy topicDetailedAnatomy = new TopicDetailedAnatomy();

        Integer stepSize = topic.getPlotterStepSize();
        Double positiveThreshold = topic.getProcessorPositiveBoundary();
        Double negativeThreshold = topic.getProcessorNegativeBoundary();

        try {
            leftDateBoundary = getBoundary(topic.getPlotterLeftDateBoundary(), stepSize);
            rightDateBoundary = getBoundary(topic.getPlotterRightDateBoundary(), stepSize);

        } catch (Exception e) {
            // Ignoring
            log.debug("No valid date boundaries were found for this topic.", e);
        }

        Map<Long, IndividualDetail> dataset = topicDetailedAnatomy.getData();

        mongoDatastoreOperations.setCollection(topic.getName());
        DBCursor cursor = (DBCursor) mongoDatastoreOperations.getProcessedData();

        while (cursor.hasNext()) {
            // Loop Variables
            Long timeStampSlot;
            Date date;
            Double polarity;

            DBObject item = cursor.next();
            String dateInString = (String) item.get("created_at");
            polarity = Double.parseDouble(item.get("polarity").toString());

            try {
                // Get timeslot for the current tweet
                date = tweetsDateformatter.parse(dateInString);
                timeStampSlot = (long) Math.floor(date.getTime() / stepSize);
            } catch (ParseException e) {
                continue;
            }

            // Check if date is within boundaries
            if ((leftDateBoundary == 0 || timeStampSlot > leftDateBoundary)
                    && (rightDateBoundary == 0 || timeStampSlot < rightDateBoundary)) {

                // Check date slot and creating it if needed for the first map
                if (!dataset.containsKey(timeStampSlot)) {
                    dataset.put(timeStampSlot, new IndividualDetail());
                }

                // Get tweets origin
                Boolean isRetweet = Boolean.parseBoolean((String) item.get("is_retweet"));

                dataset.get(timeStampSlot).incrementStatuses();

                if (isRetweet) {
                    dataset.get(timeStampSlot).incrementRetweets();
                }
                else {
                    dataset.get(timeStampSlot).incrementOriginals();
                }

                if (polarity > positiveThreshold) {
                    dataset.get(timeStampSlot).incrementPositiveStatuses();
                } else if (polarity < negativeThreshold) {
                    dataset.get(timeStampSlot).incrementNegativeStatuses();
                }

                dataset.get(timeStampSlot).addToAveragePolarity(polarity);

                String user = (String) item.get("user");
                if (user != null) {
                    dataset.get(timeStampSlot).addUser(user);
                }

                String timeZone = (String) item.get("time_zone");
                if (timeZone != null) {
                    dataset.get(timeStampSlot).addUser(timeZone);
                }
            }
        }

        topicDetailedAnatomy.sortAndTotalDetails(stepSize);

        return topicDetailedAnatomy;

    }

    private static Long getBoundary(String boundary, Integer stepSize) throws ParseException {
        Date dDateBoundary = morrisDateFormatter.parse(boundary);
        long dateBoundary = dDateBoundary.getTime() / stepSize;
        return dateBoundary;
    }
}
