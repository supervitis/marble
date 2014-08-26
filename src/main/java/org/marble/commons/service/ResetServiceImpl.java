package org.marble.commons.service;

import java.util.List;

import org.marble.commons.dao.ConfigurationItemDao;
import org.marble.commons.dao.ExecutionDao;
import org.marble.commons.dao.TopicDao;
import org.marble.commons.dao.TwitterApiKeyDao;
import org.marble.commons.dao.model.ConfigurationItem;
import org.marble.commons.dao.model.Execution;
import org.marble.commons.dao.model.ExecutionStatus;
import org.marble.commons.dao.model.Topic;
import org.marble.commons.dao.model.TwitterApiKey;
import org.marble.commons.thread.Executor;
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
    public Integer getTheSpecial() {

        // Special function to perform special operations ;)
        log.info("Running \"The Special\"...");

        Execution execution = new Execution();

        List<Topic> topics = topicDao.findAll();
        Topic topic = topics.get(0);
        log.info("Topic selected: " + topic.getId());

        execution.setStatus(ExecutionStatus.created);
        execution.setLog("Hola\nEste es\nUn log\n");
        topic.getExecutions().add(execution);
        execution.setTopic(topic);
        topic = topicDao.save(topic);
        execution = executionDao.save(execution);

        log.info("Starting execution <" + execution.getId() + ">... NOW!");
        Executor executor = (Executor) context.getBean("executorTest");
        executor.setExecution(execution);
        //xecutor.setId(execution.getId());
        taskExecutor.execute(executor);

        log.info("That's it. Have fun!");
        
        return execution.getId();
    }

}
