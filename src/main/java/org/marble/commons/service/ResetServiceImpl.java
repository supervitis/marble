package org.marble.commons.service;


import org.marble.commons.dao.ConfigurationItemDao;
import org.marble.commons.dao.TopicDao;
import org.marble.commons.dao.TwitterApiKeyDao;
import org.marble.commons.dao.model.ConfigurationItem;
import org.marble.commons.dao.model.Topic;
import org.marble.commons.dao.model.TwitterApiKey;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class ResetServiceImpl implements ResetService {
	private static final Logger log =  LoggerFactory.getLogger(ResetServiceImpl.class);
	
	@Autowired
	ConfigurationItemDao configurationItemDao;
	@Autowired
	TwitterApiKeyDao twitterApiKeyDao;
	@Autowired
	TopicDao topicDao;

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
		for (int i = 0; i < twitterApiKeys.length; i = i + 4) {
			
			TwitterApiKey twitterApiKey = new TwitterApiKey();
			twitterApiKey.setConsumerKey(twitterApiKeys[i]);
			twitterApiKey.setConsumerSecret(twitterApiKeys[i + 1]);
			twitterApiKey.setAccessToken(twitterApiKeys[i + 2]);
			twitterApiKey.setAccessTokenSecret(twitterApiKeys[i + 3]);
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

}
