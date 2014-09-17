package org.marble.commons.executor.plotter;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

import org.marble.commons.dao.model.Execution;
import org.marble.commons.dao.model.OriginalStatus;
import org.marble.commons.dao.model.Plot;
import org.marble.commons.dao.model.Topic;
import org.marble.commons.exception.InvalidExecutionException;
import org.marble.commons.exception.InvalidPlotException;
import org.marble.commons.model.ExecutionStatus;
import org.marble.commons.service.DatastoreService;
import org.marble.commons.service.ExecutionService;
import org.marble.commons.service.PlotService;
import org.marble.commons.service.TopicService;

import org.apache.commons.lang.math.RandomUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.mongodb.DBCursor;
import com.mongodb.DBObject;

@Component
@Scope("prototype")
public class BasePlotterExecutor implements PlotterExecutor {

    private static final Logger log = LoggerFactory.getLogger(BasePlotterExecutor.class);

    public static final Map<String, String> availableOperations;
    static {
        Map<String, String> operations = new HashMap<>();
        operations.put("plotAllOriginalStatuses", "Plots all the extracted statuses over time.");
        operations.put("plotOriginalVsRetweetedStatuses", "Plots original statuses vs retweeted ones over time.");
        availableOperations = Collections.unmodifiableMap(operations);
    }

    public static final Map<String, String> availableParameters;
    static {
        Map<String, String> parameters = new HashMap<>();
        parameters.put("Param1", "Param 1.");
        parameters.put("Param2", "Param 2.");
        availableParameters = Collections.unmodifiableMap(parameters);
    }

    @Autowired
    ExecutionService executionService;

    @Autowired
    DatastoreService datastoreService;

    @Autowired
    TopicService topicService;

    @Autowired
    PlotService plotService;

    private Execution execution;

    private String operation;

    private Map<String, String> parameters;

    private static Comparator<List<Double>> sortComparator = new Comparator<List<Double>>() {
        @Override
        public int compare(List<Double> arg0, List<Double> arg1) {
            return arg0.get(0).compareTo(arg1.get(0));
        }
    };

    private List<List<Double>> convertAndSortDataMap(HashMap<Long, Double> hashMap, Integer stepSize) {
        List<List<Double>> data = new ArrayList<List<Double>>();
        for (Entry<Long, Double> entry : hashMap.entrySet()) {
            List<Double> item = new ArrayList<Double>();
            // Map<String, String> item = new HashMap<String, String>();
            item.add((double) (entry.getKey() * stepSize));
            item.add((double) entry.getValue());
            data.add(item);
        }
        Collections.sort(data, sortComparator);

        return data;
    }

    private Map<String, Object> getMainOptions(Integer stepSize) {

        Map<String, Object> mainOptions = new HashMap<>();

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

    @Override
    public String getOperation() {
        return this.operation;
    }

    /*
     * private static Long getBoundary(String boundary, Integer stepSize) throws
     * ParseException {
     * Date dDateBoundary = morrisDateFormatter.parse(boundary);
     * long dateBoundary = dDateBoundary.getTime() / stepSize;
     * return dateBoundary;
     * }
     */

    public List<Map<String, Object>> getStatusesChartData(Topic topic) {
        // Get horizontal boundaries
        long leftDateBoundary = 0;
        long rightDateBoundary = 0;

        Integer stepSize = topic.getPlotterStepSize();
        stepSize = 1000;

        try {
            // leftDateBoundary =
            // getBoundary(topic.getPlotterLeftDateBoundary(), stepSize);
            // rightDateBoundary =
            // getBoundary(topic.getPlotterRightDateBoundary(), stepSize);

        } catch (Exception e) {
            // Ignoring
            log.debug("No valid date boundaries were found for this topic.", e);
        }

        List<HashMap<Long, Double>> dataMaps = new ArrayList<HashMap<Long, Double>>();

        // How much maps do we need?
        List<String> labels = new ArrayList<String>();

        // We need two, one for each type
        labels.add("1. Original Statuses");
        dataMaps.add(new HashMap<Long, Double>());
        labels.add("2. Retweet Statuses");
        dataMaps.add(new HashMap<Long, Double>());

        DBCursor statusesCursor = datastoreService.findCursorByTopicId(topic.getId(), OriginalStatus.class);

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
                for (Integer i = 0; i < dataMaps.size(); i++) {
                    if (!dataMaps.get(i).containsKey(timeStampSlot)) {
                        dataMaps.get(i).put(timeStampSlot, 0D);
                    }
                }

                // Get tweets origin
                Boolean isRetweet = Boolean.FALSE;
                if (status.getRetweetedStatus() != null) {
                    isRetweet = Boolean.TRUE;
                }

                // By Convention: Original -> 0, Retweet -> 1
                if (!isRetweet) {
                    dataMaps.get(0).put(timeStampSlot, dataMaps.get(0).get(timeStampSlot) + 1);
                } else {
                    dataMaps.get(1).put(timeStampSlot, dataMaps.get(1).get(timeStampSlot) + 1);
                }

            }
        }

        // Ready to ship...
        Map<String, List<List<Double>>> allData = new TreeMap<String, List<List<Double>>>();

        for (Integer i = 0; i < dataMaps.size(); i++) {
            List<List<Double>> data = convertAndSortDataMap(dataMaps.get(i), stepSize);
            allData.put(labels.get(i), data);
        }

        List<Map<String, Object>> finalData = new ArrayList<>();

        for (Entry<String, List<List<Double>>> datum : allData.entrySet()) {
            // log.info("data: " + datum.getValue());
            Map<String, Object> chartData = new HashMap<String, Object>();
            chartData.put("data", datum.getValue());
            chartData.put("label", datum.getKey());
            // Add the color
            // chartData.put("color", colors.remove(0));
            finalData.add(chartData);
        }

        return finalData;

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

            log.info("Creating plot...");

            Topic topic = execution.getTopic();

            // Here starts the execution

            Plot plot = new Plot();
            plot.setName("My_Plot_" + RandomUtils.nextInt());
            plot.setTopic(topic);

            try {
                Method method;
                method = this.getClass().getMethod(this.operation);
                log.error((String) method.invoke(this, new Object[] {}));
            } catch (SecurityException e) {
                log.error("MFC 1", e);
            } catch (NoSuchMethodException e) {
                log.error("MFC 2", e);
            }

            plot.setData(getStatusesChartData(topic));
            plot.setMainOptions(getMainOptions(1));

            try {
                plot = plotService.save(plot);
            } catch (InvalidPlotException e) {
                log.error("Couldn't create the plot.");
            }

            // Here finishes the execution

            msg = "Plot generation has finished. The new plot was assigned the id <" + plot.getId() + ">.";
            log.info(msg);
            execution.appendLog(msg);
            execution.setStatus(ExecutionStatus.Stopped);
            execution = executionService.save(execution);
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

    @Override
    public void setExecution(Execution execution) {
        this.execution = execution;
    }

    @Override
    public void setOperation(String operation) {
        this.operation = operation;
    }

    public String plotAllOriginalStatuses() {
        return "Hola wey!";
    }

    @Override
    public Map<String, String> getParameters() {
        // TODO Auto-generated method stub
        return parameters;
    }

    @Override
    public void setParameters(Map<String, Object> parameters) {
        // TODO Auto-generated method stub

    }
}
