package org.marble.service.plotters;

import java.lang.reflect.Type;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeMap;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.inject.Named;

import org.apache.log4j.Logger;
import org.marble.data.TopicRepository;
import org.marble.data.datastore.MongoDatastoreOperations;
import org.marble.model.FlotChart;
import org.marble.model.Topic;
import org.marble.util.Constants;
import org.marble.util.Utilities;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;

@Stateless
@Named
public class Histogram {

    private static DateFormat morrisDateFormatter = new SimpleDateFormat(Constants.MORRIS_FULL_DATE_FORMAT);
    private static SimpleDateFormat tweetsDateformatter = new SimpleDateFormat(Constants.TWEETS_DATE_FORMAT);

    @Inject
    private MongoDatastoreOperations mongoDatastoreOperations;

    @Inject
    private TopicRepository topicRepository;

    @Inject
    private Logger log;

    public String getFlotStatusesChart(String topicName, String statusTypes) {
        // Get topic from repository
        log.info("Entering getFlotStatusesChart function, with arguments <" + topicName + "> and <" + statusTypes + ">");
        Topic topic = topicRepository.getByName(topicName);

        // Basic properties of the Chart
        FlotChart flotChart = new FlotChart();

        // Get Json definitions for Main Chart Options
        flotChart.setMainOptions(getMainOptions(topic.getPlotterStepSize()));

        // Get Json definitions for Overview Chart Options
        flotChart.setOverviewOptions(getOverviewChartOptionsJson());

        // Define the colors of the bars
        List<String> colors = new ArrayList<String>();
        colors.add("green");
        colors.add("red");

        // Get the Data
        List<Map<String, Object>> dataset = new ArrayList<Map<String, Object>>();
        Map<String, List<List<Double>>> rawData = this.getStatusesChartData(topic, statusTypes);
        for (Entry<String, List<List<Double>>> datum : rawData.entrySet()) {
            // log.info("data: " + datum.getValue());
            Map<String, Object> chartData = new HashMap<String, Object>();
            chartData.put("data", datum.getValue());
            chartData.put("label", datum.getKey());
            // Add the color
            chartData.put("color", colors.remove(0));
            dataset.add(chartData);
        }
        flotChart.setData(dataset);

        log.info("Exiting getFlotStatusesChart function, with arguments <" + topicName + "> and <" + statusTypes + ">");
        return flotChart.toString();
    }

    public String getFlotPolaritiesChart(String topicName, String statusTypes) {
        // Get topic from repository
        log.info("Entering getFlotPolaritiesChart function, with arguments <" + topicName + "> and <" + statusTypes
                + ">");
        Topic topic = topicRepository.getByName(topicName);

        // Basic properties of the Chart
        FlotChart flotChart = new FlotChart();

        // Get Json definitions for Main Chart Options
        flotChart.setMainOptions(getMainOptions(topic.getPlotterStepSize()));

        // Get Json definitions for Overview Chart Options
        flotChart.setOverviewOptions(getOverviewChartOptionsJson());

        // Define the colors of the bars
        List<String> colors = new ArrayList<String>();
        colors.add("green");
        colors.add("red");

        // Get the Data
        List<Map<String, Object>> dataset = new ArrayList<Map<String, Object>>();
        Map<String, List<List<Double>>> rawData = this.getPolaritiesChartData(topic, statusTypes);
        for (Entry<String, List<List<Double>>> datum : rawData.entrySet()) {
            // log.info("data: " + datum.getValue());
            Map<String, Object> chartData = new HashMap<String, Object>();
            chartData.put("data", datum.getValue());
            chartData.put("label", datum.getKey());
            // Add the color
            chartData.put("color", colors.remove(0));
            dataset.add(chartData);
        }
        flotChart.setData(dataset);

        log.info("Exiting getFlotPolaritiesChart function, with arguments <" + topicName + "> and <" + statusTypes
                + ">");
        return flotChart.toString();
    }

    public Map<String, List<List<Double>>> getStatusesChartData(Topic topic, String statusTypes) {

        // Get horizontal boundaries
        long leftDateBoundary = 0;
        long rightDateBoundary = 0;

        Integer stepSize = topic.getPlotterStepSize();

        try {
            leftDateBoundary = getBoundary(topic.getPlotterLeftDateBoundary(), stepSize);
            rightDateBoundary = getBoundary(topic.getPlotterRightDateBoundary(), stepSize);

        } catch (Exception e) {
            // Ignoring
            log.debug("No valid date boundaries were found for this topic.", e);
        }

        List<HashMap<Long, Double>> dataMaps = new ArrayList<HashMap<Long, Double>>();

        // How much maps do we need?
        List<String> labels = new ArrayList<String>();
        switch (statusTypes) {
        case Constants.PLOTTER_TOTAL:
            // Only one is needed
            labels.add("Total Statuses (originals and retweets)");
            dataMaps.add(new HashMap<Long, Double>());
            break;
        case Constants.PLOTTER_ONLY_ORIGINALS:
            // Only one is needed
            labels.add("Original Statuses (No retweets)");
            dataMaps.add(new HashMap<Long, Double>());
            break;
        case Constants.PLOTTER_ONLY_RETWEETS:
            // Only one is needed
            labels.add("Retweet Statuses (No originals)");
            dataMaps.add(new HashMap<Long, Double>());
            break;
        case Constants.PLOTTER_BOTH:
            // We need two, one for each type
            labels.add("1. Original Statuses");
            dataMaps.add(new HashMap<Long, Double>());
            labels.add("2. Retweet Statuses");
            dataMaps.add(new HashMap<Long, Double>());
            break;
        case Constants.PLOTTER_TOTAL_UNIQUE_USERS:
            labels.add("Unique users per interval");
            dataMaps.add(new HashMap<Long, Double>());
            break;
        }

        mongoDatastoreOperations.setCollection(topic.getName());
        DBCursor cursor = (DBCursor) mongoDatastoreOperations.getProcessedData();

        // The following object will be used only for the UNIQUE_USERS query
        HashMap<Long, Set<String>> uniqueUsers = new HashMap<Long, Set<String>>();

        while (cursor.hasNext()) {
            // Loop Variables
            Long timeStampSlot;
            Date date;

            DBObject item = cursor.next();
            String dateInString = (String) item.get("created_at");
            String user = (String) item.get("user");

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
                for (Integer i = 0; i < dataMaps.size(); i++) {
                    if (!dataMaps.get(i).containsKey(timeStampSlot)) {
                        dataMaps.get(i).put(timeStampSlot, 0D);
                    }
                }

                // Get tweets origin
                Boolean isRetweet = Boolean.parseBoolean((String) item.get("is_retweet"));
                switch (statusTypes) {
                case Constants.PLOTTER_TOTAL:
                    dataMaps.get(0).put(timeStampSlot, dataMaps.get(0).get(timeStampSlot) + 1);
                    break;
                case Constants.PLOTTER_ONLY_ORIGINALS:
                    if (!isRetweet) {
                        dataMaps.get(0).put(timeStampSlot, dataMaps.get(0).get(timeStampSlot) + 1);
                    }
                    break;
                case Constants.PLOTTER_ONLY_RETWEETS:
                    if (isRetweet) {
                        dataMaps.get(0).put(timeStampSlot, dataMaps.get(0).get(timeStampSlot) + 1);
                    }
                    break;
                case Constants.PLOTTER_BOTH:
                    // By Convention: Original -> 0, Retweet -> 1
                    if (!isRetweet) {
                        dataMaps.get(0).put(timeStampSlot, dataMaps.get(0).get(timeStampSlot) + 1);
                    } else {
                        dataMaps.get(1).put(timeStampSlot, dataMaps.get(1).get(timeStampSlot) + 1);
                    }
                    break;

                case Constants.PLOTTER_TOTAL_UNIQUE_USERS:
                    if (!uniqueUsers.containsKey(timeStampSlot)) {
                        uniqueUsers.put(timeStampSlot, new HashSet<String>());
                    }
                    uniqueUsers.get(timeStampSlot).add(user);
                }
            }
        }

        // Special case for UNIQUE_USERS query
        if (statusTypes.equals(Constants.PLOTTER_TOTAL_UNIQUE_USERS)) {
            for (Entry<Long, Double> entry : dataMaps.get(0).entrySet()) {
                int userCount = uniqueUsers.get(entry.getKey()).size();
                dataMaps.get(0).put(entry.getKey(), (double) userCount);
            }
        }
        
        // Ready to ship...
        Map<String, List<List<Double>>> allData = new TreeMap<String, List<List<Double>>>();

        for (Integer i = 0; i < dataMaps.size(); i++) {
            List<List<Double>> data = convertAndSortDataMap(dataMaps.get(i), stepSize);
            allData.put(labels.get(i), data);
        }

        return allData;

    }

    public Map<String, List<List<Double>>> getPolaritiesChartData(Topic topic, String statusTypes) {

        // Get horizontal boundaries
        long leftDateBoundary = 0;
        long rightDateBoundary = 0;

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

        List<HashMap<Long, Double>> dataMaps = new ArrayList<HashMap<Long, Double>>();

        // How much maps do we need?
        List<String> labels = new ArrayList<String>();
        switch (statusTypes) {
        case Constants.PLOTTER_TOTAL_DIFFERENCE:
            labels.add("(Positive - Negative) Statuses");
            dataMaps.add(new HashMap<Long, Double>());
            break;
        case Constants.PLOTTER_TOTAL_AVERAGE:
            labels.add("Raw Polarity Average");
            dataMaps.add(new HashMap<Long, Double>());
            dataMaps.add(new HashMap<Long, Double>());
            break;
        case Constants.PLOTTER_TOTAL:
            labels.add("1. Positive Statuses");
            labels.add("2. Negative Statuses");
        case Constants.PLOTTER_ONLY_ORIGINALS:
            labels.add("1. Positive Statuses (only Originals)");
            labels.add("2. Negative Statuses (only Originals)");
        case Constants.PLOTTER_ONLY_RETWEETS:
            labels.add("1. Positive Statuses (only Retweets)");
            labels.add("2. Negative Statuses (only Retweets)");
        default:
            // case Constants.BOTH:
            // We need two, one for each type
            dataMaps.add(new HashMap<Long, Double>());
            dataMaps.add(new HashMap<Long, Double>());
            break;
        }

        mongoDatastoreOperations.setCollection(topic.getName());
        DBCursor cursor = (DBCursor) mongoDatastoreOperations.getProcessedData();

        Integer k = 0;
        while (cursor.hasNext()) {
            // Loop Variables
            Long timeStampSlot;
            Date date;
            Double polarity;

            // Increment counter
            k++;

            DBObject item = cursor.next();
            String dateInString = (String) item.get("created_at");
            polarity = Double.parseDouble(item.get("polarity").toString());

            try {
                // Get timeslot for the current tweet
                date = tweetsDateformatter.parse(dateInString);
                timeStampSlot = (long) Math.floor(date.getTime() / stepSize);
            } catch (Exception e) {
                log.warn("Exception received for tweet <" + item.get("id_str") + ">");
                continue;
            }

            // Check if date is within boundaries
            if ((leftDateBoundary == 0 || timeStampSlot > leftDateBoundary)
                    && (rightDateBoundary == 0 || timeStampSlot < rightDateBoundary)) {

                // Check date slot and creating it if needed for the first map
                for (Integer i = 0; i < dataMaps.size(); i++) {
                    if (!dataMaps.get(i).containsKey(timeStampSlot)) {
                        dataMaps.get(i).put(timeStampSlot, 0D);
                    }
                }

                // Get tweets origin
                Boolean isRetweet = Boolean.parseBoolean((String) item.get("is_retweet"));
                switch (statusTypes) {

                case Constants.PLOTTER_TOTAL_DIFFERENCE:
                    if (polarity > positiveThreshold) {
                        dataMaps.get(0).put(timeStampSlot, dataMaps.get(0).get(timeStampSlot) + 1);
                    } else if (polarity < negativeThreshold) {
                        dataMaps.get(0).put(timeStampSlot, dataMaps.get(0).get(timeStampSlot) - 1);
                    }
                    break;
                case Constants.PLOTTER_TOTAL_AVERAGE:
                    // This is used to hold the sum
                    dataMaps.get(0).put(timeStampSlot, dataMaps.get(0).get(timeStampSlot) + polarity);
                    // And this to hold the number of measurements
                    dataMaps.get(1).put(timeStampSlot, dataMaps.get(1).get(timeStampSlot) + 1);
                    break;
                // By Convention: Positive -> 0, Negative -> 1
                case Constants.PLOTTER_TOTAL:
                    if (polarity > positiveThreshold) {
                        dataMaps.get(0).put(timeStampSlot, dataMaps.get(0).get(timeStampSlot) + 1);
                    } else if (polarity < negativeThreshold) {
                        dataMaps.get(1).put(timeStampSlot, dataMaps.get(1).get(timeStampSlot) + 1);
                    }
                    break;
                case Constants.PLOTTER_ONLY_ORIGINALS:
                    if (!isRetweet) {
                        if (polarity > positiveThreshold) {
                            dataMaps.get(0).put(timeStampSlot, dataMaps.get(0).get(timeStampSlot) + 1);
                        } else if (polarity < negativeThreshold) {
                            dataMaps.get(1).put(timeStampSlot, dataMaps.get(1).get(timeStampSlot) + 1);
                        }
                    }
                    break;
                case Constants.PLOTTER_ONLY_RETWEETS:
                    if (isRetweet) {
                        if (polarity > positiveThreshold) {
                            dataMaps.get(0).put(timeStampSlot, dataMaps.get(0).get(timeStampSlot) + 1);
                        } else if (polarity < negativeThreshold) {
                            dataMaps.get(1).put(timeStampSlot, dataMaps.get(1).get(timeStampSlot) + 1);
                        }
                    }
                    break;
                }
            }
        }

        // For the Average special case, we need to divide
        if (statusTypes.equals(Constants.PLOTTER_TOTAL_AVERAGE)) {
            for (Entry<Long, Double> entry : dataMaps.get(0).entrySet()) {
                dataMaps.get(0).put(entry.getKey(),
                        dataMaps.get(0).get(entry.getKey()) / dataMaps.get(1).get(entry.getKey()));
            }
            dataMaps.remove(1);
        }

        // Ready to ship...
        Map<String, List<List<Double>>> allData = new TreeMap<String, List<List<Double>>>();

        for (Integer i = 0; i < dataMaps.size(); i++) {
            List<List<Double>> data = convertAndSortDataMap(dataMaps.get(i), stepSize);
            allData.put(labels.get(i), data);
        }
        return allData;

    }

    private static Map<String, Object> getMainOptions(Integer stepSize) {
        Gson gson = new Gson();
        String mainOptionsJson = "{tooltipOpts: {shifts: {y: 25, x: -60}},"
                + " xaxis: {mode: \"time\", timeformat: \"%m/%d %H:%M\"}, yaxis: {tickDecimals:3}, "
                + "bars: {align: \"center\", show: true, barWidth: " + stepSize + "}, "
                + "grid: {content: \"'%s' of %x.1 is %y.4\", hoverable: true}, "
                + "selection: {mode: \"x\"}, tooltip: true}";

        Type listType = new TypeToken<Map<String, Object>>() {
        }.getType();

        Map<String, Object> mainOptions = gson.fromJson(mainOptionsJson, listType);

        return mainOptions;
    }

    private static Map<String, Object> getOverviewChartOptionsJson() {
        Gson gson = new Gson();
        String overviewOptionsJson = "{series: {lines:{show: true, lineWidth: 1}, shadowSize: 0}, "
                + "xaxis: {ticks: [], mode: \"time\"}, yaxis: {ticks: [],  autoscaleMargin: 0.1}, "
                + "selection: { mode: \"x\"}}";

        Type listType = new TypeToken<Map<String, Object>>() {
        }.getType();

        Map<String, Object> overviewOptions = gson.fromJson(overviewOptionsJson, listType);

        return overviewOptions;
    }

    private static List<List<Double>> convertAndSortDataMap(HashMap<Long, Double> hashMap, Integer stepSize) {
        List<List<Double>> data = new ArrayList<List<Double>>();
        for (Entry<Long, Double> entry : hashMap.entrySet()) {
            List<Double> item = new ArrayList<Double>();
            // Map<String, String> item = new HashMap<String, String>();
            item.add((double) (entry.getKey() * stepSize));
            item.add((double) entry.getValue());
            data.add(item);
        }
        Collections.sort(data, Utilities.sortComparator);

        return data;
    }

    private static Long getBoundary(String boundary, Integer stepSize) throws ParseException {
        Date dDateBoundary = morrisDateFormatter.parse(boundary);
        long dateBoundary = dDateBoundary.getTime() / stepSize;
        return dateBoundary;
    }
}
