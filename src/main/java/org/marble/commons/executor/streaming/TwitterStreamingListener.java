package org.marble.commons.executor.streaming;

import java.util.ArrayList;
import java.util.Date;

import org.marble.commons.dao.model.Execution;
import org.marble.commons.dao.model.StreamingStatus;
import org.marble.commons.dao.model.StreamingTopic;
import org.marble.commons.exception.InvalidExecutionException;
import org.marble.commons.exception.InvalidStreamingTopicException;
import org.marble.commons.executor.extractor.TwitterExtractionExecutor;
import org.marble.commons.service.DatastoreService;
import org.marble.commons.service.ExecutionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import twitter4j.GeoLocation;
import twitter4j.StallWarning;
import twitter4j.Status;
import twitter4j.StatusDeletionNotice;
import twitter4j.StatusListener;
import twitter4j.Query.Unit;

public class TwitterStreamingListener implements StatusListener {

	ExecutionService executionService;

	DatastoreService datastoreService;
	private boolean failure = false;
	private boolean stopping = false;
	private Integer streamingTopicId;
	private String keywords;
	private ArrayList<double[]> locations;
	private StreamingTopic streamingTopic;
	private static final Logger log = LoggerFactory
			.getLogger(TwitterExtractionExecutor.class);
	private long count;
	Execution execution;

	public TwitterStreamingListener(StreamingTopic streamingTopic,
			Execution execution, DatastoreService datastoreService,
			ExecutionService executionService) {
		this.streamingTopic = streamingTopic;
		this.keywords = streamingTopic.getKeywords().toLowerCase();
		this.execution = execution;
		this.datastoreService = datastoreService;
		this.streamingTopicId = streamingTopic.getId();
		this.executionService = executionService;
		count = 0;
		if ("".equals(keywords)) {
			Double longitude = streamingTopic.getGeoLongitude();
			Double latitude = streamingTopic.getGeoLatitude();
			Double radius = streamingTopic.getGeoRadius();
			Unit unit = streamingTopic.getGeoUnit();
			if (longitude != null && latitude != null && radius != null
					&& unit != null) {
				locations = getSquareAroundPoint(latitude, longitude, radius,
						unit);
			}
		}
	}

	private ArrayList<double[]> getSquareAroundPoint(Double lat, Double lon,
			Double radius, Unit unit) {
		double R = 6371;
		double distance = radius.doubleValue();
		if (unit == Unit.mi) {
			distance = distance * 0.621371192;
		}

		double north = (lat * Math.PI / 180 + distance / R) * 180 / Math.PI;
		if (north > 90)
			north = 90;

		double neast = (lon + (Math.atan2(
				Math.sin(distance / R) * Math.cos(lat * Math.PI / 180),
				Math.cos(distance / R) - Math.sin(lat * Math.PI / 180)
						* Math.sin(lat * Math.PI / 180 + distance / R)))
				* 180 / Math.PI);
		double nwest = (lon + (Math.atan2(
				-Math.sin(distance / R) * Math.cos(lat * Math.PI / 180),
				Math.cos(distance / R) - Math.sin(lat * Math.PI / 180)
						* Math.sin(lat * Math.PI / 180 + distance / R)))
				* 180 / Math.PI);

		double south = (lat * Math.PI / 180 - distance / R) * 180 / Math.PI;
		if (south < -90)
			south = -90;
		double seast = (lon + (Math.atan2(
				Math.sin(distance / R) * Math.cos(lat * Math.PI / 180),
				Math.cos(distance / R) - Math.sin(lat * Math.PI / 180)
						* Math.sin(lat * Math.PI / 180 - distance / R)))
				* 180 / Math.PI);
		double swest = (lon + (Math.atan2(
				-Math.sin(distance / R) * Math.cos(lat * Math.PI / 180),
				Math.cos(distance / R) - Math.sin(lat * Math.PI / 180)
						* Math.sin(lat * Math.PI / 180 - distance / R)))
				* 180 / Math.PI);

		double east = Math.max(seast, neast);
		double west = Math.min(swest, nwest);
		while (east > 180)
			east -= 360;
		while (east <= -180)
			east += 360;
		while (west > 180)
			east -= 360;
		while (west <= -180)
			east += 360;
		ArrayList<double[]> coords = new ArrayList<double[]>();
		double[] southwest = { west, south };
		double[] northeast = { east, north };
		coords.add(southwest);
		coords.add(northeast);
		return coords;
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

		if (!"".equals(keywords)) {
			String[] kwords = keywords.split(" ");
			String tweetText = status.getText().toLowerCase();
			for (String kword : kwords) {
				if (!tweetText.contains(kword)) {
					return;
				}
			}
		}

		if (!"".equals(streamingTopic.getLanguage())) {
			if (!streamingTopic.getLanguage().equals(status.getLang())) {
				return;
			}
		}

		// Filtrar por fecha
		Date statusDate = status.getCreatedAt();
		Date sinceDate = null;
		if (streamingTopic.getSinceDate() != null) {
			sinceDate = streamingTopic.getSinceDate();
			if (statusDate.before(sinceDate)) {
				return;
			}
		}

		Date untilDate = null;
		if (streamingTopic.getUntilDate() != null) {
			untilDate = streamingTopic.getUntilDate();
			// Si ya se ha pasado la fecha desactivamos el topic
			if (statusDate.after(untilDate)) {
				if (stopping)
					return;
				stopping = true;
				try {
					executionService.stopStreaming(streamingTopic.getId());
				} catch (InvalidStreamingTopicException e) {
					log.error("InvalidStreaming");
				} catch (InvalidExecutionException e) {
					log.error("InvalidStreaming");
				}
				return;
			} else {
				stopping = false;
			}
		}

		Double longitude = streamingTopic.getGeoLongitude();
		Double latitude = streamingTopic.getGeoLatitude();
		Double radius = streamingTopic.getGeoRadius();
		Unit unit = streamingTopic.getGeoUnit();

		if (longitude != null && latitude != null && radius != null
				&& unit != null) {
			GeoLocation tweetGeo = status.getGeoLocation();
			if (tweetGeo == null) {
				return;
			} else {
				double R = 6371; // Radio de la Tierra
				double tweetLat = tweetGeo.getLatitude() * Math.PI / 180;// Latitud
																			// en
																			// radianes
				double tweetLng = tweetGeo.getLongitude() * Math.PI / 180;// Longitud
																			// en
																			// radianes
				double centerLat = latitude.doubleValue() * Math.PI / 180;// Latitud
																			// en
																			// radianes
				double centerLng = longitude.doubleValue() * Math.PI / 180;// Longitud
																			// en
																			// radianes
				double dist = Math.acos(Math.sin(tweetLat)
						* Math.sin(centerLat) + Math.cos(tweetLat)
						* Math.cos(centerLat) * Math.cos(tweetLng - centerLng))
						* R;
				if (unit.equals(Unit.mi)) {
					// convertir la distancia a millas
					dist = dist * 0.621371192;
				}
				if (dist > radius) {
					return;
				}

			}
		}

		// Guardar el Status
		if (!stopping) {
			StreamingStatus streamingStatus = new StreamingStatus(status,
					streamingTopic.getId());
			try {
				if (datastoreService == null)
					log.error("Data store is null");
				datastoreService.insertStreamingStatus(streamingStatus);
				failure = false;
			} catch (Exception e) {
				log.error(e.getMessage());
			}

			// Combrobar si se han extraido suficientes Status
			count++;
		}
		long maxStatuses = 200;
		if (streamingTopic.getStatusesPerFullExtraction() != null) {
			maxStatuses = streamingTopic.getStatusesPerFullExtraction();
			if (count > maxStatuses && maxStatuses > 0) {
				if (stopping)
					return;
				stopping = true;
				try {
					executionService.stopStreaming(streamingTopic.getId());
				} catch (InvalidStreamingTopicException e) {
					log.error("InvalidStreaming");
				} catch (InvalidExecutionException e) {
					log.error("InvalidStreaming");
				}

				log.warn("Should be stopped");
				return;
			} else {
				stopping = false;
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
		if (!failure) {
			failure = true;
			executionService.sendMail("Marble Streaming Topic"
					+ streamingTopicId, ex.getMessage(),
					"dani_montalvo34@hotmail.com");
		}
		failure = true;
		executionService.useNextAPIKey();
	}

	public StreamingTopic getStreamingTopic() {
		return streamingTopic;
	}

	public void setStreamingTopic(StreamingTopic streamingTopic) {
		this.streamingTopic = streamingTopic;
	}

	public String getLanguage() {
		return streamingTopic.getLanguage();
	}

	public ArrayList<double[]> getLocation() {
		return locations;
	}

}