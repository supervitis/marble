package org.marble.commons.executor.streaming;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import org.marble.commons.dao.model.Execution;
import org.marble.commons.dao.model.OriginalStatus;
import org.marble.commons.dao.model.StreamingStatus;
import org.marble.commons.dao.model.StreamingTopic;
import org.marble.commons.exception.InvalidExecutionException;
import org.marble.commons.executor.extractor.TwitterExtractionExecutor;
import org.marble.commons.service.DatastoreService;
import org.marble.commons.service.ExecutionService;
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
	
	
	@Autowired
	ExecutionService executionService;
	

    DatastoreService datastoreService;
	
	
	private String keyword;
	private StreamingTopic streamingTopic;
    private static final Logger log = LoggerFactory.getLogger(TwitterExtractionExecutor.class);
    private long count;
    Execution execution;

    
    DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
    DateFormat dateOnlyFormat = new SimpleDateFormat("yyyy-MM-dd");

	public TwitterStreamingListener(StreamingTopic streamingTopic, Execution execution,DatastoreService datastoreService) {
		this.streamingTopic = streamingTopic;
		this.keyword = streamingTopic.getKeywords();
		this.execution = execution;
		this.datastoreService = datastoreService;
		count = 0;
	}

	
	
	public long getCount() {
		return count;
	}



	public void setCount(long count) {
		this.count = count;
	}



	public void setKeyword(String keyword) {
		this.keyword = keyword;
	}
	


	public String getKeyword() {
		return keyword;
	}



	public void onStatus(Status status) {
        Boolean inRange = true;
        String msg = null;
		if (status.getText().toLowerCase().contains(streamingTopic.getKeywords())) {
			//TODO: FILTRAR LOS TWEETS SEGUN LOS PARÁMETROS DEL TOPIC
			
			/*/No hace falta el lastId porque no vamos a ir hacia atras
			long lastId = 0;
            if (streamingTopic.getUpperLimit() != null) {
                lastId = streamingTopic.getUpperLimit();
            }*/
            
            //Esto supongo que si se puede usar. Hay que hacer que al llegar al máximo se pause el topic
            long maxStatuses = 200;
            if (streamingTopic.getStatusesPerFullExtraction() != null) {
                maxStatuses = streamingTopic.getStatusesPerFullExtraction();
            }
            
            //Filtrar por fecha y por localización
            String sinceDate = null;
            if(streamingTopic.getSinceDate() != null)
            	sinceDate = dateOnlyFormat.format(streamingTopic.getSinceDate());
            
            String untilDate = null;
            if(streamingTopic.getUntilDate() != null)
            	untilDate = dateOnlyFormat.format(streamingTopic.getUntilDate());
            
            Double longitude = streamingTopic.getGeoLongitude();
            Double latitude = streamingTopic.getGeoLatitude();
            Double radius = streamingTopic.getGeoRadius();
            Unit unit = streamingTopic.getGeoUnit();
            
            GeoLocation geoLoc = null;
            if (longitude != null && latitude != null){
            	geoLoc = new GeoLocation(latitude.doubleValue(), longitude.doubleValue());
            }
			//lastId = status.getId();
			
            //log.info("UpperLimit: " + lastId + ", count: " + count + ", maxStatuses: " + maxStatuses);
            //streamingTopic.setUpperLimit(lastId);
            // save
            StreamingStatus streamingStatus = new StreamingStatus(status, streamingTopic.getId());
            /*if(streamingTopic.getLowerLimit() != null && streamingTopic.getLowerLimit() >= streamingStatus.getId()) {
                inRange = false;
                msg = "Reached the lower limit for this topic.";
                log.info(msg);
                execution.appendLog(msg);
                try {
					executionService.save(execution);
				} catch (InvalidExecutionException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
            }*/
            log.info("Saving tweet: " + streamingStatus.getText());
            try{
            	if(datastoreService == null)
            		log.error("Data store is null");
            datastoreService.insertStreamingStatus(streamingStatus);
            }catch(Exception e){
            	log.error(e.getMessage());
            }
            count++;
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