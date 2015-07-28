package org.marble.commons.executor.streaming;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import org.marble.commons.dao.model.Execution;
import org.marble.commons.dao.model.OriginalStatus;
import org.marble.commons.dao.model.StreamingStatus;
import org.marble.commons.dao.model.StreamingTopic;
import org.marble.commons.exception.InvalidExecutionException;
import org.marble.commons.exception.InvalidStreamingTopicException;
import org.marble.commons.executor.extractor.TwitterExtractionExecutor;
import org.marble.commons.service.DatastoreService;
import org.marble.commons.service.ExecutionService;
import org.marble.commons.service.ExecutionServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;
import com.mongodb.util.JSON;

import twitter4j.GeoLocation;
import twitter4j.JSONObject;
import twitter4j.StallWarning;
import twitter4j.Status;
import twitter4j.StatusDeletionNotice;
import twitter4j.StatusListener;
import twitter4j.Query.Unit;

//TODO: LISTENER DE TEST. AQUI TODAVIA HAY QUE IMPLEMENTAR TODA LA FUNCIONALIDAD PARA CADA STATUS
public class TwitterStreamingListener implements StatusListener {


	ExecutionService executionService;

	DatastoreService datastoreService;

	private boolean stopping = false;
	private Integer streamingTopicId;
	private String keywords;
	private StreamingTopic streamingTopic;
	private static final Logger log = LoggerFactory
			.getLogger(TwitterExtractionExecutor.class);
	private long count;
	Execution execution;


	public TwitterStreamingListener(StreamingTopic streamingTopic,
			Execution execution, DatastoreService datastoreService, ExecutionService executionService) {
		this.streamingTopic = streamingTopic;
		this.keywords = streamingTopic.getKeywords().toLowerCase();
		this.execution = execution;
		this.datastoreService = datastoreService;
		this.streamingTopicId = streamingTopic.getId();
		this.executionService = executionService;
		count = 0;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((keywords == null) ? 0 : keywords.hashCode());
		result = prime
				* result
				+ ((streamingTopicId == null) ? 0 : streamingTopicId.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		TwitterStreamingListener other = (TwitterStreamingListener) obj;
		if (keywords == null) {
			if (other.keywords != null)
				return false;
		} else if (!keywords.equals(other.keywords))
			return false;
		if (streamingTopicId == null) {
			if (other.streamingTopicId != null)
				return false;
		} else if (!streamingTopicId.equals(other.streamingTopicId))
			return false;
		return true;
	}

	public Integer getStreamingTopicId() {
		return streamingTopicId;
	}

	public void setStreamingTopicId(Integer streamingTopicId) {
		this.streamingTopicId = streamingTopicId;
	}

	public long getCount() {
		return count;
	}

	public void setCount(long count) {
		this.count = count;
	}

	public void setKeywords(String keywords) {
		this.keywords = keywords;
	}

	public String getKeywords() {
		return keywords;
	}

	public void onStatus(Status status) {

		String[] kwords = keywords.split(" ");
		String tweetText = status.getText().toLowerCase();
		for (String kword : kwords) {
			if (!tweetText.contains(kword)) {
				return;
			}
		}

		// Filtrar por fecha
		Date statusDate = status.getCreatedAt();
		Date sinceDate = null;
		if (streamingTopic.getSinceDate() != null){
			sinceDate = streamingTopic.getSinceDate();
			if(statusDate.before(sinceDate)){
				return;
			}
		}	
		
		
		Date untilDate = null;
		if (streamingTopic.getUntilDate() != null){
			untilDate = streamingTopic.getUntilDate();
			//Si ya se ha pasado la fecha desactivamos el topic
			if(statusDate.after(untilDate)){
					if(stopping)
						return;
					try {
						executionService.stopStreaming(streamingTopic.getId());
					} catch (InvalidStreamingTopicException e) {
						log.error("InvalidStreaming");
					} catch (InvalidExecutionException e) {
						log.error("InvalidStreaming");
					}
					log.warn("Should be stopped");
				return;
			}else{
				stopping = true;
			}
		}

		Double longitude = streamingTopic.getGeoLongitude();
		Double latitude = streamingTopic.getGeoLatitude();
		Double radius = streamingTopic.getGeoRadius();
		Unit unit = streamingTopic.getGeoUnit();

		if (longitude != null && latitude != null && radius != null && unit != null) {
			GeoLocation tweetGeo = status.getGeoLocation();
			if(tweetGeo == null){
				return;
			}
			else{
				double R = 6371; //Radio de la Tierra				
				double tweetLat  = tweetGeo.getLatitude() * Math.PI / 180;//Latitud en radianes
				double tweetLng  = tweetGeo.getLongitude()* Math.PI / 180;//Longitud en radianes
				double centerLat = latitude.doubleValue() * Math.PI / 180;//Latitud en radianes
				double centerLng = longitude.doubleValue()* Math.PI / 180;//Longitud en radianes				
				double dist = Math.acos(Math.sin(tweetLat) * Math.sin(centerLat) 
						+ Math.cos(tweetLat) * Math.cos(centerLat) * Math.cos(tweetLng - centerLng))* R;
				if(unit.equals(Unit.mi)){
					//convertir la distancia a millas
					dist = dist * 0.621371192;
				}
				if(dist > radius){
					return;
				}
				
			}
		}

		//Guardar el Status
		StreamingStatus streamingStatus = new StreamingStatus(status,streamingTopic.getId());
		log.info("Saving tweet: " + streamingStatus.getText());
		try {
			if (datastoreService == null)
				log.error("Data store is null");
			datastoreService.insertStreamingStatus(streamingStatus);
		} catch (Exception e) {
			log.error(e.getMessage());
		}
		
		//Combrobar si se han extraido suficientes Status
		count++;
		long maxStatuses = 200;
		if (streamingTopic.getStatusesPerFullExtraction() != null) {
			maxStatuses = streamingTopic.getStatusesPerFullExtraction();
			if (count > maxStatuses && maxStatuses > 0) {
				try {
					executionService.stopStreaming(streamingTopic.getId());
				} catch (InvalidStreamingTopicException
						| InvalidExecutionException e) {
					log.error(e.getMessage());
				}
			}
		}

	}

	public void onDeletionNotice(StatusDeletionNotice statusDeletionNotice) {

	}

	public void onTrackLimitationNotice(int numberOfLimitedStatuses) {

	}

	public void onScrubGeo(long userId, long upToStatusId) {

	}

	public void onStallWarning(StallWarning warning) {

	}

	public void onException(Exception ex) {

	}

	public StreamingTopic getStreamingTopic() {
		return streamingTopic;
	}

	public void setStreamingTopic(StreamingTopic streamingTopic) {
		this.streamingTopic = streamingTopic;
	}
}