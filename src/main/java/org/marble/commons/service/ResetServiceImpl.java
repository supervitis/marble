package org.marble.commons.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Random;

import org.marble.commons.dao.ConfigurationItemDao;
import org.marble.commons.dao.ExecutionDao;
import org.marble.commons.dao.TopicDao;
import org.marble.commons.dao.TwitterApiKeyDao;
import org.marble.commons.dao.model.ConfigurationItem;
import org.marble.commons.dao.model.OriginalStatus;
import org.marble.commons.dao.model.Plot;
import org.marble.commons.dao.model.Topic;
import org.marble.commons.dao.model.TwitterApiKey;
import org.marble.commons.exception.InvalidPlotException;

import org.apache.commons.lang.math.RandomUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.core.task.TaskExecutor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ResetServiceImpl implements ResetService {
    private static final Logger log = LoggerFactory.getLogger(ResetServiceImpl.class);

    @Autowired
    ConfigurationItemDao configurationItemDao;
    @Autowired
    TwitterApiKeyDao twitterApiKeyDao;
    @Autowired
    TopicDao topicDao;
    @Autowired
    ExecutionDao executionDao;

    @Autowired
    SenticNetService senticNetService;
    @Autowired
    DatastoreService datastoreService;
    @Autowired
    PlotService plotService;

    @Autowired
    private TaskExecutor taskExecutor;
    @Autowired
    private ApplicationContext context;

    @Value("${rebase.configuration:}")
    private String[] configuration;

    @Value("${rebase.twitterApiKeys:}")
    private String[] twitterApiKeys;

    @Value("${rebase.topics:}")
    private String[] topics;

    @Override
    public void resetAll() {
        this.resetConfiguration();
        this.resetTwitterApiKeys();
        this.resetTopics();
    }

    @Override
    public void resetConfiguration() {
        log.info("Reseting ConfigurationItemDAO...");
        configurationItemDao.deleteAll();
        for (int i = 0; i < configuration.length; i = i + 2) {
            ConfigurationItem configurationItem = new ConfigurationItem();
            configurationItem.setName(configuration[i]);
            configurationItem.setValue(configuration[i + 1]);

            configurationItem = configurationItemDao.save(configurationItem);
        }
        log.info("ConfigurationItemDAO reset.");
        return;
    }

    @Override
    public void resetTwitterApiKeys() {
        log.info("Reseting TwitterApiKeyDAO...");
        twitterApiKeyDao.deleteAll();
        for (int i = 0; i < twitterApiKeys.length; i = i + 5) {

            TwitterApiKey twitterApiKey = new TwitterApiKey();
            twitterApiKey.setDescription(twitterApiKeys[i]);
            twitterApiKey.setConsumerKey(twitterApiKeys[i + 1]);
            twitterApiKey.setConsumerSecret(twitterApiKeys[i + 2]);
            twitterApiKey.setAccessToken(twitterApiKeys[i + 3]);
            twitterApiKey.setAccessTokenSecret(twitterApiKeys[i + 4]);
            twitterApiKey.setEnabled(Boolean.TRUE);

            twitterApiKey = twitterApiKeyDao.save(twitterApiKey);
        }
        log.info("TwitterApiKeyDAO reset.");
        return;
    }

    @Override
    public void resetTopics() {
        log.info("Reseting TopicDAO...");
        topicDao.deleteAll();

        for (int i = 0; i < topics.length; i = i + 1) {
            Topic topic = new Topic();
            topic.setName(topics[i]);
            topic.setKeywords(topics[i]);

            topic = topicDao.save(topic);
        }
        log.info("TopicDAO reset.");
        return;
    }

    @Override
    @Transactional
    public void getTheSpecial() {

        // Special function to perform special operations ;)
        log.info("Running \"The Special\"...");

        log.info("Creating plot...");
        Plot plot = new Plot();
        plot.setName("My_Plot_" + RandomUtils.nextInt());
        Map<String, Object> map = new HashMap<>();
        map.put("key1", "value1");
        map.put("key2", 2);
        plot.setMainOptions(map);
        plot.setTopic(topicDao.findOne(4));

        List<Map<String, Object>> mainPlotData = new ArrayList<Map<String, Object>>();

        for (Integer i = 0; i < 1; i++) {
            Map<String, Object> itemData = new HashMap<String, Object>();

            // Generate data set
            List<List<Double>> data = new ArrayList<>();
            for (Double j = 0D; j < 20; j++) {
                List<Double> bar = new ArrayList<>();
                bar.add(j);
                bar.add(Math.sin(j));
                data.add(bar);
            }

            itemData.put("data", data);
            itemData.put("label", "Sin");
            itemData.put("color", "#000000");

            mainPlotData.add(itemData);
        }
        plot.setData(mainPlotData);

        Map<String, Object> mainOptions = new HashMap<>();

        Map<String, Object> bars = new HashMap<>();
        bars.put("show", true);
        bars.put("barWidth", 0.5);
        bars.put("fill", 0.9);
        mainOptions.put("bars", bars);

        Map<String, Object> xaxis = new HashMap<>();
        bars.put("ticks", new ArrayList<String>());
        bars.put("autoscaleMargin", 0.02);
        mainOptions.put("xaxis", xaxis);

        Map<String, Object> yaxis = new HashMap<>();
        bars.put("min", -2);
        bars.put("max", 2);
        mainOptions.put("yaxis", yaxis);
        
        Map<String, Object> grid = new HashMap<>();
        List<Map<String, Object>> markings = new ArrayList<>();
        Map<String, Object> markingItem;
        Map<String, Object> axisItem;

        markingItem = new HashMap<>();
        markingItem.put("color", "#F6F6F6");
        axisItem = new HashMap<>();
        axisItem.put("from", 1);
        markingItem.put("yaxis", axisItem);
        markings.add(markingItem);

        markingItem = new HashMap<>();
        markingItem.put("color", "#F6F6F6");
        axisItem = new HashMap<>();
        axisItem.put("to", -1);
        markingItem.put("yaxis", axisItem);
        markings.add(markingItem);

        markingItem = new HashMap<>();
        markingItem.put("color", "#F6F6F6");
        markingItem.put("lineWidth", 1);
        axisItem = new HashMap<>();
        axisItem.put("from", 2);
        axisItem.put("to", 2);
        markingItem.put("xaxis", axisItem);
        markings.add(markingItem);

        markingItem = new HashMap<>();
        markingItem.put("color", "#F6F6F6");
        markingItem.put("lineWidth", 1);
        axisItem = new HashMap<>();
        axisItem.put("from", 8);
        axisItem.put("to", 8);
        markingItem.put("xaxis", axisItem);
        markings.add(markingItem);

        grid.put("markings", markings);
        mainOptions.put("grid", grid);

        plot.setMainOptions(mainOptions);
        /*
         * var markings = [
         * { color: "#f6f6f6", yaxis: { from: 1 } },
         * { color: "#f6f6f6", yaxis: { to: -1 } },
         * { color: "#000", lineWidth: 1, xaxis: { from: 2, to: 2 } },
         * { color: "#000", lineWidth: 1, xaxis: { from: 8, to: 8 } }
         * ];
         * 
         * $("#plot-placeholder").plot(data, {
         * bars: { show: true, barWidth: 0.5, fill: 0.9 },
         * xaxis: { ticks: [], autoscaleMargin: 0.02 },
         * yaxis: { min: -2, max: 2 },
         * grid: { markings: markings }
         */

        try {
            plotService.save(plot);
        } catch (InvalidPlotException e) {
            log.error("Couldn't create the plot.");
        }

        Float pol = senticNetService.getPolarity("wonderment");
        log.info("Wonderment: <" + pol + ">");
        pol = senticNetService.getPolarity("asdasdsa");
        log.info("Wonderment: <" + pol + ">");

        List<OriginalStatus> statuses = datastoreService.findByTopicId(1, OriginalStatus.class);
        log.info("Statuses count is <" + statuses.size() + ">");

        log.info("That's it. Have fun!");

        return;
    }
}
