package org.marble.commons.executor.plotter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import org.marble.commons.dao.model.Execution;
import org.marble.commons.dao.model.OriginalStatus;
import org.marble.commons.dao.model.Plot;
import org.marble.commons.dao.model.ProcessedStatus;
import org.marble.commons.dao.model.Topic;
import org.marble.commons.exception.InvalidExecutionException;
import org.marble.commons.model.ExecutionStatus;
import org.marble.commons.service.DatastoreService;
import org.marble.commons.service.ExecutionService;
import org.marble.commons.service.PlotService;
import org.marble.commons.service.TopicService;

import com.mongodb.DBCursor;
import com.mongodb.DBObject;

@Component
@Scope("prototype")
public class ProcessedStatusesPlotterExecutor implements PlotterExecutor {

    public static final Map<String, String> availableOperations;

    public static final Map<String, String> availableParameters;
    private static final Logger log = LoggerFactory.getLogger(ProcessedStatusesPlotterExecutor.class);

    static {
        Map<String, String> operations = new TreeMap<>();
        operations.put("plotAllStatuses", "All the extracted statuses by polarity over time.");
        operations.put("plotCreatedStatuses", "Only created statuses by polarity over time.");
        operations.put("plotRetweetedStatuses", "Only retweeted statuses by polarity over time.");
        operations.put("plotAveragePolarity", "The average polarity of statuses over time.");
        operations.put("plotTotalDifference", "The difference in count of positive and negative statuses over time.");
        availableOperations = Collections.unmodifiableMap(operations);
    }

    /*
     * case Constants.PLOTTER_TOTAL_DIFFERENCE:
     * labels.add("(Positive - Negative) Statuses");
     * dataMaps.add(new HashMap<Long, Double>());
     * break;
     * case Constants.PLOTTER_TOTAL_AVERAGE:
     * labels.add("Raw Polarity Average");
     * dataMaps.add(new HashMap<Long, Double>());
     * dataMaps.add(new HashMap<Long, Double>());
     * break;
     * case Constants.PLOTTER_TOTAL:
     * labels.add("1. Positive Statuses");
     * labels.add("2. Negative Statuses");
     * case Constants.PLOTTER_ONLY_ORIGINALS:
     * labels.add("1. Positive Statuses (only Originals)");
     * labels.add("2. Negative Statuses (only Originals)");
     * case Constants.PLOTTER_ONLY_RETWEETS:
     * labels.add("1. Positive Statuses (only Retweets)");
     * labels.add("2. Negative Statuses (only Retweets)");
     */

    static {
        Map<String, String> parameters = new TreeMap<>();
        // parameters.put("Param1", "Param 1.");
        availableParameters = Collections.unmodifiableMap(parameters);
    }

    private enum PlotType
    {
        ALL,
        CREATED,
        RETWEETED,
        AVERAGE_POLARITY,
        TOTAL_RATIO
    }

    private enum DataType
    {
        ALL_POSITIVE,
        ALL_NEGATIVE,
        CREATED_POSITIVE,
        CREATED_NEGATIVE,
        RETWEETED_POSITIVE,
        RETWEETED_NEGATIVE,
        AVERAGE_POLARITY,
        RATIO
    }

    @Autowired
    private DatastoreService datastoreService;

    @Autowired
    private ExecutionService executionService;

    @Autowired
    private PlotService plotService;

    @Autowired
    private TopicService topicService;

    private Execution execution;

    private Map<String, String> parameters;

    @Override
    public void setExecution(Execution execution) {
        this.execution = execution;
    }

    @Override
    public Map<String, String> getParameters() {
        return parameters;
    }

    @Override
    public void setParameters(Map<String, String> parameters) {
        this.parameters = parameters;

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

            msg = "Starting plotter <" + id + ">.";
            log.info(msg);
            execution.appendLog(msg);

            // Changing execution state
            execution.setStatus(ExecutionStatus.Running);
            execution = executionService.save(execution);

            // Old invocation style
            // Method method =
            // this.getClass().getMethod(this.execution.getModuleParameters().getOperation());
            // method.invoke(this, new Object[] {});
            switch (this.execution.getModuleParameters().getOperation()) {
            case "plotAllStatuses":
                plot(PlotType.ALL);
                break;
            case "plotCreatedStatuses":
                plot(PlotType.CREATED);
                break;
            case "plotRetweetedStatuses":
                plot(PlotType.RETWEETED);
                break;
            case "plotAveragePolarity":
                plot(PlotType.AVERAGE_POLARITY);
                break;
            case "plotTotalDifference":
                plot(PlotType.TOTAL_RATIO);
                break;
            }

        } catch (Exception e) {
            msg = "An error ocurred while generating plot with execution <" + execution.getId()
                    + ">. Execution aborted.";
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

    private void plot(PlotType plotType) throws InvalidExecutionException {
        String msg = "";
        Topic topic = execution.getTopic();

        log.info("Creating plot...");

        // Here starts the execution
        Plot plot = new Plot();
        plot.setName(execution.getModuleParameters().getName());
        plot.setTopic(topic);

        List<Map<String, Object>> data = new ArrayList<>();

        switch (plotType) {
        case ALL: {
            Map<String, Object> createdData = new HashMap<>();
            createdData.put("data", getStatusesChartData(topic, DataType.ALL_POSITIVE));
            createdData.put("label", "Positive Statuses");
            data.add(createdData);
            Map<String, Object> retweetedData = new HashMap<>();
            retweetedData.put("data", getStatusesChartData(topic, DataType.ALL_NEGATIVE));
            retweetedData.put("label", "Negative Statuses");
            data.add(retweetedData);
        }
            break;
        case CREATED: {
            Map<String, Object> createdData = new HashMap<>();
            createdData.put("data", getStatusesChartData(topic, DataType.CREATED_POSITIVE));
            createdData.put("label", "Positive Created Statuses");
            data.add(createdData);
            Map<String, Object> retweetedData = new HashMap<>();
            retweetedData.put("data", getStatusesChartData(topic, DataType.CREATED_NEGATIVE));
            retweetedData.put("label", "Negative Created Statuses");
            data.add(retweetedData);
        }
            break;
        case RETWEETED: {
            Map<String, Object> createdData = new HashMap<>();
            createdData.put("data", getStatusesChartData(topic, DataType.RETWEETED_POSITIVE));
            createdData.put("label", "Positive Retweeted Statuses");
            data.add(createdData);
            Map<String, Object> retweetedData = new HashMap<>();
            retweetedData.put("data", getStatusesChartData(topic, DataType.RETWEETED_NEGATIVE));
            retweetedData.put("label", "Negative Retweeted Statuses");
            data.add(retweetedData);
        }
            break;
        case AVERAGE_POLARITY: {
            Map<String, Object> createdData = new HashMap<>();
            createdData.put("data", getStatusesChartData(topic, DataType.AVERAGE_POLARITY));
            createdData.put("label", "Average Polarity");
            data.add(createdData);
        }
            break;
        case TOTAL_RATIO:
            Map<String, Object> singleData = new HashMap<>();
            singleData.put("data", getCalculatedChartData(topic, DataType.RATIO));
            singleData.put("label", "Positive Negative Ratio");
            data.add(singleData);
            break;
        }

        plot.setData(data);
        plot.setMainOptions(getMainOptions(topic));
        plot.setExecution(execution);
        execution.setPlot(plot);

        try {
            execution = executionService.save(execution);
        } catch (Exception e) {
            msg = "Couldn't create the plot. Aborting the operation. The data length is <" + plot.getData().length()
                    + ">.";
            log.error(msg, e);
            execution.appendLog(msg);
            execution.setStatus(ExecutionStatus.Aborted);
            execution.setPlot(null);
            execution = executionService.save(execution);
            throw new InvalidExecutionException();
        }

        // Here finishes the execution
        msg = "Plot generation has finished. The new plot was assigned the id <" + execution.getPlot().getId() + ">.";
        log.info(msg);
        execution.appendLog(msg);
        // execution.setPlot(plot);
        execution.setStatus(ExecutionStatus.Stopped);
        execution = executionService.save(execution);
    }

    private List<List<Double>> getStatusesChartData(Topic topic, DataType dataType) {
        // Get horizontal boundaries
        long leftDateBoundary = 0;
        long rightDateBoundary = 0;

        Long stepSize = topic.getPlotterStepSize();

        try {
            leftDateBoundary = getBoundary(topic.getPlotterLeftDateBoundary(), stepSize);
            rightDateBoundary = getBoundary(topic.getPlotterRightDateBoundary(), stepSize);
        } catch (Exception e) {
            // Ignoring
            log.debug("No valid date boundaries were found for this topic.", e);
        }
        Map<Long, Double> dataMap = new HashMap<>();

        // Building the query according to the data type
        Map<String, Object> query = new HashMap<>();
        query.put("topicId", topic.getId());
        switch (dataType) {
        case ALL_POSITIVE: {
            Map<String, Object> attribute = new HashMap<>();
            attribute.put("$gt", topic.getProcessorPositiveBoundary());
            query.put("polarity", attribute);
        }
            break;
        case ALL_NEGATIVE: {
            Map<String, Object> attribute = new HashMap<>();
            attribute.put("$lt", topic.getProcessorNegativeBoundary());
            query.put("polarity", attribute);
        }
            break;
        case CREATED_POSITIVE: {
            Map<String, Object> attribute = new HashMap<>();
            attribute.put("$gt", topic.getProcessorPositiveBoundary());
            query.put("polarity", attribute);
            query.put("isRetweeted", false);
        }
            break;
        case CREATED_NEGATIVE: {
            Map<String, Object> attribute = new HashMap<>();
            attribute.put("$lt", topic.getProcessorNegativeBoundary());
            query.put("polarity", attribute);
            query.put("isRetweeted", false);
        }
            break;
        case RETWEETED_POSITIVE: {
            Map<String, Object> attribute = new HashMap<>();
            attribute.put("$gt", topic.getProcessorPositiveBoundary());
            query.put("polarity", attribute);
            query.put("isRetweeted", true);
        }
            break;
        case RETWEETED_NEGATIVE: {
            Map<String, Object> attribute = new HashMap<>();
            attribute.put("$lt", topic.getProcessorNegativeBoundary());
            query.put("polarity", attribute);
            query.put("isRetweeted", true);
        }
            break;
        default:
            // Should never occur
            return null;
        }

        DBCursor statusesCursor = datastoreService.findCursorByQuery(query, ProcessedStatus.class);

        while (statusesCursor.hasNext()) {
            // Loop Variables
            Long timeStampSlot;
            log.error("MFC: Aqui vou");

            DBObject rawStatus = statusesCursor.next();
            ProcessedStatus status = datastoreService.getConverter().read(ProcessedStatus.class, rawStatus);

            Date createdAt = status.getCreatedAt();
            timeStampSlot = (long) Math.floor(createdAt.getTime() / stepSize);

            // Check if date is within boundaries
            if ((leftDateBoundary == 0 || timeStampSlot > leftDateBoundary)
                    && (rightDateBoundary == 0 || timeStampSlot < rightDateBoundary)) {
                // Check date slot and creating it if needed for the first map
                if (!dataMap.containsKey(timeStampSlot)) {
                    dataMap.put(timeStampSlot, 0D);
                }
                dataMap.put(timeStampSlot, dataMap.get(timeStampSlot) + 1);
            }
        }

        // Ready to ship...
        List<List<Double>> data = convertAndSortDataMap(dataMap, stepSize);
        return data;

    }

    private List<List<Double>> getCalculatedChartData(Topic topic, DataType dataType) {
        // Get horizontal boundaries
        long leftDateBoundary = 0;
        long rightDateBoundary = 0;

        Long stepSize = topic.getPlotterStepSize();

        try {
            leftDateBoundary = getBoundary(topic.getPlotterLeftDateBoundary(), stepSize);
            rightDateBoundary = getBoundary(topic.getPlotterRightDateBoundary(), stepSize);
        } catch (Exception e) {
            // Ignoring
            log.debug("No valid date boundaries were found for this topic.", e);
        }
        Map<Long, List<Float>> auxiliarDataMap = new HashMap<>();

        // Building the query according to the data type
        Map<String, Object> query = new HashMap<>();
        query.put("topicId", topic.getId());
        switch (dataType) {
        case AVERAGE_POLARITY:
        case RATIO:
            break;
        default:
            // Should never occur
            return null;
        }

        DBCursor statusesCursor = datastoreService.findCursorByQuery(query, ProcessedStatus.class);

        while (statusesCursor.hasNext()) {
            // Loop Variables
            Long timeStampSlot;

            DBObject rawStatus = statusesCursor.next();
            ProcessedStatus status = datastoreService.getConverter().read(ProcessedStatus.class, rawStatus);

            Date createdAt = status.getCreatedAt();
            timeStampSlot = (long) Math.floor(createdAt.getTime() / stepSize);

            // Check if date is within boundaries
            if ((leftDateBoundary == 0 || timeStampSlot > leftDateBoundary)
                    && (rightDateBoundary == 0 || timeStampSlot < rightDateBoundary)) {
                // Check date slot and creating it if needed for the first map
                if (!auxiliarDataMap.containsKey(timeStampSlot)) {
                    auxiliarDataMap.put(timeStampSlot, new ArrayList<Float>());
                }
                auxiliarDataMap.get(timeStampSlot).add(status.getPolarity());
            }
        }

        Map<Long, Double> dataMap = new HashMap<>();
        for (Entry<Long, List<Float>> entry : auxiliarDataMap.entrySet()) {
            switch (dataType) {
            case AVERAGE_POLARITY: {
                double average = 0D;
                for (double item: entry.getValue()) {
                    average += item;
                }
                average = average / entry.getValue().size();
                dataMap.put(entry.getKey(), average);
            }
                break;
            case RATIO: {
                double ratio = 0D;
                for (double item: entry.getValue()) {
                    if (item > topic.getProcessorPositiveBoundary()) {
                        ratio++;
                    }
                    else if (item < topic.getProcessorNegativeBoundary()) {
                        ratio--;
                    }
                }
                ratio = ratio / entry.getValue().size();
                dataMap.put(entry.getKey(), ratio);
            }
                break;
            default:
                return null;
            }
            
        }

        // Ready to ship...
        List<List<Double>> data = convertAndSortDataMap(dataMap, stepSize);
        return data;
    }

    private static List<List<Double>> convertAndSortDataMap(Map<Long, Double> hashMap, Long stepSize) {
        List<List<Double>> data = new ArrayList<List<Double>>();
        for (Entry<Long, Double> entry : hashMap.entrySet()) {
            List<Double> item = new ArrayList<Double>();
            item.add((double) (entry.getKey() * stepSize));
            item.add((double) entry.getValue());
            data.add(item);
        }
        Collections.sort(data, sortComparator);

        return data;
    }

    private static Map<String, Object> getMainOptions(Topic topic) {

        Map<String, Object> mainOptions = new HashMap<>();
        Long stepSize = topic.getPlotterStepSize();

        Map<String, Object> tooltipOpts = new HashMap<>();
        Map<String, Object> shifts = new HashMap<>();
        shifts.put("x", -60);
        shifts.put("y", 25);
        tooltipOpts.put("shifts", shifts);
        mainOptions.put("tooltipOpts", tooltipOpts);

        Map<String, Object> xaxis = new HashMap<>();
        xaxis.put("mode", "time");
        xaxis.put("timeformat", "%m/%d %H:%M");
        mainOptions.put("xaxis", xaxis);

        Map<String, Object> yaxis = new HashMap<>();
        xaxis.put("tickDecimals", 3);
        mainOptions.put("yaxis", yaxis);

        Map<String, Object> bars = new HashMap<>();
        bars.put("align", "center");
        bars.put("show", true);
        bars.put("barWidth", stepSize);
        mainOptions.put("bars", bars);

        Map<String, Object> grid = new HashMap<>();
        grid.put("content", "'%s'of%x.1is%y.4");
        grid.put("hoverable", true);
        mainOptions.put("grid", grid);

        Map<String, Object> selection = new HashMap<>();
        grid.put("mode", "x");
        mainOptions.put("selection", selection);

        mainOptions.put("tooltip", true);

        return mainOptions;
    }

    private static Long getBoundary(Date boundary, Long stepSize) {
        long dateBoundary = 0;
        if (boundary != null) {
            dateBoundary = boundary.getTime() / stepSize;
        }
        return dateBoundary;
    }

    private static Comparator<List<Double>> sortComparator = new Comparator<List<Double>>() {
        @Override
        public int compare(List<Double> arg0, List<Double> arg1) {
            return arg0.get(0).compareTo(arg1.get(0));
        }
    };
}
