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
public class OriginalStatusesPlotterExecutor implements PlotterExecutor {

    public static final Map<String, String> availableOperations;

    public static final Map<String, String> availableParameters;
    private static final Logger log = LoggerFactory.getLogger(OriginalStatusesPlotterExecutor.class);

    static {
        Map<String, String> operations = new TreeMap<>();
        operations.put("plotAllStatuses", "Plot all the extracted statuses over time.");
        operations.put("plotCreatedStatuses", "Plot only created statuses over time.");
        operations.put("plotRetweetedStatuses", "Plot only retweeted statuses over time.");
        operations.put("plotCreatedVsRetweetedStatuses", "Plot created and retweeted statuses over time.");
        operations.put("plotUniqueUsers", "Plot unique users over each step in time.");
        availableOperations = Collections.unmodifiableMap(operations);
    }

    static {
        Map<String, String> parameters = new TreeMap<>();
        // parameters.put("Param1", "Param 1.");
        availableParameters = Collections.unmodifiableMap(parameters);
    }

    private enum PlotType
    {
        PLOT_ALL,
        PLOT_CREATED,
        PLOT_RETWEETED,
        PLOT_CREATED_RETWEETED,
        PLOT_UNIQUE_USERS
    }

    private enum DataType
    {
        ALL,
        CREATED,
        RETWEETED,
        UNIQUE_USERS
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
                plot(PlotType.PLOT_ALL);
                break;
            case "plotCreatedStatuses":
                plot(PlotType.PLOT_CREATED);
                break;
            case "plotRetweetedStatuses":
                plot(PlotType.PLOT_RETWEETED);
                break;
            case "plotCreatedVsRetweetedStatuses":
                plot(PlotType.PLOT_CREATED_RETWEETED);
                break;
            case "plotUniqueUsers":
                plot(PlotType.PLOT_UNIQUE_USERS);
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
        case PLOT_ALL: {
            Map<String, Object> singleData = new HashMap<>();
            singleData.put("data", getStatusesChartData(topic, DataType.ALL));
            singleData.put("label", "Statuses Count");
            data.add(singleData);
        }
            break;
        case PLOT_CREATED: {
            Map<String, Object> singleData = new HashMap<>();
            singleData.put("data", getStatusesChartData(topic, DataType.CREATED));
            singleData.put("label", "Created Statuses Count");
            data.add(singleData);
        }
            break;
        case PLOT_RETWEETED: {
            Map<String, Object> singleData = new HashMap<>();
            singleData.put("data", getStatusesChartData(topic, DataType.RETWEETED));
            singleData.put("label", "Retweeted Statuses Count");
            data.add(singleData);
        }
            break;
        case PLOT_CREATED_RETWEETED: {
            Map<String, Object> createdData = new HashMap<>();
            createdData.put("data", getStatusesChartData(topic, DataType.CREATED));
            createdData.put("label", "Created Statuses Count");
            data.add(createdData);
            Map<String, Object> retweetedData = new HashMap<>();
            retweetedData.put("data", getStatusesChartData(topic, DataType.RETWEETED));
            retweetedData.put("label", "Retweeted Statuses Count");
            data.add(retweetedData);
        }
            break;
        case PLOT_UNIQUE_USERS:
            Map<String, Object> singleData = new HashMap<>();
            singleData.put("data", getUniquesChartData(topic, DataType.UNIQUE_USERS));
            singleData.put("label", "Unique Users Count");
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
        case ALL:
            break;
        case CREATED: {
            Map<String, Object> attribute = new HashMap<>();
            attribute.put("$exists", false);
            query.put("retweetedStatus", attribute);
        }
            break;
        case RETWEETED: {
            Map<String, Object> attribute = new HashMap<>();
            attribute.put("$exists", true);
            query.put("retweetedStatus", attribute);
        }
            break;
        default:
            // Should never occur
            return null;
        }

        DBCursor statusesCursor = datastoreService.findCursorByQuery(query, OriginalStatus.class);

        while (statusesCursor.hasNext()) {
            // Loop Variables
            Long timeStampSlot;

            DBObject rawStatus = statusesCursor.next();
            OriginalStatus status = datastoreService.getConverter().read(OriginalStatus.class, rawStatus);

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
    
    private List<List<Double>> getUniquesChartData(Topic topic, DataType dataType) {
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
        Map<Long, Set<String>> auxiliarDataMap = new HashMap<>();

        // Building the query according to the data type
        Map<String, Object> query = new HashMap<>();
        query.put("topicId", topic.getId());
        switch (dataType) {
        case UNIQUE_USERS:
            break;
        default:
            // Should never occur
            return null;
        }

        DBCursor statusesCursor = datastoreService.findCursorByQuery(query, OriginalStatus.class);

        while (statusesCursor.hasNext()) {
            // Loop Variables
            Long timeStampSlot;

            DBObject rawStatus = statusesCursor.next();
            OriginalStatus status = datastoreService.getConverter().read(OriginalStatus.class, rawStatus);

            Date createdAt = status.getCreatedAt();
            timeStampSlot = (long) Math.floor(createdAt.getTime() / stepSize);

            // Check if date is within boundaries
            if ((leftDateBoundary == 0 || timeStampSlot > leftDateBoundary)
                    && (rightDateBoundary == 0 || timeStampSlot < rightDateBoundary)) {
                // Check date slot and creating it if needed for the first map
                if (!auxiliarDataMap.containsKey(timeStampSlot)) {
                    auxiliarDataMap.put(timeStampSlot, new HashSet<String>());
                }
                auxiliarDataMap.get(timeStampSlot).add(status.getUser().getScreenName());
            }
        }
        
        Map<Long, Double> dataMap = new HashMap<>();
        for (Entry<Long, Set<String>> entry : auxiliarDataMap.entrySet()) {
            double userCount = entry.getValue().size();
            dataMap.put(entry.getKey(), userCount);
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
