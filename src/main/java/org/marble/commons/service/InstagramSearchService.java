package org.marble.commons.service;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.List;

import org.marble.commons.dao.model.InstagramStatus;
import org.marble.commons.exception.EmptyInstagramResponseException;

import twitter4j.JSONException;
import twitter4j.Query.Unit;

public interface InstagramSearchService {

    public void configure(String instagramAccessToken);

    public void unconfigure();

	public List<InstagramStatus> search(Integer instagramTopicId, Double latitude, Double longitude, Double radius, Unit unit,Long maxDate) throws EmptyInstagramResponseException, JSONException, MalformedURLException, IOException;
}
