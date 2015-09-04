package org.marble.commons.executor.streaming;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.marble.commons.dao.model.Execution;
import org.marble.commons.dao.model.StreamingTopic;
import org.marble.commons.dao.model.TwitterApiKey;
import org.marble.commons.exception.InvalidExecutionException;
import org.marble.commons.executor.extractor.ExtractorExecutor;
import org.marble.commons.model.ExecutionStatus;
import org.marble.commons.service.DatasetService;
import org.marble.commons.service.DatastoreService;
import org.marble.commons.service.ExecutionService;
import org.marble.commons.service.StreamingTopicService;
import org.marble.commons.service.TwitterApiKeyService;
import org.marble.commons.service.TwitterStreamingService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import twitter4j.FilterQuery;
import twitter4j.StreamController;
import twitter4j.TwitterStream;
import twitter4j.TwitterStreamFactory;
import twitter4j.conf.Configuration;
import twitter4j.conf.ConfigurationBuilder;

@Component
@Scope("singleton")
public class TwitterStreamingExecutor implements StreamingExecutor {

	private static final Logger log = LoggerFactory
			.getLogger(TwitterStreamingExecutor.class);

	@Autowired
	ExecutionService executionService;

	@Autowired
	StreamingTopicService streamingTopicService;

	@Autowired
	DatasetService datasetService;

	@Autowired
	TwitterApiKeyService twitterApiKeyService;

	@Autowired
	DatastoreService datastoreService;

	Execution execution;

	@Autowired
	TwitterStreamingService twitterStreamingService;

	ArrayList<TwitterStreamingListener> listeners;
	TwitterStream twitterStream = null;

	Integer apiKeysIndex = 0;

	StreamController streamController;
	DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
	DateFormat dateOnlyFormat = new SimpleDateFormat("yyyy-MM-dd");

	public void executeStreaming(Execution execution) {

		String msg = "";
		try {
			log.info("Initializing execution...");
			Thread.sleep(1000);
		} catch (InterruptedException e) {
		}

		try {
			Integer id = execution.getId();
			msg = "Starting twitter streaming extraction <" + id + ">.";
			log.info(msg);
			execution.appendLog(msg);

			// Changing execution state
			execution.setStatus(ExecutionStatus.Running);
			execution = executionService.save(execution);

			StreamingTopic streamingTopic = streamingTopicService
					.findOne(execution.getStreamingTopic().getId());
			sendMail("Marble Streaming Topic " + streamingTopic.getId(),msg,streamingTopic.getEmail());
			// Get twitter keys
			List<TwitterApiKey> apiKeys = twitterApiKeyService
					.getEnabledTwitterApiKeys();
			for (TwitterApiKey key : apiKeys) {
				log.info("Key available: " + key);
			}

			Integer apiKeysCount = apiKeys.size();
			if (apiKeysCount == 0) {
				msg = "There are no Api Keys available. Aborting execution.";
				log.info(msg);
				execution.appendLog(msg);
				execution.setStatus(ExecutionStatus.Aborted);
				executionService.save(execution);
				return;
			}

			// Solo se crea el streaming si no existe ya
			if (twitterStream == null) {

				twitterStream = twitterStreamingService.configure(apiKeys
						.get(apiKeysIndex));
			}

			if (listeners == null) {
				listeners = new ArrayList<TwitterStreamingListener>();
			}

			msg = "Extraction will begin with Api Key <"
					+ apiKeys.get(apiKeysIndex).getDescription() + ">";
			log.info(msg);
			execution.appendLog(msg);
			executionService.save(execution);

			FilterQuery query = new FilterQuery();
			TwitterStreamingListener listener = new TwitterStreamingListener(
					streamingTopic, execution, datastoreService,
					executionService);
			twitterStream.shutdown();
			twitterStream.addListener(listener);
			listeners.add(listener);
			String[] languages = getLanguages();
			String[] keywords = getKeywords();
			double[][] locations = getLocations();
	
			log.info("Active Streaming topics: " + keywords.length);
			query = query.language(languages);
			if(keywords.length > 0)
				query = query.track(keywords);
			if(locations.length > 0)
				query = query.locations(locations);
			streamingTopic.setActive(true);
			streamingTopicService.save(streamingTopic);
			twitterStream.filter(query);
			log.info("Thread" + keywords[0] + "finished");
		} catch (Exception e) {
			msg = "An error ocurred while manipulating execution <"
					+ execution.getId() + ">. Execution aborted.";
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

	public void stopStreaming(Execution execution) {

		String msg = "";
		try {
			log.info("Initializing execution...");
			Thread.sleep(1000);
		} catch (InterruptedException e) {
		}

		try {
			Integer id = execution.getId();
			msg = "Stopping twitter streaming extraction <" + id + ">.";
			
			log.info(msg);
			execution.appendLog(msg);

			// Changing execution state
			execution.setStatus(ExecutionStatus.Stopped);
			execution = executionService.save(execution);
			StreamingTopic streamingTopic = streamingTopicService
					.findOne(execution.getStreamingTopic().getId());
			sendMail("Marble Streaming Topic " + streamingTopic.getId(),msg,streamingTopic.getEmail());
			FilterQuery query = new FilterQuery();
			TwitterStreamingListener listener = new TwitterStreamingListener(
					streamingTopic, execution, datastoreService,
					executionService);
			twitterStream.shutdown();
			twitterStream.removeListener(listener);
			listeners.remove(listener);
			if (!listeners.isEmpty()) {
				String[] languages = { streamingTopic.getLanguage() };
				String[] keywords = getKeywords();
				log.info("Active Streaming topics: " + keywords.length);
				query = query.track(keywords).language(languages);
				streamingTopic.setActive(false);
				streamingTopicService.save(streamingTopic);
				twitterStream.filter(query);
			}
			log.info("Thread finished");
		} catch (Exception e) {
			msg = "An error ocurred while manipulating execution <"
					+ execution.getId() + ">. Execution aborted.";
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

	public void useNextAPIKey() {
		twitterStream.shutdown();
		String msg = "";
		List<TwitterApiKey> apiKeys = twitterApiKeyService
				.getEnabledTwitterApiKeys();
		for (TwitterApiKey key : apiKeys) {
			log.info("Key available: " + key);
		}
		
		Integer apiKeysCount = apiKeys.size();
		if (apiKeysCount == 0) {
			msg = "There are no Api Keys available. Aborting execution.";
			// TODO: Para todo el streaming.
		}

		// Usar la siguiente API Key (ciclico)
		apiKeysIndex = (apiKeysIndex + 1) % apiKeysCount;
		twitterStream = twitterStreamingService.configure(apiKeys.get(apiKeysIndex));

		if (listeners == null) {
			listeners = new ArrayList<TwitterStreamingListener>();
		}else{
			for (TwitterStreamingListener listener : listeners){
				twitterStream.addListener(listener);
			}
		}
		FilterQuery query = new FilterQuery();

		String[] languages = getLanguages();
		String[] keywords = getKeywords();
		double[][] locations = getLocations();
		log.info("Active Streaming topics: " + keywords.length);
		query = query.language(languages);
		if(keywords.length > 0)
			query = query.track(keywords);
		if(locations.length > 0)
			query = query.locations(locations);
		twitterStream.filter(query);
		msg = "Updated Api Key <"
				+ apiKeys.get(apiKeysIndex).getDescription() + ">";
		log.info(msg);
	}

	public void setExecution(Execution execution) {
		this.execution = execution;
	}

	public String[] getKeywords() {
		ArrayList<String> keywords = new ArrayList<String>();
		for (TwitterStreamingListener listener : listeners) {
			if(listener.getKeywords() != "")
				keywords.add(listener.getKeywords());

		}
		String[] result = {};
		return keywords.toArray(result);

	}
	
	public String[] getLanguages() {
		ArrayList<String> languages = new ArrayList<String>();
		for (TwitterStreamingListener listener : listeners) {
			if(!"".equals(listener.getLanguage())){
				languages.add(listener.getLanguage());
			}
		}
		String[] result = {};
		return languages.toArray(result);
	}
	
	public double[][] getLocations() {
		ArrayList<double[]> locations = new ArrayList<double[]>();
		for (TwitterStreamingListener listener : listeners) {
			ArrayList<double[]> listenerLocation =  listener.getLocation();
			if(listenerLocation != null){
				locations.addAll(listenerLocation);
			}
		}
		double[][] result = {{}};
		return locations.toArray(result);
	}
	
	public void sendMail(String subject, String msg, String to) {

		// Sender's email ID needs to be mentioned
		String from = "daniel@det.uvigo.es";

		// Assuming you are sending email from localhost
		String host = "jucar.det.uvigo.es";

		// Get system properties
		Properties properties = System.getProperties();

		// Setup mail server
		properties.setProperty("mail.smtp.host", host);
		properties.setProperty("mail.user", "daniel");
		properties.setProperty("mail.password", "36N0010LK");
		// Get the default Session object.
		Session session = Session.getDefaultInstance(properties);

		try {
			// Create a default MimeMessage object.
			MimeMessage message = new MimeMessage(session);

			// Set From: header field of the header.
			message.setFrom(new InternetAddress(from));

			// Set To: header field of the header.
			message.addRecipient(Message.RecipientType.TO, new InternetAddress(
					to));
			message.setSubject(subject);
			message.setText(msg);

			// Send message
			Transport.send(message);

		} catch (MessagingException mex) {
			mex.printStackTrace();
		}
	}
}
